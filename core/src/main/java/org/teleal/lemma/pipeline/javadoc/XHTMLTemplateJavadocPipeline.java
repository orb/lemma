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

package org.teleal.lemma.pipeline.javadoc;

import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.teleal.common.io.IO;
import org.teleal.common.jdoc.EasyDoclet;
import org.teleal.common.logging.LoggingUtil;
import org.teleal.common.xhtml.XHTML;
import org.teleal.common.xhtml.XHTMLParser;
import org.teleal.common.xml.ParserException;
import org.teleal.lemma.pipeline.Pipeline;
import org.teleal.lemma.processor.Processor;
import org.teleal.lemma.processor.xhtml.JavadocCitationProcessor;
import org.teleal.lemma.processor.xhtml.TocProcessor;
import org.teleal.lemma.processor.xhtml.XRefProcessor;
import org.teleal.lemma.reader.text.PlaintextReader;
import org.teleal.lemma.reader.javacode.JavacodeRawReader;
import org.teleal.lemma.reader.javadoc.AbstractJavadocReader;
import org.teleal.lemma.reader.xml.XMLReader;

import javax.xml.xpath.XPath;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Reads an input XHTML document, processes it and returns an XHTML output document.
 * <p>
 * Call the {@link #main(String[])} method with your
 * {@link org.teleal.lemma.pipeline.javadoc.XHTMLTemplateJavadocPipeline.Options} to start
 * Lemma.
 * </p>
 * <p>
 * The processors of this pipeline, in order, are:
 * </p>
 * <ol>
 * <li>{@link org.teleal.lemma.processor.xhtml.JavadocCitationProcessor}</li>
 * <li>{@link org.teleal.lemma.processor.xhtml.XRefProcessor}</li>
 * <li>{@link org.teleal.lemma.processor.xhtml.TocProcessor}</li>
 * </ol>
 *
 * @author Christian Bauer
 */
public class XHTMLTemplateJavadocPipeline extends Pipeline<XHTML, XHTML> {

    static final private Logger log = Logger.getLogger(XHTMLTemplateJavadocPipeline.class.getName());

    final private XHTMLParser parser = new XHTMLParser();
    final private XPath xpath;

    final private RootDoc rootDoc;
    final private File baseDirectory;
    final private boolean normalizeOutput;

    public XHTMLTemplateJavadocPipeline(SharedOptions options) {
        this(options.baseDirectory, options.packageNames);
    }

    public XHTMLTemplateJavadocPipeline(File baseDirectory, Collection<String> packageNames) {
        this(new EasyDoclet(baseDirectory, packageNames).getRootDoc(), baseDirectory, true);
    }

    public XHTMLTemplateJavadocPipeline(RootDoc rootDoc, File baseDirectory) {
        this(rootDoc, baseDirectory, true);
    }

    public XHTMLTemplateJavadocPipeline(RootDoc rootDoc, File baseDirectory,
                                        boolean normalizeOutput) {
        log.info("Configuring pipeline with base directory: " + baseDirectory);
        for (PackageDoc packageDoc : rootDoc.specifiedPackages()) {
            log.info("Included package: " + packageDoc);
        }
        this.rootDoc = rootDoc;
        this.baseDirectory = baseDirectory;
        this.normalizeOutput = normalizeOutput;

        this.xpath = getParser().createXPath();
        
    }

    public XHTMLParser getParser() {
        return parser;
    }

    public XPath getXPath() {
        return xpath;
    }

    public RootDoc getRootDoc() {
        return rootDoc;
    }

    public File getBaseDirectory() {
        return baseDirectory;
    }

    public boolean isNormalizeOutput() {
        return normalizeOutput;
    }

    public XHTML execute(File xhtmlTemplateFile) {
        XHTML template;
        try {
            log.info("Parsing initial XHTML template file: " + xhtmlTemplateFile);
            template = parser.parse(xhtmlTemplateFile);
        } catch (ParserException ex) {
            throw new RuntimeException(ex);
        }
        return execute(template);
    }

    @Override
    protected void resetContext() {
        super.resetContext();
        getContext().put(AbstractJavadocReader.CONTEXT_ROOT_DOC, getRootDoc());
        getContext().put(XMLReader.CONTEXT_BASE_DIRECTORY, getBaseDirectory());
        getContext().put(JavacodeRawReader.CONTEXT_BASE_DIRECTORY, getBaseDirectory());
        getContext().put(PlaintextReader.CONTEXT_BASE_DIRECTORY, getBaseDirectory());
    }

    @Override
    public XHTML execute(XHTML input) {
        XHTML output = super.execute(input);

        if (isNormalizeOutput())
            output.getW3CDocument().normalizeDocument();

        return output;
    }

    public Processor<XHTML, XHTML>[] getProcessors() {
        return new Processor[]{
                new JavadocCitationProcessor(getRootDoc()),
                new XRefProcessor(),
                new TocProcessor(),
        };
    }

    public static void main(String[] args) throws Exception {

        LoggingUtil.loadDefaultConfiguration();

        Options options = new Options(args);

        XHTMLTemplateJavadocPipeline pipeline =
                new XHTMLTemplateJavadocPipeline(options.baseDirectory, options.packageNames);

        XHTML result = pipeline.execute(options.xhtmlTemplateFile);

        pipeline.prepareOutputFile(options.xhtmlOutputFile, options.overwriteOutputFile);

        System.out.println("Writing output file: " + options.xhtmlOutputFile.getAbsolutePath());

        IO.writeUTF8(
                options.xhtmlOutputFile,
                pipeline.getParser().print(result, 4, true) // TODO: Make configurable?
        );
    }

    /**
     * Options which are shared between this bootstrap class and the Maven plugin.
     */
    public static class SharedOptions {

        @Option(required = true, name = "-d", metaVar = "<path>", usage =
                "The base path of all source and resource files.")
        public File baseDirectory;

        @Option(required = false, name = "-p", metaVar = "<package.name>", multiValued = true,
                usage = "Included package, repeat option for multiple packages.")
        public List<String> packageNames = new ArrayList();

        // TODO: Make this optional
        @Option(required = true, name = "-i", metaVar = "<template.xhtml>",
                usage = "XHTML template file.")
        public File xhtmlTemplateFile;

        public SharedOptions() {
        }

        public SharedOptions(String[] args) {
            CmdLineParser cmdLineParser = new CmdLineParser(this);
            try {
                cmdLineParser.parseArgument(args);
            } catch (CmdLineException e) {
                System.err.println(e.getMessage());
                System.err.println("USAGE: java -jar <JARFILE> [options]");
                cmdLineParser.printUsage(System.err);
                System.exit(1);
            }

            if (!prepare()) {
                System.exit(1);
            }
        }

        /**
         * Called by the constructor to convert and validate the given option values.
         *
         * @return true if validation was successful.
         */
        public boolean prepare() {

            if (!baseDirectory.canRead()) {
                System.err.println("Base directory not found or not readable: " + baseDirectory);
                return false;
            }

            if (!xhtmlTemplateFile.exists()) {
                System.err.println("XHTML template file not found: " + xhtmlTemplateFile);
                return false;
            }

            if (packageNames.size() == 0) {
                // Default to all sub-directories in base directory
                File[] subdirs = baseDirectory.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.isDirectory() && file.getName().matches("[a-zA-Z_]+");
                    }
                });
                for (File subdir : subdirs) {
                    packageNames.add(subdir.getName());
                }
            }
            return true;
        }
    }

    /**
     * Options which are specific to this bootstrap class.
     */
    public static class Options extends SharedOptions {

        @Option(required = true, name = "-o", metaVar = "<result.xhtml>", usage = "XHTML output file.")
        public File xhtmlOutputFile;

        @Option(name = "-overwrite", metaVar = "true|false", usage = "Overwrite existing output file quietly.")
        public boolean overwriteOutputFile = false;

        public Options() {
        }

        public Options(String[] args) {
            super(args);
        }
    }

}
