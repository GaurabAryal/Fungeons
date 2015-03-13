package fungeons.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

//http://www.indiedb.com/games/endure/news/parsecom-restful-api-in-libgdx-game/
public class Parse implements HttpResponseListener {

    private URL url = null;
    private URLConnection conn = null;
    private String app_id;
    private String app_key;

    public Parse(){
        try {
            url = new URL("https://api.parse.com/1/classes/login/");
            app_id = "";
            app_key = "";

        } catch (MalformedURLException e) {

            System.out.println(e.getMessage());
        }
    }

    public void addLogin(String sUserName, String sPassword){
        // LibGDX NET CLASS
        HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
        httpPost.setUrl("https://api.parse.com/1/classes/login/");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("X-Parse-Application-Id", app_id);
        httpPost.setHeader("X-Parse-REST-API-Key", app_key);
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        Register register = new Register();
        register.setCreds(sUserName, sPassword);
        httpPost.setContent(json.toJson(register));
        main n = new main();
        Gdx.net.sendHttpRequest(httpPost,Parse.this);
    }

    public boolean add_score(){
        // USING JAVA IO AND NET CLASS
        try {
            conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("X-Parse-Application-Id", app_id);
            conn.setRequestProperty("X-Parse-REST-API-Key", app_key);
            conn.setRequestProperty("Content-type", "application/json");
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write("{\"score\": 1337, \"user\": \"CarelessLabs GDX\"}");

            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String decodedString;
            while ((decodedString = in.readLine()) != null) {
                System.out.println(decodedString);
            }
            in.close();
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean get_score(){
        // USING JAVA IO AND NET CLASS
        try {
            conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("X-Parse-Application-Id", app_id);
            conn.setRequestProperty("X-Parse-REST-API-Key", app_key);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void get_net_score(){
        // LibGDX NET CLASS
        HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
        httpGet.setUrl("https://api.parse.com/1/classes/score/");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("X-Parse-Application-Id", app_id);
        httpGet.setHeader("X-Parse-REST-API-Key", app_key);
        Gdx.net.sendHttpRequest(httpGet,Parse.this);
    }

    @Override
    public void handleHttpResponse(HttpResponse httpResponse) {
        final int statusCode = httpResponse.getStatus().getStatusCode();
        System.out.println(statusCode + " " + httpResponse.getResultAsString());
    }

    @Override
    public void failed(Throwable t) {
        System.out.println(t.getMessage());
    }

    @Override
    public void cancelled() {

    }
}
class Register{
    private String username;
    private String password;
    void setCreds(String sUsr, String sPass){
        username = sUsr;
        password = sPass;
    }
}