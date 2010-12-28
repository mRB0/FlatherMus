package com.snobwall.FlatherMus

import com.snobwall.FlatherMus.Services._

import java.util.logging._


object Main {
    
    val logger : Logger = Logger.getLogger("com.snobwall.FlatherMus");
    
    def main(args : Array[String]) : Unit = {
        var handlers = Logger.getLogger("").getHandlers()
        handlers.foreach(handler => handler.setLevel(Level.FINEST))
        logger.setLevel(Level.FINEST);
        
        var locator = ServiceLocator.getInstance()
        
        
        
    }
}
