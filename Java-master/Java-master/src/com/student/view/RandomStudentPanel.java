package com.student.view;

import com.student.util.Constant;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class RandomStudentPanel extends JPanel {
    private JLabel lbl2 = new JLabel("学生姓名：");
    private JLabel lbl3 = new JLabel("学生照片：");
    private JLabel lblPic = new JLabel("照片");
    private JTextField txtStudent = new JTextField();
    private JButton btnChooseStudent = new JButton("随机学生");
    private JButton btnAbsence = new JButton("缺勤");
    private JButton btnLeave = new JButton("请假");
    private JButton btnAnswer = new JButton("答题");
    private JComboBox<String> classComboBox;
    private JComboBox<String> groupComboBox;
    Thread threadStudent = null;   // 随机抽取学生的线程
    private List<String> studentList = new ArrayList<>();  // 存储当前可选的学生列表
    private Random random = new Random();

    public RandomStudentPanel() {
        this.setBorder(new TitledBorder(new EtchedBorder(), "随机学生点名"));
        this.setLayout(null);

        // 添加班级和小组选择
        JLabel lblClass = new JLabel("选择班级：");
        classComboBox = new JComboBox<>();
        classComboBox.addItem("全部班级");
        loadClasses();

        JLabel lblGroup = new JLabel("选择小组：");
        groupComboBox = new JComboBox<>();
        groupComboBox.addItem("全部小组");
        groupComboBox.setEnabled(false);

        this.add(lblClass);
        this.add(classComboBox);
        this.add(lblGroup);
        this.add(groupComboBox);
        this.add(lbl2);
        this.add(lbl3);
        this.add(txtStudent);
        this.add(lblPic);
        this.add(btnChooseStudent);
        this.add(btnAbsence);
        this.add(btnLeave);
        this.add(btnAnswer);

        // 设置组件位置
        lblClass.setBounds(50, 30, 100, 30);
        classComboBox.setBounds(50, 60, 150, 30);
        lblGroup.setBounds(220, 30, 100, 30);
        groupComboBox.setBounds(220, 60, 150, 30);

        lbl2.setBounds(160, 100, 100, 30);
        txtStudent.setBounds(160, 140, 130, 30);
        txtStudent.setEditable(false);
        lblPic.setBounds(160, 180, 130, 150);
        btnChooseStudent.setBounds(160, 340, 130, 30);
        btnAbsence.setBounds(160, 380, 60, 30);
        btnLeave.setBounds(230, 380, 60, 30);
        btnAnswer.setBounds(300, 380, 60, 30);

        // 班级选择事件
        classComboBox.addActionListener(e -> {
            String selectedClass = (String) classComboBox.getSelectedItem();
            if (selectedClass != null) {
                if ("全部班级".equals(selectedClass)) {
                    groupComboBox.setEnabled(false);
                    groupComboBox.removeAllItems();
                    groupComboBox.addItem("全部小组");
                    loadAllStudents();
                } else {
                    groupComboBox.setEnabled(true);
                    loadGroups(selectedClass);
                    loadStudents(selectedClass, null);
                }
            }
        });

        // 小组选择事件
        groupComboBox.addActionListener(e -> {
            String selectedClass = (String) classComboBox.getSelectedItem();
            String selectedGroup = (String) groupComboBox.getSelectedItem();
            if (selectedClass != null && selectedGroup != null && !"全部小组".equals(selectedGroup)) {
                loadStudents(selectedClass, selectedGroup);
            } else if (selectedClass != null && !"全部班级".equals(selectedClass)) {
                loadStudents(selectedClass, null);
            }
        });

        // 初始��载所有学生
        loadAllStudents();

        // 随机学生按钮事件
        btnChooseStudent.addActionListener(e -> {
            if (studentList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "当前没有可选的学生", "", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (e.getActionCommand().equals("停")) {
                if (threadStudent != null) {
                    threadStudent.interrupt();
                    threadStudent = null;
                }
                btnChooseStudent.setText("随机学生");
            } else {
                btnChooseStudent.setText("停");
                threadStudent = new Thread(() -> {
                    try {
                        while (!Thread.interrupted()) {
                            final String randomStudent = studentList.get(random.nextInt(studentList.size()));
                            SwingUtilities.invokeLater(() -> txtStudent.setText(randomStudent));
                            Thread.sleep(50);
                        }
                    } catch (InterruptedException ex) {
                        // 线程被断，不需要处理
                    }
                });
                threadStudent.start();
            }
        });

        // 缺勤按���事件
        btnAbsence.addActionListener(e -> {
            if (txtStudent.getText() == null || txtStudent.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先随机选择学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String selectedClass = (String) classComboBox.getSelectedItem();
            String studentName = txtStudent.getText().trim();
            
            // 如果是从"全部班级"中选择的学生，需要解析班级名
            if ("全部班级".equals(selectedClass)) {
                String[] parts = studentName.split("-");
                if (parts.length >= 2) {
                    selectedClass = parts[0];
                    studentName = parts[1];
                }
            }

            // 记录缺勤数据
            File attendanceFile = new File("D:" + File.separator + "workspacemax" 
                + File.separator + "java" 
                + File.separator + "student" 
                + File.separator + "attendance.txt");

            try {
                // 确保父目录存在
                File parentDir = attendanceFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }

                // 追加写入文件，格式：班级,学生姓名,类型(absence/leave),日期
                try (PrintWriter writer = new PrintWriter(new FileWriter(attendanceFile, true))) {
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    writer.println(String.join(",", selectedClass, studentName, "absence", date));
                }

                JOptionPane.showMessageDialog(this, "已记录缺勤", "", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "记录缺勤失败：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // 请假按钮事件
        btnLeave.addActionListener(e -> {
            if (txtStudent.getText() == null || txtStudent.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先随机选择学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String selectedClass = (String) classComboBox.getSelectedItem();
            String studentName = txtStudent.getText().trim();
            
            // 如果是从"全部班���"中选择的学生，需要解析班级名
            if ("全部班级".equals(selectedClass)) {
                String[] parts = studentName.split("-");
                if (parts.length >= 2) {
                    selectedClass = parts[0];
                    studentName = parts[1];
                }
            }

            // 记录请假数据
            File attendanceFile = new File("D:" + File.separator + "workspacemax" 
                + File.separator + "java" 
                + File.separator + "student" 
                + File.separator + "attendance.txt");

            try {
                // 确保父目录存在
                File parentDir = attendanceFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }

                // 追加写入文件，格式：班级,学生姓名,类型(absence/leave),日期
                try (PrintWriter writer = new PrintWriter(new FileWriter(attendanceFile, true))) {
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    writer.println(String.join(",", selectedClass, studentName, "leave", date));
                }

                JOptionPane.showMessageDialog(this, "已记录请假", "", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "记录请假失败：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadClasses() {
        File classDir = new File(Constant.FILE_PATH);
        File[] classes = classDir.listFiles(File::isDirectory);
        if (classes != null) {
            for (File classFile : classes) {
                classComboBox.addItem(classFile.getName());
            }
        }
    }

    private void loadGroups(String className) {
        groupComboBox.removeAllItems();
        groupComboBox.addItem("全部小组");
        
        File groupFile = new File("D:" + File.separator + "workspacemax" 
            + File.separator + "java" 
            + File.separator + "student" 
            + File.separator + "group.txt");
            
        if (groupFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(groupFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2 && parts[0].equals(className)) {
                        groupComboBox.addItem(parts[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAllStudents() {
        studentList.clear();
        File studentFile = new File("D:" + File.separator + "workspacemax" 
            + File.separator + "java" 
            + File.separator + "student" 
            + File.separator + "student.txt");
            
        if (studentFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(studentFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        studentList.add(parts[0] + "-" + parts[2]);  // 班级-姓名
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadStudents(String className, String groupName) {
        studentList.clear();
        File studentFile = new File("D:" + File.separator + "workspacemax" 
            + File.separator + "java" 
            + File.separator + "student" 
            + File.separator + "student.txt");
            
        if (studentFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(studentFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        if (parts[0].equals(className) && 
                            (groupName == null || groupName.equals(parts[3]))) {
                            studentList.add(parts[2]);  // 只添加姓名
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
