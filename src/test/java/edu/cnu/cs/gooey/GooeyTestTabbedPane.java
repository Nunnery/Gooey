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

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GooeyTestTabbedPane {

	@SuppressWarnings("serial")
	private static class TabbedPaneDemo extends JPanel {
		public TabbedPaneDemo() {
			super(new GridLayout(1, 1));

			JTabbedPane tabbedPane = new JTabbedPane();
			// ImageIcon icon = createImageIcon("images/middle.gif");
			ImageIcon icon = null;

			JComponent panel1 = makeTextPanel("Panel #1");
			tabbedPane.addTab("Tab 1", icon, panel1, "Does nothing");
			tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

			JComponent panel2 = makeTextPanel("Panel #2");
			tabbedPane.addTab("Tab 2", icon, panel2, "Does twice as much nothing");
			tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

			JComponent panel3 = makeTextPanel("Panel #3");
			tabbedPane.addTab("Tab 3", icon, panel3, "Still does nothing");
			tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

			JComponent panel4 = makeTextPanel("Panel #4 (has a preferred size of 410 x 50).");
			panel4.setPreferredSize(new Dimension(410, 50));
			tabbedPane.addTab("Tab 4", icon, panel4, "Does nothing at all");
			tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

			// Add the tabbed pane to this panel.
			add(tabbedPane);

			// The following line enables to use scrolling tabs.
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		}

		protected JComponent makeTextPanel(String text) {
			JPanel panel = new JPanel(false);
			JLabel filler = new JLabel(text);
			filler.setHorizontalAlignment(JLabel.CENTER);
			panel.setLayout(new GridLayout(1, 1));
			panel.add(filler);
			return panel;
		}
		public static void main(String[] args) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JFrame frame = new JFrame("TabbedPaneDemo");
					frame.add(new TabbedPaneDemo(), BorderLayout.CENTER);
					frame.pack();
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setVisible(true);
				}
			});
		}
	}

	@Test
	public void test1() {
		Gooey.capture( new GooeyFrame() {
			@Override
			public void invoke() {
				TabbedPaneDemo.main( null );
			}
			@Override
			public void handle(JFrame frame) {
				JTabbedPane tabPane = Gooey.getComponent( frame, JTabbedPane.class );
				assertNotNull( "Incorrect result", tabPane );
				assertEquals ( "Incorrect result", 4, tabPane.getTabCount() );
				assertEquals ( "Incorrect result", 0, tabPane.getSelectedIndex() );
				
				assertEquals ( "Incorrect result", "Tab 1", tabPane.getTitleAt( 0 ));
				assertEquals ( "Incorrect result", "Tab 2", tabPane.getTitleAt( 1 ));
				assertEquals ( "Incorrect result", "Tab 3", tabPane.getTitleAt( 2 ));
				assertEquals ( "Incorrect result", "Tab 4", tabPane.getTitleAt( 3 ));
				
				List<JPanel> panels = Gooey.getComponents( tabPane, JPanel.class );
				assertEquals( "Incorrect result", 5, panels.size());

				assertEquals  ( "Incorrect result", "Does nothing",               tabPane.getToolTipTextAt(0) );
				assertEquals  ( "Incorrect result", "Does twice as much nothing", tabPane.getToolTipTextAt(1) );
				assertEquals  ( "Incorrect result", "Still does nothing",         tabPane.getToolTipTextAt(2) );
				assertEquals  ( "Incorrect result", "Does nothing at all",        tabPane.getToolTipTextAt(3) );

				Gooey.getLabel( panels.get(0), "Panel #1" );
				Gooey.getLabel( panels.get(1), "Panel #2" );
				Gooey.getLabel( panels.get(2), "Panel #3" );
				Gooey.getLabel( panels.get(3), "Panel #4 (has a preferred size of 410 x 50)." );
				
				frame.dispose();
			}
		});
	}
}
