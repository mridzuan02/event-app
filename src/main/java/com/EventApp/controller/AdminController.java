package com.EventApp.controller;

import com.EventApp.model.EventForm;
import com.EventApp.model.EventQuestion;
import com.EventApp.model.EventResponse;
import com.EventApp.repository.EventQuestionRepository;
import com.EventApp.repository.EventResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventResponseRepository responseRepo;

    @Autowired
    private EventQuestionRepository questionRepo;

    @GetMapping("/responses")
    public String viewAllResponses(Model model) {
        List<EventResponse> responses = responseRepo.findAll();
        model.addAttribute("responses", responses);
        return "views/Admin/responses";
    }

    @GetMapping("/events")
    public String viewAllEvents(Model model) {
        List<EventQuestion> allQuestions = questionRepo.findAll();

        Map<String, List<EventQuestion>> eventMap = new HashMap<>();
        for (EventQuestion q : allQuestions) {
            eventMap.computeIfAbsent(q.getEventName(), k -> new ArrayList<>()).add(q);
        }

        model.addAttribute("eventMap", eventMap);
        return "views/Admin/events";
    }

    @GetMapping("/create-event")
    public String showCreateEventForm(Model model) {
        EventForm eventForm = new EventForm();
        eventForm.getQuestions().add(new EventQuestion());
        eventForm.getQuestions().add(new EventQuestion());
        eventForm.getQuestions().add(new EventQuestion());

        model.addAttribute("form", eventForm);
        return "views/Admin/create-event";
    }

    @PostMapping("/create")
    public String handleCreateEvent(@ModelAttribute("form") EventForm form) {
        String eventName = form.getEventName();

        for (EventQuestion question : form.getQuestions()) {
            if (question.getQuestionText() != null && !question.getQuestionText().isBlank()) {
                question.setEventName(eventName); // Link question to the event
                questionRepo.save(question);
            }
        }
        return "redirect:/admin/events";
    }

    @GetMapping("/edit")
    public String editEvent(@RequestParam String name, Model model) {
        List<EventQuestion> questions = questionRepo.findByEventName(name);
        EventForm form = new EventForm();
        form.setEventName(name);
        form.setQuestions(questions);
        model.addAttribute("form", form);
        return "views/Admin/edit-event";
    }

    @PostMapping("/update")
    public String updateEvent(@ModelAttribute("form") EventForm form) {
        questionRepo.deleteAll(questionRepo.findByEventName(form.getEventName()));
        for (EventQuestion q : form.getQuestions()) {
            q.setEventName(form.getEventName());
            if (q.getQuestionText() != null && !q.getQuestionText().isBlank()) {
                questionRepo.save(q);
            }
        }
        return "redirect:/admin/events";
    }

    @GetMapping("/delete")
    public String deleteByEventName(@RequestParam String name) {
        List<EventQuestion> questions = questionRepo.findByEventName(name);
        questionRepo.deleteAll(questions);
        return "redirect:/admin/events";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable String id) {
        Optional<EventQuestion> questionOpt = questionRepo.findById(id);
        if (questionOpt.isPresent()) {
            String eventName = questionOpt.get().getEventName();

            questionRepo.deleteAll(questionRepo.findByEventName(eventName));
        }
        return "redirect:/admin/events";
    }

}
