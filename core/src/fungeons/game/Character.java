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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

/**
 * Created by Ben on 2015-04-28.
 */
public class Character extends Sprite {
    int  nDeltaY, nOldX=128, nOldY=128, nDir=2, Columns=6, Rows=6, nImgHeight, nImgWidth, nUp1Dn2;
    float fCharX=20, fCharY=5, ArrowTime, CharRotation,Time, GroundTime=0;
    //Dir 1 is left, Dir 2 is right
    int nCharVX, nCharVY;
    Animation WalkR,WalkL,StandR,StandL,JumpR,JumpL, DeathR, DeathL, DeathHat,LegsL, LegsR, LegsJL, LegsJR, CurAnim, OldAnim;
    Sprite sChar, sLegs;
    TextureAtlas.AtlasRegion CharSheet;
    TextureAtlas Atlas;
    Boolean bCanJump=true, bArrowShot, bDead=false;
    TextureRegion[][] ArrowArms, ArrowLegs, ArrowLegsStill;
    Vector2 ArrowMove;

    BodyDef CharDef;
    Body CharBody, CharBody2;

    FixtureDef CharFixDef, CharFixDef2;
    CircleShape CharCirc;
    PolygonShape CharBox;
    WeldJointDef jointDef;
    Joint joint;
    Play play;


    public void create(float CharX, float CharY){

        play = new Play();
        Atlas= new TextureAtlas(Gdx.files.internal("Fungeons_3.pack"));
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
        ArrowArms= CharSheet.split(CharSheet.getRegionWidth()/2, CharSheet.getRegionHeight()/2);

        CharSheet=Atlas.findRegion("Arrow Legs Still ALT");
        ArrowLegsStill=CharSheet.split(CharSheet.getRegionWidth(),CharSheet.getRegionHeight());
        sLegs=new Sprite(ArrowLegsStill[0][0]);

        CharSheet=Atlas.findRegion("Arrow Legs ALT");
        ArrowLegs=CharSheet.split(CharSheet.getRegionWidth() / 6, CharSheet.getRegionHeight() / 4);
        LegsR=new Animation(0.075f, ArrowLegs[0]);
        LegsL=new Animation(0.075f, ArrowLegs[1]);
        LegsJR=new Animation(0.075f, ArrowLegs[2]);
        LegsJL=new Animation(0.075f, ArrowLegs[3]);



        CharSheet=Atlas.findRegion("Char Death Animation ALT");
        TextureRegion[][] Death=CharSheet.split(CharSheet.getRegionWidth()/10,CharSheet.getRegionHeight()/4);
        TextureRegion[] DeathHatRgn = new TextureRegion[20];
        int index=0;
        for(int i=2;i<4;i++){
            for(int j=0;j<10;j++){
                DeathHatRgn[index++]=Death[i][j];
            }
        }

        DeathR=new Animation(0.21f, Death[0]);
        DeathL = new Animation(0.21f, Death[1]);
        DeathHat = new Animation(0.1f, DeathHatRgn);
        OldAnim=DeathHat;

        CharDef=new BodyDef();
        CharCirc= new CircleShape();
        CharFixDef=new FixtureDef();
        jointDef= new WeldJointDef();
        CharBox = new PolygonShape();
        CharFixDef2 = new FixtureDef();

        CharBox.setAsBox(1f,1.5f);
        CharCirc.setRadius(1f);

        CharDef.position.set(CharX, CharY-1);
        CharFixDef.shape=CharCirc;
        CharDef.type= BodyDef.BodyType.DynamicBody;
        CharFixDef2.shape=CharBox;

        CharBody=play.world.createBody(CharDef);

        CharFixDef.density=1f;
        CharFixDef.restitution=0f;
        CharFixDef.friction=0;
        CharFixDef2.density=1f;
        CharFixDef2.restitution=0f;
        CharFixDef2.friction=0;


        CharBody2=play.world.createBody(CharDef);
        CharBody2.createFixture(CharFixDef2);
        CharDef.position.set(CharX,CharY);

       // CharBody2=CharBody;
        nDeltaY=0;




    }
    public void setVars(int VX, int VY, float X, float Y, int Dir, Boolean CanJump, Boolean dead){
        Time += Gdx.graphics.getDeltaTime();


        bCanJump=CanJump;
        bDead=dead;
        fCharX=X;
        fCharY=Y;
        nCharVX=VX;
        nCharVY=VY;

        // all of this determines which animation the character performs
        if(nDir!=0) {
            nDir=Dir;
        }
       /* if(nCharVY!=0){
            bCanJump=false;
        }*/

        if(nDir==1){//left
            if(nCharVY==0  && bCanJump==true){
                if(nCharVX!=0){
                    CurAnim=WalkL;
                }
                else if(nCharVX==0){
                    CurAnim=StandL;
                }
            }
            else if(nCharVY!=0 || bCanJump==false){
                CurAnim=JumpL;
            }
        }
        if(nDir==2){//right
            if(nCharVY==0  && bCanJump==true ){
                if(nCharVX!=0){
                    CurAnim=WalkR;
                }
                else if(nCharVX==0){
                    CurAnim=StandR;
                }
            }
            else if(nCharVY!=0 || bCanJump==false){
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
        if(CurAnim==DeathR || CurAnim==DeathL) {
            if(CurAnim.isAnimationFinished(Time)){//assess whether the char is dead and if the time exceeds the time it takes to reach the end of the animation
                CurAnim = DeathHat;
                Time = 0;
                nDir = 0;
            }
        }




//Arrow Animation Stuff

    }
    public Sprite getCharSprite(float time, float arrowTime, Vector2 arrowMove, Boolean arrowShot){
        //determines the sprite rendered (either arrow animations, or the current frame of the regular animations)

        ArrowTime=arrowTime;
        ArrowMove=arrowMove;
        bArrowShot=arrowShot;

            if (ArrowMove.x != 0) {
                CharRotation = (float) (Math.atan(ArrowMove.y / ArrowMove.x)) * MathUtils.radiansToDegrees;
            }
            if (bArrowShot == false) {
                if(nDir==1){
                    sChar = new Sprite(ArrowArms[0][0]);
                }
                if(nDir==2){
                    sChar = new Sprite(ArrowArms[1][0]);
                }
                sChar.setRotation(CharRotation);
            }
            else if (bArrowShot == true && ArrowTime < 0.8f) {
                if(nDir==1) {
                    sChar = new Sprite(ArrowArms[0][1]);
                }
                if(nDir==2){
                    sChar = new Sprite(ArrowArms[1][1]);
                }
                sChar.setRotation(CharRotation);
            }


        else{
            sChar =new Sprite(CurAnim.getKeyFrame(Time, true));
        }
        if(bDead==true){
            sChar =new Sprite(CurAnim.getKeyFrame(Time, true));
        }

        sChar.setSize(4,4);
        sChar.setOriginCenter();
        sChar.setPosition(fCharX-2,fCharY-1);
    //    sChar.setColor((float)Math.sin(Time)/2+0.5f,(float)Math.cos(2*Time)/2+0.5f, (float)Math.sin(3*Time)/2+0.5f, 1f);
        if(CurAnim.equals(DeathHat) && DeathHat.isAnimationFinished(Time)==true){
            sChar.setColor(0,0,0,0);//makes it invisible.  as if it dissapeared
        }

        return(sChar);
    }
    public Boolean getJump(float VY, Button Jump){

        nDeltaY=(int)fCharY-nOldY;
//VY<0.1 && VY>-0.1
        if(VY<0.1 && VY>-0.1){
            GroundTime+=Gdx.graphics.getDeltaTime();
        }
        if(VY>0.1 || VY<-0.1){
            GroundTime=0;
        }

        if(VY>0.5 || nCharVY>0){
            nUp1Dn2=1;
        }
        if(VY<-0.1){
            nUp1Dn2=2;
        }
        if(VY<0.1 && VY>-0.1){
            if(nUp1Dn2==2) {
                bCanJump=true;
            }
            if(bCanJump==true){
                nOldY=(int)fCharY;
            }
        }
        else if(nCharVY!=0 && Jump.isPressed()==false ){
            bCanJump=false;
        }
        System.out.println(nDeltaY+"   "+nOldY+"    "+fCharY);
        if(nDeltaY>=8){
            bCanJump=false;
        }
        if(GroundTime==0 && Jump.isPressed()==true){
            if(VY<1 && VY>-1){
                bCanJump=false;
            }
        }
        if(GroundTime>=0.1){
            bCanJump=true;

        }


        return(bCanJump);
    }
    public Sprite getSprite2(){
     //   if(sChar==(Sprite)ArrowArms[0][0] || sChar==(Sprite)ArrowArms[0][1]){

            if(nCharVX>0){
                CurAnim=LegsR;//we can do this based entirely off of horizontal velocity because we only call when the person is using the bow
                if(bCanJump==false || nCharVY!=0){
                    CurAnim=LegsJR;
                }
            }
            if(nCharVX<0){
                CurAnim=LegsL;
                if(bCanJump==false || nCharVY!=0){
                    CurAnim=LegsJL;
                }
            }
        if(bCanJump==false) {
            if (nDir == 1) {
                CurAnim=LegsJL;
            }
            if(nDir==2){
                CurAnim=LegsJR;
            }
        }
            sLegs=new Sprite(CurAnim.getKeyFrame(Time,true));
        if(nCharVX==0 && bCanJump==true){
            sLegs=new Sprite (ArrowLegsStill[0][0]);
        }


        sLegs.setSize(4,4);
        sLegs.setOriginCenter();
        sLegs.setPosition(fCharX-2,fCharY-1);
            return(sLegs);
       // }
        //else{
        //    return(null);
       // }

    }
    public void dispose(){
        Atlas.dispose();
    }
    public void LoopCheck(int TelX, int TelY, float CharX, float CharY, Play play){
        if((CharX-TelX)<0.15 && (CharX-TelX)>-0.15){
            if((CharY-TelY)<22 && (CharY-TelY)>-4){
                play.Loop();
            }
        }
    }
}
