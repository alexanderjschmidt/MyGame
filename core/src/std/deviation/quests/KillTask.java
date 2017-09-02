package std.deviation.quests;

import com.badlogic.ashley.core.Entity;

import std.deviation.screen.Play;

public class KillTask extends QuestTask {

	public KillTask(int targetID, String text, String[] messages) {
		super(targetID, text, messages);
	}

	@Override
	public boolean updateTarget(Play screen) {
		Entity e = getTarget(screen);
		if (e instanceof Human) {
			return ((Human) e).dead || e.present;
		}
		return getTarget(screen).present;
	}
}
