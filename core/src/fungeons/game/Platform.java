package fungeons.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Ben on 2015-05-09.
 */
public class Platform {
    float fPlatX, fPlatY, fArrowVX;

    BodyDef PlatDef;
    Body PlatBody;
    PolygonShape PlatBox;
    World world;
    public Body makePlat(double VX, float X, float Y, World _world){
        world=_world;
        fArrowVX=(float)VX;
        PlatDef= new BodyDef();
        PlatBox= new PolygonShape();

        PlatBox.setAsBox(4, 0.5f);
        if(fArrowVX>0){
            PlatDef.position.set(X-2,Y);
        }
        if(fArrowVX<0){
            PlatDef.position.set(X+2,Y);
        }

        PlatBody=world.createBody(PlatDef);
        PlatBody.createFixture(PlatBox, 1f);
        System.out.println(PlatBody.getPosition()+"     1");
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
        System.out.println(PlatBody.getPosition()+"     2");
        return(PlatBody.getPosition());
    }
}
