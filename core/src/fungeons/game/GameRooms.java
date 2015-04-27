package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import org.json.JSONObject;

import java.util.Scanner;

import pablo127.almonds.Parse;


public class GameRooms extends Game {
    Game game;
    SpriteBatch sbBatch;

    Skin skin;
    Stage stage;
    int nSHeight, nSWidth;
    Query q = new Query();
    ScrollPane scrollPane;

    ScreenControl screenControl;
    public void render() {
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        sbBatch.begin();
        stage.draw();
        sbBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose(){

    }
    @Override
    public void create() {
        nSHeight = Gdx.graphics.getHeight();
        nSWidth = Gdx.graphics.getWidth();
        sbBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());
        /*for (int i =0; i < 20;i++) {
        ParseObject pO = new ParseObject("gamerooms");
            pO.put("Name","some"+i);
        pO.put("map",1);
        pO.put("isJoinable",true);
        pO.saveInBackground();
        }*/
        String requestContent = null;
        Net.HttpRequest httpRequest;
        httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("https://api.parse.com/1/classes/gamerooms/");
        System.out.println(Parse.getRestAPIKey() + Parse.getApplicationId());
        httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
        httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());
        httpRequest.setContent(requestContent);
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {


            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Scanner sin = new Scanner(httpResponse.getResultAsStream());

                final JSONObject ob = new JSONObject(sin.next());
                System.out.println("ssa");
                System.out.println(ob.getString("Name")+"fk");
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
    }
    public void setScreenControl(ScreenControl screenControl_){
        screenControl = screenControl_;
    }
}