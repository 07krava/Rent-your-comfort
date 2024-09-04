package com.example.rent_yourcomfort.service;

import com.example.rent_yourcomfort.dto.FeedbackDTO;
import com.example.rent_yourcomfort.model.Feedback;
import java.util.List;

public interface FeedbackService {

    Feedback createFeedback(Feedback feedback);

    List<FeedbackDTO> getFeedbacks(Long housingId);

    Float getAnAverageHousingScore(Long housingId);

}
