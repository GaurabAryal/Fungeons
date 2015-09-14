package fungeons.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Ben on 2015-09-05.
 */
public class SplashScreen implements Screen{
    Texture splashImg;
    Sprite sSplash;
    SpriteBatch batch;
    public AssetManager assetManager;
    OrthographicCamera camera;
    float Time;
    ScreenControl screenControl;
    main main1;
    MainMenu mainMenu;
    public SplashScreen(){
      //  main1=main_;

    }

    public void render() {

    }

    public void create() {

    }

    @Override
    public void show() {
        Time=0;
        mainMenu = new MainMenu();
        splashImg=new Texture(Gdx.files.internal("SplashScreen 12.png"));
        sSplash=new Sprite(splashImg);
        batch= new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.viewportHeight=Gdx.graphics.getHeight();
        camera.viewportWidth=Gdx.graphics.getWidth();
        camera.position.set(0,0,0);
        sSplash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sSplash.setPosition(-sSplash.getWidth()/2,-sSplash.getHeight()/2);
        batch.setProjectionMatrix(camera.combined);
        //System.out.println("FUKIN HELLO!??! 3333333");
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Time+=Gdx.graphics.getDeltaTime();
       // System.out.println("splash render    "+Time);
        batch.begin();
        sSplash.draw(batch);
        sSplash.setAlpha((float)(-0.5*Math.cos(Time)+0.5));
      //  System.out.println("switch  "+screenControl.getnScreen()+"      "+screenControl.nScreen );
        camera.update();
        batch.end();

        if(Time>=6.5){
            screenControl.setnScreen(1,0);
         //   System.out.println("switch  "+screenControl.getnScreen()+"      "+screenControl.nScreen );
         //   main1.setScreen(mainMenu);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
        sSplash.setAlpha(0);
    }

    @Override
    public void dispose() {
        batch.dispose();
        splashImg.dispose();
    }
    public void setScreenControl(ScreenControl screenControl_){
        screenControl = screenControl_;
    }
}
