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

package org.teleal.lemma.processor.xhtml;

import org.teleal.common.xhtml.XHTML;
import org.teleal.common.xhtml.XHTMLElement;
import org.teleal.lemma.Constants;
import org.teleal.lemma.pipeline.Context;
import org.teleal.lemma.anchor.CitationAnchor;
import org.teleal.lemma.anchor.AnchorAddress;
import org.teleal.lemma.anchor.Scheme;
import org.teleal.lemma.processor.AbstractProcessor;

import java.util.logging.Logger;

/**
 * Detects cross-reference anchors and sets their link text.
 *
 * @author Christian Bauer
 */
public class XRefProcessor extends AbstractProcessor<XHTML, XHTML> {

    private Logger log = Logger.getLogger(XRefProcessor.class.getName());

    public XHTML process(XHTML input, Context context) {
        log.fine("Processing input...");

        XHTML output = transformReferences(input, context);

/*
        if (log.isLoggable(Level.FINEST)) {
            log.finest("Completed processing input, generated output: ");
            log.finest("--------------------------------------------------------------------------------");
            log.finest(XML.toString(output, false));
            log.finest("--------------------------------------------------------------------------------");
        }
*/

        return output;
    }

    protected XHTML transformReferences(XHTML input, Context context) {

        CitationAnchor[] xrefs = findCitationAnchors(input, Constants.TYPE_XREF);
        for (CitationAnchor xref : xrefs) {

            String xrefTarget = xref.getAddress().toIdentifierString();

            // We have to try both schemes because {@link} just specifies the class/method and not the scheme
            String javacodeXrefTargetIdentifier =
                    new AnchorAddress(
                            Scheme.JAVACODE,
                            xref.getAddress().getPath(),
                            xref.getAddress().getFragment()
                    ).toIdentifierString();

            log.finest("Trying to resolve xref: " + xrefTarget);
            XHTMLElement resolvedCitationElement = input.getRoot(getXPath()).findChildWithIdentifier(xrefTarget);

            if (resolvedCitationElement == null) {
                log.fine("Could not resolve xref, trying: " + javacodeXrefTargetIdentifier);
                resolvedCitationElement = input.getRoot(getXPath()).findChildWithIdentifier(javacodeXrefTargetIdentifier);
            }

            if (resolvedCitationElement != null) {

                // Set new address (HREF) on xref element
                xref.setAttribute(XHTML.ATTR.href, "#" + resolvedCitationElement.getId());

                // If it doesn't have a label, set a label
                if (!xref.getW3CElement().hasChildNodes()) {
                    String citationLabel = getResolvedLabel(xref, resolvedCitationElement);
                    xref.setContent(citationLabel != null ? citationLabel : getResolvedLabel(xref));
                }

            } else {
                log.warning("Linked citation identifier not found: " + xrefTarget + "/" + javacodeXrefTargetIdentifier);

                // Clean up and set a message that makes unresolved links easy to see
                xref.removeChildren();
                xref.setAttribute(XHTML.ATTR.href, getUnresolvedLink(xrefTarget))
                        .setAttribute(XHTML.ATTR.CLASS, Constants.TYPE_XREF + " " + Constants.TYPE_UNRESOLVED)
                        .setContent(getUnresolvedLabel(xrefTarget));
            }

        }
        return input;
    }

    protected String getResolvedLabel(CitationAnchor xref) {
        return "(LINK)";
    }

    protected String getResolvedLabel(CitationAnchor xref, XHTMLElement citationElement) {
        // Try to find a direct child element that is a 'title' class, then take that content as label

        XHTMLElement[] children = citationElement.getChildren();
        for (XHTMLElement child : children) {
            String[] types = child.getClasses();
            for (String t : types) {
                if (t.trim().equals(Constants.TYPE_TITLE)) {
                    String title = child.getContent();
                    // TODO: And cut off that last period of the 'first sentence' title
                    return title.endsWith(".") ? title.substring(0, title.length() - 1) : title;
                }
            }
        }
        return null;
    }

    protected String getUnresolvedLink(String xrefTarget) {
        return "#UNRESOLVED_LINK";
    }

    protected String getUnresolvedLabel(String xrefTarget) {
        return "UNRESOLVED ID: " + xrefTarget;
    }

}
