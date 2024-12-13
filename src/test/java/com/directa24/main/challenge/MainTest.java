package com.directa24.main.challenge;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=local",
        "eron.movies.service.url=https://eron-movies.wiremockapi.cloud"
})
public class MainTest {

    @Test
    void contextLoads() {

    }
}
