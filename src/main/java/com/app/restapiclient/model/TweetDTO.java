package com.app.restapiclient.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TweetDTO {

    private long id;

    private String created_at;

    private String text;
}
