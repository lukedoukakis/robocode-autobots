package baron_doukakis;

import robocode.*;
import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Robobot - a robot by (your name here)
 */
public class Robobot extends Robot
{
	
	public void run() { //go to corner at the beginning then do normal movement
	
		setColors(Color.blue,Color.blue,Color.red); // body,gun,radar
	
		setAdjustRadarForGunTurn(false);
		alignGuntoDirection(getRadarHeading());
		if(getX() != getBattleFieldWidth()){
			alignRobottoDirection(270);
			ahead(getBattleFieldWidth()-getX());
		}
		if(getY() != getBattleFieldHeight()){
			alignRobottoDirection(180);
			ahead(getBattleFieldHeight()-getY());
		}
	
		while(true){
			navigateSides();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */	

	public void onScannedRobot(ScannedRobotEvent e) { //shoot with power depending on distance from the enemy
		
		if(e.getDistance() < 100){
			fire(Rules.MAX_BULLET_POWER);
		}
		
		if(e.getDistance() >= 100 && e.getDistance() <= 200){
			fire((Rules.MAX_BULLET_POWER)/2);
		}
		if(e.getDistance() > 200){
			fire(1);
		}	
	}
	

	public void navigateSides(){ //moves around the edges
		double x = getBattleFieldWidth();
		double y = getBattleFieldHeight();
		
		turnRight(90);
		alignGuntoDirection(getHeading()+90);
		
		if(Math.abs(getX()-x) < 10){
			ahead(y);
		}
		if(Math.abs(getX()-x) >= 10){
			ahead(x);
		}
	}
	
	public void alignGuntoDirection(double direction){ //sets the heading of the gun to a direction
		double var = direction-getGunHeading();
		if(var >= 180){
			turnGunLeft(var);
		}
		if(var >= 0 && var < 180){
			turnGunRight(var);
		}
		if(var<=-180){
			turnGunRight(360-getGunHeading()+direction);
		}
		if(var<0 && var > -180){
			turnGunLeft(Math.abs(var));
		}
	}
	
	public void alignRobottoDirection(double direction){ //sets the heading of the robot to a direction
		double var = direction-getHeading();
		if(var >= 180){
			turnLeft(var);
		}
		if(var >= 0 && var < 180){
			turnRight(var);
		}
		if(var<=-180){
			turnRight(360-getGunHeading()+direction);
		}
		if(var<0 && var > -180){
			turnLeft(Math.abs(var));
		}
	}

}