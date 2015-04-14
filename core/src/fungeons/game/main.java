package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import org.json.JSONArray;

import pablo127.almonds.LogInCallback;
import pablo127.almonds.Parse;
import pablo127.almonds.ParseException;
import pablo127.almonds.ParseObject;
import pablo127.almonds.ParseQuery;
import pablo127.almonds.ParseUser;
import pablo127.almonds.SignUpCallback;


public class main extends Game {
    Skin skin;
    Stage stage;
    Music LoginSong;
    SpriteBatch batch;
    Texture img;
    Game game;
    @Override
    public void create () {
      setScreen(new MainMenu(game));
    }

    public void render(float delta){
        super.render();

    }
    @Override
    public void resize(int width, int height){

    }
    @Override
    public void dispose(){

    }

}