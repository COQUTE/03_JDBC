package edu.kh.jdbc.service;

import static edu.kh.jdbc.common.JDBCTemplate.commit;
import static edu.kh.jdbc.common.JDBCTemplate.rollback;

import java.sql.Connection;
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

	public int insertUser(String userId, String userPw, String userName) {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공
		User user = new User(0, userId, userPw, userName, null);
		
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

	public List<User> selectAll() {
		
		// 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// DAO 메서드 호출 결과 반환
		List<User> userList = dao.selectAll(conn);
		
		// 다 쓴 커넥션 자원 반환
		JDBCTemplate.close(conn);
		
		// 결과를 view 리턴
		return userList;
	}

	public List<User> selectName(String input) {
		
		// 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		List<User> userList = dao.selectName(conn, input);
		
		JDBCTemplate.close(conn);
		
		return userList;
	}

	public User selectUser(int input) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		User user = dao.selectUser(conn, input);
		
		JDBCTemplate.close(conn);
		
		return user;
	}

	public int deleteUser(int iuput) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.deleteUser(conn, iuput);
		
		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	public int updateName(String userId, String userPw, String userName) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		User user = new User(0, userId, userPw, userName, null);
		
		int result = dao.updateName(conn, user);
		
		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	
	
	
	
	
}
