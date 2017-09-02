package std.deviation.gameworld;

import std.deviation.Application;
import std.deviation.managers.ResourceManager;

import com.badlogic.gdx.graphics.Texture;

public class Fog
{

    public int width, height;

    public static final String path = "Textures/Fog.png";

    public Fog()
    {
        width = getTex().getWidth();
        height = getTex().getHeight();
    }

    public void render(Application app)
    {
        int x0 = (int) ((app.getCamera().position.x - (app.getCamera().viewportWidth / 2)) / width) - 1;
        int y0 = (int) ((app.getCamera().position.y - (app.getCamera().viewportHeight / 2)) / height) - 1;
        int x1 = (int) ((app.getCamera().position.x + (app.getCamera().viewportWidth / 2)) / width) + 1;
        int y1 = (int) ((app.getCamera().position.y + (app.getCamera().viewportHeight / 2)) / width) + 1;

        for (int x = x0; x <= x1; x++)
        {
            for (int y = y0; y <= y1; y++)
            {
                app.getBatch().draw(getTex(), x * width, y * height);
            }
        }
    }

    private Texture getTex()
    {
        return ResourceManager.get().getTexture(path);
    }

}
