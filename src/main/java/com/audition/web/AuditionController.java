package com.audition.web;

import static com.audition.Constants.ErrorMessages.POST_ID_NOT_VALID_NUMBER_ERROR;

import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.service.AuditionService;
import com.audition.util.ValidationUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class AuditionController {

    private transient AuditionService auditionService;

    @Autowired
    public void setAuditionService(final AuditionService auditionService) {
        this.auditionService = auditionService;
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts() {
        return auditionService.getPosts();
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPosts(
        @PathVariable("id") final String postId,
        @RequestParam(required = false, defaultValue = "false") final Boolean includeComments
    ) {
        ValidationUtil.isValidNumber(postId, POST_ID_NOT_VALID_NUMBER_ERROR);
        return auditionService.getPostById(postId, includeComments);
    }

    @RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPostComment> getCommentsForPost(@PathVariable("id") final String postId) {
        ValidationUtil.isValidNumber(postId, POST_ID_NOT_VALID_NUMBER_ERROR);
        return auditionService.getCommentsForPost(postId);
    }


}
