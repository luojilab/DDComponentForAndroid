package com.dd.buildgradle

import com.dd.buildgradle.exten.ComExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class ComBuild implements Plugin<Project> {

    private final String MAINMODULE = "mainmodulename"
    private final String DEDAULT = "app"
    private final String ALLMODULE = "all"
    private final String TAG = "ComBuild --->"
    //默认是app，直接运行assembleRelease的时候，等同于运行app:assembleRelease
    private def compileModule = DEDAULT

    void apply(Project project) {
        project.extensions.create('combuild', ComExtension)

        String taskNames = project.gradle.startParameter.taskNames.toString()
        //类似这种格式[clean, :basiclib:generateDebugSources]
        System.out.println("$TAG taskNames is " + taskNames)
        String module = project.path.replace(":", "")
        System.out.println("$TAG current module is " + module)
        AssembleTask assembleTask = getTaskInfo(project.gradle.startParameter.taskNames)

        if (assembleTask.isAssemble) {
            fetchCompileModuleName(project, assembleTask)
            System.out.println("$TAG compile module  is " + compileModule)
        }

        if (!project.hasProperty("isRunAlone")) {
            throw new RuntimeException("$TAG you should set isRunAlone in " + module + "'s gradle.properties")
        }

        //对于isRunAlone==true的情况需要根据实际情况修改其值，
        // 但如果是false，则不用修改，该module作为一个lib，运行module:assembleRelease则发布aar到中央仓库
        boolean isRunAlone = Boolean.parseBoolean((project.properties.get("isRunAlone")))
        String mainModuleName = project.rootProject.property(MAINMODULE)
        if (isRunAlone && assembleTask.isAssemble) {
            //对于要编译的组件和主项目，isRunAlone修改为true，其他组件都强制修改为false
            //这就意味着组件不能引用主项目，这在层级结构里面也是这么规定的
            isRunAlone = module == compileModule || module == mainModuleName
        }
        project.setProperty("isRunAlone", isRunAlone)

        //根据配置添加各种组件依赖，并且自动化生成组件加载代码
        if (isRunAlone) {
            project.apply plugin: 'com.android.application'
            if (module != mainModuleName) {
                project.android.sourceSets {
                    main {
                        manifest.srcFile 'src/main/runalone/AndroidManifest.xml'
                        java.srcDirs = ['src/main/java', 'src/main/runalone/java']
                        res.srcDirs = ['src/main/res', 'src/main/runalone/res']
                    }
                }
            }
            System.out.println("$TAG apply plugin is " + 'com.android.application')
            if (assembleTask.isAssemble && module == compileModule) {
                compileComponents(assembleTask, project)
                project.android.registerTransform(new ComCodeTransform(project))
            }
        } else {
            project.apply plugin: 'com.android.library'
            System.out.println("$TAG apply plugin is " + 'com.android.library')
            project.afterEvaluate {
                Task assembleReleaseTask = project.tasks.findByPath("assembleRelease")
                if (assembleReleaseTask != null) {
                    assembleReleaseTask.doLast {
                        File infile = project.file("build/outputs/aar/$module-release.aar")
                        File outfile = project.file("../componentrelease")
                        File desFile = project.file("$module-release.aar")
                        project.copy {
                            from infile
                            into outfile
                            rename {
                                String fileName -> desFile.name
                            }
                        }
                        System.out.println("$TAG $module-release.aar copy success ")
                    }
                }
            }
        }
    }

    /**
     * 根据当前的task，获取要运行的组件，规则如下：
     * assembleRelease ---app
     * app:assembleRelease :app:assembleRelease ---app
     * sharecomponent:assembleRelease :sharecomponent:assembleRelease ---sharecomponent
     *
     * @param project 构建项目
     * @param assembleTask 构建任务
     */
    private void fetchCompileModuleName(Project project, AssembleTask assembleTask) {
        if (!project.rootProject.hasProperty(MAINMODULE)) {
            throw new RuntimeException("$TAG you should set compileModule in rootproject's gradle.properties")
        }
        if (assembleTask.modules.size() > 0 && assembleTask.modules.get(0) != null
                && assembleTask.modules.get(0).trim().length() > 0
                && assembleTask.modules.get(0) != ALLMODULE) {
            compileModule = assembleTask.modules.get(0)
        } else {
            compileModule = project.rootProject.property(MAINMODULE)
        }
        if (compileModule == null || compileModule.trim().length() <= 0) {
            compileModule = DEDAULT
        }
    }

    private AssembleTask getTaskInfo(List<String> taskNames) {
        AssembleTask assembleTask = new AssembleTask()
        for (String task : taskNames) {
            if (task.toUpperCase().contains("ASSEMBLE")
                    || task.contains("aR")
                    || task.toUpperCase().contains("RESGUARD")) {
                if (task.toUpperCase().contains("DEBUG")) {
                    assembleTask.isDebug = true
                }
                assembleTask.isAssemble = true
                //task类似这种格式[:app:assembleDebug],所以需要提取第一个冒号后面的名称
                String[] split = task.split(":")
                assembleTask.modules.add(split.length > 1 ? split[split.length - 2] : ALLMODULE)
                break
            }
        }
        return assembleTask
    }

    /**
     * 自动添加依赖，只在运行assemble任务的才会添加依赖，因此在开发期间组件之间是完全感知不到的，这是做到完全隔离的关键
     * 支持两种语法：module或者modulePackage:module,前者直接引用module工程，后者使用'componentrelease'中已经发布的aar
     * @param assembleTask 构建任务
     * @param project 构建项目
     */
    private void compileComponents(AssembleTask assembleTask, Project project) {
        String components
        if (assembleTask.isDebug) {
            components = (String) project.properties.get("debugComponent")
        } else {
            components = (String) project.properties.get("compileComponent")
        }

        if (components == null || components.length() == 0) {
            System.out.println("$TAG there is no add dependencies ")
            return
        }
        String[] compileComponents = components.split(",")
        if (compileComponents == null || compileComponents.length == 0) {
            System.out.println("$TAG there is no add dependencies ")
            return
        }
        for (String component : compileComponents) {
            System.out.println("$TAG add compile component $component")
            //modulePackage:module语法格式，添加依赖aar
            if (component.contains(":")) {
                def aarPath = "../componentrelease/${component.split(":")[1]}-release.aar"
                File file = project.file(aarPath)
                if (file.exists()) {
                    project.dependencies.add("compile", "$aarPath")
                    System.out.println("$TAG add dependencies : $aarPath")
                } else {
                    throw new RuntimeException("$TAG $aarPath not found ! maybe you should generate a new one ")
                }
            } else {
                //module语法格式，添加project依赖
                project.dependencies.add("compile", project.project(":$component"))
                System.out.println("$TAG add dependencies project : $component")
            }
        }
    }
    /**
     * 构建任务信息类
     */
    private class AssembleTask {
        boolean isAssemble = false
        boolean isDebug = false
        List<String> modules = new ArrayList<>()
    }
}