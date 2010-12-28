package com.snobwall.FlatherMus.Services;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceLocator {
    
    private static ServiceLocator mInstance = null;
    
    public static ServiceLocator getInstance() {
        if (mInstance == null) {
            mInstance = new ServiceLocator();
        }
        return mInstance;
    }
    
    
    protected Logger mLogger;
    
    private ServiceLocator() {
        mLogger = Logger.getLogger("com.snobwall.FlatherMus");
        
        enumerateServices();
    }
    
    protected void enumerateServices() {
        mLogger.log(Level.FINER, "Enumerating services");
        
        File pluginDir = new File("/Users/mrb/code/FlatherMus/bin/plugins/");
        File[] plugins = pluginDir.listFiles();
        
        LinkedList<URL> fileURLs = new LinkedList<URL>();
        
        for (File plugin : plugins) {
            mLogger.log(Level.FINER, plugin.toURI().toString());
            
            try {
                fileURLs.add(plugin.toURI().toURL());
            } catch(MalformedURLException mue) {
                mLogger.log(Level.WARNING, "MalformedURLException: " + mue.toString());
            }
            
        }
        
        ClassLoader loader = URLClassLoader.newInstance(fileURLs.toArray(new URL[0]));
        
        try {
            Class foo = loader.loadClass("com.example.FunService.FunService");
            
            TestService inst = (TestService)foo.newInstance();
            
            String msg = inst.get();
            
            mLogger.log(Level.FINER, "Message: " + msg);
            
        } catch(Exception e) {
            mLogger.log(Level.WARNING, "Exception: " + e.toString());
        }
    }
    
    protected Collection<Class> mClasses;
    protected Collection<ServiceEntrypoint> mServices;
    
    
}
