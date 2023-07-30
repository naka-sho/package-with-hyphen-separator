package com.github.sho.naka.mybatis.parts;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Parser {
    private final static String REGEX = "\\.[^.]+$";
    private final String baseRecordType;
    private final String mybatis3JavaMapperType;
    private final String mybatisDynamicSqlSupportType;

    public Parser(String fullyQualifiedTableNameAtRuntime, String baseRecordType, String mybatis3JavaMapperType, String mybatisDynamicSqlSupportType) {
        String packageName = this.packageAndFileName(fullyQualifiedTableNameAtRuntime);
        this.baseRecordType = baseRecordType
                .replaceAll(REGEX, "." + packageName + "Record");
        this.mybatis3JavaMapperType = mybatis3JavaMapperType
                .replaceAll(REGEX, "." + packageName + "Mapper");
        this.mybatisDynamicSqlSupportType = mybatisDynamicSqlSupportType
                .replaceAll(REGEX, "." + packageName + "DynamicSqlSupport");
    }

    public String getBaseRecordType() {
        return this.baseRecordType;
    }

    public String getMybatis3JavaMapperType() {
        return this.mybatis3JavaMapperType;
    }

    public String getMybatisDynamicSqlSupportType() {
        return this.mybatisDynamicSqlSupportType;
    }

    /**
     * table名の_を.に変更し、最後の要素はアッパーキャメルケースに変更する
     *
     * @param fullyQualifiedTableNameAtRuntime table名の要素
     * @return
     */
    private String packageAndFileName(String fullyQualifiedTableNameAtRuntime){
        String tableName = fullyQualifiedTableNameAtRuntime
                .replaceAll("public.", "");
        String[] parts = tableName.split("_");
        String MapperName =
                Arrays
                        .stream(parts)
                        .map( e -> e.substring(0, 1).toUpperCase() + e.substring(1))
                        .collect(Collectors.joining());
        parts[parts.length - 1] = MapperName;
        return String.join(".", parts);
    }
}
