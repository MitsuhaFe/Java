package com.student.util;

import com.student.entity.Group;
import com.student.entity.Student;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Constant {
    // 小组、学生等文件路径
    public static final String FILE_PATH = "D:" + File.separator + "workspacemax" 
        + File.separator + "java" 
        + File.separator + "student"
        + File.separator + "class";
    // 班级路径
    public static String CLASS_PATH = "";
    // 存放当前班级的小组和学生
    public static LinkedHashMap<Group, List<Student>> groups = new LinkedHashMap<>();
    // 存放当前班级的所有学生
    public static List<Student> students = new ArrayList<>();
    // 缺勤扣5分
    public static final int ABSENTEEISM_SCORE = 5;
    // 请假扣2分
    public static final int LEAVE_SCORE = 2;
    // 回答问题正确加3分
    public static final int ANSWER_QUESTION = 3;

}
