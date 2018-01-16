package com.dd.buildgradle

import com.dd.buildgradle.exten.ComExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class ComBuild implements Plugin<Project> {

    //默认是app，直接运行assembleRelease的时候，等同于运行app:assembleRelease
    String compilemodule = "app"

    void apply(Project project) {
        /**
         * 每个Gradle的Project都维护了一个ExtenionContainer，
           我们可以通过project.extentions访问额外的Property和定义额外的Property

           弄好之后，在build.gradle中的配置如下面所示的格式
         *      combuild {
                     applicationName = 'com.luojilab.reader.runalone.application.ReaderApplication'
                     isRegisterCompoAuto = false
                }
         */
        project.extensions.create('combuild', ComExtension)

        String taskNames = project.gradle.startParameter.taskNames.toString()
        System.out.println("taskNames is " + taskNames)
        String module = project.path.replace(":", "")
        System.out.println("current module is " + module)
        AssembleTask assembleTask = getTaskInfo(project.gradle.startParameter.taskNames)

        if (assembleTask.isAssemble) {
            fetchMainModuleName(project, assembleTask)
            System.out.println("compilemodule  is " + compilemodule)
        }

        if (!project.hasProperty("isRunAlone")) {
            throw new RuntimeException("you should set isRunAlone in " + module + "'s gradle.properties")
        }

        //对于isRunAlone==true的情况需要根据实际情况修改其值，
        // 但如果是false，则不用修改，该module作为一个lib，运行module:assembleRelease则发布aar到中央仓库
        boolean isRunAlone = Boolean.parseBoolean((project.properties.get("isRunAlone")))
        String mainmodulename = project.rootProject.property("mainmodulename")
        if (isRunAlone && assembleTask.isAssemble) {
            //对于要编译的组件和主项目，isRunAlone修改为true，其他组件都强制修改为false
            //这就意味着组件不能引用主项目，这在层级结构里面也是这么规定的
            if (module.equals(compilemodule) || module.equals(mainmodulename)) {
                isRunAlone = true
            } else {
                isRunAlone = false
            }
        }
        project.setProperty("isRunAlone", isRunAlone)

        //根据配置添加各种组件依赖，并且自动化生成组件加载代码
        if (isRunAlone) {
            //应用plugin为 application
            project.apply plugin: 'com.android.application'
            if (!module.equals(mainmodulename)) {
                project.android.sourceSets {
                    main {
                        manifest.srcFile 'src/main/runalone/AndroidManifest.xml'
                        java.srcDirs = ['src/main/java', 'src/main/runalone/java']
                        res.srcDirs = ['src/main/res', 'src/main/runalone/res']
                    }
                }
            }
            System.out.println("apply plugin is " + 'com.android.application')
            if (assembleTask.isAssemble && module.equals(compilemodule)) {
                //编译组件
                compileComponents(assembleTask, project)
                project.android.registerTransform(new ComCodeTransform(project))
            }
        } else {
            //应用plugin为 library
            project.apply plugin: 'com.android.library'
            System.out.println("apply plugin is " + 'com.android.library')
            project.afterEvaluate {
                Task assembleReleaseTask = project.tasks.findByPath("assembleRelease")
                if (assembleReleaseTask != null) {
                    //每次assembleRelease 执行完之后，将aar copy到 componentrelease 目录
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
                        System.out.println("$module-release.aar copy success ")
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
     * @param assembleTask
     */
    private void fetchMainModuleName(Project project, AssembleTask assembleTask) {
        if (!project.rootProject.hasProperty("mainmodulename")) {
            throw new RuntimeException("you should set compilemodule in rootproject's gradle.properties")
        }
        if (assembleTask.modules.size() > 0 && assembleTask.modules.get(0) != null
                && assembleTask.modules.get(0).trim().length() > 0
                && !assembleTask.modules.get(0).equals("all")) {
            compilemodule = assembleTask.modules.get(0)
        } else {
            compilemodule = project.rootProject.property("mainmodulename")
        }
        if (compilemodule == null || compilemodule.trim().length() <= 0) {
            compilemodule = "app"
        }
    }

    private AssembleTask getTaskInfo(List<String> taskNames) {
        AssembleTask assembleTask = new AssembleTask()
        for (String task : taskNames) {
            //ASSEMBLE打包 、 aR = assembleRelease  、RESGUARD混淆
            if (task.toUpperCase().contains("ASSEMBLE")
                    || task.contains("aR")
                    || task.toUpperCase().contains("RESGUARD")) {
                if (task.toUpperCase().contains("DEBUG")) {
                    assembleTask.isDebug = true
                }
                assembleTask.isAssemble = true
                String[] strs = task.split(":")
                /**
                 * 根据当前的task，获取要运行的组件，规则如下：
                 * assembleRelease ---app
                 * app:assembleRelease :app:assembleRelease ---app
                 * sharecomponent:assembleRelease :sharecomponent:assembleRelease ---sharecomponent
                 */
                assembleTask.modules.add(strs.length > 1 ? strs[strs.length - 2] : "all")
                break
            }
        }
        return assembleTask
    }

    /**
     * 自动添加依赖，只在运行assemble任务的才会添加依赖，因此在开发期间组件之间是完全感知不到的，这是做到完全隔离的关键
     * 支持两种语法：module或者modulePackage:module,前者之间引用module工程，后者使用componentrelease中已经发布的aar
     * @param assembleTask
     * @param project
     */
    private void compileComponents(AssembleTask assembleTask, Project project) {
        /**
         * 从配置文件中读取要依赖的组件名  配置文件格式如下：
             debugComponent=readercomponent,sharecomponent,sharecomponentkotlin
             compileComponent=readercomponent,sharecomponent,sharecomponentkotlin
         */
        String components
        if (assembleTask.isDebug) {
            components = (String) project.properties.get("debugComponent")
        } else {
            components = (String) project.properties.get("compileComponent")
        }
        if (components == null || components.length() == 0) {
            System.out.println("there is no add dependencies ")
            return
        }
        //通过 逗号分隔 转换为组件名数组
        String[] compileComponents = components.split(",")
        if (compileComponents == null || compileComponents.length == 0) {
            System.out.println("there is no add dependencies ")
            return
        }
        for (String str : compileComponents) {
            System.out.println("comp is " + str)
            //如果module格式为componentrelease中已经发布的aar，格式类似为： modulePackage:module
            if (str.contains(":")) {
                /**
                 *  这一段逻辑可以优化为  配置文件中配置具体的版本信息即可
                     debugComponent=readercomponent,com.xtc.comp:sharecomponent:1.0.1,sharecomponentkotlin
                     compileComponent=readercomponent,com.xtc.comp:sharecomponent:1.0.1,sharecomponentkotlin

                     这样就可以直接用  project.dependencies.add("compile", str)
                 */
                File file = project.file("../componentrelease/" + str.split(":")[1] + "-release.aar")
                if (file.exists()) {
                    project.dependencies.add("compile", str + "-release@aar")
                    System.out.println("add dependencies : " + str + "-release@aar")
                } else {
                    throw new RuntimeException(str + " not found ! maybe you should generate a new one ")
                }
            } else {
                //如果为直接引用module工程
                project.dependencies.add("compile", project.project(':' + str))
                System.out.println("add dependencies project : " + str)
            }
        }
    }

    private class AssembleTask {
        boolean isAssemble = false
        boolean isDebug = false
        List<String> modules = new ArrayList<>()
    }

}