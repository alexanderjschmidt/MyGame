package std.deviation.entities.components.ai;

import com.badlogic.ashley.core.Component;

public class TalkingComponent implements Component {

	public String[] questions;
	public String[][] text;
	// -1 means end convo
	public String[][][] textGoto;

	public void loadData(String data) {
		String[] lines = data.split("`");

		questions = new String[lines.length];
		text = new String[lines.length][];
		textGoto = new String[lines.length][][];

		// Question:Answer1,Answer2,Answer3,...:1,2,3>4,2,3>x,y>...`
		for (int i = 0; i < lines.length; i++) {
			String[] parts = lines[i].split("%");
			questions[i] = parts[0];
			text[i] = parts[1].split(",");
			textGoto[i] = new String[text[i].length][];
			String[] goTo = parts[2].split(",");
			for (int j = 0; j < goTo.length; j++) {
				textGoto[i][j] = goTo[j].split("&");
			}
		}
	}

	public String[] getNext(int question, int selected) {
		return textGoto[question][selected];
	}

	// Question:Answer1,Answer2,Answer3,...:1,2,3>4,2,3>x,y>...`
	public String getData() {
		String data = "";

		for (int i = 0; i < questions.length; i++) {
			data += questions[i] + "%";
			for (int j = 0; j < text[i].length; j++) {
				data += text[i][j] + (j < text[i].length - 1 ? "," : "%");
			}
			for (int j = 0; j < textGoto[i].length; j++) {
				for (int k = 0; k < textGoto[i][j].length; k++) {
					data += textGoto[i][j][k] + (k < textGoto[i][j].length - 1 ? "&" : "");
				}
				data += j < textGoto[i].length - 1 ? "," : "";
			}
			data += i < questions.length - 1 ? "`" : "";
		}

		return data;
	}
}
