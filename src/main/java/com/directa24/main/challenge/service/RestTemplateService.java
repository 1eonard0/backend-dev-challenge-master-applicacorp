package com.directa24.main.challenge.service;

import com.directa24.main.challenge.entity.Movie;
import com.directa24.main.challenge.entity.MovieResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestTemplateService {

    private final RestTemplate restTemplate;

    @Value("${eron.movies.service.url}")
    private String eronApiUrl;

    public List<Movie> getMovies(int pageNumber){
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
