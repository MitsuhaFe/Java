package com.student.entity;

import java.io.Serializable;

public class Student implements Serializable {
    private String id;          // 学号
    private String name;        // 姓名
    private int score;          // 分数
    private Group group;        // 所属小组
    
    public Student() {}
    
    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.score = 0;
    }
    
    // getter 和 setter 方法
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public Group getGroup() {
        return group;
    }
    
    public void setGroup(Group group) {
        this.group = group;
    }
}
