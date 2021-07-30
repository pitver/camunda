package ru.vershinin.util;

import java.util.List;
import java.util.function.Consumer;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.JobEntity;
import org.camunda.bpm.engine.runtime.Job;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class ProcessEngineBootstrapRule extends TestWatcher {

    private ProcessEngine processEngine;
    protected Consumer<ProcessEngineConfigurationImpl> processEngineConfigurator;

    public ProcessEngineBootstrapRule() {
        this("camunda.cfg.xml");
    }

    public ProcessEngineBootstrapRule(String configurationResource) {
        this(configurationResource, null);
    }

    public ProcessEngineBootstrapRule(Consumer<ProcessEngineConfigurationImpl> processEngineConfigurator) {
        this("camunda.cfg.xml", processEngineConfigurator);
    }

    public ProcessEngineBootstrapRule(String configurationResource, Consumer<ProcessEngineConfigurationImpl> processEngineConfigurator) {
        this.processEngineConfigurator = processEngineConfigurator;
        this.processEngine = bootstrapEngine(configurationResource);
    }

    public ProcessEngine bootstrapEngine(String configurationResource) {
        ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource(configurationResource);
        configureEngine(processEngineConfiguration);
        return processEngineConfiguration.buildProcessEngine();
    }

    public ProcessEngineConfiguration configureEngine(ProcessEngineConfigurationImpl configuration) {
        if (processEngineConfigurator != null) {
            processEngineConfigurator.accept(configuration);
        }
        return configuration;
    }

    public ProcessEngine getProcessEngine() {
        return processEngine;
    }

    @Override
    protected void finished(Description description) {
        deleteHistoryCleanupJob();
        processEngine.close();
        ProcessEngines.unregister(processEngine);
        processEngine = null;
    }

    private void deleteHistoryCleanupJob() {
        final List<Job> jobs = processEngine.getHistoryService().findHistoryCleanupJobs();
        for (final Job job: jobs) {
            ((ProcessEngineConfigurationImpl)processEngine.getProcessEngineConfiguration()).getCommandExecutorTxRequired().execute(new Command<Void>() {
                public Void execute(CommandContext commandContext) {
                    commandContext.getJobManager().deleteJob((JobEntity) job);
                    commandContext.getHistoricJobLogManager().deleteHistoricJobLogByJobId(job.getId());
                    return null;
                }
            });
        }
    }

}

