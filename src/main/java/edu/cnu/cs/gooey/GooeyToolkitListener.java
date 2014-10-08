package edu.cnu.cs.gooey;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;

public class GooeyToolkitListener implements AWTEventListener {
	public static final int TIMEOUT = 5000;

	public interface EventCriteria {
		boolean isAccepted(Object obj, AWTEvent event);
	}

	private Object        target;
	private EventCriteria criteria;
	
	public synchronized void setCriteria(EventCriteria theCriteria) {
		target   = null;
		criteria = theCriteria;
	}
	public synchronized Object getTarget() {
		int elapsed = 0;
		while (target == null && elapsed < TIMEOUT) {
			synchronized(Thread.currentThread()) {
				try {
					Thread.currentThread().wait( 10 );
					elapsed += 10;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Object result = target;
		target = null;
		return result;
	}
	@Override
	public void eventDispatched(AWTEvent event) {
		if (criteria != null) {
			Object source = event.getSource();
			if (criteria.isAccepted( source, event )) {
				target   = source;
				criteria = null;
			}
//			System.out.printf( "[%s] %s\n", ((Component)event.getSource()).getName(), event );
		}
	}			
}
