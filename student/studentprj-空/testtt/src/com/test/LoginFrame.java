package com.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    public LoginFrame(){

        JLabel lblName = new JLabel("账号：");
        JTextField txtName = new JTextField("请填写账号");
        JLabel lblPwd = new JLabel("密码：");
        JPasswordField txtPwd = new JPasswordField();
        JButton btnOk = new JButton("确认");
        JButton btnCancle = new JButton("取消");
        this.getContentPane().setLayout(null);

        this.getContentPane().add(lblName);
        lblName.setBounds(100, 50, 80, 30);
        this.getContentPane().add(txtName);
        txtName.setBounds(160, 50, 120, 30);

        ButtonGroup bg = new ButtonGroup();
        JRadioButton rbm = new JRadioButton("男");
        JRadioButton rbf = new JRadioButton("女");
        bg.add(rbm);
        bg.add(rbf);

        this.getContentPane().add(rbm);
        rbm.setBounds(100, 90, 100, 30);
        this.getContentPane().add(rbf);
        rbf.setBounds(220, 90, 100, 30);

        rbm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "选择了性别", "选择", JOptionPane.ERROR_MESSAGE);
            }
        });

        JCheckBox  checkBox = new JCheckBox("看书");
        this.getContentPane().add(checkBox);
        checkBox.setBounds(100, 130, 100, 30);

        JComboBox cmbHome = new JComboBox();
        cmbHome.addItem("北京");
        cmbHome.addItem("河北");
        this.getContentPane().add(cmbHome);
        cmbHome.setBounds(100, 170, 100, 30);




//        this.getContentPane().setLayout(new FlowLayout());
//        this.getContentPane().setLayout(new GridLayout(3,3));
//        this.getContentPane().setLayout(new BorderLayout());
//        JPanel panel = new JPanel();
//        panel.setLayout(new FlowLayout());
//        for(int i = 0; i < 9; i++){
//            JButton btn = new JButton("按钮" + i);
//            panel.add(btn);
//        }
//        this.getContentPane().add(panel, BorderLayout.NORTH);
//
//        JTextField txt = new JTextField();
//        this.getContentPane().add(txt, BorderLayout.CENTER);

        this.setSize(500, 300);
        this.setVisible(true);
    }
}
