package mycontroller;

import java.util.ArrayList;
import java.util.HashMap;

import tiles.GrassTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import tiles.MudTrap;
import utilities.Coordinate;
import world.WorldSpatial;

public class SimpleAvoidingStrategy implements AvoidingStrategy {
	
	private static final int ONE_UNIT = 1;
	
	private ArrayList<Coordinate> route;
	private HashMap<Coordinate, MapTile> currentView;
	private Coordinate currentPosition;
	private Coordinate target;
	private WorldSpatial.Direction orientation;
	private HashMap<Coordinate, MapTile> lavas;
	private HashMap<Coordinate, MapTile> muds;
    private HashMap<Coordinate, MapTile> grasses;
    private HashMap<Coordinate, MapTile> walls;
	
	public SimpleAvoidingStrategy(){
		route = new ArrayList<Coordinate>();	
	}

	@Override
	public void avoid(MyAIController AI, float delta) {
		currentView = AI.getView();
		currentPosition = new Coordinate(AI.getPosition());
		orientation = AI.getOrientation();
		lavas = new HashMap<Coordinate, MapTile>();
		muds = new HashMap<Coordinate, MapTile>();
		grasses = new HashMap<Coordinate, MapTile>();
		walls = new HashMap<Coordinate, MapTile>();
		selectRoute(AI);
	}
	
	public void selectRoute(MyAIController AI){
		
		Coordinate targetPoint;
		//If the car is tightly following wall
		if(AI.checkFollowingWall(orientation, currentView, ONE_UNIT)){
			switch(orientation){
			case NORTH:
				if(lavas.containsKey(new Coordinate(x,y+2))&& !walls.containsKey(new Coordinate(x-1, y+1))){
					
				}
			}
		}
	}
	
	private void parseView(){
		for(int i = currentPosition.x+3;i>=currentPosition.x-3;i--){
			for(int j=currentPosition.y+3;i>=currentPosition.y-3;i--){
				MapTile tile = currentView.get(new Coordinate(i, j));
				if(tile instanceof LavaTrap){
					lavas.put(new Coordinate(i, j), tile);
				}else if(tile instanceof MudTrap){
					muds.put(new Coordinate(i, j), tile);
				}else if(tile instanceof GrassTrap){
					grasses.put(new Coordinate(i, j), tile);
				}else if(tile.getName().equals("Wall")){
					walls.put(new Coordinate(i, j), tile);
				}
			}
		}
	}
	
	private Coordinate findTargetPoint(MyAIController AI){
		ArrayList<Coordinate> targetsLeft = new ArrayList<Coordinate>();
		ArrayList<Coordinate> targetsRight = new ArrayList<Coordinate>();
			switch(orientation){
			case EAST:
				for(int i=currentPosition.y-1;i>=currentPosition.y-3;i--){
					if(!lavas.containsKey(new Coordinate(currentPosition.x+2,i)) && !walls.containsKey(new Coordinate(currentPosition.x+2,i))){
						if(i<currentPosition.y && walls.containsKey(new Coordinate(currentPosition.x+1,i)))
							break;
						targetsRight.add(new Coordinate(currentPosition.x+1,i));
					}
				}
				for(int i=currentPosition.y+1;i<=currentPosition.y+3;i++){
					if(!lavas.containsKey(new Coordinate(currentPosition.x+2,i)) && !walls.containsKey(new Coordinate(currentPosition.x+2,i))){
						if(i>currentPosition.y && walls.containsKey(new Coordinate(currentPosition.x+1,i)))
							break;
						targetsLeft.add(new Coordinate(currentPosition.x+1,i));
					}
				}
				//If we can avoid the traps
				if(targetsLeft.size() !=0 || targetsRight.size() != 0){
					target = (targetsLeft.size() !=0) ? targetsLeft.get(0) : targetsRight.get(0);
				//try to traverse the taps
				}else if(!lavas.containsKey(new Coordinate(currentPosition.x+3,currentPosition.y)) && !walls.containsKey(new Coordinate(currentPosition.x+3,currentPosition.y))){
					target = new Coordinate(currentPosition.x+2,currentPosition.y);
				}else{
					for(int i=currentPosition.y-1;i>=currentPosition.y-3;i--){
						if(!lavas.containsKey(new Coordinate(currentPosition.x+3,i)) && !walls.containsKey(new Coordinate(currentPosition.x+3,i))){
							if(i<currentPosition.y && (walls.containsKey(new Coordinate(currentPosition.x+2,i)) || walls.containsKey(new Coordinate(currentPosition.x+1,i))))
								break;
							targetsRight.add(new Coordinate(currentPosition.x+2,i));
						}
					}
					for(int i=currentPosition.y+1;i<=currentPosition.y+3;i++){
						if(!lavas.containsKey(new Coordinate(currentPosition.x+3,i)) && !walls.containsKey(new Coordinate(currentPosition.x+3,i))){
							if(i>currentPosition.y && (walls.containsKey(new Coordinate(currentPosition.x+2,i)) || walls.containsKey(new Coordinate(currentPosition.x+1,i))))
								break;
							targetsLeft.add(new Coordinate(currentPosition.x+2,i));
						}
					}
					
					if(targetsLeft.size() !=0 || targetsRight.size() != 0){
						target = (targetsLeft.size() !=0) ? targetsLeft.get(0) : targetsRight.get(0);
					}else{
						target = new Coordinate(currentPosition.x+2,currentPosition.y);
					}
				}
			break;
			case NORTH:
				for(int i=currentPosition.x+1;i<=currentPosition.x+3;i++){
					if(!lavas.containsKey(new Coordinate(i,currentPosition.y+2)) && !walls.containsKey(new Coordinate(i,currentPosition.y+2))){
						if(i>currentPosition.x && walls.containsKey(new Coordinate(i,currentPosition.y+1)))
							break;
						targetsRight.add(new Coordinate(i,currentPosition.y+1));
					}
				}
				for(int i=currentPosition.x-1;i>=currentPosition.x-3;i--){
					if(!lavas.containsKey(new Coordinate(i,currentPosition.y+2)) && !walls.containsKey(new Coordinate(i,currentPosition.y+2))){
						if(i<currentPosition.x && walls.containsKey(new Coordinate(i,currentPosition.y+1)))
							break;
						targetsLeft.add(new Coordinate(i,currentPosition.y+1));
					}
				}
				//If we can avoid the traps
				if(targetsLeft.size() !=0 || targetsRight.size() != 0){
					target = (targetsLeft.size() !=0) ? targetsLeft.get(0) : targetsRight.get(0);
				//try to traverse the taps
				}else if(!lavas.containsKey(new Coordinate(currentPosition.x,currentPosition.y+3)) && !walls.containsKey(new Coordinate(currentPosition.x,currentPosition.y+3))){
					target = new Coordinate(currentPosition.x,currentPosition.y+2);
				}else{
					for(int i=currentPosition.x+1;i<=currentPosition.x+3;i++){
						if(!lavas.containsKey(new Coordinate(i,currentPosition.y+3)) && !walls.containsKey(new Coordinate(i,currentPosition.y+3))){
							if(i>currentPosition.x && (walls.containsKey(new Coordinate(i,currentPosition.y+2)) || walls.containsKey(new Coordinate(i,currentPosition.y+1))))
								break;
							targetsRight.add(new Coordinate(i,currentPosition.y+2));
						}
					}
					for(int i=currentPosition.x-1;i>=currentPosition.x-3;i--){
						if(!lavas.containsKey(new Coordinate(i,currentPosition.y+3)) && !walls.containsKey(new Coordinate(i,currentPosition.y+3))){
							if(i<currentPosition.x && (walls.containsKey(new Coordinate(i,currentPosition.y+2)) || walls.containsKey(new Coordinate(i,currentPosition.y+1))))
								break;
							targetsLeft.add(new Coordinate(i,currentPosition.y+2));
						}
					}
					//if there is no safety paths on each side, just go straight
					if(targetsLeft.size() !=0 || targetsRight.size() != 0){
						target = (targetsLeft.size() !=0) ? targetsLeft.get(0) : targetsRight.get(0);
					}else{
						target = new Coordinate(currentPosition.x,currentPosition.y+2);
					}
				}
			break;
			case WEST:
				for(int i=currentPosition.y+1;i<=currentPosition.y+3;i++){
					if(!lavas.containsKey(new Coordinate(currentPosition.x-2,i)) && !walls.containsKey(new Coordinate(currentPosition.x-2,i))){
						if(i>currentPosition.y && walls.containsKey(new Coordinate(currentPosition.x-1,i)))
							break;
						targetsRight.add(new Coordinate(currentPosition.x-1,i));
					}
				}
				for(int i=currentPosition.y-1;i>=currentPosition.y-3;i--){
					if(!lavas.containsKey(new Coordinate(currentPosition.x-2,i)) && !walls.containsKey(new Coordinate(currentPosition.x-2,i))){
						if(i<currentPosition.y && walls.containsKey(new Coordinate(currentPosition.x-1,i)))
							break;
						targetsLeft.add(new Coordinate(currentPosition.x-1,i));
					}
				}
				//If we can avoid the traps
				if(targetsLeft.size() !=0 || targetsRight.size() != 0){
					target = (targetsLeft.size() !=0) ? targetsLeft.get(0) : targetsRight.get(0);
				//then try to traverse the taps
				}else if(!lavas.containsKey(new Coordinate(currentPosition.x-3,currentPosition.y)) && !walls.containsKey(new Coordinate(currentPosition.x-3,currentPosition.y))){
					target = new Coordinate(currentPosition.x-2,currentPosition.y);
				}else{
					for(int i=currentPosition.y+1;i<=currentPosition.y+3;i++){
						if(!lavas.containsKey(new Coordinate(currentPosition.x-3,i)) && !walls.containsKey(new Coordinate(currentPosition.x-3,i))){
							if(i>currentPosition.y && (walls.containsKey(new Coordinate(currentPosition.x-2,i)) || walls.containsKey(new Coordinate(currentPosition.x-1,i))))
								break;
							targetsRight.add(new Coordinate(currentPosition.x-2,i));
						}
					}
					for(int i=currentPosition.y-1;i>=currentPosition.y-3;i--){
						if(!lavas.containsKey(new Coordinate(currentPosition.x-3,i)) && !walls.containsKey(new Coordinate(currentPosition.x-3,i))){
							if(i<currentPosition.y && (walls.containsKey(new Coordinate(currentPosition.x-2,i)) || walls.containsKey(new Coordinate(currentPosition.x-1,i))))
								break;
							targetsLeft.add(new Coordinate(currentPosition.x-2,i));
						}
					}
					//if there is no safety paths on each side, just go straight
					if(targetsLeft.size() !=0 || targetsRight.size() != 0){
						target = (targetsLeft.size() !=0) ? targetsLeft.get(0) : targetsRight.get(0);
					}else{
						target = new Coordinate(currentPosition.x-2,currentPosition.y);
					}
				}
			break;
			case SOUTH:
				for(int i=currentPosition.x-1;i>=currentPosition.x-3;i--){
					if(!lavas.containsKey(new Coordinate(i,currentPosition.y-2)) && !walls.containsKey(new Coordinate(i,currentPosition.y-2))){
						if(i<currentPosition.x && walls.containsKey(new Coordinate(i,currentPosition.y-1)))
							break;
						targetsRight.add(new Coordinate(i,currentPosition.y-1));
					}
				}
				for(int i=currentPosition.x+1;i<=currentPosition.x+3;i++){
					if(!lavas.containsKey(new Coordinate(i,currentPosition.y-2)) && !walls.containsKey(new Coordinate(i,currentPosition.y-2))){
						if(i>currentPosition.x && walls.containsKey(new Coordinate(i,currentPosition.y-1)))
							break;
						targetsLeft.add(new Coordinate(i,currentPosition.y-1));
					}
				}
				//If we can avoid the traps
				if(targetsLeft.size() !=0 || targetsRight.size() != 0){
					target = (targetsLeft.size() !=0) ? targetsLeft.get(0) : targetsRight.get(0);
				//then try to traverse the taps
				}else if(!lavas.containsKey(new Coordinate(currentPosition.x,currentPosition.y-3)) && !walls.containsKey(new Coordinate(currentPosition.x,currentPosition.y-3))){
					target = new Coordinate(currentPosition.x,currentPosition.y-2);
				}else{
					for(int i=currentPosition.x-1;i>=currentPosition.x-3;i--){
						if(!lavas.containsKey(new Coordinate(i,currentPosition.y-3)) && !walls.containsKey(new Coordinate(i,currentPosition.y-3))){
							if(i<currentPosition.x && (walls.containsKey(new Coordinate(i,currentPosition.y-2)) || walls.containsKey(new Coordinate(i,currentPosition.y-1))))
								break;
							targetsRight.add(new Coordinate(i,currentPosition.y-2));
						}
					}
					for(int i=currentPosition.x+1;i<=currentPosition.x+3;i++){
						if(!lavas.containsKey(new Coordinate(i,currentPosition.y-3)) && !walls.containsKey(new Coordinate(i,currentPosition.y-3))){
							if(i>currentPosition.x && (walls.containsKey(new Coordinate(i,currentPosition.y-2)) || walls.containsKey(new Coordinate(i,currentPosition.y-1))))
								break;
							targetsLeft.add(new Coordinate(i,currentPosition.y-2));
						}
					}
					//if there is no safety paths on each side, just go straight
					if(targetsLeft.size() !=0 || targetsRight.size() != 0){
						target = (targetsLeft.size() !=0) ? targetsLeft.get(0) : targetsRight.get(0);
					}else{
						target = new Coordinate(currentPosition.x,currentPosition.y-2);
					}
				}
			break;
			}
		return target;
	}
}