package com.example.rent_yourcomfort.service.impl;

import com.example.rent_yourcomfort.dto.FeedbackDTO;
import com.example.rent_yourcomfort.exception.EmptyFeedbacksException;
import com.example.rent_yourcomfort.exception.HousingNotFoundException;
import com.example.rent_yourcomfort.model.Feedback;
import com.example.rent_yourcomfort.model.Housing;
import com.example.rent_yourcomfort.repository.FeedBackRepository;
import com.example.rent_yourcomfort.repository.HousingRepository;
import com.example.rent_yourcomfort.repository.UserRepository;
import com.example.rent_yourcomfort.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final HousingRepository housingRepository;
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;

    @Autowired
    public FeedbackServiceImpl(HousingRepository housingRepository, FeedBackRepository feedBackRepository, UserRepository userRepository) {
        this.housingRepository = housingRepository;
        this.feedBackRepository = feedBackRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        if (!feedback.getComment().equals("")) {
            feedBackRepository.save(feedback);
        } else throw new RuntimeException("Feedback cannot be empty!");
        return feedback;
    }

    @Override
    public List<FeedbackDTO> getFeedbacks(Long housingId) {

        Optional<Housing> housing = housingRepository.findById(housingId);
        if (housing.isEmpty()) {
            throw new HousingNotFoundException(String.valueOf(housingId));
        }

        List<Feedback> feedbacks = feedBackRepository.findByHousingId(housing.get());

        if (!feedbacks.isEmpty()) {
            return feedbacks.stream()
                    .map(FeedbackDTO::convertToDTO).toList();
        } else {
            throw new EmptyFeedbacksException("Review from housingId is empty!");
        }
    }

    @Override
    public Float getAnAverageHousingScore(Long housingId) {
        Optional<Housing> housing = housingRepository.findById(housingId);
        List<Feedback> feedbacks = feedBackRepository.findByHousingId(housing.get());
        if (feedbacks.isEmpty()) {
            return 0.0f;
        }
        int totalStars = feedbacks.stream().mapToInt(Feedback::getCountStars).sum();
        return (float) totalStars / feedbacks.size();
    }
}

