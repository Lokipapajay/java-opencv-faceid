package com.cqupt.faceid.ui;

import com.cqupt.faceid.domin.faceCap;
import com.cqupt.faceid.domin.faceCompare;
import com.cqupt.faceid.domin.faceDetect;
import com.cqupt.faceid.domin.faceRecog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;

public class ui {

    private String path = "img";

    public void start() {
        JFrame jFrame1 = new JFrame("人脸识别解锁系统");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        Dimension frameSize = jFrame1.getSize();
        int x = (screenWidth - frameSize.width) / 3;
        int y = (screenHeight - frameSize.height) / 3;
        JPanel jPanel1 = new JPanel();
        jPanel1.setOpaque(false);
        jPanel1.setLayout(null);
        ImageIcon bg = new ImageIcon("img/background.jpg");
        JLabel jLabel1 = new JLabel(bg);
        jLabel1.setSize(bg.getIconWidth(), bg.getIconHeight());

        JButton btn1 = new JButton("录入人脸");
        JButton btn2 = new JButton("开始检测");
        ImageIcon img1 = new ImageIcon("img/settings.jpg");
        JButton btn3 = new JButton("", img1);

        btn1.setBounds(540, 170, 160, 80);
        btn2.setBounds(540, 270, 160, 80);
        btn3.setBounds(20, 580, 80, 80);
        jPanel1.add(btn1);
        jPanel1.add(btn2);
        jPanel1.add(btn3);
        jPanel1.add(jLabel1);
        jFrame1.getContentPane().add(jPanel1);    //将窗体转换为容器再添加上面板
        jFrame1.setSize(2 * x, 2 * y);
        jFrame1.setVisible(true);
        jFrame1.setResizable(false);

        btn3.addActionListener(e -> {
            ui settings = new ui();
            settings.setting();
        });

        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Void,Void> worker=new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        faceCap fc = new faceCap();
                        fc.facec();
                        String src = "Src";
                        faceDetect.imageFaceDetection(src);
                        return null;
                    }
                };
                worker.execute();
            }
        });
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Void,Void> worker=new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        faceRecog fr = new faceRecog();
                        fr.facec();
                        String img = "Img";
                        faceDetect.imageFaceDetection(img);
                        faceCompare fc = new faceCompare();
                        ifSuccess(fc.faceReco());
                        return null;
                    }
                };
                worker.execute();
            }
        });
        jFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setting() {
        JFrame jFrame2 = new JFrame("设置");
        JPanel jPanel2 = new JPanel();
        JLabel jLabel2 = new JLabel("请输入文件存放路径：");
        JButton btn5 = new JButton("确定");
        JTextField jTextField = new JTextField(10);
        jPanel2.add(jLabel2, BorderLayout.CENTER);
        jPanel2.add(jTextField);
        jPanel2.add(btn5);
        jFrame2.setSize(480, 90);
        jFrame2.getContentPane().add(jPanel2);
        jFrame2.setVisible(true); // 使窗口可见
        jFrame2.setResizable(false);
        jFrame2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // Add a listener to the button
        btn5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the text from the text field
                String path = jTextField.getText();
                // Save the text to a file
                try {
                    FileWriter writer = new FileWriter(path);
                    writer.write("This is a test file.");
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // Close the frame
                jFrame2.dispose();
            }
        });
    }


    public void ifSuccess(double rate) {
        if (rate > 50) {
            JFrame frameS = new JFrame();
            JLabel jLabelS = new JLabel("恭喜您，解锁成功！");
            JLabel jLabelR = new JLabel("面部相似度为：" + rate + "%");
            JButton btnS = new JButton("确定");
            btnS.setBounds(215, 170, 90, 45);
            jLabelS.setBounds(40, 50, 500, 50);
            jLabelR.setBounds(40, 140, 500, 40);
            Font fontS = new Font("宋体", Font.PLAIN, 50);
            jLabelS.setFont(fontS);
            jLabelS.setForeground(new Color(255, 0, 0));
            JPanel jPanelS = new JPanel();
            jPanelS.setLayout(null);
            jPanelS.add(jLabelS);
            jPanelS.add(jLabelR);
            jPanelS.add(btnS);
            frameS.setSize(520, 270);
            frameS.getContentPane().add(jPanelS);
            frameS.setVisible(true); // 使窗口可见
            frameS.setResizable(false);
            btnS.addActionListener(e -> {
                frameS.dispatchEvent(new WindowEvent(frameS, WindowEvent.WINDOW_CLOSING));
            });
            frameS.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        } else {
            JFrame frameF = new JFrame();
            JLabel jLabelF = new JLabel("对不起，解锁失败！");
            JLabel jLabelR = new JLabel("面部相似度为：" + rate + "%");
            JButton btnF = new JButton("确定");
            btnF.setBounds(215, 170, 90, 45);
            jLabelF.setBounds(40, 50, 500, 50);
            jLabelR.setBounds(40, 140, 500, 40);
            Font fontF = new Font("宋体", Font.PLAIN, 50);
            jLabelF.setFont(fontF);
            jLabelF.setForeground(new Color(255, 0, 0));
            JPanel jPanelF = new JPanel();
            jPanelF.setLayout(null);
            jPanelF.add(jLabelF);
            jPanelF.add(jLabelR);
            jPanelF.add(btnF);
            frameF.setSize(520, 270);
            frameF.getContentPane().add(jPanelF);
            frameF.setVisible(true); // 使窗口可见
            frameF.setResizable(false);
            btnF.addActionListener(e -> {
                frameF.dispatchEvent(new WindowEvent(frameF, WindowEvent.WINDOW_CLOSING));
            });
            frameF.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }
    }
}