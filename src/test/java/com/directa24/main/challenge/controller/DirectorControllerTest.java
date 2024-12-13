package com.directa24.main.challenge.controller;


import com.directa24.main.challenge.service.DirectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DirectorController.class)
class DirectorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DirectorService directorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void getGenerateViewAnexoYReportTestWithResults() throws Exception {

         List<String> mockList = Arrays.asList("Martin Scorsese", "Woody Allen");

         BDDMockito.BDDMyOngoingStubbing<List<String>> stubbing = given((List<String>)directorService.getTopDirectors(anyInt()));
         stubbing.willReturn(mockList);

         mockMvc.perform(get("/top-directors")
                         .param("threshold", "4"))
                 .andExpect(status().isOk());
     }
}
