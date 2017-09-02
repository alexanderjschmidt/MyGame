package std.deviation.desktop;

import std.deviation.Application;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "RPGame";
        config.width = 1280;
        config.height = 720;
        new LwjglApplication(new Application(), config);
    }
}
