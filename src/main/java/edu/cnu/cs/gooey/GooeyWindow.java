package edu.cnu.cs.gooey;

import edu.cnu.cs.gooey.GooeyToolkitListener.EventCriteria;

import java.awt.*;
import java.awt.event.WindowEvent;

public abstract class GooeyWindow <T extends Window> implements Runnable {
	private GooeyToolkitListener.EventCriteria criteria;
	private RuntimeException                   exception;
	private AssertionError					   assertion;
	private boolean                            done;
	
	protected GooeyWindow(final Class<T> swing) {
		exception = null;
		assertion = null;
		done      = false;
		criteria  = new GooeyToolkitListener.EventCriteria() {
			@Override
			public boolean isAccepted(Object obj, AWTEvent event) {
				if (swing.isInstance( obj )) {
					long id = event.getID();
					if ( id == WindowEvent.WINDOW_OPENED ) {
						return true;
					}
				}
				return false;
			}
		};
	}
	public abstract void invoke();
	public abstract void handle(T window);

	public EventCriteria getEventCriteria() {
		return criteria;
	}

	@Override
	public final void run() {
		try {
			invoke();
		} catch (RuntimeException e) {
			exception = e;
		} catch (AssertionError e) {
			assertion = e;
		}
		finally {
			done = true;
		}
	}
	public void reset() {
		done      = false;
		exception = null;
		assertion = null;
	}
	public final void finish() {
		Thread thread = Thread.currentThread();
		while (!done) {
			synchronized( thread ) {
				try {
					thread.wait( 10 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (exception != null) {
			throw exception;
		}
		if (assertion != null) {
			throw assertion;
		}
	}
}
