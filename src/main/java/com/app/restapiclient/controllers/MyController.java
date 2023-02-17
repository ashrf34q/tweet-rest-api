package com.app.restapiclient.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MyController.BASE_URL)
public class MyController {

    public static final String BASE_URL = "/api/v1/tweets";

    /*
    TODO: Get all tweets available in the archive (create time, id, and text)
     */

    /*
    TODO: Based on tweet id given in the request, return details of the tweet (created_at, text, screen_name, lang)
     */

    /*
    TODO: Get detailed profile info (name, location, description) given user screen name
     */

}
