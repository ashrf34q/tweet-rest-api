package com.app.restapiclient.controllers;

import com.app.restapiclient.model.TweetDetails;
import com.app.restapiclient.model.TweetDTO;
import com.app.restapiclient.model.TweetDetailsDTO;
import com.app.restapiclient.model.UserDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
This code defines four endpoint methods in a RESTful API, implemented using the Spring Boot framework.
The API is designed to work with JSON data, and each of the methods use an instance of the Jackson ObjectMapper
class to deserialize JSON data into Java objects, or to serialize Java objects into JSON.
 */

@RestController
@RequestMapping(MyController.BASE_URL)
public class MyController {

    public static final String BASE_URL = "/api/v1/tweets";

    RestTemplate restTemplate = new RestTemplate();

    private static final String RESOURCE_URL = "https://foyzulhassan.github.io/files/favs.json";

    private final ObjectMapper mapper = new ObjectMapper();

    // Load the JSON from the API into a string
    String json = restTemplate.getForEntity(RESOURCE_URL, String.class).getBody();

    /*

     This method is not mapped to a URI, but this controller class is mapped to /api/v1/tweets.
     So this method is mapped to the same URI as our controller class
    returns an array of TweetDTO objects that represents all the tweets in the system.
     */
    @GetMapping({"","/"})
    public TweetDTO[] getAllTweets() throws JsonProcessingException {
        setMapperVisibility(mapper);

        // deserialize the JSON data in the json string into an array of TweetDTO objects.
        return mapper.readValue(json, TweetDTO[].class);
    }

    /*
    This method configures the visibility of the ObjectMapper instance used for deserialization
     */
    private void setMapperVisibility(ObjectMapper mapper) {
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    // GET request mapped to /api/v1/tweets/threadlinks
    // This method gets the links from the text field of every tweet and groups them by tweet id
    @GetMapping("/threadlinks")
    public String getLinks() throws JsonProcessingException {

        setMapperVisibility(mapper);

        // Mapping JSON to array of tweetDetails POJOs
        TweetDetails[] tweetDetailsArr = mapper.readValue(json, TweetDetails[].class);

        // Pattern for recognizing a URL, based off RFC 3986
          final Pattern urlPattern = Pattern.compile(
                "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                        + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                        + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

          // Create a hashmap to store the id of every tweet as key, and the url found as value
          Map<Long, String> urlMap = new HashMap<>();

          // loop over the tweet details objects
        for(TweetDetails details : tweetDetailsArr){

            Matcher matcher = urlPattern.matcher(details.getText());

            while(matcher.find()){
                // If found a url, put it in the map
                int matchStart = matcher.start(1);
                int matchEnd = matcher.end();

                String s = details.getText().substring(matchStart, matchEnd);

                urlMap.put(details.getId(), s);
            }
        }

//        return map as JSON string
        return mapper.writeValueAsString(urlMap);
    }


    // GET Request to /api/v1/tweets/{id} where {id} is a placeholder for a tweet ID.
    @GetMapping("/{id}")
    public TweetDetailsDTO getTweetById(@PathVariable long id) throws JsonProcessingException {

        setMapperVisibility(mapper);

        // Mapping JSON to array of tweet details objects
        TweetDetails[] tweetDetailsArr = mapper.readValue(json, TweetDetails[].class);

        for(TweetDetails details : tweetDetailsArr) {
            if(details.getId() == id) {

                // Mapping User object from our Details object to our userDTO
                UserDTO userDTO = mapper.convertValue(details.getUser(), UserDTO.class);

                // Get user screen name from userDTO, set it to our details screen name attribute
                details.setScreen_name(userDTO.getScreen_name());

                // details to detailsDTO and return it
                return mapper.convertValue(details, TweetDetailsDTO.class);
            }
        }

        // No matching tweet id
       throw new RuntimeException("404 NOT FOUND!");
    }


    // GET Request mapped to /api/v1/tweets/user/{username} where {username} is a placeholder
    // for a Twitter username.
    @GetMapping("/user/{username}")
    public UserDTO getProfileInfoByUserName(@PathVariable String username) throws JsonProcessingException {

        setMapperVisibility(mapper);

        TweetDetails[] tweetDetailsArr = mapper.readValue(json, TweetDetails[].class);

        // Loop over tweets
        for(TweetDetails details : tweetDetailsArr) {
            UserDTO userDTO = mapper.convertValue(details.getUser(), UserDTO.class);

            // If user screen name equals username provided, then return the current userDTO
            if(userDTO.getScreen_name().equals(username)) {
                return userDTO;
            }
        }

//        If no matching user is found
        throw new RuntimeException("404 NOT FOUND!");
    }

}