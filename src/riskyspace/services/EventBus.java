package riskyspace.services;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author Joachim von Hacht
 * @modified Alexander Hederstaf
 *
 */
public enum EventBus {
	SERVER,
	CLIENT;
	
	/*
	 * set true if you want to print events, false if not.
	 */
	private boolean traceable = true;
	
	/*
	 * a list of all the eventHandlers.
	 */
	private final List<EventHandler> eventHandlers = new ArrayList<EventHandler>();
	
	
	public void addHandler(EventHandler handler) {
		eventHandlers.add(handler);
	}
	
	public void removeHandler(EventHandler handler) {
		eventHandlers.remove(handler);
	}
	
	public void publish(Event evt) {
		if(traceable) {
			System.out.println(evt);
		}
		for(EventHandler eventHandler : eventHandlers) {
			eventHandler.performEvent(evt);
		}
	}
	
}
