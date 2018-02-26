package autobots;

import java.util.*;

import robocode.*;
import robocode.util.Utils;

public class Auto extends Robot {

    private static final double radarSpeed = 7.5;

    private static final double wobbleIncrement = 5;

    private static int shortestTurnRoute(final double current, final double target) {

	if (current > target) {
	    return -1;
	} else if (target > current) {
	    return 1;
	} else {
	    return 0;
	}
    }

    // Tracks 5-7 targets
    private ArrayList<TargetDetails> target = new ArrayList<>();

    private boolean turnRadarLeftFlag = false;
    private double lastWobbleTarget, wobbleExtreme = wobbleIncrement;
    private byte lastWobbleDir = 1;

    /**
     * Returns whether if the current gun heading is within 1 degree of the target
     */
    private boolean closeEnough(final double currentHeading, final double deviation) {
	return Math.abs(Utils.normalAbsoluteAngleDegrees(currentHeading) - getGunHeading()) <= deviation;
    }

    /**
     *
     * @param useAngle
     *            Whether to return the closest target by angle
     * @return The closest target to the robot's current postion
     */
    private TargetDetails getClosestTarget(final boolean useAngle) {
	int indexClosestDist = -1, indexClosestAngle = -1;
	double closestDistSquared = Double.MAX_VALUE;
	double closestAngle = Double.MAX_VALUE;

	for (int i = 0; i < target.size(); i++) {
	    final TargetDetails current = target.get(i);
	    final double distSquared = current.getSqrDistance(getX(), getY());
	    double angle = current.getAbsoluteHeadingDegreesFrom(getX(), getY()) - getGunHeading();
	    angle = Math.abs(angle);

	    if (distSquared < closestDistSquared) {
		indexClosestDist = i;
		closestDistSquared = distSquared;
	    }

	    if (angle < closestAngle) {
		indexClosestAngle = i;
		closestAngle = angle;
	    }
	}

	if (useAngle) {
	    return target.get(indexClosestAngle);
	}

	return target.get(indexClosestDist);
    }

    private void init() {
	setAdjustGunForRobotTurn(true);
	setAdjustRadarForGunTurn(true);
	setAdjustRadarForRobotTurn(true);
    }

    public void navigate() { // movement, tends to avoid the edges of the battlefield

	final double x = getBattleFieldWidth();
	final double y = getBattleFieldHeight();
	final double heading = getHeading();

	// Secondary variables.
	final double midX = x / 2;
	final double midY = y / 2;
	final double randHeading = (heading + (Math.random() * 10)) - 5; // The randomized part of the heading

	if ((getX() < midX) && (getY() < midY)) {// if west and south
	    turnRight(Math.abs(45 - randHeading));
	}
	if ((getX() < midX) && (getY() > midY)) {// if west and north
	    turnRight(Math.abs(135 - randHeading));
	}
	if ((getX() > midX) && (getY() > midY)) {// if east and north
	    turnLeft(Math.abs(225 - randHeading));
	}
	if ((getX() > midX) && (getY() < midY)) {// if east and south
	    turnLeft(Math.abs(315 - randHeading));
	}
	ahead(200);
    }

    public void navigateSerpentine() { // probably useless, but this is some basic serpentine movement
	for (int i = 0; i < 6; i++) {
	    ahead(10);
	    turnRight(10);
	}
	for (int i = 0; i < 6; i++) {
	    ahead(10);
	    turnLeft(10);
	}
    }

    @Override
    public void onScannedRobot(final ScannedRobotEvent event) {
	target.add(new TargetDetails(event, this));
    }

    @Override
    public void run() {
	init();

	do {
	    update();
	} while (getEnergy() > 0);
    }

    private void update() {

	if (!target.isEmpty()) {

	    TargetDetails closest = getClosestTarget(true);

	    // Gets angle to target 0 to 360
	    final double targetDir = closest.getAbsoluteHeadingDegreesFrom(getX(), getY());
	    // Calc optimal speed
	    final double gunSpeed = Math.min(Math.abs(getGunHeading() - targetDir), Rules.GUN_TURN_RATE);

	    out.println("Target Direction: " + targetDir + "\nGun turn speed: " + gunSpeed + "\n" + target.toString());

	    if (closeEnough(targetDir, 2.5) && (getGunHeat() == 0)) {
		fire(2);
	    }

	    turnGunRight(shortestTurnRoute(getGunHeading(), targetDir) * gunSpeed);
	    wobbleRadar(targetDir);
	}
	// Replace the following line with the method for movement
	ahead(1);

	// Set radar to a wobbly path (recenter on every scan)
	if (getRadarHeading() >= 359) {
	    turnRadarLeftFlag = true;
	}
	if (getRadarHeading() <= 0) {
	    turnRadarLeftFlag = false;
	}
	if (!turnRadarLeftFlag) {
	    turnRadarRight(radarSpeed);
	} else {
	    turnRadarLeft(radarSpeed);
	}

    }

    /**
     *
     * @param targetDir
     */
    private void wobbleRadar(final double targetDir) {
	if (targetDir != lastWobbleTarget) {
	    turnRadarRight(shortestTurnRoute(getRadarHeading(), targetDir) * radarSpeed);
	    lastWobbleTarget = targetDir;
	    return;
	}

	double posDev = targetDir + (wobbleExtreme * lastWobbleDir);
	double currentRadar = getRadarHeading();

	if ((lastWobbleDir == 1) && (posDev > currentRadar)) {
	    turnRadarRight(posDev - currentRadar);
	    return;
	} else if ((lastWobbleDir == -1) && (posDev < currentRadar)) {
	    turnRadarRight(posDev - currentRadar);
	    return;
	}

	if (currentRadar == posDev) {
	    wobbleExtreme += wobbleIncrement;

	    if (lastWobbleDir < 0) {
		lastWobbleDir = 1;
	    } else if (lastWobbleDir > 0) {
		lastWobbleDir = -1;
	    }
	}

    }

}
