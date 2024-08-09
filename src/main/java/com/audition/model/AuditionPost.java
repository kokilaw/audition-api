package com.audition.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class AuditionPost {

    private int userId;
    private int id;
    private String title;
    private String body;
    private List<AuditionPostComment> comments;

    public List<AuditionPostComment> getComments() {
        if (comments != null) {
            return Collections.unmodifiableList(comments);
        }
        return Collections.emptyList();
    }

    public void setComments(final List<AuditionPostComment> comments) {
        this.comments = Collections.unmodifiableList(comments);
    }

}
