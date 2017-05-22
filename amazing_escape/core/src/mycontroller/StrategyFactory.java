package mycontroller;

public class StrategyFactory {
	private static StrategyFactory instance;
	TurningStrategy turningStrategy;
	DetectStrategy detectStrategy;
	AvoidingStrategy avoidingStrategy;
	
	public static StrategyFactory getInstance(){
		if(instance == null)
			instance = new StrategyFactory();
		return instance;
	}
	
	public TurningStrategy getTurningStrategy(String className){
		//if(turningStrategyAdapter == null)
			try {
				turningStrategy = (TurningStrategy) Class.forName(className).newInstance();
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
		return turningStrategy;
	}
	
	public DetectStrategy getDetectStrategy(String className){
		if(detectStrategy == null)
			try {
				detectStrategy = (DetectStrategy) Class.forName(className).newInstance();
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
		return detectStrategy;
	}
	
	public AvoidingStrategy getAvoidingStrategy(String className){
		//if(detectStrategyAdapter == null)
			try {
				avoidingStrategy = (AvoidingStrategy) Class.forName(className).newInstance();
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
			
		return avoidingStrategy;
	}
}
