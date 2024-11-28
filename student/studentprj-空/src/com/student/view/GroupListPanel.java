package com.student.view;

import com.student.entity.Group;
import com.student.util.Constant;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class GroupListPanel extends JPanel {
    String[] headers = {"序号", "小组名称", "分数"};
    JTable groupTable;
    JTextField txtName = new JTextField();
    JTextField txtScore = new JTextField();
    JButton btnEdit = new JButton("修改");
    JButton btnDelete = new JButton("删除");
    DefaultTableModel tableModel;
    JComboBox<String> classComboBox;

    public GroupListPanel() {
        this.setBorder(new TitledBorder(new EtchedBorder(), "小组列表"));
        this.setLayout(new BorderLayout());
        
        // 添加班级选择下拉框
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel classLabel = new JLabel("选择班级：");
        classComboBox = new JComboBox<>();
        loadClasses();  // 加载班级列表
        topPanel.add(classLabel);
        topPanel.add(classComboBox);
        this.add(topPanel, BorderLayout.NORTH);
        
        // 创建表格模型
        tableModel = new DefaultTableModel(null, headers);
        groupTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        groupTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(groupTable);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(txtName);
        txtName.setPreferredSize(new Dimension(200, 30));
        btnPanel.add(txtScore);
        txtScore.setPreferredSize(new Dimension(100, 30));
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        this.add(btnPanel, BorderLayout.SOUTH);

        // 添加班级选择事件监听器
        classComboBox.addActionListener(e -> {
            String selectedClass = (String) classComboBox.getSelectedItem();
            if (selectedClass != null) {
                loadGroupData(selectedClass);
            }
        });

        // 如果已经选择了班级，加载该班级的小组数据
        if (Constant.CLASS_PATH != null && !Constant.CLASS_PATH.isEmpty()) {
            classComboBox.setSelectedItem(Constant.CLASS_PATH);
        }

        // 添加表格选择监听器
        groupTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = groupTable.getSelectedRow();
            if (selectedRow >= 0) {
                txtName.setText((String) groupTable.getValueAt(selectedRow, 1));
                txtScore.setText((String) groupTable.getValueAt(selectedRow, 2));
            }
        });

        // 添加删除按钮事件
        btnDelete.addActionListener(e -> {
            int selectedRow = groupTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择小组", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            if (JOptionPane.showConfirmDialog(this, "确定要删除这个小组吗？", 
                "确认删除", JOptionPane.YES_NO_OPTION) != 0) {
                return;
            }
            
            String selectedClass = (String) classComboBox.getSelectedItem();
            String groupName = (String) groupTable.getValueAt(selectedRow, 1);
            
            File groupFile = new File("D:" + File.separator + "workspacemax" 
                + File.separator + "java" 
                + File.separator + "student" 
                + File.separator + "group.txt");
                
            if (groupFile.exists()) {
                try {
                    List<String> remainingLines = new ArrayList<>();
                    try (BufferedReader reader = new BufferedReader(new FileReader(groupFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",");
                            if (parts.length < 2 || !parts[0].equals(selectedClass) 
                                || !parts[1].equals(groupName)) {
                                remainingLines.add(line);
                            }
                        }
                    }
                    
                    // 重写group.txt文件
                    try (PrintWriter writer = new PrintWriter(new FileWriter(groupFile))) {
                        for (String line : remainingLines) {
                            writer.println(line);
                        }
                    }
                    
                    // 从内存中移除小组
                    for (Group group : new ArrayList<>(Constant.groups.keySet())) {
                        if (group.getName().equals(groupName)) {
                            Constant.groups.remove(group);
                            break;
                        }
                    }
                    
                    JOptionPane.showMessageDialog(this, "删除小组成功", "", JOptionPane.INFORMATION_MESSAGE);
                    // 刷新小组列表
                    loadGroupData(selectedClass);
                    
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "删除小组失败：" + ex.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 修改编辑按钮事件
        btnEdit.addActionListener(e -> {
            int selectedRow = groupTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择小组", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String selectedClass = (String) classComboBox.getSelectedItem();
            String oldGroupName = (String) groupTable.getValueAt(selectedRow, 1);
            String newGroupName = txtName.getText().trim();
            String score = txtScore.getText().trim();

            // 验证新小组名
            if (newGroupName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "小组名称不能为空", "", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 验证分数
            try {
                if (!score.isEmpty()) {  // 允许分数为空，表示不修改分数
                    int scoreValue = Integer.parseInt(score);
                    if (scoreValue < 0 || scoreValue > 100) {
                        JOptionPane.showMessageDialog(this, "分数必须在0-100之间", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "分数必须是整数", "", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 如果小组名有修改，检查新名称是否已存在
            if (!oldGroupName.equals(newGroupName)) {
                File groupFile = new File("D:" + File.separator + "workspacemax" 
                    + File.separator + "java" 
                    + File.separator + "student" 
                    + File.separator + "group.txt");
                    
                if (groupFile.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(groupFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",");
                            if (parts.length >= 2 && parts[0].equals(selectedClass) 
                                && parts[1].equals(newGroupName)) {
                                JOptionPane.showMessageDialog(this, "小组名称已存在", "", 
                                    JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            // 更新小组名称
            if (!oldGroupName.equals(newGroupName)) {
                File groupFile = new File("D:" + File.separator + "workspacemax" 
                    + File.separator + "java" 
                    + File.separator + "student" 
                    + File.separator + "group.txt");
                    
                try {
                    List<String> allGroups = new ArrayList<>();
                    if (groupFile.exists()) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(groupFile))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] parts = line.split(",");
                                if (parts.length >= 2 && parts[0].equals(selectedClass) 
                                    && parts[1].equals(oldGroupName)) {
                                    allGroups.add(selectedClass + "," + newGroupName);
                                } else {
                                    allGroups.add(line);
                                }
                            }
                        }
                    }

                    try (PrintWriter writer = new PrintWriter(new FileWriter(groupFile))) {
                        for (String line : allGroups) {
                            writer.println(line);
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "更新小组名称失败：" + ex.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // 更新小组分数
            if (!score.isEmpty()) {
                File groupScoreFile = new File("D:" + File.separator + "workspacemax" 
                    + File.separator + "java" 
                    + File.separator + "student" 
                    + File.separator + "group_score.txt");

                try {
                    List<String> allScores = new ArrayList<>();
                    boolean scoreUpdated = false;
                    
                    if (groupScoreFile.exists()) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(groupScoreFile))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] parts = line.split(",");
                                if (parts.length >= 3 && parts[0].equals(selectedClass) 
                                    && parts[1].equals(oldGroupName)) {
                                    allScores.add(selectedClass + "," + newGroupName + "," + score);
                                    scoreUpdated = true;
                                } else {
                                    allScores.add(line);
                                }
                            }
                        }
                    }

                    if (!scoreUpdated) {
                        allScores.add(selectedClass + "," + newGroupName + "," + score);
                    }

                    try (PrintWriter writer = new PrintWriter(new FileWriter(groupScoreFile))) {
                        for (String line : allScores) {
                            writer.println(line);
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "更新小组分数失败：" + ex.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "修改成功", "", JOptionPane.INFORMATION_MESSAGE);
            // 刷新小组列表
            loadGroupData(selectedClass);
            txtName.setText("");
            txtScore.setText("");
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

    private void loadGroupData(String className) {
        // 清空现有数据
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        // 先加载小组分数
        Map<String, String> groupScores = new HashMap<>();
        File groupScoreFile = new File("D:" + File.separator + "workspacemax" 
            + File.separator + "java" 
            + File.separator + "student" 
            + File.separator + "group_score.txt");
            
        if (groupScoreFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(groupScoreFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3 && parts[0].equals(className)) {
                        groupScores.put(parts[1], parts[2]);  // 保存小组名称和分数
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 从文件读取小组数据
        File groupFile = new File("D:" + File.separator + "workspacemax" 
            + File.separator + "java" 
            + File.separator + "student" 
            + File.separator + "group.txt");
            
        if (groupFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(groupFile))) {
                String line;
                int index = 1;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2 && parts[0].equals(className)) {
                        String groupName = parts[1];
                        String score = groupScores.getOrDefault(groupName, "0");  // 获取小组分数，如果没有则默认为0
                        tableModel.addRow(new String[]{
                            String.valueOf(index++),
                            groupName,
                            score
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "读取小组数据失败：" + e.getMessage(), 
                    "错误", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
