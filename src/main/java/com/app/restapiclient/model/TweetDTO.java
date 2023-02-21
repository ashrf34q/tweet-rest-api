package com.app.restapiclient.model;

import lombok.Data;

@Data
public class TweetDTO {

    // id, create time and text of a tweet

    private long id;

    private String created_at;

    private String text;
}
