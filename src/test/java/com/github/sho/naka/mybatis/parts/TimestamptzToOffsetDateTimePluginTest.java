package com.github.sho.naka.mybatis.parts;

import io.github.nakasho.mybatis.parts.TimestamptzToOffsetDateTimePlugin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.ModelType;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for TimestamptzToOffsetDateTimePlugin.
 */
class TimestamptzToOffsetDateTimePluginTest {

    @Test
    @DisplayName("validate() should always return true")
    void testValidate() {
        TimestamptzToOffsetDateTimePlugin plugin = new TimestamptzToOffsetDateTimePlugin();
        List<String> warnings = new ArrayList<>();
        assertThat(plugin.validate(warnings)).isTrue();
        assertThat(warnings).isEmpty();
    }

    @Test
    @DisplayName("TIMESTAMPTZ column should be converted to OffsetDateTime")
    void testTimestamptzConversion() {
        // Setup
        TimestamptzToOffsetDateTimePlugin plugin = new TimestamptzToOffsetDateTimePlugin();
        plugin.setContext(createContext());
        
        TopLevelClass topLevelClass = new TopLevelClass("com.example.TestModel");
        IntrospectedColumn column = createIntrospectedColumn("created_at", "TIMESTAMPTZ");
        
        Field field = new Field("createdAt", new FullyQualifiedJavaType("java.sql.Timestamp"));
        
        // Execute
        boolean result = plugin.modelFieldGenerated(
            field,
            topLevelClass,
            column,
            null,  // introspectedTable not needed for this test
            org.mybatis.generator.api.Plugin.ModelClassType.BASE_RECORD
        );
        
        // Verify
        assertThat(result).isTrue();
        assertThat(field.getType().getFullyQualifiedName()).isEqualTo("java.time.OffsetDateTime");
        assertThat(topLevelClass.getImportedTypes())
            .anyMatch(type -> type.getFullyQualifiedName().equals("java.time.OffsetDateTime"));
    }

    @Test
    @DisplayName("TIMESTAMP WITH TIME ZONE column should be converted to OffsetDateTime")
    void testTimestampWithTimeZoneConversion() {
        // Setup
        TimestamptzToOffsetDateTimePlugin plugin = new TimestamptzToOffsetDateTimePlugin();
        plugin.setContext(createContext());
        
        TopLevelClass topLevelClass = new TopLevelClass("com.example.TestModel");
        IntrospectedColumn column = createIntrospectedColumn("updated_at", "TIMESTAMP WITH TIME ZONE");
        
        Field field = new Field("updatedAt", new FullyQualifiedJavaType("java.sql.Timestamp"));
        
        // Execute
        boolean result = plugin.modelFieldGenerated(
            field,
            topLevelClass,
            column,
            null,
            org.mybatis.generator.api.Plugin.ModelClassType.BASE_RECORD
        );
        
        // Verify
        assertThat(result).isTrue();
        assertThat(field.getType().getFullyQualifiedName()).isEqualTo("java.time.OffsetDateTime");
    }

    @Test
    @DisplayName("TIMESTAMP column should NOT be converted (remains as-is)")
    void testTimestampNoConversion() {
        // Setup
        TimestamptzToOffsetDateTimePlugin plugin = new TimestamptzToOffsetDateTimePlugin();
        plugin.setContext(createContext());
        
        TopLevelClass topLevelClass = new TopLevelClass("com.example.TestModel");
        IntrospectedColumn column = createIntrospectedColumn("simple_timestamp", "TIMESTAMP");
        
        FullyQualifiedJavaType originalType = new FullyQualifiedJavaType("java.sql.Timestamp");
        Field field = new Field("simpleTimestamp", originalType);
        
        // Execute
        boolean result = plugin.modelFieldGenerated(
            field,
            topLevelClass,
            column,
            null,
            org.mybatis.generator.api.Plugin.ModelClassType.BASE_RECORD
        );
        
        // Verify
        assertThat(result).isTrue();
        // Type should remain unchanged
        assertThat(field.getType().getFullyQualifiedName()).isEqualTo("java.sql.Timestamp");
    }

    @Test
    @DisplayName("VARCHAR column should NOT be converted")
    void testVarcharNoConversion() {
        // Setup
        TimestamptzToOffsetDateTimePlugin plugin = new TimestamptzToOffsetDateTimePlugin();
        plugin.setContext(createContext());
        
        TopLevelClass topLevelClass = new TopLevelClass("com.example.TestModel");
        IntrospectedColumn column = createIntrospectedColumn("name", "VARCHAR");
        
        FullyQualifiedJavaType originalType = new FullyQualifiedJavaType("java.lang.String");
        Field field = new Field("name", originalType);
        
        // Execute
        boolean result = plugin.modelFieldGenerated(
            field,
            topLevelClass,
            column,
            null,
            org.mybatis.generator.api.Plugin.ModelClassType.BASE_RECORD
        );
        
        // Verify
        assertThat(result).isTrue();
        assertThat(field.getType().getFullyQualifiedName()).isEqualTo("java.lang.String");
    }

    @Test
    @DisplayName("Case insensitive check for timestamptz")
    void testCaseInsensitiveTimestamptz() {
        // Setup
        TimestamptzToOffsetDateTimePlugin plugin = new TimestamptzToOffsetDateTimePlugin();
        plugin.setContext(createContext());
        
        TopLevelClass topLevelClass = new TopLevelClass("com.example.TestModel");
        IntrospectedColumn column = createIntrospectedColumn("created_at", "timestamptz");
        
        Field field = new Field("createdAt", new FullyQualifiedJavaType("java.sql.Timestamp"));
        
        // Execute
        boolean result = plugin.modelFieldGenerated(
            field,
            topLevelClass,
            column,
            null,
            org.mybatis.generator.api.Plugin.ModelClassType.BASE_RECORD
        );
        
        // Verify - lowercase should also work
        assertThat(result).isTrue();
        assertThat(field.getType().getFullyQualifiedName()).isEqualTo("java.time.OffsetDateTime");
    }

    @Test
    @DisplayName("Multiple TIMESTAMPTZ columns should all be converted")
    void testMultipleTimestamptzColumns() {
        // Setup
        TimestamptzToOffsetDateTimePlugin plugin = new TimestamptzToOffsetDateTimePlugin();
        plugin.setContext(createContext());
        
        TopLevelClass topLevelClass = new TopLevelClass("com.example.TestModel");
        
        // First column
        IntrospectedColumn column1 = createIntrospectedColumn("created_at", "TIMESTAMPTZ");
        Field field1 = new Field("createdAt", new FullyQualifiedJavaType("java.sql.Timestamp"));
        
        plugin.modelFieldGenerated(
            field1,
            topLevelClass,
            column1,
            null,
            org.mybatis.generator.api.Plugin.ModelClassType.BASE_RECORD
        );
        
        // Second column
        IntrospectedColumn column2 = createIntrospectedColumn("updated_at", "TIMESTAMPTZ");
        Field field2 = new Field("updatedAt", new FullyQualifiedJavaType("java.sql.Timestamp"));
        
        plugin.modelFieldGenerated(
            field2,
            topLevelClass,
            column2,
            null,
            org.mybatis.generator.api.Plugin.ModelClassType.BASE_RECORD
        );
        
        // Verify
        assertThat(field1.getType().getFullyQualifiedName()).isEqualTo("java.time.OffsetDateTime");
        assertThat(field2.getType().getFullyQualifiedName()).isEqualTo("java.time.OffsetDateTime");
    }

    // Helper methods

    private Context createContext() {
        Context context = new Context(ModelType.FLAT);
        context.setId("test");
        context.setTargetRuntime("MyBatis3DynamicSql");
        return context;
    }

    private IntrospectedColumn createIntrospectedColumn(String columnName, String jdbcTypeName) {
        IntrospectedColumn column = new IntrospectedColumn();
        column.setActualColumnName(columnName);
        column.setJdbcTypeName(jdbcTypeName);
        column.setJavaProperty(toCamelCase(columnName));
        return column;
    }

    private String toCamelCase(String snakeCase) {
        String[] parts = snakeCase.split("_");
        StringBuilder camelCase = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            camelCase.append(parts[i].substring(0, 1).toUpperCase())
                    .append(parts[i].substring(1));
        }
        return camelCase.toString();
    }
}
