package com.directa24.main.challenge.service;

import com.directa24.main.challenge.entity.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final MovieService movieService;

    public Map<String, Integer> countMoviesByDirector() {
        Map<String, Integer> dirCounts = new HashMap<>();
        int pageNumber = 1;
        List<Movie> movies;
        do{
            movies = movieService.getMovies(pageNumber++);
            for(Movie movie : movies){
                dirCounts.put(movie.getDirector(), dirCounts.getOrDefault(movie.getDirector(), 0) + 1);
            }
        }while(!movies.isEmpty());
        return dirCounts;
    }

    public List<String> getTopDirectors(int threshold) {
        Map<String, Integer> dirCounts = countMoviesByDirector();
        return dirCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }
}
