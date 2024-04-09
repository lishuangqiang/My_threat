package com.example.my_theatre.utils;


import cn.hutool.core.lang.UUID;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.example.my_theatre.entity.po.Order;
import com.example.my_theatre.entity.po.User;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.my_theatre.utils.ObsUtils.bucketName;


public class CodeImageUtil {
    //todo(测试删除)
    @Resource
    static AliOssUtil aliOssUtil;
    // 默认二维码宽度
    public static final int WIDTH = 366;
    // 默认二维码高度
    public static final int HEIGHT = 366;
    // 默认二维码文件格式
    public static final String FORMAT = "jpg";
    // 二维码参数
    public static final Map<EncodeHintType, Object> HINTS = new HashMap<EncodeHintType, Object>();
    //初始化编码格式等参数
    static {
        // 字符编码
        HINTS.put(EncodeHintType.CHARACTER_SET, "utf-8");
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 二维码与图片边距
        HINTS.put(EncodeHintType.MARGIN, 2);
    }

    /**
     *
     * @description：功能描述     将二维码写出到输出流中
     * @param content    二维码内容即要存储在二维码中的内容(扫描二维码之后获取的内容)
     * @param stream    输出流
     * @param width    二维码宽
     * @param height    二维码高
     * @throws WriterException
     * @throws IOException
     * @see： 需要参见的其它内容
     */
    public static void writeToStream(Object content, OutputStream stream,
                                     Integer width, Integer height) throws WriterException, IOException {
        if(width==null){
            width=WIDTH;
        }
        if(height==null){
            height=HEIGHT;
        }
        Gson gson = new Gson();
        String json = gson.toJson(content);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(json,
                BarcodeFormat.QR_CODE, width, height, HINTS);
        MatrixToImageWriter.writeToStream(bitMatrix, FORMAT, stream);
    }
}

