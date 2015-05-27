package fungeons.game;

/**
 * Created by Ben on 2015-05-09.
 */
public class Arrow {
    float ArrowX, ArrowY;
    double ArrowVX, ArrowVY;


    public void setVars(double VX, double VY, float X, float Y){
        ArrowVX=VX;
        ArrowVY=VY;
        ArrowX=X;
        ArrowY=Y;
    }
    public float getArrowX(){
        return(ArrowX);
    }
    public float getArrowY(){
        return(ArrowY);
    }
    public double getArrowVX(){
        return(ArrowVX);
    }
    public double getArrowVY(){
        return(ArrowVY);
    }
}
