/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enrollmentsystem;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hp
 */
public class SubjectManager {

    private HashMap<String, Subject> subjectMap;
    HashMap<String, String> bsuCourse = new HashMap<>();

    public SubjectManager() {
        this.subjectMap = new HashMap<>();
        this.bsuCourse = new HashMap<>();
        initializeSubjects();
    }

    private void initializeSubjects() {
        addSubjects(
                new Subject("IT101", "Introduction to Computing"),
                new Subject("IT102", "Computer Programming 1"),
                new Subject("IT103", "Hardware System and Servicing"),
                new Subject("IT104", "Computer Programming 2"),
                new Subject("IT105", "Networking 1"),
                new Subject("IT106", "Discrete Structures for IT"),
                new Subject("IT201", "Data Structures and Algorithms"),
                new Subject("IT202", "Information Management"),
                new Subject("IT203", "Object-Oriented Programming 1"),
                new Subject("IT204", "Integrative Programming and Technologies 1"),
                new Subject("IT205", "Human Computer Interface"),
                new Subject("IT206", "Advanced Database Systems"),
                new Subject("IT207", "Object-Oriented Programming 2"),
                new Subject("IT208", "Platform Technologies"),
                new Subject("IT209", "Interactive Systems and Technologies"),
                new Subject("IT210", "Networking 2"),
                new Subject("IT301", "Application Development and Technologies"),
                new Subject("IT302", "Social and Professional Issues"),
                new Subject("IT303", "System Analysis and Design"),
                new Subject("IT304", "Web Systems and Technologies 1"),
                new Subject("IT305", "Quantitative Methods"),
                new Subject("IT306", "Elective 1"),
                new Subject("IT307", "Elective 2"),
                new Subject("IT308", "Information Assurance and Security"),
                new Subject("IT309", "Systems Integration and Architecture 1"),
                new Subject("IT310", "Web Systems and Technologies 2"),
                new Subject("IT311", "Elective 3"),
                new Subject("IT312", "Elective 4"),
                new Subject("IT401", "System Administration and Maintenance"),
                new Subject("IT402", "System Integration and Architecture 2"),
                new Subject("IT403", "Elective 5"),
                new Subject("IT404", "Internship")
        );
    }

    public void addSubjects(Subject... subjects) {
        for (Subject subject : subjects) {
            subjectMap.put(subject.getSubjectCode(), subject);
        }
    }
    
    public void addCourse(String course, String courseName) {
        bsuCourse.put(course, courseName);
    }

//    public void addSubject(String subjectCode, String subjectName) {
//        Subject subject = new Subject(subjectCode, subjectName);
//        subjectMap.put(subjectCode, subject);
//    }
    public Subject getSubjectCode(String subjectCode) {
        return subjectMap.get(subjectCode);
    }
    
    public String getCourse(String course) {
        return bsuCourse.get(course);
    }

    public static class Subject {

        private final String subjectCode, subjectName;

        public Subject(String subjectCode, String subjectName) {
            this.subjectCode = subjectCode;
            this.subjectName = subjectName;
        }

        public String getSubjectCode() {
            return subjectCode;
        }

        public String getSubjectName() {
            return subjectName;
        }

        @Override
        public String toString() {
            return "Subject Code: " + subjectCode + ", Subject Name: " + subjectName;
        }
    }

}
