package autobots;

import java.awt.*;

import robocode.*;
import robocode.Robot;

public class Auto extends Robot {

    private double targetDir;
    private boolean trackingFlag = false;

    private boolean turnGunLeftFlag = false, turnRadarLeftFlag = false;

    @Override
    public void run() {
	init();
	    //getEnergy >0
	while (true) {
	    update();
	}
    }

    private void update() {

	if (trackingFlag) {
	    if (closeEnough()) {
		fire(3);
	    }
	    if (targetDir < getGunHeading()) {
		turnGunLeft(1);
	    } else if (targetDir > getGunHeading()) {
		turnGunRight(1);
	    }
	} else {
	    //TODO: SERPENTINE!
		ahead(1);
//Maybe we should not have the gun idly rotate
	    if (this.getGunHeading() >= 359) {
		turnGunLeftFlag = true;
	    }
	    if (this.getGunHeading() <= 0) {
		turnGunLeftFlag = false;
	    }
	    if (!turnGunLeftFlag) {
		turnGunRight(5);
	    } else {
		turnGunLeft(5);
	    }

	    if (this.getRadarHeading() >= 359) {
		turnRadarLeftFlag = true;
	    }
	    if (this.getRadarHeading() <= 0) {
		turnRadarLeftFlag = false;
	    }
	    if (!turnRadarLeftFlag) {
		turnRadarRight(5);
	    } else {
		turnRadarLeft(5);
	    }
	}
    }

    private void init() {
	setAdjustGunForRobotTurn(true);
	setAdjustRadarForGunTurn(true);
	setAdjustRadarForRobotTurn(true);
    }
/** Returns whether if the current gun heading is within 1 degree of the target
*/
    private boolean closeEnough(double deviation) {
	return Math.abs(targetDir - this.getGunHeading()) <= 1;
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
	//Use event.getHeading()
	    targetDir = getHeading() + event.getBearing();
	targetDir = fixToHeading(targetDir);
	trackingFlag = true;
    }
/** Returns a value between 0 (inclusive) and 360 (exclusive)
*/
    public static double fixToHeading(double val) {
	double toRet = val;
	while (toRet >= 360) {
	    toRet -= 360;
	}
	while (toRet < 0) {
	    toRet += 360;
	}
	return toRet;
    }

    @SuppressWarnings("boxing")
    @Override
    public void onPaint(Graphics2D g) {
	g.setColor(Color.red);
	g.drawString(String.format("%.2f", targetDir), (int) this.getX(), (int) this.getY() + 10);
	g.drawString(String.format("%.2f", getGunHeading()), (int) this.getX(), (int) this.getY() + 20);
    }
}
