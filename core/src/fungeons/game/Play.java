package fungeons.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

public class Play extends Game {
    OrthographicCamera camera;
    Vector2 gravity=new Vector2(0,-9.8f);
    World world = new World(gravity, false);

    BodyDef MapDef;
    Body MapBody, CharBody;
    FixtureDef MapFixDef;
    PolygonShape MapBox;

    Box2DDebugRenderer b2Renderer;

    TiledMap Map;
    TiledMapTileLayer MapCol, MapAlt;
    TmxMapLoader MapLoader;
    OrthogonalTiledMapRenderer MapRenderer;

    int nScreenHeight, nScreenWidth, nTileHeight, nTileWidth;
    float Time=0;
    TextureRegion Frame;

    Animation CurAnim;
    int nCharX =128,nCharY=128;
    int nCharVX, nCharVY;
    int nDir=2;

    TextureAtlas Atlas;
    TextureRegion[][] MoveBG1, MoveKnob1;

    SpriteBatch batch;
    Touchpad touchpadMove;
    Touchpad.TouchpadStyle touchpadMoveStyle;
    Skin touchpadMoveSkin;
    Drawable touchMoveKnob, touchMoveBackground;
    Stage stage;
    Boolean bCanJump=false, bLight=true;

    Character character = new Character();
    ScreenControl screenControl;

    @Override
    public void create() {
        Atlas= new TextureAtlas(Gdx.files.internal("Fungeons_2.pack"));

        character.create();

        stage=new Stage();
        TextureAtlas.AtlasRegion Region;
        int RegionHeight, RegionWidth;
        Region = Atlas.findRegion("TouchPad BackGround",-1);
        RegionHeight=Region.getRegionHeight();
        RegionWidth=Region.getRegionWidth();
        MoveBG1 = Region.split(RegionWidth,RegionHeight);
        Region = Atlas.findRegion("Touchpad Knob",-1);
        RegionHeight=Region.getRegionHeight();
        RegionWidth=Region.getRegionWidth();
        MoveKnob1 = Region.split(RegionWidth,RegionHeight);

        touchpadMoveSkin = new Skin();//making a touchpad which is kinda like an analog stick
        touchpadMoveSkin.add("MoveKnob", MoveKnob1[0][0]);
        touchpadMoveSkin.add("MoveBackground", MoveBG1[0][0]);
        touchMoveKnob = touchpadMoveSkin.getDrawable("MoveKnob");
        touchMoveBackground = touchpadMoveSkin.getDrawable("MoveBackground");
        touchpadMoveStyle = new Touchpad.TouchpadStyle();
        touchpadMoveStyle.knob = touchMoveKnob;
        touchpadMoveStyle.background = touchMoveBackground;
        touchpadMove = new Touchpad(0, touchpadMoveStyle);
        touchpadMove.setSize(200, 200);
        touchpadMove.setPosition(0, 0);

        stage.addActor(touchpadMove);

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
        camera.position.set(nCharX,nCharY,0);

        b2Renderer=new Box2DDebugRenderer();

        MapDef= new BodyDef();
        MapFixDef= new FixtureDef();
        MapBox= new PolygonShape();

        nTileHeight=(int)MapCol.getTileHeight();
        nTileWidth=(int)MapCol.getTileWidth();
        CharBody=character.getCharBody();
        batch= new SpriteBatch();

        for(int i =0; i< MapCol.getWidth(); i++){//makes a 2D grid of box2D rectangles overtop the tiled map
            for(int j=0; j<MapCol.getHeight(); j++){ // Awesome right?
                if(MapCol.getCell(i,j).getTile().getProperties().containsKey("Hit")) {
                    MapDef= new BodyDef();
                    MapFixDef= new FixtureDef();
                    MapBox= new PolygonShape();
                    MapDef.position.set(i*nTileWidth+nTileWidth/2, j*nTileHeight+ nTileHeight/2);//sets the box to proper coordinates to correlate to the tiled map
                    MapBody = world.createBody(MapDef);
                    MapBox.setAsBox((nTileWidth+20)/2, nTileHeight/2);
                    MapBody.createFixture(MapBox, 0f);
                }
            }
        }
        CharBody=world.createBody(character.CharDef);//grabs the character definition from character file
        CharBody.createFixture(character.CharFixDef);//grabs the character's fixture definition from character file
    }

    @Override
    public void render(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(screenControl.nScreen==3) {
            Gdx.input.setInputProcessor(stage);
        }

        CurAnim=character.getCharAnim();
        Time += Gdx.graphics.getDeltaTime();           // #15
        Frame = CurAnim.getKeyFrame(Time, true);

        camera.update();

        nCharX=character.getCharX();
        nCharY=character.getCharY();

        if(touchpadMove.getKnobPercentX()>=0.50){
            nCharVX=150;
        }
        else if(touchpadMove.getKnobPercentX()<=-0.50){
            nCharVX=-150;
        }
        else{
            nCharVX=0;
        }
        CharBody.setLinearVelocity(nCharVX,CharBody.getLinearVelocity().y);

        batch.setProjectionMatrix(camera.combined);
        MapRenderer.setView(camera);
        MapRenderer.render();

        batch.begin();
        batch.draw(Frame, nCharX - 32, nCharY - 32);
        batch.end();

        b2Renderer.render(world, camera.combined);
        world.step(1/60f, 6, 2);
        nCharX=(int)CharBody.getPosition().x;
        nCharY=(int)CharBody.getPosition().y;
        nCharVY=(int)CharBody.getLinearVelocity().y;
        nCharVX=(int)CharBody.getLinearVelocity().x;

        if(nCharVX<0){
            nDir=1;
        }
        if(nCharVX>0){
            nDir=2;
        }
        bCanJump=MapCol.getCell((int) ((nCharX/2)/nTileWidth), (int) ((nCharY-nTileHeight) / nTileHeight))//Collide Down
                .getTile().getProperties().containsKey("Hit");

        character.setVars(nCharVX, nCharVY, nCharX, nCharY, nDir, bCanJump);
        stage.draw();
    }
    @Override
    public void dispose(){
    }
    public void setScreenControl(ScreenControl screenControl_){
        screenControl = screenControl_;
    }
}


