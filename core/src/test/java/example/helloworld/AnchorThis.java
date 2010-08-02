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

package example.helloworld;

import example.util.DocletTest;
import org.teleal.common.xhtml.XHTML;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Referencing 'this'
 * <p>
 * Although URLs which reference the fully qualified name of a class, method, or package are
 * safe for refactoring in a good IDE, an equally safe shorthand is available with the
 * special path "<code>this</code>." For example, the following Javadoc will contain a
 * citation from the method it is written on:
 * </p>
 * <a class="citation" href="javacode://this" style="include: FRAG1"/>
 * <p>
 * You can use this shorthand for any citation anchor in Javadoc on classes, methods, and
 * packages.
 * </p>
 */
public class AnchorThis extends DocletTest {

     // DOC: FRAG1
     /**
      * Calling Hello World
      * <p>
      * A anchor which cites this method's code:
      * </p>
      * <a class="citation" href="javacode://this"/>
      */
     public void callHelloWorld() {
         new HelloWorld();
     }
     // DOC: FRAG1

     @Test
     public void processDocumentation() throws Exception {
         XHTML output = getTemplatePipeline().execute(
                 parseDocument("example/helloworld/example08_input.xhtml")
         );

         assertEquals(
                 getParser().print(output),
                 getContent("example/helloworld/example08_output.xhtml")
         );
     }

}