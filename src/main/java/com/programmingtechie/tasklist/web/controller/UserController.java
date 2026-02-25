package com.programmingtechie.tasklist.web.controller;

import com.programmingtechie.tasklist.domain.task.Task;
import com.programmingtechie.tasklist.domain.user.User;
import com.programmingtechie.tasklist.service.TaskService;
import com.programmingtechie.tasklist.service.UserService;
import com.programmingtechie.tasklist.web.dto.mappers.TaskMapper;
import com.programmingtechie.tasklist.web.dto.mappers.UserMapper;
import com.programmingtechie.tasklist.web.dto.task.TaskDto;
import com.programmingtechie.tasklist.web.dto.user.UserDto;
import com.programmingtechie.tasklist.web.dto.validation.OnCreate;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User Controller", description = "User API")
public class UserController {
    private final UserService userService;
    private final TaskService taskService;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    @MutationMapping(name = "updateUser")
    @PreAuthorize("@customSecurtiyExpression.canAccessUser(#userDto.id)")
    @Operation(summary = "update user")
    @PutMapping
    public UserDto update(@Validated(OnUpdate.class) @RequestBody @Argument final UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @Operation(summary = "user get by id")
    @GetMapping("/{id}")
    @QueryMapping(name = "userById")
    @PreAuthorize("@customSecurtiyExpression.canAccessUser(#id)")
    public UserDto getById(@PathVariable @Argument final Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @PreAuthorize("@customSecurtiyExpression.canAccessUser(#id)")
    @MutationMapping(name = "deleteUserById")
    @Operation(summary = "delete user")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Argument final Long id) {
        userService.delete(id);
    }

    @PreAuthorize("@customSecurtiyExpression.canAccessUser(#id)")
    @Operation(summary = "find task by userid")
   // @GetMapping("/{id}/tasks") @PathVariable
    @QueryMapping(name = "tasksByUserId")
    public List<TaskDto> getTasksByUserId( @Argument final Long id) {
        List<Task> tasks = taskService.getAllByUser(id);
        return  taskMapper.toDto(tasks);
    }

    @PostMapping("/{id}/tasks")
    @MutationMapping(name = "createTask")
    @Operation(summary = "Add task to user")
    @PreAuthorize("@customSecurtiyExpression.canAccessUser(#id)")
    public TaskDto createTask(@PathVariable @Argument final Long id, @Validated(OnCreate.class)
    @RequestBody @Argument final TaskDto dto) {
        Task task = taskMapper.toEntity(dto);
        Task createdTask = taskService.create(task, id);
        return taskMapper.toDto(createdTask);

    }


}
