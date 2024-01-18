import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGeneratorWithText {

    public static void main(String[] args) {
        String qrCodeData = "Hello, QR Code!";
        String filePath = "/Users/wangyang/Desktop/test/qrcode_with_text.png";
        int size = 300;
        String fileType = "png";
        String additionalText = "Additional Text";

        generateQRCodeWithText(qrCodeData, filePath, size, fileType, additionalText);
        System.out.println("QR Code with text generated successfully.");
    }

    private static void generateQRCodeWithText(String qrCodeData, String filePath, int size, String fileType, String additionalText) {
        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, size, size, hintMap);

            BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            bufferedImage.createGraphics();

            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, size, size);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            // 添加文字
            graphics.setColor(Color.BLACK);
            Font font = new Font("Arial", Font.PLAIN, 12);
            graphics.setFont(font);
            graphics.drawString(additionalText, 10, size - 10);

            ImageIO.write(bufferedImage, fileType, new File(filePath));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}
