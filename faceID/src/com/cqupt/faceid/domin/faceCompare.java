package com.cqupt.faceid.domin;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.util.Arrays;
import static org.opencv.imgcodecs.Imgcodecs.imread;

public class faceCompare {
    // 初始化人脸探测器
    static CascadeClassifier faceDetector;

    static {
        // 加载OpenCV本地库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        faceDetector = new CascadeClassifier(".idea/libraries/haarcascade_frontalface_alt.xml");
    }

    public double faceReco() {
        double comparison = faceRecognitionComparison("img/faceCut/Src.jpg", "img/faceCut/Img.jpg");
        comparison *= 100;
        System.out.println(comparison);
        return comparison;
    }


    public static double faceRecognitionComparison(String src, String img) {
        Mat mat1 = imread(src);
        Mat mat2 = imread(img);
        if (mat2 == null) {
            return 0.0; // 没有检测到人脸，返回一个很小的值
        }

        // 将图片转换为灰度图
        Imgproc.cvtColor(mat1, mat1, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(mat2, mat2, Imgproc.COLOR_BGR2GRAY);

        Mat mat3 = new Mat();
        Mat mat4 = new Mat();
        // 颜色范围
        MatOfFloat ranges = new MatOfFloat(0f, 256f);
        // 使用256个bin来计算直方图
        MatOfInt histSize = new MatOfInt(256);

        // 使用一个通道来计算直方图
        Imgproc.calcHist(Arrays.asList(mat1), new MatOfInt(0), new Mat(), mat3, histSize, ranges);
        Imgproc.calcHist(Arrays.asList(mat2), new MatOfInt(0), new Mat(), mat4, histSize, ranges);

        // 对直方图进行归一化和平滑处理
        Core.normalize(mat3, mat3, 0, 1, Core.NORM_MINMAX);
        Core.normalize(mat4, mat4, 0, 1, Core.NORM_MINMAX);
        Imgproc.GaussianBlur(mat3, mat3, new Size(5, 5), 0);
        Imgproc.GaussianBlur(mat4, mat4, new Size(5, 5), 0);

        // 使用相关系数作为比较方法
        return Imgproc.compareHist(mat3, mat4, Imgproc.CV_COMP_CORREL);
    }
}
