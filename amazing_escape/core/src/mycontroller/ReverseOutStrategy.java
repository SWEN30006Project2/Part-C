package mycontroller;

import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.World;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;
import world.WorldSpatial.Direction;

public class ReverseOutStrategy implements TurningStrategyAdapter {

	private static WorldSpatial.Direction target;
	private static Boolean isReady;
	private static Boolean isTurning;
	private static Boolean isReversing;
	private static Boolean isFirst;
	private static int counter;

	@Override
	public void applyTurning(MyAIController AI, float delta) {

		if (!AI.strategyLock) {
			target = null;
			isReady = false;
			isTurning = false;
			isReversing = null;
			counter = 0;
			isFirst = true;
		}
		AI.checkStateChange();
		HashMap<Coordinate, MapTile> currentView = AI.getView();

		if (isFirst) {
			target = AI.getOrientation();
			isFirst = false;
		}

		switch (target) {
		case NORTH:
			if (!AI.isTurningLeft && !AI.isTurningRight && isReady == false && AI.checkEast(currentView, 1)) {

				AI.applyReverseAcceleration();
				AI.lastTurnDirection = WorldSpatial.RelativeDirection.RIGHT;
				AI.strategyLock = true;
				isReversing = true;

			} else {
				// switch (AI.getOrientation()) {
				// case NORTH:
				if (!AI.checkEast(currentView, 1) && !AI.getOrientation().equals(WorldSpatial.Direction.EAST)) {

					AI.applyRightTurn(AI.getOrientation(), delta);
					AI.isTurningRight = true;
					isTurning = true;

				} else if (AI.getOrientation().equals(WorldSpatial.Direction.EAST)) {
					AI.isTurningRight = false;
					isTurning = false;
					isReady = true;
				}

				if (isReady) {
					if (counter < 205) {
						if(AI.getVelocity()<AI.CAR_SPEED){
							AI.applyForwardAcceleration();
							System.out.println("!!!!!!!!!!!");
						}
						
						counter++;
					} else {
						System.out.println("2222222");
						AI.strategyLock = false;
						target = null;
						System.out.println("xxxxxxx");
					}
				}

			}
			break;

		case SOUTH:
			if (!AI.isTurningLeft && !AI.isTurningRight && isReady == false && AI.checkWest(currentView, 2)) {

				AI.applyReverseAcceleration();
				AI.lastTurnDirection = WorldSpatial.RelativeDirection.RIGHT;
				AI.strategyLock = true;
				isReversing = true;

			} else {
				// switch (AI.getOrientation()) {
				// case NORTH:
				if (!AI.checkEast(currentView, 2) && !AI.getOrientation().equals(WorldSpatial.Direction.WEST)) {

					AI.applyRightTurn(AI.getOrientation(), delta);
					AI.isTurningRight = true;
					isTurning = true;

				} else if (AI.getOrientation().equals(WorldSpatial.Direction.WEST)) {
					AI.isTurningRight = false;
					isTurning = false;
					isReady = true;
				}

				if (isReady) {
					if (counter < 205) {
						if(AI.getVelocity()<AI.CAR_SPEED){
							AI.applyForwardAcceleration();
							System.out.println("!!!!!!!!!!!");
						}
						
						counter++;
					} else {
						System.out.println("2222222");
						AI.strategyLock = false;
						target = null;
						System.out.println("xxxxxxx");
					}
				}

			}
			break;

		case EAST:
			if (!AI.isTurningLeft && !AI.isTurningRight && isReady == false && AI.checkSouth(currentView, 1)) {

				AI.applyReverseAcceleration();
				AI.lastTurnDirection = WorldSpatial.RelativeDirection.RIGHT;
				AI.strategyLock = true;
				isReversing = true;

			} else {
				// switch (AI.getOrientation()) {
				// case NORTH:
				if (!AI.checkEast(currentView, 1) && !AI.getOrientation().equals(WorldSpatial.Direction.SOUTH)) {

					AI.applyRightTurn(AI.getOrientation(), delta);
					AI.isTurningRight = true;
					isTurning = true;

				} else if (AI.getOrientation().equals(WorldSpatial.Direction.SOUTH)) {
					AI.isTurningRight = false;
					isTurning = false;
					isReady = true;
				}

				if (isReady) {
					if (counter < 180) {
						AI.applyForwardAcceleration();
						System.out.println("!!!!!!!!!!!");
						counter++;
					} else {
						System.out.println("2222222");
						AI.applyForwardAcceleration();
						AI.strategyLock = false;
						System.out.println("xxxxxxx");
					}
				}

			}
			break;
		case WEST:
			if (!AI.isTurningLeft && !AI.isTurningRight && isReady == false && AI.checkNorth(currentView, 1)) {

				AI.applyReverseAcceleration();
				AI.lastTurnDirection = WorldSpatial.RelativeDirection.RIGHT;
				AI.strategyLock = true;
				isReversing = true;

			} else {
				// switch (AI.getOrientation()) {
				// case NORTH:
				if (!AI.checkEast(currentView, 1) && !AI.getOrientation().equals(WorldSpatial.Direction.NORTH)) {

					AI.applyRightTurn(AI.getOrientation(), delta);
					AI.isTurningRight = true;
					isTurning = true;

				} else if (AI.getOrientation().equals(WorldSpatial.Direction.NORTH)) {
					AI.isTurningRight = false;
					isTurning = false;
					isReady = true;
				}

				if (isReady) {
					if (counter < 230) {
						AI.applyForwardAcceleration();
						System.out.println("!!!!!!!!!!!");
						counter++;
					} else {
						System.out.println("2222222");
						AI.applyForwardAcceleration();
						AI.strategyLock = false;
						System.out.println("xxxxxxx");
					}
				}

			}
			break;
		}

		

	}

	public WorldSpatial.Direction getTargetOrientation(WorldSpatial.Direction currentOrientation) {
		WorldSpatial.Direction target = null;
		switch (currentOrientation) {
		case NORTH:
			target = WorldSpatial.Direction.EAST;
			break;
		case EAST:
			target = WorldSpatial.Direction.SOUTH;
			break;
		case SOUTH:
			target = WorldSpatial.Direction.WEST;
			break;
		case WEST:
			target = WorldSpatial.Direction.NORTH;
			break;
		}
		return target;
	}
}
