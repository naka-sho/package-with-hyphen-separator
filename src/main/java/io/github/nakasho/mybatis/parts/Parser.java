package io.github.nakasho.mybatis.parts;

/**
 * Parser
 */
public class Parser {
    private final static String REGEX = "\\.[^.]+$";
    private final String baseRecordType;
    private final String mybatis3JavaMapperType;
    private final String mybatisDynamicSqlSupportType;

    /**
     * Constructor
     *
     * @param fullyQualifiedTableNameAtRuntime テーブル名
     * @param baseRecordType Model名(パス付き)
     * @param mybatis3JavaMapperType Mapper名(パス付き)
     * @param mybatisDynamicSqlSupportType DynamicSqlSupport名(パス付き)
     */
    public Parser(String fullyQualifiedTableNameAtRuntime, String baseRecordType, String mybatis3JavaMapperType, String mybatisDynamicSqlSupportType) {
        String packageName = new ParserUtils().packageAndFileName(fullyQualifiedTableNameAtRuntime);
        this.baseRecordType = baseRecordType
                .replaceAll(REGEX, "." + packageName + "Record");
        this.mybatis3JavaMapperType = mybatis3JavaMapperType
                .replaceAll(REGEX, "." + packageName + "Mapper");
        this.mybatisDynamicSqlSupportType = mybatisDynamicSqlSupportType
                .replaceAll(REGEX, "." + packageName + "DynamicSqlSupport");
    }

    /**
     * Model名
     *
     * @return Model名
     */
    public String getBaseRecordType() {
        return this.baseRecordType;
    }

    /**
     * Mapper名
     *
     * @return Mapper名
     */
    public String getMybatis3JavaMapperType() {
        return this.mybatis3JavaMapperType;
    }

    /**
     * DynamicSqlSupport名
     *
     * @return DynamicSqlSupport名
     */
    public String getMybatisDynamicSqlSupportType() {
        return this.mybatisDynamicSqlSupportType;
    }
}
