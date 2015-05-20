package fungeons.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Created by Ben on 2015-04-28.
 */
public class Character extends Sprite {
    int  nDeltaY, nOldX=128, nOldY=128, nDir=2, Columns=6, Rows=6, nImgHeight, nImgWidth;
    float fCharX=100, fCharY=100;
    //Dir 1 is left, Dir 2 is right
    int nCharVX, nCharVY;
    Animation WalkR,WalkL,StandR,StandL,JumpR,JumpL, CurAnim;
    TextureAtlas.AtlasRegion CharSheet;
    TextureAtlas Atlas;
    Boolean bCanJump=true, bIsAiming;

    BodyDef CharDef;
    Body CharBody;
    FixtureDef CharFixDef;
    PolygonShape CharBox;
    Play play;

    public void create(){

        play = new Play();
        Atlas= new TextureAtlas(Gdx.files.internal("Fungeons_2.pack"));
        CharSheet=Atlas.findRegion("Fungeon Char 64 W");
        nImgHeight=CharSheet.getRegionHeight()/Rows;
        nImgWidth=CharSheet.getRegionWidth()/Columns;
        TextureRegion[][] Character= CharSheet.split(nImgWidth, nImgHeight);

        WalkR=new Animation(0.075f,Character[0]);
        WalkL=new Animation(0.075f,Character[1]);
        StandR=new Animation(0.2f,Character[2]);
        StandL=new Animation(0.2f,Character[3]);
        JumpR=new Animation(0.075f,Character[4]);
        JumpL=new Animation(0.075f,Character[5]);
        CurAnim=StandR;

        CharDef=new BodyDef();
        CharBox= new PolygonShape();
        CharFixDef=new FixtureDef();

        CharBox.setAsBox(1f,2f);
        CharDef.position.set(15, 15);
        CharFixDef.shape=CharBox;
        CharDef.type= BodyDef.BodyType.DynamicBody;

        CharBody=play.world.createBody(CharDef);
        CharFixDef.density=1f;
        CharFixDef.restitution=0f;
        CharFixDef.friction=0;


        CharBody.createFixture(CharFixDef);
    }
    public void setVars(int VX, int VY, float X, float Y, int Dir, Boolean CanJump){
        nDir=Dir;
        bCanJump=CanJump;
        fCharX=X;
        fCharY=Y;
        nCharVX=VX;
        nCharVY=VY;
        if(nCharVX<0){
            nDir=1;
        }
        else if(nCharVX>0){
            nDir=2;
        }
        if(nCharVY!=0){
            bCanJump=false;
        }
        if(nDir==1){//left
            if(bCanJump==true){
                if(nCharVX!=0){
                    CurAnim=WalkL;
                }
                else if(nCharVX==0){
                    CurAnim=StandL;
                }
            }
            else{
                CurAnim=JumpL;
            }
        }
        if(nDir==2){//right
            if(bCanJump==true){
                if(nCharVX!=0){
                    CurAnim=WalkR;
                }
                else if(nCharVX==0){
                    CurAnim=StandR;
                }
            }
            else{
                CurAnim=JumpR;
            }
        }
    }
    public Character(){

    }
    public float getCharX(){
        return(fCharX);
    }
    public float getCharY(){
        return(fCharY);
    }
    public boolean getCanJump(){
        return(bCanJump);
    }
    public Animation getCharAnim(){
        return(CurAnim);
    }
    public Body getCharBody(){
        return(CharBody);
    }
}
