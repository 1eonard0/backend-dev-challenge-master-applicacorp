package com.directa24.main.challenge.service;

import com.directa24.main.challenge.entity.Movie;
import com.directa24.main.challenge.entity.MovieResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${eron.movies.service.url}")
    private String eronApiUrl;

    public Mono<List<Movie>> getMovies(int pageNumber){

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(this.eronApiUrl + "/api/movies/search")
                .queryParam("page", pageNumber);
        return webClientBuilder
                    .build()
                    .get()
                    .uri(uriComponentsBuilder.toUriString())
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .flatMap(monoResponse -> {
                        try {
                            MovieResponse movieResponse = objectMapper.readValue(monoResponse, MovieResponse.class);
                            return Mono.just(movieResponse.getData());
                        }catch(IOException e){
                            return Mono.error(new RuntimeException("Error converting from byte [] to MovieResponse", e));
                        }
                    })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(10))
                        .jitter(0.5)
                        .filter(throwable -> throwable instanceof WebClientResponseException && ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.TOO_MANY_REQUESTS))
                .doOnError(error -> log.error("Error while trying to process API response:{}", error.getMessage()));
    }
}
