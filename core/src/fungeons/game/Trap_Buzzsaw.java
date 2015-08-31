package fungeons.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Ben on 2015-07-09.
 */
public class Trap_Buzzsaw {
    float SawX, PPM=1f/16f, TileWidth, TileHeight, fCharX,fCharY;//save xy of tiledmap tile, then set sawx and y to that times ppm n stuff
    int nChance=20, nRandS, nRandUD, nCharVX, SawY;//nRandSpawn and nRandUpDown
    long id;
    Vector2 vSaw=new Vector2(0,0), vTraps=new Vector2(0,0), vChar;
    Sprite sSaw;
    Array<Vector2> arTraps=new Array<Vector2>();
    Sound BuzzSawSound;
    Boolean isPlaying=false;

    public void setVars(int CharVX, float CharX, float CharY, TiledMapTileLayer Col, Array<Vector2> Traps) {
        arTraps = Traps;
        if(BuzzSawSound==null) {
            BuzzSawSound = Gdx.audio.newSound(Gdx.files.internal("BuzzSaw Sound.mp2"));

           /* BuzzSawSound.setPriority(1,1);
            BuzzSawSound.setLooping(1,true);
            BuzzSawSound.play();
            BuzzSawSound.pause();
            BuzzSawSound.resume(1);*/
        }
        if (CharVX != 0) {
            nCharVX = CharVX;//only updates when char is moving thus storing it's previous velocity if the player stops
        }
        for(int i=0;i<arTraps.size;i++){
            if(arTraps.get(i).dst(CharX,CharY)>=200){
                arTraps.removeIndex(i);
            }
        }

        SawX = 0;
        SawY = 2;
        TileHeight = Col.getTileHeight();
        TileWidth = Col.getTileWidth();
        nRandUD = (int)MathUtils.random(1);
        try {
        for (int i = 0; i <= 5; i++) {//this is unique to buzzsaw because it can go on ceilings

                if (nRandUD == 0) {
                    if (nCharVX >= 0) {
                        if (Col.getCell((int) ((CharX / PPM) / TileWidth) + 8, (int) ((CharY / PPM) / TileHeight) + i)
                                .getTile().getProperties().containsKey("Hit")) {
                            SawX = CharX + (8 * PPM * TileHeight);
                            SawY = 2+(int)(CharY/4)*4+(4*i);
                            break;
                        }
                    }
                    if (nCharVX < 0) {
                        if (Col.getCell((int) ((CharX / PPM) / TileWidth) - 8, (int) ((CharY / PPM) / TileHeight) + i)
                                .getTile().getProperties().containsKey("Hit")) {
                            SawX = CharX - (8 * PPM * TileHeight);
                            SawY = 2+(int)(CharY/4)*4+(4*i);
                            break;
                        }
                    }

                }
                if (nRandUD == 1) {
                    if (nCharVX >= 0) {
                        if (Col.getCell((int) ((CharX / PPM) / TileWidth) + 8, (int) ((CharY / PPM) / TileHeight) - i)
                                .getTile().getProperties().containsKey("Hit")) {
                            SawX = CharX + (8 * PPM * TileHeight);
                            SawY = 2+(int)(CharY/4)*4-(4*i);
                            break;
                        }
                    }
                    if (nCharVX < 0) {
                        if (Col.getCell((int) ((CharX / PPM) / TileWidth) - 8, (int) ((CharY / PPM) / TileHeight) - i)
                                .getTile().getProperties().containsKey("Hit")) {
                            SawX = CharX - (8 * PPM * TileHeight);
                            SawY = 2+(int)(CharY/4)*4-(4*i);
                            break;
                        }
                    }
                }

            }
/*
            SawY+=7;//FIND NEW WAY TO CENTER THE SAWS ON THE WALL BLOCKS!!!!
            while(SawY%4!=0 && Col.getCell((int) ((SawX / PPM) / TileWidth), (int) ((SawY / PPM) / TileHeight))
                    .getTile().getProperties().containsKey("Hit")==false){
                SawY--;
            }
            SawY-=6;*/
        vSaw.set(SawX, SawY);
                for(int i=-6;i<=6;i++){
                    if(Col.getCell((int) (SawX /PPM/TileWidth +i), (int) (((CharY) / PPM) / TileHeight))
                            .getTile().getProperties().containsKey("Hit")){
                        break;
                    }

                    if(i==6 && Col.getCell((int) (SawX / PPM / TileWidth), (int) ((SawY / PPM) / TileHeight))
                            .getTile().getProperties().containsKey("Hit")){
                        makeTrap();
                    }
                }

    }
        catch (NullPointerException e) {}

    }
    public void makeTrap(){
         nRandS= MathUtils.random(nChance);

        if(nRandS==nChance-1){
            if(arTraps.size>0) {
                for (int i = 0; i < arTraps.size; i++) {
                    vTraps.set(arTraps.get(i));
                    System.out.println(vTraps.x+"     "+vSaw.x);
                    if (vSaw.dst(vTraps) <= 25 && vSaw.y==vTraps.y) {
                            break;
                    }
                    if (i == arTraps.size - 1) {
                        arTraps.add(new Vector2(vSaw));

                    }
                }

            }

            else{
                arTraps.add(new Vector2(vSaw));
            }

        }
    }
    public String getTrapType(){
        return("Saw");
    }
    public Sprite getSprite(TextureAtlas Atlas){
        TextureAtlas.AtlasRegion Region;
        Region=Atlas.findRegion("BuzzSaw");
        TextureRegion[][] Saws=Region.split(Region.getRegionWidth()/2,Region.getRegionHeight());

        sSaw=new Sprite(Saws[0][0]);
        sSaw.setSize(sSaw.getWidth()*PPM*1.7f,sSaw.getHeight()*PPM*1.7f);
        sSaw.setOrigin(sSaw.getWidth()/2,sSaw.getHeight()/2);

        return(sSaw);
    }
    public Array getTrapArray(){
        return(arTraps);
    }
    public void PlaySound(float CharX, float CharY, Array<Vector2> Traps, Boolean Dead){
        vChar = new Vector2(CharX,CharY);
        float closest=120;
            for (int i = 0; i < Traps.size; i++) {
                if (vChar.dst(Traps.get(i)) < closest) {
                    closest = vChar.dst(Traps.get(i));
                }
                if (i == Traps.size - 1) {

                    BuzzSawSound.setVolume(id, 7f / (float) Math.pow(closest, 1.2));

                    if (isPlaying == false) {
                        id=BuzzSawSound.loop();
                        isPlaying = true;
                    }

                }
            }

    }
    public void dispose(){
        BuzzSawSound.stop(id);
        BuzzSawSound.dispose();
    }
}
