package fungeons.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Ben on 2015-06-23.
 */
public class DeathThing {
    TextureAtlas Atlas;
    TextureAtlas.AtlasRegion Region;
    TextureRegion[][] DThingRgns;
    TextureRegion[] DThingRgn;
    Animation DThingAnim;
    Sprite DThingSprite;
    Boolean bDead=false;
    float Time, VX=1f/16f,VY=0,X=4,Y=14, PPM=1f/16f;
    Array<Vector2> arTrapVec=new Array<Vector2>();
    Array<String> arTrapStr=new Array<String>();
    Vector2 CharVec = new Vector2(0,0);
    Boolean bLethal;
    public void create(){
        VX=1.5f*PPM;
        VY=0;
        Atlas = new TextureAtlas("Fungeons_3.pack");
        Region=Atlas.findRegion("Death thing");
        DThingRgns=Region.split(Region.getRegionWidth()/5,Region.getRegionHeight());

        DThingAnim=new Animation(0.1f,DThingRgns[0]);
        DThingSprite=new Sprite();

    }//for change direction, check the dir your going then one we hit a wall, check 90 degrees rotation, if thats a wall go the other way, if not then go that way

    public void setVars(TiledMapTileLayer Col, Vector2 CharVec_, int TelX, int TelY, int Tel2X, int Tel2Y){
        CharVec.set(CharVec_);
        if(VX>0) {
            for (int i = 0; i <= 3; i++) {
                    if (Col.getCell((int) ((X / PPM) / 64) + i, (int) ((Y / PPM) / 64))//Collide on Left
                            .getTile().getProperties().containsKey("Hit")) {
                        VX = 0;
                        for(int j=0;j<=3;j++){
                            if (Col.getCell((int) ((X / PPM) / 64) , (int) ((Y / PPM) / 64)+j)//Collide on Left
                                    .getTile().getProperties().containsKey("Hit")) {
                                VY = -PPM;
                                break;
                            } else {
                                VY = PPM;
                            }
                        }
                    }
                }
            }

        if(VX<0) {
            for (int i = 0; i >= -3; i--) {
                    if (Col.getCell((int) ((X / PPM) / 64) + i, (int) ((Y / PPM) / 64))//Collide on Left
                            .getTile().getProperties().containsKey("Hit")) {
                        VX = 0;
                        for(int j=0;j<=3;j++){
                            if (Col.getCell((int) ((X / PPM) / 64) , (int) ((Y / PPM) / 64)+j)//Collide on Left
                                    .getTile().getProperties().containsKey("Hit")) {
                                VY = -PPM;
                                break;
                            } else {
                                VY = PPM;
                            }
                        }
                    }
                }
            }

        if(VY>0) {
            for (int i = 0; i <= 3; i++) {
                    if (Col.getCell((int) ((X / PPM) / 64), (int) ((Y / PPM) / 64) + i)//Collide on Left
                            .getTile().getProperties().containsKey("Hit")) {
                        VY = 0;
                        for (int j = 0; j <= 3; j++) {
                            if (Col.getCell((int) ((X / PPM) / 64)+j, (int) ((Y / PPM) / 64))//Collide on Left
                                    .getTile().getProperties().containsKey("Hit")) {
                                VX = -1.5f*PPM;
                                break;
                            } else {
                                VX =1.5f*PPM;
                            }
                        }
                    }
                }
            }

        if(VY<0) {
            for (int i = 0; i >= -3; i--) {
                    if (Col.getCell((int) ((X / PPM) / 64), (int) ((Y / PPM) / 64) + i)//Collide on Left
                            .getTile().getProperties().containsKey("Hit")) {
                        VY = 0;
                        for (int j = 0; j <= 3; j++) {
                            if (Col.getCell((int) ((X / PPM) / 64)+j, (int) ((Y / PPM) / 64))//Collide on Left
                                    .getTile().getProperties().containsKey("Hit")) {
                                VX = -1.5f*PPM;
                                break;
                            } else {
                                VX = 1.5f*PPM;
                            }
                        }
                    }
                }
            }

        X+=VX;
        Y+=VY;
        if((X-TelX)<1 && (X-TelX)>-1){
            if((Y-TelY)<20 && (Y-TelY)>-2){
                X=Tel2X-20;
                Y=Tel2Y+11;
            }
        }
        if(CharVec.dst(X,Y)>60){
            if(VX>0){
                X+=1;
            }
            if(VX<0){
                X-=1;
            }
            if(VY>0){
                Y+=1;
            }
            if(VY<0){
                Y-=1;
            }
        }

    }
    public Sprite getSprite(float time){
        Time=time;
        DThingSprite=new Sprite(DThingAnim.getKeyFrame(Time,true));

        DThingSprite.setSize(DThingSprite.getWidth()*PPM,DThingSprite.getHeight()*PPM);
        DThingSprite.setOriginCenter();
        DThingSprite.setPosition(X-DThingSprite.getWidth()/2,Y-DThingSprite.getHeight()/2);
        return(DThingSprite);
    }

    public Boolean getDead(float CharX, float CharY, Array<Vector2> arTrapV, Array<String> arTrapS, Trap_Flame flame){
        CharVec.set(CharX,CharY);
        arTrapVec=arTrapV;
        arTrapStr=arTrapS;
        bLethal = flame.getbLethal();
        for(int i=0;i<arTrapVec.size;i++){
            Vector2 trap = new Vector2(arTrapVec.get(i));
            if(arTrapStr.get(i).equals("saw")) {
                if (trap.dst(CharVec) <= 8.5f) {
                    bDead = true;
                }
            }
            if(arTrapStr.get(i).equals("flame")) {
                if (bLethal == true && CharX < (trap.x + flame.sFlame.getWidth()) && CharX > trap.x
                        && CharY > trap.y && CharY < (trap.y + flame.sFlame.getHeight())) {
                    bDead=true;
                }
            }
        }
        if(CharVec.dst(X,Y)<=14.5){
            bDead=true;
        }
        return(bDead);
    }
    public void dispose(){
        Atlas.dispose();
        arTrapVec.clear();
    }
    public void setbDead(Boolean dead){ bDead=dead; }
}


