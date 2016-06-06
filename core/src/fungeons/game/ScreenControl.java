package fungeons.game;

/**
 * Created by Gaurab on 2015-04-14.
 */

public class ScreenControl {// basically holds an int
    int nScreen=0, nOldScreen=0;
    boolean Owner;
    String userId = "";
    String chatId ="";
    String authToken ="";
    String Name = "";
    String sMap = "BunsTown.tmx";
    int nMap = 0;
    boolean online = false;
//    @Override
    public void create() {
       // nScreen = 0;
    }
    public void setnScreen(int nScreen_, int nOldScreen_){
        nScreen = nScreen_;
        nOldScreen=nOldScreen_;
    }

    public void setName (String sName, boolean bOwner){
        Name = sName;
        Owner = bOwner;
    }
    public void setChatId(String _chatId){
        chatId = _chatId;
    }
    public void setOnline(boolean _online){
        online = _online;
    }
    public void setAuthToken(String _authToken){
        authToken = _authToken;
    }
    public void setUserId(String _userId){
        userId = _userId;
    }
    public void setnMap(int _nMap){
        nMap = _nMap;
    }
    public void setMap(String _sMap){
        sMap = _sMap;
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
    public String getMap(){
        return sMap;
    }
    public String getUserId(){
        return userId;
    }
    public String getAuthToken(){
        return authToken;
    }
    public int getnScreen(){
        return(nScreen);
    }
    public int getnMap(){return nMap;}
    public boolean isOnline (){return online;}
}
