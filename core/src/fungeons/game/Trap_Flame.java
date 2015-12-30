package fungeons.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Ben on 2015-12-28.
 */
public class Trap_Flame {

    float FlameX, PPM=1f/16f, TileWidth, TileHeight,Time=0 ,time, fCharX,fCharY;//save xy of tiledmap tile, then set FlameX and y to that times ppm n stuff
    int nChance=20, nRandS, nCharVX, FlameY;//nRandSpawn and nRandUpDown
    Vector2 vFlame=new Vector2(0,0), vTraps=new Vector2(0,0), vChar;
    Sprite sFlame;
    TextureAtlas Atlas = new TextureAtlas(Gdx.files.internal("Fungeons_3.pack"));
    Animation FAnim;
    Array<Vector2> arTrapVec=new Array<Vector2>();
    Array<String>arTrapStr= new Array<String>();
    TextureRegion[][] tempFires;
    TextureRegion[] fires;
    Boolean bLethal=false;
    DeathThing deathThing;
    Play play;

    public void create(){
        int Rows= 2, Cols = 10;
        TextureAtlas.AtlasRegion Region;
        Region=Atlas.findRegion("fire trap");
        TextureRegion textFire = Region;
        tempFires=Region.split(Region.getRegionWidth()/Cols,Region.getRegionHeight()/Rows);
        //int FlameH=Region.getRegionHeight()/Cols, FlameW=Region.getRegionWidth()/Rows;
        fires=new TextureRegion[Cols * Rows];
        int index=0;
        for(int i =0;i<Rows;i++) {
            for (int j = 0; j < Cols; j++) {
                fires[index++] = tempFires[i][j];//sets the different animations for walking jumping and standing still for the char
            }
        }
        FAnim = new Animation(0.075f, fires);
    }
    public void setVars(int CharVX, float CharX, float CharY, TiledMapTileLayer Col, Array<Vector2> trapVec, Array<String> trapStr) {
        arTrapVec = trapVec;
        arTrapStr = trapStr;
        fCharX=CharX;
        fCharY=CharY;
        if (CharVX != 0) {
            nCharVX = CharVX;//only updates when char is moving thus storing it's previous velocity if the player stops
        }
        for(int i=0;i<arTrapVec.size;i++){
            if(arTrapVec.get(i).dst(CharX,CharY)>=200){
                arTrapVec.removeIndex(i);
                arTrapStr.removeIndex(i);
                play.updateTraps(arTrapVec, arTrapStr);
            }
        }

        FlameX = 0;
        FlameY = 2;
        TileHeight = Col.getTileHeight();
        TileWidth = Col.getTileWidth();
        try {
            for (int i = 0; i <= 5; i++) {
                if (nCharVX >= 0) {
                    if (Col.getCell((int) ((CharX / PPM) / TileWidth) + 8, (int) ((CharY / PPM) / TileHeight)-i)
                            .getTile().getProperties().containsKey("Hit")) {
                        FlameX = CharX + (8 * PPM * TileHeight);
                        FlameY = 4 + (int)(((int)CharY / 4) * 4) - (4 * i);
                        break;
                    }
                }
                if (nCharVX < 0) {
                    if (Col.getCell((int) ((CharX / PPM) / TileWidth) - 8, (int) ((CharY / PPM) / TileHeight)-i)
                            .getTile().getProperties().containsKey("Hit")) {
                        FlameX = CharX - (8 * PPM * TileHeight);
                        FlameY = 4 + (int) (((int)CharY / 4) * 4) - (4 * i);//FIGURE THIS SHIT OUT
                        break;
                    }
                }
            }


/*
            FlameY+=7;//FIND NEW WAY TO CENTER THE SAWS ON THE WALL BLOCKS!!!!
            while(FlameY%4!=0 && Col.getCell((int) ((FlameX / PPM) / TileWidth), (int) ((FlameY / PPM) / TileHeight))
                    .getTile().getProperties().containsKey("Hit")==false){
                FlameY--;
            }
            FlameY-=6;*/
            vFlame.set(FlameX, FlameY);
            for(int i=-6;i<=6;i++){
                if(Col.getCell((int) (FlameX /PPM/TileWidth +i), (int) (((CharY) / PPM) / TileHeight))
                        .getTile().getProperties().containsKey("Hit")){
                    break;
                }

                if(i==6 && Col.getCell((int) (FlameX / PPM / TileWidth), (int) ((FlameY / PPM) / TileHeight)-1)
                        .getTile().getProperties().containsKey("Hit")){
                    //we subtract 1 because the trap sits on the block, hence the one below must have the "Hit" property
                    makeTrap();
                }
            }

        }
        catch (NullPointerException e) {}

    }
    public void makeTrap(){
        nRandS= MathUtils.random(nChance);

        if(nRandS==nChance-1){
            if(arTrapVec.size>0) {
                for (int i = 0; i < arTrapVec.size; i++) {//FIND A WAY TO BETTER UPDATE THE VECTOR TRAPS ARRAY?!?!?!?!?
                    vTraps.set(arTrapVec.get(i));

                    if (vFlame.dst(vTraps) <= 21) {
                        break;
                    }
                    if (i == arTrapVec.size - 1) {
                        arTrapVec.add(new Vector2(vFlame));
                        arTrapStr.add("flame");
                        play.updateTraps(arTrapVec, arTrapStr);

                    }
                }

            }

            else{
                arTrapVec.add(new Vector2(vFlame));
                arTrapStr.add("flame");
                play.updateTraps(arTrapVec, arTrapStr);
            }

        }
    }
    public String getTrapType(){
        return("Flame");
    }
    public Sprite getSprite(){
        Time+= Gdx.graphics.getDeltaTime();

    /*    if(Time%3>3){
            time=0;
            Time=0;
        }*/
        if(Time%1.5<0.75) {
            sFlame = new Sprite(tempFires[0][0]);
            bLethal=false;
        }
        if(Time%1.5>=0.75){
            sFlame=new Sprite(tempFires[0][1]);
            bLethal=false;
        }
        if(Time%3>=1.5) {
            sFlame = new Sprite(FAnim.getKeyFrame(Time, true));
            bLethal=true;
        }
   /*     if(bLethal==true && fCharX<(FlameX+sFlame.getWidth()) && fCharX>FlameX
                && fCharY>FlameY && fCharY<(FlameY+sFlame.getHeight())){
            deathThing.setbDead(true);
        }*/
        sFlame.setSize(sFlame.getWidth()*PPM*2f ,sFlame.getHeight()*PPM*2f);
     //   sFlame.setOrigin(sFlame.getWidth()/2,sFlame.getHeight()/2);

        return(sFlame);
    }
    public void updateDeath(DeathThing death){
        deathThing= death;
    }


    public void dispose(){
        Atlas.dispose();
    }
    public void setPlay(Play p){
        play= p;
    }
    public Boolean getbLethal(){
        return(bLethal);
    }
}
