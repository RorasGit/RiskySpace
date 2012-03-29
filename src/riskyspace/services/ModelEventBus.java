package riskyspace.services;

import java.util.List;
import java.util.ArrayList;
/**
 * @deprecated 
 */
public enum ModelEventBus {
	INSTANCE;
	
	/*
	 * set true if you want to print events, false if not.
	 */
	private boolean traceable = true;
	
	/*
	 * a list of all the eventHandlers.
	 */
	private final List<ModelEventHandler> eventHandlers = new ArrayList<ModelEventHandler>();
	
	
	public void addHandler(ModelEventHandler handler) {
		eventHandlers.add(handler);
	}
	
	public void removeHandler(ModelEventHandler handler) {
		eventHandlers.remove(handler);
	}
	
	public void publish(ModelEvent evt) {
		if(traceable) {
			System.out.println(evt);
		}
		for(ModelEventHandler eventHandler : eventHandlers) {
			eventHandler.performEvent(evt);
		}
	}
	
}
