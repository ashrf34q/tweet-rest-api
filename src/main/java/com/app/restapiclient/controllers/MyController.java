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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping(MyController.BASE_URL)
public class MyController {

    public static final String BASE_URL = "/api/v1/tweets";

    RestTemplate restTemplate = new RestTemplate();

    private static final String RESOURCE_URL = "https://foyzulhassan.github.io/files/favs.json";


    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping
    public TweetDTO[] getAllTweets() throws JsonProcessingException {

        // Needs to be fixed later
        ResponseEntity<String> response = restTemplate.getForEntity(RESOURCE_URL, String.class);

        String json = response.getBody();

        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper.readValue(json, TweetDTO[].class);
    }

    @GetMapping("/threadlinks")
    public String getLinks() throws JsonProcessingException {

        // map to tweet details, so we can use the text field

        String json = restTemplate.getForEntity(RESOURCE_URL, String.class).getBody();

        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Mapping JSON to DTO POJO
        TweetDetails[] tweetDetailsArr = mapper.readValue(json, TweetDetails[].class);


        // Pattern for recognizing a URL, based off RFC 3986
          final Pattern urlPattern = Pattern.compile(
                "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                        + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                        + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

          Map<Long, String> urlMap = new HashMap<>();

        for(TweetDetails details : tweetDetailsArr){

            Matcher matcher = urlPattern.matcher(details.getText());

            while(matcher.find()){
                int matchStart = matcher.start(1);
                int matchEnd = matcher.end();

                String s = details.getText().substring(matchStart, matchEnd);

                urlMap.put(details.getId(), s);
            }
        }

        return mapper.writeValueAsString(urlMap);
    }



    @GetMapping("/{id}")
    public TweetDetailsDTO getTweetById(@PathVariable long id) throws JsonProcessingException {

        // fix later
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


    @GetMapping("/user/{username}")
    public UserDTO getProfileInfoByUserName(@PathVariable String username) throws JsonProcessingException {
        String json = restTemplate.getForEntity(RESOURCE_URL, String.class).getBody();

        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        TweetDetails[] tweetDetailsArr = mapper.readValue(json, TweetDetails[].class);

        for(TweetDetails details : tweetDetailsArr) {
            UserDTO userDTO = mapper.convertValue(details.getUser(), UserDTO.class);

            if(userDTO.getScreen_name().equals(username)){
                return userDTO;
            }
        }

        throw new RuntimeException("404 NOT FOUND!");
    }

}
