package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyJFrame extends JFrame{
    public void createPanel(){
        JFrame jf = new JFrame();                                  //创建窗体对象
        jf.setTitle("班级学生管理系统。");                            //设置窗体标题
        Container container = jf.getContentPane();                 //获取主容器
        container.setLayout(null);                                 //容器使用绝对布局
        JLabel jl = new JLabel("这是一个JFrame的窗体");          //一个文本标签

        jl.setHorizontalAlignment(SwingConstants.CENTER);          //使标签上的文字居中
        container.add(jl);                                         //将标签添加到主容器中

        JButton jb = new JButton("弹出对话框");                 //创建一个按钮
        jb.setBounds(100,100,100,80);             //定义按钮的坐标和大小
        jb.addActionListener(new ActionListener(){                 //为按钮添加单击事件
            public void actionPerformed(ActionEvent e){            //单击事件触发的方法
                JFrameDialog dialog = new JFrameDialog(MyJFrame.this);//创建MyJDialog对话框
                dialog.setVisible(true);                           //使对话框可见
            }
        });
        container.add(jb);                                         //将按钮添加到容器中

        jf.setSize(900,600);                           //设置窗体宽高
        jf.setLocation(550,300);                              //设置窗体在屏幕中出现的位置
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         //关闭窗体则停止程序
        jf.setVisible(true);                                       //让窗体展示出来
    }


}
