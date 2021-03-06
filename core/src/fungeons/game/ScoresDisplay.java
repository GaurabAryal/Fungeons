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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import org.json.JSONArray;
import org.json.JSONObject;

import pablo127.almonds.Parse;


public class ScoresDisplay implements Screen {
    Game game;
    SpriteBatch sbBatch;
    boolean bCanCreate = false;
    Skin skin;
    Stage stage;
    int nSHeight, nSWidth, length = 0;
    JSONObject jsonObject;

    TextureAtlas Atlas;
    TextureRegion Region;

    ScreenControl screenControl;


    @Override
    public void show() {
        bCanCreate = true;
        nSHeight = Gdx.graphics.getHeight();
        nSWidth = Gdx.graphics.getWidth();
        sbBatch = new SpriteBatch();

        Atlas= new TextureAtlas(Gdx.files.internal("Fungeons_3.pack"));

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());
        skin.getFont("default-font").scale(2.0f);
        final Table scoresTable = new Table(skin);
        Region=Atlas.findRegion("BG Wall Brick Wide");
        Drawable dBGWall = new TextureRegionDrawable(Region);
        scoresTable.setBackground(dBGWall);

        Region=Atlas.findRegion("Button 1");
        Drawable dbtnUp = new TextureRegionDrawable(Region);
        skin.add("btnUp",dbtnUp);
        Region=Atlas.findRegion("Button 2");
        Drawable dbtnDn = new TextureRegionDrawable(Region);
        skin.add("btnDn", dbtnDn);
        final BitmapFont ButtonFont = new BitmapFont(Gdx.files.internal("FungeonsFont.fnt"));
        ButtonFont.setScale(nSWidth / 512);//will implement when Texture pack is fixed
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle(dbtnUp,dbtnDn,dbtnUp,ButtonFont);
        skin.add("btnStyle",btnStyle);
        final Button btnRestart = new TextButton("Go back to the Gameroom", skin, "btnStyle");
        final Button btnLobby = new TextButton("Go to the Lobby", skin, "btnStyle");
        final Timer timer = new Timer();
        Timer.Task task = timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
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
                                for (int i = results.length()-1; i >= 0; i--) {
                                    scoresTable.row();
                                    scoresTable.add(results.getString(i));
                                }
                                if (length<4){
                                    for (int i = length; i < 4; i++) {
                                        scoresTable.row();
                                        scoresTable.add("In progress...");
                                    }
                                }else{
                                    scoresTable.row();
                                    scoresTable.add(btnRestart).height(nSHeight/8f).width(nSWidth/2f)
                                            .padTop(nSHeight/16f);
                                    scoresTable.row();
                                    scoresTable.add(btnLobby).height(nSHeight/8f).width(nSWidth/2f)
                                            .padTop(nSHeight/16f).padBottom(nSHeight/8f);
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
        Timer.Task task2 = timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                final String requestContent = null;
                final Net.HttpRequest httpRequest;
                httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
                httpRequest.setUrl("https://api.parse.com/1/classes/chat/"+screenControl.getChatId());
                httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
                httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());

                httpRequest.setContent(requestContent);
                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        try {

                            jsonObject = new JSONObject(httpResponse.getResultAsString());

                            if (jsonObject.get("gameroom").toString().equals("1")){
                                screenControl.setnScreen(7,4);//was 4,5

                                timer.stop();
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

        btnRestart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {//This will take you to a specific game~~
                bCanCreate = false;
                length = 0;
                if (screenControl.Owner) {
                    final Net.HttpRequest httpRequest;
                    httpRequest = new Net.HttpRequest(Net.HttpMethods.PUT);
                    httpRequest.setUrl("https://api.parse.com/1/classes/chat/" + screenControl.getChatId());
                    httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
                    httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());
                    JSONObject json = new JSONObject();
                    JSONObject skills = new JSONObject();
                    skills.put("__op", "Remove");
                    skills.put("objects", jsonObject.get("scores"));
                    json.put("scores", skills);
                    System.out.println(json.toString());
                    httpRequest.setContent(json.toString());
                    Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                        @Override
                        public void handleHttpResponse(Net.HttpResponse httpResponse) {
                            final Net.HttpRequest httpRequest;
                            httpRequest = new Net.HttpRequest(Net.HttpMethods.PUT);
                            httpRequest.setUrl("https://api.parse.com/1/classes/chat/" + screenControl.getChatId());
                            httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
                            httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());
                            JSONObject json = new JSONObject();
                            json.put("start", false);
                            httpRequest.setContent(json.toString());
                            Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                                @Override
                                public void handleHttpResponse(Net.HttpResponse httpResponse) {

                                    screenControl.setnScreen(7,4);//was 4,5
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

                        @Override
                        public void failed(Throwable t) {
                            System.out.println(t.toString());
                        }

                        @Override
                        public void cancelled() {

                        }
                    });

                }
            }
        });
        btnLobby.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {//This will take you to a specific game ~~
                bCanCreate = false;
                if (screenControl.Owner) {
                    final String requestContent = null;
                    final Net.HttpRequest httpRequest;
                    httpRequest = new Net.HttpRequest(Net.HttpMethods.DELETE);
                    httpRequest.setUrl("https://api.parse.com/1/classes/chat/" + screenControl.getChatId());
                    httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
                    httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());

                    httpRequest.setContent(requestContent);
                    Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                        @Override
                        public void handleHttpResponse(Net.HttpResponse httpResponse) {
                            length = 0;
                            screenControl.setnScreen(7,2);//was 2,5
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
            }
        });
        stage.addActor(scoresTable);

    }

    @Override
    public void render(float delta) {
        if (!bCanCreate)create();
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(0, 0, 0, 1);
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

  }