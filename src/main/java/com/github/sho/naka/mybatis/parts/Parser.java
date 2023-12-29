package com.github.sho.naka.mybatis.parts;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Parser {
    private final static String REGEX = "\\.[^.]+$";
    private final String baseRecordType;
    private final String mybatis3JavaMapperType;
    private final String mybatisDynamicSqlSupportType;

    public Parser(String fullyQualifiedTableNameAtRuntime, String baseRecordType, String mybatis3JavaMapperType, String mybatisDynamicSqlSupportType) {
        String packageName = new ParserUtils().packageAndFileName(fullyQualifiedTableNameAtRuntime);
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
}
