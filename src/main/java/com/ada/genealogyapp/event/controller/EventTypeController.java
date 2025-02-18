package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.event.type.EventType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/genealogy/types/event")
public class EventTypeController {

    @GetMapping
    public List<EventType> getEventTypes() {
        return Arrays.asList(EventType.values());
    }
}
