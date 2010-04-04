/*
 * Copyright (C) 2010 Teleal GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teleal.lemma.gui;

import org.teleal.common.swingfwk.Controller;
import org.teleal.common.swingfwk.DefaultEvent;
import org.teleal.common.swingfwk.logging.LogController;
import org.teleal.common.swingfwk.logging.LogMessage;
import org.teleal.lemma.gui.Lemma;

import javax.swing.*;
import java.awt.*;

/**
 * @author Christian Bauer
 */
public class LemmaLogController extends LogController {

    public LemmaLogController(Controller parentController) {
        super(parentController, new DefaultLogCategories());
    }

    protected void expand(LogMessage message) {
        fireEventGlobal(
                new TextExpandEvent(message.getMessage())
        );
    }

    protected Frame getParentWindow() {
        return Lemma.getRootView();
    }

    protected JButton createConfigureButton() {
        return new JButton("Options...", Lemma.createImageIcon("img/16/configure.png"));
    }

    protected JButton createClearButton() {
        return new JButton("Clear Log", Lemma.createImageIcon("img/16/removetext.png"));
    }

    protected JButton createCopyButton() {
        return new JButton("Copy", Lemma.createImageIcon("img/16/copyclipboard.png"));
    }

    protected JButton createExpandButton() {
        return new JButton("Expand", Lemma.createImageIcon("img/16/viewtext.png"));
    }

    protected JButton createPauseButton() {
        return new JButton("Pause/Continue Log", Lemma.createImageIcon("img/16/pause.png"));
    }

    protected ImageIcon getWarnErrorIcon() {
        return Lemma.createImageIcon("img/16/warn.png");
    }

    protected ImageIcon getDebugIcon() {
        return Lemma.createImageIcon("img/16/debug.png");
    }

    protected ImageIcon getTraceIcon() {
        return Lemma.createImageIcon("img/16/trace.png");
    }

    protected ImageIcon getInfoIcon() {
        return Lemma.createImageIcon("img/16/info.png");
    }

    public class TextExpandEvent extends DefaultEvent<String> {
        public TextExpandEvent(String s) {
            super(s);
        }
    }

}
