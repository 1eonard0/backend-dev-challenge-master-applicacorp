package com.directa24.main.challenge.service;

import com.directa24.main.challenge.entity.Movie;
import com.directa24.main.challenge.entity.MovieResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final RestTemplate restTemplate;

    @Value("${eron.movies.service.url}")
    private String eronApiUrl;

    @Cacheable("movieCache")
    public List<Movie> getMovies(int pageNumber){
        //Just will be printed when the cache needs to be updated or during the first time
        log.info("Hitting the external API");
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(this.eronApiUrl + "/api/movies/search")
                .queryParam("page", pageNumber);
        byte[] responseBytes = restTemplate.getForObject(uriComponentsBuilder.toUriString(), byte [].class);

        if (responseBytes != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                MovieResponse response = objectMapper.readValue(responseBytes, MovieResponse.class);
                return response.getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
}
