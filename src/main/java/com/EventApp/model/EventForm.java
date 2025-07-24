package com.EventApp.model;

import java.util.ArrayList;
import java.util.List;

public class EventForm {
    private String eventName;
    private List<EventQuestion> questions = new ArrayList<>();

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public List<EventQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<EventQuestion> questions) {
        this.questions = questions;
    }
}
