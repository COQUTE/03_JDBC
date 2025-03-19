package edu.kh.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dao.UserDAO;
import edu.kh.jdbc.dto.User;

// (Model 중 하나)Service : 비즈니스 로직을 처리하는 계층,
// 데이터를 가공하고 트랜잭션(commit, rollback) 관리 수행
public class UserService {
	
	// 필드
	private UserDAO dao = new UserDAO();

	// 메서드
	
	/** 전달받은 아이디와 일치하는 User 정보 반환 서비스
	 * @param input (입력된 아이디)
	 * @return 아이디가 일치하는 회원 정보가 담긴 User 객체,
	 * 			없으면 null 반환
	 */
	public User selectId(String input) {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공(할게 없으면 넘어감)
		
		// 3. DAO 메서드 호출 결과 반환
		User user = dao.selectId(conn, input);
		
		// 4. DML(commit/rollback)
		
		// 5. 다 쓴 커넥션 자원 반환
		JDBCTemplate.close(conn);
		
		// 6. 결과를 view 리턴
		return user;
	}

	/**
	 * @param userId : 입력된 아이디
	 * @param userPw : 입력된 비밀번호
	 * @param userName : 입력된 이름
	 * @return <strong>result</strong> : INSERT 결과 행의 개수
	 * @throws SQLException
	 */
	public int insertUser(String userId, String userPw, String userName) throws Exception {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공
		User user = new User();
		
		user.setUserId(userId);
		user.setUserPw(userPw);
		user.setUserName(userName);
		
		// 3. DAO 메서드 호출 결과 반환
		int result = dao.insertUser(conn, user);
		
		// 4. DML(commit/rollback)
		if(result > 0) { // INSERT 성공
			JDBCTemplate.commit(conn);
			
		} else { // INSERT 실패
			JDBCTemplate.rollback(conn);
			
		}
		
		// 5. 다 쓴 커넥션 자원 반환
		JDBCTemplate.close(conn);
		
		// 6. 결과를 view 리턴
		return result;
	}

	/** 2. User 전체 조회 서비스
	 * @return <strong>userList</strong> : 조회된 User들이 담긴 List
	 */
	public List<User> selectAll() throws Exception {
		
		// 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// DAO 메서드 호출(SELECT) 후 결과 반환(List<User>) 받기
		List<User> userList = dao.selectAll(conn);
		
		// 다 쓴 커넥션 자원 반환
		JDBCTemplate.close(conn);
		
		// 결과를 view 리턴
		return userList;
	}

	/** 3. User 중 이름에 검색어가 포함된 회원 조회 서비스
	 * @param keyword : 입력한 키워드
	 * @return <strong>searchList</strong> : 조회된 회원 리스트
	 * @throws Exception
	 */
	public List<User> selectName(String keyword) throws Exception {
		
		// 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		List<User> userList = dao.selectName(conn, keyword);
		
		JDBCTemplate.close(conn);
		
		return userList;
	}

	/** 4. USER_NO를 입력받아 일치하는 User 조회 서비스
	 * @param input
	 * @return <strong>user</strong> : 조회된 회원 정보 또는 null
	 * @throws Exception
	 */
	public User selectUser(int input) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		User user = dao.selectUser(conn, input);
		
		JDBCTemplate.close(conn);
		
		return user;
	}

	/** USER_NO를 입력받아 일치하는 User 삭제 서비스
	 * @param iuput
	 * @return <strong>result</strong>
	 */
	public int deleteUser(int iuput) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.deleteUser(conn, iuput);
		
		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	/** ID, PW가 일치하는 회원의 USER_NO 조회 서비스
	 * @param userId
	 * @param userPw
	 * @return <strong>userNo</strong>
	 */
	public int selectUserNo(String userId, String userPw) throws Exception {

		Connection conn = JDBCTemplate.getConnection();
		
		int userNo = dao.selectUser(conn, userId, userPw);
		
		JDBCTemplate.close(conn);
		
		return userNo;
	}
	
	/** userNo가 일치하는  회원의 이름 수정 서비스
	 * @param userNo
	 * @param userName
	 * @return <strong>result</strong>
	 */
	public int updateName(int userNo, String userName) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.updateName(conn, userNo, userName);
		
		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	/** 아이디 중복 확인 서비스
	 * @param userId
	 * @return count
	 */
	public int idCheck(String userId) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int count = dao.idCheck(conn, userId);
		
		JDBCTemplate.close(conn);
		
		return count;
	}

	/** userList에 있는 모든 User 객체를 INSERT 서비스
	 * @param userList
	 * @return result
	 */
	public int multiInsertUser(List<User> userList) throws Exception {
		
		// 다중 INSERT 방법
		// 1) SQL을 이용한 다중 INSERT (PL/SQL -> BEGIN - END)
		// 2) Java 반복문을 이용한 다중 INSERT (이거 사용!)
		Connection conn = JDBCTemplate.getConnection();
		
		int count = 0; // 삽입 성공한 행의 개수 count
		
		// 1행씩 삽입
		for(User user : userList) {
			
			int result = dao.insertUser(conn, user);
			
			count += result;
		}
		
		// 트랜잭션 제어 처리
		// 전체 삽입 성공 시 commit / 아니면 rollback(일부 삽입, 전체 실패)
		if(count == userList.size()) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return count;
	}
	
}
