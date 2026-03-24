package com.inspire.schedule_service.schedule.schedule.specification;

import com.inspire.schedule_service.schedule.schedule.domain.entity.ScheduleEntity;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ScheduleSpecification {

    public static Specification<ScheduleEntity> withUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("userId"), userId);
        };
    }

    public static Specification<ScheduleEntity> betweenDate(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {

            Expression<LocalDate> date = root.get("date");

            if (from != null && to != null) {
                return cb.between(date, from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(date, from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(date, to);
            } else {
                return cb.conjunction();
            }
        };
    }
}
