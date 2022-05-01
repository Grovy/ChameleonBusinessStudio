package com.compilercharisma.chameleonbusinessstudio.appointments;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/** 
 * can use findAll(AppointmentSpecification.xyz)
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public interface AppointmentRepository extends PagingAndSortingRepository<AppointmentEntity, Integer>, JpaSpecificationExecutor<AppointmentEntity> {
    public List<AppointmentEntity> findAllByStartTimeGreaterThanAndEndTimeLessThan(LocalDateTime start, LocalDateTime end);
    public List<AppointmentEntity> findAllByStartTimeGreaterThanAndEndTimeLessThan(LocalDateTime start, LocalDateTime end, Pageable sort);
}
