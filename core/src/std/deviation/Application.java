package std.deviation;

import std.deviation.managers.ResourceManager;
import std.deviation.screen.LoadingScreen;
import std.deviation.screen.Play;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Application extends Game
{
    public static final String TITLE = "Game";
    public static final float VERSION = .8f;

    private Camera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    private String currentLoad;

    private String load = null;
    private String save = null;

    @Override
    public void create()
    {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new Camera();
        viewport = new StretchViewport(w, h, new OrthographicCamera());
        getCamera().setToOrtho(false, w, h);
        viewport.apply();
        batch = new SpriteBatch();

        setScreen(new LoadingScreen(this));
    }

    public void load()
    {
        Gdx.input.getTextInput(new TextInputListener()
        {

            @Override
            public void input(String str)
            {
                load = str;
            }

            @Override
            public void canceled()
            {
            }

        }, "Load Name", currentLoad, "Input name of file to Load name");
    }

    private void load2()
    {
        currentLoad = load;
        Gdx.files.local("bin/savs/" + load).copyTo(Gdx.files.local("bin/current"));
        setScreen(new Play(this));
        load = null;
    }

    public void newGame()
    {
        currentLoad = "NewGame";
        Gdx.files.local("bin/NewGame").copyTo(Gdx.files.local("bin/current"));
        setScreen(new Play(this));
    }

    public void save()
    {
        Gdx.input.getTextInput(new TextInputListener()
        {

            @Override
            public void input(String str)
            {
                save = str;
            }

            @Override
            public void canceled()
            {
            }

        }, "Save Name", currentLoad, "Input new save's name");
    }

    private void save2()
    {
        currentLoad = save;
        Gdx.files.local("bin/current").copyTo(Gdx.files.local("bin/savs/" + save));
        save = null;
    }

    @Override
    public void dispose()
    {
        super.dispose();
        ResourceManager.get().dispose();
    }

    @Override
    public void render()
    {
        super.render();
        if (load != null)
            load2();
        if (save != null)
            save2();
    }

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
        viewport.update(width, height);
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    public Camera getCamera()
    {
        return camera;
    }

    public SpriteBatch getBatch()
    {
        return batch;
    }

}
