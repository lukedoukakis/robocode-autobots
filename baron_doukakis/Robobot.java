package baron_doukakis;

import robocode.*;
import robocode.util.Utils;

//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Robobot - a robot by (your name here)
 */
public class Robobot extends Robot
{
	
	public void run() {
	
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
			navigateWalls();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */	

	public void onScannedRobot(ScannedRobotEvent e) {
		
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
	
	 public void navigate() { // movement, tends to avoid the edges of the battlefield

		final double x = getBattleFieldWidth();
		final double y = getBattleFieldHeight();
		final double heading = getHeading();
		final double midX = x / 2;
		final double midY = y / 2;
		final double randHeading = (heading + (Math.random() * 10)) - 5; // The randomized part of the heading
		double var = 0.00;
		
		if ((getX() < midX) && (getY() < midY)) {// if west and south
	   		var = Math.abs(45 - randHeading);
			turnRight(var);
			
		}
		if ((getX() < midX) && (getY() > midY)) {// if west and north
			var = Math.abs(135 - randHeading);    	
			turnRight(var);
			
		}
		if ((getX() > midX) && (getY() > midY)) {// if east and north
	    	var = Math.abs(225 - randHeading);
			turnLeft(var);
			
			
		}
		if ((getX() > midX) && (getY() < midY)) {// if east and south
	    	var = Math.abs(315 - randHeading);
			turnLeft(var);
			
			
		}
		ahead(200);
    }
	

	public void navigateWalls(){
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
		
				

	
	
	public void scan(){
		for(int i = 0; i < 10; i++){
			turnRadarLeft(36);
		}	
	}
	
	public void alignGuntoDirection(double direction){
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
	
	public void alignRobottoDirection(double direction){
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
