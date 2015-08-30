package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class main extends Game {
    Skin skin;
    Stage stage;
    Music MenuSong, GameSong, CurrentSong;
    SpriteBatch batch;
    Texture img;
    Game game;
    MainMenu mainMenu;
    GameRooms gameRooms;
    GameRoom gameRoom;
    Play play;
    ScoresDisplay scoresDisplay;
    int nScreen = 1, nOldScreen=0;
    ScreenControl screenControl;
    @Override
    public void create () {
        MenuSong= Gdx.audio.newMusic(Gdx.files.internal("Menu Music.mp2"));
        GameSong=Gdx.audio.newMusic(Gdx.files.internal("In Game Music.mp2"));
        MenuSong.play();
        MenuSong.setLooping(true);
        screenControl = new ScreenControl();
        mainMenu = new MainMenu();
        mainMenu.create();
        mainMenu.setScreenControl(screenControl);
        gameRooms = new GameRooms();
        gameRooms.create();
        gameRooms.setScreenControl(screenControl);
        gameRoom = new GameRoom();
        //gameRoom.create();
        gameRoom.setScreenControl(screenControl);
        scoresDisplay = new ScoresDisplay();
        scoresDisplay.setScreenControl(screenControl);
        play = new Play();
        play.create();
        play.setScreenControl(screenControl);
        screenControl.create();

    }

    public void render(){
        nScreen = screenControl.nScreen;
        if (nScreen == 1) {
            Gdx.input.setInputProcessor(mainMenu.stage);
            mainMenu.render();
        } else if (nScreen == 2) {
            gameRooms.render();
        } else if (nScreen == 3){
            Gdx.input.setInputProcessor(play.stage);
            play.render();
            if(MenuSong.isPlaying()==true){
                MenuSong.stop();
                GameSong.play();
                GameSong.setLooping(true);
            }
        }
        if(nScreen == 4){
           // gameRoom.create();
          gameRoom.render();
        }
        if (nScreen == 5){
            scoresDisplay.render();
        }
        if(nScreen!=5 && nScreen!=3 && GameSong.isPlaying()==true){
            GameSong.stop();
            MenuSong.play();
            MenuSong.setLooping(true);
        }
    }
    @Override
    public void resize(int width, int height){

    }
    @Override
    public void dispose(){

    }
    @Override
    public void pause(){
        if (screenControl.nScreen==4){
            gameRoom.pause();
        }
    }

}