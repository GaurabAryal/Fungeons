package fungeons.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Ben on 2015-09-11.
 */
public class LoadingScreen implements Screen{
    Texture imgSaw = new Texture("loading image.png");
    Sprite sSaw = new Sprite(imgSaw);
    SpriteBatch batch = new SpriteBatch();
    OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    ScreenControl screenControl;
    int newScreen=0;
    Timer timer = new Timer();


    public void setNewScreen(int i){
        newScreen=i;
    }
    public void setScreenControl(ScreenControl screenControl_){
        screenControl=screenControl_;
    }

    @Override
    public void show() {
     //  sSaw.setSize(sSaw.getWidth()*(Gdx.graphics.getHeight()/2/sSaw.getHeight()), Gdx.graphics.getHeight()/2);
        sSaw.setScale(Gdx.graphics.getHeight()/2/sSaw.getHeight());
        sSaw.setCenter(sSaw.getWidth() / 2, sSaw.getHeight() / 2);
        sSaw.setOrigin(sSaw.getWidth() / 2, sSaw.getHeight() / 2);
        sSaw.setPosition(-sSaw.getWidth() / 2, -sSaw.getHeight() / 2);

    //    timer.start();
        camera.position.set(0,0,0);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sSaw.draw(batch);
        batch.end();
        camera.update();
        screenControl.setnScreen(newScreen,7);

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
        System.out.println(newScreen+"  fasdhjfhkh");

    }

    @Override
    public void dispose() {

    }
}
