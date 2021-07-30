package ru.vershinin.util;

import java.util.concurrent.Callable;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.test.ProcessEngineRule;

public class ProvidedProcessEngineRule extends ProcessEngineRule {


    /**
     * The one process engine created from camunda.cfg.xml.
     * To save the effort of building unnecessary process engines, it should
     * be used in any test that does not require extra engine configuration.
     * It should not be reconfigured on the fly (=> violates test isolation).
     * If that cannot be avoided a test must make sure to restore the original
     * configuration.
     */
    protected static ProcessEngine cachedProcessEngine;

    protected Callable<ProcessEngine> processEngineProvider;

    public ProvidedProcessEngineRule() {
        super(getOrInitializeCachedProcessEngine(), true);
    }

    public ProvidedProcessEngineRule(final ProcessEngineBootstrapRule bootstrapRule) {
        this(() -> bootstrapRule.getProcessEngine());
    }

    public ProvidedProcessEngineRule(Callable<ProcessEngine> processEngineProvider) {
        super(true);
        this.processEngineProvider = processEngineProvider;
    }

    @Override
    protected void initializeProcessEngine() {

        if (processEngineProvider != null) {
            try {
                this.processEngine = processEngineProvider.call();
            } catch (Exception e) {
                throw new RuntimeException("Could not get process engine", e);
            }
        }
        else {
            super.initializeProcessEngine();
        }
    }

    protected static ProcessEngine getOrInitializeCachedProcessEngine() {
        if (cachedProcessEngine == null) {
            cachedProcessEngine = ProcessEngineConfiguration
                    .createProcessEngineConfigurationFromResource("camunda.cfg.xml")
                    .buildProcessEngine();
        }
        return cachedProcessEngine;
    }

}
