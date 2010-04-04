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

package org.teleal.lemma.reader.javacode;

import org.teleal.common.xhtml.XHTML;
import org.teleal.common.xhtml.XHTMLElement;
import org.teleal.lemma.Constants;
import org.teleal.lemma.anchor.CitationAnchor;
import org.teleal.lemma.pipeline.Context;
import org.teleal.lemma.reader.content.filter.ContentFilter;

import java.io.File;
import java.util.logging.Logger;

/**
 * Reads raw Java source content, handles <code>file://MyClass.java</code> URIs.
 * <p>
 * Uses handler, printer, filter of {@link org.teleal.lemma.reader.javacode.JavacodeReader}.
 * </p>
 *
 * @author Christian Bauer
 */
public class JavacodeRawReader extends JavacodeReader {

    final private Logger log = Logger.getLogger(JavacodeRawReader.class.getName());

    final public static String CONTEXT_BASE_DIRECTORY = "JavacodeRawReader.basePath";

    @Override
    public XHTML read(CitationAnchor citation, Context context) {

        File addressedFile = resolveFile(citation.getAddress().getPath(), (File)context.get(CONTEXT_BASE_DIRECTORY));
        log.fine("Including and parsing XHTML file: " + addressedFile);

        XHTML xhtml = getParser().createDocument();

        XHTMLElement root =
                xhtml.createRoot(getXPath(), Constants.WRAPPER_ELEMENT)
                        .setAttribute(XHTML.ATTR.CLASS, citation.getOutputClasses())
                        .setAttribute(XHTML.ATTR.id, citation.getOutputIdentifier());

        appendTitle(root, citation.getTitle());

        appendContent(root, addressedFile, citation);

        return xhtml;
    }


    protected void appendContent(XHTMLElement parent, File file, CitationAnchor citation) {

        String[] content = handler.getContent(file, null);

        // Filtering of source
        for (ContentFilter filter : filters) {
            content = filter.filter(content, citation);
        }

        // Transform the source into an XML document
        String contentOutput = printer.print(content);
        if (contentOutput != null) {
            parent.createChild(Constants.WRAPPER_ELEMENT)
                    .setAttribute(XHTML.ATTR.CLASS, Constants.TYPE_CONTENT)
                    .setContent(contentOutput);
        }
    }

}
