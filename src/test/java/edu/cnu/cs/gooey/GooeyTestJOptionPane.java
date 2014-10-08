package edu.cnu.cs.gooey; /**
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

import static org.junit.Assert.*;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.junit.Test;

import edu.cnu.cs.gooey.Gooey;
import edu.cnu.cs.gooey.GooeyDialog;

public class GooeyTestJOptionPane {

	// JOptionPane: Message 
	private static class MainClassJOptionPaneMessageDialog {
		public static void main(String[] args) {
			JOptionPane.showMessageDialog( null, "Hello World" );
		}
	}
	@Test
	public void testJOptionPaneMessageDialogDisplayed() {
		Gooey.capture(
			new GooeyDialog() {
				@Override
				public void invoke() {
					MainClassJOptionPaneMessageDialog.main( null );
				}
				@Override
				public void handle(JDialog dialog) {
					assertTrue  ( "JDialog should be displayed", dialog.isShowing() );
					assertEquals( "Incorrect title", "Message",  dialog.getTitle() );

					Gooey.getLabel ( dialog, "Hello World" );
					
					JButton ok = Gooey.getButton( dialog, "OK" );

					List<JButton> count = Gooey.getComponents( dialog, JButton.class );
					assertEquals ( "No buttons other than OK should exist", 1, count.size() );

					ok.doClick();
					assertFalse( "JDialog should be hidden", dialog.isShowing() );
				}
			});
	}

	// JOptionPane: Confirm
	private static class MainClassJOptionPaneConfirmDialog {
		public static void main(String[] args) {
			JOptionPane.showConfirmDialog( null, "What to do?", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION );
		}
	}
	@Test
	public void testJOptionPaneConfirmDialogDisplayed() {
		Gooey.capture(
			new GooeyDialog() {
				@Override
				public void invoke() {
					MainClassJOptionPaneConfirmDialog.main( null );
				}
				@Override
				public void handle(JDialog dialog) {
					assertTrue  ( "JDialog should be displayed", dialog.isShowing() );
					assertEquals( "Incorrect title", "Confirm",  dialog.getTitle() );

					Gooey.getLabel( dialog, "What to do?" );

					Gooey.getButton( dialog, "Yes" );
					Gooey.getButton( dialog, "No" );
					JButton cancel = Gooey.getButton( dialog, "Cancel" );

					List<JButton> count = Gooey.getComponents( dialog, JButton.class );
					assertEquals ( "No buttons other than YES/NO/CANCEL should exist", 3, count.size() );

					cancel.doClick();
					assertFalse( "JDialog should be hidden", dialog.isShowing() );
				}
			});
	}

	// JOptionPane: Input 
	private static class MainClassJOptionPaneInputDialog {
		public static void main(String[] args) {
			JOptionPane.showInputDialog( null, "Type your name", "Please", JOptionPane.OK_CANCEL_OPTION );
		}
	}
	@Test
	public void testJOptionPaneInputDialogDisplayed() {
		Gooey.capture(
			new GooeyDialog() {
				@Override
				public void invoke() {
					MainClassJOptionPaneInputDialog.main( null );
				}
				@Override
				public void handle(JDialog dialog) {
					assertTrue  ( "JDialog should be displayed", dialog.isShowing() );
					assertEquals( "Incorrect title", "Please",   dialog.getTitle() );

					Gooey.getLabel( dialog, "Type your name" );

					JTextField field  = Gooey.getComponent     ( dialog, JTextField.class );
					assertEquals( "Text field should be empty", "", field.getText() );
					List<JTextField> fields = Gooey.getComponents( dialog, JTextField.class );
					assertEquals ( "Only 1 text field should exist", 1, fields.size() );

					Gooey.getButton( dialog, "OK" );
					JButton    cancel = Gooey.getButton( dialog, "Cancel" );

					List<JButton> count = Gooey.getComponents( dialog, JButton.class );
					assertEquals ( "No buttons other than OK/CANCEL should exist", 2, count.size() );

					cancel.doClick();
					assertFalse( "JDialog should be hidden", dialog.isShowing() );
				}
			});
	}
}
