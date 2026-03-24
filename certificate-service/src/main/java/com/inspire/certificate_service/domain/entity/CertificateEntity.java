// package com.example.certificate_service.domain.entity;

// import jakarta.persistence.*;
// import lombok.*;

// @Entity
// @Table(name = "certificate")
// @Getter
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
// @AllArgsConstructor
// @Builder
// public class CertificateEntity {

//     @Id
//     @Column(name = "item_code")
//     private String itemCode; // 종목 코드 (PK)

//     @Column(name = "item_name", nullable = false)
//     private String itemName; // 종목 이름

//     @Column(name = "cert_type_code")
//     private String certTypeCode; // 자격구분 코드

//     @Column(name = "cert_type_name")
//     private String certTypeName; // 자격구분명

//     @Column(name = "series_code")
//     private String seriesCode; // 계열코드

//     @Column(name = "series_name")
//     private String seriesName; // 계열명

//     @Column(name = "large_field_code")
//     private String largeFieldCode; // 대직무분야 코드

//     @Column(name = "large_field_name")
//     private String largeFieldName; // 대직무분야 명

//     @Column(name = "medium_field_code")
//     private String mediumFieldCode; // 중직무분야 코드

//     @Column(name = "medium_field_name")
//     private String mediumFieldName; // 중직무분야 명
// }


package com.inspire.certificate_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.ColumnDefault;


@Entity
@Table(name = "certificate")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicInsert
public class CertificateEntity {

    @Id
    @Column(name = "item_code")
    private String itemCode;                // 종목 코드 (PK)

    @Column(name = "item_name", nullable = false)
    private String itemName;                // 종목 이름

    @Column(name = "cert_type_code")
    private String certTypeCode;            // 자격구분 코드

    @Column(name = "cert_type_name")
    private String certTypeName;            // 자격구분명

    @Column(name = "series_code")
    private String seriesCode;              // 계열코드

    @Column(name = "series_name")
    private String seriesName;              // 계열명

    @Column(name = "large_field_code")
    @ColumnDefault("'00'")
    @Builder.Default
    private String largeFieldCode = "00";   // 대직무분야 코드

    @Column(name = "large_field_name")
    @ColumnDefault("'기타'")
    @Builder.Default
    private String largeFieldName = "기타"; // 대직무분야 명

}