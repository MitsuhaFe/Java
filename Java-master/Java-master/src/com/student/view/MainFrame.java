package com.student.view;

import com.student.util.Constant;
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        this.getContentPane().setLayout(new BorderLayout());
        initMenus();

        this.setSize(600, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void initMenus() {
        JMenuBar mainMenu = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        JMenuItem changeClassMenuItem = new JMenuItem("切换当前班");
        JMenuItem exportScoreMenuItem = new JMenuItem("导出当前班成绩");
        JMenuItem exitMenuItem = new JMenuItem("退出");

        JMenu classMenu = new JMenu("班级管理");
        JMenuItem addClassMenuItem = new JMenuItem("新增班级");
        JMenuItem classListMenuItem = new JMenuItem("班级列表");

        JMenu groupMenu = new JMenu("小组管理");
        JMenuItem addGroupMenuItem = new JMenuItem("新增小组");
        JMenuItem groupListMenuItem = new JMenuItem("小组列表");

        JMenu studentMenu = new JMenu("学生管理");
        JMenuItem addStudentMenuItem = new JMenuItem("新增学生");
        JMenuItem studentListMenuItem = new JMenuItem("学生列表");

        JMenu onClassMenu = new JMenu("课堂管理");
        JMenuItem randomGroupMenuItem = new JMenuItem("随机小组");
        JMenuItem randomStudentMenuItem = new JMenuItem("随机学生");

        this.getContentPane().add(mainMenu, BorderLayout.NORTH);
        mainMenu.add(fileMenu);
        mainMenu.add(classMenu);
        mainMenu.add(groupMenu);
        mainMenu.add(studentMenu);
        mainMenu.add(onClassMenu);
        fileMenu.add(changeClassMenuItem);
        fileMenu.add(exportScoreMenuItem);
        fileMenu.add(exitMenuItem);
        classMenu.add(addClassMenuItem);
        classMenu.add(classListMenuItem);
        groupMenu.add(addGroupMenuItem);
        groupMenu.add(groupListMenuItem);
        studentMenu.add(addStudentMenuItem);
        studentMenu.add(studentListMenuItem);
        onClassMenu.add(randomGroupMenuItem);
        onClassMenu.add(randomStudentMenuItem);
        // 添加菜单事件
        // 切换班级
        changeClassMenuItem.addActionListener(e -> {
            this.getContentPane().removeAll();
            initMenus();
            ChangeClassPanel changeClassPanel = new ChangeClassPanel(this, "ChangeClass");
            this.getContentPane().add(changeClassPanel, BorderLayout.CENTER);
            this.getContentPane().validate();
        });
        // 导出成绩
        exportScoreMenuItem.addActionListener(e -> {
            if (Constant.CLASS_PATH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先选择班级", "", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // TODO 导出
                JOptionPane.showMessageDialog(this, "成绩已导出", "", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        // 退出程序
        exitMenuItem.addActionListener(e -> System.exit(0));
        // 新增班级
        addClassMenuItem.addActionListener(e -> {
            this.getContentPane().removeAll();
            initMenus();
            ClassAddPanel classAddPanel = new ClassAddPanel();
            this.getContentPane().add(classAddPanel, BorderLayout.CENTER);
            this.getContentPane().validate();
        });
        // 班级列表
        classListMenuItem.addActionListener(e -> {
            this.getContentPane().removeAll();
            initMenus();
            ClassListPanel classListPanel = new ClassListPanel();
            this.getContentPane().add(classListPanel, BorderLayout.CENTER);
            this.getContentPane().validate();
        });
        // 新增小组
        addGroupMenuItem.addActionListener(e -> {
            if (Constant.CLASS_PATH == null || Constant.CLASS_PATH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先在【文件】菜单中选择班级", "", JOptionPane.WARNING_MESSAGE);
                this.getContentPane().removeAll();
                initMenus();
                ChangeClassPanel changeClassPanel = new ChangeClassPanel(this, "GroupAdd");
                this.getContentPane().add(changeClassPanel, BorderLayout.CENTER);
                this.getContentPane().validate();
            } else {
                this.getContentPane().removeAll();
                initMenus();
                this.getContentPane().add(new GroupAddPanel(), BorderLayout.CENTER);
                this.getContentPane().repaint();
                this.getContentPane().validate();
            }
        });
        // 小组列表
        groupListMenuItem.addActionListener(e -> {
            this.getContentPane().removeAll();
            initMenus();
            this.getContentPane().add(new GroupListPanel(), BorderLayout.CENTER);
            this.getContentPane().repaint();
            this.getContentPane().validate();
        });
        // 新增学生
        addStudentMenuItem.addActionListener(e -> {
            if (Constant.CLASS_PATH == null || Constant.CLASS_PATH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先在【文件】菜单中选择班级", "", JOptionPane.WARNING_MESSAGE);
                this.getContentPane().removeAll();
                initMenus();
                ChangeClassPanel changeClassPanel = new ChangeClassPanel(this, "StudentAdd");
                this.getContentPane().add(changeClassPanel, BorderLayout.CENTER);
                this.getContentPane().validate();
            } else {
                this.getContentPane().removeAll();
                initMenus();
                this.getContentPane().add(new StudentAddPanel(), BorderLayout.CENTER);
                this.getContentPane().repaint();
                this.getContentPane().validate();
            }
        });
        // 学生列表
        studentListMenuItem.addActionListener(e -> {
            this.getContentPane().removeAll();
            initMenus();
            this.getContentPane().add(new StudentListPanel(), BorderLayout.CENTER);
            this.getContentPane().repaint();
            this.getContentPane().validate();
        });
        // 随机抽取小组
        randomGroupMenuItem.addActionListener(e -> {
            this.getContentPane().removeAll();
            initMenus();
            this.getContentPane().add(new RandomGroupPanel(), BorderLayout.CENTER);
            this.getContentPane().repaint();
            this.getContentPane().validate();
        });
        // 随机抽取学生
        randomStudentMenuItem.addActionListener(e -> {
            this.getContentPane().removeAll();
            initMenus();
            this.getContentPane().add(new RandomStudentPanel(), BorderLayout.CENTER);
            this.getContentPane().repaint();
            this.getContentPane().validate();
        });
    }

    public void refreshClassList() {
        // 移除旧的班级列表面板
        for (Component component : this.getContentPane().getComponents()) {
            if (component instanceof ClassListPanel) {
                this.remove(component);
            }
        }
        // 添加新的班级列表面板
        ClassListPanel classListPanel = new ClassListPanel();
        this.add(classListPanel);
        this.revalidate();
        this.repaint();
    }

    public void refreshGroupList() {
        // 移除旧的小组列表面板
        for (Component component : this.getContentPane().getComponents()) {
            if (component instanceof GroupListPanel) {
                this.remove(component);
            }
        }
        // 添加新的小组列表面板
        GroupListPanel groupListPanel = new GroupListPanel();
        this.add(groupListPanel);
        this.revalidate();
        this.repaint();
    }
}
