package com.programmingtechie.tasklist.domain.task;

import com.programmingtechie.tasklist.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime expirationDate;

    @ElementCollection
    @CollectionTable(
            name = "tasks_images",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "image")
    private List<String> images;


}
