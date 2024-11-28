package com.student.view;

import com.student.entity.Group;
import com.student.util.Constant;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;

public class GroupAddPanel extends JPanel {
    private JComboBox<String> classComboBox;
    private JTextField txtName;

    public GroupAddPanel() {
        this.setLayout(null);
        this.setBorder(new TitledBorder(new EtchedBorder(), "新增小组"));

        // 添加班级选择下拉框
        JLabel lblClass = new JLabel("选择班级：");
        classComboBox = new JComboBox<>();
        loadClasses();  // 加载班级列表

        JLabel lblName = new JLabel("小组名称：");
        txtName = new JTextField();
        JButton btnName = new JButton("确认");

        // 添加组件
        this.add(lblClass);
        this.add(classComboBox);
        this.add(lblName);
        this.add(txtName);
        this.add(btnName);

        // 设置位置
        lblClass.setBounds(200, 40, 100, 30);
        classComboBox.setBounds(200, 80, 200, 30);
        lblName.setBounds(200, 120, 100, 30);
        txtName.setBounds(200, 160, 200, 30);
        btnName.setBounds(200, 210, 100, 30);

        // 如果已经选择了班级，设置为当前选中项
        if (Constant.CLASS_PATH != null && !Constant.CLASS_PATH.isEmpty()) {
            classComboBox.setSelectedItem(Constant.CLASS_PATH);
        }

        btnName.addActionListener(e -> {
            String selectedClass = (String) classComboBox.getSelectedItem();
            if (selectedClass == null || selectedClass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择班级", "", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (txtName.getText() == null || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写小组名称", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String groupName = txtName.getText().trim();
            
            // 检查小组名是否已存在
            File groupFile = new File("D:" + File.separator + "workspacemax" 
                + File.separator + "java" 
                + File.separator + "student" 
                + File.separator + "group.txt");
                
            if (groupFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(groupFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 2 && parts[0].equals(selectedClass) && parts[1].equals(groupName)) {
                            JOptionPane.showMessageDialog(this, "小组名称已存在", "", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            
            try {
                // 确保父目录存在
                File parentDir = groupFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                
                // 追加写入文件
                try (PrintWriter writer = new PrintWriter(new FileWriter(groupFile, true))) {
                    writer.println(selectedClass + "," + groupName);
                }
                
                JOptionPane.showMessageDialog(this, "新增小组成功", "", JOptionPane.INFORMATION_MESSAGE);
                txtName.setText("");
                
                // 如果父窗口是MainFrame，刷新小组列表
                if (SwingUtilities.getWindowAncestor(this) instanceof MainFrame) {
                    MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                    mainFrame.refreshGroupList();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "保存小组数据失败：" + ex.getMessage(), 
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
}
