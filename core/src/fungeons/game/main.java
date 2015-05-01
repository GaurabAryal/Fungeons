package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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
    Table table;
    List list;
    ScrollPane scrollPane;
    TextureAtlas atlas;

    @Override
    public void create () {
        //final Parse parse = new Parse();
        //Parse.initialize("","");
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);
        int nWidth =Gdx.graphics.getWidth();
        int nHeight = Gdx.graphics.getHeight();
        batch = new SpriteBatch();
        img = new Texture("bgimg2.jpeg");
        LoginSong = Gdx.audio.newMusic(Gdx.files.internal("login.mp3"));
        LoginSong.setVolume(1f);
        LoginSong.setLooping(true);
        LoginSong.play();

        list = new List(skin);
        String arsNames[] = new String[50];
        for(int i=0;i<50;i++){
            arsNames[i]="J'son"+i;
        }
        list.setItems(arsNames);

        list.getSelected();
        scrollPane = new ScrollPane(list,skin);
        scrollPane.setFillParent(true);

        scrollPane.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                final DialogB infoB = new DialogB(list.getSelected().toString(),skin);
                //infoB.SetMap("I've been Defeated");// I spent 20 minutes relearning to
                // making a class for this when it's already a prebuilt function ...
                infoB.text("Map Placeholder");
                infoB.show(stage);
                /*infoB.addCaptureListener(new EventListener() {
                    @Override
                    public boolean handle(Event event) {
                        infoB.remove();
                        return false;
                    }
                });*/
               return false;
            }
        });
        stage.addActor(scrollPane);

        //exitDialog.show(stage);//This makes Dialog Box pop up, it automatically centers itself
        //txtUsername.setTextFieldListener(new TextFieldListener() {
        /*    public void keyTyped (TextField textField, char key) {
                if (key == '\n') textField.getOnscreenKeyboard().show(false);
            }
        });
        txtPassword.setTextFieldListener(new TextFieldListener() {
            public void keyTyped (TextField textField, char key) {
                if (key == '\n') textField.getOnscreenKeyboard().show(false);
            }
        });*/
        //http://stackoverflow.com/questions/21488311/libgdx-how-to-create-a-button
        /**button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                parse.addLogin(txtUsername.getText(),txtPassword.getText());
            }
        });*/
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

    public static class DialogB extends Dialog{
        //String sMap;
        public DialogB(String title, Skin skin,String windowStyle){
            super(title, skin, windowStyle);
        }
        public DialogB(String title, Skin skin){
            super(title, skin);

        }

        public DialogB(String title,WindowStyle windowStyle, String sMap){
            super(title, windowStyle);
        }
        /*public void SetMap(String _sMap){
            _sMap = sMap;
        }*/
        {
            setScale(1.5f,1.5f);
            //text(sMap+"WTF");
            button("OK");

        }
        @Override
        protected void result(Object object){

        }
    }
}