package com.directa24.main.challenge.improvement;

import com.directa24.main.challenge.service.MovieService;
import com.directa24.main.challenge.service.RestTemplateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(properties = {
        "eron.movies.service.url=https://eron-movies.wiremockapi.cloud"
})
class PerformanceTest {

    @Autowired
    private RestTemplateService restTemplateService;

    @Autowired
    private MovieService movieService;

    @Test
    void testRestTemplatePerformance() {
        long startTime = System.currentTimeMillis();
        IntStream.rangeClosed(1, 3).forEach(i -> {
            restTemplateService.getMovies(i);
            assertTrue(true);
        });
        long endTime = System.currentTimeMillis();
        System.out.println("RestTemplate Time: " + (endTime - startTime) + " ms");
    }

    @Test
    void testWebClientPerformance() {
        long startTime = System.currentTimeMillis();
        Flux.range(1, 3)
                .flatMap(i -> movieService.getMovies(i));
        assertTrue(true);
        long endTime = System.currentTimeMillis();
        System.out.println("WebClient Time: " + (endTime - startTime) + " ms"); }
}
