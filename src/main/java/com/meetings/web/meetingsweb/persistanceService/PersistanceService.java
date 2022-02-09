package com.meetings.web.meetingsweb.persistanceService;

import java.util.List;

import com.meetings.web.meetingsweb.model.Meeting;
import com.meetings.web.meetingsweb.model.Person;

public interface PersistanceService {
    public void createMeeting(Meeting meeting);

    public Meeting deleteMeeting(Integer id, Integer personId);

    public Meeting addPersonToMeeting(Integer id, Person person);

    public List<Meeting> listAllMeetings();

    public Person deletePersonFromMeeting(Integer id, Integer personId);
}
