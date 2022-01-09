package com.example.foodies.model;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    private Model(){
        for(int i=0;i<100;i++){
            Student s = new Student("name",""+i,false);
            data.add(s);
        }
    }

    List<Student> data = new LinkedList<Student>();

    public List<Student> getAllStudents(){
        return data;
    }

    public void addStudent(Student student){
        data.add(student);
    }

    public Student getStudentById(String studentId) {
        for (Student s:data
        ) {
            if (s.getId().equals(studentId)){
                return s;
            }
        }
        return null;
    }
}
