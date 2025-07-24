package com.EventApp.controller;

import com.EventApp.model.EventQuestion;
import com.EventApp.model.EventResponse;
import com.EventApp.repository.EventQuestionRepository;
import com.EventApp.repository.EventResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EventController
{
    @Autowired
    private EventQuestionRepository questionRepo;

    @Autowired
    private EventResponseRepository responseRepo;

    @GetMapping("/events")
    public String showEvents(Model model) {
        List<EventQuestion> allQuestions = questionRepo.findAll();

        // Extract unique event names only
        Set<String> uniqueEventNames = allQuestions.stream()
                .map(EventQuestion::getEventName)
                .collect(Collectors.toCollection(LinkedHashSet::new)); // maintains order

        model.addAttribute("uniqueEventNames", uniqueEventNames);
        return "views/Event/events"; // or your correct path
    }

    @GetMapping("/register-event")
    public String showEventForm(@RequestParam String event, Model model)
    {
        List<EventQuestion> questions = questionRepo.findByEventName(event);
        model.addAttribute("eventName", event);
        model.addAttribute("questions", questions);
        return "views/Event/event-form";
    }

    @PostMapping("/submit-event")
    public String submitAnswers(@RequestParam String event,
                                @RequestParam Map<String, String> params,
                                @RequestParam MultiValueMap<String, String> multiParams,
                                Principal principal) {
        System.out.println("Received event: " + event);
        System.out.println("Submitted answers: " + multiParams);

        Map<String, String> singleValuedAnswers = new HashMap<>();

        for (String key : multiParams.keySet()) {
            List<String> values = multiParams.get(key);
            if (values != null) {
                if (values.size() == 1) {
                    singleValuedAnswers.put(key, values.get(0));
                } else {
                    singleValuedAnswers.put(key, String.join(", ", values)); // merge multiple checkbox values
                }
            }
        }

        singleValuedAnswers.remove("event");

        EventResponse response = new EventResponse();
        response.setUsername(principal.getName());
        response.setEventName(event);
        response.setAnswers(singleValuedAnswers);

        responseRepo.save(response);
        System.out.println("Saved response: " + response);

        return "redirect:/thank-you";
    }

    @GetMapping("/thank-you")
    public String thankYou() {
        return "views/Event/thank-you";
    }

}
