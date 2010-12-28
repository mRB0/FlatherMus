package com.snobwall.FlatherMus.Services;

import java.util.Collection;

public interface ServiceEntrypoint extends ServiceBase {
    
    public abstract Collection<String> supportedServiceTypes();
    public abstract boolean querySupportedServiceTypes(String serviceType);
    
    public abstract ServiceBase getInstance();
    
}
