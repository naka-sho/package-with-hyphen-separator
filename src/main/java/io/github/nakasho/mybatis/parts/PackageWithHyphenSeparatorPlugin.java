package io.github.nakasho.mybatis.parts;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;
import java.util.Properties;

/**
 * mybatis generator
 */
public class PackageWithHyphenSeparatorPlugin extends PluginAdapter {

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        Parser parser = new Parser(
                introspectedTable.getFullyQualifiedTableNameAtRuntime(),
                introspectedTable.getBaseRecordType(),
                introspectedTable.getMyBatis3JavaMapperType(),
                introspectedTable.getMyBatisDynamicSqlSupportType()
        );

        introspectedTable.setBaseRecordType(parser.getBaseRecordType());
        introspectedTable.setMyBatis3JavaMapperType(parser.getMybatis3JavaMapperType());
        introspectedTable.setMyBatisDynamicSqlSupportType(parser.getMybatisDynamicSqlSupportType());
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
}
