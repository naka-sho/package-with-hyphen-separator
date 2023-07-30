# package-with-hyphen-separator

mybatis generator のカスタムプラグインです。

テーブル名のスネークケース(_区切り)をパッケージにします。

# 使用方法

https://github.com/mybatis/generator

以下のように、`mybatisGenerator` に設定してください。

```groovy
dependencies {
    implementation "org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.3"
    implementation "org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.1.4"
    implementation "org.mybatis.scripting:mybatis-freemarker:1.2.2"
    runtimeOnly "org.postgresql:postgresql:42.5.1"
    mybatisGenerator project(":package-with-hyphen-separator")
}
```

generatorConfigurationのpluginに追加してください。

```xml
<generatorConfiguration>
    <context id="service" targetRuntime="MyBatis3DynamicSql">
        ...

        <plugin type="com.github.sho.naka.mybatis.parts.PackageWithHyphenSeparatorPlugin"/>

        ...
    </context>
</generatorConfiguration>

```

mbGenerator を実行するとスネークケースに合わせてパッケージが作成されます。