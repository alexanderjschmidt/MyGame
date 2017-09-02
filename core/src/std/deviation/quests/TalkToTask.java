package std.deviation.quests;

import std.deviation.entities.types.Human;
import std.deviation.screen.Play;

public class TalkToTask extends QuestTask
{

    public TalkToTask(int targetID, String text, String[] messages)
    {
        super(targetID, text, messages);
    }

    @Override
    public boolean updateTarget(Play screen)
    {
        if (getTarget(screen).getClass().equals(Human.class))
        {
            Human h = (Human) getTarget(screen);
            return h.talking;
        }
        return false;
    }

}
