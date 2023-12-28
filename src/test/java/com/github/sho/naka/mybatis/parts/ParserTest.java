package com.github.sho.naka.mybatis.parts;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("全体のテスト 1回");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("全体のテスト終了後 1回");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("各テストメソッドごとに1回");
    }

    @AfterEach
    void afterEach() {
        System.out.println("各テストメソッドごとの終了に1回");
    }

    @Test
    @DisplayName("仮テスト")
    void test() {
        assertEquals(5, 5, "仮テストの確認");
    }
}
