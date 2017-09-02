package std.deviation.hud;

import std.deviation.Application;
import std.deviation.managers.KeyManager;
import std.deviation.managers.ResourceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class MainMenu extends Menu
{

    private Label[] options;
    private int selected = 0;
    private Image cursor;

    private Application app;

    public static final String path = "Textures/UI/UI.png";

    public MainMenu(Application app)
    {
        super(-500, 0, 0, 0, 500, 720, ResourceManager.get().getTexture(path));
        this.app = app;
        this.setSize(500, 720);
        this.setPosition(-600, 0);

        TextureRegion cursorTex = ResourceManager.get().ui[0][0];
        cursor = new Image(cursorTex);
        cursor.scaleBy(-.5f);
        cursor.setPosition(20, 545);

        options = new Label[4];
        Label title = new Label("Main Menu", ResourceManager.get().skin, "title");
        title.setPosition(40, 600);
        options[0] = new Label("Options", ResourceManager.get().skin);
        options[0].setPosition(40, 550);
        options[1] = new Label("Save", ResourceManager.get().skin);
        options[1].setPosition(40, 500);
        options[2] = new Label("Load", ResourceManager.get().skin);
        options[2].setPosition(40, 450);
        options[3] = new Label("Exit", ResourceManager.get().skin);
        options[3].setPosition(40, 400);

        this.addActor(cursor);
        this.addActor(title);
        this.addActor(options[0]);
        this.addActor(options[1]);
        this.addActor(options[2]);
        this.addActor(options[3]);
        options[selected].addAction(Actions.moveBy(15, 0));
    }

    @Override
    public void handleInputs()
    {
        if (Gdx.input.isKeyJustPressed(KeyManager.DOWN) && selected < options.length - 1)
        {
            options[selected].addAction(Actions.moveBy(-15, 0, .5f, Interpolation.pow5Out));
            selected++;
            cursor.addAction(Actions.moveBy(0, -50, .5f, Interpolation.pow5Out));
            options[selected].addAction(Actions.moveBy(15, 0, .5f, Interpolation.pow5Out));
        }
        if (Gdx.input.isKeyJustPressed(KeyManager.UP) && selected > 0)
        {
            options[selected].addAction(Actions.moveBy(-15, 0, .5f, Interpolation.pow5Out));
            selected--;
            cursor.addAction(Actions.moveBy(0, 50, .5f, Interpolation.pow5Out));
            options[selected].addAction(Actions.moveBy(15, 0, .5f, Interpolation.pow5Out));
        }
        if (Gdx.input.isKeyJustPressed(KeyManager.SELECT))
        {
            switch (selected)
            {
                case 0:

                    break;
                case 1:
                    app.save();
                    break;
                case 2:
                    app.load();
                    break;
                case 3:
                    Gdx.app.exit();
                    break;
            }
        }
    }
}
