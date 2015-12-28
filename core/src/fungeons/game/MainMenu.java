package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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

import pablo127.almonds.LogInCallback;
import pablo127.almonds.Parse;
import pablo127.almonds.ParseException;
import pablo127.almonds.ParseUser;
import pablo127.almonds.SignUpCallback;

/**
 * Created by Gaurab on 2015-04-13.
 */
public class MainMenu implements Screen {
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

   // BitmapFont mockFont= new BitmapFont(Gdx.files.internal("mockFont.fnt"));
    //Texture img =  new Texture("bgimg2.jpeg");

    ScreenControl screenControl;
    Game game;

    public void render(){


    }

    @Override
    public void show() {
        int nScreenHeight=Gdx.graphics.getHeight(), nScreenWidth=Gdx.graphics.getWidth();
        Parse.initialize("ayDwhTuCZaESDYV4OvdRIWHjX2DW2DuUWwGB6BTk", "s2NFEowokTeIhqxB1eYFTBNNY1hE6dVPoSDVDCaB");//initialize parse with our keys
        batch = new SpriteBatch();


        Drawable dBGWall;
        Atlas= new TextureAtlas(Gdx.files.internal("Fungeons_3.pack"));
        Region=Atlas.findRegion("BG Wall Brick Wide");
        BGWall= Region;

      /*  for(int i=0;i<(Gdx.graphics.getWidth()+100)/Region.getRegionWidth();i++){
            for(int j=0;j<(Gdx.graphics.getHeight()+100)/Region.getRegionHeight();j++){
                BGWall[i][j].setRegion(Region);

            }
        }*/
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
        dialogStyle = new Label.LabelStyle(ButtonFontAlt, Color.WHITE);

        final Label passwordLabel = new Label("Password: ", skin);
        final Label userLabel = new Label("Username: ", skin);
        //  final Label someSpace = new Label("         ", skin);
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
        //    skin.getFont("default-font").setScale(2);
        //     userLabel.setFontScale(1.9f);
//        userLabel.setFontScaleY(1f);
        //     passwordLabel.setFontScale(1.9f);
        //     passwordLabel.setFontScaleY(1f);
        //     someSpace.setFontScale(2f);
        //  someSpace.setFontScaleY(2f);
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
        // skin.getFont("default-font").setScale(1.9f/1f);
        /*
        table.add(userLabel).padBottom(10).padRight(25);
        table.add(txtUsername).width(300).height(50).padBottom(10);
        table.row();
        table.add(passwordLabel).padBottom(10).padRight(25);
        table.add(txtPassword).width(300).height(50).padBottom(10);
        table.row();
        table.add(button).width(200).height(50).padBottom(10).padRight(25);
        table.add(btnRegister).width(200).height(50).padBottom(10).padRight(25);
        table.row();
        table.add(btnOffline).width(200).height(50);
        table.center().top().pad(300);
        table.setFillParent(true);
        txtUsername.setHeight(500);
        */

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
            public void changed(ChangeEvent event, Actor actor) {//this is login button. Parse.User is an object from Almonds library.
                // screenControl.setnScreen(2);
                try {
                    ParseUser.logIn(txtUsername.getText(), txtPassword.getText(), new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                ParseUser u = ParseUser.getCurrentUser();
                                if (u.getUsername() != null) {

                                    exitDialog.text(" Welcome, " + u.getUsername() + "! ", dialogStyle).padTop(20);//Opens up a dialog box saying you successfully logged in. When you press OK, it will redirect you to the lobby
                                    exitDialog.show(stage);
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        btnRegister.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) { // Register bt
                if (txtUsername.getText() != "" && txtPassword.getText() !="") {
                    ParseUser user = new ParseUser();
                    user.setUsername(txtUsername.getText());
                    user.setPassword(txtPassword.getText());
                    user.setEmail(txtUsername.getText() + "@example.com");
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now.
                                exitDialog.text("Thank you for registering, " + txtUsername.getText() + "!", dialogStyle).padTop(20);
                                exitDialog.show(stage);
                            } else {
                                System.out.println(e.getMessage());
                                exitDialog.text(e.getMessage() + ". Please choose another Username", dialogStyle).padTop(20);
                                exitDialog.show(stage);
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                            }
                        }
                    });
                }
            }
        });
        btnOffline.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //   stage.dispose();
                screenControl.setnScreen(7,3);
            }
        });
        btnH2p.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //   stage.dispose();

                screenControl.setnScreen(7,6);
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        batch.begin();
        // batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
            setSize(nScreenWidth/3f, nScreenHeight/2f);
            setScale(1f,1.5f);
            button("OK", this,btnWhiteStyle).pad(nScreenHeight/30f,nScreenWidth/10f,nScreenHeight/20f,nScreenWidth/8f);
         //   button
          //  TextButton button = new TextButton("OK", skin, "btnWhiteStyle");

        }
        @Override
            protected void result(Object object){

            System.out.println(object);
            screenControl.setnScreen(7,2);//was 2,1
            }
    }

}
