package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import pablo127.almonds.Parse;
import pablo127.almonds.ParseUser;


public class GameRoom extends Game {
    Game game;
    SpriteBatch sbBatch;

    Skin skin;
    Stage stage;
    int nSHeight, nSWidth;
    Query q = new Query();
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
    boolean fkboi = false;

    JSONObject resultObject;

    ScreenControl screenControl;

    public void render() {
        if (!fkboi){
            create();
        }
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
        fkboi = true;
        nSHeight = Gdx.graphics.getHeight();
        nSWidth = Gdx.graphics.getWidth();
        sbBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        stage = new Stage(new ScreenViewport());
        btnAddGameroom = new TextButton("START", skin);
        final SelectBox selectBox = new SelectBox(skin);
        selectBox.setItems("Fun City", "Buns Town", "Meth Lab", "Cash Money", "Wet Cash", "Dog tail");

        window = new Window(screenControl.getName()+" Chat", skin);
        final TextField txtName = new TextField("", skin);
        txtName.setMessageText("Write a message...");
        list = new List<String>(skin);
        list.setItems(gamerooms.toArray());
        list.setSelected(list.getItems().size);
        scrollPane = new ScrollPane(list,skin);
        table = new Table(skin);
        gameroomTable = new Table(skin);
        gameTable = new Table(skin);
        table.left().top();
        table.add(window).width((nSWidth * (int) (nSWidth / 1.25)) / nSWidth).height((nSHeight * (int) (nSHeight / 1)) / nSHeight);
        table.add(gameroomTable);
        //scrollPane.setFillParent(true);
        window.setMovable(false);
        window.add(scrollPane).width((nSWidth * (int) (nSWidth / 1.25)) / nSWidth).height(((nSHeight * (int) (nSHeight / 1)) / nSHeight) - 100);
        window.row();
        window.add(txtName).width((nSWidth * (int) (nSWidth / 1.25)) / nSWidth).height(nSHeight * 70 / nSHeight);
        gameroomTable.add(btnAddGameroom).expand();
        gameroomTable.row();
        gameroomTable.add(selectBox).expand();
        table.debugCell();
        //table.debugTable();
        table.setFillParent(true);
        stage.addActor(table);
        Timer timer = new Timer();

        skin.getFont("default-font").scale(0.6f);
        skin.getFont("default-font").getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Timer.Task task = timer.scheduleTask(new Timer.Task() {
            @Override
            public void run () {
                final String requestContent = null;
                final Net.HttpRequest httpRequest;
                httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
                httpRequest.setUrl("https://api.parse.com/1/classes/chat/1GTPRERceY");
                httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
                httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());

                httpRequest.setContent(requestContent);
                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener(){
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        try {
                            jsonObject = new JSONObject(httpResponse.getResultAsString());
                            JSONArray results = (JSONArray) jsonObject.get("players");
                            gamerooms.clear();
                            for (int n = 0; n < results.length(); n++) {
                                gamerooms.add(results.getString(n));

                            }
                            list.clear();
                            list.setItems(gamerooms.toArray());
                        }catch (Exception e){}
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
        }, 0, 5);
        txtName.setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped(TextField textField, char key) {
                if (key == '\n'){
                    if (gamerooms.size()>23)gamerooms.remove(0);
                    textField.getOnscreenKeyboard().show(false);
                    gamerooms.add(ParseUser.getCurrentUser().getUsername().toString()+ ": "+textField.getText().toString());
                    list.setItems(gamerooms.toArray());
                    final Net.HttpRequest httpRequest;
                    httpRequest = new Net.HttpRequest(Net.HttpMethods.PUT);
                    httpRequest.setUrl("https://api.parse.com/1/classes/chat/1GTPRERceY");
                    httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
                    httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());
                    JSONObject json =new JSONObject();
                    JSONObject skills =new JSONObject();
                    skills.put("__op", "Add");
                    skills.put("objects", new JSONArray(Arrays.asList(ParseUser.getCurrentUser().getUsername().toString() + ": " + txtName.getText())));
                    json.put("players", skills);
                    httpRequest.setContent(json.toString());
                    Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener(){
                        @Override
                        public void handleHttpResponse(Net.HttpResponse httpResponse) {
                            System.out.println(httpResponse.toString());
                        }

                        @Override
                        public void failed(Throwable t) {
                            System.out.println(t.toString());
                        }

                        @Override
                        public void cancelled() {

                        }
                    });

                    textField.setText("");
                }
            }
        });
    }

    public void setScreenControl(ScreenControl screenControl_) {
        screenControl = screenControl_;
    }
}