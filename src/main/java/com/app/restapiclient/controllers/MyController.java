package com.app.restapiclient.controllers;

import com.app.restapiclient.domain.TweetDetails;
import com.app.restapiclient.model.TweetDTO;
import com.app.restapiclient.model.TweetDetailsDTO;
import com.app.restapiclient.model.UserDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping(MyController.BASE_URL)
public class MyController {

    public static final String BASE_URL = "/api/v1/tweets";

    RestTemplate restTemplate = new RestTemplate();

    private static final String RESOURCE_URL = "https://foyzulhassan.github.io/files/favs.json";


    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping
    public TweetDTO[] getAllTweets() throws JsonProcessingException {

        ResponseEntity<String> response = restTemplate.getForEntity(RESOURCE_URL, String.class);

        String json = response.getBody();

        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper.readValue(json, TweetDTO[].class);
    }


    /*
    TODO: Get a list of all tweets appearing in the text field. Links should be grouped based on tweet ids.
     */

    @GetMapping("/{id}")
    public TweetDetailsDTO getTweetById(@PathVariable long id) throws JsonProcessingException {

        String json = restTemplate.getForEntity(RESOURCE_URL, String.class).getBody();

        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Mapping JSON to DTO POJO
        TweetDetails[] tweetDetailsArr = mapper.readValue(json, TweetDetails[].class);

        for(TweetDetails details : tweetDetailsArr) {
            if(details.getId() == id) {

                // Mapping User object from our DetailsDTO to our userDTO
                UserDTO userDTO = mapper.convertValue(details.getUser(), UserDTO.class);

                details.setScreen_name(userDTO.getScreen_name());

                return mapper.convertValue(details, TweetDetailsDTO.class);
            }
        }
       throw new RuntimeException("404 NOT FOUND!");
    }

    /*
    TODO: Get detailed profile info (name, location, description) given user screen name
     */

//    @GetMapping("/{userName}")
//    public ResponseEntity<> getProfileInfoByUserName(@PathVariable String userName) {
//    }

}
