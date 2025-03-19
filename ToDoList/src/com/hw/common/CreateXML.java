package com.hw.common;

import java.io.FileOutputStream;
import java.util.Properties;

/**
 * driver.xml 파일을 생성하기 위한 클래스?
 */
public class CreateXML {
	
	public static void main(String[] args) {
	
		// XML을 만들기 위해서 OutputStream과 Properties 필요
		FileOutputStream fos = null;
		Properties prop = null;
		
		try {
			fos = new FileOutputStream("driver.xml");
			prop = new Properties();
			
			prop.storeToXML(fos, null);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			
			try {
				if(fos != null) fos.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
