package baron_doukakis;

import robocode.*;
import robocode.util.Utils;
import java.awt.Color;

//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Robobot - a robot by (your name here)
 */
public class Robobot extends Robot
{
	
	public void run() {
	
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
	

	public void navigateSides(){
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
