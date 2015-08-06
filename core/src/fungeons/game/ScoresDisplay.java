package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pablo127.almonds.Parse;
import pablo127.almonds.ParseException;
import pablo127.almonds.ParseObject;
import pablo127.almonds.SaveCallback;


public class ScoresDisplay extends Game {
    Game game;
    SpriteBatch sbBatch;
    boolean bCanCreate = false;
    Skin skin;
    Stage stage;
    int nSHeight, nSWidth, length = 0;
    ScrollPane scrollPane;
    List list;
    Table gameroomTable;
    Table gameTable;
    Window.WindowStyle windowStyle;
    Window window;
    String []arsMaps = {"Fun City", "Buns Town", "Meth Lab", "Cash Money", "Wet Cash", "Dog tail"};

    boolean check = true;
    int ctpos = 0;
    int pos = 0;
    Table table;
    JSONObject jsonObject;
    TextButton btnAddGameroom;
    TextButton btnExit;
    TextButton btnJoin;
    ArrayList<String> gamerooms = new ArrayList<String>();
    TextButton btnRefresh;

    JSONObject resultObject;

    ScreenControl screenControl;

    public void render() {

        if (!bCanCreate)create();
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
    public void dispose() {

    }

    @Override
    public void create() {
        bCanCreate = true;
        nSHeight = Gdx.graphics.getHeight();
        nSWidth = Gdx.graphics.getWidth();
        sbBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());
        skin.getFont("default-font").scale(2.0f);
        final Table scoresTable = new Table(skin);
        final Button btnRestart = new TextButton("Go back to the Gameroom", skin);
        final Button btnLobby = new TextButton("Go to the Lobby", skin);
        final Timer timer = new Timer();
        Timer.Task task = timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() { //Call the server to update messages every 5 seconds
            /***Load up the scores to display***/
            final String requestContent = null;
            final Net.HttpRequest httpRequest2;
            httpRequest2 = new Net.HttpRequest(Net.HttpMethods.GET);
            httpRequest2.setUrl("https://api.parse.com/1/classes/chat/"+screenControl.getChatId());
            httpRequest2.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
            httpRequest2.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());

            httpRequest2.setContent(requestContent);
            Gdx.net.sendHttpRequest(httpRequest2, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    try {

                        jsonObject = new JSONObject(httpResponse.getResultAsString());
                        JSONArray results = (JSONArray) jsonObject.get("scores");
                        if (results.length()>length){
                            length = results.length();
                            scoresTable.reset();
                            scoresTable.add("Scores");
                            for (int i = 0; i < results.length(); i++) {
                                scoresTable.row();
                                scoresTable.add(results.getString(i));
                            }
                            if (length<4){
                                for (int i = 0; i < results.length(); i++) {
                                    scoresTable.row();
                                    scoresTable.add("In progress...");
                                }
                            }else{
                                scoresTable.row();
                                scoresTable.add(btnRestart).width(nSWidth);
                                scoresTable.row();
                                scoresTable.add(btnLobby).width(nSWidth);
                            }
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void failed(Throwable t) {
                    System.out.println(t.toString());
                }

                @Override
                public void cancelled() {

                }
            });
            }
        }, 1, 5);

        scoresTable.setFillParent(true);
        stage.addActor(scoresTable);

    }

    public void setScreenControl(ScreenControl screenControl_) {
        screenControl = screenControl_;
    }

  }