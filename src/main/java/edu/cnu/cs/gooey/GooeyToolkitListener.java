/**
 * <p>Copyright: Copyright (c) 2013, JoSE Group, Christopher Newport University. 
 * Permission to use, copy, modify, distribute and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice appear in all copies and that both that copyright
 * notice and this permission notice appear in supporting documentation.  
 * The JoSE Group makes no representations about the suitability
 * of  this software for any purpose. It is provided "as is" without express
 * or implied warranty.</p>
 * <p>Company: JoSE Group, Christopher Newport University</p>
 */

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
