```text
 _    _                   _____              __  _       
| |  | |                 |  __ \            / _|(_)      
| |  | | ___   ___  _ __ | |__) |_ __  ___ | |_  _ __  __
| |  | |/ __| / _ \| '__||  ___/| '__|/ _ \|  _|| |\ \/ /
| |__| |\__ \|  __/| |   | |    | |  |  __/| |  | | >  < 
 \____/ |___/ \___||_|   |_|    |_|   \___||_|  |_|/_/\_\
```

# UserPrefix 帮助介绍文档

## 插件介绍目录

- 使用示例
    - [前缀配置文件预设示例](../src/main/resources/prefixes/example-prefix.yml)
- [操作(Actions)配置](ACTIONS.md)

## [开发文档](JAVADOC-README.md)

基于 [Github Pages](https://pages.github.com/) 搭建，请访问 [JavaDoc](https://carmjos.github.io/UltraDepository) 。

## 依赖方式

### Maven 依赖

```xml

<project>
    <repositories>

        <repository>
            <!--采用github依赖库，安全稳定，但需要配置 (推荐)-->
            <id>UserPrefix</id>
            <name>GitHub Packages</name>
            <url>https://maven.pkg.github.com/CarmJos/UserPrefix</url>
        </repository>

        <repository>
            <!--采用我的私人依赖库，简单方便，但可能因为变故而无法使用-->
            <id>carm-repo</id>
            <name>Carm's Repo</name>
            <url>https://repo.carm.cc/repository/maven-public/</url>
        </repository>

    </repositories>

    <dependencies>

        <dependency>
            <groupId>cc.carm.plugin</groupId>
            <artifactId>userprefix</artifactId>
            <version>[LATEST RELEASE]</version>
            <scope>provided</scope>
        </dependency>

    </dependencies>
</project>
```

### Gradle 依赖

```groovy
repositories {
    // 采用github依赖库，安全稳定，但需要配置 (推荐)
    maven { url 'https://maven.pkg.github.com/CarmJos/UserPrefix' }

    // 采用我的私人依赖库，简单方便，但可能因为变故而无法使用
    maven { url 'https://repo.carm.cc/repository/maven-public/' }
}

dependencies {
    compileOnly "cc.carm.plugin:userprefix:[LATEST RELEASE]"
}
```