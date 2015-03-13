package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class main extends Game {
    Skin skin;
    Stage stage;
    Music LoginSong;
    SpriteBatch batch;
    Texture img;

    @Override
    public void create () {
        final Parse parse = new Parse();
        batch = new SpriteBatch();
        img = new Texture("bgimg2.jpeg");
        LoginSong = Gdx.audio.newMusic(Gdx.files.internal("login.mp3"));
        LoginSong.setVolume(1f);
        LoginSong.setLooping(true);
        LoginSong.play();
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        stage = new Stage(new ScreenViewport());
        Table table = new Table();
        Gdx.input.setInputProcessor(stage);
        final ExitDialog exitDialog = new ExitDialog("Error", skin);
        final Label passwordLabel = new Label("Password: ", skin);
        final Label userLabel = new Label("Username: ", skin);
        final Label someSpace = new Label("         ", skin);
        final TextField txtPassword = new TextField("", skin);
        final TextButton button = new TextButton("Login!", skin);
        final TextField txtUsername = new TextField("", skin);
        txtUsername.setMessageText("ex.John101");
        txtUsername.setAlignment(Align.left);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);

        skin.add("default", textButtonStyle);
        userLabel.setFontScaleX(1.5f);
        userLabel.setFontScaleY(1.5f);

        passwordLabel.setFontScaleX(1.5f);
        passwordLabel.setFontScaleY(1.5f);

        someSpace.setFontScaleX(1.5f);
        someSpace.setFontScaleY(1.5f);

        txtPassword.setMessageText("*****");
        txtPassword.setPasswordCharacter('*');
        txtPassword.setPasswordMode(true);

        table.add(userLabel).padBottom(10).padRight(25);
        table.add(txtUsername).width(300).padBottom(10);
        table.row();
        table.add(passwordLabel).padBottom(10).padRight(25);
        table.add(txtPassword).width(300).padBottom(10);
        table.row();
        table.add(button).padBottom(10).padRight(25);
        table.setFillParent(true);
        stage.addActor(table);

        exitDialog.show(stage);//This makes Dialog Box pop up, it automatically centers itself

        txtUsername.setTextFieldListener(new TextFieldListener() {
            public void keyTyped (TextField textField, char key) {
                if (key == '\n') textField.getOnscreenKeyboard().show(false);
            }
        });
        txtPassword.setTextFieldListener(new TextFieldListener() {
            public void keyTyped (TextField textField, char key) {
                if (key == '\n') textField.getOnscreenKeyboard().show(false);
            }
        });
        //http://stackoverflow.com/questions/21488311/libgdx-how-to-create-a-button
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                parse.addLogin(txtUsername.getText(),txtPassword.getText());
            }
        });
    }

    @Override
    public void render () {

        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose () {
        stage.dispose();
        skin.dispose();
    }
    public static class ExitDialog extends Dialog {
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
            setScale(1.5f,1.5f);
            text("An unexpected error has occurred");
            button("OK");

        }
        @Override
        protected void result(Object object){
           // System.out.println("result");
        }
    }
}