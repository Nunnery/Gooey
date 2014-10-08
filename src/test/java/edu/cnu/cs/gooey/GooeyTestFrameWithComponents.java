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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static org.junit.Assert.*;

public class GooeyTestFrameWithComponents {

	// JFrame: Not displayed 
	private static class MainClassJFrameNotDisplayed {
		public static void main(String[] args) {
			new JFrame();
		}
	}
	@Test(expected=AssertionError.class)
	public void testJFrameNotDisplayed() {
		Gooey.capture(
			new GooeyFrame() {
				@Override
				public void invoke() {
					MainClassJFrameNotDisplayed.main( null );
				}
				@Override
				public void handle(JFrame frame) {
					throw new AssertionError( "No frame should be displayed" );
				}
			});
	}

	// JFrame Empty: Displayed 
	private static class MainClassJFrameEmptyDisplayed {
		public static void main(String[] args) {
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					JFrame frame = new JFrame( "My own title" );
					frame.setSize( new Dimension( 150, 200 ));
					frame.setVisible( true );
				}
			});
		}
	}
	@Test
	public void testJFrameEmptyDisplayed() {
		Gooey.capture(
			new GooeyFrame() {
				@Override
				public void invoke() {
					MainClassJFrameEmptyDisplayed.main( null );
				}
				@Override
				public void handle(JFrame frame) {
					assertEquals ( "Incorrect result", "My own title",            frame.getTitle() );
					assertEquals ( "Incorrect result", new Dimension( 150, 200 ), frame.getSize() );
					assertTrue   ( "Incorrect result", frame.isShowing() );
					frame.dispose();
					assertFalse  ( "Incorrect result", frame.isShowing() );
				}
			});
	}

	// JFrame With Components: Displayed 
	@SuppressWarnings("serial")
	private static class MainClassJFrameNotEmptyDisplayed extends JFrame {
		public MainClassJFrameNotEmptyDisplayed() {
			super("FlowLayout Example");
			
			setLayout( new FlowLayout());
			
			JTextField first, middle, last;
			
			add( new JLabel( "First name" ));
			first = new JTextField( 10 );
			first.setName( "name.first" );
			add( first );
			
			add( new JLabel( "MI" ));
			middle = new JTextField( 1 );
			middle.setName( "name.middle" );
			add( middle );
			
			add( new JLabel( "Last name" ));
			last = new JTextField( 10 );
			last.setName( "name.last" );
			add( last );
			
			pack();
		}
		public static void main(String[] args) {
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					JFrame frame = new MainClassJFrameNotEmptyDisplayed();
					frame.setVisible( true );
				}
			});
		}
	}
	@Test
	public void testJFrameDisplayed() {
		Gooey.capture(
			new GooeyFrame() {
				@Override
				public void invoke() {
					MainClassJFrameNotEmptyDisplayed.main( null );
				}
				@Override
				public void handle(JFrame frame) {
					assertEquals ( "Incorrect result", "FlowLayout Example", frame.getTitle() );

					JTextField nameFirst  = Gooey.getComponent( frame, JTextField.class, "name.first" );
					JTextField nameMiddle = Gooey.getComponent( frame, JTextField.class, "name.middle" );
					JTextField nameLast   = Gooey.getComponent( frame, JTextField.class, "name.last" );

					assertTrue   ( "Incorrect result", nameLast  != nameFirst );
					assertTrue   ( "Incorrect result", nameLast  != nameMiddle );
					assertTrue   ( "Incorrect result", nameFirst != nameMiddle );

					List<JTextField> textFields = Gooey.getComponents( frame, JTextField.class );
					assertEquals ( "Incorrect result", 3, textFields.size() );
					assertTrue   ( "Incorrect result",    textFields.contains( nameFirst ));
					assertTrue   ( "Incorrect result",    textFields.contains( nameMiddle ));
					assertTrue   ( "Incorrect result",    textFields.contains( nameLast ));

					assertTrue   ( "Incorrect result", frame.isShowing() );
					frame.dispose();
					assertFalse  ( "Incorrect result", frame.isShowing() );
				}
			});
	}

	// JFrame With Named Components: Displayed 
	@SuppressWarnings("serial")
	private static class BMI extends JFrame {
		private JTextField jtfWeight;
		private JTextField jtfHeight;
		private JButton    jbtGo;
		private JLabel     jlbIndex;
		
		public BMI() {
			super( "Body Mass Index" );
			
			setLayout( new GridLayout( 2, 1 ) );
			
			JPanel first = new JPanel( new FlowLayout() );
			first .add( new JLabel( "Weight (lb)" ));
			first .add( jtfWeight = new JTextField( 10 ));
			first .add( new JLabel( "Height (ft)" ));
			first .add( jtfHeight = new JTextField( 10 ));
			first .add( jbtGo     = new JButton( "Go" ));
			JPanel second = new JPanel( new FlowLayout() );
			second.add( new JLabel( "Index:" ));
			second.add( jlbIndex = new JLabel());
			
			jtfWeight.setName( "weight" );
			jtfHeight.setName( "height" );
			jlbIndex .setName( "index"  );
			
			add( first );
			add( second );
			
			pack();
			
			setDefaultCloseOperation( DISPOSE_ON_CLOSE );
			setLocationRelativeTo( null );
			
			jbtGo.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent anEvent) {
					double  weight = 0;
					double  height = 0;
					boolean ok     = false; 
					try {
						weight = Float.parseFloat( jtfWeight.getText() );
						height = Float.parseFloat( jtfHeight.getText() );
						ok     = weight > 0 && height > 0;
					}
					catch (NumberFormatException e) {
					}
					if (ok) {
						double index = (weight * 4.88) / (height * height);
						jlbIndex.setText( "" + Math.round( index * 100 ) / 100.0 );
					}
					else {
						jlbIndex.setText( "" );
						JOptionPane.showMessageDialog( BMI.this, "Please type positive numbers" );
					}
				}
			});
		}
		public static void main(String[] args) {
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					JFrame f = new BMI();
					f.setVisible( true );
				}
			});
		}
	}
	@Test
	public void testBMI() {
		Gooey.capture(
			new GooeyFrame() {
				@Override
				public void invoke() {
					BMI.main( null );
				}
				@Override
				public void handle(JFrame frame) {
					assertEquals ( "Incorrect result", "Body Mass Index", frame.getTitle() );

					JTextField weight = Gooey.getComponent( frame, JTextField.class, "weight" );
					JTextField height = Gooey.getComponent( frame, JTextField.class, "height" );
					JLabel     index  = Gooey.getComponent( frame, JLabel.class,     "index" );
					JButton    go     = Gooey.getButton   ( frame, "Go" );
					JLabel     lb     = Gooey.getLabel    ( frame, "Weight (lb)" );
					JLabel     ft     = Gooey.getLabel    ( frame, "Height (ft)" );

					assertTrue   ( "Incorrect result", weight != height );

					List<JTextField> textFields = Gooey.getComponents( frame, JTextField.class );
					assertEquals ( "Incorrect result", 2, textFields.size() );
					assertTrue   ( "Incorrect result",    textFields.contains( weight ));
					assertTrue   ( "Incorrect result",    textFields.contains( height ));

					List<JLabel> labels = Gooey.getComponents( frame, JLabel.class );
					assertEquals ( "Incorrect result", 4, labels.size() );
					assertTrue   ( "Incorrect result",    labels.contains( index ));
					assertTrue   ( "Incorrect result",    labels.contains( lb ));
					assertTrue   ( "Incorrect result",    labels.contains( ft ));

					List<JButton> buttons = Gooey.getComponents( frame, JButton.class );
					assertEquals ( "Incorrect result", 1, buttons.size() );
					assertTrue   ( "Incorrect result",    buttons.contains( go ));

					assertEquals ( "Incorrect result", "",     index.getText() );
					weight.setText( "120" );
					height.setText( "5.411" );
					go.doClick();
					assertEquals ( "Incorrect result", "20.0", index.getText() );

					assertTrue   ( "Incorrect result", frame.isShowing() );
					frame.dispose();
					assertFalse  ( "Incorrect result", frame.isShowing() );
				}
			});
	}

	// JFrame that displays a dialog 
	@SuppressWarnings("serial")
	private static class MainClassJFrameDisplaysMessageDialog extends JFrame {
		public MainClassJFrameDisplaysMessageDialog() {
			super();
			
			setLayout( new FlowLayout() );
			
			JButton jbtGo = new JButton( "Go" );
			add(    jbtGo );
			
			pack();
			
			setDefaultCloseOperation( DISPOSE_ON_CLOSE );
			setLocationRelativeTo( null );
			
			jbtGo.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent anEvent) {
					JOptionPane.showMessageDialog( MainClassJFrameDisplaysMessageDialog.this, "Yeah" );
				}
			});
		}
		public static void main(String[] args) {
			JFrame f = new MainClassJFrameDisplaysMessageDialog();
			f.setVisible( true );
		}
	}
	@Test
	public void testMainClassJFrameDisplaysMessageDialogOnce() {
		Gooey.capture(
			new GooeyFrame() {
				@Override
				public void invoke() {
					MainClassJFrameDisplaysMessageDialog.main( null );
				}
				@Override
				public void handle(JFrame frame) {
					assertTrue   ( "Incorrect result",     frame.isShowing() );
					assertEquals ( "Incorrect result", "", frame.getTitle() );

					final JButton go = Gooey.getButton( frame, "Go" );

					List<JButton> buttons = Gooey.getComponents( frame, JButton.class );
					assertEquals ( "Incorrect result", 1, buttons.size() );
					assertTrue   ( "Incorrect result",    buttons.contains( go ));

					// press GO once
					Gooey.capture(
						new GooeyDialog() {
							@Override
							public void invoke() {
								go.doClick();
							}
							@Override
							public void handle(JDialog dialog) {
								assertTrue ( "dialog should be showing", dialog.isShowing() );
								Gooey.getButton( dialog, "OK" ).doClick();
								assertFalse( "dialog should be hidden",  dialog.isShowing() );
							}
						});
					// dispose frame
					assertTrue ( "Incorrect result", frame.isShowing() );
					frame.dispose();
					assertFalse( "Incorrect result", frame.isShowing() );
				}
			});
	}
	@Test
	public void testMainClassJFrameDisplaysMessageDialogTwice() {
		Gooey.capture(
			new GooeyFrame() {
				@Override
				public void invoke() {
					MainClassJFrameDisplaysMessageDialog.main( null );
				}
				@Override
				public void handle(JFrame frame) {
					assertTrue   ( "Incorrect result",     frame.isShowing() );
					assertEquals ( "Incorrect result", "", frame.getTitle() );

					final JButton go = Gooey.getButton( frame, "Go" );

					List<JButton> buttons = Gooey.getComponents( frame, JButton.class );
					assertEquals ( "Incorrect result", 1, buttons.size() );
					assertTrue   ( "Incorrect result",    buttons.contains( go ));

					// press GO five times in sequence
					for (int i = 0; i < 5; i++) {
						Gooey.capture(
							new GooeyDialog() {
								@Override
								public void invoke() {
									go.doClick();
								}
								@Override
								public void handle(JDialog dialog) {
									assertTrue ( "dialog should be showing", dialog.isShowing() );
									Gooey.getButton( dialog, "OK" ).doClick();
									assertFalse( "dialog should be hidden",  dialog.isShowing() );
								}
							});
						assertTrue( "Incorrect result", frame.isShowing() );
					}
					// dispose frame
					assertTrue  ( "Incorrect result", frame.isShowing() );
					frame.dispose();
					assertFalse ( "Incorrect result", frame.isShowing() );
				}
			});
	}
	
	// JFrame that displays itself (potentially many times) 
	private static class MainClassJFrameDisplaysItself {
		public static void main(String[] args) {
			JFrame f = new JFrame();
			f.setLayout( new FlowLayout() );
			
			JButton jbtGo = new JButton( "Go" );
			f.add(  jbtGo );
			
			f.pack();
			
			jbtGo.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent anEvent) {
					SwingUtilities.invokeLater( new Runnable() {
						@Override
						public void run() {
							main( new String[]{ } );
						}
					});
				}
			});
			f.setVisible( true );
		}
	}
	@Test
	public void testMainClassJFrameDisplayItself() {
		Gooey.capture(
			new GooeyFrame() {
				@Override
				public void invoke() {
					MainClassJFrameDisplaysItself.main( null );
				}
				@Override
				public void handle(JFrame frame) {
					clickGo( frame, 5 );
				}
				private void clickGo(JFrame frame, final int clicksLeft) {
					if (clicksLeft > 0) {
						assertTrue   ( "Incorrect result",     frame.isShowing() );
						assertEquals ( "Incorrect result", "", frame.getTitle() );

						final JButton go = Gooey.getButton( frame, "Go" );

						List<JButton> buttons = Gooey.getComponents( frame, JButton.class );
						assertEquals ( "Incorrect result", 1, buttons.size() );
						assertTrue   ( "Incorrect result",    buttons.contains( go ));

						Gooey.capture(
							new GooeyFrame() {
								@Override
								public void invoke() {
									go.doClick();
								}
								@Override
								public void handle(JFrame frame) {
									clickGo( frame, clicksLeft -1 );
								}										
							});
						frame.dispose();
						assertFalse( "Incorrect result", frame.isShowing() );
					}
				}
			});
	}
	@Test(expected=RuntimeException.class)
	public void testMainClassJFrameDisplayItselfSeveralTimesBeforeThrowingException() {
		Gooey.capture(
			new GooeyFrame() {
				@Override
				public void invoke() {
					MainClassJFrameDisplaysItself.main( null );
				}
				@Override
				public void handle(JFrame frame) {
					clickGo( frame, 5 );
				}
				private void clickGo(JFrame frame, final int clicksLeft) {
					if (clicksLeft > 0) {
						assertTrue   ( "Incorrect result",     frame.isShowing() );
						assertEquals ( "Incorrect result", "", frame.getTitle() );

						final JButton   go      = Gooey.getButton( frame, "Go" );
						List<JButton> buttons = Gooey.getComponents( frame, JButton.class );
						assertEquals ( "Incorrect result", 1, buttons.size() );
						assertTrue   ( "Incorrect result",    buttons.contains( go ));

						Gooey.capture(
							new GooeyFrame() {
								@Override
								public void invoke() {
									go.doClick();
								}
								@Override
								public void handle(JFrame frame) {
									clickGo( frame, clicksLeft -1 );
								}										
							});
						frame.dispose();
						assertFalse( "Incorrect result", frame.isShowing() );
					}
					else {
						throw new RuntimeException();
					}
				}
			});
	}
}
