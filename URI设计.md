# URI设计
By Leobert

## 为什么要有这一篇
DDComponent设计的初衷应当是注重于实现组件化，得到的几位大牛也提供了非常优秀的解决方案。但是组件化的代码边界带来了一个问题：UI跳转的时候没那么方便了，以往常见的做法：

* 耦合非常高的直接构造Intent并启动
* 将所需的参数、参数组装等都在目标Activity中进行方法封装，暴露静态方法供调用
* 实现Factory进行Intent的构造、实现启动器

都不行了。所以DDComponent中有了Router部分，使用路由进行UI跳转（通过路由寻找组件的内容本篇略过）。而DDComponent中的UIRouter部分是比较轻量级的，本人也有幸和得到的大牛进行了一定的讨论，对router部分略尽了绵薄之力。

## URI的基础格式
> [scheme:][//host:port][path][?query][#fragment]  


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
我有在项目中提过一个issue，intent的FLAG问题，当前是没有支持的，可以使用port来做一些手脚，也可以直接在Router中将方法原型扩展一下。

**经过一定的思考后，port部分不占用，从方法上进行扩展**

### path部分
当前的anno分支实现中，在对path提出了group的概念，可以针对group分一下Router，但是除了看起来有点噱头其实没啥大用，因为路由表都是按照注解自动生成的，最多就是路由器命名的时候贴切一些。

*而实际上确实是一个噱头，不可否认在使用Router时出现了hardcode对一些严重代码洁癖者是一万点暴击，提供了一个UiRoutersHolder的注解可以将module中的所有route节点生成一个“地址”常量类，并且按照group进行了划分，但仅能在module内使用，无法跨module。所以在跨module的UI跳转时，仍然无法直接使用这一特性，除非在公共的下层lib中手动维护一个常量类，即手工将各个包含UI的Component的路由表进行拷贝。所以本人提供的是一个相当鸡肋的、对开发者算不上友好的功能，仅推荐严重代码洁癖者使用*

### query部分
有一些朋友提过issue：runalone时的传值问题。参考一些服务端的同事在一些非敏感接口同时兼容了query传值和表单传值的做法，我觉得Router部分兼容query传值和Intent的bundle传值是可取的。

计划中需要支持使用query进行传值

优先级：bundle > queryString
当bundle中获取了相应的数据，即忽略queryString取值查询

mark： *因android的参数传递还是以Bundle为主的，暂时不会支持使用query传值*

## 关于使用web启动
出于社会化分享、增加入口等等原因，App往往都有配套的m站、各种分享页面；又为了提示用户粘度、功能完整性、提升体验、安全性等等一系列原因又需要将用户从这些web页面带到App中，balabala...

总之，使用web启动是很重要的，人工实现这一功能是很不友好的，引入ARouter是比较重量级的，DDComponent需要支持这一功能是必须要实现使用query传值的。**纠结的是如何在实现功能的同时最大程度的保持轻量级**。


