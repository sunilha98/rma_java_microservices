package com.resourcemgmt.reports.feigns;

import com.resourcemgmt.reports.feigns.interfaces.LessonsClient;
import com.resourcemgmt.reports.reports.dto.LessonLearnedDTO;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
public class LessonsFeignFallback implements LessonsClient {

    @Override
    public List<LessonLearnedDTO> getLessonsLearnedReports(String bearerToken) {
        System.out.println("Fallback triggered: Lessons service unavailable");
        return List.of();
    }
}
