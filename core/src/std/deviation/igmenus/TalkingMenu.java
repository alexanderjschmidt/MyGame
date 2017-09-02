package std.deviation.igmenus;

import std.deviation.Application;
import std.deviation.entities.types.Entity;
import std.deviation.entities.types.Human;
import std.deviation.managers.KeyManager;
import std.deviation.managers.ResourceManager;
import std.deviation.screen.Play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class TalkingMenu extends Menu
{

    private Human e2;

    private int selected;
    private int questionNum;
    private int length;

    private Label[] answers;
    private Label question;

    private Image character;
    private Stage stage;

    private boolean wait = false;

    private Application app;
    private Play screen;

    public TalkingMenu(Application app, Play screen, Stage stage)
    {
        this.app = app;
        this.screen = screen;
        this.stage = stage;
        this.setSize(1280, 250);
        this.setPosition(0, -250);

        Image image = new Image(ResourceManager.get().textUI);
        image.setFillParent(true);

        TextureRegion cursorTex = ResourceManager.get().ui[0][0];
        Image cursor = new Image(cursorTex);
        cursor.scaleBy(-.5f);
        cursor.setPosition(900, 95);

        answers = new Label[3];
        questionNum = 0;

        this.addActor(image);
        this.addActor(cursor);

    }

    private void update()
    {
        removeActor(question);
        question = new Label(e2.talk.questions[questionNum], ResourceManager.get().skin);
        question.setPosition(500, 100);
        question.setWrap(true);
        question.setWidth(350);
        addActor(question);

        removeActor(answers[0]);
        if (selected > 0)
        {
            answers[0] = new Label(e2.talk.text[questionNum][selected - 1], ResourceManager.get().skin);
            answers[0].addAction(Actions.alpha(.5f));
            answers[0].setPosition(950, 150);
            addActor(answers[0]);
        }

        removeActor(answers[1]);
        answers[1] = new Label(e2.talk.text[questionNum][selected], ResourceManager.get().skin);
        answers[1].setPosition(950, 100);
        addActor(answers[1]);

        removeActor(answers[2]);
        if (selected < length)
        {
            answers[2] = new Label(e2.talk.text[questionNum][selected + 1], ResourceManager.get().skin);
            answers[2].addAction(Actions.alpha(.5f));
            answers[2].setPosition(950, 50);
            addActor(answers[2]);
        }
    }

    @Override
    public void update(Entity e)
    {
        if (!e.getClass().equals(Human.class))
            return;
        e2 = (Human) e;
        wait = true;
        questionNum = 0;
        selected = 0;
        length = e2.talk.text[questionNum].length - 1;
        e2.talking = true;
    }

    @Override
    public void open()
    {
        app.getScreen().pause();
        this.setPosition(0, -250);
        addAction(Actions.moveBy(0, 250, 1f, Interpolation.pow5Out));
        menuOpen = true;
        if (character != null)
            character.remove();
        character = new Image(e2.getFace());
        character.setPosition(-500, 0);
        character.addAction(Actions.moveBy(450, 0, 1f, Interpolation.pow5Out));
        stage.addActor(character);
        update();
    }

    @Override
    public void close()
    {
        e2.talking = false;
        app.getScreen().resume();
        addAction(Actions.sequence(Actions.moveBy(0, -250, 1f, Interpolation.pow5Out), Actions.run(new Runnable()
        {
            @Override
            public void run()
            {
                menuOpen = false;
            }
        })));
        character.addAction(Actions.moveBy(-450, 0, 1f, Interpolation.pow5Out));
    }

    @Override
    public void handleInputs()
    {
        if (Gdx.input.isKeyJustPressed(KeyManager.ESCAPE))
        {
            close();
        }
        if (Gdx.input.isKeyJustPressed(KeyManager.UP) && selected > 0 && !wait)
        {
            selected--;
            answers[0].addAction(Actions.parallel(Actions.moveBy(0, -50, .5f, Interpolation.pow5Out), Actions.alpha(1f, .5f, Interpolation.pow5Out)));
            answers[1].addAction(Actions.parallel(Actions.moveBy(0, -50, .5f, Interpolation.pow5Out), Actions.alpha(.5f, .5f, Interpolation.pow5Out)));
            if (answers[2] != null)
                answers[2].addAction(Actions.sequence(Actions.fadeOut(.5f), Actions.run(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        update();
                    }

                })));

            answers[2] = answers[1];
            answers[1] = answers[0];

            answers[0] = null;
            if (selected > 0)
            {
                answers[0] = new Label(e2.talk.text[questionNum][selected - 1], ResourceManager.get().skin);
                answers[0].addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(.5f, .5f, Interpolation.pow5Out)));
                answers[0].setPosition(950, 150);
                addActor(answers[0]);
            }
        }
        if (Gdx.input.isKeyJustPressed(KeyManager.DOWN) && selected < length && !wait)
        {
            selected++;
            if (answers[0] != null)
                answers[0].addAction(Actions.sequence(Actions.fadeOut(.5f), Actions.run(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        update();
                    }

                })));
            answers[1].addAction(Actions.parallel(Actions.moveBy(0, 50, .5f, Interpolation.pow5Out), Actions.alpha(.5f, .5f, Interpolation.pow5Out)));
            answers[2].addAction(Actions.parallel(Actions.moveBy(0, 50, .5f, Interpolation.pow5Out), Actions.alpha(1f, .5f, Interpolation.pow5Out)));

            answers[0] = answers[1];
            answers[1] = answers[2];

            answers[2] = null;
            if (selected < length)
            {
                answers[2] = new Label(e2.talk.text[questionNum][selected + 1], ResourceManager.get().skin);
                answers[2].addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(.5f, .5f, Interpolation.pow5Out)));
                answers[2].setPosition(950, 50);
                addActor(answers[2]);
            }
        }
        if (Gdx.input.isKeyJustPressed(KeyManager.SELECT) && !wait)
        {
            String[] events = e2.talk.getNext(questionNum, selected);
            for (int i = 1; i < events.length; i++)
            {
                handleEvents(events[i]);
            }
            try
            {
                questionNum = Integer.parseInt(events[0]);
            }
            catch (Exception error)
            {
                handleEvents(events[0]);
                return;
            }

            selected = 0;
            length = e2.talk.text[questionNum].length - 1;
            update();
        }
        wait = false;

    }

    private void handleEvents(String message)
    {
        // c-close, t-trade, s-sale
        switch (message)
        {
            case "c":
                close();
                break;
            default:
                screen.manager.handleMessage(message);
                break;
        }
    }
}
