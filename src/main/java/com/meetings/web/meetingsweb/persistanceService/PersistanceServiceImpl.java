package com.meetings.web.meetingsweb.persistanceService;

import com.meetings.web.meetingsweb.model.Meeting;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.meetings.web.meetingsweb.model.Person;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class PersistanceServiceImpl implements PersistanceService {

    @Value("${persistance.localStorageFile}")
    private String localStorageFile;

    private Map<Integer, Meeting> storedMeetings = new HashMap<>();

    @PostConstruct
    public void init() {
        loadInitialData();
    }

    @Override
    public void createMeeting(Meeting meeting) {
        // Write to storedMeetings hashMap
        this.storedMeetings.put(meeting.getId(), meeting);

        // Write to localStorage.json file
        this.storeMapToFile();
    }

    @Override
    public Meeting deleteMeeting(Integer id, Integer personId) {
        System.out.println("Delete request for meeting id: " + id + " with person id: " + personId);
        if (storedMeetings.containsKey(id)) {
            Meeting deletedMeeting = storedMeetings.get(id);
            if (deletedMeeting.getResponsiblePerson().getId() == personId) {
                System.out.println("Deleting meeting id: " + id + " requested by: " + personId);
                storedMeetings.remove(id);
                this.storeMapToFile();

                return deletedMeeting;
            }
        }
        System.out.println("Delete requirements were not satisfied.");
        return null;
    }

    @Override
    public Meeting addPersonToMeeting(Integer id, Person person) {
        if (storedMeetings.containsKey(id)) {
            Meeting meetingToAddPersonTo = storedMeetings.get(id);
            for (Person atendee : meetingToAddPersonTo.getAtendees()) {
                if (atendee.getId() == person.getId()) {
                    System.out.println("Atendee with id:" + person.getId() + " already register for meeting: " + id);
                    return meetingToAddPersonTo;
                }
            }
            meetingToAddPersonTo.addPersonToMeet(person);
            this.storeMapToFile();
            return meetingToAddPersonTo;
        }
        return null;
    }

    @Override
    public List<Meeting> listAllMeetings() {
        List<Meeting> allMeetings = new ArrayList<>();
        allMeetings = (ArrayList<Meeting>) storedMeetings.values().stream().collect(Collectors.toList());
        return allMeetings;
    }

    @Override
    public Person deletePersonFromMeeting(Integer id, Integer personId) {
        System.out.println("Delete request for person id: " + personId + " in meeting id: " + id + " received");

        if (storedMeetings.containsKey(id)) {
            Meeting meetingFromWhichToRemovePerson = storedMeetings.get(id);
            if (meetingFromWhichToRemovePerson.getResponsiblePerson().getId() == personId) {
                System.out.println(
                        "Can not delete responsible person from his meeting. Responsible person id: " + personId);
                return null;
            }

            for (Person person : meetingFromWhichToRemovePerson.getAtendees()) {
                if (person.getId() == personId) {
                    meetingFromWhichToRemovePerson.getAtendees().remove(person);
                    System.out.println("Deleting person id: " + personId + " from meeting id: " + id);
                    this.storeMapToFile();
                    return person;
                }
            }
        }

        return null;
    }

    private void storeMapToFile() {
        List<Meeting> allMeetings = new ArrayList<>();
        allMeetings = (ArrayList<Meeting>) storedMeetings.values().stream().collect(Collectors.toList());

        Gson gson = new Gson();
        String json = gson.toJson(allMeetings);

        try {
            Files.writeString(Paths.get(this.localStorageFile, "localStorage.json"), json);
        } catch (Exception e) {
            System.err.println(
                    "Failed to WRITE meetings from a given file: " + this.localStorageFile + "/localStorage.json");
            System.err.println(e.toString());
        }
    }

    public void loadInitialData() {
        String persistedMeetings = new String();
        try {
            persistedMeetings = Files.readString(
                    Paths.get(this.localStorageFile, "localStorage.json"));
        } catch (Exception e) {
            System.err.println(
                    "Failed to READ meetings from a given file: " + this.localStorageFile + "/localStorage.json");
            System.err.println(e.toString());
            return;
        }

        Gson gson = new Gson();
        List<Meeting> parsedMeetings;
        try {
            Type meetingListType = new TypeToken<List<Meeting>>() {
            }.getType();
            parsedMeetings = gson.fromJson(persistedMeetings, meetingListType);
            if (parsedMeetings == null) {
                parsedMeetings = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Failed to parse Meetings from a JSON String");
            System.err.println(e.toString());
            return;
        }

        for (Meeting meeting : parsedMeetings) {
            System.out.println("add meeting id: " + meeting.getId() + " to the map");
            this.storedMeetings.put(meeting.getId(), meeting);
        }

        System.out.println("Meetings loaded from a file: " + parsedMeetings.size());
    }

}
