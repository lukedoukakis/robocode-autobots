package autobots;

import robocode.*;
import robocode.util.Utils;

public final class TargetDetails {

    // This class uses radians
    private double head, dist, facing, velocity; // Received from event object

    private double x, y;// Calculated w/ prediction

    public TargetDetails(final ScannedRobotEvent event, final Robot scanner) {

	head = Utils.normalAbsoluteAngle(event.getBearingRadians() + Math.toRadians(scanner.getHeading()));
	dist = event.getDistance();

	facing = event.getHeadingRadians();
	velocity = event.getVelocity();

	calcPosition(scanner.getX(), scanner.getY());

    }

    private void calcPosition(final double dx, final double dy) {

	// Calculate x and y with prediction
	x = dx + (dist * Math.sin(head)) + (velocity * Math.sin(facing));
	y = dy + (dist * Math.cos(head)) + (velocity * Math.cos(facing));
    }

    public double getAbsoluteHeadingDegreesFrom(final double x1, final double y1) {
	return Utils.normalAbsoluteAngleDegrees(Math.toDegrees(getHeadingFrom(x1, y1)));
    }

    public double getDistance() {
	return dist;
    }

    public double getFacingDegrees() {
	return Math.toDegrees(facing);
    }

    public double getHeadingDegrees() {
	return Math.toDegrees(head);
    }

    public double getHeadingDegreesFrom(final double x1, final double y1) {

	return Math.toDegrees(getHeadingFrom(x1, y1));
    }

    /**
     *
     * @param x1
     *            Current x location of shooter
     * @param y1
     *            Current y location of shooter
     * @return A angle in radians
     */
    public double getHeadingFrom(final double x1, final double y1) {
	return Math.atan2(x - x1, y - y1);
    }

    /**
     *
     * @return The square of the distance for speed reasons
     */
    public double getSqrDistance(final double x1, final double y1) {
	return ((x - x1) * (x - x1)) + ((y - y1) * (y - y1));
    }

    public double getVelocity() {
	return velocity;
    }

    @SuppressWarnings("boxing")
    @Override
    public String toString() {
	return "Target Details: \n\tHeading: " + String.format("%.2f", getHeadingDegrees()) + "\n\tDistance: "
		+ String.format("%.2f", dist);
    }
}
