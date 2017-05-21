package mycontroller;

import java.util.HashMap;


import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

public class ThreePointTurnAdapter implements TurningStrategyAdapter{
	
	private static WorldSpatial.Direction targetOrientation;
	private final static float SPEED_LIMIT = (float) 1.15;
	
	@Override
	public void applyTurning(MyAIController AI, float delta) {
		if(!AI.strategyLock){
			targetOrientation = null;
		}
		AI.checkStateChange();
		HashMap<Coordinate, MapTile> currentView = AI.getView();
		
		if(!AI.isTurningLeft && !AI.isTurningRight && targetOrientation ==null){
			
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
				
					System.out.println(AI.getPosition());
					AI.applyReverseAcceleration();
					AI.lastTurnDirection = WorldSpatial.RelativeDirection.RIGHT;
					AI.applyLeftTurn(AI.getOrientation(),delta);
					AI.isTurningRight = true;	
					targetOrientation = getTargetOrientation(AI.getOrientation());
					AI.strategyLock = true;
			}
			
		}else{
			
			
			
			if(AI.isTurningRight && !AI.getOrientation().equals(targetOrientation)&&!AI.isTurningLeft){
				AI.applyReverseAcceleration();
				AI.applyLeftTurn(AI.getOrientation(), delta);
				

				
			}else if (AI.isTurningLeft) {
				if(!AI.checkFollowingWall(AI.getOrientation(),currentView,AI.wallSensitivity)){
					AI.applyLeftTurn(AI.getOrientation(),delta);
				}
				else{
					AI.isTurningLeft = false;
				}
			}
			else if(AI.getOrientation().equals(targetOrientation)){
				System.out.println(AI.strategyLock);
				switch (AI.getOrientation()) {
				case EAST:
					if(AI.checkWest(currentView, 1)){
						AI.applyForwardAcceleration();
						AI.strategyLock = false;
						targetOrientation = null;
						AI.checkFollowingWall(AI.getOrientation(),currentView,AI.wallSensitivity);
					}else{
						AI.applyReverseAcceleration();
						if(AI.getVelocity()>SPEED_LIMIT){
							AI.applyForwardAcceleration();
						}
					}
					break;
				case NORTH:
					if(AI.checkSouth(currentView, 1)){
						AI.applyForwardAcceleration();
						AI.strategyLock = false;
						targetOrientation = null;
						AI.checkFollowingWall(AI.getOrientation(),currentView,AI.wallSensitivity);
					}else{
						
						AI.applyReverseAcceleration();
						if(AI.getVelocity()>SPEED_LIMIT){
							AI.applyForwardAcceleration();
						}
						
						
					}
					break;
					
				case SOUTH:
					if(AI.checkNorth(currentView, 1)){
						AI.applyForwardAcceleration();
						AI.strategyLock = false;
						targetOrientation = null;
						AI.checkFollowingWall(AI.getOrientation(),currentView,AI.wallSensitivity);
					}else{
						AI.applyReverseAcceleration();
						if(AI.getVelocity()>SPEED_LIMIT){
							AI.applyForwardAcceleration();
						}
						
					}
					break;
					
				case WEST:
					if(AI.checkEast(currentView, 1)){
						AI.applyForwardAcceleration();
						AI.strategyLock = false;
						targetOrientation = null;
						AI.checkFollowingWall(AI.getOrientation(),currentView,AI.wallSensitivity);
					}else{
						AI.applyReverseAcceleration();
						if(AI.getVelocity()>SPEED_LIMIT){
							AI.applyForwardAcceleration();
						}
					}
					break;
				}
			}
			
		}
		
	}
	
	
	
	public WorldSpatial.Direction getTargetOrientation(WorldSpatial.Direction currentOrientation){
		WorldSpatial.Direction target =null;
			switch (currentOrientation) {
			case NORTH:
				target =  WorldSpatial.Direction.EAST;	
				break;
			case EAST:
				target =  WorldSpatial.Direction.SOUTH;	
				break;
			case SOUTH:
				target =  WorldSpatial.Direction.WEST;
				break;
			case WEST:
				target =  WorldSpatial.Direction.NORTH;
				break;
			}
		return target;
	}
	
	public WorldSpatial.Direction getReverseOrentation(WorldSpatial.Direction currentOrientation){
		WorldSpatial.Direction target =null;
			switch (currentOrientation) {
			case NORTH:
				target =  WorldSpatial.Direction.SOUTH;	
				break;
			case EAST:
				target =  WorldSpatial.Direction.WEST;	
				break;
			case SOUTH:
				target =  WorldSpatial.Direction.NORTH;
				break;
			case WEST:
				target =  WorldSpatial.Direction.EAST;
				break;
			}
		return target;
	}

}
