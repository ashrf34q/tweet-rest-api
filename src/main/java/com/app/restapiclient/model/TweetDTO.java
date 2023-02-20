package com.app.restapiclient.model;

import lombok.Data;

@Data
public class TweetDTO {

    private long id;

    private String created_at;

    private String text;
}
