package fungeons.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Ben on 2015-08-30.
 */
public class Instructions implements Screen {
    TextureAtlas Atlas;
    Table table;
    TextButton.TextButtonStyle btnWhiteStyle;
    TextButton btnBack;
    BitmapFont ButtonFont;
    int nScreenWidth, nScreenHeight;
    Stage stage;

    ScreenControl screenControl;

    SpriteBatch batch;

    public void create(){

    }

    @Override
    public void show() {
        nScreenWidth=Gdx.graphics.getWidth();
        nScreenHeight=Gdx.graphics.getHeight();
        batch = new SpriteBatch();
        stage=new Stage();
        final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        Atlas=new TextureAtlas(Gdx.files.internal("Fungeons_3.pack"));
        TextureAtlas.AtlasRegion Region = Atlas.findRegion("Instructions Page");
        Drawable dInstructions = new TextureRegionDrawable(Region);

        Region=Atlas.findRegion("Button 1");
        Drawable dbtnWhiteUp = new TextureRegionDrawable(Region);
        Region=Atlas.findRegion("Button 2");
        Drawable dbtnWhiteDn = new TextureRegionDrawable(Region);

        ButtonFont = new BitmapFont(Gdx.files.internal("FungeonsFont.fnt"));
        ButtonFont.setScale(nScreenWidth/512);
        btnWhiteStyle= new TextButton.TextButtonStyle(dbtnWhiteUp,dbtnWhiteDn,dbtnWhiteUp, ButtonFont);
        btnBack=new TextButton("BACK",btnWhiteStyle);

        table=new Table(skin);
        table.setSize(nScreenWidth,nScreenHeight);
        table.setBackground(dInstructions);
        table.pad(nScreenHeight/16);



        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //   stage.dispose();
                screenControl.setnScreen(7,1);//was 1,6
            }
        });
        table.add(btnBack).right().top().expand();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.draw();
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
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        ButtonFont.dispose();
        Atlas.dispose();
    }
    public void setScreenControl(ScreenControl screenControl_){
        screenControl = screenControl_;
    }
}
