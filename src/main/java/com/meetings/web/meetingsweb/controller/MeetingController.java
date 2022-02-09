package com.meetings.web.meetingsweb.controller;

import com.meetings.web.meetingsweb.persistanceService.PersistanceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.meetings.web.meetingsweb.model.Meeting;
import com.meetings.web.meetingsweb.model.Person;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.List;

@Controller
public class MeetingController {
    @Autowired
    PersistanceService persistanceService;

    @PostMapping(path = "/meeting", produces = { "application/json" })
    public ResponseEntity<Void> addMeeting(@RequestBody Meeting meeting) {
        if (meeting == null) {
            return ResponseEntity.noContent().build();
        }

        persistanceService.createMeeting(meeting);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(meeting.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/meeting/{id}/{personId}", produces = { "application/json" })
    public ResponseEntity<Meeting> deleteMeeting(@PathVariable int id, @PathVariable int personId) {
        return ResponseEntity.ok(persistanceService.deleteMeeting(id, personId));
    }

    @DeleteMapping(path = "meeting/{id}/personId/{personId}", produces = { "application/json" })
    public ResponseEntity<Person> removePersonFromMeeting(@PathVariable int id, @PathVariable int personId) {
        return ResponseEntity.ok(persistanceService.deletePersonFromMeeting(id, personId));
    }

    @PostMapping(path = "/meeting/{id}/person", produces = { "application/json" })
    public ResponseEntity<Meeting> addPersonToMeeting(@RequestBody Person person, @PathVariable int id) {
        return ResponseEntity.ok(persistanceService.addPersonToMeeting(id, person));
    }

    @GetMapping(path = "/meetings", produces = { "application/json" })
    public ResponseEntity<List<Meeting>> listAllMeetings() {
        return new ResponseEntity(persistanceService.listAllMeetings(), HttpStatus.OK);
    }
}
