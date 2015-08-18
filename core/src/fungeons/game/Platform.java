package fungeons.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Ben on 2015-05-09.
 */
public class Platform {
    float fPlatX, fPlatY, fArrowVX, Time,PPM;
    Sprite sPlat;
    TextureAtlas.AtlasRegion Region;
    TextureRegion[][] PlatRgns;
    TextureRegion[] PlatRgn;
    Animation PlatAnim;

    BodyDef PlatDef;
    Body PlatBody;
    PolygonShape PlatBox;
    World world;
    public void create(){
        Time=0;
       // int index=0;
        PPM=1f/16f;



    }
    public Body makePlat(double VX, float X, float Y, World _world){
        Time=0;
        world=_world;
        fArrowVX=(float)VX;
        PlatDef= new BodyDef();
        PlatBox= new PolygonShape();

        PlatBox.setAsBox(4, 0.5f);
        if(fArrowVX>0){
            PlatDef.position.set(X-2,Y-0.25f);
        }
        if(fArrowVX<0){
            PlatDef.position.set(X+4,Y-0.25f);
        }

        PlatBody=world.createBody(PlatDef);
        PlatBody.createFixture(PlatBox, 1f);
        return(PlatBody);

    }

    public float getPlatX(){
        fPlatX=PlatBody.getPosition().x;
        return(fPlatX);
    }
    public float getPlatY(){
        fPlatY=PlatBody.getPosition().y;
        return(fPlatY);
    }
    public Vector2 getPosition(){
        return(PlatBody.getPosition());
    }
    public Sprite getSprite(TextureAtlas Atlas){
        if(PlatAnim==null) {
            Region = Atlas.findRegion("platform animation");
            PlatRgns = Region.split(Region.getRegionWidth(), Region.getRegionHeight() / 6);
            PlatRgn = new TextureRegion[6];
            int index = 0;
            for (int i = 0; i < 6; i++) {
                PlatRgn[index++] = PlatRgns[i][0];
            }
            PlatAnim = new Animation(0.1f, PlatRgn);

        }
     //   if(PlatAnim.isAnimationFinished(Time)==false){
            Time+= Gdx.graphics.getDeltaTime();
       // }

        sPlat=new Sprite (PlatAnim.getKeyFrame(Time,false));
        sPlat.setSize(8,1);
        sPlat.setPosition(PlatBody.getPosition().x-sPlat.getWidth()/2,PlatBody.getPosition().y-sPlat.getHeight()/2);
        return(sPlat);
    }
}
