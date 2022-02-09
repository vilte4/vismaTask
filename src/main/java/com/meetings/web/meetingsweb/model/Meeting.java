package com.meetings.web.meetingsweb.model;

import java.util.*;

public class Meeting {

    private int id;
    private String name;
    private Person responsiblePerson;
    private String description;
    private Category category;
    private Type type;

    private enum Category {
        CodeMonkey,
        Hub,
        Short,
        TeamBuilding
    }

    private enum Type {
        Live,
        InPerson
    }

    private Date startDate;
    private Date endDate;
    List<Person> atendees = new ArrayList<Person>();

    public void addPersonToMeet(Person Person) {
        this.atendees.add(Person);
    }

    public List<Person> getAtendees() {
        return this.atendees;
    }

    public void setAtendees(List<Person> atendees) {
        this.atendees = atendees;
    }

    public Meeting(String name, Person responsiblePerson, Category category, Type type, String description,
            Date startDate, Date endDate) {
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.description = description;
        this.setCategory(category);
        this.setType(type);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", responsiblePerson='" + responsiblePerson + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(id, meeting.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(Person responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
