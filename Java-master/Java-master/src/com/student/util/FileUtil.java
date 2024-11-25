package com.student.util;

import com.student.entity.Group;
import com.student.entity.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FileUtil {
    
    // 保存班级数据
    public static void saveClassData() {
        File dir = new File(Constant.CLASS_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 保存小组和学生数据
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(Constant.CLASS_PATH + "class_data.dat"))) {
            oos.writeObject(Constant.groups);
            oos.writeObject(Constant.students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // 加载班级数据
    @SuppressWarnings("unchecked")
    public static void loadClassData(String className) {
        Constant.CLASS_PATH = Constant.FILE_PATH + className + "/";
        File dataFile = new File(Constant.CLASS_PATH + "class_data.dat");
        
        if (dataFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(dataFile))) {
                Constant.groups = (LinkedHashMap<Group, List<Student>>) ois.readObject();
                Constant.students = (List<Student>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                // 如果读取失败，初始化空数据
                Constant.groups = new LinkedHashMap<>();
                Constant.students = new ArrayList<>();
            }
        } else {
            // 如果文件不存在，初始化空数据
            Constant.groups = new LinkedHashMap<>();
            Constant.students = new ArrayList<>();
        }
    }
    
    // 获取所有班级名称
    public static List<String> getAllClasses() {
        List<String> classes = new ArrayList<>();
        File dir = new File(Constant.FILE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
            return classes;
        }
        
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.add(file.getName());
                }
            }
        }
        return classes;
    }
} 