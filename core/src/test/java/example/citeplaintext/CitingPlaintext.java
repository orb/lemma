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

package example.citeplaintext;

import com.sun.tools.javac.util.Pair;
import org.teleal.common.xhtml.XHTML;
import example.util.DocletTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Citing plain text files
 * <p>
 * Regular text files that end with a <code>.txt</code> or <code>.csv</code> extension use the <code>#</code>
 * character as a comment marker:
 * </p>
 * <a class="citation" href="file://example/citeplaintext/myorders.txt" style="clean-labels:false;">Citation</a>
 * <p>
 * Reference the file in your citation anchor, and if required any inclusion labels:
 * </p>
 * <a class="citation" href="file://example/citeplaintext/sample01_input.xhtml">Citation</a>
 * <p>
 * Lemma will generate the following output:
 * </p>
 * <a class="citation" href="file://example/citeplaintext/sample01_output.xhtml">Citation</a>
 *
 * @author Christian Bauer
 */
public class CitingPlaintext extends DocletTest {

    @DataProvider(name = "samples")
    public Object[][] getSamples() {
        return new Object[][]{
                {new Pair<String, String>("example/citeplaintext/sample01_input.xhtml", "example/citeplaintext/sample01_output.xhtml")},
        };
    }

    @Test(dataProvider = "samples")
    public void process(Pair<String, String> sample) throws Exception {

        XHTML output = getTemplatePipeline().execute(parseDocument(sample.fst));
        assertEquals(getParser().print(output), getContent(sample.snd));

    }
}