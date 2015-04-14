package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import pablo127.almonds.LogInCallback;
import pablo127.almonds.Parse;
import pablo127.almonds.ParseException;
import pablo127.almonds.ParseUser;

/**
 * Created by Gaurab on 2015-04-13.
 */
public class MainMenu implements Screen {
    Skin skin;
    Stage stage;
    Music LoginSong;
    SpriteBatch batch;
    Texture img =  new Texture("bgimg2.jpeg");
    Game game;
    public MainMenu(Game game){
        this.game = game;
    }
    @Override
    public void render(float delta){
        if(Gdx.input.justTouched())
            game.setScreen(new GameRooms(game));

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.draw();
        batch.end();

    }
    @Override
    public void resize(int width, int height){
    }
    @Override
    public void show(){
        Parse.initialize("", "");
        batch = new SpriteBatch();
        LoginSong = Gdx.audio.newMusic(Gdx.files.internal("login.mp3"));
        LoginSong.setVolume(1f);
        LoginSong.setLooping(true);
        LoginSong.play();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        stage = new Stage(new ScreenViewport());
        Table table = new Table();
        Gdx.input.setInputProcessor(stage);
        final ExitDialog exitDialog = new ExitDialog("Success", skin);
        final Label passwordLabel = new Label("Password: ", skin);
        final Label userLabel = new Label("Username: ", skin);
        final Label someSpace = new Label("         ", skin);
        final TextField txtPassword = new TextField("", skin);
        final TextButton button = new TextButton("Login!", skin);
        final TextField txtUsername = new TextField("", skin);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        txtUsername.setMessageText("ex.John101");
        txtUsername.setAlignment(Align.left);
        txtUsername.setHeight(100);
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.up = skin.newDrawable("white", Color.WHITE);
        textButtonStyle.down = skin.newDrawable("white", Color.WHITE);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);



        skin.add("default", textButtonStyle);
        userLabel.setFontScaleX(4f);
        userLabel.setFontScaleY(4f);
        passwordLabel.setFontScaleX(4f);
        passwordLabel.setFontScaleY(4f);
        someSpace.setFontScaleX(4f);
        someSpace.setFontScaleY(4f);
        txtPassword.setMessageText("*****");
        txtPassword.setPasswordCharacter('*');
        txtPassword.setPasswordMode(true);
        txtUsername.pack();
        txtUsername.setHeight(100);
        table.add(userLabel).padBottom(10).padRight(25);
        table.add(txtUsername).width(300).height(100).padBottom(10);
        table.row();
        table.add(passwordLabel).padBottom(10).padRight(25);
        table.add(txtPassword).width(300).height(100).padBottom(10);
        table.row();
        table.add(button).width(200).height(100).padBottom(10).padRight(25);
        table.center().top().pad(300);
        table.setFillParent(true);
        txtUsername.setHeight(500);
        stage.addActor(table);
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
                    ParseUser.logIn(txtUsername.getText(), txtPassword.getText(), new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                ParseUser u = ParseUser.getCurrentUser();
                                exitDialog.text("Welcome, " + u.getUsername() + "!");
                                exitDialog.show(stage);
                                game.setScreen(new GameRooms(game));

                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e+"E wtf yy");
                }
            }
        });

    }
    @Override
    public void hide(){


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
            button("OK");

        }
        @Override
        protected void result(Object object){

        }
    }

}
