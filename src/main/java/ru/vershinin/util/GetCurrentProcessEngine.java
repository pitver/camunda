package ru.vershinin.util;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.rest.spi.ProcessEngineProvider;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class GetCurrentProcessEngine implements ProcessEngineProvider {

    @Override
    public ProcessEngine getDefaultProcessEngine() {
        return ProcessEngines.getDefaultProcessEngine();
    }

    @Override
    public ProcessEngine getProcessEngine(String name) {
        return ProcessEngines.getProcessEngine(name);
    }

    @Override
    public Set<String> getProcessEngineNames() {
        return ProcessEngines.getProcessEngines().keySet();
    }
}

