# package-with-hyphen-separator

https://central.sonatype.com/artifact/io.github.naka-sho/package-with-hyphen-separator

This is a custom plugin for mybatis generator.

Package the snake case (_ delimiter) of the table name.

 - before

```shell
modelgen
├── StackedBookPurchaseRecordDynamicSqlSupport.java
├── StackedBookPurchaseRecordMapper.java
├── StackedBookRecordDynamicSqlSupport.java
├── StackedBookRecordMapper.java
├── StackedBookTaskLinkRecordDynamicSqlSupport.java
├── StackedBookTaskLinkRecordMapper.java
├── StackedUserRecordDynamicSqlSupport.java
├── StackedUserRecordMapper.java
├── TaskCategoryRecordDynamicSqlSupport.java
├── TaskCategoryRecordMapper.java
├── TaskLabelRecordDynamicSqlSupport.java
├── TaskLabelRecordMapper.java
├── TaskRecordDynamicSqlSupport.java
├── TaskRecordMapper.java
├── UsersRecordDynamicSqlSupport.java
└── UsersRecordMapper.java

mappergen
├── StackedBookPurchaseRecord.java
├── StackedBookRecord.java
├── StackedBookTaskLinkRecord.java
├── StackedUserRecord.java
├── TaskCategoryRecord.java
├── TaskLabelRecord.java
├── TaskRecord.java
└── UsersRecord.java
```

 - after

```shell
modelgen
├── UsersRecord.java
├── stacked
│   ├── StackedBookRecord.java
│   ├── StackedUserRecord.java
│   └── book
│       ├── StackedBookPurchaseRecord.java
│       └── task
│           └── StackedBookTaskLinkRecord.java
└── task
    ├── TaskCategoryRecord.java
    └── TaskLabelRecord.java

mappergen
├── UsersDynamicSqlSupport.java
├── UsersMapper.java
├── stacked
│   ├── StackedBookDynamicSqlSupport.java
│   ├── StackedBookMapper.java
│   ├── StackedUserDynamicSqlSupport.java
│   ├── StackedUserMapper.java
│   └── book
│       ├── StackedBookPurchaseDynamicSqlSupport.java
│       ├── StackedBookPurchaseMapper.java
│       └── task
│           ├── StackedBookTaskLinkDynamicSqlSupport.java
│           └── StackedBookTaskLinkMapper.java
└── task
    ├── TaskCategoryDynamicSqlSupport.java
    ├── TaskCategoryMapper.java
    ├── TaskLabelDynamicSqlSupport.java
    └── TaskLabelMapper.java
```

# using

https://github.com/mybatis/generator

Set it to `mybatisGenerator` as shown below.

```groovy
dependencies {
    implementation "org.mybatis.spring.boot:mybatis-spring-boot-starter:2.1.3"
    implementation "org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.1.4"
    implementation "org.mybatis.scripting:mybatis-freemarker:1.2.2"
    runtimeOnly "org.postgresql:postgresql:42.5.1"
    implementation 'io.github.naka-sho:package-with-hyphen-separator:0.0.11'
    mybatisGenerator 'io.github.naka-sho:package-with-hyphen-separator:0.0.11'
}
```

Please add it to the generatorConfiguration plugin.

```xml
<generatorConfiguration>
    <context id="service" targetRuntime="MyBatis3DynamicSql">
        ...

        <plugin type="io.github.nakasho.mybatis.parts.PackageWithHyphenSeparatorPlugin"/>

        ...
    </context>
</generatorConfiguration>

```

When you run mbGenerator, a package will be created according to the snake case.
