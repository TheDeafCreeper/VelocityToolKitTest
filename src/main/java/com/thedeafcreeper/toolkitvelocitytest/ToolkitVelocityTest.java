package com.thedeafcreeper.toolkitvelocitytest;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import group.aelysium.rustyconnector.toolkit.RustyConnector;
import group.aelysium.rustyconnector.toolkit.core.events.Listener;
import group.aelysium.rustyconnector.toolkit.core.packet.Packet;
import group.aelysium.rustyconnector.toolkit.core.packet.PacketIdentification;
import group.aelysium.rustyconnector.toolkit.core.packet.VelocityPacketBuilder;
import group.aelysium.rustyconnector.toolkit.velocity.central.VelocityFlame;
import group.aelysium.rustyconnector.toolkit.velocity.central.VelocityTinder;
import group.aelysium.rustyconnector.toolkit.velocity.events.mc_loader.RegisterEvent;
import group.aelysium.rustyconnector.toolkit.velocity.server.IMCLoader;
import org.slf4j.Logger;

import java.util.Objects;

@Plugin(
        id = "toolkitvelocitytest",
        name = "ToolkitVelocityTest",
        version = "1.0-SNAPSHOT",
        dependencies = {
                @Dependency(id="rustyconnector-velocity")
        }
)
public class ToolkitVelocityTest {

    @Inject
    private Logger logger;

    VelocityTinder tinder;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        tinder = RustyConnector.Toolkit.proxy().orElseThrow();

        tinder.onStart(flame -> {
            logger.info("RustyConnector is on version " + flame.version().toString());

            flame.services().events().on(RegisterEvent.class, new OnMCLoaderRegister());
        });

        tinder.onStop(() -> {
            logger.info("Lost connection to RustyConnector!");
        });
    }

    public class OnMCLoaderRegister implements Listener<RegisterEvent> {
        public void handler(RegisterEvent event) {
            IMCLoader loader = event.mcLoader();
            if (Objects.equals(loader.family().id(), "server2")) {
                loader.lock();
                logger.info("Loader " + loader.uuidOrDisplayName() + " connected then was auto locked.");
            }
        }
    }
}
