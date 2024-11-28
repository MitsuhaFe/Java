package com.student;

import com.student.util.Constant;
import com.student.view.MainFrame;
import java.io.File;

public class Application {
    public static void main(String[] args) {
        // 确保基础目录存在
        File baseDir = new File(Constant.FILE_PATH);
        if (!baseDir.exists()) {
            if (!baseDir.mkdirs()) {
                System.err.println("无法创建基础目录：" + Constant.FILE_PATH);
            }
        }
        
        // 启动主窗口
        new MainFrame();
    }
}
