# EventBus 增强

## 1. Guava EventBus

采用`反射`的方式对方法进行调用

## 2. 增强

使用`APT(Annotation Processor Tool)`技术增强`Subscribe`对应的类

> APT 学习可以参考 [IceMimosa/apt](https://github.com/IceMimosa/apt)

### 2.1 实现

前面几步基本都跟Guava实现的类似, 假设有类如下:

```java
// Event 是个简单类
public class TestSubscribe {
    
    @Subscribe
    public void handle(Event e) {
        System.out.println("执行了 handle");
    }

    @Subscribe
    public void handle2(Event e) {
        System.out.println("执行了 handle2");
    }
    
}
```

* 在`regist`阶段, 将`方法参数`和`对应的对象和方法(Subscriber对象)`, 形成一个map
* `post`阶段, 找出`Event`类型对应的`所有Subscriber`, 然后利用`反射`的方式调用每个方法

### 2.2 增强

在编译阶段使用`APT`对类进行增强

* 定义个通用接口 `EventBusHandler`

```java
public interface EventBusHandler {
    void $$__invoke__$$(String type, Object arg);
}
```

* 编译期间
    * `TestSubscribe` 实现 `EventBusHandler接口`
    *  为 `TestSubscribe` 添加接口方法, 如下
    
    ```java
    public void $$__invoke__$$(String type, Object arg) {
        if ("handle@io.patamon.eventbus.demo.Event".equals(type)) {
            this.handle((Event)arg);
        }
        if ("handle2@io.patamon.eventbus.demo.Event".equals(type)) {
            this.handle2((Event)arg);
        }
    }
    ```

* 这样在`post`阶段, 就可以传入 `type` 和 `arg` 进行方法的调用, 无需进行反射, 很大的提高的调用效率.


## TODO

其他细节需要处理的, 只实现的demo级别. (抽了Guava部分代码)
