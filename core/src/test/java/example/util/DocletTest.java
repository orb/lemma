package example.util;

import com.sun.javadoc.RootDoc;
import org.teleal.common.jdoc.EasyDoclet;
import org.teleal.common.logging.LoggingUtil;
import org.teleal.common.io.IO;
import org.teleal.common.xhtml.XHTML;
import org.teleal.common.xhtml.XHTMLParser;
import org.teleal.lemma.pipeline.javadoc.XHTMLTemplateJavadocPipeline;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DocletTest {

    protected XHTMLParser xhtmlParser = new XHTMLParser();

    protected RootDoc rootDoc;
    protected XHTMLTemplateJavadocPipeline xhtmlTemplatePipeline;

    @BeforeTest
    public void init() throws Exception {
        LoggingUtil.loadDefaultConfiguration();
    }

    @BeforeClass
    @Parameters("baseDirectory")
    public void init(@Optional String baseDirectoryString) throws Exception {

        File baseDirectory = new File(baseDirectoryString);

        rootDoc =
                new EasyDoclet(
                        baseDirectory,
                        getPackageNames().length == 0
                                ? getDefaultPackageNames(baseDirectory)
                                : getPackageNames()
                ).getRootDoc();
        
        xhtmlTemplatePipeline = new XHTMLTemplateJavadocPipeline(getRootDoc(), baseDirectory);
    }

    public String[] getDefaultPackageNames(File baseDirectory) {
        List<String> names = new ArrayList();
        File[] subdirs = baseDirectory.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory() && file.getName().matches("[a-zA-Z_]+");
            }
        });
        if (subdirs != null) {
            for (File subdir : subdirs) {
                names.add(subdir.getName());
            }
            return names.toArray(new String[names.size()]);
        } else {
            return new String[0];
        }
    }

    public String[] getPackageNames() {
        return new String[0];
    }

    public RootDoc getRootDoc() {
        return rootDoc;
    }

    public XHTMLParser getParser() {
        return xhtmlParser;
    }

    public XHTMLTemplateJavadocPipeline getTemplatePipeline() {
        return xhtmlTemplatePipeline;
    }

    public XHTML parseDocument(String file) throws Exception {
        return getParser().parse(getResource(file));
    }

    public String getContent(String file) throws Exception {
        return IO.readLines(new File(getResource(file).toURI()));
    }

    protected URL getResource(String file) throws Exception {
        if (getTemplatePipeline() == null)
            throw new IllegalStateException("Call init() before accessing resources");
        File resourceFile = new File(getTemplatePipeline().getBaseDirectory(), file);
        if (!resourceFile.canRead()) {
            throw new IllegalArgumentException("Can't read or find file: " +  resourceFile);
        }
        return resourceFile.toURI().toURL();
    }

}
