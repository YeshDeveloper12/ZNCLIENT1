package com.znclient;

import com.znclient.module.ClientModule;
import com.znclient.module.ModuleManager;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.*;
import java.io.*;
import java.util.Properties;

public final class ClientConfig {
    private static final Path FILE=FabricLoader.getInstance().getConfigDir().resolve("znclient.properties");
    public static void load(ModuleManager modules){
        if(!Files.isRegularFile(FILE))return;
        Properties props=new Properties();
        try(InputStream in=Files.newInputStream(FILE)){
            props.load(in);
            for(ClientModule m:modules.all()){
                String v=props.getProperty(m.name());
                if(v!=null && !m.name().equals("Click GUI"))m.setEnabled(Boolean.parseBoolean(v));
            }
        }catch(IOException ex){System.err.println("ZNClient: Could not load config: "+ex.getMessage());}
    }
    public static void save(ModuleManager modules){
        Properties props=new Properties();
        for(ClientModule m:modules.all())props.setProperty(m.name(),Boolean.toString(m.enabled()));
        try{Files.createDirectories(FILE.getParent());try(OutputStream out=Files.newOutputStream(FILE)){props.store(out,"ZNClient settings");}}
        catch(IOException ex){System.err.println("ZNClient: Could not save config: "+ex.getMessage());}
    }
}
