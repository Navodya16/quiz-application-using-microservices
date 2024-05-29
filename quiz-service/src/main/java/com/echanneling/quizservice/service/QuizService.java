package com.echanneling.quizservice.service;

import com.echanneling.quizservice.dao.QuizDao;
import com.echanneling.quizservice.feign.QuizInterface;
import com.echanneling.quizservice.model.QuestionWrapper;
import com.echanneling.quizservice.model.Quiz;
import com.echanneling.quizservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;
    
    @Autowired
    QuizInterface quizInterface;


    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
    	List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
    	Quiz quiz = new Quiz();
    	quiz.setTitle(title);
    	quiz.setQuestionIds(questions);
    	quizDao.save(quiz);
    	
    	return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionids = quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionids);

        return questions;

    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        
        return score;
    }
}
