package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public  class main extends Game {
    Music MenuSong, GameSong, CurrentSong;
    Game game;
    MainMenu mainMenu;
    GameRooms gameRooms;
    GameRoom gameRoom;
    Play play;
    SplashScreen splashScreen;
    ScoresDisplay scoresDisplay;
    Instructions instructions;
    int nScreen, nOldScreen=0;
    ScreenControl screenControl;
    LoadingScreen loadingScreen;
    MapSelection mapSelection;
    float Time;
    @Override
    public void create() {

        MenuSong= Gdx.audio.newMusic(Gdx.files.internal("Menu Music.mp2"));
        GameSong=Gdx.audio.newMusic(Gdx.files.internal("In Game Music.mp2"));
      //  MenuSong.play();
       // MenuSong.setLooping(true);
        screenControl = new ScreenControl();



       // screenControl.create();
        mainMenu=new MainMenu();
        mainMenu.setScreenControl(screenControl);

        gameRooms=new GameRooms();
        gameRooms.setScreenControl(screenControl);
        gameRoom = new GameRoom();
        gameRoom.setScreenControl(screenControl);
        play = new Play();
        play.setScreenControl(screenControl);

        scoresDisplay= new ScoresDisplay();
        scoresDisplay.setScreenControl(screenControl);
        instructions = new Instructions();
        instructions.setScreenControl(screenControl);

        splashScreen = new SplashScreen();
        splashScreen.setScreenControl(screenControl);
        loadingScreen = new LoadingScreen();
        loadingScreen.setScreenControl(screenControl);
        mapSelection = new MapSelection();
        mapSelection.setScreenControl(screenControl);

        Time=0;
    }
    @Override
    public void render(){
        nScreen = screenControl.nScreen;

        if(nScreen==0 && getScreen()!=splashScreen){

            this.setScreen(splashScreen);
        }
        if (nScreen == 1 && getScreen()!=mainMenu) {
            Gdx.input.setInputProcessor(mainMenu.stage);
            this.setScreen(mainMenu);
        }
        else if (nScreen == 2  && getScreen()!=gameRooms) {

            this.setScreen(gameRooms);

        }
        else if (nScreen == 3 && getScreen()!=play){
            Gdx.input.setInputProcessor(play.stage);
            this.setScreen(play);

            if(MenuSong.isPlaying()==true){
                MenuSong.stop();
                GameSong.play();
                GameSong.setLooping(true);
            }
        }
        if(nScreen == 4  && getScreen()!=gameRoom){
            // gameRoom.create();
            this.setScreen(gameRoom);
        }
        if (nScreen == 5  && getScreen()!=scoresDisplay){
            this.setScreen(scoresDisplay);
        }
        if(nScreen==6 && getScreen()!=instructions){
            Gdx.input.setInputProcessor(instructions.stage);
            this.setScreen(instructions);
        }
        if(nScreen==7 && getScreen()!=loadingScreen){
            loadingScreen.setNewScreen(screenControl.nOldScreen);
            this.setScreen(loadingScreen);
        }
        if(nScreen==8 && getScreen()!= mapSelection){
            this.setScreen(mapSelection);
        }
        if(nScreen!=5){
            GameSong.stop();
            MenuSong.play();
            MenuSong.setLooping(true);
        }
        super.render();
    }
}
/*
public  class main extends Game{
    Music MenuSong, GameSong, CurrentSong;
    Game game;
    MainMenu mainMenu;
    GameRooms gameRooms;
    GameRoom gameRoom;
    Play play;
    SplashScreen splashScreen;
    ScoresDisplay scoresDisplay;
    Instructions instructions;
    int nScreen, nOldScreen=0;
    ScreenControl screenControl;
    @Override
    public void create() {
        mainMenu=new MainM
    }
    @Override
    public void render(){

    }
}
/*
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
    SplashScreen splashScreen;
    ScoresDisplay scoresDisplay;
    Instructions instructions;
    int nScreen, nOldScreen=0;
    ScreenControl screenControl;
    @Override
    public void create () {
        splashScreen=new SplashScreen();
        setScreen(splashScreen);
        nScreen=1;
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
        instructions= new Instructions();
        instructions.create();
        instructions.setScreenControl(screenControl);
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
        if(nScreen==6){
            Gdx.input.setInputProcessor(instructions.stage);
            instructions.render();
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


}*/