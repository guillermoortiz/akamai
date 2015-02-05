package com.mycompany.app;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class GeneratorBinaryFile {
	public static void main(String[] args) throws IOException {

		int padding = 10;
		byte[] rowBytes;
		String row;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		FileOutputStream out = new FileOutputStream("c:\\miscosas\\bytesFile");
		for (int i = 0; i < 10; i++) {
			String number = StringUtils.leftPad(String.valueOf(i), 10, '0')
					+ "|" + "hola";
			rowBytes = number.getBytes();
			outputStream.write(rowBytes);
			System.out.println(rowBytes);
		}
		out.write(outputStream.toByteArray());
		out.close();
	}
}
