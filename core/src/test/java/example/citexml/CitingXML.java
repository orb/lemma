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

package example.citexml;

import com.sun.tools.javac.util.Pair;
import org.teleal.common.xhtml.XHTML;
import example.util.DocletTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Citing from XML files
 * <p>
 * Let's assume you have an XML file - note that its schema or DTD does not matter because Lemma
 * will never try to actually parse it as XML. You want to include some lines of this XML file,
 * or the whole file content in your documentation:
 * </p>
 * <a class="citation" href="file://example/citexml/MyOrders.xml" style="clean-labels:false;">Citation</a>
 * <p>
 * In your manual template (or any Javadoc comment), create a citation anchor referencing this file:
 * </p>
 * <a class="citation" href="file://example/citexml/sample01_input.xhtml">Citation</a>
 * <p>
 * Lemma will generate the following output:
 * </p>
 * <a class="citation" href="file://example/citexml/sample01_output.xhtml">Citation</a>
 * <p>
 * Just like with Javadoc comments or code, you can use citation options to customize
 * the included content:
 * </p>
 * <a class="citation" href="file://example/citexml/sample02_input.xhtml">Citation</a>
 * <p>
 * The output now only contains the source of the given fragment(s):
 * </p>
 * <a class="citation" href="file://example/citexml/sample02_output.xhtml">Citation</a>
 *
 * @author Christian Bauer
 */
public class CitingXML extends DocletTest {

    @DataProvider(name = "samples")
    public Object[][] getSamples() {
        return new Object[][]{
                {new Pair<String, String>("example/citexml/sample01_input.xhtml", "example/citexml/sample01_output.xhtml")},
                {new Pair<String, String>("example/citexml/sample02_input.xhtml", "example/citexml/sample02_output.xhtml")},
        };
    }

    @Test(dataProvider = "samples")
    public void process(Pair<String, String> sample) throws Exception {

        XHTML output = getTemplatePipeline().execute(parseDocument(sample.fst));
        assertEquals(getParser().print(output), getContent(sample.snd));

    }
}
