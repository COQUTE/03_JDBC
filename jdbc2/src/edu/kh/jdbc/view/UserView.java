package edu.kh.jdbc.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.dto.User;
import edu.kh.jdbc.service.UserService;

// View : 사용자와 직접 상호작용하는 화면(UI)를 담당,
// 입력을 받고 결과를 출력하는 역할
public class UserView {

	// 필드
	private Scanner sc = new Scanner(System.in);
	private UserService service = new UserService();

	// 메서드

	/**
	 * JDBCTemplate 사용 테스트
	 */
	public void test() {

		// 입력된 ID와 일치하는 USER 정보 조회
		System.out.print("ID 입력 : ");
		String input = sc.next();

		// 서비스 호출 후 결과 반환 받기
		User user = service.selectId(input);

		// 결과에 따라 사용자에게 보여줄 응답화면 결정
		if (user == null) {
			System.out.println("없어용");
		} else {
			System.out.println(user);
		}

	}

	/**
	 * User 관리 프로그램 메인 메뉴
	 */
	public void mainMenu() {

		int input = 0;

		do {
			try {

				System.out.println("\n===== User 관리 프로그램 =====\n");
				System.out.println("1. User 등록(INSERT)");
				System.out.println("2. User 전체 조회(SELECT)");
				System.out.println("3. User 중 이름에 검색어가 포함된 회원 조회 (SELECT)");
				System.out.println("4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)");
				System.out.println("5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)");
				System.out.println("6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)");
				System.out.println("7. User 등록(아이디 중복 검사)");
				System.out.println("8. 여러 User 등록하기");
				System.out.println("0. 프로그램 종료");

				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine(); // 버퍼에 남은 개행문자 제거

				System.out.println();
				
				switch (input) {
				case 1: insertUser(); break;
				case 2: selectAll(); break;
				case 3: selectName(); break;
				case 4: selectUser(); break;
				case 5: deleteUser(); break;
				case 6: updateName(); break;
				case 7: insertUser2(); break;
				case 8: multiInsertUser(); break;
				case 0: System.out.println("\n[프로그램 종료]\n"); break;
				default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
				}

				System.out.println("----------------------------------------------------------\n");

			} catch (InputMismatchException e) {
				// Scanner를 이용한 입력 시 자료형이 잘못된 경우
				System.out.println("\n***잘못 입력 하셨습니다***\n");

				input = -1; // 잘못 입력해서 while문 멈추는걸 방지
				sc.nextLine(); // 입력 버퍼에 남아있는 잘못된 문자 제거

			} catch (Exception e) {
				// 발생되는 예외를 모두 해당 catch 구문으로 모아서 처리
				e.printStackTrace();
			}

		} while (input != 0);

	} // mainMenu() 종료

	/**
	 * 1. User 등록 관련 View
	 */
	private void insertUser() throws Exception {
		
		System.out.println("=== 1. User 등록 ===\n");
		
		System.out.print("ID 입력 : ");
		String userId = sc.next();
		
		System.out.print("PW 입력 : ");
		String userPw = sc.next();
		
		System.out.print("NAME : ");
		String userName = sc.next();
		
		// 입력받은 값 3개를 한번에 묶어서 전달할 수 있도록
		// User DTO 객체를 생성한 후 필드에 값을 세팅
//		User user = new User();
//		
		// setter 이용
//		user.setUserId(userId);
//		user.setUserPw(userPw);
//		user.setUserName(userName);
		
		// 서비스 호출(INSERT) 후 결과 반환(int, 결과 행의 개수)받기
//		int result = service.insertUser(user);
		
		// DML(INSERT를 수행하기 때문에 결과가 int형으로 나옴)
		int result = service.insertUser(userId, userPw, userName);
		
		if(result > 0) { // 성공
			System.out.println("User 등록 성공");
		} else { // 실패
			System.out.println("User 등록 실패");
		}
	}	

	/**
	 * 테이블 컬럼 정보들 출력
	 */
	private void display() {
		
		System.out.println("USER_NO | USER_ID | USER_PW | USER_NAME | ENROLL_DATE");
		System.out.println("----------------------------------------------------------");
	}
	
	/**
	 * 2. User 전체 조회 관련 View (SELECT)
	 */
	private void selectAll() throws Exception {
		
		System.out.println("=== 2. User 전체 조회 ===\n");
		
		// 서비스 호출(SELECT) 후 결과(List<User>) 반환받기
		List<User> userList = service.selectAll();
		
		display();
		
		if (userList.isEmpty()) {
			System.out.println("User 테이블이 비어있습니다.");
			return;
		}
		
		// userList에 있는 User 객체 출력
		for(User user : userList) {
			System.out.println(user);
		}
		
	}
	
	/** 
	 * 3. User 중 이름에 검색어가 포함된 회원 조회 (SELECT)
	 */
	private void selectName() throws Exception {
		
		System.out.println("=== 3. User 중 이름에 검색어가 포함된 회원 조회 ===\n");
		
		// 입력된 문자열을 포함하는 이름을 가진 USER 정보 조회
		System.out.print("검색어 입력 : ");
		String keyword = sc.next();
		
		List<User> userList = service.selectName(keyword);
		
		display();
		
		if (userList.isEmpty()) {
			System.out.println("검색 결과가 없습니다.");
			return;
		}
		
		for(User user : userList) {
			System.out.println(user);
		}
		
	}
	
	/**
	 * 4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)
	 */
	private void selectUser() throws Exception {
		
		System.out.println("=== 4. USER_NO를 입력 받아 일치하는 User 조회 ===\n");
		
		System.out.print("사용자 번호 입력 : ");
		int input = sc.nextInt();
		
		// 사용자 번호 == PK == 중복이 있을 수 없다!
		// == 일치하는 사용자가 있다면 딱 1행만 조회된다
		// -> 1행의 조회 결과를 담기 위해서 User DTO 객체 1개 사용
		User user = service.selectUser(input);
		
		display();
		
		if (user == null) {
			System.out.println("해당 USER_NO가 없습니다.");
			return;
		}
		
		System.out.println(user);
		
	}

	/**
	 * 5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)
	 */
	private void deleteUser() throws Exception {
		
		System.out.println("=== 5. USER_NO를 입력 받아 일치하는 User 삭제 ===\n");
		
		System.out.print("삭제할 사용자 번호 입력 : ");
		int input = sc.nextInt();
		
		int result = service.deleteUser(input);
		
		if (result > 0) {
			System.out.println("삭제 성공");
		} else {
			System.out.println("삭제 실패");
		}
	}
	
	/**
	 * 6. ID, PW가 일치하는 회원(SELECT)이 있을 경우 이름 수정(UPDATE)
	 */
	private void updateName() throws Exception {
		
		System.out.println("=== 6. ID, PW가 일치하는 회원이 있을 경우 이름 수정 ===\n");
		
		System.out.print("ID 입력 : ");
		String userId = sc.next();
		
		System.out.print("PW 입력 : ");
		String userPw = sc.next();
		
		// 입력받은 ID, PW가 일치하는 회원이 존재하는지 조회(SELECT)
		// -> USER_NO 조회
		int userNo = service.selectUserNo(userId, userPw);
		
		if(userNo == 0) {
			System.out.println("아이디, 비밀번호가 일치하는 사용자가 없음");
			return;
		}
		
		// 조회 결과 있음
		System.out.print("수정할 이름 입력 : ");
		String userName = sc.next();
		
		// 위에서 조회된 회원(USER_NO)의 이름 수정
		// 서비스 호출(UPDATE) 후 결과 반한(int)받기
		int result = service.updateName(userNo, userName);
		
		if (result > 0) {
			System.out.println("수정 성공");
		} else {
			System.out.println("수정 실패");
		}
	}

	/** 
	 * 7. User 등록(아이디 중복 검사)
	 */
	private void insertUser2() throws Exception {
		
		System.out.println("=== 7. User 등록(아이디 중복 검사) ===\n");
		
		String userId = null; // 입력된 아이디를 저장할 변수
		
		while(true) {
			
			System.out.print("ID 입력 : ");
			userId = sc.next();
			
			// 입력받은 userId가 중복인지 검사하는
			// 서비스(SELECT) 호출 후
			// 결과(int, 중복 == 1, 아니면 == 0)반환 받기
			int count = service.idCheck(userId);
			
			if(count == 0) {
				System.out.println("사용 가능한 아이디입니다.");
				break;
			}
			
			System.out.println("이미 사용중인 아이디입니다. 다시 입력해주세요.");
		}
		
		System.out.print("PW 입력 : ");
		String userPw = sc.next();
		
		System.out.print("NAME 입력 : ");
		String userName = sc.next();
		
		// DML(INSERT를 수행하기 때문에 결과가 int형으로 나옴)
		int result = service.insertUser(userId, userPw, userName);
		
		if(result > 0) { // 성공
			System.out.println("User 등록 성공");
		} else { // 실패
			System.out.println("User 등록 실패");
		}
	}
	
	/**
	 * 8. 여러 User 등록하기
	 */
	private void multiInsertUser() throws Exception {
		
		/* 등록할 User 수 : 2
		 * 
		 * 1번째 userID : user100
		 * -> 사용가능한 ID입니다
		 * 1번째 userPw : pass100
		 * 1번째 userName : 유저백
		 * -----------------------
		 * 2번째 userId : user200
		 * -> 사용가능한 ID입니다
		 * 2번째 userPw : pass200
		 * 2번째 userName : 유저이백
		 * 
		 * -- 전체 삽입 성공 / 삽입 실패
		 * 
		 */
		
		System.out.println("=== 8. 여러 User 등록하기 ===\n");
		
		System.out.print("등록할 User 수 : ");
		int input = sc.nextInt();
		sc.nextLine(); // 버퍼 개행문자 제거
		
		// 입력받은 회원 정보를 저장할 List 객체 생성
		List<User> userList = new ArrayList<User>();
		
		for(int i = 0; i < input; i++) {
			
			String userId = null; // 입력된 아이디를 저장할 변수
			
			while(true) {
				
				System.out.print((i+1) + "번째 ID 입력 : ");
				userId = sc.next();
				
				// 입력받은 userId가 중복인지 검사하는
				// 서비스(SELECT) 호출 후
				// 결과(int, 중복 == 1, 아니면 == 0)반환 받기
				int count = service.idCheck(userId);
				
				if(count == 0) {
					System.out.println("사용 가능한 아이디입니다.");
					break;
				}
				
				System.out.println("이미 사용중인 아이디입니다. 다시 입력해주세요.");
			}
			
			System.out.print((i+1) + "번째 PW 입력 : ");
			String userPw = sc.next();
			
			System.out.print((i+1) + "번째 NAME 입력 : ");
			String userName = sc.next();
			
			System.out.println("---------------------------------");
			
			// 입력받은 값 3개를 한번에 묶어서 전달할 수 있도록
			// User 객체를 생성한 후 userList에 추가
			User user = new User();
			
			user.setUserId(userId);
			user.setUserPw(userPw);
			user.setUserName(userName);
			
			userList.add(user);
		}
		
		// 입력 받은 모든 사용자를 insert 하는 서비스 호출
		// -> 결과로 삽입된 행의 개수 반환
		int result = service.multiInsertUser(userList);
		
		if(result == userList.size()) { // 성공
			System.out.println("전체 삽입 성공");
		} else { // 실패
			System.out.println("삽입 실패");
		}
	}
}
