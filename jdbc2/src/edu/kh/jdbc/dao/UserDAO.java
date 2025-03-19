package edu.kh.jdbc.dao;

// import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와
// 클래스명.메서드명() 이 아닌 메서드명() 만 작성해도 호출 가능하게 함.
import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.dto.User;

// (Model 중 하나)DAO (Data Access Object)
// 데이터가 저장된 곳에 접근하는 용도의 객체
//-> DB에 접근하여 Java에서 원하는 결과를 얻기위해
//   SQL을 수행하고 결과를 반환 받는 역할
public class UserDAO {
	
	// 필드
	// - DB 접근 관련한 JDBC 객체 참조 변수 미리 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	// 메서드
	
	/** 전달받은 Connection을 이용해서 DB에 접근하여
	 * 전달받은 아이디(input)와 일치하는 User 정보를 DB 조회하기
	 * @param conn : Service에서 생성한 Connection 객체
	 * @param input : View에서 입력받은 아이디
	 * @return 아이디가 일치하는 회원의 User 또는 null
	 */
	public User selectId(Connection conn, String input) {
		
		// 1. 결과 저장용 변수 선언
		User user = null;
		
		try {
			
			// 2. SQL 작성
			String sql = "SELECT * FROM TB_USER WHERE USER_ID = ?";
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? (위치홀더) 에 알맞은 값 세팅
			pstmt.setString(1, input);
			
			// 5. SQL 수행 후 결과 반환 받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과가 있을 경우
			// + 중복되는 아이디가 없다고 가정
			// -> 1행만 조회되기 때문에 while문 보다는 if를 사용하는게 효과적
			if(rs.next()) {
				// 첫 행에 데이터가 존재한다면
				
				// 각 컬럼의 값 얻어오기
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				// java.sql.Date
				Date enrollDate = rs.getDate("ENROLL_DATE");
				
				// 조회된 컬럼값을 이용해서 User 객체 생성
				user = new User(userNo, 
						userId, 
						userPw, 
						userName, 
						enrollDate.toString());
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			// 사용한 JDBC 객체 자원 반환(close)
			//JDBCTemplate.close(rs);
			//JDBCTemplate.close(pstmt);
			close(rs);
			close(pstmt);
			
			// Connection 객체는 생성된 Service에서 close!
			
		}
		
		return user; // 결과 반환 (생성된 User 객체 또는 null)
	}

	/** 1. User 등록 DAO
	 * @param conn : DB 연결 정보가 담겨있는 Connection 객체
	 * @param user : 입력받은 id, pw, name이 세팅된 User 객체
	 * @return <strong>result</strong> : INSERT 결과 행의 개수
	 * @throws SQLException
	 */
	public int insertUser(Connection conn, User user) throws Exception {
		
		// SQL 수행 중 발생하는 예외를
		// catch로 처리하지 않고, throws를 이용해서 호출부로 던져 처리
		// -> catch 문 필요없다!
		
		int result = 0;
		
		try {
			// query문 작성
			String query = "INSERT INTO TB_USER VALUES(SEQ_USER_NO.NEXTVAL,?,?,?,DEFAULT)";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/** 2. User 전체 조회 DAO
	 * @param conn
	 * @return <strong>userList</strong> : 조회된 User들이 담긴 List
	 */
	public List<User> selectAll(Connection conn) throws Exception {
		
		List<User> userList = new ArrayList<User>();
		
		try {
			// sql 작성
			String query = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') AS ENROLL_DATE
					FROM TB_USER
					ORDER BY USER_NO
					""";
			
			// Statement 생성
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(query);
			
			if(!rs.isBeforeFirst()) {
				System.out.println("조회 결과 없음");
				
				return userList;
			}
			
			while(rs.next()) {
				userList.add(new User(rs.getInt("USER_NO"), rs.getString("USER_ID"), rs.getString("USER_PW"), rs.getString("USER_NAME"), rs.getString("ENROLL_DATE")));
			}
			
		} finally {
			close(rs);
			close(stmt);
		}
		
		return userList;
	}

	/** 3. 이름에 검색어가 포함되는 회원 모두 조회 DAO
	 * @param conn
	 * @param keyword
	 * @return <strong>searchList</strong>
	 * @throws Exception
	 */
	public List<User> selectName(Connection conn, String keyword) throws Exception {
		
		List<User> userList = new ArrayList<User>();
		
		try {
			String query = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') AS ENROLL_DATE
					FROM TB_USER WHERE USER_NAME LIKE '%' || ? || '%'
					ORDER BY USER_NO
					""";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, keyword);
			
			rs = pstmt.executeQuery();
			
			if(!rs.isBeforeFirst()) {
				return userList;
			}
			
			while(rs.next()) {
				userList.add(new User(rs.getInt("USER_NO"), rs.getString("USER_ID"), rs.getString("USER_PW"), 
									  rs.getString("USER_NAME"), rs.getString("ENROLL_DATE")));
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return userList;
	}

	/** 4. USER_NO를 입력받아 일치하는 User 조회 DAO
	 * @param conn
	 * @param input
	 * @return user 객체 or null
	 * @throws Exception
	 */
	public User selectUser(Connection conn, int input) throws Exception {
		
		User user = null;
		
		try {
			String query = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') AS ENROLL_DATE
					FROM TB_USER
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, input);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return user;
	}

	/** USER_NO를 입력받아 일치하는 User 삭제 DAO
	 * @param conn
	 * @param input
	 * @return <strong>result</strong>
	 */
	public int deleteUser(Connection conn, int input) throws Exception {
		
		int result = 0;
		
		try {
		
		String query = "DELETE FROM TB_USER WHERE USER_NO = ?";
		
		pstmt = conn.prepareStatement(query);
		
		pstmt.setInt(1, input);
		
		result = pstmt.executeUpdate();
		
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/** ID, PW가 일치하는 회원의 USER_NO 조회 DAO
	 * @param conn
	 * @param userId
	 * @param <strong>userPw</strong>
	 * @return
	 */
	public int selectUser(Connection conn, String userId, String userPw) throws Exception {
		
		int userNo = 0;
		
		try {
			String query = """
					SELECT USER_NO
					FROM TB_USER
					WHERE USER_ID = ?
					AND USER_PW = ?
					""";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, userId);
			pstmt.setString(2, userPw);
			
			rs = pstmt.executeQuery();
			
			// 조회된 1행이 있을 경우
			if(rs.next()) {
				userNo = rs.getInt("USER_NO");
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return userNo; // 조회 성공 USER_NO, 실패 0 반환
	}
	
	/** userNo가 일치하는 회원의 이름 수정 DAO
	 * @param conn
	 * @param userNo
	 * @param userName
	 * @return result
	 * @throws Exception
	 */
	public int updateName(Connection conn, int userNo, String userName) throws Exception {
		
		int result = 0;
		
		try {
			String query = "UPDATE TB_USER SET USER_NAME = ? WHERE USER_No = ?";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, userName);
			pstmt.setInt(2, userNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}		
		
		return result;
	}

	/** 아이디 중복 확인 DAO
	 * @param conn
	 * @param userId
	 * @return count
	 */
	public int idCheck(Connection conn, String userId) throws Exception {
		
		int count = 0;
		
		try {
			String query = """
					SELECT COUNT(*)
					FROM TB_USER
					WHERE USER_ID = ?
					""";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt(1); // 조회된 컬럼 순서번호를 이용해
									  // 컬럼값 얻어오기
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return count;
	}

}
