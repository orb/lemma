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

package org.teleal.lemma.renderer.javadoc;

import com.sun.pdfview.PDFFile;
import org.teleal.lemma.pipeline.javadoc.XHTMLTemplateJavadocPipeline;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * @author Christian Bauer
 */
public class XHTMLTemplateJavadocRenderer {

    final private Logger log = Logger.getLogger(XHTMLTemplateJavadocRenderer.class.getName());

    final private XHTMLTemplateJavadocPipeline.SharedOptions options;

    public XHTMLTemplateJavadocRenderer(XHTMLTemplateJavadocPipeline.SharedOptions options) {
        this.options = options;
    }

    public XHTMLTemplateJavadocPipeline.SharedOptions getOptions() {
        return options;
    }

    public Document generateDOM(File file) throws Exception {
        XHTMLTemplateJavadocPipeline pipeline = new XHTMLTemplateJavadocPipeline(getOptions());
        return pipeline.execute(file).getW3CDocument();
    }

    public PDFFile generatePDF(Document dom, URI baseURI) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(dom, baseURI.toString());
        renderer.layout();
        renderer.createPDF(os);

        PDFFile pdf = new PDFFile(ByteBuffer.wrap(os.toByteArray()));
        log.info("Generated pages: " + pdf.getNumPages());
        return pdf;
    }


}
