package com.directa24.main.challenge.service;

import com.directa24.main.challenge.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final MovieService movieService;

    public Mono<Map<String, Integer>> countMoviesByDirector() {
        return Flux.range(1, Integer.MAX_VALUE)
                .flatMap(this.movieService::getMovies)
                .takeUntil(List::isEmpty)
                .flatMap(Flux::fromIterable)
                .collect(Collectors.groupingBy(Movie::getDirector, Collectors.counting()))
                .map( map -> {
                    Map<String, Integer> dirCounts = new HashMap<>();
                    map.forEach((k, v) -> dirCounts.put(k, v.intValue()));
                    return dirCounts;
                });
    }

    public Mono<List<String>> getTopDirectors(int threshold) {
        return countMoviesByDirector()
                .map( dirCount -> dirCount.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList()));
    }
}
