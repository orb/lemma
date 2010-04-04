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

package org.teleal.lemma.reader;

import org.teleal.common.xhtml.XHTML;
import org.teleal.common.xhtml.XHTMLElement;
import org.teleal.common.xhtml.XHTMLParser;
import org.teleal.common.xml.ParserException;
import org.teleal.lemma.Constants;

import javax.xml.xpath.XPath;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Provides shared operations for reading and wrapping citation content.
 *
 * @author Christian Bauer
 */
public abstract class AbstractReader implements Reader {

    final private XHTMLParser parser = new XHTMLParser();
    final private XPath xpath;

    protected AbstractReader() {
        this.xpath = parser.createXPath();
    }

    public XHTMLParser getParser() {
        return parser;
    }

    public XPath getXPath() {
        return xpath;
    }

    /**
     * Resolves a file with the given path.
     * <p>
     * First, the given path is resolved against the given base directory (looked up in
     * the context). If no file can be found in this base directory that matches the path, a
     * classpath lookup is attempted.
     * </p>
     *
     * @param path The path of the file to be resolved.
     * @param baseDirectory The base directory of any file.
     * @return The found file.
     */
    protected File resolveFile(String path, File baseDirectory) {
        File file = new File(baseDirectory, path);
        if (!file.canRead()) {
            // Try the classpath
            URL url = Thread.currentThread().getContextClassLoader().getResource(path);
            try {
                if (url != null)
                    file = new File(url.toURI());
            } catch (URISyntaxException e) {
                // Ignore
            }
        }
        if (!file.canRead()) {
            throw new RuntimeException("Referenced file not found in base directory or classpath: " + path);
        }
        return file;
    }

    /**
     * Appends a new child element to the given element, wrapping the title string.
     *
     * @param parent The parent element to which the title child element is appended.
     * @param titleString The title string, any XHTML elements within will be parsed.
     */
    protected void appendTitle(XHTMLElement parent, String titleString) {
        if (titleString == null) return;
        try {
            String wrappedTitle = XHTMLParser.wrap(Constants.WRAPPER_ELEMENT.name(), XHTML.NAMESPACE_URI, titleString);
            XHTML titleDom = getParser().parse(wrappedTitle, false);

            titleDom.getRoot(getXPath()).setAttribute(XHTML.ATTR.CLASS, Constants.TYPE_TITLE);
            parent.appendChild(titleDom.getRoot(getXPath()), false);

        } catch (ParserException ex) {
            throw new RuntimeException("Can't parse title: " + titleString, ex);
        }
    }

}
