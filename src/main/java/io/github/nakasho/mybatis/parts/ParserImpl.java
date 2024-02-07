package io.github.nakasho.mybatis.parts;

/**
 * Generate Model name, Mapper name, and DynamicSqlSupport name from Table name
 * implements class
 */
public class ParserImpl implements Parser {
    private final static String REGEX = "\\.[^.]+$";
    private final String baseRecordType;
    private final String mybatis3JavaMapperType;
    private final String mybatisDynamicSqlSupportType;

    /**
     * Constructor
     *
     * @param fullyQualifiedTableNameAtRuntime table name
     * @param baseRecordType                   Model name(path)
     * @param mybatis3JavaMapperType           Mapper name(path)
     * @param mybatisDynamicSqlSupportType     DynamicSqlSupport name(path)
     */
    public ParserImpl(String fullyQualifiedTableNameAtRuntime, String baseRecordType, String mybatis3JavaMapperType, String mybatisDynamicSqlSupportType) {
        String packageName = new ParserUtils().packageAndFileName(fullyQualifiedTableNameAtRuntime);
        this.baseRecordType = baseRecordType
                .replaceAll(REGEX, "." + packageName + "Record");
        this.mybatis3JavaMapperType = mybatis3JavaMapperType
                .replaceAll(REGEX, "." + packageName + "Mapper");
        this.mybatisDynamicSqlSupportType = mybatisDynamicSqlSupportType
                .replaceAll(REGEX, "." + packageName + "DynamicSqlSupport");
    }

    @Override
    public String getBaseRecordType() {
        return this.baseRecordType;
    }

    @Override
    public String getMybatis3JavaMapperType() {
        return this.mybatis3JavaMapperType;
    }

    @Override
    public String getMybatisDynamicSqlSupportType() {
        return this.mybatisDynamicSqlSupportType;
    }
}
