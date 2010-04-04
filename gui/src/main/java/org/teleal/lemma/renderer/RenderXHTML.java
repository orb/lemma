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

package org.teleal.lemma.renderer;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;
import org.teleal.common.xhtml.XHTML;
import org.teleal.common.xhtml.XHTMLParser;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 * @author Christian Bauer
 */
public class RenderXHTML {

    public static void main(String[] args) throws Exception {

        System.out.println("### RUNNING: " + new Date());
        XHTMLParser parser = new XHTMLParser();
        XHTML dom = parser.parse(new File("target/manual/lemma-core-manual.xhtml"));

        System.out.println("### PARSED: " + new Date());
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(dom.getW3CDocument(), "file:///Users/cb/software/lemma/core/target/manual/");
        renderer.layout();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        renderer.createPDF(os);
        final ByteBuffer byteBuffer = ByteBuffer.wrap(os.toByteArray());
/*

        OutputStream os = new FileOutputStream(new File("target/manual.pdf"));
        renderer.createPDF(os);

*/
        System.out.println("### DONE: " + new Date());


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    RenderXHTML.display(byteBuffer);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
/*
        int width = 700;
        int height = 5000;
        Graphics2DRenderer renderer = new Graphics2DRenderer();
        renderer.setDocument(dom.getW3CDocument(), "file:///Users/cb/software/lemma/core/target/manual/");
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D imageGraphics = (Graphics2D) image.getGraphics();
        imageGraphics.setColor(Color.white);
        imageGraphics.fillRect(0, 0, width, height);
        renderer.layout(imageGraphics, new Dimension(width, height));
        renderer.render(imageGraphics);
        //Now output the image to PNG using the ImageIO libraries.
        OutputStream os = new FileOutputStream(new File("target/manual.png"));
        ImageIO.write(image, "png", os);
*/


    }


    public static void display(ByteBuffer buf) throws IOException {

        //set up the frame and panel
        JFrame frame = new JFrame("PDF Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PagePanel panel = new PagePanel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        PDFFile pdffile = new PDFFile(buf);
        // show the first page
        PDFPage page = pdffile.getPage(0);
        panel.showPage(page);

    }
}
