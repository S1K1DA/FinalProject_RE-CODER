package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testGetUserById() {
        // 데이터베이스에서 ID가 1인 사용자 조회
        User user = userMapper.getUserByIdTest(1L);

        // 결과 확인
        assertNotNull(user, "User should not be null");
        assertEquals(1L, user.getId(), "User ID should be 1");
        assertEquals("John Doe", user.getName(), "User name should be 'John Doe'");
        assertEquals("john.doe@example.com", user.getEmail(), "User email should be 'john.doe@example.com'");
    }
}
