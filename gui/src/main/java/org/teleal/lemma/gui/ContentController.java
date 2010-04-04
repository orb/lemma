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

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PagePanel;
import org.teleal.common.swingfwk.AbstractController;
import org.teleal.common.swingfwk.Controller;
import org.teleal.common.swingfwk.DefaultEvent;
import org.teleal.common.swingfwk.DefaultEventListener;
import org.teleal.common.swingfwk.Event;
import org.teleal.common.swingfwk.EventListener;
import org.teleal.lemma.gui.events.RenderFileEvent;
import org.teleal.lemma.gui.events.RenderNextPageEvent;
import org.teleal.lemma.gui.events.RenderPreviousPageEvent;
import org.teleal.lemma.renderer.javadoc.XHTMLTemplateJavadocRenderer;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.logging.Logger;

/**
 * @author Christian Bauer
 */
public class ContentController extends AbstractController<PagePanel> {

    final private Logger log = Logger.getLogger(ContentController.class.getName());

    protected PDFFile content;

    public ContentController(Controller parentController) {
        super(new PagePanel(), parentController);

        registerEventListener(RenderFileEvent.class, new DefaultEventListener<File>() {
            public void handleEvent(final DefaultEvent<File> e) {
                new Thread() {
                    @Override
                    public void run() {
                        display(e.getPayload());
                    }
                }.start();
            }
        });

        registerEventListener(RenderPreviousPageEvent.class, new EventListener() {
            public void handleEvent(Event event) {
                if (getView().getPage() != null) {
                    int currentPageNum = getView().getPage().getPageNumber();
                    if (currentPageNum > 1) {
                        display(content, currentPageNum - 1);
                    }
                }
            }
        });

        registerEventListener(RenderNextPageEvent.class, new EventListener() {
            public void handleEvent(Event event) {
                if (getView().getPage() != null) {
                    int currentPageNum = getView().getPage().getPageNumber();
                    if (content.getNumPages() > currentPageNum) {
                        display(content, currentPageNum + 1);
                    }
                }
            }
        });

        getView().useZoomTool(true);
        getView().setBackground(Color.WHITE);
    }

    public PDFFile getContent() {
        return content;
    }

    protected void display(File file) {
        try {
            XHTMLTemplateJavadocRenderer renderer =
                    new XHTMLTemplateJavadocRenderer(LemmaMainController.INSTANCE.getOptions());
            Document dom = renderer.generateDOM(file);
            content = renderer.generatePDF(dom, file.getParentFile().toURI());
            display(content, 1);
        } catch (Exception ex) {
            log.warning("Error rendering file: " + file + " - " + ex.toString());
        }
    }

    protected void display(final PDFFile pdf, final int pageNum) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getView().showPage(pdf.getPage(pageNum));
            }
        });
    }



}
