package com.directa24.main.challenge.controller;

import com.directa24.main.challenge.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping("/top-directors")
    public List<String> getTopDirectors(@RequestParam int threshold){
        return directorService.getTopDirectors(threshold);
    }

}
