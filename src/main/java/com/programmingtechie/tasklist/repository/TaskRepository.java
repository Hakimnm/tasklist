package com.programmingtechie.tasklist.repository;

import com.programmingtechie.tasklist.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
            select t.* from tasklist.tasks t 
            join tasklist.users_task ut on t.id = ut.task_id
            where ut.user_id = :userId
            """, nativeQuery = true)
    List<Task> findAllByUserId(Long userId);
    @Modifying
    @Query(value = """
            INSERT INTO users_task (user_id, task_id)
            VALUES (:userId, :taskId)
            """, nativeQuery = true)
    void assignTask(
            @Param("userId") Long userId,
            @Param("taskId") Long taskId
    );
}
