package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

import pablo127.almonds.Parse;
import pablo127.almonds.ParseException;
import pablo127.almonds.ParseObject;
import pablo127.almonds.SaveCallback;

//When you join , screencontrol.maps() should be the same as one from the gameroom. When you create your screencontrol.maps should be the same as the map you select.
public class GameRooms implements Screen {
    Game game;
    SpriteBatch sbBatch;

    Skin skin;
    Stage stage;
    int nSHeight, nSWidth, Results = -1;
    ScrollPane scrollPane;
    List list;
    Table gameroomTable;
    Table gameTable;
    Window.WindowStyle windowStyle;
    Window window;
    String []arsMaps = {"Fun City", "Buns Town", "Frogosaurus",
            "Fun City", "Fun Leaf Clover", "Fun Mountain"};
    int ctpos = 0;
    int pos = 0;
    Table table;
    JSONObject jsonObject;
    TextButton btnAddGameroom;
    TextButton btnExit;
    TextButton btnJoin;
    ArrayList<String> gamerooms = new ArrayList<String>();
    TextButton btnRefresh;
    TextButton.TextButtonStyle btnWhiteStyle;


    TextureAtlas Atlas;
    TextureAtlas.AtlasRegion Region;
    TextureRegion BGWall;
    Drawable dbtnWhite;

    JSONObject resultObject;

    ScreenControl screenControl;


    @Override
    public void show() {

        nSHeight = Gdx.graphics.getHeight();
        nSWidth = Gdx.graphics.getWidth();
        sbBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        Atlas= new TextureAtlas(Gdx.files.internal("Fungeons_3.pack"));
        Region=Atlas.findRegion("Button 1");
        dbtnWhite = new TextureRegionDrawable(Region);
        Region=Atlas.findRegion("Button 2");
        Drawable dbtnWhiteDn = new TextureRegionDrawable(Region);
        final BitmapFont ButtonFont = new BitmapFont(Gdx.files.internal("FungeonsFont.fnt"));
        final BitmapFont ButtonFontAlt = new BitmapFont(Gdx.files.internal("FungeonsFontAlt.fnt"));
        ButtonFont.setScale(nSWidth/512);//will implement when Texture pack is fixed
        ButtonFontAlt.setScale(nSWidth/512);
        btnWhiteStyle = new TextButton.TextButtonStyle(dbtnWhite,dbtnWhiteDn,dbtnWhite,ButtonFont);
        skin.add("btnWhiteStyle",btnWhiteStyle);


        final int nSelectbox = 0;
        final Label nameLabel = new Label("Name: ", skin);
        final Label mapLabel = new Label("Map: ", skin);
        final TextField txtName = new TextField("", skin);

        final SelectBox selectBox = new SelectBox(skin);
        selectBox.setItems("BunsTown", "Dawgg", "Frogosaurus", "FunCity", "FunLeafClover", "FunMountain");
        // Maps
        //
        final Drawable dBGWall;
        Region=Atlas.findRegion("BG Wall Brick");
        BGWall= Region;
        dBGWall= new TextureRegionDrawable(BGWall);
        Region=Atlas.findRegion("WindowBG Square");
        BGWall= Region;
        Drawable dWinBG= new TextureRegionDrawable(BGWall);

        int i = 11;
        String s = "1GTPRERceY";
        table = new Table(skin);
        table.setBackground(dBGWall);
        ParseObject pO = new ParseObject("gamerooms");

        btnAddGameroom = new TextButton("Add Game", skin, "btnWhiteStyle");

        btnRefresh = new TextButton("Refresh", skin, "btnWhiteStyle");
        btnExit = new TextButton("Exit",skin, "btnWhiteStyle");
        btnJoin = new TextButton ("Join",skin, "btnWhiteStyle");
        final TextButton btnAdd = new TextButton("Add", skin, "btnWhiteStyle");
        final TextButton btnExitAdd = new TextButton("Exit", skin, "btnWhiteStyle");

        populateGmRms();
        sbBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        stage = new Stage(new ScreenViewport());
        list = new List(skin);


        gameroomTable = new Table(skin);

        gameTable = new Table(skin);
        gameroomTable.setBackground(dWinBG);

        window = new Window("", skin);
        window.setBackground(dWinBG);
        skin.getFont("default-font").scale(nSHeight / 480);
        window.setMovable(true);
        window.padTop(nSHeight / 16);
        window.setWidth(nSWidth/2f);
        window.setHeight(nSHeight/2f);
        //
        selectBox.setPosition(nSWidth / 10f, nSHeight / 7f);
        selectBox.setSize(nSWidth / 6f, nSHeight / 8f);
        selectBox.setSelected("Fun City");


        btnAddGameroom.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) { // Add a gameroom
                System.out.println("Added");
                float winW=window.getWidth(), winH=window.getHeight();
                window.setVisible(true);
                gameroomTable.setPosition(0, 0);
                nameLabel.setFontScale(2f);
                gameroomTable.add(nameLabel).size(winH/6f).padTop(winH / 16f);
                txtName.getStyle().font.setScale(2f);
                gameroomTable.add(txtName).left().size(winW/2f, winH/6f)
                        .padTop(winH/16f);
                gameroomTable.row();
                mapLabel.setFontScale(2f);
                gameroomTable.add(mapLabel).size(winH / 6f).padTop(winH / 16f);
                gameroomTable.add(selectBox).size(winW / 4f, winH / 6f)
                        .padTop(winH / 16f);//
                gameroomTable.row();
                gameroomTable.add(btnAdd).height(winH/6f).width(winW/3f)
                        .padTop(winH / 16f).padBottom(winH / 16f);
                gameroomTable.add(btnExitAdd).height(winH/6f).width(winW/3f)
                        .padTop(winH/16f).padBottom(winH/16f);
                gameroomTable.setHeight(winH);
                gameroomTable.setWidth(winW);
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
                pO2.put("gameroom",0);
                pO2.put("game",txtName.getText());
                pO2.save(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        window.remove();
                        screenControl.setName(txtName.getText(), true);
                        screenControl.setnScreen(7, 4);//was 4,2
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
                screenControl.setName(list.getSelected().toString(), false);
                screenControl.setnScreen(7, 4);//was 4,2
            }
        });
        window.setModal(true);
        window.setVisible(false);
        window.setPosition(nSWidth / 3, nSHeight / 3);

        stage.addActor(window);
    }

    @Override
    public void render(float delta) {
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
    public void hide() {

    }

    @Override
    public void dispose() {
    }

    public void create() {

    }

    public void setScreenControl(ScreenControl screenControl_) {
        screenControl = screenControl_;
    }

    public void populateGmRms() {//Grab all the gamerooms from the server
        String requestContent = null;
        final Net.HttpRequest httpRequest;
        httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl("https://api.parse.com/1/classes/gamerooms/");
        httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
        httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());
        httpRequest.setContent(requestContent);
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() { // Listens to the server response
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                try {
                    jsonObject = new JSONObject(httpResponse.getResultAsString());
                    JSONArray results = (JSONArray) jsonObject.get("results");
                    if (results.length()!= Results) {
                        Results = results.length();
                        gamerooms.clear();
                        table.clearChildren();
                        for (int n = 0; n < results.length(); n++) {
                            resultObject = (JSONObject) results.get(n);
                            gamerooms.add(resultObject.get("Name").toString());
                        }
                        list.clearItems();
                        list.setItems(gamerooms.toArray());
                        table.add(btnRefresh).height(nSHeight/7f).width(nSWidth/4f);
                        scrollPane = new ScrollPane(list, skin);
                        scrollPane.setColor(0.2f, 0.2f, 0.2f, 0.7f);//makes it translucent asaposed to opaque or transparent
                        scrollPane.addListener(new EventListener() {
                            @Override
                            public boolean handle(Event event) {

                                if (pos > ((int) scrollPane.getScrollY() + 9) || pos < ((int) scrollPane.getScrollY() - 9)) {
                                    ctpos = 0;
                                } else if (ctpos > 3) {
                                    JSONArray results = (JSONArray) jsonObject.get("results");
                                    float winW=window.getWidth(),winH=window.getHeight();
                                    for (int n = 0; n < results.length(); n++) {
                                        resultObject = (JSONObject) results.get(n);
                                        if (resultObject.get("Name").toString().equals(list.getSelected().toString()) && gameTable.getRows() < 3) {
                                            resultObject = (JSONObject) results.get(n);
                                            gameTable.add(resultObject.get("Name").toString())
                                                    .colspan(2).center().pad(winH/16f);

                                            gameTable.row();
                                            gameTable.add(arsMaps[Integer.parseInt(resultObject.get("map").toString())])
                                                    .colspan(2).center().pad(winH/16f);

                                            gameTable.row();
                                            if (resultObject.get("isJoinable").toString().equals("true")) {
                                                gameTable.add("Open").colspan(2).center().pad(winH/16f);
                                            } else {
                                                gameTable.add("Closed").colspan(2).center().pad(winH/16f);
                                            }
                                            gameTable.row();
                                            gameTable.add(btnExit).width(winW/3f).height(winH/6f).padLeft(winW/16f).padBottom(winH/8f);
                                            gameTable.add(btnJoin).width(winW/3f).height(winH/6f).padRight(winW/16f)
                                                    .padLeft(winW/16f).padBottom(winH/8f);
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
                        table.add(btnAddGameroom).height(nSHeight/7f).width(nSWidth/4f);
                        table.row().height(nSHeight);

                        table.add(scrollPane).width(nSWidth).colspan(2);

                        table.setFillParent(true);

                        table.center().top().pad(0);
                        stage.addActor(table);
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
