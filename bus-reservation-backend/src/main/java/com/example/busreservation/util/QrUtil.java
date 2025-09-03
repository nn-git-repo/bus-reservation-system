package com.example.busreservation.util;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Component
public class QrUtil {
    public byte[] png(String text) throws Exception {
        BitMatrix m = new MultiFormatWriter().encode(
                new String(text.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8),
                BarcodeFormat.QR_CODE, 260, 260);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(m, "PNG", out);
        return out.toByteArray();
    }
}
