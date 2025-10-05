# mybatis custom plugin

https://central.sonatype.com/artifact/io.github.naka-sho/package-with-hyphen-separator



## Set it to `mybatisGenerator` as shown below.

https://github.com/mybatis/generator

```groovy
dependencies {
    implementation 'io.github.naka-sho:package-with-hyphen-separator:0.0.11'
    mybatisGenerator 'io.github.naka-sho:package-with-hyphen-separator:0.0.11'
}
```

# PostgreSQL TIMESTAMPTZ to OffsetDateTime Mapping

## Description

Adds TimestamptzToOffsetDateTimePlugin, a MyBatis Generator (MBG) plugin that automatically converts PostgreSQL TIMESTAMPTZ (timestamp with time zone) columns to `java.time.OffsetDateTime` instead of the default `java.sql.Timestamp`.

## Background

PostgreSQL's TIMESTAMPTZ type stores timezone-aware timestamps. By default, MyBatis Generator maps TIMESTAMPTZ columns to `java.sql.Timestamp`, which can cause issues:
- Loss of timezone information
- Conversion errors when retrieving data from the database
- Incompatibility with modern Java time API

This plugin automatically changes the mapping to `java.time.OffsetDateTime`, which:
- Preserves timezone information
- Works seamlessly with PostgreSQL TIMESTAMPTZ columns
- Uses the modern Java 8+ time API

## Usage

Add the plugin to your MyBatis Generator configuration:

```xml
<context id="PostgresContext" targetRuntime="MyBatis3DynamicSql">
  <jdbcConnection driverClass="org.postgresql.Driver"
                  connectionURL="jdbc:postgresql://localhost:5432/appdb"
                  userId="app"
                  password="secret"/>

  <plugin type="io.github.nakasho.mybatis.parts.TimestamptzToOffsetDateTimePlugin"/>

  <!-- Other configuration -->
</context>
```

The plugin automatically detects TIMESTAMPTZ and "TIMESTAMP WITH TIME ZONE" columns and converts them to `java.time.OffsetDateTime`.

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

Output example (if allowlistFile is set):

 - before

```
auditLog
customers
orders
orders202301
orders202302
orders202303
orders202304
orders202305
orders202306
orders202307
orders202308
orders202309
orders202310
orders202311
orders202312
products
productsView
users
```

 - after

```
auditLog
customers
orders
products
productsView
users
```

# package-with-hyphen-separator

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
