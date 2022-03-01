package com.aliyun.idaas.device.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * Alibaba Cloud IDaaS
 *
 * @author hatterjiang
 */
public class TerminalQrUtil {

    public static String toTerminalQr(String text, int width) throws WriterException {
        final Map<EncodeHintType, Object> qrParam = new HashMap<EncodeHintType, Object>(2);
        qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        qrParam.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        @SuppressWarnings({"UnnecessaryLocalVariable", "SuspiciousNameCombination"})
        final int height = width;
        final BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(text, BarcodeFormat.QR_CODE, width, height, qrParam);

        int from = 1;
        for (int i = 0; i < bitMatrix.getHeight(); i++) {
            if (bitMatrix.get(i, i)) {
                from = i;
                break;
            }
        }

        final StringBuilder terminalOutputBuffer = new StringBuilder();
        for (int x = (from - 1); x < bitMatrix.getHeight() - (from - 1); x++) {
            for (int y = (from - 1); y < bitMatrix.getWidth() - (from - 1); y++) {
                boolean isBlackPoint = bitMatrix.get(x, y);
                terminalOutputBuffer.append(isBlackPoint ? "\033[40m  \033[0m" : "\033[47m  \033[0m");
            }
            terminalOutputBuffer.append("\n");
        }
        return terminalOutputBuffer.toString();
    }
}
