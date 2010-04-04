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

import com.apple.eawt.ApplicationAdapter;
import org.teleal.common.logging.LoggingUtil;
import org.teleal.common.swingfwk.Application;
import org.teleal.common.swingfwk.logging.LogMessage;
import org.teleal.common.swingfwk.logging.LoggingHandler;
import org.teleal.common.util.OS;
import org.teleal.lemma.pipeline.javadoc.XHTMLTemplateJavadocPipeline;

import javax.swing.*;
import java.awt.*;

/**
 * @author Christian Bauer
 */
public class Lemma {

    public static void main(final String[] args) throws Exception {

        // Platform dependent setup
        if (OS.checkForMac()) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Lemma");
            System.setProperty("apple.awt.showGrowBox", "true");
            com.apple.eawt.Application.getApplication().addApplicationListener(new ApplicationAdapter() {
                @Override
                public void handleQuit(com.apple.eawt.ApplicationEvent applicationEvent) {
                    LemmaMainController.INSTANCE.dispose();
                    LemmaMainController.INSTANCE.getView().dispose();
                }

            });
        }

        // Some UI stuff (of course, why would the OS L&F be the default -- too easy?!)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException ex) {
            System.out.println("Unable to load native look and feel");
        }

        // Logging
        LoggingUtil.resetRootHandler(new LoggingHandler() {
            protected void log(LogMessage msg) {
                Lemma.log(msg);
            }
        });

        // Shutdown behavior
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // getSomeService().shutdown();
            }
        });
        AWTExceptionHandler.register();

        // Schedule a job for the EDT, show the applications main GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LemmaMainController.INSTANCE.init(new XHTMLTemplateJavadocPipeline.SharedOptions(args));
                Application.center(LemmaMainController.INSTANCE.getView());
                LemmaMainController.INSTANCE.getView().setVisible(true);
                LemmaMainController.INSTANCE.start();
            }
        });

    }

    public static void log(LogMessage message) {
        LemmaMainController.INSTANCE.getLogController().pushMessage(message);
    }

    public static LemmaMainController getRootController() {
        return LemmaMainController.INSTANCE;
    }

    public static Frame getRootView() {
        return LemmaMainController.INSTANCE.getView();
    }

    public static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = Lemma.class.getResource(path); // Get stuff relative to this class
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            throw new RuntimeException("Couldn't find image icon on path: " + path);
        }
    }

    public static ImageIcon createImageIcon(String path) {
        return createImageIcon(path, null);
    }

    public static class AWTExceptionHandler {

        public static void register() {
            System.setProperty("sun.awt.exception.handler", AWTExceptionHandler.class.getName());
        }

        public void handle(Throwable ex) {
            System.err.println("===================== The application threw an unhandled exception, exiting.... ==============================");
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

}

