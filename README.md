# tweet-rest-api

REST Java Spring Client that consumes tweets from a [REST API](https://foyzulhassan.github.io/files/favs.json), and responds to the following requests:

- Get all tweets (create time, id, and tweet text) available in the archive.
- Get a list of all external links (all links that appear in the tweet text field. Use regular expressions to extract the links, the links should be grouped based on tweet ids.
- Get the details about a given tweet (given the tweet’s id). Details of the tweet include created_at, text, screen_name, lang.
- Get detailed profile information(name, location, description) about a given Twitter user (given the user’s screen name)


To use this API, clone the repository into your IDE (preferably Intellij or Eclipse).
Run it, then open Postman and start sending HTTP requests to the following endpoints.

Endpoints:

- There is a global endpoint '/api/v1/tweets' mapped to the controller class containing below endpoints. This means that for any request on the endpoints below, it needs to have this URI at the beginning.
- GET '/' - This endpoint returns an array of all tweets in the system as a JSON response.
- GET '/threadlinks' - This endpoint returns a JSON response containing a map of tweet IDs to URLs contained within their text fields.
- GET '/{id}' - This endpoint takes a tweet ID and returns the tweet details in JSON format.
- GET '/user/{username}' - This endpoint takes a username and returns the user's profile information as JSON.

Helper functions:

setMapperVisibility - This function sets the visibility of the object mapper and disables deserialization feature of the object mapper.

Classes:

- TweetDTO - This class represents a tweet in the system.
- TweetDetailsDTO - This class represents the details of a tweet in the system.
- UserDTO - This class represents a user in the system.

Overall, the code reads a JSON file that contains data about tweets and users and maps it to corresponding DTO objects, which can be returned by the RESTful API endpoints. The setMapperVisibility function is a helper function that helps with the mapping process. The endpoints use regular expressions to extract URLs and user information from the tweet data. The endpoints also handle errors if a tweet or user cannot be found.