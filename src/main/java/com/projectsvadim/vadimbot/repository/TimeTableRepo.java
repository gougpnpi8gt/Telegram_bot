package com.projectsvadim.vadimbot.repository;

import com.projectsvadim.vadimbot.enity.User.User;
import com.projectsvadim.vadimbot.enity.timetable.TimeTable;
import com.projectsvadim.vadimbot.enity.timetable.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimeTableRepo extends JpaRepository<TimeTable, UUID> {
    List<TimeTable> findAllByUsersContainingAndWeekDayAndInCreation(User user,
                                                                    WeekDay weekDay,
                                                                    Boolean isInCreation);

    TimeTable findTimeTableById(UUID id);

}
