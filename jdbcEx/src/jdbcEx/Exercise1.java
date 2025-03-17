package jdbcEx;

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
			
			while(rs.next()) {
				
				System.out.printf("%s, %s\n", rs.getString(1), rs.getString(2));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
