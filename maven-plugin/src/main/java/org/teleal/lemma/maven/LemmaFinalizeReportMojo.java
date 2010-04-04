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

package org.teleal.lemma.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.teleal.common.io.IO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * You only need this when you integrate Lemma with your 'site' lifecycle.
 *
 * @author Christian Bauer
 * @goal finalize-manual
 * @phase post-site
 * @requiresDependencyResolution test
 */
public class LemmaFinalizeReportMojo extends LemmaMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        getLog().info("Finalizing Lemma output...");
        File outputDir = new File(project.getReporting().getOutputDirectory());

        File base = new File(outputDir, outputPath);

        File file = new File(base, outputFilename + ".html");
        File newFile = new File(base, "/" + outputFilename + ".xhtml");
        if (file.canWrite()) {
            getLog().info("Renaming output file with XHTML extension: " + newFile);
            file.renameTo(newFile);
        }

        File xhtmlSourceDir = new File(project.getBasedir() + "/src/site/xhtml");
        if (renameXHTMLFiles && xhtmlSourceDir.canRead() && xhtmlSourceDir.isDirectory()) {
            getLog().info("Searching for source XHTML files to rename in: " + xhtmlSourceDir);
            final List<File> xhtmlFiles = new ArrayList();
            IO.findFiles(xhtmlSourceDir, new IO.FileFinder() {
                public void found(File file) {
                    if (file.isFile() && file.getName().endsWith(".xhtml"))
                        xhtmlFiles.add(file);
                }
            });

            getLog().info("Found XHTML files to rename: " + xhtmlFiles.size());

            for (File sourceFile : xhtmlFiles) {
                String relativePath = IO.makeRelativePath(sourceFile.toString(), xhtmlSourceDir.toString());
                relativePath = relativePath.substring(0, relativePath.length()-6);
                File targetFile = new File(outputDir, relativePath + ".html");
                File targetNewFile = new File(outputDir, relativePath + ".xhtml");
                if (targetFile.canWrite()) {
                    getLog().info("Renaming target XHTML file: " + targetNewFile);
                    targetFile.renameTo(targetNewFile);
                }
            }
        }

        for (String deleteFile : deleteSiteFiles) {
            File f = new File(outputDir, deleteFile);
            if (f.canWrite()) {
                getLog().info("Deleting file: " + f);
                IO.deleteFile(f);
            }
        }
    }
}
