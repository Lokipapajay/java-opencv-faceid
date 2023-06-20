package com.cqupt.faceid.domin;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class faceRecog extends JPanel {

    private static BufferedImage mImg;
    private static VideoCapture capture;
    private static Mat capSrc;

    private BufferedImage mat2BI(Mat mat) {
        int dataSize = mat.cols() * mat.rows() * (int) mat.elemSize();
        byte[] data = new byte[dataSize];
        mat.get(0, 0, data);
        int type = mat.channels() == 1 ?
                BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR;

        if (type == BufferedImage.TYPE_3BYTE_BGR) {
            for (int i = 0; i < dataSize; i += 3) {
                byte blue = data[i + 0];
                data[i + 0] = data[i + 2];
                data[i + 2] = blue;
            }
        }
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);

        return image;
    }

    public void paintComponent(Graphics g) {
        if (mImg != null) {
            g.drawImage(mImg, 0, 0, mImg.getWidth(), mImg.getHeight(), this);
        }
    }

    public void facec() {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            capSrc = new Mat();
            capture = new VideoCapture(0);
            // capture.open(0);
            int height = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
            int width = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
            if (height == 0 || width == 0) {
                throw new Exception("找不到摄像头!");
            }

            JFrame frame = new JFrame("人脸解锁");
            // 设置按下右上角X号后关闭
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            faceCap panel = new faceCap();
            JButton btn = new JButton("开始比对");
            btn.addActionListener(e -> {
                saveImg();
                // 开始识别后关闭当前窗体
                frame.dispose();
            });
            JLabel jl = new JLabel("请将自己的脸放入屏幕正中央！");
            jl.setForeground(Color.RED);

            frame.setContentPane(panel);
            frame.setVisible(true);
            frame.add(btn);
            frame.add(jl);
            frame.setSize(width + frame.getInsets().left + frame.getInsets().right,
                    height + frame.getInsets().top + frame.getInsets().bottom);
            int n = 0;
            Mat temp = new Mat();
            while (frame.isShowing() && n < 5) {
                capture.read(capSrc);
                Imgproc.cvtColor(capSrc, temp, Imgproc.COLOR_BGRA2BGR);
                panel.mImg = panel.mat2BI(capSrc);
                panel.repaint();
            }
            capture.release();
            frame.setResizable(false);

        } catch (Exception e) {
            System.out.println("例外：" + e);
        } finally {
            System.out.println("--done--");
        }

    }

    //点击面板
    public static void saveImg() {
        Mat temp = new Mat();
        int height = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        int width = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        capture.read(capSrc);
        Imgproc.cvtColor(capSrc, temp, Imgproc.COLOR_BGRA2BGR);
        Imgcodecs.imwrite("img/faceImg/1.jpg", temp);
        System.out.println("保存图片");
    }
}