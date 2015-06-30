package fungeons.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Ben on 2015-06-23.
 */
public class DeathThing {
    TextureAtlas Atlas;
    TextureAtlas.AtlasRegion Region;
    TextureRegion[][] DThingRgn;
    Animation DThingAnim;
    Sprite DThingSprite;
    Boolean bDead=false;
    float Time, VX=9f/16f,VY=0,X=4,Y=14, PPM=1f/16f;
    public void create(){
        Atlas = new TextureAtlas("Fungeons_2.pack");
        Region=Atlas.findRegion("Death thing");
        DThingRgn=Region.split(Region.getRegionWidth()/5,Region.getRegionHeight());
        DThingAnim=new Animation(0.1f,DThingRgn[0]);
        DThingSprite=new Sprite();

    }//for change direction, check the dir your going then one we hit a wall, check 90 degrees rotation, if thats a wall go the other way, if not then go that way

    public void setVars(TiledMapTileLayer Col, Vector2 CharVec){
        if(VX>0) {
            for (int i = 0; i <= 3; i++) {
                if (((int) ((X / PPM) / 64) + i) > 0 && ((int) ((X / PPM) / 64) + i) < Col.getWidth()) {
                    if (Col.getCell((int) ((X / PPM) / 64) + i, (int) ((Y / PPM) / 64))//Collide on Left
                            .getTile().getProperties().containsKey("Hit")) {
                        VX = 0;
                        for(int j=0;j<=3;j++){
                            if (Col.getCell((int) ((X / PPM) / 64) , (int) ((Y / PPM) / 64)+j)//Collide on Left
                                    .getTile().getProperties().containsKey("Hit")) {
                                VY = -6*PPM;
                                break;
                            } else {
                                VY = 6*PPM;
                            }
                        }
                    }
                }
            }
        }
        if(VX<0) {
            for (int i = 0; i >= -3; i--) {
                if (((int) ((X / PPM) / 64) + i) > 0 && ((int) ((X / PPM) / 64) + i) < Col.getWidth()) {
                    if (Col.getCell((int) ((X / PPM) / 64) + i, (int) ((Y / PPM) / 64))//Collide on Left
                            .getTile().getProperties().containsKey("Hit")) {
                        VX = 0;
                        for(int j=0;j<=3;j++){
                            if (Col.getCell((int) ((X / PPM) / 64) , (int) ((Y / PPM) / 64)+j)//Collide on Left
                                    .getTile().getProperties().containsKey("Hit")) {
                                VY = -6*PPM;
                                break;
                            } else {
                                VY = 6*PPM;
                            }
                        }
                    }
                }
            }
        }
        if(VY>0) {
            for (int i = 0; i <= 3; i++) {
                if (((int) ((Y / PPM) / 64) + i) > 0 && ((int) ((Y / PPM) / 64) + i) < Col.getHeight()) {
                    if (Col.getCell((int) ((X / PPM) / 64), (int) ((Y / PPM) / 64) + i)//Collide on Left
                            .getTile().getProperties().containsKey("Hit")) {
                        VY = 0;
                        for (int j = 0; j <= 3; j++) {
                            if (Col.getCell((int) ((X / PPM) / 64)+j, (int) ((Y / PPM) / 64))//Collide on Left
                                    .getTile().getProperties().containsKey("Hit")) {
                                VX = -9 * PPM;
                                break;
                            } else {
                                VX = 9 * PPM;
                            }
                        }
                    }
                }
            }
        }
        if(VY<0) {
            for (int i = 0; i >= -3; i--) {
                if (((int) ((Y / PPM) / 64) + i) > 0 && ((int) ((Y / PPM) / 64) + i) < Col.getHeight()) {
                    if (Col.getCell((int) ((X / PPM) / 64), (int) ((Y / PPM) / 64) + i)//Collide on Left
                            .getTile().getProperties().containsKey("Hit")) {
                        VY = 0;
                        for (int j = 0; j <= 3; j++) {
                            if (Col.getCell((int) ((X / PPM) / 64)+j, (int) ((Y / PPM) / 64))//Collide on Left
                                    .getTile().getProperties().containsKey("Hit")) {
                                VX = -9 * PPM;
                                break;
                            } else {
                                VX = 9 * PPM;
                            }
                        }
                    }
                }
            }
        }
        X+=VX;
        Y+=VY;
        if(CharVec.dst(X,Y)>45){
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
        if(CharVec.dst(X,Y)<=13.45){
            bDead=true;

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
    public Boolean getDead(){
        return(bDead);
    }
    public float getX(){
        return(X);
    }
    public float getY(){
        return(Y);
    }
}

