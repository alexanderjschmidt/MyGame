package std.deviation.utils;

public class WorldClock {
	private float current;
	// Ratio should be 2.5
	private static final float RATIO = .02f;
	private int minute = 0, hour = 6, day = 1, month = 0, year = 1200;
	private static final String[] MONTHS = { "January", "Febuary", "March",
		"April", "May", "June", "July", "August", "September", "October",
		"November", "December" };

	public void update(float delta) {
		current += delta;
		if (current >= RATIO) {
			current -= RATIO;
			minute++;
		}
		if (minute == 60) {
			minute = 0;
			hour++;
		}
		if (hour == 25) {
			hour = 1;
			day++;
		}
		if (day == 31) {
			day = 1;
			month++;
		}
		if (month == 12) {
			month = 0;
			year++;
		}
	}

	public static float totalSeconds() {
		return RATIO * 60 * 24;
	}

	@Override
	public String toString() {
		return hour + ":" + (minute < 10 ? "0" : "") + minute + " "
				+ MONTHS[month] + " " + day + ", " + year;
	}

}
