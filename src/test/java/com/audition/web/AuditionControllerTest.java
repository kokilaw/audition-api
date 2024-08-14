package com.audition.web;

import static com.audition.Constants.ErrorMessages.INTERNAL_SERVER_ERROR;
import static com.audition.Constants.ErrorMessages.POST_ID_NOT_VALID_NUMBER_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.audition.common.exception.SystemException;
import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import lombok.Setter;
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

@Setter
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuditionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuditionIntegrationClient auditionIntegrationClient;

    private static final String POST_URI = "/v1/posts/";
    private static final String APPLICATION_JSON_HEADER = "application/json";

    @Test
    @SneakyThrows
    void whenValidPostIdPassedResultsAreReturned() {

        final int postId = 1;
        final AuditionPost mockedResult = AuditionPost.builder()
            .id(postId)
            .title("sample title")
            .build();
        Mockito.when(auditionIntegrationClient.getPostById(any()))
            .thenReturn(mockedResult);

        mockMvc.perform(get(POST_URI.concat(String.valueOf(postId)))
                .contentType(APPLICATION_JSON_HEADER))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.id").value(postId))
            .andExpect(jsonPath("$.title").value(mockedResult.getTitle()));

    }

    @Test
    @SneakyThrows
    void givenPostIdWhenNotFoundErrorIsReturned() {

        final int postId = 1;
        Mockito.when(auditionIntegrationClient.getPostById(any()))
            .thenThrow(new SystemException(
                String.format(
                    "Cannot find a Post with id - %s", postId), "Resource Not Found", HttpStatus.NOT_FOUND.value()
            ));

        mockMvc.perform(get(POST_URI.concat(String.valueOf(postId)))
                .contentType(APPLICATION_JSON_HEADER))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(jsonPath("$.title").value("Resource Not Found"))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.detail").value("Cannot find a Post with id - ".concat(String.valueOf(postId))));

    }

    @Test
    @SneakyThrows
    void givenPostIdWhenWhenUnknownErrorOccurredErrorIsReturned() {

        final int postId = 1;
        Mockito.when(auditionIntegrationClient.getPostById(any()))
            .thenThrow(new SystemException(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value()));

        mockMvc.perform(get(POST_URI.concat(String.valueOf(postId)))
                .contentType(APPLICATION_JSON_HEADER))
            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.detail").value(INTERNAL_SERVER_ERROR));

    }

    @Test
    @SneakyThrows
    void givenInvalidPostIdErrorIsReturned() {

        mockMvc.perform(get(POST_URI.concat("78da7s"))
                .contentType(APPLICATION_JSON_HEADER))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.detail").value(POST_ID_NOT_VALID_NUMBER_ERROR));

    }


}