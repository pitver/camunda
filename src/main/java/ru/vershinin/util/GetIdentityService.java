package ru.vershinin.util;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.IdentityServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class GetIdentityService {
    private final GetCurrentProcessEngine currentProcessEngine;

    public GetIdentityService(GetCurrentProcessEngine currentProcessEngine) {
        this.currentProcessEngine = currentProcessEngine;
    }

    public IdentityServiceImpl getIdentityService() {
        ProcessEngine processEngine = currentProcessEngine.getDefaultProcessEngine();
        return (IdentityServiceImpl) processEngine.getIdentityService();
    }
}
