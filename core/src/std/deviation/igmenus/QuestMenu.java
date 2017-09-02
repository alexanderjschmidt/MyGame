package std.deviation.igmenus;

import java.util.ArrayList;

import std.deviation.Application;
import std.deviation.entities.types.Entity;
import std.deviation.managers.KeyManager;
import std.deviation.managers.ResourceManager;
import std.deviation.quests.Quest;
import std.deviation.screen.Play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

public class QuestMenu extends Menu
{

    private Image cursor;
    private ArrayList<Quest> quests;
    private ScrollPane scroll;
    private int selected = 0;

    private Application app;
    private Play screen;

    private SelectedQuest selectedQuest;

    public QuestMenu(Application app, Play screen, Stage stage)
    {
        this.app = app;
        this.screen = screen;

        selectedQuest = new SelectedQuest();
        stage.addActor(selectedQuest);

        this.setSize(600, 720);
        this.setPosition(-600, 0);

        Image image = new Image(ResourceManager.get().mainUI);
        image.setFillParent(true);

        TextureRegion cursorTex = ResourceManager.get().ui[0][0];
        cursor = new Image(cursorTex);
        cursor.scaleBy(-.5f);
        cursor.setPosition(20, 545);

        Label title = new Label("Quest Menu", ResourceManager.get().skin, "title");
        title.setPosition(40, 600);

        this.addActor(image);
        this.addActor(cursor);
        this.addActor(title);
    }

    @Override
    public void handleInputs()
    {
        if (Gdx.input.isKeyJustPressed(KeyManager.DOWN) && selected < quests.size() - 1)
        {
            selected++;
            selectedQuest.update(quests.get(selected));
        }
        if (Gdx.input.isKeyJustPressed(KeyManager.UP) && selected > 0)
        {
            selected--;
            selectedQuest.update(quests.get(selected));
        }
    }

    @Override
    public void update(Entity e)
    {
    }

    @Override
    public void open()
    {
        menuOpen = true;
        app.getScreen().pause();

        quests = screen.manager.getQuests();
        VerticalGroup list = new VerticalGroup();
        for (Quest q : quests)
        {
            Label l = new Label(q + "", ResourceManager.get().skin);
            if (q.isComplete())
                l.setColor(Color.LIGHT_GRAY);
            list.addActor(l);
        }

        list.left();
        removeActor(scroll);
        scroll = new ScrollPane(list, ResourceManager.get().skin);
        scroll.setSize(510, 520);
        scroll.setPosition(100, 50);
        addActor(scroll);

        this.setPosition(-600, 0);
        addAction(Actions.moveBy(600, 0, 1f, Interpolation.pow5Out));
        selectedQuest.setPosition(1280, 0);
        selectedQuest.addAction(Actions.moveBy(-600, 0, 1f, Interpolation.pow5Out));
        selectedQuest.update(quests.get(0));
    }

    @Override
    public void close()
    {
        app.getScreen().resume();
        addAction(Actions.sequence(Actions.moveBy(-600, 0, 1f, Interpolation.pow5Out),
                Actions.run(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        menuOpen = false;
                    }
                })));
        selectedQuest.addAction(Actions.moveBy(600, 0, 1f, Interpolation.pow5Out));
    }

    private class SelectedQuest extends Group
    {
        private ScrollPane scroll;

        public SelectedQuest()
        {
            this.setSize(600, 720);
            this.setPosition(1280, 0);
            Image image = new Image(ResourceManager.get().invUI);
            image.setFillParent(true);
            this.addActor(image);
        }

        public void update(Quest q)
        {
            this.clearChildren();
            if (q == null)
                return;
            Label title = new Label(q.toString(), ResourceManager.get().skin, "title");
            title.setPosition(40, 600);
            Label qText = new Label(q.getText(), ResourceManager.get().skin);
            qText.setWrap(true);
            qText.setPosition(40, 600);
            VerticalGroup list = q.getLabels(qText);
            list.left();
            scroll = new ScrollPane(list, ResourceManager.get().skin);
            scroll.setSize(510, 520);
            scroll.setPosition(140, 50);

            this.addActor(title);

            this.addActor(scroll);
        }
    }
}
