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
    float fCharX=100, fCharY=100, ArrowTime, CharRotation,
            Time;
    //Dir 1 is left, Dir 2 is right
    int nCharVX, nCharVY;
    Animation WalkR,WalkL,StandR,StandL,JumpR,JumpL, CurAnim;
    Sprite sChar;
    TextureAtlas.AtlasRegion CharSheet;
    TextureAtlas Atlas;
    Boolean bCanJump=true, bArrowShot;
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

        WalkR=new Animation(0.075f,Character[0]);
        WalkL=new Animation(0.075f,Character[1]);
        StandR=new Animation(0.2f,Character[2]);
        StandL=new Animation(0.2f,Character[3]);
        JumpR=new Animation(0.075f,Character[4]);
        JumpL=new Animation(0.075f,Character[5]);
        CurAnim=StandR;

        CharSheet=Atlas.findRegion("Arrow arms ALT");
        ArrowArms= CharSheet.split(CharSheet.getRegionWidth()/2, CharSheet.getRegionHeight());
       // Texture Shit=new Texture(ArrowArms[0][0]);
        //sArrowShoot=new Sprite(ArrowArms[0][1]);


        CharDef=new BodyDef();
        CharBox= new CircleShape();
        CharFixDef=new FixtureDef();
        jointDef= new WeldJointDef();

        //CharBox.setAsBox(1f,2f);
        CharBox.setRadius(1f);

        CharDef.position.set(15, 15);
        CharFixDef.shape=CharBox;
        CharDef.type= BodyDef.BodyType.DynamicBody;

        CharBody=play.world.createBody(CharDef);

        CharFixDef.density=1f;
        CharFixDef.restitution=0f;
        CharFixDef.friction=0;

        CharBody.createFixture(CharFixDef);
        CharDef.position.set(15,17);
       // CharBody2=CharBody;
        CharBody2=play.world.createBody(CharDef);
        jointDef.bodyA=CharBody;
        jointDef.bodyB=CharBody2;
        jointDef.localAnchorA.set(0,2f);
        joint=play.world.createJoint(jointDef);



    }
    public void setVars(int VX, int VY, float X, float Y, int Dir, Boolean CanJump){
        Time += Gdx.graphics.getDeltaTime();
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

//Arrow Animation Stuff

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
    public Sprite getCharSprite(float time, float arrowTime, Vector2 arrowMove, Boolean arrowShot){
        ArrowTime=arrowTime;
        Time=time;
        ArrowMove=arrowMove;
        bArrowShot=arrowShot;

        if(ArrowMove.x!=0){
            CharRotation=(float)(Math.atan(ArrowMove.y/ArrowMove.x))* MathUtils.radiansToDegrees;
        }
        System.out.println(CharRotation);
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
        sChar.setOrigin(sChar.getWidth()/2,sChar.getHeight()/2);
        sChar.setPosition(fCharX-2,fCharY-1);


        return(sChar);
        /*if( nDir==2 && (bArrowShot==false||(bArrowShot==true && ArrowTime<1))){
            sChar.flip(true,false);
        }*/
    }
    /*public Joint getSpliff(){
        CharBody2=play.world.createBody(CharDef);
        jointDef.bodyA=CharBody;
        jointDef.bodyB=CharBody2;
        jointDef.localAnchorA.set(0,2f);
        joint=play.world.createJoint(jointDef);
        return(joint);
    }*/
}
