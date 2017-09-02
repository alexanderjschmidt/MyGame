package std.deviation.quests;

import std.deviation.entities.types.Chest;
import std.deviation.items.Item;
import std.deviation.screen.Play;

public class DeliverTask extends QuestTask
{

    private Item item;
    private int numOfItem;

    public DeliverTask(int targetID, String text, String[] messages, Item item, int numOfItem)
    {
        super(targetID, text, messages);
        this.item = item;
        this.numOfItem = numOfItem;
    }

    @Override
    public boolean updateTarget(Play screen)
    {
        if (getTarget(screen) instanceof Chest)
        {
            Chest c = (Chest) getTarget(screen);
            return c.getInv().contains(item, numOfItem);
        }
        return false;
    }

}
