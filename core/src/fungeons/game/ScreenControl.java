package fungeons.game;

import com.badlogic.gdx.Game;

/**
 * Created by Gaurab on 2015-04-14.
 */
public class ScreenControl extends Game {// basically holds an int
    int nScreen;
    @Override
    public void create() {
        nScreen = 1;
    }
    public void setnScreen(int nScreen_){
        nScreen = nScreen_;
    }
}
