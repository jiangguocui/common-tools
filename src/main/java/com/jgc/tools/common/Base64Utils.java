package com.jgc.tools.common;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Base64Utils {

	public static String encodeStr(byte[] plainByte) {
		Base64 base64 = new Base64();
		plainByte = base64.encode(plainByte);
		return new String(plainByte);
	}

	public static String decodeStr(String encodeStr) {
		byte[] b = encodeStr.getBytes();
		Base64 base64 = new Base64();
		b = base64.decode(b);
		return new String(b);
	}

	public static String encodeImageToBase64(BufferedImage image, String type) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ImageIO.write(image, type, outputStream);

		return Base64Utils.encodeStr(outputStream.toByteArray());

	}

	public static void decodeBase64ToImage(String base64, String path, String imgName) {
		try {
			FileOutputStream write = new FileOutputStream(new File(path + imgName));
			String decoderStr = Base64Utils.decodeStr(base64);
			write.write(decoderStr.getBytes());
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
