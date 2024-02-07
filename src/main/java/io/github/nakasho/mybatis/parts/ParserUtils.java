package io.github.nakasho.mybatis.parts;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utils
 */
public class ParserUtils {

    /**
     * Change _ in the table name to . and change the last element to upper camel case.
     *
     * @param tableName tablename
     * @return after package and name
     */
    public static String packageAndFileName(String tableName) {
        String[] tableNameParts = tableName
                .replaceAll("public.", "")
                .split("_");
        String MapperName =
                Arrays
                        .stream(tableNameParts)
                        .map(e -> e.substring(0, 1).toUpperCase() + e.substring(1))
                        .collect(Collectors.joining());
        tableNameParts[tableNameParts.length - 1] = MapperName;
        return String.join(".", tableNameParts);
    }
}
