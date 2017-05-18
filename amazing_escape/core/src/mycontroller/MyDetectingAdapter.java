package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class MyDetectingAdapter implements DetectStrategyAdapter {

	@Override
	public String detect(MyAIController AI,float delta){
		HashMap<Coordinate, MapTile> currentView = AI.getView();
		WorldSpatial.Direction currentOrientation = AI.getOrientation();
		Coordinate currentPosition = new Coordinate(AI.getPosition());
		//if the car is not following and no traps ahead, use U turn
		//if the car is following wall and no traps but a wall ahead, using the 
		//following logic to decide to adapt which turning strategy 
		if(AI.isFollowingWall && !AI.isTurningLeft && !AI.isTurningRight){
			switch(currentOrientation){
			case EAST:
				if(currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y)).getName().equals("Wall")&&
				   currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y-1)).getName().equals("Wall")&&
				   currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y-2)).getName().equals("Wall")&&
				   currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y-3)).getName().equals("Wall")){
					return "mycontroller.ThreePointTurnAdapter";
				}else if(currentView.get(new Coordinate(currentPosition.x+3, currentPosition.y)).getName().equals("Wall")&&
						 currentView.get(new Coordinate(currentPosition.x+3, currentPosition.y-1)).getName().equals("Wall")&&
						 currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y-2)).getName().equals("Wall")){
					return "mycontroller.ReverseAdapter";
				}else{
					return "mycontroller.UTurnAdapter";
				}
			case NORTH:
				if(currentView.get(new Coordinate(currentPosition.x, currentPosition.y+2)).getName().equals("Wall")&&
				   currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y+2)).getName().equals("Wall")&&
				   currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y+2)).getName().equals("Wall")&&
				   currentView.get(new Coordinate(currentPosition.x+3, currentPosition.y+1)).getName().equals("Wall")){
					return "mycontroller.ThreePointTurnAdapter";
				}else if(currentView.get(new Coordinate(currentPosition.x, currentPosition.y+3)).getName().equals("Wall")&&
						 currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y+3)).getName().equals("Wall")&&
						 currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y+2)).getName().equals("Wall")){
					return "mycontroller.ReverseAdapter";
				}else{
					return "mycontroller.UTurnAdapter";
				}
			case WEST:
				if(currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y)).getName().equals("Wall")&&
				   currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y+1)).getName().equals("Wall")&&
				   currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y+2)).getName().equals("Wall")&&
				   currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y+3)).getName().equals("Wall")){
				    /*System.out.println("Start");
				    System.out.println("Current location: "+currentPosition);
				    System.out.println("TurningRight: "+AI.isTurningRight + " Left: "+ AI.isTurningLeft);
				    System.out.println(AI.peek(AI.getRawVelocity(),90,WorldSpatial.RelativeDirection.RIGHT,delta).getCoordinate());
				    System.out.println(AI.peek(AI.getRawVelocity(),180,WorldSpatial.RelativeDirection.RIGHT,delta).getCoordinate());
				    System.out.println(AI.peek(AI.getRawVelocity(),180,WorldSpatial.RelativeDirection.RIGHT,delta).getReachable());
*/				   return "mycontroller.ThreePointTurnAdapter";
				}else if(currentView.get(new Coordinate(currentPosition.x-3, currentPosition.y)).getName().equals("Wall")&&
						 currentView.get(new Coordinate(currentPosition.x-3, currentPosition.y+1)).getName().equals("Wall")&&
						 currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y+2)).getName().equals("Wall")){
					return "mycontroller.ReverseAdapter";
				}else{
					return "mycontroller.UTurnAdapter";
				}
			case SOUTH:
				if(currentView.get(new Coordinate(currentPosition.x, currentPosition.y-2)).getName().equals("Wall")&&
				   currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y-2)).getName().equals("Wall")&&
				   currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y-2)).getName().equals("Wall")&&
				   currentView.get(new Coordinate(currentPosition.x-3, currentPosition.y-1)).getName().equals("Wall")){
					return "mycontroller.ThreePointTurnAdapter";
				}else if(currentView.get(new Coordinate(currentPosition.x, currentPosition.y-3)).getName().equals("Wall")&&
						 currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y-3)).getName().equals("Wall")&&
						 currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y-2)).getName().equals("Wall")){
					return "mycontroller.ReverseAdapter";
				}else{
					return "mycontroller.UTurnAdapter";
				}
			default:
				return "mycontroller.UTurnAdapter";
			}
		}else
			return "mycontroller.UTurnAdapter";
		
	}
}
