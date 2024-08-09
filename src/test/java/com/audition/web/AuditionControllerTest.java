package com.audition.web;

import static com.audition.Constants.ErrorMessages.INTERNAL_SERVER_ERROR;
import static com.audition.Constants.ErrorMessages.POST_ID_NOT_VALID_NUMBER_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.audition.common.exception.SystemException;
import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuditionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuditionIntegrationClient auditionIntegrationClient;

    @Test
    @SneakyThrows
    void whenValidPostIdPassed_resultsAreReturned() {

        int postId = 1;
        AuditionPost mockedResult = AuditionPost.builder()
            .id(postId)
            .title("sample title")
            .build();
        Mockito.when(auditionIntegrationClient.getPostById(any()))
            .thenReturn(mockedResult);

        mockMvc.perform(get("/v1/posts/".concat(String.valueOf(postId)))
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.id").value(postId))
            .andExpect(jsonPath("$.title").value(mockedResult.getTitle()));

    }

    @Test
    @SneakyThrows
    void givenPostIdWhenNotFoundErrorIsReturned() {

        int postId = 1;
        Mockito.when(auditionIntegrationClient.getPostById(any()))
            .thenThrow(new SystemException(
                String.format(
                    "Cannot find a Post with id - %s", postId), "Resource Not Found", HttpStatus.NOT_FOUND.value()
            ));

        mockMvc.perform(get("/v1/posts/".concat(String.valueOf(postId)))
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(jsonPath("$.title").value("Resource Not Found"))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.detail").value("Cannot find a Post with id - ".concat(String.valueOf(postId))));

    }

    @Test
    @SneakyThrows
    void givenPostIdWhenWhenUnknownErrorOccurredErrorIsReturned() {

        int postId = 1;
        Mockito.when(auditionIntegrationClient.getPostById(any()))
            .thenThrow(new SystemException(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value()));

        mockMvc.perform(get("/v1/posts/".concat(String.valueOf(postId)))
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.detail").value(INTERNAL_SERVER_ERROR));

    }

    @Test
    @SneakyThrows
    void givenInvalidPostIdErrorIsReturned() {

        mockMvc.perform(get("/v1/posts/".concat("78da7s"))
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.detail").value(POST_ID_NOT_VALID_NUMBER_ERROR));

    }


}