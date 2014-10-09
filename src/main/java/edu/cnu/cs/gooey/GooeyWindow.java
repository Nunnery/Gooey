/*
 * This file is part of Gooey, licensed under the ISC License.
 *
 * Copyright (c) 2013 - 2014, JoSE Group, Christopher Newport University
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 */
package edu.cnu.cs.gooey;

import edu.cnu.cs.gooey.GooeyToolkitListener.EventCriteria;

import java.awt.*;
import java.awt.event.WindowEvent;

public abstract class GooeyWindow<T extends Window> implements Runnable {
    private GooeyToolkitListener.EventCriteria criteria;
    private RuntimeException exception;
    private AssertionError assertion;
    private boolean done;

    protected GooeyWindow(final Class<T> swing) {
        exception = null;
        assertion = null;
        done = false;
        criteria = new GooeyToolkitListener.EventCriteria() {
            @Override
            public boolean isAccepted(Object obj, AWTEvent event) {
                if (swing.isInstance(obj)) {
                    long id = event.getID();
                    if (id == WindowEvent.WINDOW_OPENED) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

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
        } finally {
            done = true;
        }
    }

    public abstract void invoke();

    public void reset() {
        done = false;
        exception = null;
        assertion = null;
    }

    public final void finish() {
        Thread thread = Thread.currentThread();
        while (!done) {
            synchronized (thread) {
                try {
                    thread.wait(10);
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
