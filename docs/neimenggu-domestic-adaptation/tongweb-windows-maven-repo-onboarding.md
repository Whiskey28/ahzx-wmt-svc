# TongWeb 嵌入式版（Windows）Maven 仓库接入教程（基于 70E8_P2 手册第 3 章）

## 1. 适用范围

- 目标：在 Windows 环境下，将 TongWeb 嵌入式版资源包安装到本地 Maven 仓库或企业私仓，并在项目中通过 `pom.xml` 正常引入依赖。
- 依据文档：`002_TongWeb_V7.0嵌入式版_Servlet标准容器手册_70E8_P2.pdf` 第 3 章《使用指引》（3.1 ~ 3.7）。
- 适配版本：文中示例按 `7.0.E.8_P2` 说明。

## 2. 一次性准备（避免中途返工）

### 2.1 检查 Java / Maven 基础环境

1. 打开 `cmd`（建议管理员权限）。
2. 执行：

```bat
java -version
mvn -v
echo %JAVA_HOME%
```

预期：
- `java -version` 正常输出版本信息。
- `mvn -v` 正常输出 Maven 与 JDK 信息。
- `JAVA_HOME` 指向有效 JDK 路径。

### 2.2 配置 Maven 环境变量（如未配置）

- 将 Maven 的 `bin` 路径加入系统 `Path`（示例：`D:\maven\bin`）。
- 重新打开命令行后再次执行 `mvn -v` 验证。

### 2.3 明确 `settings.xml` 的实际生效文件

建议统一使用：
- `%USERPROFILE%\.m2\settings.xml`

并确保至少包含本地仓库配置：

```xml
<settings>
  <localRepository>D:\repository</localRepository>
</settings>
```

说明：
- 路径请按实际修改。
- 若同时存在 `%MAVEN_HOME%\conf\settings.xml` 与 `%USERPROFILE%\.m2\settings.xml`，以用户目录配置为主更稳妥。

## 3. 安装包完整性校验（对应 3.2）

在 `tongweb-embed-7.0.E.8_P2.zip` 所在目录执行：

```bat
certUtil -hashfile tongweb-embed-7.0.E.8_P2.zip MD5
```

将输出值与 `tongweb-embed-7.0.E.8_P2.zip.md5` 对比，一致即通过。

## 4. 将 TongWeb 资源包安装到 Maven 仓库（对应 3.3）

### 4.1 本地仓库安装（推荐先做）

在资源包目录执行：

```bat
installAll-7.0.E.8_P2.bat
```

作用：
- 解压资源包。
- 将 `lib` 下需要的 jar 安装到本地 Maven 仓库（`settings.xml` 指定目录）。

### 4.2 企业私仓部署（可选）

```bat
deployAll-7.0.E.8_P2.bat <url> <sid>
```

参数说明：
- `url`：企业私仓完整可写地址（必须可 deploy）。
- `sid`：`settings.xml` 中 `<servers><server><id>...</id></server></servers>` 的 id。

示例（按手册风格）：

```bat
deployAll-7.0.E.8_P2.bat http://127.0.0.1:8081/nexus/content/repositories/central/ central
```

### 4.3 手动解压路径（备选）

若不走一键脚本：
1. 手动解压 `tongweb-embed-7.0.E.8_P2.zip`。
2. 在解压目录执行：
   - 本地安装：`installMavenJar.bat`
   - 私仓部署：`deployMavenJar.bat <url> <sid>`

### 4.4 国密包（如项目需要）

- 将 `tongweb-gmssl-1.0.0.zip` 放在同级目录，可随安装脚本一并处理。

## 5. 在项目中引入依赖（对应 3.4.1）

### 5.1 通用原则

1. 排除 Spring Boot 默认 Tomcat。
2. 按 Spring Boot 大版本引入对应 TongWeb starter：
   - Boot 1.x -> `tongweb-spring-boot-starter-1.x`
   - Boot 2.x -> `tongweb-spring-boot-starter-2.x`
   - Boot 3.x -> `tongweb-spring-boot-starter-3.x`
3. 若需 JSP，再加 `tongweb-jsp`（Boot 1/2）或 `tongweb-jsp-3.x`（Boot 3）。

### 5.2 Boot 3.x 参考片段

```xml
<!-- 排除 spring-boot 默认 tomcat -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  <exclusions>
    <exclusion>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-tomcat</artifactId>
    </exclusion>
  </exclusions>
</dependency>

<!-- 引入 TongWeb starter -->
<dependency>
  <groupId>com.tongweb.springboot</groupId>
  <artifactId>tongweb-spring-boot-starter-3.x</artifactId>
  <version>7.0.E.8_P2</version>
</dependency>
```

## 6. 安装结果验证（关键闭环）

### 6.1 仓库验证

- 本地仓库：检查 `D:\repository` 下是否出现 `com\tongweb\...`、`com\tongweb\springboot\...` 目录与版本。
- 私仓：在 Nexus/制品库页面确认对应坐标已上传。

### 6.2 项目依赖解析验证

在项目根目录执行：

```bat
mvn -U clean package -DskipTests
```

预期：
- 不再出现找不到 `com.tongweb*` 依赖的错误。

### 6.3 应用启动验证（对应 3.7）

```bat
java -jar your-app.jar
```

预期：
- 日志出现 TongWeb 初始化成功关键字（例如 `TongWeb initialized`、`Starting Servlet engine: [TongWeb/... ]`）。

## 7. 缺失环节检查清单（防漏项）

以下任一未完成，都会导致“脚本执行了但项目仍不可用”：

1. 未校验 `JAVA_HOME` 或 JDK 不可用。
2. Maven 生效配置文件搞错（改了一个，实际生效另一个）。
3. `localRepository` 未设置或指向无权限目录。
4. 未做 MD5 校验，包损坏后仍继续安装。
5. `deploy` 使用了只读仓库地址（非 hosted 可写仓库）。
6. `sid` 与 `settings.xml` `<server><id>` 不一致。
7. 私仓账户无 deploy 权限（导致 401）。
8. 密码含 XML 特殊字符未正确转义。
9. 项目 `pom.xml` 忘记排除默认 Tomcat。
10. starter 版本与 Spring Boot 主版本不匹配（1.x/2.x/3.x 选错）。
11. 未执行 `mvn -U clean package -DskipTests` 做最终解析验证。

## 8. 常见故障与快速处理

### 8.1 401 Unauthorized

优先检查：
- `<server><id>` 是否与 deploy 命令中的 `sid` 一致；
- 用户是否具备仓库 deploy 权限；
- 仓库 URL 是否正确且可写；
- 密码特殊字符是否转义。

### 8.2 依赖找不到

- 先查本地仓库是否已安装对应坐标；
- 再查 `pom.xml` 坐标、版本、artifactId 是否与资源包一致；
- 执行 `mvn -U clean package -DskipTests` 强制更新索引。

### 8.3 启动后仍是 Tomcat

- 检查 `spring-boot-starter-web` 是否正确排除了 `spring-boot-starter-tomcat`；
- 检查 TongWeb starter 是否被正确引入且未被 dependencyManagement 覆盖掉版本。

## 9. 最小执行路径（给实施同学）

1. 配好 `JAVA_HOME` / `mvn -v`。
2. 配 `%USERPROFILE%\.m2\settings.xml` 的 `localRepository`。
3. `certUtil` 校验 zip 的 MD5。
4. 执行 `installAll-7.0.E.8_P2.bat`。
5. 项目中排除 Tomcat + 引入对应 TongWeb starter。
6. `mvn -U clean package -DskipTests`。
7. `java -jar` 看 TongWeb 启动日志。

