package riskyspace.services;

import java.util.List;
import java.util.ArrayList;

public enum ViewEventBus {
	INSTANCE;
	
	/*
	 * set true if you want to print events, false if not.
	 */
	private boolean traceable = true;
	
	/*
	 * a list of all the eventHandlers.
	 */
	private final List<ViewEventHandler> eventHandlers = new ArrayList<ViewEventHandler>();
	
	
	public void addHandler(ViewEventHandler handler) {
		eventHandlers.add(handler);
	}
	
	public void removeHandler(ViewEventHandler handler) {
		eventHandlers.remove(handler);
	}
	
	public void publish(ViewEvent evt) {
		if(traceable) {
			System.out.println(evt);
		}
		for(ViewEventHandler eventHandler : eventHandlers) {
			eventHandler.preformEvent(evt);
		}
	}
	
}
