package mycontroller;

import java.util.HashMap;

import controller.CarController;
import tiles.GrassTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

public class MyAIController extends CarController {
	
	// How many minimum units the wall is away from the player.
    int wallSensitivity = 2;
    boolean isFollowingWall = false; // This is initialized when the car sticks to a wall.
	WorldSpatial.RelativeDirection lastTurnDirection = null; // Shows the last turn direction the car takes.
	boolean isTurningLeft = false;
	boolean isTurningRight = false; 
	WorldSpatial.Direction previousState = null; // Keeps track of the previous state
	//keep track of the opposite direction of the last time the car was following the wall
	WorldSpatial.Direction preFollowWallOpsiteDir = null;   
	boolean strategyLock = false; 
	TurningStrategyAdapter turningStrategy;
	AvoidingStrategyAdapter avoidingStrategy;                                                  
	 
	// Car Speed to move at
	final float CAR_SPEED = 3;
	
	// Offset used to differentiate between 0 and 360 degrees
	int EAST_THRESHOLD = 3;
	
	public MyAIController(Car car) {
		super(car);
		HashMap<Coordinate,MapTile> myMap = new HashMap<Coordinate,MapTile>();
	}

	@Override
	public void update(float delta) {
//		System.out.println("Current position: "+getPosition());
//		System.out.println("Current speed: "+getVelocity());
//		System.out.println(getAngle());
		DetectStrategyAdapter detector = StrategyFactory.getInstance().getDetectStrategyAdapter("mycontroller.ThreeDistanceDetectorStrategy");
		String adapter = detector.detect(this, delta);
		try{
			if(!strategyLock)
			    avoidingStrategy = StrategyFactory.getInstance().getAvoidingStrategyAdapter(adapter);
			avoidingStrategy.avoid(this, delta);   
		}catch(Exception e){
			if(!strategyLock)
				turningStrategy = StrategyFactory.getInstance().getTurningStrategyAdapter(adapter);
			turningStrategy.applyTurning(this, delta);
		}
	}
	
	/**
	 * Readjust the car to the orientation we are in.
	 * @param lastTurnDirection
	 * @param delta
	 */
	void readjust(WorldSpatial.Direction orientation ,float delta) {
		if(lastTurnDirection != null && !isTurningLeft && !isTurningRight){
			switch(orientation){
			case EAST:
				if(getAngle() > WorldSpatial.EAST_DEGREE_MIN && getAngle()<WorldSpatial.NORTH_DEGREE){
					turnRight(delta);
				}else if(getAngle() < WorldSpatial.EAST_DEGREE_MAX && getAngle() > WorldSpatial.SOUTH_DEGREE){
					turnLeft(delta);
				}
				break;
			case NORTH:
				if(getAngle() > WorldSpatial.NORTH_DEGREE){
					turnRight(delta);
				}else if(getAngle()<WorldSpatial.NORTH_DEGREE){
					turnLeft(delta);
				}
				break;
			case SOUTH:
				if(getAngle()>WorldSpatial.SOUTH_DEGREE){
					turnRight(delta);
				}else if(getAngle()<WorldSpatial.SOUTH_DEGREE){
					turnLeft(delta);
				}
				break;
			case WEST:
				if(getAngle() > WorldSpatial.WEST_DEGREE){
					turnRight(delta);
				}else if(getAngle()<WorldSpatial.WEST_DEGREE){
					turnLeft(delta);
				}
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Checks whether the car's state has changed or not, stops turning if it
	 *  already has. is turning return true, otherwise return false 
	 */
	 void checkStateChange() {
		if(previousState == null){
			previousState = getOrientation();
		}
		else{
			if(previousState != getOrientation()){
				if(isTurningLeft){
					isTurningLeft = false;
				}
				if(isTurningRight){
					isTurningRight = false;
				}
				previousState = getOrientation();
			}
		}
	}
	
	/**
	 * Turn the car counter clock wise (think of a compass going counter clock-wise)
	 */
	void applyLeftTurn(WorldSpatial.Direction orientation, float delta) {
		switch(orientation){
		case EAST:
			if(!getOrientation().equals(WorldSpatial.Direction.NORTH)){
				turnLeft(delta);
			}
			break;
		case NORTH:
			if(!getOrientation().equals(WorldSpatial.Direction.WEST)){
				turnLeft(delta);
			}
			break;
		case SOUTH:
			if(!getOrientation().equals(WorldSpatial.Direction.EAST)){
				turnLeft(delta);
			}
			break;
		case WEST:
			if(!getOrientation().equals(WorldSpatial.Direction.SOUTH)){
				turnLeft(delta);
			}
			break;
		default:
			break;
		
		}
		
	}
	
	/**
	 * Turn the car clock wise (think of a compass going clock-wise)
	 */
	void applyRightTurn(WorldSpatial.Direction orientation, float delta) {
		switch(orientation){
		case EAST:
			if(!getOrientation().equals(WorldSpatial.Direction.SOUTH)){
				turnRight(delta);
			}
			break;
		case NORTH:
			if(!getOrientation().equals(WorldSpatial.Direction.EAST)){
				turnRight(delta);
			}
			break;
		case SOUTH:
			if(!getOrientation().equals(WorldSpatial.Direction.WEST)){
				turnRight(delta);
			}
			break;
		case WEST:
			if(!getOrientation().equals(WorldSpatial.Direction.NORTH)){
				turnRight(delta);
			}
			break;
		default:
			break;
		
		}
		
	}

	/**
	 * Check if you have a wall in front of you!
	 * @param orientation the orientation we are in based on WorldSpatial
	 * @param currentView what the car can currently see
	 * @return
	 */
	boolean checkWallAhead(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView,int distance){
		switch(orientation){
		case EAST:
			return checkEast(currentView,distance);
		case NORTH:
			return checkNorth(currentView,distance);
		case SOUTH:
			return checkSouth(currentView,distance);
		case WEST:
			return checkWest(currentView,distance);
		default:
			return false;
		
		}
	}
	
	/**
	 * check whether there is a trap in the second unit ahead and there is no wall between the car and the trap
	 * @param orientation the current orientation of the car
	 * @param currentView the current view of the car
	 * @param distance the distance you want to check
	 * @return if there is a lava in the second unit, return true, otherwise return false
	 */
	boolean checkLavaAhead(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView,int distance){
		Coordinate currentPosition = new Coordinate(getPosition());
		switch(orientation){
		case EAST:
			for(int i = 2; i <= distance; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x+i, currentPosition.y));
				if(tile instanceof LavaTrap &&
				   !currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y)).getName().equals("Wall")){
					return true;
				}
			}
			return false;
		case NORTH:
			for(int i = 2; i <= distance; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y+i));
				if(tile instanceof LavaTrap
					&& !currentView.get(new Coordinate(currentPosition.x, currentPosition.y+1)).getName().equals("Wall")){
					return true;
				}
			}
			return false;
		case SOUTH:
			for(int i = 2; i <= distance; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
				if(tile instanceof LavaTrap &&
				   !currentView.get(new Coordinate(currentPosition.x, currentPosition.y-1)).getName().equals("Wall")){
					return true;
				}
			}
			return false;
		case WEST:
			for(int i = 2; i <= distance; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
				if(tile instanceof LavaTrap &&
				   !currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y)).getName().equals("Wall")){
					return true;
				}
			}
			return false;
		default:
			return false;
		
		}
	}
	
	/**
	 * Check if the wall is on your left hand side given your orientation
	 * @param orientation
	 * @param currentView
	 * @return
	 */
	boolean checkFollowingWall(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView,int distance) {
		
		switch(orientation){
		case EAST:
			isFollowingWall = checkNorth(currentView,distance);
			if(isFollowingWall){
				preFollowWallOpsiteDir = WorldSpatial.Direction.WEST;
			}
			//System.out.println("checkNorth"+" "+isFollowingWall);
			return isFollowingWall;
		case NORTH:
			isFollowingWall = checkWest(currentView,distance);
			if(isFollowingWall){
				preFollowWallOpsiteDir = WorldSpatial.Direction.SOUTH;
			}
			//System.out.println("checkWest"+" "+isFollowingWall);
			return isFollowingWall;
		case SOUTH:
			isFollowingWall = checkEast(currentView,distance);
			if(isFollowingWall){
				preFollowWallOpsiteDir = WorldSpatial.Direction.NORTH;
			}
			//System.out.println("checkEast"+" "+isFollowingWall);
			return isFollowingWall;
		case WEST:
			isFollowingWall = checkSouth(currentView,distance);
			if(isFollowingWall){
				preFollowWallOpsiteDir = WorldSpatial.Direction.EAST;
			}
			//System.out.println("checkSouth"+" "+isFollowingWall);
			return isFollowingWall;
		default:
			isFollowingWall = false;
			return false;
		}
		
	}
	

	/**
	 * Method below just iterates through the list and check in the correct coordinates.
	 * i.e. Given your current position is 10,10
	 * checkEast will check up to wallSensitivity amount of tiles to the right.
	 * checkWest will check up to wallSensitivity amount of tiles to the left.
	 * checkNorth will check up to wallSensitivity amount of tiles to the top.
	 * checkSouth will check up to wallSensitivity amount of tiles below.
	 */
	boolean checkEast(HashMap<Coordinate, MapTile> currentView,int distance){
		// Check tiles to my right
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= distance; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x+i, currentPosition.y));
			if(tile.getName().equals("Wall")){
				return true;
			}
		}
		return false;
	}
	
	boolean checkWest(HashMap<Coordinate,MapTile> currentView,int distance){
		// Check tiles to my left
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= distance; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
			if(tile.getName().equals("Wall")){
				return true;
			}
		}
		return false;
	}
	
	boolean checkNorth(HashMap<Coordinate,MapTile> currentView,int distance){
		// Check tiles to towards the top
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= distance; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y+i));
			if(tile.getName().equals("Wall")){
				return true;
			}
		}
		return false;
	}
	
	boolean checkSouth(HashMap<Coordinate,MapTile> currentView,int distance){
		// Check tiles towards the bottom
		Coordinate currentPosition = new Coordinate(getPosition());
		for(int i = 0; i <= distance; i++){
			MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
			if(tile.getName().equals("Wall")){
				return true;
			}
		}
		return false;
	}
}
