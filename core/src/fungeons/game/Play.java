package fungeons.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Arrays;

import pablo127.almonds.Parse;
import pablo127.almonds.ParseUser;

public class Play extends Game {
    OrthographicCamera camera;
    Vector2 gravity=new Vector2(0,-9.8f), CurMove=new Vector2(), ArrowMove=new Vector2();
    World world = new World(gravity, false);

    DecimalFormat twoDec = new DecimalFormat("#0.00");
    Label timeLabel;
    String chatId;

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

    int nScreenHeight, nScreenWidth, nTileHeight, nTileWidth, nZoomHeight, nZoomWidth;
    float Time=1, ArrowTime=1, PPM=(1f/16f), CharRotation; //PPM is pixels per meter, we use it for box2d conversions since box2d works in meters

    float fCharX =20,fCharY=5, DeltaY;
    int nCharVX, nCharVY;
    int nDir=2;

    TextureAtlas Atlas;
    TextureRegion[][] MoveBG1, MoveKnob1, btnJump1;

    SpriteBatch batch;
    Sprite sArrow, sChar, sDThing;
    Arrow arrow;
    Array<Arrow> arArrows = new Array<Arrow>();
    float ArrowX, ArrowY, DThingX, DThingY;
    double ArrowVX, ArrowVY;

    Body PlatBody;
    Array<Platform> arPlats = new Array <Platform>();

    Touchpad touchpadMove, touchpadArrow;
    Touchpad.TouchpadStyle touchpadMoveStyle;
    Skin touchpadMoveSkin, btnJumpSkin;
    Drawable touchMoveKnob, touchMoveBackground, btnJumpImg;

    Button btnJump;
    Button.ButtonStyle btnJumpStyle;

    Stage stage;
    Boolean bCanJump=true, bLight=true, bArrowShot=true, bZoomOut, bDead;

    Sprite sSaw, sFire, sSpike, sSpikeBlock;

    Character character = new Character();
    Platform platform = new Platform();
    DeathThing death = new DeathThing();
    Trap_Buzzsaw saw = new Trap_Buzzsaw();
    Array<Vector2> arTraps = new Array<Vector2>();
    ScreenControl screenControl;


    @Override
    public void create() {
        nScreenWidth= Gdx.graphics.getWidth();
        nScreenHeight=Gdx.graphics.getHeight();
        camera=new OrthographicCamera();
        camera.viewportHeight=nScreenHeight*PPM/2;//set the regular zoom of the camera
        camera.viewportWidth=nScreenWidth*PPM/2;
        nZoomHeight=(int)(nScreenHeight*PPM);//set the zoom of the camera when it pans out for shooting arrows
        nZoomWidth=(int)(nScreenWidth*PPM);

        character.create();
        death.create();
        stage=new Stage();
        Atlas= new TextureAtlas(Gdx.files.internal("Fungeons_2.pack"));//grabs texture pack
        batch= new SpriteBatch();

        /*****Scoreeeee*******/
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        timeLabel = new Label("", skin);
        timeLabel.setPosition(0, nScreenHeight - 10);
        stage.addActor(timeLabel);



        TextureAtlas.AtlasRegion Region;
        int RegionHeight, RegionWidth;
        Region=Atlas.findRegion("Arrow ALT");
        sArrow = new Sprite(Region);
        sArrow.setSize(sArrow.getWidth()*PPM,sArrow.getHeight()*PPM);//sets the size of the arrow texture relative to the box2d world scale
        sArrow.setOrigin(sArrow.getWidth()/2,sArrow.getHeight()/2);

        sDThing=new Sprite();

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
        touchpadMove.setSize(300, 300);
        touchpadMove.setPosition(0, 0);

        touchpadArrow= new Touchpad(0,touchpadMoveStyle);
        touchpadArrow.setPosition(nScreenWidth-300,0);
        touchpadArrow.setSize(300,300);
        stage.addActor(touchpadMove);
        stage.addActor(touchpadArrow);

        MapLoader=new TmxMapLoader();
        Map=MapLoader.load("BunsTown.tmx");//name of the tmx map file
        MapCol= (TiledMapTileLayer) Map.getLayers().get(0);//sets a layer of the tiled map that is used for collision
        MapAlt= (TiledMapTileLayer) Map.getLayers().get(1);//sets a layer of the map with inverted colours, not yet used
        MapAlt.setVisible(false);
        nTileHeight=(int)MapCol.getTileHeight();
        nTileWidth=(int)MapCol.getTileWidth();
        MapRenderer = new OrthogonalTiledMapRenderer(Map, PPM);//Map renderer chooses map then the rendering scale, we used PPM for box2d to work properly
        //the rendering scale does not change any sort of scaling for the actual map, just the rendering of it, so the tiles can render
        // to be 2 pixels wide, when they are actually 32 pixels wide, or 64 pixels wide ect.  it makes collsion detection difficult

        b2Renderer=new Box2DDebugRenderer();

        MapDef= new BodyDef();
        MapFixDef= new FixtureDef();
        MapBox= new PolygonShape();

        for(int i =0; i< MapCol.getWidth(); i++){//makes a 2D grid of box2D rectangles overtop the tiled map
            for(int j=0; j<MapCol.getHeight(); j++){ // Awesome right?
                if(MapCol.getCell(i,j).getTile().getProperties().containsKey("Hit")) {//we check for the hit key at i, and j coordinates on the tiled map
                    MapDef= new BodyDef();//if a tile has the Hit key, we make a box2d body overtop of it
                    MapBox= new PolygonShape();
                    MapDef.position.set((i*nTileWidth+nTileWidth/2)*PPM, (j*nTileHeight+ nTileHeight/2)*PPM);//sets the box to proper coordinates to correlate to the tiled map
                    MapBody = world.createBody(MapDef);
                    MapBox.setAsBox((nTileWidth*PPM/2)+1, nTileHeight*PPM/2);//we scale according to the tiled map with the rendering ration of PPM
                    // we made the boxes slightly wider to compensate for so the character doesn't overlap with them at all
                    MapBody.createFixture(MapBox, 1f);
                }
            }
        }
        sSaw=saw.getSprite(Atlas);

        // I tried and tried to grab the character from a seperate file but there are some steps that cannot be skipped
        // the character has to be made using a world, and it wouldn't make sense to make a whole other box2d world
        CharBody=world.createBody(character.CharDef);//grabs the character definition from character file
        CharBody.createFixture(character.CharFixDef);//grabs the character's fixture definition from character file
        CharBody2=world.createBody(character.CharDef);
        CharBody2.createFixture(character.CharFixDef);
        CharBody.setFixedRotation(true);// makes it so the body cannot rotate
        jointDef=character.jointDef;// with the joint, this too had to be made in the same file as the box2d world
        jointDef.bodyA=CharBody;//get the first body that will be on the joint
        jointDef.bodyB=CharBody2;//2nd body on the joint
        jointDef.localAnchorA.set(0,2f);//sets origin of anchor on first body, and length of the joint to the 2nd body
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
        b2Renderer.render(world, camera.combined);//we need this visible for some stuff, mainly because the platforms don't have textures yet

        if(screenControl.nScreen==3) {
            Gdx.input.setInputProcessor(stage);//uesr changes the control of the start menu to this when screens change
        }

        //char stuff
        arTraps=saw.getTrapArray();
        bDead=death.getDead(arTraps,fCharX,fCharY+1);

        CurMove=(CharBody.getLinearVelocity());//sets a vector to have the current velocity of the characters box3d character

        if(touchpadMove.getKnobPercentX()>=0.50){//if the movement knob is move 50% left or right the character moves 10m/s left or right
            nCharVX= (int) (10f);
        }
        else if(touchpadMove.getKnobPercentX()<=-0.50){
            nCharVX= (int) (-10f);
        }
        else{
            nCharVX=0;
        }


        CharBody.setLinearVelocity(nCharVX,CurMove.y);

        bCanJump=character.getJump(CharBody.getLinearVelocity().y, btnJump);
        if(btnJump.isPressed() && bCanJump==true){
            CharBody.setLinearVelocity(CurMove.x,14);
            CurMove.set(CharBody.getLinearVelocity());
            //if the char can jump, and the button is pressed, the x velocity stays the same, but Y velocity changes to 35m/s upwards
        }

        nCharVY=(int)CharBody.getLinearVelocity().y;
            if (ArrowTime < 1 || bArrowShot == false) {//if either the arrow is drawn, or it has been shot, but a second hasn't passed
                //the character is unable to move
                nCharVX = 0;
                CharBody.setLinearVelocity(0, CurMove.y);
            }

        if(nCharVX<0 || ArrowMove.x<0){
            nDir=1;
        }//sets which way the character is facing, 1 is left, 2 is right
        if(nCharVX>0 || ArrowMove.x>0){
            nDir=2;
        }


        if(bDead==false) {
            fCharX = CharBody.getPosition().x;
            fCharY = CharBody.getPosition().y;
        }


        character.setVars(nCharVX, nCharVY, fCharX, fCharY, nDir, bCanJump, bDead);

        sChar=character.getCharSprite(Time, ArrowTime, ArrowMove, bArrowShot);//weird flipping issue, this has to be here
        //Gdx.app.log("FPS", Integer.toString(Gdx.graphics.getFramesPerSecond()));

        if(bDead==true){
         //   world.destroyBody(CharBody);
           // world.destroyBody(CharBody2);
            //can't delete bodies otherwise it freezes if an arrow lands against a wall (tries to form a platform) after death

            CharBody.setLinearVelocity(0, 0);
            bArrowShot=true;
            stage.clear();
            //do more death stuff.  might even just call a function that will have everything we need to do at death in it

            /*****************SERVER STUFF IF ONLINE****************/
            //Basically checks if chat id has some value to it, if it does then we know we came from a gameroom. be sure to reset it after ded
            //Upload scoresssssss, beast. basically sketch way. array with player name and then their score. so it wont be in order but when displaying, it'll be alright
            //less of a hassle
            if (!screenControl.getChatId().isEmpty()){
                chatId = screenControl.getChatId();
                // check if you are the last one, if you are, you need to remove the gameroom.
                final String requestContent = null;
                final Net.HttpRequest httpRequest2;
                httpRequest2 = new Net.HttpRequest(Net.HttpMethods.GET);
                httpRequest2.setUrl("https://api.parse.com/1/classes/chat/"+chatId);
                httpRequest2.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
                httpRequest2.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());

                httpRequest2.setContent(requestContent);
                Gdx.net.sendHttpRequest(httpRequest2, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        try {
                            JSONObject jsonObject = new JSONObject(httpResponse.getResultAsString());
                            System.out.println("wat"+jsonObject.getString("scores").length() + "shit");
                            if (jsonObject.getString("scores").length()>=3){
                                final String requestContent = null;
                                final Net.HttpRequest httpRequest;
                                httpRequest = new Net.HttpRequest(Net.HttpMethods.DELETE);
                                httpRequest.setUrl("https://api.parse.com/1/classes/chat/" + chatId);
                                screenControl.setChatId("");
                                httpRequest.setHeader("X-Parse-Application-Id", Parse.getApplicationId());
                                httpRequest.setHeader("X-Parse-REST-API-Key", Parse.getRestAPIKey());

                                httpRequest.setContent(requestContent);
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
                                });

                            }
                            else{
                                //add scores to the chat
                                final Net.HttpRequest httpRequest;
                                httpRequest = new Net.HttpRequest(Net.HttpMethods.PUT);
                                httpRequest.setUrl("https://api.parse.com/1/classes/chat/" + chatId);
                                screenControl.setChatId("");
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
                                        final Net.HttpRequest httpRequest;
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
                                        });
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
                        } catch (Exception e) {
                        }
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
            for(int i=-5;i<=5;i++){ //loop left and right 5 tiles each way on the map
                if((int)((fCharX / PPM) / nTileWidth)+i>0 &&
                        (int)((fCharX / PPM) / nTileWidth)+i<MapCol.getWidth()){ // makes sure to not go outside the map
                if (MapCol.getCell((int)((fCharX / PPM) / nTileWidth)+i,(int)((fCharY/PPM)/nTileHeight))//Collide on Left
                        .getTile().getProperties().containsKey("Hit")) { //grabs tiles that have hit key beside char for zooming out
                    bZoomOut = true;//if any tile has the hit key within 5 tiles left and right, the camera will zoom out
                    break; //leaves loop incase next tile does not have hit property
                }
            }
        }
        if(bZoomOut==true){//changes zoom in such a way that it will zoom out quickly then slowly until it stops
            camera.viewportWidth+=(nZoomWidth*1.1-camera.viewportWidth)/3;
            camera.viewportHeight+=(nZoomHeight*1.1-camera.viewportHeight)/3;
        }
        if(bZoomOut==false){//changes zoom in such a way that it will zoom back in quickly then slowly until it stops
            camera.viewportWidth+=((nScreenWidth*PPM/2)-camera.viewportWidth)/3;
            camera.viewportHeight+=((nScreenHeight*PPM/2)-camera.viewportHeight)/3;
        }
        //Arrow Stuff Now--

        if(touchpadArrow.isTouched() && ArrowTime>1){//Arrow time prevents rapid arrow shooting
            ArrowMove.set(-touchpadArrow.getKnobPercentX()*2,-touchpadArrow.getKnobPercentY()*2);//sets arrow velocity using touchpad
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

        if(ArrowTime<1|| bArrowShot==false){
            sChar.setPosition(fCharX-2,fCharY);//if the character's sprite is the arrow shooting one, we change it's position because it is smaller than the other sprites
        }

        if( nDir==2 && (bArrowShot==false||(bArrowShot==true && ArrowTime<1))){
            sChar.flip(true,false);//flips sprite to face either way since we only have one image facing one way
        }
        sChar.draw(batch);


        for(int i =0; i<arArrows.size; i++) {//loops through the array of arrows

            arrow = arArrows.get(i);
            ArrowX = arrow.getArrowX();
            ArrowY = arrow.getArrowY();
            ArrowVX = arrow.getArrowVX();
            ArrowVY = arrow.getArrowVY();
            ArrowVY -= 9.8 / 180f;//sets arrow gravity, we want them to fall pretty slow
            ArrowX += ArrowVX;
            ArrowY += ArrowVY;
            sArrow.setRotation((float) (Math.atan(ArrowVY / ArrowVX)) * MathUtils.radiansToDegrees);//rotates arrow based on velocity
            if (ArrowVX > 0) {
                sArrow.setRotation(sArrow.getRotation() + 180);//flips arrow if it's going to the right due to the images orientation
            }

            sArrow.setPosition(ArrowX, ArrowY);
            sArrow.draw(batch);
            arrow.setVars(ArrowVX, ArrowVY, ArrowX, ArrowY);

            if((int) ((ArrowX / PPM) / nTileWidth)>0 && (int)((ArrowX / PPM) / nTileWidth)<MapCol.getWidth()) {

                if (MapCol.getCell((int) ((ArrowX / PPM) / nTileWidth), (int) ((ArrowY / PPM) / nTileHeight))//Collide on Left
                        .getTile().getProperties().containsKey("Hit") ||
                        MapCol.getCell((int) ((sArrow.getWidth() + ArrowX) / PPM / nTileWidth), (int) ((ArrowY / PPM) / nTileHeight))//Collide on Right
                                .getTile().getProperties().containsKey("Hit")) {
                    //if there is a tile with the hit key to the left or right of the current arrow

                    if (MapCol.getCell((int) ((ArrowX / PPM) / nTileWidth) - 1, (int) ((ArrowY / PPM) / nTileHeight))//Collide on Left
                            .getTile().getProperties().containsKey("Hit") == false ||
                            MapCol.getCell((int) ((sArrow.getWidth() + ArrowX) / PPM / nTileWidth) + 1, (int) ((ArrowY / PPM) / nTileHeight))//Collide on Right
                                    .getTile().getProperties().containsKey("Hit") == false) {
                        //if there is a tile to the left of the arrow or to the right of the arrow


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
                                    PlatBody = platform.makePlat(ArrowVX, ArrowX, ArrowY, world);
                                    PlatBody = world.createBody(platform.PlatDef);

                                    arPlats.add(platform);// add new platform to the array
                                }
                            }
                        } else {//if the platform array length is non existant we just make one becuase there is no platfomr for it to overlap with or anything like that
                            platform = new Platform();
                            PlatBody = platform.makePlat(ArrowVX, ArrowX, ArrowY, world);
                            PlatBody = world.createBody(platform.PlatDef);

                            arPlats.add(platform);
                        }
                    }


                    arArrows.removeIndex(i);
                    i--;//we had an issue where arrows would flicker if one is deleted, so we do this to prevent that
                }
            }
        }
        death.setVars(MapCol, CharBody.getPosition());
        sDThing=death.getSprite(Time);
        sDThing.draw(batch);

        if(bZoomOut==false) {
            saw.setVars(nCharVX, fCharX, fCharY, MapCol, arTraps);
        }
        for(int i=0;i<arTraps.size;i++){
            sSaw.setPosition(arTraps.get(i).x-sSaw.getWidth()/2,arTraps.get(i).y-sSaw.getHeight()/2);
            sSaw.setRotation(sSaw.getRotation()-40);
            sSaw.draw(batch);
        }
        batch.end();
        for(int i=0;i<5;i++){//we step the world 5 times to speed it up so it doesn't look like it's going in slo mo
            world.step(1f/60f, 8, 3);//moves the box2d world
        }
        stage.draw();
    }
    @Override
    public void dispose(){//disposes stuff
        batch.dispose();
        world.dispose();
        stage.dispose();
        Map.dispose();
    }
    public void setScreenControl(ScreenControl screenControl_){
        screenControl = screenControl_;
    }
}


