package fungeons.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class Play extends Game {
    OrthographicCamera camera;
    Vector2 gravity=new Vector2(0,-9.8f), CurMove=new Vector2(), ArrowMove=new Vector2();// we multiply the gravity by 32 because of an image scaling issue with our box2D world
    World world = new World(gravity, false);

    BodyDef MapDef;
    Body MapBody, CharBody,CharBody2;
    FixtureDef MapFixDef;
    PolygonShape MapBox;
    WeldJointDef jointDef = new WeldJointDef();
    Joint joint;

    Box2DDebugRenderer b2Renderer;

    TiledMap Map;
    TiledMapTileLayer MapCol, MapAlt;
    TmxMapLoader MapLoader;
    OrthogonalTiledMapRenderer MapRenderer;

    int nScreenHeight, nScreenWidth, nTileHeight, nTileWidth;
    float Time=1, ArrowTime=1, PPM=(1f/16f), CharRotation;

    float fCharX =15,fCharY=15;
    int nCharVX, nCharVY, nUp1Dn2=0;
    int nDir=2;

    TextureAtlas Atlas;
    TextureRegion[][] MoveBG1, MoveKnob1, btnJump1;

    SpriteBatch batch;
    Sprite sArrow, sChar;
    Arrow arrow;
    Array<Arrow> arArrows = new Array<Arrow>();
    float ArrowX, ArrowY;
    double ArrowVX, ArrowVY;

    float fPlatX, fPlatY;
    Body PlatBody;
    Array<Platform> arPlats = new Array <Platform>();

    Touchpad touchpadMove, touchpadArrow;
    Touchpad.TouchpadStyle touchpadMoveStyle;
    Skin touchpadMoveSkin, btnJumpSkin;
    Drawable touchMoveKnob, touchMoveBackground, btnJumpImg;

    Button btnJump;
    Button.ButtonStyle btnJumpStyle;

    Stage stage;
    Boolean bCanJump=false, bLight=true, bArrowShot=true;

    Character character = new Character();
    Platform platform = new Platform();
    ScreenControl screenControl;


    @Override
    public void create() {
        nScreenWidth= Gdx.graphics.getWidth();
        nScreenHeight=Gdx.graphics.getHeight();
        camera=new OrthographicCamera();
        camera.viewportHeight=nScreenHeight*PPM/2;
        camera.viewportWidth=nScreenWidth*PPM/2;

        character.create();
        stage=new Stage();
        Atlas= new TextureAtlas(Gdx.files.internal("Fungeons_2.pack"));
        batch= new SpriteBatch();

        TextureAtlas.AtlasRegion Region;
        int RegionHeight, RegionWidth;
        Region=Atlas.findRegion("Arrow ALT");
        sArrow = new Sprite(Region);
        sArrow.setSize(sArrow.getWidth()*PPM,sArrow.getHeight()*PPM);
        sArrow.setOrigin(sArrow.getWidth()/2,sArrow.getHeight()/2);

        Region = Atlas.findRegion("Jump Button");
        RegionHeight=Region.getRegionHeight();
        RegionWidth=Region.getRegionWidth();
        btnJump1 = Region.split(RegionWidth, RegionHeight);
        btnJumpSkin= new Skin();
        btnJumpSkin.add("button", btnJump1[0][0]);
        btnJumpImg=btnJumpSkin.getDrawable("button");
        btnJumpStyle=new Button.ButtonStyle();
        btnJumpStyle.down=btnJumpImg;
        btnJumpStyle.up=btnJumpImg;
        btnJump= new Button(btnJumpStyle);
        btnJump.setSize(125,125);
        btnJump.setPosition(nScreenWidth-450,0);
        stage.addActor(btnJump);

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
        touchpadMoveStyle.knob.setMinHeight(125);
        touchpadMoveStyle.knob.setMinWidth(125);
        touchpadMoveStyle.background = touchMoveBackground;
        touchpadMove = new Touchpad(0, touchpadMoveStyle);
        touchpadMove.setSize(300, 300);
        touchpadMove.setPosition(0, 0);

        touchpadArrow= new Touchpad(0,touchpadMoveStyle);
        touchpadArrow.setPosition(nScreenWidth-300,0);
        touchpadArrow.setSize(300,300);
        stage.addActor(touchpadMove);
        stage.addActor(touchpadArrow);

        MapLoader=new TmxMapLoader();
        Map=MapLoader.load("BunsTown.tmx");
        MapCol= (TiledMapTileLayer) Map.getLayers().get(0);
        MapAlt= (TiledMapTileLayer) Map.getLayers().get(1);
        MapAlt.setVisible(false);
        nTileHeight=(int)MapCol.getTileHeight();
        nTileWidth=(int)MapCol.getTileWidth();
        MapRenderer = new OrthogonalTiledMapRenderer(Map, PPM);

        b2Renderer=new Box2DDebugRenderer();

        MapDef= new BodyDef();
        MapFixDef= new FixtureDef();
        MapBox= new PolygonShape();

        for(int i =0; i< MapCol.getWidth(); i++){//makes a 2D grid of box2D rectangles overtop the tiled map
            for(int j=0; j<MapCol.getHeight(); j++){ // Awesome right?
                if(MapCol.getCell(i,j).getTile().getProperties().containsKey("Hit")) {
                    MapDef= new BodyDef();
                    MapBox= new PolygonShape();
                    MapDef.position.set((i*nTileWidth+nTileWidth/2)*PPM, (j*nTileHeight+ nTileHeight/2)*PPM);//sets the box to proper coordinates to correlate to the tiled map
                    MapBody = world.createBody(MapDef);
                    MapBox.setAsBox((nTileWidth*PPM/2)+1, nTileHeight*PPM/2);
                    MapBody.createFixture(MapBox, 1f);
                }
            }
        }

        CharBody=world.createBody(character.CharDef);//grabs the character definition from character file
        CharBody.createFixture(character.CharFixDef);//grabs the character's fixture definition from character file
        CharBody2=world.createBody(character.CharDef);
        CharBody2.createFixture(character.CharFixDef);
        CharBody.setFixedRotation(true);
        jointDef=character.jointDef;
        jointDef.bodyA=CharBody;
        jointDef.bodyB=CharBody2;
        jointDef.localAnchorA.set(0,2f);
        joint =world.createJoint(jointDef);


    }

    @Override
    public void render(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Time += Gdx.graphics.getDeltaTime();           // #15
        camera.position.set(fCharX, fCharY,0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        MapRenderer.setView(camera);
        MapRenderer.render();
        b2Renderer.render(world, camera.combined);



        stage.draw();

        if(screenControl.nScreen==3) {
            Gdx.input.setInputProcessor(stage);
        }

        CurMove=(CharBody.getLinearVelocity());
        fCharX=character.getCharX();
        fCharY=character.getCharY();

        if(touchpadMove.getKnobPercentX()>=0.50){
            nCharVX= (int) (10f);
        }
        else if(touchpadMove.getKnobPercentX()<=-0.50){
            nCharVX= (int) (-10f);
        }
        else{
            nCharVX=0;
        }
        if(nCharVY>0){
            nUp1Dn2=1;//determines if most previous motion was up or down, does not change when char is still
        }
        if(nCharVY<0){
            nUp1Dn2=2;
        }

        fCharX=CharBody.getPosition().x;
        fCharY=CharBody.getPosition().y;

        if(nCharVY==0){
            if(nUp1Dn2==2) {
                bCanJump=true;
            }
        }
        if(btnJump.isPressed() && bCanJump==true){
            CharBody.setLinearVelocity(CurMove.x,30);
            CurMove.set(CharBody.getLinearVelocity());
            bCanJump=false;
        }
        CharBody.setLinearVelocity(nCharVX, CurMove.y);//add 0.2f to counteract sticking glitch


        nCharVY=(int)CharBody.getLinearVelocity().y;
        nCharVX=(int)CharBody.getLinearVelocity().x;
            if (ArrowTime < 1 || bArrowShot == false) {
                nCharVX = 0;
                CharBody.setLinearVelocity(0, CurMove.y);
            }

        if(nCharVX<0 || ArrowMove.x<0){
            nDir=1;
        }
        if(nCharVX>0 || ArrowMove.x>0){
            nDir=2;
        }
        character.setVars(nCharVX, nCharVY, fCharX, fCharY, nDir, bCanJump);

        sChar=character.getCharSprite(Time, ArrowTime, ArrowMove, bArrowShot);//weird flipping issue, this has to be here

        //Arrow Stuff Now--

        if(touchpadArrow.isTouched() && ArrowTime>1){
            ArrowMove.set(-touchpadArrow.getKnobPercentX()*2,-touchpadArrow.getKnobPercentY()*2);
            CharRotation=(float) (Math.atan(ArrowMove.y/ArrowMove.x))* MathUtils.radiansToDegrees;
            bArrowShot=false;
        }

        if(touchpadArrow.isTouched()==false && bArrowShot==false){
            ArrowTime=0;
            arrow = new Arrow();
            arrow.setVars(ArrowMove.x, ArrowMove.y, CharBody2.getPosition().x-1, CharBody2.getPosition().y);
            arArrows.add(arrow);
            bArrowShot=true;
            ArrowMove.set(0,0);
        }
        if(bArrowShot==true){
            ArrowTime+=Gdx.graphics.getDeltaTime();
        }

        //Platform Stuff Now--
        for(int i=0;i<arPlats.size;i++){
            //Will eventually be for when the platforms have images

        }

        batch.begin();

        if(ArrowTime<1|| bArrowShot==false){
            sChar.setPosition(fCharX-2,fCharY);
        }

        if( nDir==2 && (bArrowShot==false||(bArrowShot==true && ArrowTime<1))){
            sChar.flip(true,false);
        }
        sChar.draw(batch);


        for(int i =0; i<arArrows.size; i++){
            arrow=arArrows.get(i);
            ArrowX=arrow.getArrowX();
            ArrowY=arrow.getArrowY();
            ArrowVX=arrow.getArrowVX();
            ArrowVY=arrow.getArrowVY();
            ArrowVY-=9.8/180f;
            ArrowX+=ArrowVX;
            ArrowY+=ArrowVY;
            sArrow.setRotation((float) (Math.atan(ArrowVY/ArrowVX))* MathUtils.radiansToDegrees);
            if(ArrowVX>0){
                sArrow.setRotation(sArrow.getRotation()+180);
            }

            sArrow.setPosition(ArrowX, ArrowY);
            sArrow.draw(batch);
            arrow.setVars(ArrowVX, ArrowVY, ArrowX, ArrowY);

                if(MapCol.getCell( (int)((ArrowX/PPM)/nTileWidth), (int)((ArrowY/PPM)/nTileHeight))//Collide on Left
                        .getTile().getProperties().containsKey("Hit") ||
                        MapCol.getCell( (int)((sArrow.getWidth()+ArrowX)/PPM/nTileWidth), (int)((ArrowY/PPM)/nTileHeight))//Collide on Right
                        .getTile().getProperties().containsKey("Hit")){

                    if(arPlats.size>0) {
                        for (int j = 0; j < arPlats.size; j++) {
                            platform = arPlats.get(j);
                            Vector2 ArrowVec, pos;
                            ArrowVec=new Vector2(ArrowX,ArrowY);
                            pos=new Vector2(platform.getPosition());
                            System.out.println(pos+"     3");
                            if (ArrowVec.dst(pos)<10) {
                                break;
                            }
                                    if(j==arPlats.size-1) {
                                        platform=new Platform();
                                        PlatBody = platform.makePlat(ArrowVX, ArrowX, ArrowY, world);
                                        PlatBody = world.createBody(platform.PlatDef);

                                        arPlats.add(platform);
                                    }

                            }
                        }

                    else{
                        platform = new Platform();
                        PlatBody = platform.makePlat(ArrowVX, ArrowX, ArrowY, world);
                        PlatBody = world.createBody(platform.PlatDef);

                        arPlats.add(platform);
                    }

                    arArrows.removeIndex(i);
                    i--;

                }
        }

        batch.end();
        for(int i=0;i<5;i++){
            world.step(1f/60f, 8, 3);
        }

    }
    @Override
    public void dispose(){
        batch.dispose();
        world.dispose();
        stage.dispose();
        Map.dispose();
    }
    public void setScreenControl(ScreenControl screenControl_){
        screenControl = screenControl_;
    }
}


