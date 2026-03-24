package com.inspire.certificate_service.dao;

import com.inspire.certificate_service.domain.entity.CertificateEntity;
import com.inspire.certificate_service.domain.entity.ExamEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Set;

public class ExamSpecification {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Specification<ExamEntity> withCertificate(String itemName, String fieldCode) {
        return (root, query, cb) -> {
            Join<ExamEntity, CertificateEntity> cert = (Join) root.fetch("certificate", JoinType.LEFT);
            assert query != null;
            query.distinct(true);

            Predicate predicate = cb.conjunction();

            if (itemName != null && !itemName.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(cert.get("itemName"), "%" + itemName + "%"));
            }

            if (fieldCode != null && !fieldCode.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.equal(cert.get("largeFieldCode"), fieldCode));
            }

            return predicate;
        };
    }
//
//    // 2. 종목명 검색 (Partial Match)
//    public static Specification<ExamEntity> hasItemName(String itemName) {
//        return (root, query, cb) -> {
//            if (itemName == null || itemName.isEmpty()) return null;
//            return cb.like(root.join("certificate").get("itemName"), "%" + itemName + "%");
//        };
//    }
//
//    // 3. 대직무분야 필터링
//    public static Specification<ExamEntity> hasLargeField(String fieldCode) {
//        return (root, query, cb) -> {
//            if (fieldCode == null || fieldCode.isEmpty()) return null;
//            return cb.equal(root.join("certificate").get("largeFieldCode"), fieldCode);
//        };
//    }

    // 4. 상태 필터링 (접수중 / 접수예정)
    public static Specification<ExamEntity> hasStatus(String status, LocalDate currentDate) {
        if(status == null || status.isBlank()) {
            return (root, query, cb) -> cb.conjunction();
        }

        return switch (status) {
            case "active" -> activePredicate(currentDate);
            case "upcoming" -> upcomingPredicate(currentDate);
            default -> (root, query, cb) -> cb.conjunction();
        };
    }

    public static Specification<ExamEntity> isWithinRange(LocalDate from, LocalDate to) {
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

    public static Specification<ExamEntity> activePredicate(LocalDate currentDate) {
        return (root, query, cb) -> cb.and(
                cb.lessThanOrEqualTo(root.get("startDate"), currentDate),
                cb.greaterThanOrEqualTo(root.get("endDate"), currentDate)
        );
    }

    public static Specification<ExamEntity> upcomingPredicate(LocalDate currentDate) {
        return (root, query, cb) -> cb.greaterThan(root.get("startDate"), currentDate);
    }
//
//    /**
//     * 5. 특정 기간(startDate ~ endDate) 내에 진행되는 시험 필터링
//     */
//    public static Specification<ExamEntity> isWithinRange(LocalDate start, LocalDate end) {
//        return (root, query, cb) -> {
//            if (start == null || end == null) return null;
//
//            // 시험의 시작일이 조회 종료일보다 작거나 같고,
//            // 시험의 종료일이 조회 시작일보다 크거나 같은 경우 (기간 중첩)
//            return cb.and(
//                cb.lessThanOrEqualTo(root.get("startDate"), end),
//                cb.greaterThanOrEqualTo(root.get("endDate"), start)
//            );
//        };
//    }
}