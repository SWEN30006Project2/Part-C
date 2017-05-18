package mycontroller;

public class StrategyFactory {
	private static StrategyFactory instance;
	TurningStrategyAdapter turningStrategyAdapter;
	DetectStrategyAdapter detectStrategyAdapter;
	AvoidingStrategyAdapter avoidingStrategyAdapter;
	
	public static StrategyFactory getInstance(){
		if(instance == null)
			instance = new StrategyFactory();
		return instance;
	}
	
	public TurningStrategyAdapter getTurningStrategyAdapter(String className){
		//if(turningStrategyAdapter == null)
			try {
				turningStrategyAdapter = (TurningStrategyAdapter) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return turningStrategyAdapter;
	}
	
	public DetectStrategyAdapter getDetectStrategyAdapter(String className){
		//if(detectStrategyAdapter == null)
			try {
				detectStrategyAdapter = (DetectStrategyAdapter) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return detectStrategyAdapter;
	}
	
	public AvoidingStrategyAdapter getAvoidingStrategyAdapter(String className){
		//if(detectStrategyAdapter == null)
			try {
				avoidingStrategyAdapter = (AvoidingStrategyAdapter) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return avoidingStrategyAdapter;
	}
}
