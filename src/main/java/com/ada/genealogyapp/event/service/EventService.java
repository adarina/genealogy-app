package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.params.*;

import java.util.List;
import java.util.Map;

public interface EventService {

    void saveEvent(SaveEventParams params);

    void deleteEvent(DeleteEventParams params);

    void updateEvent(UpdateEventParams params);

    void updateEventWithParticipant(UpdateEventWithParticipantParams params);

    void addParticipantToEvent(AddParticipantToEventParams params);

    void removeParticipantFromEvent(RemoveParticipantFromEventParams params);

    void removeCitationFromEvent(RemoveCitationFromEventParams params);

    void removeLocationFromEvent(RemoveLocationFromEventParams params);

    void addCitationToEvent(AddCitationToEventParams params);

    void addLocationToEvent(AddLocationToEventParams params);

    void addParticipantAndLocationToEvent(AddParticipantAndLocationToEventParams params);

    void saveEvents(String userId, String treeId, List<Map<String, Object>> eventsData);

    void addParticipantsToEvents(String userId, String id, List<Map<String, Object>> participantsToAdd);

    void addCitationsToEvents(String userId, String id, List<Map<String, String>> citationsToAdd);

    void addLocationsToEvents(String userId, String id, List<Map<String, String>> locationsToAdd);
}
