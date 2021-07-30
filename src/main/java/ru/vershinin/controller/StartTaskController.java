package ru.vershinin.controller;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vershinin.model.ProcessInstanceInfo;
import ru.vershinin.util.GetCurrentProcessEngine;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest/starttask")

public class StartTaskController {

    private final GetCurrentProcessEngine currentProcessEngine;

    private final TaskService taskService;

    public StartTaskController(GetCurrentProcessEngine currentProcessEngine, TaskService taskService) {
        this.currentProcessEngine = currentProcessEngine;
        this.taskService = taskService;
    }


    @PostMapping
    public ResponseEntity<String> completeTask(@RequestBody @Valid ProcessInstanceInfo processInstanceInfo) {
        ProcessEngine processEngine = currentProcessEngine.getDefaultProcessEngine();
        ///////работает только если в процессе нет параллельных задач, для них нужно проверять циклом .list()
        Task task = processEngine.getTaskService()
                .createTaskQuery()
                .active()
                .processInstanceId(processInstanceInfo.getProcessInstanceId())
                .singleResult();

        taskService.complete(task.getId());



        return ResponseEntity.ok("task complete");
    }


}

