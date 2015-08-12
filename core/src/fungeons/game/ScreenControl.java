package fungeons.game;

import com.badlogic.gdx.Game;

/**
 * Created by Gaurab on 2015-04-14.
 */

public class ScreenControl extends Game {// basically holds an int
    int nScreen;
    boolean Owner;
    String userId = "";
    String chatId ="";
    String Name = "";
    @Override
    public void create() {
        nScreen = 1;
    }
    public void setnScreen(int nScreen_){
        nScreen = nScreen_;
        System.out.println("welp");
    }

    public void setName (String sName, boolean bOwner){
        Name = sName;
        Owner = bOwner;
    }
    public void setChatId(String _chatId){
        chatId = _chatId;
    }
    public void setUserId(String _userId){
        userId = _userId;
    }
    public String getName(){
        return Name;
    }
    public boolean getOwner(){
        return Owner;
    }
    public String getChatId(){
        return chatId;
    }
    public String getUserId(){
        return userId;
    }
}
