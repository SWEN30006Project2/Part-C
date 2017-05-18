package mycontroller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class predictDetectingAdapter implements DetectStrategyAdapter {
	@Override
	public String detect(MyAIController AI, float delta) {
		HashMap<Coordinate, MapTile> currentView = AI.getView();
		WorldSpatial.Direction currentOrientation = AI.getOrientation();
		Coordinate currentPosition = new Coordinate(AI.getPosition());
		if(AI.checkWallAhead(currentOrientation, currentView, 3) && !AI.isTurningLeft && AI.isTurningRight){
			switch(currentOrientation){
			case NORTH:
				System.out.println("NORTH");
				if(!currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y+1)).getName().equals("Wall")&&
						!currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y+2)).getName().equals("Wall")){
					return "mycontroller.UTurnAdapter";
				}else{
					if(AI.peek(AI.getRawVelocity(),-90,WorldSpatial.RelativeDirection.RIGHT,delta).getReachable()){
						return "mycontroller.UTurnAdapter";
					}else if(AI.peek(AI.getRawVelocity(),-360,WorldSpatial.RelativeDirection.RIGHT,delta).getReachable()){
						return "mycontroller.ThreePointTurnAdapter";
					}else
						return "mycontroller.ReverseAdapter";
				}
			case WEST:
				System.out.println("WEST");
				if(!currentView.get(new Coordinate(currentPosition.x-1, currentPosition.y-1)).getName().equals("Wall")&&
						!currentView.get(new Coordinate(currentPosition.x-2, currentPosition.y-1)).getName().equals("Wall")){
					return "mycontroller.UTurnAdapter";
				}else{
					if(AI.peek(AI.getRawVelocity(),-360,WorldSpatial.RelativeDirection.RIGHT,delta).getReachable()){
						return "mycontroller.UTurnAdapter";
					}else if(AI.peek(AI.getRawVelocity(),-270,WorldSpatial.RelativeDirection.RIGHT,delta).getReachable()){
						return "mycontroller.ThreePointTurnAdapter";
					}else
						return "mycontroller.ReverseAdapter";
				}
			case SOUTH:
				System.out.println("SOUTH");
				if(!currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y-1)).getName().equals("Wall")&&
						!currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y-2)).getName().equals("Wall")){
					return "mycontroller.UTurnAdapter";
				}else{
					if(AI.peek(AI.getRawVelocity(),90,WorldSpatial.RelativeDirection.RIGHT,delta).getReachable()){
						return "mycontroller.UTurnAdapter";
					}else if(AI.peek(AI.getRawVelocity(),180,WorldSpatial.RelativeDirection.RIGHT,delta).getReachable()){
						return "mycontroller.ThreePointTurnAdapter";
					}else
						return "mycontroller.ReverseAdapter";
				}
			case EAST:
				System.out.println("EAST");
				if(!currentView.get(new Coordinate(currentPosition.x+1, currentPosition.y+1)).getName().equals("Wall")&&
						!currentView.get(new Coordinate(currentPosition.x+2, currentPosition.y+1)).getName().equals("Wall")){
					return "mycontroller.UTurnAdapter";
				}else{
					System.out.println("future: "+AI.peek(AI.getRawVelocity(),-180,WorldSpatial.RelativeDirection.RIGHT,delta).getCoordinate());
					System.out.println("future: "+AI.peek(AI.getRawVelocity(),-180,WorldSpatial.RelativeDirection.RIGHT,delta).getReachable());					
					if(AI.peek(AI.getRawVelocity(),180,WorldSpatial.RelativeDirection.RIGHT,delta).getReachable()){
						return "mycontroller.UTurnAdapter";
					}else if(AI.peek(AI.getRawVelocity(),270,WorldSpatial.RelativeDirection.RIGHT,delta).getReachable()){
						return "mycontroller.ThreePointTurnAdapter";
					}else
						return "mycontroller.ReverseAdapter";
				}
			default:
				return "mycontroller.UTurnAdapter";
			}	
		}
		return "mycontroller.UTurnAdapter";
	}

}
