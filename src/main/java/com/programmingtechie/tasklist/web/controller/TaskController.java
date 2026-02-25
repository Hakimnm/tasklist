package com.programmingtechie.tasklist.web.controller;

import com.programmingtechie.tasklist.domain.task.Task;
import com.programmingtechie.tasklist.domain.task.TaskImage;
import com.programmingtechie.tasklist.service.TaskService;
import com.programmingtechie.tasklist.web.dto.mappers.TaskImageMapper;
import com.programmingtechie.tasklist.web.dto.mappers.TaskMapper;
import com.programmingtechie.tasklist.web.dto.task.TaskDto;
import com.programmingtechie.tasklist.web.dto.task.TaskImageDto;
import com.programmingtechie.tasklist.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskImageMapper taskImageMapper;

    @Operation(summary = "task find get by id")
    @GetMapping("/{id}")
    @PreAuthorize("@customSecurtiyExpression.canAccessTask(#id)")
    @QueryMapping(name = "taskById")
    public TaskDto getById(@PathVariable @Argument final Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @Operation(summary = "delete task")
    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurtiyExpression.canAccessTask(#id)")
    @MutationMapping(name = "deleteTask")
    public void deleteById(@PathVariable @Argument final Long id) {
        taskService.delete(id);
    }

    @Operation(summary = "update task")
    @PutMapping
    @PreAuthorize("@customSecurtiyExpression.canAccessTask(#id)")
    @MutationMapping(name = "updateTask")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody @Argument final TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);

    }

    @PostMapping("/id/image")
    @Operation(summary = "upload image to task")
    @PreAuthorize("canAccessTask(#id)")
    public void uploadImage(@PathVariable Long id,
                            @Validated @ModelAttribute TaskImageDto imageDto) {

        TaskImage image = taskImageMapper.toEntity(imageDto);
        taskService.taskImageUpload(id, image);

    }


}


