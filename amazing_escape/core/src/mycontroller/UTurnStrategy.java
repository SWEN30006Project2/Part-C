package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class UTurnStrategy implements TurningStrategyAdapter {

	@Override
	public void applyTurning(MyAIController AI,float delta) {
		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = AI.getView();
		AI.checkStateChange();
		//find a wall at the beginning
		if(AI.preFollowWallOpsiteDir == null){
				if(AI.getVelocity() < AI.CAR_SPEED){
					AI.applyForwardAcceleration();
				}
				// Turn towards the north
				if(!AI.getOrientation().equals(WorldSpatial.Direction.NORTH)){
					AI.lastTurnDirection = WorldSpatial.RelativeDirection.LEFT;
					AI.applyLeftTurn(AI.getOrientation(),delta);
				}
				if(AI.checkNorth(currentView,AI.wallSensitivity)){
					// Turn right until we go back to east!
					if(!AI.getOrientation().equals(WorldSpatial.Direction.EAST)){
						AI.lastTurnDirection = WorldSpatial.RelativeDirection.RIGHT;
						AI.applyRightTurn(AI.getOrientation(),delta);
					}
					else{
						AI.checkFollowingWall(AI.getOrientation(),currentView,AI.wallSensitivity);
					}
				}	
		}else{//after finding the first wall, apply the following to follow the wall
			if(!AI.isTurningLeft && !AI.isTurningRight){
				//if is not following wall now and was following wall before 
				//and not in the opposite direction of the direction the last time the car was following the wall, turn left
				if(!AI.checkFollowingWall(AI.getOrientation(),currentView,AI.wallSensitivity) &&
						AI.preFollowWallOpsiteDir != null && AI.getOrientation() != AI.preFollowWallOpsiteDir){
				    AI.lastTurnDirection = WorldSpatial.RelativeDirection.LEFT;
					AI.applyLeftTurn(AI.getOrientation(),delta);
					AI.isTurningLeft = true;
				}else{
					if(AI.lastTurnDirection != null){
						AI.readjust(AI.getOrientation(),delta);
					}
					//if there is a wall ahead, turn right
					if(AI.checkWallAhead(AI.getOrientation(),currentView,AI.wallSensitivity)){
						AI.lastTurnDirection = WorldSpatial.RelativeDirection.RIGHT;
						AI.applyRightTurn(AI.getOrientation(),delta);
						AI.isTurningRight = true;	
					}else if(AI.getVelocity() < AI.CAR_SPEED){
						AI.applyForwardAcceleration();
					}
				}
			}else{
				if(AI.isTurningRight){
					AI.applyRightTurn(AI.getOrientation(),delta);
				}
				else if(AI.isTurningLeft){
					// Apply the left turn if you are not currently near a wall.
					if(!AI.checkFollowingWall(AI.getOrientation(),currentView,AI.wallSensitivity)){
						AI.applyLeftTurn(AI.getOrientation(),delta);
					}
					else{
						AI.isTurningLeft = false;
					}
				}
			}
		}

	}

}
