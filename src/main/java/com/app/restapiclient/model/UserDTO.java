package com.app.restapiclient.model;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String screen_name;
    private String location;
    private String description;
}