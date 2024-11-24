package com.student.view;

import com.student.entity.Group;
import com.student.entity.Student;
import com.student.util.Constant;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class StudentAddPanel extends JPanel {
    private JComboBox<String> classComboBox;
    private JComboBox<String> groupComboBox;
    
    public StudentAddPanel() {
        this.setLayout(null);
        this.setBorder(new TitledBorder(new EtchedBorder(), "新增学生"));
        
        // 添加班级选择
        JLabel lblClass = new JLabel("选择班级：");
        classComboBox = new JComboBox<>();
        loadClasses();
        
        JLabel lblId = new JLabel("学号：");
        JTextField txtId = new JTextField();
        JLabel lblName = new JLabel("姓名：");
        JTextField txtName = new JTextField();
        JLabel lblGroup = new JLabel("小组:");
        groupComboBox = new JComboBox<>();
        groupComboBox.addItem("请选择小组");  // 添加默认选项
        JButton btnAdd = new JButton("确认");

        // 添加组件
        this.add(lblClass);
        this.add(classComboBox);
        this.add(lblId);
        this.add(txtId);
        this.add(lblName);
        this.add(txtName);
        this.add(lblGroup);
        this.add(groupComboBox);
        this.add(btnAdd);

        // 设置位置
        lblClass.setBounds(200, 30, 100, 30);
        classComboBox.setBounds(200, 60, 200, 30);
        lblId.setBounds(200, 100, 100, 30);
        txtId.setBounds(200, 130, 200, 30);
        lblName.setBounds(200, 170, 100, 30);
        txtName.setBounds(200, 200, 200, 30);
        lblGroup.setBounds(200, 240, 100, 30);
        groupComboBox.setBounds(200, 270, 200, 30);
        btnAdd.setBounds(200, 320, 100, 30);

        // 班级选择事件
        classComboBox.addActionListener(e -> {
            String selectedClass = (String) classComboBox.getSelectedItem();
            if (selectedClass != null) {
                loadGroups(selectedClass);
            }
        });

        // 如果已经选择了班级，设置为当前选中项并加载小组
        if (Constant.CLASS_PATH != null && !Constant.CLASS_PATH.isEmpty()) {
            classComboBox.setSelectedItem(Constant.CLASS_PATH);
            loadGroups(Constant.CLASS_PATH);
        }

        btnAdd.addActionListener(e -> {
            String selectedClass = (String) classComboBox.getSelectedItem();
            if (selectedClass == null || selectedClass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择班级", "", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String selectedGroup = (String) groupComboBox.getSelectedItem();
            if (selectedGroup == null || selectedGroup.equals("请选择小组")) {  // 修改判断条件
                JOptionPane.showMessageDialog(this, "请选择小组", "", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 验证学号是否为整数
            String studentId = txtId.getText().trim();
            try {
                Integer.parseInt(studentId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "学号必须是整数", "", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 验证姓名长度
            String studentName = txtName.getText().trim();
            if (studentName == null || studentName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写学生姓名", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (studentName.length() > 10) {
                JOptionPane.showMessageDialog(this, "姓名长度不能超过10个字符", "", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 检查学号是否已存在
            File studentFile = new File("D:" + File.separator + "workspacemax" 
                + File.separator + "java" 
                + File.separator + "student" 
                + File.separator + "student.txt");

            if (studentFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(studentFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 4 && parts[1].equals(studentId)) {
                            JOptionPane.showMessageDialog(this, "该学号已存在", "", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            try {
                // 确保父目录存在
                File parentDir = studentFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }

                // 追加写入文件，格式：班级,学号,姓名,小组
                try (PrintWriter writer = new PrintWriter(new FileWriter(studentFile, true))) {
                    writer.println(String.join(",", selectedClass, studentId, studentName, selectedGroup));
                }

                // 更新内存中的数据
                Student newStudent = new Student(studentId, studentName);
                Group group = new Group(selectedGroup);
                newStudent.setGroup(group);
                
                if (!Constant.groups.containsKey(group)) {
                    Constant.groups.put(group, new ArrayList<>());
                }
                Constant.groups.get(group).add(newStudent);
                Constant.students.add(newStudent);

                JOptionPane.showMessageDialog(this, "新增学生成功", "", JOptionPane.INFORMATION_MESSAGE);
                txtId.setText("");
                txtName.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "保存学生数据失败：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadClasses() {
        File classDir = new File(Constant.FILE_PATH);
        File[] classes = classDir.listFiles(File::isDirectory);
        if (classes != null) {
            classComboBox.removeAllItems();
            for (File classFile : classes) {
                classComboBox.addItem(classFile.getName());
            }
        }
    }

    private void loadGroups(String className) {
        groupComboBox.removeAllItems();
        groupComboBox.addItem("请选择小组");  // 重新添加默认选项
        
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
                JOptionPane.showMessageDialog(this, 
                    "加载小组数据失败：" + e.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // 如果没有小组，显示提示
        if (groupComboBox.getItemCount() <= 1) {  // 只有"请选择小组"选项
            JOptionPane.showMessageDialog(this, 
                "当前班级还没有小组，请先创建小组", 
                "提示", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
