package com.sparta.kidscafe.common.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class TestReceiptImageGenerator {

    public static void main(String[] args) {
        // 이미지 저장 경로 설정
        String outputDirectory = "./test_receipts/";
        File dir = new File(outputDirectory);

        // 디렉토리가 없으면 생성
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                System.out.println("디렉토리 생성: " + dir.getAbsolutePath());
            } else {
                System.out.println("디렉토리 생성 실패");
                return;
            }
        }

        try {
            // 10개의 테스트 이미지 생성
            for (int i = 1; i <= 10; i++) {
                int width = 400;  // 이미지 너비
                int height = 200; // 이미지 높이
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                // 배경 색상 설정
                Graphics2D g2d = image.createGraphics();
                g2d.setColor(Color.WHITE); // 흰색 배경
                g2d.fillRect(0, 0, width, height);

                // 텍스트 추가
                g2d.setColor(Color.BLACK); // 검은색 텍스트
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                g2d.drawString("Test Receipt #" + i, 50, 80);
                g2d.drawString("Store: Kids Cafe", 50, 110);
                g2d.drawString("Total: $" + (10 * i), 50, 140);

                g2d.dispose();

                // 파일 저장
                File outputFile = new File(outputDirectory + "test_receipt_" + i + ".jpg");
                ImageIO.write(image, "jpg", outputFile);
                System.out.println("Created: " + outputFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
