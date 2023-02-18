package com.app.restapiclient.domain;

import lombok.Data;

@Data
public class TweetDetails {
    private long id;
    private String created_at;
    private String text;
    private String screen_name;
    private String lang;

    private Object user;
}
