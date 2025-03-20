package com.hw.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

//코드의 중복 작성과 메모리 효율을 위해 싱글톤 패턴을 활용한 Template을 생성
public class Template {

	// JDBC 활용을 위한 Connection
	public static Connection conn = null;

	// driver.xml / Properties.loadXML() 활용하여 Connection 객체 할당해주는 static 메서드 작성
	public static Connection getConnection() {
		
		try {
			
			if(conn != null && !conn.isClosed()) {
				return conn;
			}
			
			// xml 파일을 읽어오기 위한 Properties 객체 선언 및 할당
			Properties prop = new Properties();
		
			// xml 파일을 읽어와야 하기 때문에 InputStream 생성
			// driver.xml에 InputStream 연결
			// 연결된 InputStream을 통해 Properties 객체에 entry 내용들 읽어들임
			prop.loadFromXML(new FileInputStream("driver.xml"));
			
			// driver 메모리에 로드
			Class.forName(prop.getProperty("driver"));
			
			// DriverManager를 통해서 Connection 객체를 생성
			conn = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("userName"), prop.getProperty("userPw"));
			
			// 트랜잭션 처리 직접하기 위해 autoCommit 끄기
			conn.setAutoCommit(false);

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public static void commit(Connection conn) {
		
		try {
			if(conn != null && !conn.isClosed()) conn.commit();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection conn) {
		
		try {
			if(conn != null && !conn.isClosed()) conn.rollback();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Connection conn) {
		
		try {
			if(conn != null && !conn.isClosed()) conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement stmt) {
		
		try {
			if(stmt != null && !stmt.isClosed()) stmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
