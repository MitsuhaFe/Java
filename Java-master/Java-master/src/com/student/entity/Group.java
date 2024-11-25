package com.student.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable {
    private String name;        // 小组名称
    private List<Student> students;  // 小组成员
    
    public Group() {
        students = new ArrayList<>();
    }
    
    public Group(String name) {
        this.name = name;
        this.students = new ArrayList<>();
    }
    
    // getter 和 setter 方法
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<Student> getStudents() {
        return students;
    }
    
    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
