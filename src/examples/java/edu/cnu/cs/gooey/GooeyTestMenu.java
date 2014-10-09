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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GooeyTestMenu {

    @Test(expected = AssertionError.class)
    public void testMainClassJFrameWithNoMenu() {
        Gooey.capture(
                new GooeyFrame() {
                    @Override
                    public void invoke() {
                        MainClassJFrameWithoutMenu.main(null);
                    }

                    @Override
                    public void handle(JFrame frame) {
                        assertEquals("Incorrect result", "I don't have a MenuBar", frame.getTitle());
                        Gooey.getMenuBar(frame);
                    }
                });
    }

    @Test
    public void testMainClassJFrameWithMenuHasMenus() {
        Gooey.capture(
                new GooeyFrame() {
                    @Override
                    public void invoke() {
                        MainClassJFrameWithMenu.main(null);
                    }

                    @Override
                    public void handle(JFrame frame) {
                        assertEquals("Incorrect result", "I have a MenuBar", frame.getTitle());

                        JMenuBar menubar = Gooey.getMenuBar(frame);
                        JMenu zero = Gooey.getSubMenu(menubar, "zero");
                        JMenu one = Gooey.getSubMenu(menubar, "one");

                        List<JMenu> menus;
                        List<JMenuItem> items;

                        menus = Gooey.getMenus(menubar);
                        assertEquals("Incorrect result", 2, menus.size());
                        assertTrue("Incorrect result", menus.contains(zero));
                        assertTrue("Incorrect result", menus.contains(one));

                        JMenuItem zero1 = Gooey.getMenu(zero, "quit");

                        items = Gooey.getMenus(zero);
                        assertEquals("Incorrect result", 1, items.size());
                        assertTrue("Incorrect result", items.contains(zero1));

                        JMenu one1 = Gooey.getSubMenu(one, "A");
                        JMenu one2 = Gooey.getSubMenu(one, "B");
                        JMenuItem one3 = Gooey.getMenu(one, "nothing");

                        items = Gooey.getMenus(one);
                        assertEquals("Incorrect result", 3, items.size());
                        assertTrue("Incorrect result", items.contains(one1));
                        assertTrue("Incorrect result", items.contains(one2));
                        assertTrue("Incorrect result", items.contains(one3));

                        JMenuItem one11 = Gooey.getMenu(one1, "dialog");

                        items = Gooey.getMenus(one1);
                        assertEquals("Incorrect result", 1, items.size());
                        assertTrue("Incorrect result", items.contains(one11));

                        JMenuItem one21 = Gooey.getMenu(one2, "exception");

                        items = Gooey.getMenus(one2);
                        assertEquals("Incorrect result", 1, items.size());
                        assertTrue("Incorrect result", items.contains(one21));

                        frame.dispose();
                    }
                });
    }

    @Test
    public void testMainClassJFrameWithMenuQuits() {
        Gooey.capture(
                new GooeyFrame() {
                    @Override
                    public void invoke() {
                        MainClassJFrameWithMenu.main(null);
                    }

                    @Override
                    public void handle(JFrame frame) {
                        JMenuBar menubar = Gooey.getMenuBar(frame);
                        JMenu zero = Gooey.getSubMenu(menubar, "zero");
                        JMenuItem quit = Gooey.getMenu(zero, "quit");

                        assertTrue("Incorrect result", frame.isShowing());
                        quit.doClick();
                        assertFalse("Incorrect result", frame.isShowing());
                    }
                });
    }

    @Test
    public void testMainClassJFrameWithMenuShowsDialog() {
        Gooey.capture(
                new GooeyFrame() {
                    @Override
                    public void invoke() {
                        MainClassJFrameWithMenu.main(null);
                    }

                    @Override
                    public void handle(JFrame frame) {
                        JMenuBar menubar = Gooey.getMenuBar(frame);
                        JMenu menu = Gooey.getSubMenu(menubar, "one");
                        JMenu submenu = Gooey.getSubMenu(menu, "A");
                        final JMenuItem option = Gooey.getMenu(submenu, "dialog");

                        Gooey.capture(
                                new GooeyDialog() {
                                    @Override
                                    public void invoke() {
                                        option.doClick();
                                    }

                                    @Override
                                    public void handle(JDialog dialog) {
                                        assertEquals("Incorrect result", "Message", dialog.getTitle());
                                        Gooey.getLabel(dialog, "Hello World");
                                        Gooey.getButton(dialog, "OK").doClick();
                                    }
                                });

                        frame.dispose();
                    }
                });
    }

    @Test(expected = RuntimeException.class)
    public void testMainClassJFrameWithMenuThrowsException() {
        Gooey.capture(
                new GooeyFrame() {
                    @Override
                    public void invoke() {
                        MainClassJFrameWithMenu.main(null);
                    }

                    @Override
                    public void handle(JFrame frame) {
                        JMenuBar menubar = Gooey.getMenuBar(frame);
                        JMenu menu = Gooey.getSubMenu(menubar, "one");
                        JMenu submenu = Gooey.getSubMenu(menu, "B");
                        final JMenuItem option = Gooey.getMenu(submenu, "exception");

                        Gooey.capture(
                                new GooeyDialog() {
                                    @Override
                                    public void invoke() {
                                        option.doClick();
                                    }

                                    @Override
                                    public void handle(JDialog dialog) {
                                    }
                                });
                        frame.dispose();
                    }
                });
    }

    @Test(expected = AssertionError.class)
    public void testMainClassJFrameWithMenuDoesNothing() {
        Gooey.capture(
                new GooeyFrame() {
                    @Override
                    public void invoke() {
                        MainClassJFrameWithMenu.main(null);
                    }

                    @Override
                    public void handle(JFrame frame) {
                        JMenuBar menubar = Gooey.getMenuBar(frame);
                        JMenu menu = Gooey.getSubMenu(menubar, "one");
                        final JMenuItem option = Gooey.getMenu(menu, "nothing");

                        Gooey.capture(
                                new GooeyDialog() {
                                    @Override
                                    public void invoke() {
                                        option.doClick();
                                    }

                                    @Override
                                    public void handle(JDialog dialog) {
                                    }
                                });
                        frame.dispose();
                    }
                });
    }

    @Test
    public void testHasMenu() {
        JMenuBar menuBar = Gooey.getMenuBar(new LovesMe());
        JMenu game = Gooey.getSubMenu(menuBar, "Game");
        JMenu help = Gooey.getSubMenu(menuBar, "Help");

        List<JMenu> menus = Gooey.getMenus(menuBar);
        assertEquals("Incorrect result", 2, menus.size());
        assertTrue("Incorrect result", menus.contains(game));
        assertTrue("Incorrect result", menus.contains(help));

        List<JMenuItem> items = Gooey.getMenus(game);
        assertEquals("Incorrect result", 1, items.size());
        assertTrue("Incorrect result", items.contains(Gooey.getMenu(game, "Exit")));

        items = Gooey.getMenus(help);
        assertEquals("Incorrect result", 2, items.size());
        assertTrue("Incorrect result", items.contains(Gooey.getMenu(help, "Solution")));
        assertTrue("Incorrect result", items.contains(Gooey.getMenu(help, "About")));
    }

    @Test
    public void testHasExit() {
        final JMenuBar menuBar = Gooey.getMenuBar(new LovesMe());

        Gooey.capture(new GooeyDialog() {
            @Override
            public void invoke() {
                JMenu game = Gooey.getSubMenu(menuBar, "Game");
                Gooey.getMenu(game, "Exit").doClick();
            }

            @Override
            public void handle(JDialog dialog) {
                assertEquals("Incorrect result", "Exit", dialog.getTitle());
                Gooey.getButton(dialog, "No").doClick();
            }
        });
        Gooey.capture(new GooeyDialog() {
            @Override
            public void invoke() {
                JMenu game = Gooey.getSubMenu(menuBar, "Game");
                Gooey.getMenu(game, "Exit").doClick();
            }

            @Override
            public void handle(JDialog dialog) {
                assertEquals("Incorrect result", "Exit", dialog.getTitle());
                Gooey.getButton(dialog, "Yes").doClick();
            }
        });
    }

    @Test
    public void testHasSolution() {
        final JMenuBar menuBar = Gooey.getMenuBar(new LovesMe());

        Gooey.capture(new GooeyDialog() {
            @Override
            public void invoke() {
                JMenu help = Gooey.getSubMenu(menuBar, "Help");
                Gooey.getMenu(help, "Solution").doClick();
            }

            @Override
            public void handle(JDialog dialog) {
                assertEquals("Incorrect result", "Solution", dialog.getTitle());
                Gooey.getButton(dialog, "OK").doClick();
            }
        });
    }

    @Test
    public void testHasAbout() {
        final JMenuBar menuBar = Gooey.getMenuBar(new LovesMe());

        Gooey.capture(new GooeyDialog() {
            @Override
            public void invoke() {
                JMenu help = Gooey.getSubMenu(menuBar, "Help");
                Gooey.getMenu(help, "About").doClick();
            }

            @Override
            public void handle(JDialog dialog) {
                assertEquals("Incorrect result", "About", dialog.getTitle());
                Gooey.getButton(dialog, "OK").doClick();
            }
        });
    }

    // JFrame without menu
    private static class MainClassJFrameWithoutMenu {
        public static void main(String[] args) {
            JFrame f = new JFrame("I don't have a MenuBar");
            f.setVisible(true);
        }
    }

    // JFrame with menu
    private static class MainClassJFrameWithMenu {
        public static void main(String[] args) {
            final JFrame f = new JFrame("I have a MenuBar");

            JMenuBar bar = new JMenuBar();
            JMenu zero = new JMenu("zero");
            JMenu one = new JMenu("one");
            bar.add(zero);
            bar.add(one);
            JMenuItem zero1 = zero.add("quit");
            JMenu one1 = new JMenu("A");
            one.add(one1);
            JMenuItem one11 = one1.add("dialog");
            JMenu one2 = new JMenu("B");
            one.add(one2);
            JMenuItem one21 = one2.add("exception");
            JMenuItem one3 = one.add("nothing");
            f.setJMenuBar(bar);

            zero1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    f.dispose();
                }
            });
            one11.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    JOptionPane.showMessageDialog(f, "Hello World");
                }
            });
            one21.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    throw new RuntimeException();
                }
            });
            one3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    // faking that a dialog is displayed here.
                }
            });

            f.setVisible(true);
        }
    }

    @SuppressWarnings("serial")
    private static class LovesMe extends JFrame {
        public LovesMe() {
            super("Loves Me, Loves Me Not");

            JMenuBar menubar = new JMenuBar();
            setJMenuBar(menubar);

            JMenu game = new JMenu("Game");
            JMenu help = new JMenu("Help");

            JMenuItem exit = game.add("Exit");
            JMenuItem solution = help.add("Solution");
            JMenuItem about = help.add("About");

            menubar.add(game);
            menubar.add(help);

            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showConfirmDialog(null, "Want to blah?", "Exit", JOptionPane.YES_NO_OPTION);
                }
            });
            solution.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "The solution is: blah", "Solution",
                                                  JOptionPane.INFORMATION_MESSAGE);
                }
            });
            about.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    JOptionPane
                            .showMessageDialog(null, "I made this program", "About", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
    }
}
