package com.portingdeadmods.modjam.events;

import com.portingdeadmods.modjam.Modjam;
import com.portingdeadmods.modjam.registries.MJShaders;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

@EventBusSubscriber(modid = Modjam.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        
    }
    
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {

    }
    
    @SubscribeEvent
    public static void registerScreens(RegisterShadersEvent event) {
        MJShaders.registerShaders(event);
    }
}
