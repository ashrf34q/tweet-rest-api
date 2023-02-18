package com.app.restapiclient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TweetDetailsDTO {
    private String created_at;
    private String text;
    private String screen_name;
    private String lang;
}
