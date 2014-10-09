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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GooeyTestJOptionPane {

    @Test
    public void testJOptionPaneMessageDialogDisplayed() {
        Gooey.capture(
                new GooeyDialog() {
                    @Override
                    public void invoke() {
                        MainClassJOptionPaneMessageDialog.main(null);
                    }

                    @Override
                    public void handle(JDialog dialog) {
                        assertTrue("JDialog should be displayed", dialog.isShowing());
                        assertEquals("Incorrect title", "Message", dialog.getTitle());

                        Gooey.getLabel(dialog, "Hello World");

                        JButton ok = Gooey.getButton(dialog, "OK");

                        List<JButton> count = Gooey.getComponents(dialog, JButton.class);
                        assertEquals("No buttons other than OK should exist", 1, count.size());

                        ok.doClick();
                        assertFalse("JDialog should be hidden", dialog.isShowing());
                    }
                });
    }

    @Test
    public void testJOptionPaneConfirmDialogDisplayed() {
        Gooey.capture(
                new GooeyDialog() {
                    @Override
                    public void invoke() {
                        MainClassJOptionPaneConfirmDialog.main(null);
                    }

                    @Override
                    public void handle(JDialog dialog) {
                        assertTrue("JDialog should be displayed", dialog.isShowing());
                        assertEquals("Incorrect title", "Confirm", dialog.getTitle());

                        Gooey.getLabel(dialog, "What to do?");

                        Gooey.getButton(dialog, "Yes");
                        Gooey.getButton(dialog, "No");
                        JButton cancel = Gooey.getButton(dialog, "Cancel");

                        List<JButton> count = Gooey.getComponents(dialog, JButton.class);
                        assertEquals("No buttons other than YES/NO/CANCEL should exist", 3, count.size());

                        cancel.doClick();
                        assertFalse("JDialog should be hidden", dialog.isShowing());
                    }
                });
    }

    @Test
    public void testJOptionPaneInputDialogDisplayed() {
        Gooey.capture(
                new GooeyDialog() {
                    @Override
                    public void invoke() {
                        MainClassJOptionPaneInputDialog.main(null);
                    }

                    @Override
                    public void handle(JDialog dialog) {
                        assertTrue("JDialog should be displayed", dialog.isShowing());
                        assertEquals("Incorrect title", "Please", dialog.getTitle());

                        Gooey.getLabel(dialog, "Type your name");

                        JTextField field = Gooey.getComponent(dialog, JTextField.class);
                        assertEquals("Text field should be empty", "", field.getText());
                        List<JTextField> fields = Gooey.getComponents(dialog, JTextField.class);
                        assertEquals("Only 1 text field should exist", 1, fields.size());

                        Gooey.getButton(dialog, "OK");
                        JButton cancel = Gooey.getButton(dialog, "Cancel");

                        List<JButton> count = Gooey.getComponents(dialog, JButton.class);
                        assertEquals("No buttons other than OK/CANCEL should exist", 2, count.size());

                        cancel.doClick();
                        assertFalse("JDialog should be hidden", dialog.isShowing());
                    }
                });
    }

    // JOptionPane: Message
    private static class MainClassJOptionPaneMessageDialog {
        public static void main(String[] args) {
            JOptionPane.showMessageDialog(null, "Hello World");
        }
    }

    // JOptionPane: Confirm
    private static class MainClassJOptionPaneConfirmDialog {
        public static void main(String[] args) {
            JOptionPane.showConfirmDialog(null, "What to do?", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
        }
    }

    // JOptionPane: Input
    private static class MainClassJOptionPaneInputDialog {
        public static void main(String[] args) {
            JOptionPane.showInputDialog(null, "Type your name", "Please", JOptionPane.QUESTION_MESSAGE);
        }
    }
}
