package fungeons.game;

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
    Vector2 vSaw=new Vector2(0,0), vTraps=new Vector2(0,0);
    Sprite sSaw;
    Array<Vector2> arTraps=new Array<Vector2>();

    public void setVars(int CharVX, float CharX, float CharY, TiledMapTileLayer Col, Array<Vector2> Traps) {
        arTraps = Traps;
        if (CharVX != 0) {
            nCharVX = CharVX;//only updates when char is moving thus storing it's previous velocity if the player stops
        }
        SawX = 95;
        SawY = 2;
        TileHeight = Col.getTileHeight();
        TileWidth = Col.getTileWidth();
        nRandUD = (int)MathUtils.random(1);
        try {
        for (int i = 0; i <= 5; i++) {//this is unique to buzzsaw because it can go on ceilings

                if (nRandUD == 0) {
                    if (nCharVX > 0) {
                        if (Col.getCell((int) ((CharX / PPM) / TileWidth) + 8, (int) ((CharY / PPM) / TileHeight) + i)
                                .getTile().getProperties().containsKey("Hit")) {
                            SawX = CharX + (8 * PPM * TileHeight);
                            SawY = 2+(int)(CharY/4)*4+(4*i);
                            System.out.println(i+"      2    "+CharY +"       "+SawY);
                            break;
                        }
                    }
                    if (nCharVX < 0) {
                        if (Col.getCell((int) ((CharX / PPM) / TileWidth) - 8, (int) ((CharY / PPM) / TileHeight) + i)
                                .getTile().getProperties().containsKey("Hit")) {
                            SawX = CharX - (8 * PPM * TileHeight);
                            SawY = 2+(int)(CharY/4)*4+(4*i);
                            System.out.println(i+"      2    "+CharY +"       "+SawY);
                            break;
                        }
                    }

                }
                if (nRandUD == 1) {
                    if (nCharVX > 0) {
                        if (Col.getCell((int) ((CharX / PPM) / TileWidth) + 8, (int) ((CharY / PPM) / TileHeight) - i)
                                .getTile().getProperties().containsKey("Hit")) {
                            SawX = CharX + (8 * PPM * TileHeight);
                            SawY = 2+(int)(CharY/4)*4-(4*i);
                            System.out.println(i+"      2    "+CharY +"       "+SawY);
                            break;
                        }
                    }
                    if (nCharVX < 0) {
                        if (Col.getCell((int) ((CharX / PPM) / TileWidth) - 8, (int) ((CharY / PPM) / TileHeight) - i)
                                .getTile().getProperties().containsKey("Hit")) {
                            SawX = CharX - (8 * PPM * TileHeight);
                            SawY = 2+(int)(CharY/4)*4-(4*i);
                            System.out.println(i+"      2    "+CharY +"       "+SawY);
                            break;
                        }
                    }
                }

            System.out.println(i+"      1    "+CharY +"       "+SawY);
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
        catch (NullPointerException e) {System.out.println("MATE?!?!?!");}

    }
    public void makeTrap(){
         nRandS= MathUtils.random(nChance);

        if(nRandS==nChance-1){
            if(arTraps.size>0) {
                for (int i = 0; i < arTraps.size; i++) {
                    vTraps.set(arTraps.get(i));
                    if (vSaw.dst(vTraps) <= 25) {
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

}
