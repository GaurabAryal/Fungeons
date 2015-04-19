package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class main extends Game {
    Skin skin;
    Stage stage;
    Music LoginSong;
    SpriteBatch batch;
    Texture img;
    Game game;
    MainMenu mainMenu;
    GameRooms gameRooms;
    int nScreen = 1;
    ScreenControl screenControl;
    @Override
    public void create () {

        screenControl = new ScreenControl();
        mainMenu = new MainMenu();
        mainMenu.create();
        mainMenu.setScreenControl(screenControl);
        gameRooms = new GameRooms();
        gameRooms.create();
        gameRooms.setScreenControl(screenControl);
        screenControl.create();

    }

    public void render(){
        nScreen = screenControl.nScreen;
        if (nScreen == 1) {
            mainMenu.render();

        } else if (nScreen == 2) {
            gameRooms.render();
        }

    }
    @Override
    public void resize(int width, int height){

    }
    @Override
    public void dispose(){

    }

}