package fungeons.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by raresradut1 on 2015-11-23.
 */
public class MapSelection implements Screen { //currently is placeholder has same image as instructions
    Table table, table2;
    TextButton.TextButtonStyle btnWhiteStyle;
    TextButton btnBack, btnGame;
    Label.LabelStyle mapLabelStyle;
    ImageButton btnBunsTown, btnDawgg,
            btnFrogosaurus, btnFunCity,
            btnFunLeafClover, btnFunMountain;

    TextureAtlas Atlas;
    TextureAtlas.AtlasRegion Region;
    TextureRegion BGWall;
    Drawable dbtnWhite;


    BitmapFont ButtonFont, ButtonFontAlt;
    int nScreenWidth, nScreenHeight;
    Stage stage;

    ScreenControl screenControl;

    SpriteBatch batch;

    public void create(){

    }

    public void render(){

    }


    @Override
    public void show() {
        nScreenWidth=Gdx.graphics.getWidth();
        nScreenHeight=Gdx.graphics.getHeight();
        batch = new SpriteBatch();
        stage=new Stage();

        ButtonFontAlt = new BitmapFont(Gdx.files.internal("FungeonsFontAlt.fnt"));

        final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        Atlas=new TextureAtlas(Gdx.files.internal("Fungeons_3.pack"));

        Drawable dBGWall;
        Region=Atlas.findRegion("BG Wall Brick Wide");
        BGWall= Region;
        dBGWall= new TextureRegionDrawable(BGWall);

        // Button Stuff
        Region=Atlas.findRegion("Button 1");
        Drawable dbtnWhiteUp = new TextureRegionDrawable(Region);
        Region=Atlas.findRegion("Button 2");
        Drawable dbtnWhiteDn = new TextureRegionDrawable(Region);
        Region=Atlas.findRegion("BunsTown");
        Drawable BunsTown = new TextureRegionDrawable(Region);
        Region=Atlas.findRegion("Dawgg");
        Drawable Dawgg = new TextureRegionDrawable(Region);
        Region=Atlas.findRegion("Frogosaurus");
        Drawable Frogosaurus = new TextureRegionDrawable(Region);
        Region=Atlas.findRegion("FunCity");
        Drawable FunCity = new TextureRegionDrawable(Region);
        Region=Atlas.findRegion("FunLeafClover");
        Drawable FunLeafClover = new TextureRegionDrawable(Region);
        Region=Atlas.findRegion("FunMountain");
        Drawable FunMountain = new TextureRegionDrawable(Region);
        //
        ButtonFont = new BitmapFont(Gdx.files.internal("FungeonsFont.fnt"));
        ButtonFont.setScale(nScreenWidth/512);
        ButtonFontAlt.setScale(nScreenWidth/612);
        btnWhiteStyle= new TextButton.TextButtonStyle(dbtnWhiteUp,dbtnWhiteDn,dbtnWhiteUp, ButtonFont);
        mapLabelStyle = new Label.LabelStyle(ButtonFontAlt, Color.WHITE);


        btnBack=new TextButton("BACK",btnWhiteStyle);
        btnGame=new TextButton("PLAY",btnWhiteStyle);

        btnBunsTown = new ImageButton(BunsTown,dbtnWhiteDn);
        btnDawgg = new ImageButton(Dawgg,dbtnWhiteDn);
        btnFrogosaurus = new ImageButton(Frogosaurus,dbtnWhiteDn);
        btnFunCity = new ImageButton(FunCity,dbtnWhiteDn);
        btnFunLeafClover = new ImageButton(FunLeafClover,dbtnWhiteDn);
        btnFunMountain = new ImageButton(FunMountain,dbtnWhiteDn);

        table=new Table(skin);
        table.setSize(nScreenWidth, nScreenHeight);
        table.setBackground(dBGWall);
        //table.pad(nScreenHeight / 16);


        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //   stage.dispose();
                screenControl.setnScreen(7, 1);
            }
        }); btnGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {screenControl.setnScreen(7, 3);}
        }); btnBunsTown.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {screenControl.setnScreen(7, 3);}
        });

        double nbtnW = 3.6;
        double nbtnH = 3.6;

        Label bunsTown = new Label("Buns Town", mapLabelStyle);
        Label dawgg = new Label("Dawgg", mapLabelStyle);
        Label frogosaurus = new Label("Frogosaurus", mapLabelStyle);
        Label funCity = new Label("FUN CITY", mapLabelStyle);
        Label funLeafClover = new Label("Fun Leaf Clover", mapLabelStyle);
        Label funMountain = new Label("Fun Mountain", mapLabelStyle);

        table.padBottom(nScreenHeight / 6);
        table.row();
        table.add(bunsTown).pad(10f);//.width((int) (nScreenWidth / (3 * nbtnW))).height((int) (nScreenHeight / (3 * nbtnH))).pad(10f);
        table.add(dawgg).pad(10f);//.width((int) (nScreenWidth / (3 * nbtnW))).height((int) (nScreenHeight / (3 * nbtnH))).pad(10f);
        table.add(frogosaurus).pad(10f);//.width((int) (nScreenWidth / (3 * nbtnW))).height((int) (nScreenHeight / (3 * nbtnH))).pad(10f);
        table.row();
        table.add(btnBunsTown).width((int) (nScreenWidth / nbtnW)).height((int) (nScreenHeight / nbtnH)).pad(10f);
        table.add(btnDawgg).width((int) (nScreenWidth / nbtnW)).height((int) (nScreenHeight / nbtnH)).pad(10f);
        table.add(btnFrogosaurus).width((int) (nScreenWidth / nbtnW)).height((int) (nScreenHeight / nbtnH)).pad(10f);
        table.row();
        table.add(funCity).pad(10f);
        table.add(funLeafClover).pad(10f);
        table.add(funMountain).pad(10f);
        table.row().padBottom(nScreenHeight / 10);
        table.add(btnFunCity).width((int) (nScreenWidth / nbtnW)).height((int) (nScreenHeight / nbtnH)).pad(10f);
        table.add(btnFunLeafClover).width((int) (nScreenWidth / nbtnW)).height((int) (nScreenHeight / nbtnH)).pad(10f);
        table.add(btnFunMountain).width((int) (nScreenWidth / nbtnW)).height((int) (nScreenHeight / nbtnH)).pad(10f);
        table.row().padBottom(nScreenHeight / 3);

        //table.add(btnGame);

        table2=new Table(skin);
        table2.setSize(nScreenWidth, nScreenHeight / 4);
        //table2.padTop(nScreenHeight / 6);
        table2.add(btnGame).width((int) (nScreenWidth / nbtnW)).height((int) (nScreenHeight / (2 * nbtnH))).pad(20f);
        table2.add(btnBack).width((int) (nScreenWidth / nbtnW)).height((int) (nScreenHeight / (2 * nbtnH))).pad(20f);

        //stage.addActor(btnGame);
        stage.addActor(table);
        stage.addActor(table2);
        //stage.addActor(btnBack);

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
