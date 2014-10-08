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

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Title: Gooey</p>
 * <p>Description: Gooey is a jUnit test library of static methods for capturing windows and retrieving their components.<br/>
 * Methods in this class are designed to be called in JUnit tests. There
 * are methods to capture displayed windows and query their
 * components. Once a window is captured, methods can retrieve any component by type, by name,
 * and (in the case of buttons, menus, tabs and labels) by displayed text.  
 * </p>
 * @see GooeyWindow interface to access a captured window.
 * @author <a hef="http://www.pcs.cnu.edu/~flores/">Roberto A. Flores</a>
 * @version 0.1
 */
public class Gooey {
	/**
	 * Listener receiving window events from the toolkit (refer to {@link java.awt.Toolkit} for 
	 * details on the handling of GUI components). Listener is indirectly enabled by tests 
	 * expecting that a window will be displayed.
	 */
	private static final GooeyToolkitListener ToolkitListener;
	static {
		 ToolkitListener = new GooeyToolkitListener();
		 Toolkit.getDefaultToolkit().addAWTEventListener( ToolkitListener, AWTEvent.WINDOW_EVENT_MASK );
	}
	
	/**
	 * Utility method to find whether a value is in an array. Used mostly for GooeyFlag values.
	 * @param array array of objects
	 * @param value value sought
	 * @return indicates whether the value was found.
	 */
	private static <T> boolean have(T[] array, T value) {
		for (T a : array) {
			if (a.equals( value )) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Private default (and only) constructor. No instance of Gooey can be created.
	 */
	private Gooey() {
	}
	
	/**
	 * Returns the component held by a tab associated with the given title.  
	 * @param tabPane pane holding the tab.
	 * @param title title of the tab sought.
	 * @return component found.
	 * @throws AssertionError if no tab with the given title is found.
	 */
	public static Component getTab(JTabbedPane tabPane, final String title) {
		for (int i = 0; i < tabPane.getTabCount(); i++) {
			String tabTitle = tabPane.getTitleAt( i );
			if (tabTitle.equals( title )) {
				return tabPane.getTabComponentAt( i );
			}
		}
		throw new AssertionError( "No tab \"" + title + "\" found" );
	}
	/**
	 * Returns the label displaying the given text. The label may be nested within the container. 
	 * @param container container holding the label.
	 * @param text text of the label sought.
	 * @return label found.
	 * @throws AssertionError if no label with the given text is found.
	 */
	public static JLabel getLabel(Container container, final String text) {
		String message = "No label \"" + text + "\" found";
		return getComponent( message, container, new GooeyCriteria() {
			@Override
			public boolean isAccepted(Component obj) {
				if (obj instanceof JLabel) {
					String displayed = ((JLabel) obj).getText();
					if (text.equals( displayed )) {
						return true;
					}
				}
				return false;
			}
		});
	}
	/**
	 * Returns the button displaying the given text. The button may be nested within the container.
	 * @param container container holding the label.
	 * @param text text of the button sought.
	 * @return button found.
	 * @throws AssertionError if no button with the given text is found.
	 */
	public static JButton getButton(Container container, final String text) {
		String message = "No button \"" + text + "\" found";
		return getComponent( message, container, new GooeyCriteria() {
			@Override
			public boolean isAccepted(Component obj) {
				if (obj instanceof JButton) {
					String displayed = ((JButton) obj).getText();
					if (text.equals( displayed )) {
						return true;
					}
				}
				return false;
			}
		});
	}
	/**
	 * Returns the menu bar associated with a frame.
	 * 
	 * @param frame the frame whose menu is requested.
	 * @return the frame's menu bar.
	 * @throws AssertionError if no menu bar is found.
	 */
	public static JMenuBar getMenuBar(JFrame frame) {
		JMenuBar menubar = frame.getJMenuBar();
		if (menubar == null) {
			throw new AssertionError( "No menubar found" );
		}
		return menubar;
	}
	/**
	 * Given a menu bar it returns the menu associated with the given text. Text may represent the label displayed 
	 * or the programmatic name of the menu. To look for a menu by displayed label (default setting) use the 
	 * GooeyFlag.Menu.BY_TEXT option. To look for a menu by name, use the GooeyFlag.Menu.BY_NAME option. If both 
	 * were present BY_TEXT takes precedence over BY_NAME. Menu searches can be made throughout the menu structure
	 * (nested search) or under the provided menu (flat search). Nested search is the default setting. To perform a
	 * nested search use the GooeyFlag.Menu.NESTED. To perform a flat search use the GooeyFlag.Menu.FLAT. If both 
	 * were present NESTED takes precedence over FLAT.
	 * 
	 * @param menubar menu bar holding the menu.
	 * @param text text of the menu sought.
	 * @param flags optional flags for text search (by label, by name) and level search (nested, flat)
	 * @return menu found.
	 * @throws AssertionError if no menu with the given text is found.
	 */
	public static JMenu getSubMenu(JMenuBar menubar, String text, GooeyFlag... flags) {
		return getMenu( menubar.getComponents(), JMenu.class, text, flags );
	}
	/**
	 * Given a menu bar it returns the menu associated with the given text. Text may represent the label displayed 
	 * or the programmatic name of the menu. To look for a menu by displayed label (default setting) use the 
	 * GooeyFlag.Menu.BY_TEXT option. To look for a menu by name, use the GooeyFlag.Menu.BY_NAME option. If both 
	 * were present BY_TEXT takes precedence over BY_NAME. Menu searches can be made throughout the menu structure
	 * (nested search) or under the provided menu (flat search). Nested search is the default setting. To perform a
	 * nested search use the GooeyFlag.Menu.NESTED. To perform a flat search use the GooeyFlag.Menu.FLAT. If both 
	 * were present NESTED takes precedence over FLAT.
	 * 
	 * @param submenu sub-menu holding the sought menu.
	 * @param text text of the menu sought.
	 * @param flags optional flags for text search (by label, by name) and level search (nested, flat)
	 * @return sub-menu found.
	 * @throws AssertionError if no sub-menu with the given text is found.
	 */
	public static JMenu getSubMenu(JMenu submenu, String text, GooeyFlag... flags) {
		return getMenu( submenu.getMenuComponents(), JMenu.class, text, flags );
	}
	/**
	 * Given a sub-menu it returns the menu associated with the given text. Text may represent the label displayed 
	 * or the programmatic name of the menu. To look for a menu by displayed label (default setting) use the 
	 * GooeyFlag.Menu.BY_TEXT option. To look for a menu by name, use the GooeyFlag.Menu.BY_NAME option. If both 
	 * were present BY_TEXT takes precedence over BY_NAME. Menu searches can be made throughout the menu structure
	 * (nested search) or under the provided menu (flat search). Nested search is the default setting. To perform a
	 * nested search use the GooeyFlag.Menu.NESTED. To perform a flat search use the GooeyFlag.Menu.FLAT. If both 
	 * were present NESTED takes precedence over FLAT.
	 * 
	 * @param submenu sub-menu holding the menu.
	 * @param text text of the menu sought.
	 * @param flags (optional) flags for text search (by label, by name) and level search (nested, flat)
	 * @return menu found.
	 * @throws AssertionError if no menu with the given text is found.
	 */
	public static JMenuItem getMenu(JMenu submenu, String text, GooeyFlag... flags) {
		return getMenu( submenu.getMenuComponents(), JMenuItem.class, text, flags );
	}
	/**
	 * Funnel method for public getMenu methods. It creates a criteria based on the given text and the flags
	 * GooeyFlag.Menu.BY_NAME and SEARCH_FLAT. It then searches (breadth first) for a matching menu. 
	 * @param components menu components to evaluate
	 * @param text name or label of the menu sought. 
	 * @param flags (optional) flags for text search (by label, by name) and level search (nested, flat)
	 * @return menu found.
	 * @throws AssertionError if no menu with the given text is found.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends JMenuItem> T getMenu(Component[] components, final Class<T> swing, final String text, GooeyFlag... flags) {
		// create criteria
		final boolean byName   = have( flags, GooeyFlag.MATCH_BY_NAME );
		GooeyCriteria criteria = new GooeyCriteria() {
			@Override
			public boolean isAccepted(Component obj) {
				if (swing.isInstance( obj )) {
					T      item = (T) obj;
					String str  = byName ? item.getName() : item.getText();
					if (str.equals( text )) {
						return true;
					}
				}
				return false;
			}
		};
		// search breadth first
		boolean         goNested = !have( flags, GooeyFlag.SEARCH_FLAT );
		List<Component> toQueue  = Arrays.asList( components );
		List<Component> queue    = new LinkedList<Component>( toQueue );
		while (!queue.isEmpty()) {
			Component c = queue.remove( 0 );
			if (criteria.isAccepted( c )) {
				return (T)c;
			}
			if (goNested && c instanceof JMenu) {
				toQueue = Arrays.asList( ((JMenu) c).getMenuComponents() );
				queue.addAll( toQueue );
			}
		}
		throw new AssertionError( "No menu \"" + text + "\" found" );
	}
	
	/**
	 * Returns a list with top-level sub-menus in the menu bar. Nested sub-menus are not traversed.  
	 * @param menubar menu bar to evaluate. 
	 * @return list of sub-menus in the menu bar.
	 */
	public static List<JMenu> getMenus(JMenuBar menubar) {
		List<JMenu> result = new ArrayList<JMenu>();
		if (menubar != null) {
			for (int i = 0; i < menubar.getMenuCount(); i++) {
				JMenu menu = menubar.getMenu( i );
				result.add( menu );
			}
		}
		return result;
	}
	/**
	 * Returns a list with all menu options in the sub-menu provided. Nested sub-menus are not traversed.
	 * @param submenu sub-menu to evaluate.
	 * @return list of menu options in the sub-menu.
	 */
	public static List<JMenuItem> getMenus(JMenu submenu) {
		List<JMenuItem> result = new ArrayList<JMenuItem>();
		for (Component m : submenu.getMenuComponents()) {
			if (m instanceof JMenuItem) {
				result.add( (JMenuItem) m );
			}
		}
		return result;
	}

	/**
	 * Returns the first component of a class found in a container. Nested components are searched depth first.
	 * @param container container to evaluate.
	 * @param swing class of component sought.
	 * @return component found.
	 * @throws AssertionError if no component of the given class is found.
	 */
	public static <T extends Component> T getComponent(Container container, Class<T> swing) {
		return getComponent( container, swing, null );
	}
	/**
	 * Returns the first component of a class found in a container. If a name is provided (i.e., it's not null) then
	 * the component found will match both the class and name sought.
	 * @param container container to evaluate.
	 * @param swing class of component sought.
	 * @param name name of the component sought. It's not used if null.
	 * @param flags (optional) flags for level search (nested, flat)
	 * @return component found.
	 * @throws AssertionError if no component with the given class and name is found.
	 */
	public static <T extends Component> T getComponent(Container container, final Class<T> swing, final String name, GooeyFlag... flags) {
		String message = "No \""+ swing.getName() +"\" component" + (name == null ? "" : " \'" + name + "\'") + " found";
		return getComponent( message, container, new GooeyCriteria() {
			@Override
			public boolean isAccepted(Component obj) {
                return swing.isInstance(obj) && (name == null || name.equals(((Component) obj).getName()));
            }
		}, flags);
	}
	/**
	 * Returns the first component in a container that matches the given criteria.
	 * @param message assertion message used when no component is found.
	 * @param container container to evaluate.
	 * @param criteria criteria indicating an accepting component.
	 * @param flags (optional) flags for level search (nested, flat).
	 * @return component found.
	 * @throws AssertionError if no component with the given class and name is found.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Component> T getComponent(String message, Container container, GooeyCriteria criteria, GooeyFlag... flags) {
		boolean         goNested = !have( flags, GooeyFlag.SEARCH_FLAT );
		List<Component> toQueue  = Arrays.asList( container.getComponents() );
		List<Component> queue    = new LinkedList<Component>( toQueue );
		while (!queue.isEmpty()) {
			Component c = queue.remove( 0 );
			if (criteria.isAccepted( c )) {
				return (T)c;
			}
			if (goNested && c instanceof Container) {
				toQueue = Arrays.asList( ((Container) c).getComponents() );
				queue.addAll( toQueue );
			}
		}
		throw new AssertionError( message );
	}

	/**
	 * Returns all components of a given class found in a container.
	 * @param container container to evaluate.
	 * @param swing class of components sought.
	 * @return list of components found.
	 */
	public static <T extends Component> List<T> getComponents(Container container, final Class<T> swing) {
		return getComponents( container, new GooeyCriteria() {
			@Override
			public boolean isAccepted(Component obj) {
				return swing.isInstance( obj );
			}
		});
	}
	/**
	 * Returns all components in a container that match the given criteria.
	 * @param container container to evaluate.
	 * @param criteria criteria indicating an accepting component.
	 * @return list of components found.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Component> List<T> getComponents(Container container, GooeyCriteria criteria) {
		List<T>         result  = new ArrayList<T>();
		List<Component> toQueue = Arrays.asList( container.getComponents() );
		List<Component> queue   = new LinkedList<Component>( toQueue );
		while (!queue.isEmpty()) {
			Component c = queue.remove( 0 );
			if (criteria.isAccepted( c )) {
				result.add( (T)c );
			}
			if (c instanceof Container) {
				toQueue = Arrays.asList( ((Container) c).getComponents() );
				queue.addAll( toQueue );
			}
		}
		return result;
	}
	
	/**
	 * Invokes a custom method displaying a window, waits for the window to display (within a timeout period) and invokes 
	 * a method where testing can be performed. 
	 * The parameter doRun is an instance of GooeyWindow with 2 abstract methods: <code>invoke</code> (overridden with the code to
	 * display a window) and <code>handle</code> (overridden with the code to test the window displayed). 
	 * If no window is detected within a waiting period the method throws an AssertionError.  
	 * @param doRun interface to display and handle the test of a window.
	 * @throws AssertionError if no window is displayed.
	 */
	public synchronized static <T extends GooeyWindow<U>, U extends Window> void capture(T doRun) {
		capture( "No window detected", doRun );
	}
	/**
	 * Invokes a custom method displaying a window, waits for the window to display (within a timeout period) and invokes 
	 * a method where testing can be performed. 
	 * The parameter doRun is an instance of GooeyWindow with 2 abstract methods: <code>invoke</code> (overridden with the code to
	 * display a window) and <code>handle</code> (overridden with the code to test the window displayed). 
	 * If no window is detected within a waiting period the method throws an AssertionError.  
	 * @param doRun interface to display and handle the test of a window.
	 * @throws AssertionError if no window is displayed.
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <T extends GooeyWindow<U>, U extends Window> void capture(String message, T doRun) {
		// reset in cases when "doRun" is reused
		doRun.reset();
		// set capture criteria and begin listening
		ToolkitListener.setCriteria( doRun.getEventCriteria() );
		// runs "doRun.invoke" to create window
		SwingUtilities.invokeLater ( doRun );
		// "getTarget" waits until detecting window or timing out
		U   window = (U) ToolkitListener.getTarget();
		if (window != null) {
			doRun.handle( window );
		}
		// wait until doRun.invoke finishes running
		doRun.finish();
		if (window == null) {
			throw new AssertionError( message );
		}
	}
}
