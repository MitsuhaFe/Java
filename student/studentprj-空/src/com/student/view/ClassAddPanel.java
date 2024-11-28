package com.student.view;

import com.student.util.Constant;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ClassAddPanel extends JPanel {
    public ClassAddPanel() {
        this.setLayout(null);
        this.setBorder(new TitledBorder(new EtchedBorder(), "新增班级"));
        JLabel lblName = new JLabel("班级名称：");
        JTextField txtName = new JTextField();
        JButton btnName = new JButton("确认");
        this.add(lblName);
        this.add(txtName);
        this.add(btnName);
        lblName.setBounds(200, 80, 100, 30);
        txtName.setBounds(200, 130, 200, 30);
        btnName.setBounds(200, 180, 100, 30);

        btnName.addActionListener(e -> {
            if (txtName.getText() == null || txtName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写班级名称", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String className = txtName.getText().trim();
                File baseDir = new File(Constant.FILE_PATH);
                // 确保基础目录存在
                if (!baseDir.exists()) {
                    if (!baseDir.mkdirs()) {
                        JOptionPane.showMessageDialog(this, "创建基础目录失败，请检查程序权限", "", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                File classDir = new File(baseDir, className);
                if (classDir.exists()) {
                    JOptionPane.showMessageDialog(this, "班级已存在", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    if (classDir.mkdir()) {
                        JOptionPane.showMessageDialog(this, "新增班级成功", "", JOptionPane.INFORMATION_MESSAGE);
                        txtName.setText("");
                        // 刷新班级列表
                        if (getParent() instanceof MainFrame) {
                            MainFrame mainFrame = (MainFrame) getParent();
                            mainFrame.refreshClassList();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "新增班级失败，可能原因：\n1. 没有写入权限\n2. 磁盘空间不足\n3. 文件名不合法", 
                            "错误", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SecurityException se) {
                    JOptionPane.showMessageDialog(this, 
                        "没有足够的权限创建目录，请检查程序权限", 
                        "权限错误", 
                        JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, 
                        "创建班级时发生错误：" + ex.getMessage(), 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

}
