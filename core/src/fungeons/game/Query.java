package fungeons.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;

import pablo127.almonds.Parse;

/**
 * Created by Gaurab on 2015-04-19.
 */
public class Query {
    public String content = "";
    public Net.HttpRequest gamerooms(){


        String requestContent = null;
        Net.HttpRequest httpRequest;
        httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("https://api.parse.com/1/classes/gamerooms/");
        System.out.println(Parse.getRestAPIKey() + Parse.getApplicationId());
        httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
        httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());
        httpRequest.setContent(requestContent);


        return httpRequest;
    }
    public String setup(String content2){

        System.out.println(content2);
        content = content2;
        return content;
    }
}
