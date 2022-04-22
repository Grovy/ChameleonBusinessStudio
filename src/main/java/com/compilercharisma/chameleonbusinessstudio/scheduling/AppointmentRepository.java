package com.compilercharisma.chameleonbusinessstudio.scheduling;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/** 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public interface AppointmentRepository extends CrudRepository<AppointmentEntity, Integer> {
    public List<AppointmentEntity> findAllByStartTimeGreaterThanAndEndTimeLessThan(LocalDateTime start, LocalDateTime end);
}
