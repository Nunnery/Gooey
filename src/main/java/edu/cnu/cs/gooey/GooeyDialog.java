package edu.cnu.cs.gooey;

import javax.swing.JDialog;

public abstract class GooeyDialog extends GooeyWindow<JDialog> {
	public GooeyDialog() {
		super( JDialog.class );
	}
}
