package std.deviation.quests;

import std.deviation.gameworld.Location;
import std.deviation.screen.Play;

public class EscortTask extends QuestTask
{

    public EscortTask(int targetID, String text, String[] messages, Location loc)
    {
        super(targetID, text, messages);
        location = loc;
    }

    private Location location;

    @Override
    public boolean updateTarget(Play screen)
    {
        return getTarget(screen).loc == location;
    }

}
