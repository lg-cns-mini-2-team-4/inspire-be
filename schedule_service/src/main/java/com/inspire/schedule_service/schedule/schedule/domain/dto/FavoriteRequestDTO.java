package com.inspire.schedule_service.schedule.schedule.domain.dto;

import java.util.List;

public class FavoriteRequestDTO {
    public class FavoriteSaveRequestDTO {
    // 자격증 카드 정보
    private String itemCode;
    private String itemName;
    private String officeName;

    // 달력에 뿌려질 일정 리스트 (우리가 만든 그 리스트!)
    private List<ScheduleRequestDTO> schedules;


    // json
    
    // {
    // "itemCode": "440",
    // "itemName": "정보처리기사",
    // "officeName": "한국산업인력공단",
    // "schedules": [
    //     { "title": "[정처기] 필기시험", "date": "2026-03-15", "type": "CERT", "examId": "440" },
    //     { "title": "[정처기] 실기시험", "date": "2026-04-25", "type": "CERT", "examId": "440" }
    // ]
    // }
}
}
