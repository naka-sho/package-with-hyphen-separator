package io.github.nakasho.mybatis.parts;

/**
 * Generate Model name, Mapper name, and DynamicSqlSupport name from Table name
 */
public interface Parser {
    /**
     * Model
     *
     * @return Model name
     */
    String getBaseRecordType();

    /**
     * Mapper name
     *
     * @return Mapper name
     */
    String getMybatis3JavaMapperType();

    /**
     * DynamicSqlSupport name
     *
     * @return DynamicSqlSupport name
     */
    String getMybatisDynamicSqlSupportType();
}
