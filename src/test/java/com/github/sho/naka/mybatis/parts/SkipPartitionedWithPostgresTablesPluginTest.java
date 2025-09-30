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
package com.github.sho.naka.mybatis.parts;

import io.github.nakasho.mybatis.parts.SkipPartitionedWithPostgresTablesPlugin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.ModelType;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for SkipPartitionedWithPostgresTablesPlugin schemaname handling.
 */
class SkipPartitionedWithPostgresTablesPluginTest {

    /**
     * Test helper plugin that captures the schema name passed to discovery.
     */
    static class TestPlugin extends SkipPartitionedWithPostgresTablesPlugin {
        String capturedSchema;
        Set<String> tablesToReturn;

        TestPlugin(Set<String> tablesToReturn) {
            this.tablesToReturn = tablesToReturn;
        }

        @Override
        protected Set<String> discoverNonPartitionedTables(String schemaName) {
            this.capturedSchema = schemaName;
            return tablesToReturn;
        }
    }

    private Context newContext() {
        Context context = new Context(ModelType.CONDITIONAL);
        context.setId("test");
        JDBCConnectionConfiguration jdbc = new JDBCConnectionConfiguration();
        jdbc.setDriverClass("org.postgresql.Driver");
        jdbc.setConnectionURL("jdbc:postgresql://localhost:5432/dummy");
        jdbc.setUserId("dummy");
        jdbc.setPassword("dummy");
        context.setJdbcConnectionConfiguration(jdbc);
        return context;
    }

    @Test
    @DisplayName("schemaname プロパティを指定した場合にその値が利用される")
    void testCustomSchemaNameIsUsed() throws Exception {
        TestPlugin plugin = new TestPlugin(Set.of("t1", "t2"));
        plugin.setContext(newContext());

        Properties props = new Properties();
        props.setProperty("schemaname", "custom_schema");
        plugin.setProperties(props);

        assertThat(plugin.capturedSchema).isEqualTo("custom_schema");

        // allowlist 反映確認 (reflection で private フィールドを取得)
        Field f = SkipPartitionedWithPostgresTablesPlugin.class.getDeclaredField("allowlist");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<String> allowlist = (Set<String>) f.get(plugin);
        assertThat(allowlist).containsExactlyInAnyOrder("t1", "t2");
    }

    @Test
    @DisplayName("schemaname 未指定なら public が使われる")
    void testDefaultSchemaNameIsPublic() throws Exception {
        TestPlugin plugin = new TestPlugin(Set.of("a"));
        plugin.setContext(newContext());

        Properties props = new Properties(); // schemaname 指定なし
        plugin.setProperties(props);

        assertThat(plugin.capturedSchema).isEqualTo("public");

        Field f = SkipPartitionedWithPostgresTablesPlugin.class.getDeclaredField("allowlist");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<String> allowlist = (Set<String>) f.get(plugin);
        assertThat(allowlist).containsExactly("a");
    }

    @Test
    @DisplayName("caseInsensitive=true の場合に allowlistNormalized が小文字化される")
    void testCaseInsensitiveNormalizedList() throws Exception {
        TestPlugin plugin = new TestPlugin(Set.of("CamelCase", "MIXED_name"));
        plugin.setContext(newContext());

        Properties props = new Properties();
        props.setProperty("caseInsensitive", "true");
        // schemaname 未指定 => public
        plugin.setProperties(props);

        Field fNorm = SkipPartitionedWithPostgresTablesPlugin.class.getDeclaredField("allowlistNormalized");
        fNorm.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<String> normalized = (Set<String>) fNorm.get(plugin);
        assertThat(normalized)
                .containsExactlyInAnyOrder("camelcase", "mixed_name")
                .allSatisfy(s -> assertThat(s).isEqualTo(s.toLowerCase(Locale.ROOT)));
    }
}

