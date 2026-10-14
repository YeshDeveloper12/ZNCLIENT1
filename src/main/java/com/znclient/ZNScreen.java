package com.znclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.*;

/** Responsive, animated ClickGUI. Non-functional modules are explicitly labeled as previews. */
public class ZNScreen extends Screen {
    private static final int BLUE = 0xFF357DDE, WHITE = 0xFFF2F6FF, MUTED = 0xFFB0BED1;
    private static final String[] CATEGORIES = {"Combat", "Movement", "Visuals", "Macros", "Utility", "Internals"};
    private static final String[][] MODULES = {
        {"Aim Assist", "Auto Anchor", "Auto Crystal", "Auto Mace", "AutoCart", "Backtrack", "Criticals", "Crystal Optimizer", "Hitboxes", "Knockback Delay", "Mace Swap", "Piercing", "Shield Breaker", "Spear Assist", "Spear Lunge", "Spear Reach", "Totem Hit", "Trigger Bot"},
        {"Auto Walk", "Combo Helper", "Fast Stairs", "GUI Walk", "Jump Reset", "Keep Sprint", "Move Fix", "No Jump Delay", "No Lunge Cooldown", "No Slow", "No Web", "Speed", "Sprint", "Velocity"},
        {"Aim Visualizer", "Ambience", "Array List", "Freecam", "Full Bright", "Keybinds", "Nametags", "Notifications", "Player ESP", "Search", "Storage ESP", "Target ESP", "Timer Cooldown", "Trajectories", "Watermark", "Xray", "Coordinates", "FPS Counter", "Clock", "Direction", "Crosshair Info", "Status HUD"},
        {"Chest Stealer", "Dive Bomb", "Key Charge", "Key Pearl Macro", "Pearl Catch"},
        {"Anti Trap", "Attribute Mine", "Auto Tool", "Auto Totem", "Block Bruteforcer", "Elytra Swap", "Fast EXP", "Fast Place", "No Break Delay", "Pot Refill", "Smart Pot", "Totem Hand", "Totem Offhand", "Totem Refill"},
        {"Capes", "Click GUI", "Command Logger", "Prevent", "Self Destruct"}
    };
    private final int[] offsets = new int[CATEGORIES.length];
    private final Map<String, Float> animation = new HashMap<>();
    private String status = "Right Shift: close  •  Mouse wheel: scroll column  •  Preview modules are inactive";
    private long openedAt;
    private int hoverColumn = -1;

    public ZNScreen() { super(Text.literal("ZNClient")); openedAt = System.currentTimeMillis(); }
    @Override public boolean shouldPause() { return false; }
    private int countColumns() { return width >= 760 ? 6 : width >= 510 ? 4 : width >= 330 ? 3 : 2; }
    private int gap() { return 7; }
    private int panelWidth() { return Math.max(90, Math.min(205, (width - 24 - (countColumns()-1)*gap()) / countColumns())); }
    private int startX() { return (width - countColumns()*panelWidth() - (countColumns()-1)*gap())/2; }
    private int panelY(int col) { return 67 + (col / countColumns()) * Math.max(80, (height - 105) / ((6+countColumns()-1)/countColumns())); }
    private int panelHeight() { return Math.max(65, (height - 96) / ((6+countColumns()-1)/countColumns()) - 8); }
    private int panelX(int col) { return startX() + (col % countColumns())*(panelWidth()+gap()); }
    private boolean inside(double mx,double my,int x,int y,int w,int h) { return mx>=x && mx<x+w && my>=y && my<y+h; }

    @Override public void render(DrawContext ctx, int mx, int my, float delta) {
        super.render(ctx,mx,my,delta);
        float progress = Math.min(1f,(System.currentTimeMillis()-openedAt)/220f);
        float ease = 1f-(1f-progress)*(1f-progress)*(1f-progress);
        ctx.fill(0,0,width,height,0x85070D18);
        ctx.fill(0,0,width,51,0xE7111B2C);
        ctx.fill(0,49,width,51,BLUE);
        ctx.drawTextWithShadow(textRenderer,"ZN",12,16,0xFF79BAFF);
        ctx.drawTextWithShadow(textRenderer,"CLIENT",31,16,WHITE);
        ctx.drawTextWithShadow(textRenderer,"v1.1  •  Fabric 1.21.11",Math.max(100,width-170),16,MUTED);
        hoverColumn = -1;
        for (int c=0;c<CATEGORIES.length;c++) {
            int x=panelX(c), y=panelY(c)+(int)((1-ease)*18), w=panelWidth(), h=panelHeight();
            if (inside(mx,my,x,y,w,h)) hoverColumn=c;
            ctx.fill(x,y,x+w,y+h,0xE51C273A);
            ctx.fill(x,y,x+w,y+2,BLUE);
            ctx.fill(x,y+2,x+w,y+24,0xFF25354E);
            ctx.drawTextWithShadow(textRenderer,CATEGORIES[c],x+8,y+9,WHITE);
            ctx.enableScissor(x+3,y+26,x+w-3,y+h-5);
            for (int m=0;m<MODULES[c].length;m++) {
                String name=MODULES[c][m];
                int yy=y+29+m*22-offsets[c];
                if (yy+21<y+26 || yy>y+h-4) continue;
                boolean active = isEnabled(name);
                float a=animation.getOrDefault(name,active?1f:0f);
                a += ((active?1f:0f)-a)*Math.min(1f,0.2f+delta*0.1f);
                animation.put(name,a);
                if (a>0.01f) {
                    int alpha=(int)(a*210);
                    ctx.fill(x+5,yy,x+w-5,yy+20,(alpha<<24)|0x357DDE);
                } else if (inside(mx,my,x+5,yy,w-10,20)) ctx.fill(x+5,yy,x+w-5,yy+20,0x554A5C78);
                String label = textRenderer.trimToWidth(name,w-25);
                ctx.drawTextWithShadow(textRenderer,label,x+10,yy+6,active?WHITE:MUTED);
                if (isFunctional(name)) ctx.fill(x+w-11,yy+8,x+w-8,yy+11,0xFF6DE7B4);
            }
            ctx.disableScissor();
            if (MODULES[c].length*22 > h-34) {
                int track=h-37;
                int thumb=Math.max(12,(int)((double)track*track/(MODULES[c].length*22)));
                int max=Math.max(1,MODULES[c].length*22-track);
                int ty=y+29+(int)((track-thumb)*(offsets[c]/(double)max));
                ctx.fill(x+w-3,ty,x+w-1,ty+thumb,0xFF73A8F2);
            }
        }
        ctx.fill(0,height-26,width,height,0xEB121C2B);
        ctx.drawTextWithShadow(textRenderer,textRenderer.trimToWidth(status,width-16),8,height-17,MUTED);
    }
    private boolean isFunctional(String name) { return ZNClient.MODULES.get(name) != null; }
    private boolean isEnabled(String name) { return ZNClient.MODULES.enabled(name); }
    @Override public boolean mouseClicked(net.minecraft.client.gui.Click click,boolean doubled) {
        double mx=click.x(),my=click.y();
        for(int c=0;c<6;c++) {
            int x=panelX(c),y=panelY(c),w=panelWidth(),h=panelHeight();
            if(!inside(mx,my,x,y+26,w,h-30)) continue;
            int idx=((int)my-y-29+offsets[c])/22;
            if(idx<0||idx>=MODULES[c].length) continue;
            String name=MODULES[c][idx];
            if(ZNClient.MODULES.toggle(name)) {
                ClientConfig.save(ZNClient.MODULES);
                status=name+": "+(ZNClient.MODULES.enabled(name)?"ON":"OFF")+"  •  Saved";
            } else status=name+" — not implemented in this build";
            return true;
        }
        return super.mouseClicked(click,doubled);
    }
    @Override public boolean mouseScrolled(double mouseX,double mouseY,double horizontalAmount,double verticalAmount) {
        for(int c=0;c<6;c++) {
            int x=panelX(c),y=panelY(c),h=panelHeight();
            if(!inside(mouseX,mouseY,x,y,panelWidth(),h)) continue;
            int max=Math.max(0,MODULES[c].length*22-(h-34));
            offsets[c]=Math.max(0,Math.min(max,offsets[c]-(int)Math.signum(verticalAmount)*44));
            return true;
        }
        return super.mouseScrolled(mouseX,mouseY,horizontalAmount,verticalAmount);
    }
    @Override public boolean keyPressed(net.minecraft.client.input.KeyInput input) {
        if(input.key()==GLFW.GLFW_KEY_RIGHT_SHIFT) { close(); return true; }
        return super.keyPressed(input);
    }
}
