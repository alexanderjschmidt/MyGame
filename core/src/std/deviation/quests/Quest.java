package std.deviation.quests;

import std.deviation.managers.ResourceManager;
import std.deviation.screen.Play;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

/*
 * quest types:
 * kill, delivery, escort, talk to
 */
public class Quest {

	private int currentTask;
	private boolean active = false;
	private boolean completed = false;
	protected QuestTask[] tasks;
	private String name;
	private String questText;

	private Quest(String name, String questText, QuestTask[] tasks) {
		this.name = name;
		this.questText = questText;
		this.tasks = tasks;
	}

	public void load(boolean active, int currentTask) {
		this.active = active;
		this.currentTask = currentTask;
	}

	public void update(Play screen) {
		if (!tasks[currentTask].updateTarget(screen)) {
			return;
		}
		tasks[currentTask].complete(screen);
		currentTask++;
		if (currentTask >= tasks.length) {
			active = false;
			completed = true;
		}
	}

	public boolean isActive() {
		return active;
	}

	public boolean isComplete() {
		return completed;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getText() {
		return questText;
	}

	public VerticalGroup getLabels(Label qText) {
		VerticalGroup list = new VerticalGroup();
		list.addActor(qText);
		for (int i = 0; i <= currentTask && i < tasks.length; i++) {
			Label l = new Label(tasks[i].getText(), ResourceManager.get().skin);
			if (tasks[i].isComplete()) {
				l.setColor(Color.LIGHT_GRAY);
			}
			list.addActor(l);
		}
		return list;
	}

	public void activate() {
		active = true;
	}
}
