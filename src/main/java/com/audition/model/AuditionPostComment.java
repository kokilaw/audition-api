package com.audition.model;

import lombok.Data;

@Data
public class AuditionPostComment {

    private int id;
    private int postId;
    private String name;
    private String email;
    private String body;

}
