package com.student.view;

import com.student.util.Constant;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ChangeClassPanel extends JScrollPane {
    JLabel infoLbl = new JLabel();
    private final String sourcePanel;  // 记录来源页面

    public ChangeClassPanel(MainFrame mainFrame, String source) {
        this.sourcePanel = source;  // 保存来源页面信息
        
        this.setBorder(new TitledBorder(new EtchedBorder(), "新选择班级"));
        int x = 160, y = 100;
        this.setLayout(null);
        // 读取目录获取班级
        File classDir = new File(Constant.FILE_PATH);
        File[] files = classDir.listFiles(File::isDirectory);
        
        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(this, "请先创建班级", "", JOptionPane.INFORMATION_MESSAGE);
        } else {
            ButtonGroup btnGroup = new ButtonGroup();
            for (File file : files) {
                if (file.isDirectory()) {
                    JRadioButton classRadio = new JRadioButton(file.getName());
                    // 如果是当前选中的班级，设置为选中状态
                    if (file.getName().equals(Constant.CLASS_PATH)) {
                        classRadio.setSelected(true);
                    }
                    btnGroup.add(classRadio);
                    this.add(classRadio);
                    classRadio.setBounds(x, y, 200, 30);
                    y += 40;
                    
                    // 添加选择事件监听器
                    classRadio.addActionListener(e -> {
                        mainFrame.setTitle(classRadio.getText());
                        Constant.CLASS_PATH = classRadio.getText();
                        // 清空当前小组和学生数据
                        Constant.groups.clear();
                        Constant.students.clear();
                        
                        // 更新信息标签
                        infoLbl.setText("班级：" + classRadio.getText() + "，班级学生总数：" + Constant.students.size());
                        infoLbl.setBounds(160, 100, 200, 30);
                        
                        // 显示成功消息
                        JOptionPane.showMessageDialog(this, "已切换到班级：" + Constant.CLASS_PATH, 
                            "切换成功", JOptionPane.INFORMATION_MESSAGE);
                            
                        // 根据来源页面跳转回对应界面
                        mainFrame.getContentPane().removeAll();
                        mainFrame.initMenus();
                        switch (sourcePanel) {
                            case "StudentAdd":
                                mainFrame.getContentPane().add(new StudentAddPanel(), BorderLayout.CENTER);
                                break;
                            case "StudentList":
                                mainFrame.getContentPane().add(new StudentListPanel(), BorderLayout.CENTER);
                                break;
                            case "GroupAdd":
                                mainFrame.getContentPane().add(new GroupAddPanel(), BorderLayout.CENTER);
                                break;
                            case "GroupList":
                                mainFrame.getContentPane().add(new GroupListPanel(), BorderLayout.CENTER);
                                break;
                            case "RandomGroup":
                                mainFrame.getContentPane().add(new RandomGroupPanel(), BorderLayout.CENTER);
                                break;
                            case "RandomStudent":
                                mainFrame.getContentPane().add(new RandomStudentPanel(), BorderLayout.CENTER);
                                break;
                        }
                        mainFrame.getContentPane().revalidate();
                        mainFrame.getContentPane().repaint();
                    });
                }
            }
            
            this.repaint();
            this.validate();
        }
    }
}
