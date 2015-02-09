package fungeons.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class main extends ApplicationAdapter {
    Object[] listEntries = {"This is a list entry1", "And another one1", "The meaning of life1", "Is hard to come by1",
            "This is a list entry2", "And another one2", "The meaning of life2", "Is hard to come by2", "This is a list entry3",
            "And another one3", "The meaning of life3", "Is hard to come by3", "This is a list entry4", "And another one4",
            "The meaning of life4", "Is hard to come by4", "This is a list entry5", "And another one5", "The meaning of life5",
            "Is hard to come by5"};

    Skin skin;
    Stage stage;
    Label fpsLabel;

    @Override
    public void create () {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        // stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, new PolygonSpriteBatch());
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
        // Group.debug = true;


        TextField textfield = new TextField("", skin);
        textfield.setMessageText("ex.John101");
        textfield.setAlignment(Align.left);
        CheckBox idRem = new CheckBox("Remember ID", skin);
        idRem.setChecked(true);
        CheckBox pwRem = new CheckBox("Remember ID", skin);
        fpsLabel = new Label("fps:", skin);
        // configures an example of a TextField in password mode.
        final Label passwordLabel = new Label("Password: ", skin);
        final Label userLabel = new Label("Username: ", skin);
        final Label someSpace = new Label("         ", skin);
        userLabel.setFontScaleX(1.5f);
        userLabel.setFontScaleY(1.5f);
        passwordLabel.setFontScaleX(1.5f);
        passwordLabel.setFontScaleY(1.5f);
        someSpace.setFontScaleX(1.5f);
        someSpace.setFontScaleY(1.5f);
        final TextField passwordTextField = new TextField("", skin);
        passwordTextField.setMessageText("*****");
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setPasswordMode(true);

        // window.debug();
        Window window = new Window("Login", skin);

        window.setPosition((width/5), height);
        window.row();
        window.add(userLabel).colspan(1);
        window.add(textfield).minWidth(width/2).minHeight(height/8).expandX().fillX().colspan(1);
        window.row();
        window.add(passwordLabel).colspan(1);
        window.add(passwordTextField).minWidth(width/2).minHeight(height/8).expandX().fillX().colspan(1);
        window.row();

        window.row();
        window.pack();
        window.setMovable(false);
        window.setWidth((float)width);
        // stage.addActor(new Button("Behind Window", skin));
        stage.addActor(window);
        textfield.setTextFieldListener(new TextFieldListener() {
            public void keyTyped (TextField textField, char key) {
                if (key == '\n') textField.getOnscreenKeyboard().show(false);
            }
        });
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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