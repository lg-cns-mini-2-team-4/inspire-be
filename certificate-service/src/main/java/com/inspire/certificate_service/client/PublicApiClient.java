package com.inspire.certificate_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublicApiClient {

    @Value("${api.qnet.service-key}")
    private String serviceKey;

    @Value("${api.qnet.certificate-url}")
    private String certificateUrl;

    @Value("${api.qnet.schedule-url}")
    private String scheduleUrl;

    private final WebClient webClient = WebClient.create();

    // 자격증 종목 목록 XML 가져오기
    public String fetchCertificateList() {
        try {
            return webClient.get()
                    .uri(certificateUrl + "?serviceKey=" + serviceKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("자격증 목록 API 호출 실패: {}", e.getMessage());
            return null;
        }
    }

    // 시험 일정 JSON 가져오기 (연도별)
public String fetchScheduleList(String year, String itemCode, String qualgbCd) {
    try {
        return webClient.get()
                .uri(scheduleUrl + "?serviceKey=" + serviceKey
                        + "&implYy=" + year
                        + "&jmCd=" + itemCode
                        + "&qualgbCd=" + qualgbCd
                        + "&dataFormat=json"
                        + "&numOfRows=10&pageNo=1")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    } catch (Exception e) {
        log.error("시험일정 API 호출 실패 ({}년 {}): {}", year, itemCode, e.getMessage());
        return null;
}
}
}