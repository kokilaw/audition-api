package com.audition.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import java.util.List;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@Setter
@ExtendWith(MockitoExtension.class)
class AuditionServiceTest {

    @Mock
    AuditionIntegrationClient auditionIntegrationClient;
    private AuditionService auditionService;

    @BeforeEach
    public void setup() {
        auditionService = new AuditionService();
        auditionService.setAuditionIntegrationClient(auditionIntegrationClient);
    }

    @Test
    void givenAvailablePostsWhenRequestedIsReturned() {
        Mockito.when(auditionIntegrationClient.getPosts()).thenReturn(List.of(new AuditionPost()));
        final List<AuditionPost> posts = auditionService.getPosts();
        assertEquals(1, posts.size());
    }

    @Test
    void givenPostWithCommentsWhenRequestedIsReturned() {
        final String postId = "POST_ID";
        final AuditionPostComment auditionPostComment = new AuditionPostComment();
        final AuditionPost auditionPost = new AuditionPost();
        auditionPost.setComments(List.of(auditionPostComment));
        Mockito.when(auditionIntegrationClient.getPostByIdIncludingComments(eq(postId)))
            .thenReturn(auditionPost);
        final AuditionPost result = assertDoesNotThrow(() -> auditionService.getPostById(postId, Boolean.TRUE));
        assertNotNull(result);
        assertEquals(1, result.getComments().size());
    }

    @Test
    void givenPostWithCommentsWhenNotRequestedIsNotReturned() {
        final String postId = "POST_ID";
        final AuditionPost auditionPost = new AuditionPost();
        Mockito.when(auditionIntegrationClient.getPostById(eq(postId)))
            .thenReturn(auditionPost);
        final AuditionPost result = assertDoesNotThrow(() -> auditionService.getPostById(postId, Boolean.FALSE));
        assertNotNull(result);
    }

    @Test
    void givenCommentsWhenRequestedIsReturned() {
        final String postId = "POST_ID";
        final AuditionPostComment auditionPostComment = new AuditionPostComment();
        Mockito.when(auditionIntegrationClient.getCommentsByPost(eq(postId)))
            .thenReturn(List.of(auditionPostComment));
        final List<AuditionPostComment> result = assertDoesNotThrow(
            () -> auditionService.getCommentsForPost(postId));
        assertEquals(1, result.size());
    }

}