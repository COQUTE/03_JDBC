package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample4 {

	public static void main(String[] args) {
		
		// 부서명을 입력받아
		// 해당 부서에 근무하는 사원의
		// 사번, 이름, 부서명, 직급명을
		// 직급코드 오름차순으로 조회
		
		// [실행화면]
		// 부서명 입력 : 총무부
		// 200 / 선동일 / 총무부 / 대표
		// ...
		
		// 부서명 입력 : 개발팀
		// 일치하는 부서가 없습니다!
		
		// hint : SQL에서 문자열은 양쪽 '' (홑따옴표) 필요
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		Scanner sc = null;
		
		try {
			sc = new Scanner(System.in);
			
			System.out.print("부서명 입력 : ");
			String input = sc.nextLine();
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String address = "jdbc:oracle:thin:@localhost:1521:XE";
			
			String user = "kh";
			String pw = "kh1234";
			
			conn = DriverManager.getConnection(address, user, pw);
			
			stmt = conn.createStatement();
			
			String query = """
					SELECT EMP_ID, EMP_NAME, DEPT_TITLE, JOB_NAME
					FROM EMPLOYEE
					JOIN DEPARTMENT ON(DEPT_ID = DEPT_CODE)
					JOIN JOB ON(EMPLOYEE.JOB_CODE = JOB.JOB_CODE)
					WHERE DEPT_TITLE = '
					""" + input + "' ORDER BY JOB.JOB_CODE";
			
			rs = stmt.executeQuery(query);
			
			if(!rs.isBeforeFirst()) {
				System.out.println("일치하는 부서가 없습니다!");
				
				return;
			}
			
			while(rs.next()) {
				
				String empId = rs.getString(1);
				String empName = rs.getString(2);
				String deptTitle = rs.getString(3);
				String jobName = rs.getString(4);
				
				System.out.printf("%s / %s / %s / %s\n", empId, empName, deptTitle, jobName);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			
			try {
				if(conn != null) conn.close();
				if(stmt != null) stmt.close();
				if(rs != null) rs.close();

			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
}
