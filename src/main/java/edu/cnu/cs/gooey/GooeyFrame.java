package edu.cnu.cs.gooey;

import javax.swing.JFrame;

public abstract class GooeyFrame extends GooeyWindow<JFrame> {
	public GooeyFrame() {
		super( JFrame.class );
	}
}
