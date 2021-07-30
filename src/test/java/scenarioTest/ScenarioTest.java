package scenarioTest;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.bpm.scenario.Scenario;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.*;


public class ScenarioTest extends AbstractTest {
    @Before
    public void setVariable() {
        variables.put("One", true);
    }

    @Rule
    @ClassRule
    public static ProcessEngineRule rule =
            TestCoverageProcessEngineRuleBuilder.create()
                    .withDetailedCoverageLogging().build();


    @Test
    @Deployment(resources = {"sample.bpmn"})
    public void testGlassItemOrderingScenarioPathOne() {
        final AtomicReference<String> holder = new AtomicReference<>();

        when(scenario.waitsAtReceiveTask("ReciveTask")).thenReturn(message -> {
            holder.set(message.getProcessInstance().getBusinessKey());
            message.receive();
        });
        when(scenario.waitsAtUserTask("UserTask_2"))
                .thenReturn(task -> task.complete());
        when(scenario.waitsAtUserTask("UserTask_3"))
                .thenReturn(task -> task.complete());

        Scenario.run(scenario).
                startByKey(ProcessConstantsTest.PROCESS_DEFINITION_KEY, ProcessConstantsTest.PROCESS_BUSINESS_KEY, variables)
                .execute();

        verify(scenario, times(1)).hasStarted("ReciveTask");
        verify(scenario, times(1)).hasFinished("ReciveTask");
        verify(scenario, times(1)).hasCompleted("ReciveTask");
        verify(scenario, times(1)).hasFinished("ServiceTask_1");
        verify(scenario, times(1)).hasCompleted("ServiceTask_1");

        verify(scenario, times(1)).hasStarted("UserTask_2");
        verify(scenario, times(1)).hasFinished("UserTask_2");
        verify(scenario, never()).hasStarted("UserTask_3");
        verify(scenario, never()).waitsAtUserTask("UserTask_3");
        verify(scenario, never()).hasFinished("UserTask_3");
        verify(scenario, times(1)).hasFinished("EndEvent_1");

    }

    @Test
    @Deployment(resources = {"sample.bpmn"})
    public void testGlassItemOrderingScenarioPathNotOne() {
        final AtomicReference<String> holder = new AtomicReference<>();


        when(scenario.waitsAtReceiveTask("ReciveTask")).thenReturn(message -> {
            holder.set(message.getProcessInstance().getBusinessKey());
            message.receive();
        });
        when(scenario.waitsAtUserTask("UserTask_2"))
                .thenReturn(task -> task.complete());
        when(scenario.waitsAtUserTask("UserTask_3"))
                .thenReturn(task -> task.complete());

        variables.put("One",false);

        Scenario.run(scenario).
                startByKey(ProcessConstantsTest.PROCESS_DEFINITION_KEY, ProcessConstantsTest.PROCESS_BUSINESS_KEY, variables)
                .execute();

        verify(scenario, times(1)).hasStarted("ReciveTask");
        verify(scenario, times(1)).hasFinished("ReciveTask");
        verify(scenario, times(1)).hasCompleted("ReciveTask");
        verify(scenario, times(1)).hasFinished("ServiceTask_1");
        verify(scenario, times(1)).hasCompleted("ServiceTask_1");


        verify(scenario, times(1)).hasStarted("UserTask_3");
        verify(scenario,times(1)).hasFinished("UserTask_3");

        verify(scenario, never()).hasStarted("UserTask_2");
        verify(scenario, never()).waitsAtUserTask("UserTask_2");
        verify(scenario, never()).hasFinished("UserTask_2");
        verify(scenario, times(1)).hasFinished("EndEvent_1");

    }
}
