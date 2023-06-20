package com.cqupt.faceid.domin;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class faceDetect {
    private String imgPath = "img/face";
    public static void imageFaceDetection(String pic) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // 从配置文件lbpcascade_frontalface.xml中创建一个人脸识别器，文件位于opencv安装目录中
        CascadeClassifier faceDetector = new CascadeClassifier(".idea/libraries/haarcascade_frontalface_alt.xml");
        // 读取测试图片
        String imgPath = "img/face" + pic + "/1.jpg";
        Mat image = Imgcodecs.imread(imgPath);
        if (image.empty()) {
            throw new RuntimeException("图片内存为空");
        }

        // 检测脸部
        MatOfRect face = new MatOfRect();
        // 检测图像中的人脸
        faceDetector.detectMultiScale(image, face);

        // 识别图片中的所以人脸并分别保存
        int i = 1;
        for (Rect rect : face.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 3);
            // 进行图片裁剪
            imageCut(imgPath, "img/faceCut/" + pic + ".jpg", rect.x, rect.y, rect.width, rect.height);
            i++;
        }
        // 展示图片
        HighGui.imshow("人脸识别", image);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }

    public static void imageCut(String readPath, String outPath, int x, int y, int width, int height) {
        // 原始图像
        Mat image = Imgcodecs.imread(readPath);
        // 截取的区域
        Rect rect = new Rect(x, y, width, height);
        Mat sub = image.submat(rect);
        Mat mat = new Mat();
        Size size = new Size(width, height);
        // 人脸进行截图并保存
        Imgproc.resize(sub, mat, size);
        Imgcodecs.imwrite(outPath, mat);
    }
}