package gui;

import javax.swing.*;
import java.awt.*;

public class JFrameDialog extends JDialog {
    public JFrameDialog(MyJFrame frame){
        super(frame,"第一个JDialog窗体",true);
        Container container = getContentPane();
        container.add(new JLabel("这是一个对话框"));
        setBounds(550,300,900,600);
    }
}
