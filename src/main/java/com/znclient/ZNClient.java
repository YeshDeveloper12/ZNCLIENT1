package com.znclient;

import com.znclient.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class ZNClient implements ClientModInitializer {
    public static final ModuleManager MODULES=new ModuleManager();
    private boolean wasPressed;
    @Override public void onInitializeClient(){
        ClientConfig.load(MODULES);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.getWindow()==null)return;
            boolean pressed=InputUtil.isKeyPressed(client.getWindow(),GLFW.GLFW_KEY_RIGHT_SHIFT);
            if(pressed && !wasPressed && client.currentScreen==null)client.setScreen(new ZNScreen());
            wasPressed=pressed;
        });
        HudRenderCallback.EVENT.register((ctx,tickCounter)->{
            MinecraftClient mc=MinecraftClient.getInstance();
            if(mc.player==null || mc.options.hudHidden || mc.currentScreen instanceof ZNScreen)return;
            int y=5;
            if(MODULES.enabled("Watermark")){y=draw(mc,ctx,"ZNClient  •  1.21.11",y,0xFF74B8FF);}
            if(MODULES.enabled("FPS Counter"))y=draw(mc,ctx,"FPS: "+mc.getCurrentFps(),y,0xFFFFFFFF);
            if(MODULES.enabled("Coordinates"))y=draw(mc,ctx,String.format(java.util.Locale.ROOT,"XYZ: %.0f / %.0f / %.0f",mc.player.getX(),mc.player.getY(),mc.player.getZ()),y,0xFFFFFFFF);
            if(MODULES.enabled("Direction"))y=draw(mc,ctx,"Facing: "+mc.player.getHorizontalFacing().asString(),y,0xFFFFFFFF);
            if(MODULES.enabled("Clock"))y=draw(mc,ctx,"Time: "+LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),y,0xFFFFFFFF);
            if(MODULES.enabled("Crosshair Info") && mc.crosshairTarget instanceof net.minecraft.util.hit.BlockHitResult hit)y=draw(mc,ctx,"Block: "+hit.getBlockPos().toShortString(),y,0xFFFFFFFF);
            if(MODULES.enabled("Status HUD"))y=draw(mc,ctx,"Modules: "+MODULES.all().stream().filter(m->m.enabled() && !m.name().equals("Click GUI")).count(),y,0xFFFFFFFF);
        });
    }
    private static int draw(MinecraftClient mc,net.minecraft.client.gui.DrawContext ctx,String text,int y,int color){
        ctx.fill(3,y-2,mc.textRenderer.getWidth(text)+11,y+11,0xB5162236);
        ctx.drawTextWithShadow(mc.textRenderer,text,7,y,color);
        return y+16;
    }
}
