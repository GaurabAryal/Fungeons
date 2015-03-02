package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
public class main extends Game {
    Skin skin;
    Stage stage;
    Music LoginSong;
    SpriteBatch batch;
    Texture img;

    @Override
    public void create () {
        batch = new SpriteBatch();
        img = new Texture("bgimg2.jpeg");
        LoginSong = Gdx.audio.newMusic(Gdx.files.internal("login.mp3"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());
        final Label passwordLabel = new Label("Password: ", skin);
        final Label userLabel = new Label("Username: ", skin);
        final Label someSpace = new Label("         ", skin);
        final TextField passwordTextField = new TextField("", skin);
        final TextButton button = new TextButton("Login!", skin);
        Table table = new Table();
        TextField textfield = new TextField("", skin);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        Gdx.input.setInputProcessor(stage);
        textfield.setMessageText("ex.John101");
        textfield.setAlignment(Align.left);
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
        passwordTextField.setMessageText("*****");
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setPasswordMode(true);
        table.add(userLabel).padBottom(10).padRight(25);
        table.add(textfield).width(300).padBottom(10);
        table.row();
        table.add(passwordLabel).padBottom(10).padRight(25);
        table.add(passwordTextField).width(300).padBottom(10);
        table.row();
        table.add(button).padBottom(10).padRight(25);
        table.setFillParent(true);
        stage.addActor(table);
        LoginSong.setVolume(1f);
        LoginSong.setLooping(true);
        LoginSong.play();
        textfield.setTextFieldListener(new TextFieldListener() {
            public void keyTyped (TextField textField, char key) {
                if (key == '\n') textField.getOnscreenKeyboard().show(false);
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
}