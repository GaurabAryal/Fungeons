package fungeons.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class Play extends Game {
    OrthographicCamera camera;
    Vector2 gravity=new Vector2(0,-9.8f), CurMove=new Vector2(0,0);
    BodyDef MapDef, CharDef;
    Body MapBody, CharBody;
    FixtureDef MapFixDef, CharFixDef;
    PolygonShape MapBox, CharBox;
    ScreenControl screenControl;
    World world;
    int nScreenHeight, nScreenWidth,CharX =64,CharY=64, nTileHeight, nTileWidth;
    double VX = 0, VY = 0;
    Box2DDebugRenderer b2Renderer;

    Array<Body> arWorldBodies = new Array<Body>();

    TiledMap Map;
    TiledMapTileLayer MapCol, MapAlt;
    TmxMapLoader MapLoader;
    OrthogonalTiledMapRenderer MapRenderer;

    SpriteBatch batch;
    Touchpad touchpadMove;
    Touchpad.TouchpadStyle touchpadMoveStyle;
    Skin touchpadMoveSkin;
    Drawable touchMoveKnob, touchMoveBackground;
    Stage stage;
    Boolean bCanJump=false, bLight=true;

    @Override
    public void create() {
        MapLoader=new TmxMapLoader();
        Map=MapLoader.load("BunsTown.tmx");
        MapCol= (TiledMapTileLayer) Map.getLayers().get(0);
        MapAlt= (TiledMapTileLayer) Map.getLayers().get(1);
        MapAlt.setVisible(false);
        MapRenderer = new OrthogonalTiledMapRenderer(Map);

        nScreenWidth= Gdx.graphics.getWidth();
        nScreenHeight=Gdx.graphics.getHeight();
        camera=new OrthographicCamera();
        camera.viewportHeight=nScreenHeight*2;
        camera.viewportWidth=nScreenWidth*2;
        camera.position.set(500,500,0);

        world = new World(gravity, false);
        b2Renderer=new Box2DDebugRenderer();

        MapDef= new BodyDef();
        MapFixDef= new FixtureDef();
        MapBox= new PolygonShape();

        nTileHeight=(int)MapCol.getTileHeight();
        nTileWidth=(int)MapCol.getTileWidth();

        batch= new SpriteBatch();
        System.out.println(MapCol.getWidth());
        for(int i =0; i< MapCol.getWidth(); i++){
            for(int j=0; j<MapCol.getHeight(); j++){
                if(MapCol.getCell(i,j).getTile().getProperties().containsKey("Hit")) {
                    MapDef= new BodyDef();
                    MapFixDef= new FixtureDef();
                    MapBox= new PolygonShape();
                    MapDef.position.set(i*nTileWidth+nTileWidth/2, j*nTileHeight+ nTileHeight/2);//sets the box to proper coordinates to correlate to the tiled map
                    MapBody = world.createBody(MapDef);
                    MapBox.setAsBox(nTileWidth/2, nTileHeight/2);
                    MapBody.createFixture(MapBox, 0f);
                    arWorldBodies.add(MapBody);
                }
            }

        }


    }
    @Override
    public void render(){
        super.render();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();



        batch.setProjectionMatrix(camera.combined);
        MapRenderer.setView(camera);
        MapRenderer.render();
        batch.begin();

        batch.end();
        b2Renderer.render(world, camera.combined);
        world.step(1/60f, 6, 2);
    }
    @Override
    public void dispose(){
        world.dispose();
        batch.dispose();
        MapRenderer.dispose();
        b2Renderer.dispose();
        dispose();
    }
    public void setScreenControl(ScreenControl screenControl_){
        screenControl = screenControl_;
    }
}

