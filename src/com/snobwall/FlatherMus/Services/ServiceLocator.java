package com.snobwall.FlatherMus.Services;

import java.io.File;
import java.io.FilenameFilter;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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
    
    protected LinkedList<URL> getJarURLs(File location) {
        File[] plugins = location.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.toLowerCase().endsWith(".jar")) {
                    return true;
                }
                return false;
            }
        });
        
        LinkedList<URL> fileURLs = new LinkedList<URL>();
        
        for (File plugin : plugins) {
            try {
                fileURLs.add(plugin.toURI().toURL());
            } catch(MalformedURLException mue) {
                mLogger.log(Level.WARNING, "Exception: " + mue.toString());
            }
        }
        
        return fileURLs;
    }
    
    protected LinkedList<String> getJarClasses(Collection<URL> jarURLs) {
        LinkedList<String> classNames = new LinkedList<String>();
        
        for (URL jarURL : jarURLs) {
            try {
                URL jarConnURL = new URL("jar:" + jarURL.toString() + "!/");
                JarURLConnection juc = (JarURLConnection)jarConnURL.openConnection();
                
                JarFile jf = juc.getJarFile();
                Enumeration<JarEntry> ej = jf.entries();
                
                while (ej.hasMoreElements()) {
                    JarEntry je = ej.nextElement();
                    
                    String name = je.getName();
                    
                    if (name.endsWith(".class")) {
                        String className = name.replaceFirst("\\.class$", "").replace('/', '.');
                        classNames.add(className);
                        mLogger.log(Level.FINER, "Entry: " + className);
                    }
                }
                
            } catch(Exception e) {
                mLogger.log(Level.WARNING, "Exception: " + e.toString());
            }
            
        }
        
        
        return classNames;
    }
    
    protected void enumerateServices() {
        mLogger.log(Level.FINER, "Enumerating services");
        
        File pluginDir = new File("/Users/mrb/code/FlatherMus/bin/plugins/");
        LinkedList<URL> jarURLs = getJarURLs(pluginDir);
        
        LinkedList<String> pluginClasses = getJarClasses(jarURLs);
        

        ClassLoader loader = new URLClassLoader(jarURLs.toArray(new URL[0]));
        
        for (String className : pluginClasses) {
         
            try {
                Class foo = loader.loadClass(className);
                
                TestService inst = (TestService)foo.newInstance();
                String msg = inst.get();
                mLogger.log(Level.FINER, "Message: " + msg);
                
            } catch(Exception e) {
                mLogger.log(Level.WARNING, "Exception: " + e.toString());
            }
        }
    }
    
    protected Collection<Class> mClasses;
    protected Collection<ServiceEntrypoint> mServices;
    
    
}
