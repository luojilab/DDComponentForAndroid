# URI设计
By Leobert

## 为什么要有这一篇
DDComponent设计的初衷应当是注重于实现组件化，得到的几位大牛也提供了非常优秀的解决方案。但是组件化的代码边界带来了一个问题：UI跳转的时候没那么方便了，以往常见的做法：

* 耦合非常高的直接构造Intent并启动
* 将所需的参数、参数组装等都在目标Activity中进行方法封装，暴露静态方法供调用
* 实现Factory进行Intent的构造、实现启动器

都不行了。所以DDComponent中有了Router部分，使用路由进行UI跳转（通过路由寻找组件的内容本篇略过）。而DDComponent中的UIRouter部分是比较轻量级的，本人也有幸和得到的大牛进行了一定的讨论，对router部分略尽了绵薄之力。但是当前的Router部分使用还是没那么轻松的，而本人也对这些问题进行了思考，幸有所得。鉴于接下来一段时间我都比较忙，现将设计思路记录于此，倘若有同行有兴趣提供一臂自力也是非常感激的。

PS：格竹子君给我开了一个anno分支，本身我命这个名字是Feature/anno 使用注解实现路由定义的功能分支。

## URI的基础格式
> 协议://域名:port/目录a/目录b/文件c?key1=value1&key2=v2#author

除了这些还有query部分，author部分（URL中也有人称hash部分）
而‘目录a/目录b/文件c’这段也有人称为path。

**在当前的实现中，参数传递暂使用Bundle，不支持使用queryString**。

我们可以看到demo中有这样一段：

```
UIRouter.getInstance().openUri(getActivity(), "componentdemo://share", bundle);
```

这里使用的URI尚未经过精心设计，而且直接使用hardcode形式也是不利于维护的（当然这里只是demo，当我们将框架完善后这些问题就解决了）

## 设计构想
### 协议部分
协议部分将忽略，（逻辑中将移除scheme的部分）。


### host部分
我们可以将Component认为是不同的主机，给定不同的域名

### port部分
我有在项目中提过一个issue，intent的FLAG问题，当前是没有支持的，可以使用port来做一些手脚，也可以直接在Router中将方法原型扩展一下。*我还要再细想想哪种好*

### path部分
当前的anno分支实现中，在对path提出了group的概念，可以针对group分一下Router，但是除了看起来有点噱头其实没啥大用，因为路由表都是按照注解自动生成的，最多就是路由器命名的时候贴切一些。

但潜意识告诉我这个group迟早会有用 233333

计划中会按照注解的path生成一些常量，减少hardcode。这个时候group可能会有用哦。

### query部分
有一些朋友提过issue：runalone时的传值问题。参考一些服务端的同事在一些非敏感接口同时兼容了query传值和表单传值的做法，我觉得Router部分兼容query传值和Intent的bundle传值是可取的。

计划中需要支持使用query进行传值

优先级：bundle > queryString
当bundle中获取了相应的数据，即忽略queryString取值查询


