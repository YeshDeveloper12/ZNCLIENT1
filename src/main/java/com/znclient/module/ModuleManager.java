package com.znclient.module;

import java.util.*;
public final class ModuleManager {
    private final Map<String, ClientModule> modules = new LinkedHashMap<>();
    public ModuleManager(){
        add("Watermark","Visuals","Show client watermark",true);
        add("Coordinates","Visuals","Show player coordinates",true);
        add("FPS Counter","Visuals","Show frames per second",true);
        add("Clock","Visuals","Show local time",false);
        add("Direction","Visuals","Show facing direction",false);
        add("Crosshair Info","Visuals","Show target block coordinates",false);
        add("Status HUD","Visuals","Show selected client modules",false);
        add("Click GUI","Internals","Open client settings using Right Shift",true);
    }
    private void add(String name,String category,String description,boolean enabled){modules.put(name,new ClientModule(name,category,description,enabled));}
    public Collection<ClientModule> all(){return Collections.unmodifiableCollection(modules.values());}
    public ClientModule get(String name){return modules.get(name);}
    public boolean enabled(String name){ClientModule m=get(name);return m!=null && m.enabled();}
    public boolean toggle(String name){ClientModule m=get(name);if(m==null || name.equals("Click GUI"))return false;m.toggle();return true;}
}
