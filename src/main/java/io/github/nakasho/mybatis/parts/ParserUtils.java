package io.github.nakasho.mybatis.parts;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ParserUtils {

    /**
     * table名の_を.に変更し、最後の要素はアッパーキャメルケースに変更する
     *
     * @param tableName table名の要素
     * @return
     */
    public static String packageAndFileName(String tableName){
        String[] tableNameParts = tableName
                .replaceAll("public.", "")
                .split("_");
        String MapperName =
                Arrays
                        .stream(tableNameParts)
                        .map( e -> e.substring(0, 1).toUpperCase() + e.substring(1))
                        .collect(Collectors.joining());
        tableNameParts[tableNameParts.length - 1] = MapperName;
        return String.join(".", tableNameParts);
    }
}
