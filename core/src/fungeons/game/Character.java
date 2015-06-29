package fungeons.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

/**
 * Created by Ben on 2015-04-28.
 */
public class Character extends Sprite {
    int  nDeltaY, nOldX=128, nOldY=128, nDir=2, Columns=6, Rows=6, nImgHeight, nImgWidth;
    float fCharX=20, fCharY=5, ArrowTime, CharRotation,Time;
    //Dir 1 is left, Dir 2 is right
    int nCharVX, nCharVY;
    Animation WalkR,WalkL,StandR,StandL,JumpR,JumpL, DeathR, DeathL, DeathHat, CurAnim, OldAnim;
    Sprite sChar;
    TextureAtlas.AtlasRegion CharSheet;
    TextureAtlas Atlas;
    Boolean bCanJump=true, bArrowShot, bDead;
    TextureRegion[][] ArrowArms;
    Vector2 ArrowMove;

    BodyDef CharDef;
    Body CharBody, CharBody2;
    FixtureDef CharFixDef;
    CircleShape CharBox;
    WeldJointDef jointDef;
    Joint joint;
    Play play;


    public void create(){

        play = new Play();
        Atlas= new TextureAtlas(Gdx.files.internal("Fungeons_2.pack"));
        CharSheet=Atlas.findRegion("Fungeon Char 64 W");
        nImgHeight=CharSheet.getRegionHeight()/Rows;
        nImgWidth=CharSheet.getRegionWidth()/Columns;
        TextureRegion[][] Character= CharSheet.split(nImgWidth, nImgHeight);

        WalkR=new Animation(0.075f,Character[0]);//sets the different animations for walking jumping and standing still for the char
        WalkL=new Animation(0.075f,Character[1]);
        StandR=new Animation(0.2f,Character[2]);
        StandL=new Animation(0.2f,Character[3]);
        JumpR=new Animation(0.075f,Character[4]);
        JumpL=new Animation(0.075f,Character[5]);
        CurAnim=StandR;
        CharSheet=Atlas.findRegion("Arrow arms ALT");
        ArrowArms= CharSheet.split(CharSheet.getRegionWidth()/2, CharSheet.getRegionHeight());

        CharSheet=Atlas.findRegion("Char Death Animation ALT");
        TextureRegion[][] Death=CharSheet.split(CharSheet.getRegionWidth()/10,CharSheet.getRegionHeight()/3);
        DeathR=new Animation(0.21f, Death[0]);
        DeathL = new Animation(0.21f, Death[1]);
        DeathHat = new Animation(0.1f, Death[2]);
        OldAnim=DeathHat;

        CharDef=new BodyDef();
        CharBox= new CircleShape();
        CharFixDef=new FixtureDef();
        jointDef= new WeldJointDef();

        //CharBox.setAsBox(1f,2f);
        CharBox.setRadius(1f);

        CharDef.position.set(30, 4);
        CharFixDef.shape=CharBox;
        CharDef.type= BodyDef.BodyType.DynamicBody;

        CharBody=play.world.createBody(CharDef);

        CharFixDef.density=1f;
        CharFixDef.restitution=0f;
        CharFixDef.friction=0;

        CharBody.createFixture(CharFixDef);
        CharDef.position.set(30,6);
       // CharBody2=CharBody;




    }
    public void setVars(int VX, int VY, float X, float Y, int Dir, Boolean CanJump, Boolean dead){
        Time += Gdx.graphics.getDeltaTime();
        nDir=Dir;
        bCanJump=CanJump;
        bDead=dead;
        fCharX=X;
        fCharY=Y;
        nCharVX=VX;
        nCharVY=VY;

        // all of this determines which animation the character performs
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
        if(bDead==true){
            if(nDir==1){
                CurAnim=DeathL;
            }
            if(nDir==2){
                CurAnim=DeathR;

            }
        }
        if(CurAnim!=OldAnim){
            Time=0;
            OldAnim=CurAnim;
        }
        if(CurAnim.isAnimationFinished(Time)&&bDead==true){//assess whether the char is dead and if the time exceeds the time it takes to reach the end of the animation
            CurAnim=DeathHat;
            nDir=0;
        }
//Arrow Animation Stuff

    }
    public Sprite getCharSprite(float time, float arrowTime, Vector2 arrowMove, Boolean arrowShot){
        //determines the sprite rendered (either arrow animations, or the current frame of the regular animations)

        ArrowTime=arrowTime;
        ArrowMove=arrowMove;
        bArrowShot=arrowShot;


        if(ArrowMove.x!=0){
            CharRotation=(float)(Math.atan(ArrowMove.y/ArrowMove.x))* MathUtils.radiansToDegrees;
        }
        if(bArrowShot==false){
            sChar=new Sprite(ArrowArms[0][0]);
            sChar.setRotation(CharRotation);

        }
        else if(bArrowShot==true && ArrowTime<1){
            sChar=new Sprite(ArrowArms[0][1]);
            sChar.setRotation(CharRotation);
        }


        else{

            sChar =new Sprite(CurAnim.getKeyFrame(Time, true));
        }

        sChar.setSize(4,4);
        sChar.setOriginCenter();
        sChar.setPosition(fCharX-2,fCharY-1);

        return(sChar);
    }
}
