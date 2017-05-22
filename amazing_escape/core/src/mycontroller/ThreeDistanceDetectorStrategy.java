package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class ThreeDistanceDetectorStrategy implements DetectStrategy {

	@Override
	public String detect(MyAIController AI,float delta){
		HashMap<Coordinate, MapTile> currentView = AI.getView();
		WorldSpatial.Direction currentOrientation = AI.getOrientation();
		Coordinate currentPosition = new Coordinate(AI.getPosition());
		String strategy;
//		if(AI.checkTrapAhead(currentOrientation, currentView, AI.wallSensitivity) && !AI.checkWallAhead(currentOrientation, currentView, 1)){
//			return "mycontroller.SimpleAvoidingAdapter";
//		}
		if(AI.checkWallAhead(currentOrientation, currentView, AI.getViewSquare()) && !AI.isTurningLeft && !AI.isTurningRight){
			System.out.println("detecting");
			switch(currentOrientation){	 
			case EAST:
				if(!AI.checkNorth(currentView,AI.wallSensitivity)
				   ||(!currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y+1)).getName().equals("Wall")
					&& !currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y+2)).getName().equals("Wall"))){
						strategy = "mycontroller.UTurnStrategy";
				}
				
				if(currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y-2)).getName().equals("Wall")
					&& !currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y)).getName().equals("Wall")){
					strategy = "mycontroller.ReverseOutStrategy";
				}else if(currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y-1)).getName().equals("Wall")&&
						currentView.get(new Coordinate(currentPosition.x, currentPosition.y-3)).getName().equals("Wall")){
					strategy = "mycontroller.ThreePointTurnStrategy";
				}else{
					strategy = "mycontroller.UTurnStrategy";
				}
				break;
			case NORTH:
				if(!AI.checkWest(currentView,AI.wallSensitivity)
				    ||(!currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y+1)).getName().equals("Wall")
					&& !currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y+1)).getName().equals("Wall"))){
					strategy = "mycontroller.UTurnStrategy";
				}
				
				if(currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y+2)).getName().equals("Wall")
					&& !currentView.get(new Coordinate(currentPosition.x, currentPosition.y+2)).getName().equals("Wall")){
					strategy = "mycontroller.ReverseOutStrategy";
				}else if(currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y+2)).getName().equals("Wall")&&
						currentView.get(new Coordinate(currentPosition.x+3, currentPosition.y)).getName().equals("Wall")){
					strategy = "mycontroller.ThreePointTurnStrategy";
				}else{
					strategy = "mycontroller.UTurnStrategy";
				}
				break;
			case WEST:
				if(!AI.checkSouth(currentView,AI.wallSensitivity)
					||(!currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y-1)).getName().equals("Wall")
						&& !currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y-2)).getName().equals("Wall"))){
					strategy = "mycontroller.UTurnStrategy";
				}
				
				if(currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y+2)).getName().equals("Wall")
					&& !currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y)).getName().equals("Wall")){
					strategy = "mycontroller.ReverseOutStrategy";
				}else if(currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y+1)).getName().equals("Wall")&&
						currentView.get(new Coordinate(currentPosition.x, currentPosition.y+3)).getName().equals("Wall")){
					strategy = "mycontroller.ThreePointTurnStrategy";
				}else{
					strategy = "mycontroller.UTurnStrategy";
				}
				break;
			case SOUTH:
				if(!AI.checkEast(currentView,AI.wallSensitivity)
					||(!currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y-1)).getName().equals("Wall")
						&& !currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y-1)).getName().equals("Wall"))){
					strategy = "mycontroller.UTurnStrategy";
				}
				
				if(currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y-2)).getName().equals("Wall")
					&& !currentView.get(new Coordinate(currentPosition.x, currentPosition.y-2)).getName().equals("Wall")){
					strategy = "mycontroller.ReverseOutStrategy";
				}else if(currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y-2)).getName().equals("Wall")&&
						currentView.get(new Coordinate(currentPosition.x-3, currentPosition.y)).getName().equals("Wall")){
					strategy = "mycontroller.ThreePointTurnStrategy";
				}else{
					strategy = "mycontroller.UTurnStrategy";
				}
				break;
			default:
				strategy = "mycontroller.UTurnStrategy";
				break;
			}
		}else{
			strategy = "mycontroller.UTurnStrategy";
		}
		
		if(strategy == "mycontroller.UTurnStrategy" && AI.checkLavaAhead(currentOrientation,currentView,AI.wallSensitivity)){
			strategy = "mycontroller.SimpleAvoidingStrategy";
		}
		
		return strategy;
	}
}
