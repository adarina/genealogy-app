package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.params.*;

public interface EventService {

    void saveEvent(SaveEventParams params);

    void saveEventWithParticipant(SaveEventWithParticipantParams params);

    void deleteEvent(DeleteEventParams params);

    void updateEvent(UpdateEventParams params);

    void updateEventWithParticipant(UpdateEventWithParticipantParams params);

    void addParticipantToEvent(AddParticipantToEventParams params);

    void removeParticipantFromEvent(RemoveParticipantFromEventParams params);

    void removeCitationFromEvent(RemoveCitationFromEventParams params);

    void addCitationToEvent(AddCitationToEventParams params);
}
