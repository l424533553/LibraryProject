package com.xuanyuan.library.help;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Administrator on 2018/8/26 0026.
 * 二维码 帮助类  需要zxing的框架支持
 * implementation 'com.google.zxing:core:3.3.3'
 */
public class QRCreateHelper {
    /**
     * 根据内容获取 打印二维码的 的各像素点
     *
     * @param contents 文本内容
     * @param width    点阵像素宽度 ，字节个数   *8即为像素点个数
     * @param height   点阵像素高度，字节个数   *8即为像素点个数
     * @return 获得像素点  数组阵列  集合
     */
    public static BitMatrix createPixels(String contents, int width, int height) {
        BitMatrix bitMatrix = null;
        //判断URL合法性
        if (TextUtils.isEmpty(contents)) {
            return null;
        }
        int QR_WIDTH = width * 8;  //  二维码宽
        int QR_HEIGHT = height * 8; //  二维码高

        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.CHARACTER_SET, "gbk");
//      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);// 容错率
        hints.put(EncodeHintType.MARGIN, 0);  //设置白边

        //图像数据转换，使用了矩阵转换
        try {
            bitMatrix = new QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitMatrix;
    }

    /**
     * 位图打印的方式   获取打印数据
     * 效率很高（相对于下面）   8*8   位图模式  也有单位图 的打印方式
     * 数据命令不一样 ，处理方式 也会不一样
     * <p>
     * 未创建多余的 打印临时变量 ，比较高效
     *
     * @param contents  打印的内容
     * @param width     宽度  8位 字节长
     * @param height    高度  8位 字节长
     * @param maxPix    最大像素宽    48*8=384 ，一般为51mm 的纸或者打印机的最大的像素点宽度
     * @param printHead 位图打印的打印头  printHead[] 如{ 0x1B,0x4B,0x80, 0x01 }
     */
    public List<byte[]> createPixelsList(String contents, int width, int height, int maxPix, byte[] printHead) {
        // 得到的bitMatrix是根据从左到右，从上到下获取得到的数据
        // 下面这里按照二维码的算法，逐个生成二维码的图片，该方法使用了矩阵转换
        BitMatrix bitMatrix = createPixels(contents, width, height);
        if (bitMatrix == null) {
            return null;
        }
        List<byte[]> list = new ArrayList<>();

        for (int i = height - 1; i >= 0; i--) {
            byte[] rowByte = new byte[maxPix + printHead.length];// 384+4
            // 从printHead中复制其中的数据，到rowByte[]数组中去即可
            System.arraycopy(printHead, 0, rowByte, 0, printHead.length);
            for (int j = 0; j < width * 8; j++) {
                int[] dest = new int[8];
                dest[0] = getPixels(bitMatrix, i * 8, j - 4);
                dest[1] = getPixels(bitMatrix, i * 8 + 1, j - 4);
                dest[2] = getPixels(bitMatrix, i * 8 + 2, j - 4);
                dest[3] = getPixels(bitMatrix, i * 8 + 3, j - 4);
                dest[4] = getPixels(bitMatrix, i * 8 + 4, j - 4);
                dest[5] = getPixels(bitMatrix, i * 8 + 5, j - 4);
                dest[6] = getPixels(bitMatrix, i * 8 + 6, j - 4);
                dest[7] = getPixels(bitMatrix, i * 8 + 7, j - 4);
                rowByte[j + printHead.length] = dealArray(dest);
            }
            list.add(rowByte);
        }
        return list;
    }

    /**
     * 根据24点阵的方式 创建像素点阵
     */
    public List<byte[]> createPixelsListBy24(String contents, int width, int height, int maxPix, byte[] printHead) {
        BitMatrix bitMatrix = createPixels(contents, width, height);
        if (bitMatrix == null) {
            return null;
        }
        ArrayList<byte[]> bytes = new ArrayList<>();

        for (int j = 0; j < height / 3; j++) {
            byte[] data = new byte[maxPix * 3 + printHead.length];
            System.arraycopy(printHead, 0, data, 0, printHead.length);
            int k = printHead.length;
            for (int i = 0; i < width * 8; i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = getPixels(bitMatrix, i, j * 24 + m * 8 + n);//将 8 个0,1 byte 转成一个字节
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
            bytes.add(data);
        }

        return bytes;
    }

    /**
     * @return 返回的是一个完整的点阵像素byte,
     * 注意有一些打印机的内存不够，byte太大会有打印缺失
     */
    public byte[] getRQBitmapData(String contents, int width, int height, int maxPix, byte[] printHead) {
        BitMatrix bitMatrix = createPixels(contents, width, height);
        if (bitMatrix == null) {
            return null;
        }

        byte[] data = new byte[(maxPix * 3 + printHead.length + 1) * height / 3];
        int k = 0;
        for (int j = 0; j < height / 3; j++) {
            System.arraycopy(printHead, 0, data, k, printHead.length);
            k += printHead.length;
            for (int i = 0; i < width * 8; i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = getPixels(bitMatrix, i, j * 24 + m * 8 + n);//将 8 个0,1 byte 转成一个字节
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
            k = k + (maxPix - width * 8) * 3;// 补充空格
            data[k] = 10;
        }
        return data;
    }

    public byte[] getPixelsByte(String contents, int width, int height) {
        BitMatrix bitMatrix = createPixels(contents, width, height);
        if (bitMatrix == null) {
            return null;
        }

        byte[] bytes = new byte[(32 * 8) * height];
        //下面这里按照二维码的算法，逐个生成二维码的图片，两个for循环是图片横列扫描的结果
        int srcPos = 0;
        for (int i = height - 1; i >= 0; i--) {
            byte[] rowByte = new byte[width * 8];// 384+4
            for (int j = width * 8 - 1; j >= 0; j--) {
                int[] dest = new int[8];
                dest[7] = getPixels(bitMatrix, i * 8, j);
                dest[6] = getPixels(bitMatrix, i * 8 + 1, j);
                dest[5] = getPixels(bitMatrix, i * 8 + 2, j);
                dest[4] = getPixels(bitMatrix, i * 8 + 3, j);
                dest[3] = getPixels(bitMatrix, i * 8 + 4, j);
                dest[2] = getPixels(bitMatrix, i * 8 + 5, j);
                dest[1] = getPixels(bitMatrix, i * 8 + 6, j);
                dest[0] = getPixels(bitMatrix, i * 8 + 7, j);
                rowByte[j] = dealArray(dest);
            }
            System.arraycopy(rowByte, 0, bytes, srcPos, width * 8);
            srcPos += width * 8;
        }
        return bytes;
    }

    /**
     * @param contents 二维码内容，可以是url网址，可以是普通字符串
     */
    public static Bitmap createBitmap(String contents, int width, int height) {
        //判断URL合法性
        if (TextUtils.isEmpty(contents)) {
            return null;
        }
        BitMatrix bitMatrix = createPixels(contents, width, height);
        if (bitMatrix == null) {
            return null;
        }

        int QR_WIDTH = width * 8;  //二维码宽
        int QR_HEIGHT = height * 8;  //二维码高

        int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
        //下面这里按照二维码的算法，逐个生成二维码的图片，
        //两个for循环是图片横列扫描的结果
        for (int y = 0; y < QR_HEIGHT; y++) {
            for (int x = 0; x < QR_WIDTH; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * QR_WIDTH + x] = 0xff000000;
                } else {
                    pixels[y * QR_WIDTH + x] = 0xffffffff;
                }
            }
        }
        //生成二维码图片的格式，使用ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
        return bitmap;

    }

    /**
     * VM    bitMatrix 根据 想，x,y 坐标判定是否有 黑点 ，黑点为1 ，白点为0
     */
    private byte getPixels(BitMatrix bitMatrix, int y, int x) {
        if (bitMatrix.get(x, y)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * VM 字节数据
     * 将 int[] 转成一个byte  如   int[] arr= {-1,-16777216,-16777216,-1,-1,-1,-16777216,-16777216}  转为 01100011  即为99
     * 获得每行 的数据 ，并确定了bytee数组的长度    8个长度的 int 数组 ，一个字节
     * 将一列的8个字节数，转成一个字节
     */
    private byte dealArray(int[] dest) {
        int be = 0;
        int temp;
        for (int j = 0; j < 8; j++) {
            temp = dest[j];
            if (j == 0) {
                be = temp;
            } else {
                be = (be << 1) | temp;
            }
        }
        return (byte) be;
    }

}