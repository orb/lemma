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

package org.teleal.lemma.gui;

import org.teleal.common.swingfwk.logging.LogCategory;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @author Christian Bauer
 */
public class DefaultLogCategories extends ArrayList<LogCategory> {

    public DefaultLogCategories() {
        super(10);

        add(new LogCategory("Framework", new LogCategory.Group[]{

                new LogCategory.Group(
                        "Swing Application",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.common.swingfwk", Level.FINEST),
                        }
                ),

                new LogCategory.Group(
                        "UDP datagram processing and content",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.network.spi.DatagramProcessor", Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "TCP communication",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.network.spi.UpnpStream", Level.FINER),
                                new LogCategory.LoggerLevel("org.teleal.upnp.network.spi.StreamServer", Level.FINE),
                                new LogCategory.LoggerLevel("org.teleal.upnp.network.spi.StreamClient", Level.FINE),
                        }
                ),

                new LogCategory.Group(
                        "SOAP action message processing and content",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.network.spi.SOAPActionProcessor", Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "GENA event message processing and content",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.network.spi.GENAEventProcessor", Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "HTTP header processing",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.network.impl.HttpHeaderConverter", Level.FINER)
                        }
                ),
        }));


        add(new LogCategory("UPnP Protocol", new LogCategory.Group[]{

                new LogCategory.Group(
                        "Discovery (Notification & Search)",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.protocol.async", Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "Description",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.protocol.RetrieveRemoteDescriptors", Level.FINE),
                                new LogCategory.LoggerLevel("org.teleal.upnp.protocol.sync.ReceivingRetrieval", Level.FINE),
                                new LogCategory.LoggerLevel("org.teleal.upnp.descriptor.DeviceDescriptorBinder", Level.FINE),
                                new LogCategory.LoggerLevel("org.teleal.upnp.descriptor.ServiceDescriptorBinder", Level.FINE),
                        }
                ),

                new LogCategory.Group(
                        "Control",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.protocol.sync.ReceivingAction", Level.FINER),
                                new LogCategory.LoggerLevel("org.teleal.upnp.protocol.sync.SendingAction", Level.FINER),
                        }
                ),

                new LogCategory.Group(
                        "GENA",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.protocol.sync.ReceivingEvent", Level.FINER),
                                new LogCategory.LoggerLevel("org.teleal.upnp.protocol.sync.ReceivingSubscribe", Level.FINER),
                                new LogCategory.LoggerLevel("org.teleal.upnp.protocol.sync.ReceivingUnsubscribe", Level.FINER),
                                new LogCategory.LoggerLevel("org.teleal.upnp.protocol.sync.SendingEvent", Level.FINER),
                                new LogCategory.LoggerLevel("org.teleal.upnp.protocol.sync.SendingSubscribe", Level.FINER),
                                new LogCategory.LoggerLevel("org.teleal.upnp.protocol.sync.SendingUnsubscribe", Level.FINER),
                                new LogCategory.LoggerLevel("org.teleal.upnp.protocol.sync.SendingRenewal", Level.FINER),
                        }
                ),
        }));

        add(new LogCategory("Core", new LogCategory.Group[]{

                new LogCategory.Group(
                        "Router",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.Router", Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "Registry",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.registry", Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "Local service binding & invocation",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.model.LocalService", Level.FINER),
                                new LogCategory.LoggerLevel("org.teleal.upnp.model.LocalServiceBinder", Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "Control Point interaction",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.controlpoint", Level.FINER),
                        }
                ),
        }));

        add(new LogCategory("VLC MediaRenderer", new LogCategory.Group[]{

                new LogCategory.Group(
                        "VLC polling loop",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.vlc.VLCConnectionTask", Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "MediaRenderer services",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.vlc.renderer", Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "VLC HTTP connector and parser",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.upnp.vlc.http", Level.FINER),
                        }
                )

        }));

    }
}

