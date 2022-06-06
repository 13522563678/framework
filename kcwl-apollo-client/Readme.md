### Apollo客户端自动集成
本项目支持将携程apollo作为统一配置中心，并支持自动刷新SpringBoot的配置。

#### 使用说明
1. `pom.xml`文件增加apollo依赖：
    ```xml
    <dependency>
    <groupId>com.kcwl.framework</groupId>
    <artifactId>kcwl-apollo-client</artifactId>
    <version>1.0.0</version>
    </dependency>
    ```
2. 项目的`src/main/resources`文件夹下增加`application.yaml`(`application.properties`也可以)，内容如下
    ```yaml
    apollo.bootstrap.enabled: true
    ```
3. 增加`src/main/resources/META-INF/app.properties`，内容如下，appId参数值与apollo中的配置一致
    ```properties
    app.id=<appId>
    ```
4. 增加环境变量配置：`C:\opt\settings\server.properties`(Linux的路径：`/opt/settings/server.properties`)，
具体值由服务器所在环境决定(dev开发, fat测试, uat预生产, pro生产)。内容如下：
    ```properties
    env=dev
    ```
    注1：也可以通过启动命令的`-Denv=dev`指定。
    注2：环境变量的值不区分大小写。
5. 如果使用了ConfigurationProperties并且希望在配置修改以后自动更新，在pom.xml文件增加以下依赖，
并在相应的`@ConfigurationProperties`类上增加注解`@RefreshScope`：
    ```xml
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-context</artifactId>
      <version>2.0.2.RELEASE</version>
    </dependency>
    ```
6. 本项目内置了Apollo服务器的地址：`apollo.kcwl.com`，根据需要配置dns或者hosts访问。  
由于当前环境隔离，各环境使用独立的apollo服务器。
因此各环境的Apollo请求的apollo服务器的域名都是一样的，通过环境的内置dns或者hosts进行环境的区分。
env设置的所有值都指向同一个域名，有效的环境变量值为：dev，fat，uat，pro，fws，lpt，tools
