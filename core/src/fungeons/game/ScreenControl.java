package fungeons.game;

import com.badlogic.gdx.Game;

/**
 * Created by Gaurab on 2015-04-14.
 */
public class ScreenControl extends Game {// basically holds an int
    int nScreen;
    boolean Owner;
    String Name = "";
    @Override
    public void create() {
        nScreen = 1;
    }
    public void setnScreen(int nScreen_){
        nScreen = nScreen_;
    }

    public void setName (String sName, boolean bOwner){
        Name = sName;
        Owner = bOwner;
    }
    public String getName(){
        return Name;
    }
    public boolean getOwner(){
        return Owner;
    }
}
