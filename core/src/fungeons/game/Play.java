package fungeons.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Arrays;

import pablo127.almonds.Parse;
import pablo127.almonds.ParseUser;

public class Play implements Screen {
    OrthographicCamera camera;
    Vector2 gravity= new Vector2(0,-9.8f), CurMove, ArrowMove;
    World world=new World(gravity,false);


    DecimalFormat twoDec;
    Label timeLabel, timeLabel2;
    String chatId;
    JSONObject jsonObject;
    boolean bWent;

    BodyDef MapDef;
    Body MapBody, CharBody,CharBody2;
    FixtureDef MapFixDef;
    PolygonShape MapBox;
    WeldJointDef jointDef;
    Joint joint;


    TiledMap Map;
    TiledMapTileLayer MapCol, MapAlt;
    TmxMapLoader MapLoader;
    OrthogonalTiledMapRenderer MapRenderer;

    int nScreenHeight, nScreenWidth, nTileHeight, nTileWidth, nZoomHeight, nZoomWidth;
    float Time, DeadTime,TimeDisplay, ArrowTime, PPM, CharRotation; //PPM is pixels per meter, we use it for box2d conversions since box2d works in meters

    float fCharX,fCharY, DeltaY;
    int nCharVX, nCharVY, nTelX, nTelY, nTel2X, nTel2Y;
    int nDir;
    Timer timer;

    TextureAtlas Atlas;
    TextureRegion[][] MoveBG1, MoveKnob1, btnJump1;

    SpriteBatch batch;
    Sprite sArrow, sChar, sDThing,sChar2;
    Arrow arrow;
    Array<Arrow> arArrows;
    float ArrowX, ArrowY;
    double ArrowVX, ArrowVY;

    Body PlatBody;
    Array<Platform> arPlats;

    Touchpad touchpadMove, touchpadArrow;
    Touchpad.TouchpadStyle touchpadMoveStyle;
    Skin touchpadMoveSkin, btnJumpSkin;
    Drawable touchMoveKnob, touchMoveBackground, btnJumpImg;
    Window deathWindow;

    Button btnJump;
    Button.ButtonStyle btnJumpStyle;

    Stage stage, stage2;
    Boolean bCanJump, bLight=true, bArrowShot, bZoomOut, bDead;

    Sprite sSaw, sPlat, sFire, sSpike, sSpikeBlock;

    Character character;
    Platform platform;
    DeathThing death;
    Trap_Buzzsaw saw;
    Array<Vector2> arTraps;
    ScreenControl screenControl;


    public void create() {

    }

    public void render(){

    }

    @Override
    public void show() {
        world=new World(gravity,false);
        PPM=(1f/16f);
        Atlas = new TextureAtlas(Gdx.files.internal("Fungeons_3.pack"));//grabs texture pack

        nScreenWidth = Gdx.graphics.getWidth();
        nScreenHeight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.viewportHeight = nScreenHeight * PPM / 2;//set the regular zoom of the camera
        camera.viewportWidth = nScreenWidth * PPM / 2;
        nZoomHeight = (int) (nScreenHeight * PPM);//set the zoom of the camera when it pans out for shooting arrows
        nZoomWidth = (int) (nScreenWidth * PPM);
        timer = new Timer();

        CurMove=new Vector2();
        ArrowMove=new Vector2();
        jointDef = new WeldJointDef();

        //time is initialized at the end of the create() method
        DeadTime=-1;
        ArrowTime=1;
        nDir=2;
        fCharX=50;
        fCharY=5;
        bWent=false;
        bCanJump=true;
        bArrowShot=true;
        bDead=false;
        bZoomOut=false;
        twoDec = new DecimalFormat("#0.00");

        character = new Character();
        platform = new Platform();
        death = new DeathThing();
        saw = new Trap_Buzzsaw();
        arTraps = new Array<Vector2>();
        arPlats = new Array <Platform>();
        arArrows = new Array<Arrow>();


        character.create(fCharX,fCharY);
        death.create();
        platform.create();
        stage = new Stage();
        stage2 = new Stage();

        batch = new SpriteBatch();

        /*****Scoreeeee*******/
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.getFont("default-font").setScale(nScreenHeight/256f);
        timeLabel = new Label("0.00", skin);
        timeLabel2=new Label("0.00", skin);
        timeLabel.setPosition(5, nScreenHeight - timeLabel.getHeight());
        stage.addActor(timeLabel);

        deathWindow = new Window("", skin);


        TextureAtlas.AtlasRegion Region;
        int RegionHeight, RegionWidth;
        Region = Atlas.findRegion("Arrow ALT");
        sArrow = new Sprite(Region);
        sArrow.setSize(sArrow.getWidth() * PPM, sArrow.getHeight() * PPM);//sets the size of the arrow texture relative to the box2d world scale
        sArrow.setOrigin(sArrow.getWidth() / 2, sArrow.getHeight() / 2);

        sDThing = new Sprite();
        Drawable dbtnWhite;
        Region = Atlas.findRegion("Button 1");
        TextureRegion btnWhite = Region;
        dbtnWhite = new TextureRegionDrawable(btnWhite);
        Region=Atlas.findRegion("Button 2");
        Drawable dbtnWhiteDn = new TextureRegionDrawable(Region);
        BitmapFont ButtonFont = new BitmapFont(Gdx.files.internal("FungeonsFont.fnt"));
        ButtonFont.setScale(nScreenWidth / 712f);//will implement when Texture pack is fixed
        TextButton.TextButtonStyle btnWhiteStyle = new TextButton.TextButtonStyle(dbtnWhite, dbtnWhiteDn, dbtnWhite, ButtonFont);
        skin.add("btnWhiteStyle", btnWhiteStyle);

        TextButton btnRetry = new TextButton("RETRY", btnWhiteStyle);
        TextButton btnMaps = new TextButton("CHANGE MAP", btnWhiteStyle);
        TextButton btnMain = new TextButton("MAIN MENU", btnWhiteStyle);

        Drawable dBGWall;
        Region=Atlas.findRegion("WindowBG Square");
        TextureRegion BGWall = Region;
        dBGWall = new TextureRegionDrawable(BGWall);
        // deathWindow.setBackground(dBGWall);
        Label deathMessage = new Label("Too Bad",skin);//may make an array of messages and randomly select a string from array
        deathWindow.setSize(nScreenWidth / 1.8f, nScreenHeight / 1.6f);
        deathWindow.setPosition((nScreenWidth / 2) - (deathWindow.getWidth() / 2), (nScreenHeight / 2) - (deathWindow.getHeight() / 2));
        deathWindow.row();
        deathWindow.add(deathMessage).center().padTop(deathWindow.getHeight() / 10f);
        deathWindow.row();
        deathWindow.add(timeLabel2).center().padTop(deathWindow.getHeight() / 20f);
        deathWindow.row();
        btnRetry.setSize(deathWindow.getWidth() / 1.3f, deathWindow.getHeight() / 5f);
        deathWindow.add(btnRetry).center().padBottom(deathWindow.getHeight() / 12).padTop(deathWindow.getHeight() / 18);
        deathWindow.row();
        btnMaps.setSize(deathWindow.getWidth() / 1.3f, deathWindow.getHeight() / 5f);
        deathWindow.add(btnMaps).center().padBottom(deathWindow.getHeight() / 12);
        deathWindow.row();
        btnMain.setSize(deathWindow.getWidth() / 1.3f, deathWindow.getHeight() / 5f);
        deathWindow.add(btnMain).center().padBottom(deathWindow.getHeight() / 12);
        deathWindow.setVisible(false);
        deathWindow.setBackground(dBGWall);

        stage2.addActor(deathWindow);

        Region = Atlas.findRegion("Jump Button");
        RegionHeight = Region.getRegionHeight();
        RegionWidth = Region.getRegionWidth();
        btnJump1 = Region.split(RegionWidth, RegionHeight);
        Region = Atlas.findRegion("Jump Button 2");
        RegionHeight = Region.getRegionHeight();
        RegionWidth = Region.getRegionWidth();
        TextureRegion[][] btnJump2 = Region.split(RegionWidth, RegionHeight);
        btnJumpSkin = new Skin();
        btnJumpSkin.add("button", btnJump1[0][0]);
        btnJumpSkin.add("buttonDown", btnJump2[0][0]);
        btnJumpImg = btnJumpSkin.getDrawable("button");
        Drawable btnJumpImgDn = btnJumpSkin.getDrawable("buttonDown");
        btnJumpStyle = new Button.ButtonStyle();
        btnJumpStyle.down = btnJumpImgDn;
        btnJumpStyle.up = btnJumpImg;
        btnJump = new Button(btnJumpStyle);
        btnJump.setSize((int) (nScreenWidth / 10.24), (int) (nScreenWidth / 10.24));
        btnJump.setPosition(nScreenWidth - (int) (nScreenWidth / 2.84), 0);
        stage.addActor(btnJump);

        Region = Atlas.findRegion("TouchPad BackGround", -1);
        RegionHeight = Region.getRegionHeight();
        RegionWidth = Region.getRegionWidth();
        MoveBG1 = Region.split(RegionWidth, RegionHeight);
        Region = Atlas.findRegion("Touchpad Knob", -1);
        RegionHeight = Region.getRegionHeight();
        RegionWidth = Region.getRegionWidth();
        MoveKnob1 = Region.split(RegionWidth, RegionHeight);


        touchpadMoveSkin = new Skin();
        touchpadMoveSkin.add("MoveKnob", MoveKnob1[0][0]);//we use texture regions here becuase the .getTexture() method was broken
        touchpadMoveSkin.add("MoveBackground", MoveBG1[0][0]);//using texture regions actually worked so it seemed like the best option
        touchMoveKnob = touchpadMoveSkin.getDrawable("MoveKnob");
        touchMoveBackground = touchpadMoveSkin.getDrawable("MoveBackground");
        touchpadMoveStyle = new Touchpad.TouchpadStyle();
        touchpadMoveStyle.knob = touchMoveKnob;
        touchpadMoveStyle.knob.setMinHeight(125);
        touchpadMoveStyle.knob.setMinWidth(125);
        touchpadMoveStyle.background = touchMoveBackground;
        touchpadMove = new Touchpad(0, touchpadMoveStyle);
        touchpadMove.setSize((int) (nScreenWidth / 4.3), (int) (nScreenHeight / 2.4));
        touchpadMove.setPosition(0, 0);

        touchpadArrow = new Touchpad(0, touchpadMoveStyle);
        touchpadArrow.setPosition(nScreenWidth - (int) (nScreenHeight / 2.4), 0);
        touchpadArrow.setSize((int) (nScreenWidth / 4.3), (int) (nScreenHeight / 2.4));
        stage.addActor(touchpadMove);
        stage.addActor(touchpadArrow);

        MapLoader = new TmxMapLoader();
        Map = MapLoader.load("BunsTown.tmx");//name of the tmx map file
        MapCol = (TiledMapTileLayer) Map.getLayers().get(0);//sets a layer of the tiled map that is used for collision
        MapAlt = (TiledMapTileLayer) Map.getLayers().get(1);//sets a layer of the map with inverted colours, not yet used
        MapAlt.setVisible(false);
        nTileHeight = (int) MapCol.getTileHeight();
        nTileWidth = (int) MapCol.getTileWidth();
        MapRenderer = new OrthogonalTiledMapRenderer(Map, PPM);//Map renderer chooses map then the rendering scale, we used PPM for box2d to work properly
        //the rendering scale does not change any sort of scaling for the actual map, just the rendering of it, so the tiles can render
        // to be 2 pixels wide, when they are actually 32 pixels wide, or 64 pixels wide ect.  it makes collsion detection difficult


        MapDef = new BodyDef();
        MapFixDef = new FixtureDef();
        MapBox = new PolygonShape();

        for (int i = 0; i < MapCol.getWidth(); i++) {//makes a 2D grid of box2D rectangles overtop the tiled map
            for (int j = 0; j < MapCol.getHeight(); j++) { // Awesome right?
                if (MapCol.getCell(i, j).getTile().getProperties().containsKey("Hit")) {//we check for the hit key at i, and j coordinates on the tiled map
                    MapDef = new BodyDef();//if a tile has the Hit key, we make a box2d body overtop of it
                    MapBox = new PolygonShape();
                    MapDef.position.set((i * nTileWidth + nTileWidth / 2) * PPM, (j * nTileHeight + nTileHeight / 2) * PPM);//sets the box to proper coordinates to correlate to the tiled map
                    MapBody = world.createBody(MapDef);
                    MapBox.setAsBox((nTileWidth * PPM / 2) + 1, nTileHeight * PPM / 2);//we scale according to the tiled map with the rendering ration of PPM
                    // we made the boxes slightly wider to compensate for so the character doesn't overlap with them at all
                    MapBody.createFixture(MapBox, 1f);
                }
                if(MapCol.getCell(i, j).getTile().getProperties().containsKey("Loop2")){
                    nTel2X=(int)(i*nTileWidth*PPM);
                    nTel2Y= (int) ((j*nTileHeight*PPM)+(5));
                }
                if(MapCol.getCell(i, j).getTile().getProperties().containsKey("Loop")){
                    nTelX=(int)(i*nTileWidth*PPM);
                    nTelY= (int) ((j*nTileHeight*PPM)+(5));
                }
            }
        }

        sSaw = saw.getSprite(Atlas);

        // I tried and tried to grab the character from a seperate file but there are some steps that cannot be skipped
        // the character has to be made using a world, and it wouldn't make sense to make a whole other box2d world
        makeBoxes();



        btnMain.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               // dispose();

                screenControl.setnScreen(7,1);
            }
        });
        btnRetry.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenControl.setnScreen(7,3);


            }
        });
        btnMaps.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //add once map screen is in place

            }
        });

        Time=0;
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(Gdx.graphics.getDeltaTime()<=0.5) {//anything below 0.9 will work, otherwise deltatime will set itself as 1 and increase by ints
            Time += Gdx.graphics.getDeltaTime();
        }


        //  timeLabel.setText(String.valueOf(TimeDisplay));

        camera.position.set(fCharX, fCharY,0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        MapRenderer.setView(camera);
        MapRenderer.render();
        //b2Ren.render(world, camera.combined);//we need this visible for some stuff, mainly because the platforms don't have textures yet

        //char stuff

        DeltaY=fCharY-nTelY;
        arTraps=saw.getTrapArray();
        bDead=death.getDead(arTraps,fCharX,fCharY+1);

        CurMove=(CharBody.getLinearVelocity());//sets a vector to have the current velocity of the characters box3d character

        if(touchpadMove.getKnobPercentX()>=0.50){//if the movement knob is move 50% left or right the character moves 10m/s left or right
            nCharVX= (int) (8f);
        }
        else if(touchpadMove.getKnobPercentX()<=-0.50){
            nCharVX= (int) (-8f);//CHANGE TO 7 and -7
        }
        else{
            nCharVX=0;
        }

        CharBody.setLinearVelocity(nCharVX,CurMove.y);

        bCanJump=character.getJump(CharBody.getLinearVelocity().y, btnJump);
        if(btnJump.isPressed() && bCanJump==true){
            CharBody.setLinearVelocity(CurMove.x,10f);
            CurMove.set(CharBody.getLinearVelocity());
            //if the char can jump, and the button is pressed, the x velocity stays the same, but Y velocity changes to 35m/s upwards
        }

        nCharVY=(int)CharBody.getLinearVelocity().y;
        if (ArrowTime < 1 || bArrowShot == false) {//if either the arrow is drawn, or it has been shot, but a second hasn't passed
            //the character is unable to move
            //   nCharVX = 0;
            // CharBody.setLinearVelocity(0, CurMove.y);
        }

        if(nCharVX<0){
            nDir=1;
        }//sets which way the character is facing, 1 is left, 2 is right
        if(nCharVX>0){
            nDir=2;
        }
        if(ArrowMove.x>0){//these have to be after because they take priority over velocity
            nDir=2;
        }
        if(ArrowMove.x<0){
            nDir=1;
        }

        if(bDead==false) {
            TimeDisplay=Time;
            timeLabel.setText(twoDec.format(Time));
            timeLabel2.setText("Time:  "+twoDec.format(Time));
            fCharX = CharBody.getPosition().x;
            fCharY = CharBody.getPosition().y;
            saw.PlaySound(fCharX,fCharY,arTraps, bDead);
            //   timeLabel.setText(String.format("%.2f",Time));
            //  timeLabel2.setText("Time:  "+String.format("%.2f",Time));
        }


        character.setVars(nCharVX, nCharVY, fCharX, fCharY, nDir, bCanJump, bDead);
   /*     if((fCharX-nTelX)<1 && (fCharX-nTelX)>-1){
            if((fCharY-nTelY)<20 && (fCharY-nTelY)>-2){
                Loop();
            }
        }*/
        sChar=character.getCharSprite(Time, ArrowTime, ArrowMove, bArrowShot);//weird flipping issue, this has to be here
        //Gdx.app.log("FPS", Integer.toString(Gdx.graphics.getFramesPerSecond()));
        if(bDead==true){
            if(DeadTime==-1){
                DeadTime=Time;
            }
            //   world.destroyBody(CharBody);
            // world.destroyBody(CharBody2);
            //can't delete bodies otherwise it freezes if an arrow lands against a wall (tries to form a platform) after death

            CharBody.setLinearVelocity(0, 0);
            bArrowShot=true;
            stage.clear();
            Gdx.input.setInputProcessor(stage2);
            if((Time-DeadTime)>4.5f) {
                deathWindow.setVisible(true);
            }
            //do more death stuff.  might even just call a function that will have everything we need to do at death in it

            /*****************SERVER STUFF IF ONLINE****************/
            //Basically checks if chat id has some value to it, if it does then we know we came from a gameroom. be sure to reset it after ded
            //Upload scoresssssss, beast. basically sketch way. array with player name and then their score. so it wont be in order but when displaying, it'll be alright
            //less of a hassle
            if (!screenControl.getChatId().isEmpty() && !bWent){

                chatId = screenControl.getChatId();
                System.out.println(chatId);
                bWent=true;
                // what we are gonna do is upload the scores first and foremost
                final Net.HttpRequest httpRequest;
                httpRequest = new Net.HttpRequest(Net.HttpMethods.PUT);
                httpRequest.setUrl("https://api.parse.com/1/classes/chat/" + chatId);
                httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
                httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());
                JSONObject json = new JSONObject();
                JSONObject skills = new JSONObject();
                skills.put("__op", "Add");
                skills.put("objects", new JSONArray(Arrays.asList(ParseUser.getCurrentUser().getUsername().toString() + ": " + twoDec.format(Time))));
                json.put("scores", skills);
                httpRequest.setContent(json.toString());
                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        //what we will do is open up a new screen and display scores. This game keeps rendering still... window wont take priority
                        // dispose();
                        screenControl.setnScreen(7,5);//was 5,3
                        /**this bit of code is to upload to profile but dont worry about this for now**/
                                       /* final Net.HttpRequest httpRequest;
                                        httpRequest = new Net.HttpRequest(Net.HttpMethods.PUT);
                                        httpRequest.setUrl("https://api.parse.com/1/classes/_User/" + ParseUser.getCurrentUser().getObjectId());
                                        screenControl.setChatId("");
                                        httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
                                        httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());
                                        JSONObject json = new JSONObject();
                                        JSONObject skills = new JSONObject();
                                        skills.put("__op", "Add");
                                        skills.put("objects", new JSONArray(Arrays.asList( "Gamename" + ": " + twoDec.format(Time))));
                                        json.put("games", skills);
                                        httpRequest.setContent(json.toString());
                                        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                                            @Override
                                            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                                            }

                                            @Override
                                            public void failed(Throwable t) {
                                                System.out.println(t.toString());
                                            }

                                            @Override
                                            public void cancelled() {

                                            }
                                        });*/
                        /***********************IGNORE************************/
                    }

                    @Override
                    public void failed(Throwable t) {
                        System.out.println(t.toString());
                    }

                    @Override
                    public void cancelled() {

                    }
                });
            }
        }


        bZoomOut=false;
        for (int i = -5; i <= 5; i++) { //loop left and right 5 tiles each way on the map
            while((((fCharX / PPM) / nTileWidth) + i)<0){
                i++;
            }
            if (MapCol.getCell((int) ((fCharX / PPM) / nTileWidth) + i, (int) ((fCharY / PPM) / nTileHeight))//Collide on Left
                    .getTile().getProperties().containsKey("Hit")) { //grabs tiles that have hit key beside char for zooming out
                bZoomOut = true;//if any tile has the hit key within 5 tiles left and right, the camera will zoom out
                break; //leaves loop incase next tile does not have hit property
            }
        }
        if(bZoomOut==true){//changes zoom in such a way that it will zoom out quickly then slowly until it stops
            camera.viewportWidth+=(nZoomWidth*1.1-camera.viewportWidth)/7;
            camera.viewportHeight+=(nZoomHeight*1.1-camera.viewportHeight)/8;
        }
        if(bZoomOut==false){//changes zoom in such a way that it will zoom back in quickly then slowly until it stops
            camera.viewportWidth+=((nScreenWidth*PPM/2)-camera.viewportWidth)/7;
            camera.viewportHeight+=((nScreenHeight*PPM/2)-camera.viewportHeight)/8;
        }

        //Arrow Stuff Now--

        if(touchpadArrow.isTouched() && ArrowTime>1){//Arrow time prevents rapid arrow shooting
            ArrowMove.set(-touchpadArrow.getKnobPercentX(),-touchpadArrow.getKnobPercentY());//sets arrow velocity using touchpad
            CharRotation=(float) (Math.atan(ArrowMove.y/ArrowMove.x))* MathUtils.radiansToDegrees;//rotates arrow sprite
            bArrowShot=false;//changes boolean to indicate the arrow is drawn
        }

        if(touchpadArrow.isTouched()==false && bArrowShot==false){
            ArrowTime=0;//resets the arrow timer
            arrow = new Arrow();//makes a new arrow
            arrow.setVars(ArrowMove.x, ArrowMove.y, CharBody2.getPosition().x-1, CharBody2.getPosition().y);//sets variables of the new arrow
            arArrows.add(arrow);//adds arrow to the arrow array
            bArrowShot=true;//changes boolean to indicate arrow has been shot
            ArrowMove.set(0,0);//resets arrow velocity vector
        }
        if(bArrowShot==true){
            ArrowTime+=Gdx.graphics.getDeltaTime();
        }

        batch.begin();

        if(ArrowTime<0.8f|| bArrowShot==false){
            if(bDead==false) {
                sChar.setPosition(fCharX - 2, fCharY);//if the character's sprite is the arrow shooting one, we change it's position because it is smaller than the other sprites
                sChar2 = character.getSprite2();
                sChar2.draw(batch);
            }

        }

        sChar.draw(batch);



        for(int i =0; i<arArrows.size; i++) {//loops through the array of arrows

            arrow = arArrows.get(i);
            ArrowX = arrow.getArrowX();
            ArrowY = arrow.getArrowY();
            ArrowVX = arrow.getArrowVX();
            ArrowVY = arrow.getArrowVY();
            ArrowVY -= 9.8 / 1024f;//sets arrow gravity, we want them to fall pretty slow
            ArrowX += ArrowVX;
            ArrowY += ArrowVY;
            sArrow.setRotation((float) (Math.atan(ArrowVY / ArrowVX)) * MathUtils.radiansToDegrees);//rotates arrow based on velocity
            if (ArrowVX > 0) {
                sArrow.setRotation(sArrow.getRotation() + 180);//flips arrow if it's going to the right due to the images orientation
            }

            sArrow.setPosition(ArrowX, ArrowY);
            sArrow.draw(batch);
            arrow.setVars(ArrowVX, ArrowVY, ArrowX, ArrowY);

            try {

                if (MapCol.getCell((int) ((ArrowX / PPM) / nTileWidth), (int) ((ArrowY / PPM) / nTileHeight))//Collide on Left
                        .getTile().getProperties().containsKey("Hit") ||
                        MapCol.getCell((int) ((sArrow.getWidth() + ArrowX) / PPM / nTileWidth), (int) ((ArrowY / PPM) / nTileHeight))//Collide on Right
                                .getTile().getProperties().containsKey("Hit")) {
                    //if there is a tile with the hit key to the left or right of the current arrow

                    if ((int) ((ArrowX / PPM) / nTileWidth) - 1<=0 ||(int) ((ArrowX / PPM) / nTileWidth) + 1>=MapCol.getWidth()-1 ||
                            MapCol.getCell((int) ((ArrowX / PPM) / nTileWidth) - 1, (int) ((ArrowY / PPM) / nTileHeight))//Collide on Left
                                    .getTile().getProperties().containsKey("Hit") == false ||
                            MapCol.getCell((int) ((sArrow.getWidth() + ArrowX) / PPM / nTileWidth) + 1, (int) ((ArrowY / PPM) / nTileHeight))//Collide on Right
                                    .getTile().getProperties().containsKey("Hit") == false) {
                        //if there is a tile to the left of the arrow or to the right of the arrow
                        //first line of that if statement (I hate how many there are btw) determines if the tile is on the edge
                        //of the map because that will not allow the check to go through and even though the tile is legit
                        //those come first because the try statement will throw it all away if it finds out those tiles are null


                        if (arPlats.size > 0) {//now we loop through all of the platforms created
                            for (int j = 0; j < arPlats.size; j++) {

                                platform = arPlats.get(j);//get current platform
                                Vector2 ArrowVec, pos;
                                ArrowVec = new Vector2(ArrowX, ArrowY);
                                pos = new Vector2(platform.getPosition());//make a vector equal to the position of the platforms coordinates
                                if (ArrowVec.dst(pos) < 9) {//if the distance between the two is less than 9, this does the pythagorean equation for you
                                    break;//we break the loop because there is a platform too close, so we cannot make a new one
                                }
                                if (j == arPlats.size - 1) {//if we're at the end of the loop, there must be no platform too close to make a new one
                                    platform = new Platform();// so we make a new platform
                                    if(CharBody.getPosition().y-ArrowY>-2 && CharBody.getPosition().y-ArrowY<=1) {
                                        ArrowY+=CharBody.getPosition().y-ArrowY-1.5f;
                                    }
                                    if(CharBody.getPosition().y-ArrowY>=-3 && CharBody.getPosition().y-ArrowY<=-2) {
                                        ArrowY-=CharBody.getPosition().y-ArrowY+1.5f;
                                    }
                                    PlatBody = platform.makePlat(ArrowVX, ArrowX, ArrowY, world);
                                    PlatBody = world.createBody(platform.PlatDef);
                                    arPlats.add(platform);// add new platform to the array

                                }
                            }
                        } else {//if the platform array length is non existant we just make one becuase there is no platfomr for it to overlap with or anything like that
                            platform = new Platform();
                            if(CharBody.getPosition().y-ArrowY>-2 && CharBody.getPosition().y-ArrowY<=1) {
                                ArrowY+=CharBody.getPosition().y-ArrowY-1.5f;
                            }
                            if(CharBody.getPosition().y-ArrowY>=-3 && CharBody.getPosition().y-ArrowY<=-2) {
                                ArrowY-=CharBody.getPosition().y-ArrowY+1.5f;
                            }
                            PlatBody = platform.makePlat(ArrowVX, ArrowX, ArrowY, world);
                            PlatBody = world.createBody(platform.PlatDef);
                            arPlats.add(platform);
                        }
                    }


                    arArrows.removeIndex(i);
                    i--;//we had an issue where arrows would flicker if one is deleted, so we do this to prevent that
                }
            }
            catch(NullPointerException e){}

        }
        death.setVars(MapCol, CharBody.getPosition(), nTelX, nTelY, nTel2X, nTel2Y);
        sDThing=death.getSprite(Time);
        sDThing.draw(batch);


        if(bZoomOut==false) {
            if(fCharX-nTelX<=80 && fCharX-nTelX>=-80 &&fCharY-nTelY<24 && fCharY-nTelY>-24) {}

            else{
                saw.setVars(nCharVX, fCharX, fCharY, MapCol, arTraps);
            }
        }
        for(int i=0;i<arTraps.size;i++){
            sSaw.setPosition(arTraps.get(i).x-sSaw.getWidth()/2,arTraps.get(i).y-sSaw.getHeight()/2);
            sSaw.setRotation(sSaw.getRotation()-40);
            sSaw.draw(batch);
        }
        for(int i=0;i<arPlats.size;i++){
            sPlat=arPlats.get(i).getSprite(Atlas);
            sPlat.draw(batch);
        }
        batch.end();
        CharBody2.setLinearVelocity(CharBody.getLinearVelocity());
        // for(int i=0;i<5;i++){//we step the world 5 times to speed it up so it doesn't look like it's going in slo mo
        world.step(1f/60f, 8, 3);//moves the box2d world
        world.step(1f/60f, 8, 3);

        character.LoopCheck(nTelX, nTelY, CharBody.getPosition().x, CharBody.getPosition().y, this);
        //  }
        stage.draw();
        stage2.draw();
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
    public void dispose(){//disposes stuff
        batch.dispose();
        world.dispose();
        stage.dispose();
        stage2.dispose();
        Map.dispose();
        MapRenderer.dispose();
        Atlas.dispose();
        touchpadMoveSkin.dispose();
        btnJumpSkin.dispose();
        touchpadMoveSkin.dispose();
        death.dispose();
        character.dispose();
        saw.dispose();
        timer.clear();

       // show();// makes it so that this file is ready to go once everything is cleared out


//        batch.dispose();
       // stage.dispose();

    }
    public void setScreenControl(ScreenControl screenControl_){
        screenControl = screenControl_;
    }
    public void Loop(){

        for(int i=0;i<arPlats.size;i++){
            platform=arPlats.get(i);
            world.destroyBody(platform.PlatBody);
        }
        arPlats.clear();
        arTraps.clear();
        fCharX=nTel2X;
        fCharY=nTel2Y+DeltaY;
        float VY = CharBody.getLinearVelocity().y;
        makeBoxes();
        CharBody.setLinearVelocity(CharBody.getLinearVelocity().x,VY);
        CharBody2.setLinearVelocity(CharBody2.getLinearVelocity().x,VY);
    }
    public void makeBoxes(){
       // character.dispose();
        //character.create(nTel2X,nTel2Y);
        character.CharDef.position.set(nTel2X,fCharY);
        CharBody = world.createBody(character.CharDef);//grabs the character definition from character file
        CharBody.createFixture(character.CharFixDef);//grabs the character's fixture definition from character file
        character.CharDef.position.set(nTel2X,fCharY+0.5f);
        CharBody2 = world.createBody(character.CharDef);
        CharBody2.createFixture(character.CharFixDef2);
        CharBody.setFixedRotation(true);// makes it so the body cannot rotate
        jointDef = character.jointDef;// with the joint, this too had to be made in the same file as the box2d world
        jointDef.bodyA = CharBody;//get the first body that will be on the joint
        jointDef.bodyB = CharBody2;//2nd body on the joint
        jointDef.localAnchorA.set(0, 2f);//sets origin of anchor on first body, and length of the joint to the 2nd body
        joint = world.createJoint(jointDef);
    }
}


