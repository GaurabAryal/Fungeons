package fungeons.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


import pablo127.almonds.FindCallback;
import pablo127.almonds.ParseException;
import pablo127.almonds.ParseObject;
import pablo127.almonds.ParseQuery;


public class GameRooms extends Game {
    Game game;
    SpriteBatch sbBatch;

    Skin skin;
    Stage stage;
    int nSHeight, nSWidth;
    List list;
    ScrollPane scrollPane;

    ScreenControl screenControl;
    public void render() {
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        sbBatch.begin();
        stage.draw();
        sbBatch.end();
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
    public void dispose(){

    }
    @Override
    public void create() {
        nSHeight = Gdx.graphics.getHeight();
        nSWidth = Gdx.graphics.getWidth();
        sbBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());
        ParseQuery query = new ParseQuery("gamerooms");
        query.findInBackground(new FindCallback() {

            @Override
            public void done(java.util.List<ParseObject> objects,
                             ParseException e) {
                if (e == null) {

                    for (ParseObject dealsObject : objects) {
                        System.out.println(dealsObject.get("map"));
                    }
                } else {
                }

            }
        });
    }
    public void setScreenControl(ScreenControl screenControl_){
        screenControl = screenControl_;
    }
}