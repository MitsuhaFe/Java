package com.student.view;

import com.student.util.Constant;
import com.student.entity.Student;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class StudentListPanel extends JPanel {
    String[] headers = {"\u5E8F\u53F7", "\u59D3\u540D", "\u73ED\u7EA7", "\u5C0F\u7EC4", "\u7F3A\u52E4\u6B21\u6570", "\u8BF7\u5047\u6B21\u6570"};
    DefaultTableModel tableModel;
    JTable studentTable;
    JTextField txtId = new JTextField();
    JTextField txtName = new JTextField();
    JComboBox<String> cmbGroup = new JComboBox<>();
    JButton btnEdit = new JButton("修改");
    JButton btnDelete = new JButton("删除");
    JComboBox<String> classComboBox;
    JComboBox<String> groupComboBox;
    JRadioButton rbAll, rbClass, rbGroup;

    public StudentListPanel() {
        this.setBorder(new TitledBorder(new EtchedBorder(), "学生列表"));
        this.setLayout(new BorderLayout());

        // 创建顶部过滤面板
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // 添加查看选项
        ButtonGroup viewGroup = new ButtonGroup();
        rbAll = new JRadioButton("查看所有学生", true);
        rbClass = new JRadioButton("按班级查看");
        rbGroup = new JRadioButton("按小组查看");
        viewGroup.add(rbAll);
        viewGroup.add(rbClass);
        viewGroup.add(rbGroup);
        
        // 添加班级和小组选择
        JLabel lblClass = new JLabel("选择班级：");
        classComboBox = new JComboBox<>();
        JLabel lblGroup = new JLabel("选择小组：");
        groupComboBox = new JComboBox<>();
        
        // 初始状态下禁用选择框
        classComboBox.setEnabled(false);
        groupComboBox.setEnabled(false);
        
        // 添加到过滤面板
        filterPanel.add(rbAll);
        filterPanel.add(rbClass);
        filterPanel.add(rbGroup);
        filterPanel.add(lblClass);
        filterPanel.add(classComboBox);
        filterPanel.add(lblGroup);
        filterPanel.add(groupComboBox);
        
        // 加载班级列表
        loadClasses();

        // 创建表格
        tableModel = new DefaultTableModel(null, headers);
        studentTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // 创建底部编辑面板
        JPanel btnPanel = new JPanel();
        btnPanel.add(new JLabel("学号:"));
        btnPanel.add(txtId);
        txtId.setPreferredSize(new Dimension(100, 30));
        btnPanel.add(new JLabel("姓名:"));
        btnPanel.add(txtName);
        txtName.setPreferredSize(new Dimension(100, 30));
        btnPanel.add(new JLabel("小组:"));
        btnPanel.add(cmbGroup);
        cmbGroup.setPreferredSize(new Dimension(100, 30));
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);

        // 添加到主面板
        this.add(filterPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(btnPanel, BorderLayout.SOUTH);

        // 添加事件监听
        rbAll.addActionListener(e -> {
            classComboBox.setEnabled(false);
            groupComboBox.setEnabled(false);
            loadStudentData(null, null);
        });

        rbClass.addActionListener(e -> {
            classComboBox.setEnabled(true);
            groupComboBox.setEnabled(false);
            if (classComboBox.getSelectedItem() != null) {
                loadStudentData((String)classComboBox.getSelectedItem(), null);
            }
        });

        rbGroup.addActionListener(e -> {
            classComboBox.setEnabled(true);
            groupComboBox.setEnabled(true);
            if (classComboBox.getSelectedItem() != null && groupComboBox.getSelectedItem() != null) {
                loadStudentData((String)classComboBox.getSelectedItem(), (String)groupComboBox.getSelectedItem());
            }
        });

        classComboBox.addActionListener(e -> {
            String selectedClass = (String) classComboBox.getSelectedItem();
            if (selectedClass != null) {
                loadGroups(selectedClass);
                if (rbClass.isSelected()) {
                    loadStudentData(selectedClass, null);
                } else if (rbGroup.isSelected() && groupComboBox.getSelectedItem() != null) {
                    loadStudentData(selectedClass, (String)groupComboBox.getSelectedItem());
                }
            }
        });

        groupComboBox.addActionListener(e -> {
            if (rbGroup.isSelected() && classComboBox.getSelectedItem() != null && groupComboBox.getSelectedItem() != null) {
                loadStudentData((String)classComboBox.getSelectedItem(), (String)groupComboBox.getSelectedItem());
            }
        });

        // 初始加载所有学生数据
        loadStudentData(null, null);

        // 添加删除按钮事件
        btnDelete.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择学生", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "删除学生会删除他的考勤、成绩等，确认删除？", "", JOptionPane.YES_NO_OPTION) != 0) {
                return;
            }

            String studentId = (String) studentTable.getValueAt(selectedRow, 0);
            String studentName = (String) studentTable.getValueAt(selectedRow, 1);
            String className = (String) studentTable.getValueAt(selectedRow, 2);
            
            // 删除学生数据
            File studentFile = new File("D:" + File.separator + "workspacemax" 
                + File.separator + "java" 
                + File.separator + "student" 
                + File.separator + "student.txt");
                
            try {
                List<String> remainingLines = new ArrayList<>();
                if (studentFile.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(studentFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",");
                            // 不保存要删除的学生数据
                            if (parts.length < 4 || !parts[0].equals(className) 
                                || !parts[1].equals(studentId) || !parts[2].equals(studentName)) {
                                remainingLines.add(line);
                            }
                        }
                    }
                }

                // 重写学生文件
                try (PrintWriter writer = new PrintWriter(new FileWriter(studentFile))) {
                    for (String line : remainingLines) {
                        writer.println(line);
                    }
                }

                // 删除考勤记录
                File attendanceFile = new File("D:" + File.separator + "workspacemax" 
                    + File.separator + "java" 
                    + File.separator + "student" 
                    + File.separator + "attendance.txt");
                    
                if (attendanceFile.exists()) {
                    remainingLines.clear();
                    try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",");
                            // 不保存要删除的学生的考勤记录
                            if (parts.length < 4 || !parts[0].equals(className) 
                                || !parts[1].equals(studentName)) {
                                remainingLines.add(line);
                            }
                        }
                    }

                    // 重写考勤文件
                    try (PrintWriter writer = new PrintWriter(new FileWriter(attendanceFile))) {
                        for (String line : remainingLines) {
                            writer.println(line);
                        }
                    }
                }

                JOptionPane.showMessageDialog(this, "删除学生成功", "", JOptionPane.INFORMATION_MESSAGE);
                
                // 从内存中移除学生
                Constant.students.removeIf(student -> 
                    student.getId().equals(studentId) && student.getName().equals(studentName));
                
                // 从小组中移除学生
                for (List<Student> groupStudents : Constant.groups.values()) {
                    groupStudents.removeIf(student -> 
                        student.getId().equals(studentId) && student.getName().equals(studentName));
                }

                // 刷新学生列表
                loadStudentData(className, null);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "删除学生失败：" + ex.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadClasses() {
        classComboBox.removeAllItems();
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

    private void loadStudentData(String className, String groupName) {
        // 清空表格数据
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        // 先加载考勤数据
        Map<String, int[]> attendanceData = new HashMap<>();  // key: 班级,学生名 value: [缺勤次数,请假次数]
        File attendanceFile = new File("D:" + File.separator + "workspacemax" 
            + File.separator + "java" 
            + File.separator + "student" 
            + File.separator + "attendance.txt");
            
        // 确保目录存在
        if (!attendanceFile.getParentFile().exists()) {
            attendanceFile.getParentFile().mkdirs();
        }
            
        if (attendanceFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {  // 班级,学生姓名,类型(absence/leave),日期
                        String key = parts[0] + "," + parts[1];
                        int[] counts = attendanceData.computeIfAbsent(key, k -> new int[2]);
                        if ("absence".equals(parts[2])) {
                            counts[0]++;  // 缺勤次数+1
                        } else if ("leave".equals(parts[2])) {
                            counts[1]++;  // 请假次数+1
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 加载学生数据
        File studentFile = new File("D:" + File.separator + "workspacemax" 
            + File.separator + "java" 
            + File.separator + "student" 
            + File.separator + "student.txt");
            
        // 确保目录存在
        if (!studentFile.getParentFile().exists()) {
            studentFile.getParentFile().mkdirs();
        }
            
        if (studentFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(studentFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        // 根据筛选条件决定是否显示该学生
                        if (className == null || // 显示所有学生
                            (className.equals(parts[0]) && groupName == null) || // 只按班级筛选
                            (className.equals(parts[0]) && groupName.equals(parts[3]))) { // 按班级和小组筛选
                            
                            // 获取考勤数据
                            int[] counts = attendanceData.getOrDefault(parts[0] + "," + parts[2], new int[2]);
                            
                            tableModel.addRow(new String[]{
                                parts[1], // 学号
                                parts[2], // 姓名
                                parts[0], // 班级
                                parts[3], // 小组
                                String.valueOf(counts[0]), // 缺勤次数
                                String.valueOf(counts[1])  // 请假次数
                            });
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "读取学生数据失败：" + e.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        // 如果没有数据，显示提示
        if (tableModel.getRowCount() == 0) {
            System.out.println("当前没有学生数据");  // 添加调试信息
        }
    }
}
