package ru.vershinin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.vershinin.model.ProcessInstanceInfo;
import ru.vershinin.util.GetCurrentProcessEngine;
import ru.vershinin.util.ProvidedProcessEngineRule;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yaml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StartTaskControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private StartTaskController startTaskController;


    protected ProvidedProcessEngineRule engineRule = new ProvidedProcessEngineRule();
    protected ProcessEngine processEngine;
    protected TaskService taskService;




    @Order(1)
    @Test
    void createTask() throws Exception {
ProcessInstanceInfo processInstanceInfo = new ProcessInstanceInfo();
        processEngine = engineRule.getProcessEngine();
        taskService = processEngine.getTaskService();
        Task task = taskService.newTask();
        task.setName("testTask");
        taskService.saveTask(task);
        String processInstanceId = task.getId();
        processInstanceInfo.setProcessInstanceId(processInstanceId);



        List<Task> tasks = taskService.createTaskQuery().taskId(processInstanceId).list();

        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals("testTask", tasks.get(0).getName());

        taskService.deleteTask(processInstanceId,true);
        Assertions.assertNull(taskService.createTaskQuery().taskId(processInstanceId).singleResult());


    }
    @Order(2)
    @Test
     void test() throws Exception {
        assertThat(startTaskController).isNotNull();
    }
}