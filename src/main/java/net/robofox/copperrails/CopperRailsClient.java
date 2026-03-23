package net.robofox.copperrails;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class CopperRailsClient implements ClientModInitializer {


    private static void initializeResourcePack() {
        Identifier id = Identifier.fromNamespaceAndPath(CopperRails.MOD_ID, "copperrails3d");
        ModContainer modContainer = FabricLoader.getInstance().getModContainer(CopperRails.MOD_ID).orElseThrow();
        ResourceLoader.registerBuiltinPack(id, modContainer, Component.nullToEmpty("CopperRails 3D Rails"), PackActivationType.NORMAL);
    }

    @Override
    public void onInitializeClient() {
        initializeResourcePack();
    }
}
