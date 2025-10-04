/*
 *    Copyright 2006-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package io.github.nakasho.mybatis.parts;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * MyBatis Generator plugin to convert PostgreSQL TIMESTAMPTZ columns to java.time.OffsetDateTime.
 * <p>
 * By default, MyBatis Generator maps TIMESTAMPTZ to java.sql.Timestamp, which loses timezone information
 * and can cause conversion errors. This plugin changes the mapping to java.time.OffsetDateTime,
 * which properly handles timezone-aware timestamps.
 * <p>
 * Usage: Add this plugin to your MyBatis Generator configuration:
 * <pre>
 * &lt;plugin type="io.github.nakasho.mybatis.parts.TimestamptzToOffsetDateTimePlugin"/&gt;
 * </pre>
 */
public class TimestamptzToOffsetDateTimePlugin extends PluginAdapter {

    /**
     * Constructor
     */
    public TimestamptzToOffsetDateTimePlugin() {}

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * Intercepts model field generation to convert TIMESTAMPTZ columns to OffsetDateTime.
     * This method is called for each field in the generated model class.
     *
     * @param field the field being generated
     * @param topLevelClass the class being generated
     * @param introspectedColumn the database column being mapped
     * @param introspectedTable the table being introspected
     * @param modelClassType the type of model class being generated
     * @return true to continue generation, false to skip this field
     */
    @Override
    public boolean modelFieldGenerated(Field field,
                                       TopLevelClass topLevelClass,
                                       IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable,
                                       ModelClassType modelClassType) {
        
        // Check if this column is a TIMESTAMPTZ (also check variations)
        String jdbcTypeName = introspectedColumn.getJdbcTypeName();
        if (jdbcTypeName != null && 
            (jdbcTypeName.equalsIgnoreCase("TIMESTAMPTZ") || 
             jdbcTypeName.equalsIgnoreCase("TIMESTAMP WITH TIME ZONE"))) {
            
            // Change the field type to OffsetDateTime
            FullyQualifiedJavaType offsetDateTimeType = new FullyQualifiedJavaType("java.time.OffsetDateTime");
            field.setType(offsetDateTimeType);
            
            // Add the import to the class
            topLevelClass.addImportedType(offsetDateTimeType);
            
            // Update the column's Java type so other generated code uses it correctly
            introspectedColumn.setFullyQualifiedJavaType(offsetDateTimeType);
        }
        
        return true;
    }
}
