package com.projectsvadim.vadimbot.repository;

import com.projectsvadim.vadimbot.enity.User.User;
import com.projectsvadim.vadimbot.enity.task.CompleteStatus;
import com.projectsvadim.vadimbot.enity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepo extends JpaRepository<Task, UUID> {
    Task findTaskByUsersContainingAndIsInCreation(User user, Boolean isInCreation);

    void deleteByUsersContainingAndIsInCreation(User user, Boolean isInCreation);

    int countAllByUsersContainingAndIsFinishedAndCompleteStatus(User user,
                                                                Boolean isFinished,
                                                                CompleteStatus completeStatus);
}
