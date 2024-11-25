package com.student.view;

import com.student.util.Constant;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class ClassListPanel extends JPanel {
    String[] headers = {"序号", "班级名称"};
    JTable classTable;
    JTextField txtName = new JTextField();
    JButton btnEdit = new JButton("修改");
    JButton btnDelete = new JButton("删除");

    public ClassListPanel() {
        this.setBorder(new TitledBorder(new EtchedBorder(), "班级列表"));
        this.setLayout(new BorderLayout());
        
        File classDir = new File(Constant.FILE_PATH);
        File[] classes = classDir.listFiles(File::isDirectory);
        if (classes == null) {
            classes = new File[0];
        }

        String[][] data = new String[classes.length][2];
        for (int i = 0; i < classes.length; i++) {
            data[i][0] = String.valueOf(i + 1);
            data[i][1] = classes[i].getName();
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, headers);
        classTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        classTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(classTable);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(txtName);
        txtName.setPreferredSize(new Dimension(200, 30));
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        this.add(btnPanel, BorderLayout.SOUTH);

        classTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if(selectedRow >= 0) {
                txtName.setText(data[selectedRow][1]);
            }
        });

        btnEdit.addActionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (txtName.getText() == null || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写班级名称", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String oldName = (String) classTable.getValueAt(selectedRow, 1);
            String newName = txtName.getText().trim();
            
            // 检查新名称的班级是否已存在
            File newClassDir = new File(Constant.FILE_PATH, newName);
            if (newClassDir.exists()) {
                JOptionPane.showMessageDialog(this, "该班级名称已存在", "", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 获取旧的班级目录并重命名
            File oldClassDir = new File(Constant.FILE_PATH, oldName);
            if (oldClassDir.renameTo(newClassDir)) {
                JOptionPane.showMessageDialog(this, "修改成功", "", JOptionPane.INFORMATION_MESSAGE);
                // 更新表格数据
                classTable.setValueAt(newName, selectedRow, 1);
                // 如果是当前选中的班级，更新 Constant.CLASS_PATH
                if (oldName.equals(Constant.CLASS_PATH)) {
                    Constant.CLASS_PATH = newName;
                }
                // 刷新父窗口标题
                if (SwingUtilities.getWindowAncestor(this) instanceof MainFrame) {
                    MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                    if (oldName.equals(mainFrame.getTitle())) {
                        mainFrame.setTitle(newName);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "修改失败，请检查文件权限", "", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnDelete.addActionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if(JOptionPane.showConfirmDialog(this, "删除班级会把小组、学生和成绩都删除，您确定要删除这个班级？", "", JOptionPane.YES_NO_OPTION) != 0){
                return;
            }
            
            String className = (String) classTable.getValueAt(selectedRow, 1);
            File selectedClassDir = new File(Constant.FILE_PATH, className);
            
            try {
                // 删除班级目录
                if (selectedClassDir.exists() && deleteDirectory(selectedClassDir)) {
                    // 删除group.txt中相关的小组记录
                    File groupFile = new File("D:" + File.separator + "workspacemax" 
                        + File.separator + "java" 
                        + File.separator + "student" 
                        + File.separator + "group.txt");
                        
                    if (groupFile.exists()) {
                        List<String> remainingLines = new ArrayList<>();
                        try (BufferedReader reader = new BufferedReader(new FileReader(groupFile))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] parts = line.split(",");
                                if (parts.length < 2 || !parts[0].equals(className)) {
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
                    }
                    
                    // 如果删除的是当前选中的班级，清空CLASS_PATH
                    if (className.equals(Constant.CLASS_PATH)) {
                        Constant.CLASS_PATH = "";
                        if (SwingUtilities.getWindowAncestor(this) instanceof MainFrame) {
                            MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                            mainFrame.setTitle("学生管理系统");
                        }
                    }
                    
                    JOptionPane.showMessageDialog(this, "删除班级成功", "", JOptionPane.INFORMATION_MESSAGE);
                    // 刷新班级列表
                    if (SwingUtilities.getWindowAncestor(this) instanceof MainFrame) {
                        MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                        mainFrame.refreshClassList();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "删除班级失败", "", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除班级时发生错误：" + ex.getMessage(), 
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // 添加递归删除目录的辅助方法
    private boolean deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }
}
