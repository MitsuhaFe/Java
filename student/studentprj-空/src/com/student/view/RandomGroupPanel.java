package com.student.view;

import com.student.util.Constant;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class RandomGroupPanel extends JPanel {
    private JLabel lbl1 = new JLabel("小组名：");
    private JLabel lbl2 = new JLabel("学生姓名：");
    private JLabel lbl3 = new JLabel("学生照片：");
    private JLabel lblPic = new JLabel("照片");
    private JLabel lbl4 = new JLabel("小组评分");
    private JTextField txtGroup = new JTextField();
    private JTextField txtStudent = new JTextField();
    private JTextField txtScore = new JTextField();
    private JButton btnChooseGroup = new JButton("随机小组");
    private JButton btnChooseStudent = new JButton("随机学生");
    private JButton btnAbsence = new JButton("缺勤");
    private JButton btnLeave = new JButton("请假");
    private JButton btnScore = new JButton("小组评分");
    private JComboBox<String> classComboBox;
    Thread threadGroup = null;   // 随机抽取小组的线程
    Thread threadStudent = null;   // 随机抽取学生的线程
    private List<String> groupList = new ArrayList<>();  // 存储当前班级的小组列表
    private Random random = new Random();

    public RandomGroupPanel() {
        this.setBorder(new TitledBorder(new EtchedBorder(), "随机小组点名"));
        this.setLayout(null);

        // 添加班级选择
        JLabel lblClass = new JLabel("选择班级：");
        classComboBox = new JComboBox<>();
        classComboBox.addItem("全部班级");  // 添加全部班级选项
        loadClasses();

        this.add(lblClass);
        this.add(classComboBox);
        this.add(lbl1);
        this.add(lbl2);
        this.add(lbl3);
        this.add(txtGroup);
        this.add(txtStudent);
        this.add(lblPic);
        this.add(btnChooseGroup);
        this.add(btnChooseStudent);
        this.add(btnAbsence);
        this.add(btnLeave);
        this.add(lbl4);
        this.add(txtScore);
        this.add(btnScore);

        // 设置组件位置
        lblClass.setBounds(50, 30, 100, 30);
        classComboBox.setBounds(50, 60, 150, 30);
        lbl1.setBounds(50, 100, 100, 30);
        txtGroup.setBounds(50, 140, 100, 30);
        txtGroup.setEditable(false);
        btnChooseGroup.setBounds(50, 180, 100, 30);

        lbl4.setBounds(50, 230, 100, 30);
        txtScore.setBounds(50, 270, 100, 30);
        btnScore.setBounds(50, 310, 100, 30);

        lbl2.setBounds(220, 50, 100, 30);
        txtStudent.setBounds(220, 90, 130, 30);
        txtStudent.setEditable(false);
        lblPic.setBounds(220, 130, 130, 150);
        btnChooseStudent.setBounds(220, 300, 100, 30);
        btnAbsence.setBounds(220, 340, 60, 30);
        btnLeave.setBounds(290, 340, 60, 30);

        // 班级选择事件
        classComboBox.addActionListener(e -> {
            String selectedClass = (String) classComboBox.getSelectedItem();
            if (selectedClass != null) {
                if ("全部班级".equals(selectedClass)) {
                    loadAllGroups();  // 加载所有班级的小组
                } else {
                    loadGroups(selectedClass);  // 加载选中班级的小组
                }
            }
        });

        // 初始加载所有班级的小组
        loadAllGroups();

        // 随机小组按钮事件
        btnChooseGroup.addActionListener(e -> {
            if (classComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "请选择班级", "", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (groupList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "当前没有可选的小组", "", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (e.getActionCommand().equals("停")) {
                if (threadGroup != null) {
                    threadGroup.interrupt();
                    threadGroup = null;
                }
                btnChooseGroup.setText("随机小组");
            } else {
                btnChooseGroup.setText("停");
                threadGroup = new Thread(() -> {
                    try {
                        while (!Thread.interrupted()) {
                            // 随机选择一个小组
                            final String randomGroup = groupList.get(random.nextInt(groupList.size()));
                            SwingUtilities.invokeLater(() -> txtGroup.setText(randomGroup));
                            Thread.sleep(50);  // 控制滚动速度
                        }
                    } catch (InterruptedException ex) {
                        // 线程被中断，不需要处理
                    }
                });
                threadGroup.start();
            }
        });

        // 如果已经选择了班级，加载该班级的小组
        if (Constant.CLASS_PATH != null && !Constant.CLASS_PATH.isEmpty()) {
            classComboBox.setSelectedItem(Constant.CLASS_PATH);
            loadGroups(Constant.CLASS_PATH);
        }

        // 修改小组评分按钮事件
        btnScore.addActionListener(e -> {
            if (txtGroup.getText() == null || txtGroup.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先抽取小组", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (txtScore.getText() == null || txtScore.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写分数", "", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 验证分数是否为0-100的整数
            try {
                int score = Integer.parseInt(txtScore.getText().trim());
                if (score < 0 || score > 100) {
                    JOptionPane.showMessageDialog(this, "分数必须在0-100之间", "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "分数必须是整数", "", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedClass = (String) classComboBox.getSelectedItem();
            String groupName = txtGroup.getText().trim();
            String score = txtScore.getText().trim();

            // 如果是从"全部班级"中选择的小组，需要解析班级名
            if ("全部班级".equals(selectedClass)) {
                String[] parts = groupName.split("-");
                if (parts.length >= 2) {
                    selectedClass = parts[0];
                    groupName = parts[1];
                }
            }

            // 更新小组分数文件
            File groupScoreFile = new File("D:" + File.separator + "workspacemax" 
                + File.separator + "java" 
                + File.separator + "student" 
                + File.separator + "group_score.txt");

            try {
                // 读取现有分数记录
                List<String> allScores = new ArrayList<>();
                boolean scoreUpdated = false;
                
                if (groupScoreFile.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(groupScoreFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",");
                            if (parts.length >= 3 && parts[0].equals(selectedClass) && parts[1].equals(groupName)) {
                                // 更新已存在的分数记录
                                allScores.add(selectedClass + "," + groupName + "," + score);
                                scoreUpdated = true;
                            } else {
                                allScores.add(line);
                            }
                        }
                    }
                }

                // 如果是新的分数记录，添加到列表中
                if (!scoreUpdated) {
                    allScores.add(selectedClass + "," + groupName + "," + score);
                }

                // 写入所有分数记录
                try (PrintWriter writer = new PrintWriter(new FileWriter(groupScoreFile))) {
                    for (String line : allScores) {
                        writer.println(line);
                    }
                }

                JOptionPane.showMessageDialog(this, "小组评分已保存", "", JOptionPane.INFORMATION_MESSAGE);
                txtScore.setText("");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "保存小组分数失败：" + ex.getMessage(), 
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

    // 新增加载所有班级小组的方法
    private void loadAllGroups() {
        groupList.clear();
        File groupFile = new File("D:" + File.separator + "workspacemax" 
            + File.separator + "java" 
            + File.separator + "student" 
            + File.separator + "group.txt");
            
        if (groupFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(groupFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        // 将班级名和小组名组合在一起显示
                        groupList.add(parts[0] + "-" + parts[1]);
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
        
        if (groupList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "没有找到任何小组", 
                "提示", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadGroups(String className) {
        groupList.clear();
        File groupFile = new File("D:" + File.separator + "workspacemax" 
            + File.separator + "java" 
            + File.separator + "student" 
            + File.separator + "group.txt");
            
        // 加载小组分数
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
                        groupScores.put(parts[1], parts[2]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            
        if (groupFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(groupFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2 && parts[0].equals(className)) {
                        String groupName = parts[1];
                        String score = groupScores.getOrDefault(groupName, "0");
                        groupList.add(groupName + "(分数:" + score + ")");
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
        
        if (groupList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "当前班级没有小组", 
                "提示", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
