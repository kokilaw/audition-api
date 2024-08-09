package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditionService {

    private transient AuditionIntegrationClient auditionIntegrationClient;

    @Autowired
    public void setAuditionIntegrationClient(final AuditionIntegrationClient auditionIntegrationClient) {
        this.auditionIntegrationClient = auditionIntegrationClient;
    }

    public List<AuditionPost> getPosts() {
        return auditionIntegrationClient.getPosts();
    }

    public AuditionPost getPostById(final String postId, final Boolean includeComments) {
        if (includeComments) {
            return auditionIntegrationClient.getPostByIdIncludingComments(postId);
        }
        return auditionIntegrationClient.getPostById(postId);
    }

    public List<AuditionPostComment> getCommentsForPost(final String postId) {
        return auditionIntegrationClient.getCommentsByPost(postId);
    }

}
