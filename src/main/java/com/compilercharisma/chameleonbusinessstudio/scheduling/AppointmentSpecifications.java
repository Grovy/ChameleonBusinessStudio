package com.compilercharisma.chameleonbusinessstudio.scheduling;

import java.util.Set;
import java.time.LocalDateTime;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications are applied at the query level to filter and sort entities
 * retrieved from the database.
 * 
 * The primary advantages of using this system is that they are applied before
 * transforming from database records to java objects, which is faster than
 * filtering within the java program; additionally, Specifications can be 
 * chained together (or, and, etc.) for great re-usability.
 * 
 * However, these will likely be unusable after we transition to using Vendia as
 * our data store, though we can just use their native API language instead of
 * using JPA to translate. Additionally, Specifications are limited to operating
 * at the database level, so we cannot perform operations using them that 
 * require our methods acting on the java objects
 * 
 * Overview: https://docs.oracle.com/javaee/6/tutorial/doc/gjitv.html
 * In Spring: 
 *  https://stackoverflow.com/a/33647084
 *  https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class AppointmentSpecifications {
    /**
     * specifies that an appointment occurs entirely within the given window, 
     * neither starting before, nor ending after
     * 
     * @param start
     * @param end
     * @return 
     */
    public static Specification<AppointmentEntity> occursWithin(LocalDateTime start, LocalDateTime end){
        return (Root<AppointmentEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate p1 = cb.greaterThanOrEqualTo(root.<LocalDateTime>get("startTime"), start);
            Predicate p2 = cb.lessThanOrEqualTo(root.<LocalDateTime>get("endTime"), end);
            return cb.and(p1, p2);
        };
    }
    
    public static Specification<AppointmentEntity> isAvailable(){
        return (root, query, cb)->{
            return cb.gt(root.<Integer>get("totalSlots"), cb.size(root.<Set>get("registeredUsers")));
        };
    }
}
