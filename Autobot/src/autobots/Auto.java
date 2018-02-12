package autobots;

import robocode.*;
import robocode.util.Utils;

public class Auto extends Robot {

    private static final double radarSpeed = 7.5;

    private static int shortestTurnRoute(final double current, final double target) {

	if (current > target) {
	    return -1;
	} else if (target > current) {
	    return 1;
	} else {
	    return 0;
	}
    }

    private TargetDetails target;

    private boolean trackingFlag = false;

    private boolean turnRadarLeftFlag = false;

    /**
     * Returns whether if the current gun heading is within 1 degree of the target
     */
    private boolean closeEnough(final double currentHeading, final double deviation) {
	return Math.abs(Utils.normalAbsoluteAngleDegrees(currentHeading) - getGunHeading()) <= deviation;
    }

    private void init() {
	setAdjustGunForRobotTurn(true);
	setAdjustRadarForGunTurn(true);
	setAdjustRadarForRobotTurn(true);
    }
	
    public void navigate(double x, double y){ //movement, prefers toward center
			
	double heading;
	heading=getHeading();
			
	Random r = new Random();

	if(getX()<x/2 && getY()<y/2){//if west and south
		turnRight(Math.abs(45-(heading+ r.nextInt(10)-5)));
	}
	if(getX()<x/2 && getY()>y/2){//if west and north
		turnRight(Math.abs(135-(heading+ r.nextInt(10)-5)));
	}
	if(getX()>x/2 && getY()>y/2){//if east and north
		turnLeft(Math.abs(225-(heading+ r.nextInt(10)-5)));
	}
	if(getX()>x/2 && getY()<y/2){//if east and south
		turnLeft(Math.abs(315-(heading+ r.nextInt(10)-5)));
	}
	ahead(200);
    }
	
	
    public void navigateSerpentine(){ //probably useless, but this is some basic serpentine movement
	for(int i=0;i<6;i++){
		ahead(10);
		turnRight(10);
	}
	for(int i=0;i<6;i++){
		ahead(10);
		turnLeft(10);
	}
}

    @Override
    public void onScannedRobot(final ScannedRobotEvent event) {
	target = new TargetDetails(event, this);
	trackingFlag = true;
    }

    @Override
    public void run() {
	init();

	do {
	    update();
	} while (getEnergy() > 0);
    }

    private void update() {

	if (trackingFlag) {
	    // Gets angle to target 0 to 360
	    double targetDir = target.getAbsoluteHeadingDegreesFrom(getX(), getY());
	    // Calc optimal speed
	    double gunSpeed = Math.min(Math.abs(getGunHeading() - targetDir), Rules.GUN_TURN_RATE);

	    out.println("Target Direction: " + targetDir + "\nGun turn speed: " + gunSpeed + "\n" + target.toString());

	    if (closeEnough(targetDir, 2.5) && (getGunHeat() == 0)) {
		fire(2);
	    }

	    turnGunRight(shortestTurnRoute(getGunHeading(), targetDir) * gunSpeed);

	}
	
	ahead(1);

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

}
