package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
public class MainMenu extends Game {
    Skin skin;
    Stage stage;
    SpriteBatch batch;
    TextureAtlas Atlas;
    TextureAtlas.AtlasRegion Region;
    TextureRegion BGWall;
    Drawable dbtnWhite;

   // BitmapFont mockFont= new BitmapFont(Gdx.files.internal("mockFont.fnt"));
    //Texture img =  new Texture("bgimg2.jpeg");

    ScreenControl screenControl;
    Game game;
    @Override
    public void render(){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        batch.begin();
       // batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.draw();

        System.out.println(Region.getRegionHeight());
        batch.end();

    }
    @Override
    public void resize(int width, int height){
    }
    @Override
    public void create(){
        int nScreenHeight=Gdx.graphics.getHeight(), nScreenWidth=Gdx.graphics.getWidth();
        Parse.initialize("hNMiiD81kjVZGl9Jns0KcsMN4BhkcHh0QX1PlqTp", "FUOZOmhj3BT8dhScg6nNG9zxMt9CYN8hC7HysRNM");//initialize parse with our keys
        batch = new SpriteBatch();

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        Drawable dBGWall;
        Atlas= new TextureAtlas(Gdx.files.internal("Fungeons_2.pack"));
        Region=Atlas.findRegion("BG Wall Brick");
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

        BitmapFont ButtonFont = new BitmapFont(Gdx.files.internal("FungeonsFont.fnt"));
        ButtonFont.setScale(nScreenWidth/512);//will implement when Texture pack is fixed
        TextButton.TextButtonStyle btnWhiteStyle = new TextButton.TextButtonStyle(dbtnWhite,dbtnWhite,dbtnWhite,ButtonFont);

        skin.add("btnWhiteStyle",btnWhiteStyle);
        skin.getFont("default-font").setScale(nScreenWidth/674f);//for text buttons :D
        stage = new Stage(new ScreenViewport());
        Table table = new Table();
        Gdx.input.setInputProcessor(stage);
        final ExitDialog exitDialog = new ExitDialog(" Success ", skin);

        final Label passwordLabel = new Label("Password: ", skin);
        final Label userLabel = new Label("Username: ", skin);
      //  final Label someSpace = new Label("         ", skin);
        final TextField txtPassword = new TextField("", skin);
        final TextButton button = new TextButton("LOGIN", skin, "btnWhiteStyle");

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

        table.add(btnOffline).width(nScreenWidth / 3).height(nScreenHeight / 8).expand().bottom().right().colspan(4);
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
                                if (u.getUsername()!=null) {
                                    exitDialog.text(" Welcome, " + u.getUsername() + "! ");//Opens up a dialog box saying you successfully logged in. When you press OK, it will redirect you to the lobby
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
                ParseUser user = new ParseUser();
                user.setUsername(txtUsername.getText());
                user.setPassword(txtPassword.getText());
                user.setEmail(txtUsername.getText()+"@example.com");
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Hooray! Let them use the app now.
                            exitDialog.text("Thank you for registering, " + txtUsername.getText() + "!");
                            exitDialog.show(stage);
                        } else {
                            System.out.println(e.getMessage());
                            exitDialog.text(e.getMessage()+ ". Please choose another Username");
                            exitDialog.show(stage);
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                        }
                    }
                });
            }
        });
        btnOffline.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.dispose();
                screenControl.setnScreen(3);
            }
        });

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
    public  void dispose(){
        stage.dispose();
        batch.dispose();
    }
    public  class ExitDialog extends Dialog {
        public ExitDialog(String title, Skin skin,String windowStyle){
            super(title, skin, windowStyle);

        }
        public ExitDialog(String title, Skin skin){
            super(title, skin);

        }

        public ExitDialog(String title,WindowStyle windowStyle){
            super(title, windowStyle);
        }
        {

            setScale(2.5f, 2.5f);
            button("OK",this.getTitle());

        }
        @Override
            protected void result(Object object){

            System.out.println(object);
            screenControl.setnScreen(2);
            }
    }

}
