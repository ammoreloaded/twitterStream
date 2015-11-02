package com.ankit.services.daemon;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class TwitterStreamConnector extends Thread {
	
	private String latestTweet;
	private String tweetQuery;
    private int tweetCount;    
    
	public TwitterStreamConnector(String query){
		tweetQuery = query;
	}

	public static final String StreamURI = "https://stream.twitter.com/1.1/statuses/filter.json";
	
    public String getLatestTweet(){
        return latestTweet;
    }

    public int getTweetCount(){
        return tweetCount;
    }
	
	public void run(){
		try{
			
            System.out.println("Start fetching twitter API.");

            // Enter your consumer key and secret below
            OAuthService service = new ServiceBuilder().provider(TwitterApi.class).apiKey("Consumer key").apiSecret("Secret Key").build();

            Token accessToken = new Token("OAuth access token", "OAuth secret token access");

            // Creating request
            System.out.println("Connecting to Twitter Public Stream");
            OAuthRequest request = new OAuthRequest(Verb.POST, StreamURI);
            request.addHeader("version", "HTTP/1.1");
            request.addHeader("host", "stream.twitter.com");
            request.setConnectionKeepAlive(true);
            request.addHeader("user-agent", "Twitter Stream Reader");
            request.addBodyParameter("track", tweetQuery); // Set keywords you'd like to track here
            service.signRequest(accessToken, request);
            Response response = request.send();

            // This will be reading the twitter stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getStream()));

            String line;
            while ((line = reader.readLine()) != null) {
            	 latestTweet = line;
                 tweetCount++;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
	}	
}