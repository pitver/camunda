package junitTest;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;


@Deployment(resources = "sample.bpmn")
public class ApplicationTest extends AbstractProcessEngineRuleTest {



    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void startAndFinishProcessOne() {
        autoMock("sample.bpmn");
        Map<String,Object> variables = new HashMap<>();
        variables.put("One", Boolean.TRUE);

        ProcessInstance processInstance = processEngine().getRuntimeService()
                .startProcessInstanceByKey(ProcessConstantsTest.PROCESS_DEFINITION_KEY, ProcessConstantsTest.PROCESS_BUSINESS_KEY,variables);

        assertThat(processInstance).isStarted().isWaitingAt("ReciveTask");

        processEngine().getRuntimeService().createMessageCorrelation("TestMessage")
                .processInstanceBusinessKey(ProcessConstantsTest.PROCESS_BUSINESS_KEY)
                .correlate();

         assertThat(processInstance).isStarted().isNotWaitingAt("ReciveTask");

        assertThat(processInstance).isWaitingAt("ServiceTask_1");
        execute(job());
        assertThat(processInstance).isStarted().isNotWaitingAt("ServiceTask_1");
        assertThat(processInstance).isWaitingAt("UserTask_2");
        assertThat(processInstance).isNotWaitingAt("UserTask_3");
        complete(task());
        assertThat(processInstance).isEnded();
    }
    @Test
    public void startAndFinishProcessNotOne() {
        autoMock("sample.bpmn");
        Map<String,Object> variables = new HashMap<>();
        variables.put("One", Boolean.FALSE);

        ProcessInstance processInstance = processEngine().getRuntimeService()
                .startProcessInstanceByKey(ProcessConstantsTest.PROCESS_DEFINITION_KEY, ProcessConstantsTest.PROCESS_BUSINESS_KEY,variables);

        assertThat(processInstance).isStarted().isWaitingAt("ReciveTask");

        processEngine().getRuntimeService().createMessageCorrelation("TestMessage")
                .processInstanceBusinessKey(ProcessConstantsTest.PROCESS_BUSINESS_KEY)
                .correlate();


        assertThat(processInstance).isStarted().isNotWaitingAt("ReciveTask");

        assertThat(processInstance).isWaitingAt("ServiceTask_1");
        execute(job());
        assertThat(processInstance).isStarted().isNotWaitingAt("ServiceTask_1");
        assertThat(processInstance).isNotWaitingAt("UserTask_2");
        assertThat(processInstance).isWaitingAt("UserTask_3");
        complete(task());
        assertThat(processInstance).isEnded();
    }
}