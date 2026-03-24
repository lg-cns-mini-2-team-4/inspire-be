package com.inspire.certificate.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "certificates")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Certificate {
    @Id
    @Column(name = "item_code")
    private String itemCode; // 종목 코드 (PK)

    @Column(name = "item_name", nullable = false)
    private String itemName; // 종목 이름

    @Column(name = "cert_type_code")
    private String certTypeCode; // 자격구분 코드

    @Column(name = "cert_type_name")
    private String certTypeName; // 자격구분명

    @Column(name = "series_code")
    private String seriesCode; // 계열코드

    @Column(name = "series_name")
    private String seriesName; // 계열명

    @Column(name = "large_field_code")
    private String largeFieldCode; // 대직무분야 코드

    @Column(name = "large_field_name")
    private String largeFieldName; // 대직무분야 명

    @Column(name = "medium_field_code")
    private String mediumFieldCode; // 중직무분야 코드

    @Column(name = "medium_field_name")
    private String mediumFieldName; // 중직무분야 명

    @Builder
    public Certificate(String itemCode, String itemName, String certTypeCode, String certTypeName, String seriesCode, String seriesName, String largeFieldCode, String largeFieldName, String mediumFieldCode, String mediumFieldName) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.certTypeCode = certTypeCode;
        this.certTypeName = certTypeName;
        this.seriesCode = seriesCode;
        this.seriesName = seriesName;
        this.largeFieldCode = largeFieldCode;
        this.largeFieldName = largeFieldName;
        this.mediumFieldCode = mediumFieldCode;
        this.mediumFieldName = mediumFieldName;
    }
}
