package com.inspire.certificate.specification;

import com.inspire.certificate.domain.entity.Exam;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ExamSpecification {

    public static Specification<Exam> byStatus(String status, LocalDate currentDate) {

        return switch (status) {
            case "active" -> activePredicate(currentDate);
            case "upcoming" -> upcomingPredicate(currentDate);
            default -> (root, query, cb) -> cb.conjunction();
        };
    }

    public static Specification<Exam> withCertificate() {
        return (root, query, cb) -> {
            if (query != null && query.getResultType() != Long.class) {
                root.fetch("certificate", JoinType.LEFT);
                query.distinct(true);
            }
            return null;
        };
    }

    public static Specification<Exam> activePredicate(LocalDate currentDate) {
        return (root, query, cb) -> cb.and(
                cb.lessThanOrEqualTo(root.get("startDate"), currentDate),
                cb.greaterThanOrEqualTo(root.get("endDate"), currentDate)
        );
    }

    public static Specification<Exam> upcomingPredicate(LocalDate currentDate) {
        return (root, query, cb) -> cb.greaterThan(root.get("startDate"), currentDate);
    }

    public static Specification<Exam> betweenDate(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {

            Expression<LocalDate> startDate = root.get("startDate");
            Expression<LocalDate> endDate = root.get("endDate");

            if (from != null && to != null) {
                return cb.or(cb.between(startDate, from, to), cb.between(endDate, from, to));
            } else if (from != null) {
                return cb.or(cb.greaterThanOrEqualTo(startDate, from), cb.greaterThanOrEqualTo(endDate, from));
            } else if (to != null) {
                return cb.or(cb.lessThanOrEqualTo(startDate, to), cb.lessThanOrEqualTo(endDate, to));
            } else {
                return cb.conjunction();
            }
        };
    }
}
