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
import org.teleal.common.swingfwk.Application;
import org.teleal.common.swingfwk.DefaultEvent;
import org.teleal.common.swingfwk.DefaultEventListener;
import org.teleal.common.swingfwk.logging.LogController;
import org.teleal.lemma.gui.events.RenderFileEvent;
import org.teleal.lemma.gui.events.RenderNextPageEvent;
import org.teleal.lemma.pipeline.javadoc.XHTMLTemplateJavadocPipeline;

import javax.swing.*;
import java.awt.*;

/**
 * @author Christian Bauer
 */
public class LemmaMainController extends AbstractController<JFrame> {

    final public static LemmaMainController INSTANCE = new LemmaMainController();

    // App configuration
    private XHTMLTemplateJavadocPipeline.SharedOptions options;

    // Action
    public static String[] ACTION_QUIT = {"Quit Lemma", "quit"};

    // Dependencies
    private ContentController contentController;
    private LogController logController;

    // View
    final JToolBar toolBar;
    final private JPanel contentPanel;
    final private JPanel logPanel;
    boolean logPanelVisible = true;

    protected LemmaMainController() {
        super(new JFrame("Lemma"), null);

        // Logging is the first thing we need
        logController = new LemmaLogController(this);
        logPanel = logController.getView();

        // Main content
        contentController = new ContentController(this);
        contentPanel = contentController.getView();

        // Toolbar
        toolBar = new ToolBarController(this).getView();

    }

    public XHTMLTemplateJavadocPipeline.SharedOptions getOptions() {
        return options;
    }

    public LogController getLogController() {
        return logController;
    }

    public void init(XHTMLTemplateJavadocPipeline.SharedOptions options) {

        this.options = options;

        // TODO: Make this a plugin SPI and let the plugin impl decide how text should be detected and rendered
        registerEventListener(
                LemmaLogController.TextExpandEvent.class,
                new DefaultEventListener<String>() {
                    public void handleEvent(DefaultEvent<String> e) {
                        JDialog textDialog = new JDialog(getView());
                        textDialog.setResizable(true);

                        JTextArea textArea = new JTextArea();
                        JScrollPane textPane = new JScrollPane(textArea);
                        textPane.setPreferredSize(new Dimension(500, 400));
                        textDialog.add(textPane);

                        String pretty;
/* TODO
                        if (XML.isXML(e.getPayload())) {
                            pretty = XML.pretty(e.getPayload());
                        } else if (e.getPayload().startsWith("http-get")) {
                            pretty = ModelUtil.commaToNewline(e.getPayload());
                        } else {
                            pretty = e.getPayload();
                        }
*/
                        pretty = e.getPayload();

                        textArea.setEditable(false);
                        textArea.setText(pretty);

                        textDialog.pack();
                        Application.center(textDialog, LemmaMainController.this.getView());
                        textDialog.setVisible(true);
                    }
                }
        );

        toolBar.setMargin(new Insets(10, 10, 10, 10));
        toolBar.setFloatable(false);

        contentPanel.setPreferredSize(new Dimension(800, 50));
        JScrollPane contentPane = new JScrollPane(contentPanel);
        contentPane.setPreferredSize(new Dimension(800, 700));

        logPanel.setFocusable(false);
        logPanel.setPreferredSize(new Dimension(800, 120));

        getView().setMinimumSize(new Dimension(800, 950));
        getView().add(toolBar, BorderLayout.NORTH);
        getView().add(contentPane, BorderLayout.CENTER);
        getView().add(logPanel, BorderLayout.SOUTH);

        getView().addWindowListener(this);
        getView().pack();
        getView().setResizable(true);
    }

    public void start() {
        if (getOptions().xhtmlTemplateFile != null) {
            fireEvent(new RenderFileEvent(options.xhtmlTemplateFile));
        }
    }

    protected void toggleLogPanel() {

        if (!logPanelVisible) {
            getView().add(logPanel, BorderLayout.CENTER);
            logController.getLogTableModel().setPaused(false);
            logPanelVisible = true;
        } else {
            getView().remove(logPanel);
            logController.getLogTableModel().setPaused(true);
            logPanelVisible = false;
        }

        getView().pack();
    }

    @Override
    public void dispose() {
        super.dispose();
        ShutdownWindow.INSTANCE.setVisible(true);
        new Thread() {
            @Override
            public void run() {
                System.exit(0);
            }
        }.start();
    }

    public static class ShutdownWindow extends JWindow {
        final public static JWindow INSTANCE = new ShutdownWindow();

        protected ShutdownWindow() {
            JLabel shutdownLabel = new JLabel("Stopping application, please wait...");
            shutdownLabel.setHorizontalAlignment(JLabel.CENTER);
            getContentPane().add(shutdownLabel);
            setPreferredSize(new Dimension(300, 30));
            pack();
            Application.center(this);

        }
    }
}