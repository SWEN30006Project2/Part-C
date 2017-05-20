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
		if(AI.checkWallAhead(currentOrientation, currentView, AI.getViewSquare()) && !AI.isTurningLeft && !AI.isTurningRight){
			switch(currentOrientation){
			case EAST:
				if(!AI.checkNorth(currentView,AI.wallSensitivity)
				   ||(!currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y+1)).getName().equals("Wall")
					&& !currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y+2)).getName().equals("Wall"))){
					return "mycontroller.UTurnAdapter";
				}
				
				if(currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y-2)).getName().equals("Wall")
					&& !currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y)).getName().equals("Wall")){
					return "mycontroller.ReverseAdapter";
				}else if(currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y-1)).getName().equals("Wall")&&
						currentView.get(new Coordinate(currentPosition.x, currentPosition.y-3)).getName().equals("Wall")){
					return "mycontroller.ThreePointTurnAdapter";
				}else{
					return "mycontroller.UTurnAdapter";
				}
			case NORTH:
				if(!AI.checkWest(currentView,AI.wallSensitivity)
				    ||(!currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y+1)).getName().equals("Wall")
					&& !currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y+1)).getName().equals("Wall"))){
					return "mycontroller.UTurnAdapter";
				}
				
				if(currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y+2)).getName().equals("Wall")
					&& !currentView.get(new Coordinate(currentPosition.x, currentPosition.y+2)).getName().equals("Wall")){
					return "mycontroller.ReverseAdapter";
				}else if(currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y+2)).getName().equals("Wall")&&
						currentView.get(new Coordinate(currentPosition.x+3, currentPosition.y)).getName().equals("Wall")){
					return "mycontroller.ThreePointTurnAdapter";
				}else{
					return "mycontroller.UTurnAdapter";
				}
			case WEST:
				if(!AI.checkSouth(currentView,AI.wallSensitivity)
					||(!currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y-1)).getName().equals("Wall")
						&& !currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y-2)).getName().equals("Wall"))){
					return "mycontroller.UTurnAdapter";
				}
				
				if(currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y+2)).getName().equals("Wall")
					&& !currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y)).getName().equals("Wall")){
					return "mycontroller.ReverseAdapter";
				}else if(currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y+1)).getName().equals("Wall")&&
						currentView.get(new Coordinate(currentPosition.x, currentPosition.y+3)).getName().equals("Wall")){
					return "mycontroller.ThreePointTurnAdapter";
				}else{
					return "mycontroller.UTurnAdapter";
				}
			case SOUTH:
				if(!AI.checkEast(currentView,AI.wallSensitivity)
					||(!currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y-1)).getName().equals("Wall")
						&& !currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y-1)).getName().equals("Wall"))){
					return "mycontroller.UTurnAdapter";
				}
				
				if(currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y-1)).getName().equals("Wall")
					&& !currentView.get(new Coordinate(currentPosition.x, currentPosition.y-2)).getName().equals("Wall")){
					return "mycontroller.ReverseAdapter";
				}else if(currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y-2)).getName().equals("Wall")&&
						currentView.get(new Coordinate(currentPosition.x-3, currentPosition.y)).getName().equals("Wall")){
					return "mycontroller.ThreePointTurnAdapter";
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
