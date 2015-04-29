package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import org.json.JSONArray;
import org.json.JSONObject;


import pablo127.almonds.Parse;
import pablo127.almonds.ParseObject;


public class GameRooms extends Game {
    Game game;
    SpriteBatch sbBatch;

    Skin skin;
    Stage stage;
    int nSHeight, nSWidth;
    Query q = new Query();
    ScrollPane scrollPane;
    List list;
    Table table;
    Table gameroomTable;
    JSONObject jsonObject;
    TextButton btnAddGameroom;
    TextButton btnRefresh;

    JSONObject resultObject;

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
    public void dispose() {

    }

    @Override
    public void create() {
        nSHeight = Gdx.graphics.getHeight();
        nSWidth = Gdx.graphics.getWidth();
        sbBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());
        final int nSelectbox = 0;
        final Label nameLabel = new Label("Name: ", skin);
        final Label mapLabel = new Label("Map: ", skin);
        final TextField txtName = new TextField("", skin);
        final SelectBox selectBox = new SelectBox(skin);
        selectBox.setItems("Fun City", "Buns Town", "Meth Lab", "Cash Money", "Wet Cash", "Dog tail");
        /*for (int i =0; i < 20;i++) {
        ParseObject pO = new ParseObject("gamerooms");
            pO.put("Name","some"+i);
        pO.put("map",1);
        pO.put("isJoinable",true);
        pO.saveInBackground();
        }*/
        btnAddGameroom = new TextButton("+", skin);
        btnRefresh = new TextButton("Refresh", skin);

        final TextButton btnAdd = new TextButton("Add", skin);
        gameroomTable = new Table(skin);
        table = new Table(skin);

        table.add("Gameroom").padRight(nSWidth / 6).padBottom(20);
        table.add("Status").padRight(nSWidth / 6).padBottom(20);
        table.add("Map").padRight(nSWidth / 4).padBottom(20);
        table.add(btnAddGameroom).height(100).width(100);
        //table.add(btnRefresh).height(100).width(100).padLeft(100);
        table.row();

        populateGmRms();
        nSHeight = Gdx.graphics.getHeight();
        nSWidth = Gdx.graphics.getWidth();
        sbBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        list = new List(skin);


        //list.setItems(table);
        scrollPane = new ScrollPane(table, skin);//Used to be list
        //table.add("Players Active");
        //table.add(list);
        //table.setPosition(450f,450f);
        //list.setPosition(450f,450f);


        final Table table2 = new Table();
        table2.setFillParent(true);

        table2.add(scrollPane).fill().expand();
        ScrollPane scrollPane2 = new ScrollPane(table2, skin);

        Window.WindowStyle windowStyle = new Window.WindowStyle(new BitmapFont(), Color.WHITE, skin.newDrawable("white", Color.BLACK));

        final Window window = new Window("test", windowStyle);
        window.setMovable(true);
        window.padTop(20);
        selectBox.setPosition(100, 100);
        selectBox.setHeight(50f);
        selectBox.setWidth(100f);
        selectBox.setSelected("Fun City");
        gameroomTable.add(nameLabel);
        gameroomTable.add(txtName).width(100);
        gameroomTable.row();
        gameroomTable.add(mapLabel);
        gameroomTable.add(selectBox);
        gameroomTable.row();
        gameroomTable.add(btnAdd).height(100).width(100);
        gameroomTable.setHeight(window.getHeight());
        gameroomTable.setWidth(window.getWidth());
        gameroomTable.setFillParent(true);
        window.addActor(gameroomTable);
        Timer timer = new Timer();
        Task task = timer.scheduleTask(new Task() {
            @Override
            public void run() {
                checkPopulate();
            }
        }, 1, 7);
        btnAddGameroom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.addActor(window);
            }
        });
        btnRefresh.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.clearChildren();
                table.add("Gameroom").padRight(nSWidth / 6).padBottom(20);
                table.add("Status").padRight(nSWidth / 6).padBottom(20);
                table.add("Map").padRight(nSWidth / 4).padBottom(20);
                table.add(btnAddGameroom).height(100).width(100);
                //table.add(btnRefresh).height(100).width(100).padLeft(100);
                table.row();
                populateGmRms();
            }
        });
        btnAdd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ParseObject pO = new ParseObject("gamerooms");
                pO.put("Name", txtName.getText());
                pO.put("map", selectBox.getSelectedIndex());
                pO.put("isJoinable", true);
                pO.saveInBackground();
                table.add(txtName.getText()).padRight(nSWidth / 6).padBottom(20);
                table.add("Open").padRight(nSWidth / 6).padBottom(20);
                table.add(selectBox.getSelected().toString()).padRight(nSWidth / 6).padBottom(20);
                window.remove();
            }
        });
        window.setPosition(nSWidth / 2, nSHeight / 2);
        window.setSize(500, 300);
        this.stage.addActor(table2);


    }

    public void setScreenControl(ScreenControl screenControl_) {
        screenControl = screenControl_;
    }

    public void checkPopulate() {
        String requestContent = null;
        final Net.HttpRequest httpRequest;
        httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("https://api.parse.com/1/classes/gamerooms/");
        httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
        httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());
        httpRequest.setContent(requestContent);
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {


            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                try {
                    jsonObject = new JSONObject(httpResponse.getResultAsString());
                    JSONArray results = (JSONArray) jsonObject.get("results");
                    if (results.length() >= table.getRows()) {
                        table.clearChildren();
                        table.add("Gameroom").padRight(nSWidth / 6).padBottom(20);
                        table.add("Status").padRight(nSWidth / 6).padBottom(20);
                        table.add("Map").padRight(nSWidth / 4).padBottom(20);
                        table.add(btnAddGameroom).height(100).width(100);
                        //table.add(btnRefresh).height(100).width(100).padLeft(100);
                        table.row();
                        populateGmRms();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
    }

    public void populateGmRms() {

        String requestContent = null;
        final Net.HttpRequest httpRequest;
        httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("https://api.parse.com/1/classes/gamerooms/");
        System.out.println(Parse.getRestAPIKey() + Parse.getApplicationId());
        httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
        httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());
        httpRequest.setContent(requestContent);
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {


            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                try {
                    jsonObject = new JSONObject(httpResponse.getResultAsString());
                    JSONArray results = (JSONArray) jsonObject.get("results");
                    for (int n = 0; n < results.length(); n++) {
                        resultObject = (JSONObject) results.get(n);
                        table.add(resultObject.get("Name").toString()).padRight(nSWidth / 6);
                        if (resultObject.get("isJoinable").equals(true)) {
                            table.add("Open").padRight(nSWidth / 6);
                        } else {
                            table.add("Closed").padRight(nSWidth / 4);
                        }
                        table.add(resultObject.get("map").toString()).padRight(nSWidth / 4);
                        table.pad(30f);
                        table.row();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
    }
}