## DDComponent

[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/luojilab/DDComponentForAndroid/pulls)
[![License](https://img.shields.io/badge/License-Apache%202.0-orange.svg)](https://github.com/luojilab/DDComponentForAndroid/blob/master/LICENSE) 

### 实现功能：
- 组件可以单独调试
- 杜绝组件之前相互耦合，代码完全隔离，彻底解耦
- 组件之间通过接口+实现的方式进行数据传输
- 使用scheme和host路由的方式进行activity之间的跳转
- 自动生成路由跳转路由表
- 任意组件可以充当host，集成其他组件进行集成调试
- 可以动态对已集成的组件进行加载和卸载
- 支持kotlin组件

### 使用指南
#### 1、主项目引用编译脚本
在根目录的gradle.properties文件中，增加属性：

```ini
mainmodulename=app
```
其中mainmodulename是项目中的host工程，一般为app

在根目录的build.gradle中增加配置

```gradle
buildscript {
    dependencies {
        classpath 'com.luojilab.ddcomponent:build-gradle:1.2.0'
    }
}
```

为每个组件引入依赖库，如果项目中存在basiclib等基础库，可以统一交给basiclib引入

```gradle
compile 'com.luojilab.ddcomponent:componentlib:1.3.0'
```

#### 2、拆分组件为module工程
在每个组件的工程目录下新建文件gradle.properties文件，增加以下配置：

```ini
isRunAlone=true
debugComponent=sharecomponent
compileComponent=sharecomponent
```
上面三个属性分别对应是否单独调试、debug模式下依赖的组件，release模式下依赖的组件。具体使用方式请解释请参见上文第二篇文章

#### 3、应用组件化编译脚本
在组件和host的build.gradle都增加配置：

```gradle
apply plugin: 'com.dd.comgradle'
```

注意：不需要在引用com.android.application或者com.android.library

同时增加以下extension配置：

```gradle
combuild {
    applicationName = 'com.luojilab.reader.runalone.application.ReaderApplication'
    isRegisterCompoAuto = true
}
```
组件注册还支持反射的方式，有关isRegisterCompoAuto的解释请参见上文第二篇文章

#### 4、混淆
在混淆文件中增加如下配置
```
-keep interface * {
  <methods>;
}
-keep class com.luojilab.component.componentlib.** {*;}
-keep class com.luojilab.router.** {*;}
-keep class com.luojilab.gen.** {*;}
-keep class * implements com.luojilab.component.componentlib.router.ISyringe {*;}
-keep class * implements com.luojilab.component.componentlib.applicationlike.IApplicationLike {*;}
```

关于如何进行组件之间数据交互和UI跳转，请参看 [Wiki](https://github.com/luojilab/DDComponentForAndroid/wiki)


### License

DDComponentForAndroid 使用的 MIT 协议，详细请参考 [LICENSE](https://github.com/luojilab/DDComponentForAndroid/blob/master/LICENSE.md)。
