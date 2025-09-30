# package-with-hyphen-separator

https://central.sonatype.com/artifact/io.github.naka-sho/package-with-hyphen-separator

This is a custom plugin for mybatis generator.

Package the snake case (_ delimiter) of the table name.

 - before

```shell
modelgen
├── StackedBookPurchaseRecord.java
├── StackedBookRecord.java
├── StackedBookTaskLinkRecord.java
├── StackedUserRecord.java
├── TaskCategoryRecord.java
├── TaskLabelRecord.java
├── TaskRecord.java
└── UsersRecord.java

mappergen
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

# Configuration Change for Auto-generation Target Tables(with Postgres)

## Description

Adds SkipPartitionedWithPostgresTablesPlugin, a MyBatis Generator (MBG) plugin that skips generating artifacts (model classes, mappers, dynamic SQL support) for PostgreSQL partition child tables. It discovers all non-partition-child tables plus views (excluding pg_catalog and information_schema) and only generates code for those. Optionally writes the allowlist to a file. Supports case-insensitive matching.

## Background

PostgreSQL declarative partitioning creates a physical table for every partition. Generating MyBatis artifacts for each child partition leads to:
- A large number of nearly identical classes/mappers
- Churn when time-based partitions rotate
- Redundant code when logic works at the parent level or routing is external

This plugin:

- Queries PostgreSQL system catalogs (pg_inherits, pg_class, pg_tables, pg_views)
- Excludes partition child tables
- Keeps normal tables, partition parent tables, and views
- Fails fast (throws RuntimeException) if discovery or writing the allowlist fails

## Usage
Add the plugin inside an MBG <context> that has a PostgreSQL connection (either jdbcConnection or connectionFactory).

- Available properties:
    - allowlistFile (optional): file path to write the discovered (sorted) table/view names
    - caseInsensitive (optional, default false): perform case-insensitive matching

Example:
```xml
<context id="PostgresContext" targetRuntime="MyBatis3">
  <jdbcConnection driverClass="org.postgresql.Driver"
                  connectionURL="jdbc:postgresql://localhost:5432/appdb"
                  userId="app"
                  password="secret"/>

  <plugin type="org.mybatis.generator.plugins.SkipPartitionedWithPostgresTablesPlugin">
    <property name="allowlistFile" value="build/mbg/allowlist.txt"/>
    <property name="caseInsensitive" value="true"/>
  </plugin>

  <!-- Optional <table> elements. If omitted, MBG introspects all; the plugin filters afterward. -->
</context>
```
