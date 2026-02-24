# Arthas 线上排查快速命令手册

> Arthas 是阿里开源的 Java 诊断工具，支持 JDK 6+，无需修改应用即可动态追踪、诊断线上问题。

---

## 一、启动与连接

```bash
# 方式1：直接 attach 到已运行的 Java 进程（推荐）
java -jar arthas-boot.jar

# 方式2：指定进程 ID
java -jar arthas-boot.jar <pid>

# 方式3：指定进程名（匹配第一个）
java -jar arthas-boot.jar <进程名>
```

---

## 二、常用基础命令

| 命令 | 说明 |
|------|------|
| `help` | 查看帮助 |
| `quit` / `exit` | 退出当前会话（不关闭目标 JVM） |
| `stop` | 完全关闭 Arthas，释放资源 |
| `keymap` | 快捷键说明 |
| `history` | 历史命令 |
| `cls` | 清屏 |

---

## 三、JVM / 类与类加载

### 3.1 查看 JVM 信息

```bash
# JVM 信息
dashboard

# 线程概览（可指定刷新间隔，单位秒）
dashboard -i 3

# 仅看线程
thread

# 最忙的 N 个线程及堆栈
thread -n 5

# 查找阻塞的线程
thread -b

# 指定线程 ID 的堆栈
thread <id>

# 按状态过滤
thread --state WAITING
```

### 3.2 类与类加载器

```bash
# 查看类加载器
sc -d com.example.YourClass

# 反编译（不带 .class）
jad com.example.YourClass

# 反编译指定方法
jad com.example.YourClass methodName

# 查看类从哪里加载
sc -d com.example.YourClass
```

### 3.3 方法调用追踪（重要）

```bash
# 追踪方法调用（可指定次数）
trace com.example.Service getData '#cost > 100'

# 限制追踪次数
trace com.example.Service getData -n 5

# 多层追踪
trace -E 'com.example.*|com.other.*' methodName

# 观察方法入参、返回值、异常（重要）
watch com.example.Service getData '{params,returnObj,throwExp}' -x 3

# 观察方法执行前后（入参、返回值、耗时）
watch com.example.Service getData '{params,returnObj,#cost}' -x 3 -b -s

# 仅异常时触发
watch com.example.Service getData '{params,throwExp}' -x 3 -e
```

---

## 四、方法执行与调用栈

### 4.1 观察点 watch

```bash
# 入参、返回值，深度 2
watch com.example.Service * '{params,returnObj}' -x 2

# 方法调用前（-b）、后（-s）、前后都看（-b -s）
watch com.example.Service getData '{params,returnObj}' -x 2 -b -s

# 条件：仅当第 1 个参数为 1 时
watch com.example.Service getData '{params,returnObj}' 'params[0]==1' -x 2

# 按耗时过滤：仅当耗时 > 200ms
watch com.example.Service getData '{params,returnObj,#cost}' '#cost>200' -x 2
```

### 4.2 调用栈 trace / stack

```bash
# 追踪调用栈（默认一层）
trace com.example.Service getData

# 耗时大于 100ms 的才打印
trace com.example.Service getData '#cost > 100'

# 追踪层级
trace com.example.Service getData -n 5 --skipJDKMethod false

# 查看谁调用了当前方法
stack com.example.Service getData

# 条件
stack com.example.Service getData '#cost > 100'
```

### 4.3 调用次数统计 tt（Time Tunnel）

```bash
# 记录方法调用（重放用）
tt -t com.example.Service getData

# 列出记录
tt -l

# 重放某次调用（-i 后跟索引）
tt -i 1000 -p

# 重放并修改入参
tt -i 1000 -p --replay-times 1 --replay-interval 3000
```

---

## 五、反编译与热更新

### 5.1 反编译 jad

```bash
jad com.example.YourClass
jad com.example.YourClass methodName
jad --source-only com.example.YourClass
```

### 5.2 热更新（慎用）

```bash
# 1. 反编译后修改，再编译
jad --source-only com.example.YourClass > /tmp/YourClass.java
# 编辑 /tmp/YourClass.java
# 2. 编译
mc /tmp/YourClass.java -d /tmp
# 3. 重新加载
redefine /tmp/com/example/YourClass.class
```

---

## 六、OGNL 表达式速查

在 `watch` / `trace` / `stack` 等命令里会用到：

| 表达式 | 含义 |
|--------|------|
| `params` | 入参数组 |
| `params[0]` | 第一个参数 |
| `returnObj` | 返回值 |
| `throwExp` | 异常 |
| `#cost` | 方法耗时（ms） |
| `target` | 当前对象 |
| `method` | 当前方法 |
| `clazz` | 当前类 |

---

## 七、堆内存与 OOM

```bash
# 堆内存概要
memory

# 堆对象统计（类实例数量）
vmtool --action getInstances --className java.lang.String --limit 10

# 查看对象属性（先 heapdump 再分析，或结合 OGNL）
ognl '@java.lang.System@getProperty("user.dir")'
```

---

## 八、常用排查场景速查

| 场景 | 推荐命令 |
|------|----------|
| CPU 高 | `thread -n 5` 或 `dashboard` 看线程，再 `thread <id>` 看栈 |
| 接口慢 | `trace 全限定类名 方法名 '#cost > 200'` |
| 方法入参/返回值 | `watch 类 方法 '{params,returnObj}' -x 3` |
| 谁在调用 | `stack 类 方法` |
| 报错排查 | `watch 类 方法 '{params,throwExp}' -e -x 3` |
| 类是否加载 | `sc -d 全限定类名` |
| 反编译看逻辑 | `jad 全限定类名` 或 `jad 类 方法` |

---

## 九、注意事项

1. **生产慎用**：`watch`/`trace` 会带来一定开销，建议加 `-n` 限制次数，或用条件过滤。
2. **类名要写全**：尽量写全限定类名，避免多个同名类时搞错。
3. **redefine**：热更新有风险，仅建议临时止血，改完应发布正式版本。
4. **权限**：需有目标进程的同用户或 root 权限才能 attach。

---

## 十、一键复制示例

```bash
# 1. 启动
java -jar arthas-boot.jar

# 2. 看最忙的 5 个线程
thread -n 5

# 3. 追踪某接口耗时 >100ms 的调用
trace com.wmt.xxx.controller.XxxController getPage '#cost > 100' -n 20

# 4. 看某方法入参和返回值（深度 2，最多 5 次）
watch com.wmt.xxx.service.XxxService getById '{params,returnObj}' -x 2 -n 5
```

---

*文档基于 Arthas 3.x，具体以 [Arthas 官方文档](https://arthas.aliyun.com/doc/) 为准。*
