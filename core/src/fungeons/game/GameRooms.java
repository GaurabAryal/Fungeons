package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import pablo127.almonds.Parse;
import pablo127.almonds.ParseException;
import pablo127.almonds.ParseObject;
import pablo127.almonds.SaveCallback;


public class GameRooms extends Game {
    Game game;
    SpriteBatch sbBatch;

    Skin skin;
    Stage stage;
    int nSHeight, nSWidth;
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

    TextureAtlas Atlas;
    TextureAtlas.AtlasRegion Region;
    TextureRegion BGWall;
    Drawable dbtnWhite;

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

        final Drawable dBGWall;
        Atlas= new TextureAtlas(Gdx.files.internal("Fungeons_2.pack"));
        Region=Atlas.findRegion("BG Wall Brick");
        BGWall= Region;
        dBGWall= new TextureRegionDrawable(BGWall);

        int i = 11;
        String s = "1GTPRERceY";
        table = new Table(skin);
        ParseObject pO = new ParseObject("gamerooms");

//        for (int i =0; i < 1;i++) {
//            pO.
////            pO.put("Name","GameRoom"+i);
////            pO.put("map",1);
////            pO.put("isJoinable",true);
////            p
////            pO2.put("game", "GameRoom"+i);
////            pO.saveInBackground();
////            pO2.saveInBackground();
//        }
        btnAddGameroom = new TextButton("+", skin);
        btnRefresh = new TextButton("Refresh", skin);
        btnExit = new TextButton("Exit",skin);
        btnJoin = new TextButton ("Join",skin);
        final TextButton btnAdd = new TextButton("Add", skin);
        final TextButton btnExitAdd = new TextButton("Exit", skin);

        populateGmRms();
        sbBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        stage = new Stage(new ScreenViewport());
        list = new List(skin);


        gameroomTable = new Table(skin);

        gameTable = new Table(skin);

        window = new Window("test", skin);
        skin.getFont("default-font").scale(nSWidth / 1794 * 1.2f);
        skin.getFont("default-font").getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        window.setMovable(true);
        window.padTop(nSHeight / 16);
        selectBox.setPosition(100, 100);
        selectBox.setHeight(50f);
        selectBox.setWidth(100f);
        selectBox.setSelected("Fun City");
       // list.setVisible(false);
        btnAddGameroom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) { // Add a gameroom
                System.out.println("Added");
                window.setModal(true);
                window.setVisible(true);
                gameroomTable.setPosition(0,0);//clearly there is an issue. everytime you exit and click on add again, table moves down. WTF!!!
                gameroomTable.add(nameLabel);
                gameroomTable.add(txtName).width(100);
                gameroomTable.row();
                gameroomTable.add(mapLabel);
                gameroomTable.add(selectBox);
                gameroomTable.row();
                gameroomTable.add(btnAdd).height(100).width(100);
                gameroomTable.add(btnExitAdd).height(100).width(100);
                gameroomTable.setHeight(window.getHeight());
                gameroomTable.setWidth(window.getWidth());
                gameroomTable.setFillParent(true);
                window.addActor(gameroomTable);
                stage.addActor(window);
            }
        });
        btnRefresh.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {// this will refresh the gamerooms so pull up new gamerooms if they are created
                populateGmRms();
            }
        });
        btnAdd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {// This will add a new gameroom to the server
                ParseObject pO = new ParseObject("gamerooms");
                pO.put("Name", txtName.getText());
                pO.put("map", selectBox.getSelectedIndex());
                pO.put("isJoinable", true);
                pO.put("start", false);
                pO.saveInBackground();
                ParseObject pO2 = new ParseObject("chat");
                pO2.put("gameroom",1);
                pO2.put("game",txtName.getText());
                pO2.save(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        window.remove();
                        screenControl.setName(txtName.getText(),true);
                        screenControl.setnScreen(4);
                    }
                });

            }
        });

        btnExitAdd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {// this will refresh the gamerooms so pull up new gamerooms if they are created
               window.clearChildren();
               window.setVisible(false);
               window.remove();
            }
        });

        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {//if you don't want to join a game, just exit it
                gameTable.clearChildren();
                window.clearChildren();
                window.setVisible(false);
                window.remove();
            }
        });
        btnJoin.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {//This will take you to a specific game
                screenControl.setName(list.getSelected().toString(),false);
                screenControl.setnScreen(4);
            }
        });
        window.setModal(true);
        window.setVisible(false);
        window.setPosition(nSWidth / 2, nSHeight / 2);
        window.setSize(500, 300);

        stage.addActor(window);
    }

    public void setScreenControl(ScreenControl screenControl_) {
        screenControl = screenControl_;
    }

    public void populateGmRms() {//Grab all the gamerooms from the server
        String requestContent = null;
        final Net.HttpRequest httpRequest;
        httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("https://api.parse.com/1/classes/gamerooms/");
        System.out.println(Parse.getRestAPIKey() + Parse.getApplicationId());
        httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
        httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());
        httpRequest.setContent(requestContent);
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() { // Listens to the server response
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                gamerooms.clear();
                table.clearChildren();
                try {
                    jsonObject = new JSONObject(httpResponse.getResultAsString());
                    JSONArray results = (JSONArray) jsonObject.get("results");
                    for (int n = 0; n < results.length(); n++) {
                        resultObject = (JSONObject) results.get(n);
                        gamerooms.add(resultObject.get("Name").toString());
                    }
                    list.clearItems();
                    list.setItems(gamerooms.toArray());
                    table.add(btnRefresh).height(100).width(100);
                    //table.row();
                    scrollPane = new ScrollPane(list,skin);
                    scrollPane.addListener(new EventListener() {
                        @Override
                        public boolean handle(Event event) {

                            if(pos>((int) scrollPane.getScrollY()+9) || pos<((int)scrollPane.getScrollY()-9)){
                                //System.out.println(scrollPane.getScrollY() +"    "+ pos);
                                ctpos=0;
                            }
                            else if (ctpos>3 ){
                                JSONArray results = (JSONArray) jsonObject.get("results");
                                for (int n = 0; n < results.length(); n++) {
                                    resultObject = (JSONObject) results.get(n);
                                    //System.out.println(list.getSelected().toString() + resultObject.get("Name").toString());
                                    if (resultObject.get("Name").toString().equals(list.getSelected().toString())&&gameTable.getRows()<3) {
                                        resultObject = (JSONObject) results.get(n);
                                        gameTable.add(resultObject.get("Name").toString());
                                        gameTable.row();
                                        gameTable.add(arsMaps[Integer.parseInt(resultObject.get("map").toString())]);
                                        gameTable.row();
                                        if (resultObject.get("isJoinable").toString().equals("true")) {
                                            gameTable.add("Open");
                                        } else {
                                            gameTable.add("Closed");
                                        }
                                        gameTable.row();
                                        gameTable.add(btnExit).width(100).height(50);
                                        gameTable.add(btnJoin).width(100).height(50);
                                        break;
                                    }
                                }
                                window.clear();
                                window.setVisible(true);
                                window.add(gameTable);
                                stage.addActor(window);
                            }
                            pos = (int) scrollPane.getScrollY();
                            ctpos++;
                            return false;
                        }
                    });
                    table.add(btnAddGameroom).height(100).width(100);
                    table.row().height(nSHeight);
                    //scrollPane.setFillParent(true);

                    table.add(scrollPane).width(nSWidth).colspan(2);

                    table.setFillParent(true);
                    //   table.setPosition(nSWidth/2,nSHeight);

                    table.center().top().pad(0);
                    stage.addActor(table);
                    //stage.addActor(scrollPane);
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