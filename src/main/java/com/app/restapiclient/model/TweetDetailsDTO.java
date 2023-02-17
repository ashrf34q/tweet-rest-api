package com.app.restapiclient.model;

import lombok.Data;

@Data
public class TweetDetailsDTO {
    private long id;
    private String created_at;
    private String text;
    private String screen_name;
    private String lang;
}
