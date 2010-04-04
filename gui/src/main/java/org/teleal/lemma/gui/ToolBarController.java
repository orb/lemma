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

import org.teleal.common.swingfwk.AbstractController;
import org.teleal.common.swingfwk.ActionButton;
import org.teleal.common.swingfwk.Controller;
import org.teleal.common.swingfwk.Event;
import org.teleal.lemma.gui.events.RenderNextPageEvent;
import org.teleal.lemma.gui.events.RenderPreviousPageEvent;

import javax.swing.*;

/**
 * @author Christian Bauer
 */
public class ToolBarController extends AbstractController<JToolBar> {

    private final ActionButton previousPageButton =
            new ActionButton("Previous Page", Lemma.createImageIcon("img/32/previous.png"), "previousPage") {
                @Override
                public Event createDefaultGlobalEvent() {
                    return new RenderPreviousPageEvent();
                }
            }.enableDefaultEvents(this);

    private final ActionButton nextPageButton =
            new ActionButton("Next Page", Lemma.createImageIcon("img/32/next.png"), "nextPage") {
                @Override
                public Event createDefaultGlobalEvent() {
                    return new RenderNextPageEvent();
                }
            }.enableDefaultEvents(this);


    public ToolBarController(Controller parentController) {
        super(new JToolBar(), parentController);

        previousPageButton.setHorizontalTextPosition(SwingConstants.CENTER);
        previousPageButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        getView().add(previousPageButton);

        nextPageButton.setHorizontalTextPosition(SwingConstants.CENTER);
        nextPageButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        getView().add(nextPageButton);
    }
}