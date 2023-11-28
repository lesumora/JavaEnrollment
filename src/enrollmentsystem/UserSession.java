/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enrollmentsystem;

import java.util.HashMap;

/**
 *
 * @author hp
 */
public class UserSession {

    private static UserSession instance;
    private String username, lastName, firstName, course, scholar, campus, curriculum, section;
    private int userId, yearLevel;
    boolean isEnrolled = false;
    private HashMap<String, String> enrolledSubjects;

    private UserSession() {
        // private constructor to enforce singleton pattern
        enrolledSubjects = new HashMap<>();
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(String username, int userId, String lastName, String firstName, String course,
            String scholar, int yearLevel) {
        this.username = username;
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.course = course;
        this.scholar = scholar;
        this.yearLevel = yearLevel;
    }

    public void setUserCampus(String campus) {
        this.campus = campus;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setIsEnrolled(boolean isEnrolled) {
        this.isEnrolled = isEnrolled;
    }

    public String getUserCampus() {
        return campus;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public boolean getIsEnrolled() {
        return isEnrolled;
    }

    public String getSection() {
        return section;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getCourse() {
        return course;
    }

    public String getScholar() {
        return scholar;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    public void clearSession() {
        username = null;
        userId = 0;
        lastName = null;
        firstName = null;
        course = null;
        scholar = null;
        yearLevel = 0;
        campus = null;
        curriculum = null;
        section = null;
        isEnrolled = false;
    }

    public void enrollSubject(String subjectCode) {
        enrolledSubjects.put(subjectCode, "YES");
    }

    public void unenrollSubject(String subjectCode) {
        enrolledSubjects.remove(subjectCode);
    }

    public HashMap<String, String> getEnrolledSubjects() {
        return enrolledSubjects;
    }
}
