package com.company.project.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Greeting Entity Unit Tests")
class GreetingTest {

    @Test
    @DisplayName("Tạo đối tượng Greeting bằng default constructor")
    void testDefaultConstructor() {
        Greeting greeting = new Greeting();
        assertEquals(0, greeting.getId());
        assertNull(greeting.getName());
    }

    @Test
    @DisplayName("Tạo đối tượng Greeting bằng constructor có name")
    void testNameConstructor() {
        Greeting greeting = new Greeting("Hello World");
        assertEquals("Hello World", greeting.getName());
    }

    @Test
    @DisplayName("Tạo đối tượng Greeting bằng constructor có cả id và name")
    void testFullConstructor() {
        Greeting greeting = new Greeting(1, "Hello Spring");
        assertEquals(1, greeting.getId());
        assertEquals("Hello Spring", greeting.getName());
    }

    @Test
    @DisplayName("Cập nhật id và name thông qua setter/getter")
    void testSettersAndGetters() {
        Greeting greeting = new Greeting();
        greeting.setId(10);
        greeting.setName("Updated Name");

        assertEquals(10, greeting.getId());
        assertEquals("Updated Name", greeting.getName());
    }

    @Test
    @DisplayName("Kiểm tra equals và hashCode dựa trên trường name")
    void testEqualsAndHashCode() {
        Greeting greeting1 = new Greeting(1, "Same Name");
        Greeting greeting2 = new Greeting(2, "Same Name");
        Greeting greeting3 = new Greeting(1, "Different Name");

        // Cùng name => equals trả về true (theo logic override trong Greeting)
        assertEquals(greeting1, greeting2);
        assertEquals(greeting1.hashCode(), greeting2.hashCode());

        // Khác name => equals trả về false
        assertNotEquals(greeting1, greeting3);

        // So sánh với null hoặc object kiểu khác
        assertNotEquals(null, greeting1);
        assertNotEquals("Một string bất kỳ", greeting1);
    }
}

