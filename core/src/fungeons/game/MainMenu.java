package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import pablo127.almonds.LogInCallback;
import pablo127.almonds.Parse;
import pablo127.almonds.ParseException;
import pablo127.almonds.ParseUser;
import pablo127.almonds.SignUpCallback;

/**
 * Created by Gaurab on 2015-04-13.
 */

public class MainMenu implements Screen {
    //Stage is an InputProcessor. When it receives input events, it fires them on the appropriate actors.
    Stage stage;
    SpriteBatch batch;
    TextureAtlas Atlas;
    TextureAtlas.AtlasRegion Region;
    TextureRegion BGWall;
    Drawable dbtnWhite;
    TextButton.TextButtonStyle btnWhiteStyle;
    Window.WindowStyle windowStyle;
    Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    Label.LabelStyle dialogStyle;


    ScreenControl screenControl;
    Game game;

    @Override
    public void show() {
        final int nScreenHeight=Gdx.graphics.getHeight(), nScreenWidth=Gdx.graphics.getWidth();
        Parse.initialize("ayDwhTuCZaESDYV4OvdRIWHjX2DW2DuUWwGB6BTk", "s2NFEowokTeIhqxB1eYFTBNNY1hE6dVPoSDVDCaB");//initialize parse with our keys
        batch = new SpriteBatch();
        Drawable dBGWall;
        Atlas= new TextureAtlas(Gdx.files.internal("Fungeons_3.pack"));
        Region=Atlas.findRegion("BG Wall Brick Wide");
        BGWall= Region;

        dBGWall= new TextureRegionDrawable(BGWall);
        Region=Atlas.findRegion("Button 1");
        TextureRegion btnWhite = Region;
        dbtnWhite = new TextureRegionDrawable(btnWhite);
        skin.add("btnWhite",dbtnWhite);
        Region=Atlas.findRegion("Button 2");
        TextureRegion btnWhiteDn = Region;
        Drawable dbtnWhiteDn = new TextureRegionDrawable(btnWhiteDn);
        skin.add("btnWhiteDn",dbtnWhiteDn);
        final BitmapFont ButtonFont = new BitmapFont(Gdx.files.internal("FungeonsFont.fnt"));
        final BitmapFont ButtonFontAlt = new BitmapFont(Gdx.files.internal("FungeonsFontAlt.fnt"));
        ButtonFont.setScale(nScreenWidth/512);//will implement when Texture pack is fixed
        ButtonFontAlt.setScale(nScreenWidth/512);
        btnWhiteStyle = new TextButton.TextButtonStyle(dbtnWhite,dbtnWhiteDn,dbtnWhite,ButtonFont);

        skin.add("btnWhiteStyle",btnWhiteStyle);
        skin.getFont("default-font").setScale(nScreenWidth/674f);//for text buttons :D
        stage = new Stage(new ScreenViewport());
        Table table = new Table();
        Gdx.input.setInputProcessor(stage);

        Drawable dBGWinWall;
        Region=Atlas.findRegion("WindowBG Square");
        dBGWinWall=new TextureRegionDrawable(Region);
        windowStyle = new Window.WindowStyle(ButtonFont,Color.WHITE,dBGWinWall);
        skin.add("windowStyle",windowStyle);
        final ExitDialog exitDialog = new ExitDialog("", skin, "windowStyle");
        exitDialog.setPosition(nScreenWidth/8f,nScreenHeight/4f);
        final RegisterDialog registerDialog = new RegisterDialog("", skin, "windowStyle");
        registerDialog.setPosition(nScreenWidth/8f,nScreenHeight/4f);
        dialogStyle = new Label.LabelStyle(ButtonFontAlt, Color.WHITE);

        final Label passwordLabel = new Label("Password: ", skin);
        final Label userLabel = new Label("Username: ", skin);
        final TextField txtPassword = new TextField("", skin);
        final TextButton button = new TextButton("LOGIN", skin, "btnWhiteStyle");
        final TextButton btnH2p = new TextButton("HOW TO PLAY", skin, "btnWhiteStyle");

        final TextButton btnOffline = new TextButton("OFFLINE", skin, "btnWhiteStyle");
        final TextButton btnRegister = new TextButton("REGISTER", skin, "btnWhiteStyle");
        final TextField txtUsername = new TextField("", skin);
        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        txtUsername.setMessageText("ex.John101");
        txtUsername.setAlignment(Align.left);
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.up = skin.newDrawable("white", Color.WHITE);
        textButtonStyle.down = skin.newDrawable("white", Color.WHITE);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);

        skin.add("default", textButtonStyle);

        txtPassword.setMessageText("*****");
        txtPassword.setPasswordCharacter('*');
        txtPassword.setPasswordMode(true);
        txtUsername.pack();
        txtUsername.setHeight(100);

        table.row().padTop(nScreenHeight/10);
        table.add(userLabel).padBottom(nScreenHeight / 14);
        table.add(txtUsername).width(nScreenWidth / 4).height(nScreenHeight / 10).padBottom(nScreenHeight / 14).padRight(nScreenWidth/12);
        table.add(passwordLabel).padBottom(nScreenHeight / 14);
        table.add(txtPassword).width(nScreenWidth / 4).height(nScreenHeight / 10).padBottom(nScreenHeight / 14);
        table.row();
        table.add(button).width((int)(nScreenWidth/1.8)).height(nScreenHeight/7).padBottom(nScreenHeight/12).center().colspan(4);
        table.row();
        table.add(btnRegister).width((int) (nScreenWidth / 1.8)).height(nScreenHeight / 7).padBottom(nScreenHeight / 12).center().colspan(4);
        table.row();

        table.add(btnH2p).width(nScreenWidth / 3).height(nScreenHeight / 8).expand().bottom().left().colspan(2);
        table.add(btnOffline).width(nScreenWidth / 3).height(nScreenHeight / 8).expand().bottom().right().colspan(2);
        table.center().top().pad(nScreenHeight/16,nScreenWidth/16,nScreenHeight/16, nScreenWidth/16);

        table.setFillParent(true);

        stage.addActor(table);
        table.setBackground(dBGWall);
        txtUsername.setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped(TextField textField, char key) {
                if (key == '\n') textField.getOnscreenKeyboard().show(false);
            }
        });

        //http://stackoverflow.com/questions/21488311/libgdx-how-to-create-a-button
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    final Net.HttpRequest httpRequest;
                    httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
                    httpRequest.setUrl("http://backend-fungeons.rhcloud.com/login");
                    String authStr = txtUsername.getText()+":"+txtPassword.getText();
                    // encode data on your side using BASE64
                    byte[] bytesEncoded = Base64.encodeBase64(authStr .getBytes());
                    String authEncoded = new String(bytesEncoded);
                    httpRequest.setHeader("Authorization", "Basic "+authEncoded);
                    Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                        @Override
                        public void handleHttpResponse(Net.HttpResponse httpResponse) {
                            String res = httpResponse.getResultAsString();
                            if(!res.equals("Unauthorized")){
                                JSONObject response = new JSONObject(res);
                                screenControl.setAuthToken(response.get("token").toString());
                                exitDialog.text(" Welcome, " + txtUsername.getText() + "! ", dialogStyle).padTop(nScreenHeight/40f);//Opens up a dialog box saying you successfully logged in. When you press OK, it will redirect you to the lobby
                                exitDialog.show(stage);
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
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        btnRegister.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) { // Register button
                if (txtUsername.getText() != "" && txtPassword.getText() !="") {
                    try {
                        final Net.HttpRequest httpRequest;
                        httpRequest = new Net.HttpRequest(Net.HttpMethods.POST);
                        httpRequest.setUrl("http://backend-fungeons.rhcloud.com/register");
                        httpRequest.setHeader("Content-Type", "application/json");
                        final JSONObject userInfo = new JSONObject();
                        userInfo.append("username", txtUsername.getText());
                        userInfo.append("password", txtPassword.getText());
                        System.out.println(userInfo.toString());
                        httpRequest.setContent(userInfo.toString());
                        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                            @Override
                            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                                JSONObject response = new JSONObject(httpResponse.getResultAsString());
                                System.out.println(response.toString());
                                System.out.println(response.get("message"));
                                if(response.get("message").equals("Successfully registered user, " + txtUsername.getText())){
                                    //Opens up a dialog box saying you successfully logged in. When you press OK, it will redirect you to the lobby
                                    registerDialog.text("Welcome " + txtUsername.getText() + "! Please Login!", dialogStyle).padTop(nScreenHeight/40f);
                                    registerDialog.show(stage);
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
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }else{
                    System.out.println("Failed registration");
                }
            }
        });
        btnOffline.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenControl.setnScreen(7,8);
            }
        });
        btnH2p.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenControl.setnScreen(7,6);//CHANGE BACK TO (7, 6)
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        batch.begin();
        stage.draw();

        batch.end();
    }

    @Override
    public void resize(int width, int height){
    }
    public void create(){

    }
    public void setScreenControl(ScreenControl screenControl_){
        screenControl = screenControl_;
    }
    @Override
    public void pause(){

    }
    @Override
    public void resume(){

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public  void dispose(){
        stage.dispose();
        batch.dispose();
        Atlas.dispose();
    }
    public  class ExitDialog extends Dialog {
        public ExitDialog(String title, Skin skin_,String windowStyle_){
            super(title, skin, "windowStyle");

        }
        public ExitDialog(String title, Skin skin_){
            super(title, skin);

        }

        public ExitDialog(String title,WindowStyle windowStyle_){
            super(title, windowStyle);
        }
        {

            int nScreenWidth=Gdx.graphics.getWidth(), nScreenHeight=Gdx.graphics.getHeight();

            setSize(nScreenWidth, nScreenHeight);
            setScale(1.5f,2f);
            button("OK", this,btnWhiteStyle).pad(nScreenHeight / 20f, nScreenWidth / 20f, nScreenHeight / 40f, nScreenWidth / 20f);

        }
        @Override
            protected void result(Object object){
            screenControl.setnScreen(7,2);//was 2,1
            }
    }

    public  class RegisterDialog extends Dialog {
        public RegisterDialog(String title, Skin skin_,String windowStyle_){
            super(title, skin, "windowStyle");

        }
        public RegisterDialog(String title, Skin skin_){
            super(title, skin);

        }

        public RegisterDialog(String title,WindowStyle windowStyle_){
            super(title, windowStyle);
        }
        {

            int nScreenWidth=Gdx.graphics.getWidth(), nScreenHeight=Gdx.graphics.getHeight();

            setSize(nScreenWidth, nScreenHeight);
            setScale(1f,3f);
            button("OK", this,btnWhiteStyle).pad(nScreenHeight / 20f, nScreenWidth / 20f, nScreenHeight / 40f, nScreenWidth / 20f);

        }
        @Override
        protected void result(Object object){

        }
    }

}
