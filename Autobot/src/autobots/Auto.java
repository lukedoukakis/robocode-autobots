package autobots;

import robocode.Robot;

public class Auto extends Robot {
	@Override
	public void run() {
		init();
		while (true) {
			update();
		}
	}

	private void update() {
		fire(10);
		turnLeft(1);
	}

	private void init() {
		System.out.println(this.getTime());
	}
}
