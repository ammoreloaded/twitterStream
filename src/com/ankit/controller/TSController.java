package com.ankit.controller;

import static spark.Spark.get;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spark.Request;
import spark.Response;
import spark.Route;

import com.ankit.services.daemon.TwitterStreamConnector;

@WebServlet(description = "Main front controller handling all the requests", urlPatterns = { "/TSController" })
public class TSController extends HttpServlet {
	
	private TwitterStreamConnector strConn = null;
	
    public TSController() {
    }

	public void init(ServletConfig config) throws ServletException {
		//Start the connection when server starts
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String query = request.getParameter("tweetQuery");
		//setPort(Integer.parseInt(System.getenv("PORT"))); //Uncomment this for Heroku
		
		TwitterStreamConnector strConn = new TwitterStreamConnector(query);
		strConn.start();
		
        get(new Route("/index") {
            @Override
            public Object handle(Request request, Response response) {
            	return "<h1>Tweet Count: " + Integer.toString(strConn.getTweetCount()) + "</h1>" +
                        "<h2>Latest Payload: " + strConn.getLatestTweet() + "</h2>";
            }
        });		
	}
	
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}