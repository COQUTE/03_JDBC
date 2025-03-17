package jdbcEx;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Exercise1 {

	// 1. 입력은 최소 하나
	// 2. PreparedStatement 사용
	// 3. commit / rollback 구현
	
	public static void main(String[] args) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			
			String user = "workbook";
			String password = "workbook";
			
			conn = DriverManager.getConnection(url, user, password);
			
			String sql = """
					SELECT STUDENT_NO, STUDENT_NAME
					FROM TB_STUDENT
					JOIN TB_DEPARTMENT USING(DEPARTMENT_NO)
					JOIN TB_GRADE USING(STUDENT_NO)
					WHERE DEPARTMENT_NAME = '국어국문학과'
					GROUP BY STUDENT_NO, STUDENT_NAME
					HAVING AVG(POINT) = (SELECT MAX(AVG(POINT))
										 FROM TB_STUDENT
										 JOIN TB_DEPARTMENT USING(DEPARTMENT_NO)
										 JOIN TB_GRADE USING(STUDENT_NO)
										 WHERE DEPARTMENT_NAME = '국어국문학과'
										 GROUP BY STUDENT_NO, STUDENT_NAME)
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			StringBuilder sb = new StringBuilder();
			
			while(rs.next()) {
				
				sb.append(rs.getString(1));
				sb.append(rs.getString(2));
				sb.append("\n");
			}
			
			String result = sb.toString();
			
			fw = new FileWriter("/io_test/20250305/스터디.txt");
			bw = new BufferedWriter(fw);
			
			bw.write(result);
			
			bw.flush();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
				if(bw != null) fw.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
