package std.deviation.quests;

import std.deviation.screen.Play;

import com.badlogic.ashley.core.Entity;

public abstract class QuestTask {

	private int targetID;
	private String text;

	private String[] messages;

	public abstract boolean updateTarget(Play screen);

	private boolean complete = false;

	public QuestTask(int targetID, String text, String[] messages) {
		this.targetID = targetID;
		this.text = text;
		this.messages = messages;
	}

	public void complete(Play screen) {
		System.out.println(text + " complete");
		complete = true;
		for (String command : messages)
			screen.manager.handleMessage(command);
	}

	public Entity getTarget(Play screen) {
		return screen.getController().getEntity(targetID);
	}

	public String getText() {
		return text;
	}

	public boolean isComplete() {
		return complete;
	}
}
