package com.hw.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.hw.model.dto.Member;
import com.hw.model.dto.Todo;
import com.hw.service.ToDoService;

public class ToDoView {

	private Scanner sc;
	private ToDoService service;
	
	// 현재 로그인 된 멤버의 코드
	private int loginMemCode;
	// 현재 로그인 된 멤버의 닉네임
	private String curNickname;
	
	public ToDoView() {
		sc = new Scanner(System.in);
		service = new ToDoService();
		
		loginMemCode = 0;
	}
	
	/** <h3>Display TodoList</h3>
	 * <ol>
	 * <li>메뉴 출력</li>
	 * <li>메뉴 선택</li>
	 * <li>선택된 메서드 호출</li>
	 * </ol>
	 */
	public void display() {
	
		int menuNum = 0;
		
		do {
			try {
				showMenu();
				
				menuNum = sc.nextInt();
				sc.nextLine(); // 버퍼 비워주기
				
				selectMenu(menuNum);
				
			} catch (InputMismatchException e) {
				System.out.println("잘못된 입력입니다.");
				sc.nextLine(); // 잘못 입력된 버퍼 비워주기
				menuNum = -1;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} while(menuNum != 0);
		
	}
	
	/**
	 * <h3>로그인 여부에 따라 메뉴 출력</h3>
	 */
	private void showMenu() {
		
		List<String> menuList = new ArrayList<String>();
		
		if(!isLogin()) {
			menuList.add("회원가입");
			menuList.add("로그인");
			
		} else {
			menuList.add("내 Todo 전체 조회");
			menuList.add("기본 Todo 추가");
			menuList.add("커스텀 Todo 추가");
			menuList.add("Todo 수정");
			menuList.add("완료 여부 변경");
			menuList.add("Todo 삭제");
			menuList.add("로그아웃");
		}
		
		System.out.println("\n---------TodoList---------");
		
		int num = 1;
		for(String menu : menuList) {
			System.out.println(num++ + ". " + menu);
		}
		System.out.print("0. 종료");
		
		System.out.println("\n--------------------------");
		
		if(isLogin()) {
			System.out.println("<현재 사용자: " + curNickname + ">");
		}
		System.out.print("메뉴 번호 입력: ");
	}
	
	/** <h3>로그인 여부에 따라 메뉴 출력</h3>
	 * @param menuNum : 입력된 메뉴 번호
	 * @throws Exception
	 */
	private void selectMenu(int menuNum) throws Exception {
		
		System.out.println();
		
		if(!isLogin()) {
			switch(menuNum) {
			case 1: createMember(); break;
			case 2: login();		break;
			case 0: System.out.println("시스템 종료..."); break;
			default: System.out.println("메뉴 내에서 선택해주세요.");
			}
			
		} else {
			switch(menuNum) {
			case 1: selectAll();		break;
			case 2: insertTodo();		break;
			case 3: insertCustomTodo(); break;
			case 4: updateTodo();		break;
			case 5: updateCompleteYN(); break;
			case 6: deleteTodo();		break;
			case 7: logout();			break;
			case 0: System.out.println("시스템 종료..."); break;
			default: System.out.println("메뉴 내에서 선택해주세요.");
			}
		}
	}
	
	/**
	 * <h3>로그인 여부를 확인</h3>
	 * @return 로그인 여부(true/false)
	 */
	private boolean isLogin() {
		
		if(loginMemCode != 0)
			return true;
		else
			return false;
	}
	
	/** 
	 * @param type : 범위가 지정되어 있는 컬럼(ID/PW/NICKNAME)
	 * @param str : 입력된 문자열
	 * @return DB 컬럼별 범위 포함 여부(true/false)
	 */
	private boolean validLength(String type, String str) {
		
		final int PW_MIN_LENGTH = 4;
		final int MAX_LENGTH = 20;
		final int NICK_MAX_LENGTH = 10;
		
		if(str.length() > MAX_LENGTH) {
			System.out.println(type.toUpperCase() + "은/는 최대 20자입니다.");
			return false;
		}
		
		switch(type.toUpperCase()) {
		
		case "PW":
			if(str.length() < PW_MIN_LENGTH) {
				System.out.println(type.toUpperCase() + "는 최소 " + PW_MIN_LENGTH + "자 이상 작성해주세요");
				return false;
			}
		break;
		
		case "NICKNAME":
			if(str.length() > NICK_MAX_LENGTH) {
				System.out.println(type.toUpperCase() + "은 최대 " + NICK_MAX_LENGTH +"자입니다.");
				return false;
			}
		}
		
		return true;
	}
	
	/** <h3>회원가입</h3>
	 * <b>새로운 Member 정보를 입력받고 Member INSERT(+ COMMIT/ROLLBACK)</b>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>아이디 생략, 중복 확인</li>
	 * 	<li>닉네임까지 같이 설정</li>
	 * </ol>
	 */
	private void createMember() throws Exception {
		
		System.out.println("[회원가입]");
		
		// 로그인 확인
		if(isLogin()) {
			System.out.println("이미 로그인 되어 있습니다.");
			return;
		}
		
		String memId = null;
		do {
			// 아이디 입력
			System.out.print("ID: ");
			memId = sc.next();
			
			if(!validLength("id", memId))
				continue;
			
			// service 호출해서 입력한 아이디 중복 확인
			// 중복은 1, 중복이 아니면 0
			int count = service.idCheck(memId);
			
			if(count == 0)
				break;
			
			System.out.println("이미 존재하는 ID입니다.");
			
		} while(true);
		
		// 중복이 안 될 경우, 비밀번호, 닉네임 입력

		String memPw = null;
		do {
			System.out.print("PW: ");
			memPw = sc.next();
			
			if(validLength("pw", memPw))
				break;
			
		} while(true);
		
		String nickname = null;
		do {
			System.out.print("NICKNAME: ");
			nickname = sc.next();
			
			if(validLength("nickname", nickname))
				break;
			
		} while(true);
		
		// Member 객체로 압축
		Member mem = new Member(memId, memPw, nickname);
		
		// service 호출해서 모든 정보 DB에 저장
		int result = service.insertMem(mem);
		
		if(result > 0) { // 성공
			System.out.println("회원가입에 성공했습니다.");
		} else { // 실패
			System.out.println("회원가입에 실패했습니다.");
		}
		
	}
	
	/** <h3>로그인</h3>
	 * <b>아이디, 비밀번호 입력받고 기존 Member SELECT</b>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>로그인 성공시 닉네임 출력</li>
	 * </ol>
	 */
	private void login() throws Exception {
		
		System.out.println("[로그인]");
		
		// 로그인 확인
		if(isLogin()) {
			System.out.println("이미 로그인 되어 있습니다.");
			return;
		}
		
		System.out.print("ID: ");
		String memId = sc.next();
		
		System.out.print("PW: ");
		String memPw = sc.next();
		
		loginMemCode = service.getMemCode(memId, memPw);
		
		if(isLogin()) {
			curNickname = service.getNickname(loginMemCode);
			
			System.out.println("로그인 성공!");
		}
		else
			System.out.println("로그인 실패..");
		
	}
	
	/** <h3>내 Todo 전체 조회</h3>
	 * <b>현재 로그인 된 회원코드를 가지고 Todo 전체 SELECT</b>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>현재 Member의 모든 Todo 출력</li>
	 * </ol>
	 */
	private void selectAll() throws Exception {
		
		System.out.println("-------------------");
		System.out.println("[내 Todo 전체 조회]");
		
		if(!isLogin()) {
			System.out.println("로그인 후 다시 시도해주세요.");
			return;
		}
		
		List<Todo> TodoList = service.selectTodo(loginMemCode);
		
		if(TodoList.isEmpty()) {
			System.out.println("조회 결과가 없습니다.");
			return;
		}
		
		System.out.println("-------------------");
		
		for(Todo todo : TodoList) {
			System.out.println(todo);
		}
	
		System.out.println("-------------------");
	}
	
	/** <h3>기본 Todo 추가</h3>
	 * <b>현재 로그인 된 회원코드를 가지고 Todo INSERT(+ COMMIT/ROLLBACK)</b>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>현재 회원의 Todo - default 값으로 추가</li>
	 * </ol>
	 */
	private void insertTodo() throws Exception {

		System.out.println("[기본 Todo 추가]");
		
		if(!isLogin()) {
			System.out.println("로그인 후 다시 시도해주세요.");
			return;
		}
		
		String todoTitle = "제목";
		String todoContent = "내용";
		
		Todo todo = new Todo(loginMemCode, todoTitle, todoContent);
		
		int result = service.insertTodo(todo);
		
		if(result > 0) {
			System.out.println("기본 Todo 추가 성공!");
		} else {
			System.out.println("기본 Todo 추가 실패..");
		}
	}
	
	/** <h3>Todo 커스텀 추가</h3>
	 * <b>현재 로그인 된 회원코드를 가지고 Todo INSERT(+ COMMIT/ROLLBACK)</b>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>현재 회원의 Todo - 입력한 값으로 추가</li>
	 * </ol>
	 */
	private void insertCustomTodo() throws Exception {

		System.out.println("[Todo 커스텀 추가]");
		
		if(!isLogin()) {
			System.out.println("로그인 후 다시 시도해주세요.");
			return;
		}
		
		String todoTitle = null;
		do {
			System.out.print("제목 입력: ");
			todoTitle = sc.nextLine();
			
			if(validLength("title", todoTitle))
				break;
			
		} while(true);
		
		System.out.print("내용 입력: ");
		String todoContent = sc.nextLine();
		
		Todo todo = new Todo(loginMemCode, todoTitle, todoContent);
		
		int result = service.insertTodo(todo);
		
		if(result > 0) {
			System.out.println("커스텀 Todo 추가 성공!");
		} else {
			System.out.println("커스텀 Todo 추가 실패..");
		}
		
	}
	
	/** <h3>Todo 수정</h3>
	 * <b>현재 로그인 된 회원코드를 가지고 Todo UPDATE(+ COMMIT/ROLLBACK)</b>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>원하는 만큼 계속 제목, 내용 수정 가능</li>
	 * </ol>
	 */
	private void updateTodo() throws Exception {

		System.out.println("[Todo 수정]");
		
		if(!isLogin()) {
			System.out.println("로그인 후 다시 시도해주세요.");
			return;
		}
		
		selectAll();
		
		System.out.print("Todo 번호 입력: ");
		int todoNo = sc.nextInt();
		sc.nextLine(); // 버퍼 비워주기
		
		// 해당 유저에 해당 todoNo이 있는지 확인
		int count = service.todoNoCheck(todoNo);
		
		if(count == 0) {
			System.out.println("해당 Todo가 없습니다. 다시 시도해주세요.");
			return;
		}
		
		String todoTitle = null;
		do {
			System.out.print("제목 입력: ");
			todoTitle = sc.nextLine();
			
			if(validLength("title", todoTitle))
				break;
			
		} while(true);
		
		System.out.print("내용 입력: ");
		String todoContent = sc.nextLine();
		
		Todo todo = new Todo(loginMemCode, todoNo, todoTitle, todoContent);
		
		int result = service.updateTodo(todo);
		
		if(result > 0) {
			System.out.println("Todo 수정 성공!");
		} else {
			System.out.println("Todo 수정 실패..");
		}
	}
	
	/** <h3>완료여부 변경</h3>
	 * <b>현재 로그인 된 회원코드를 가지고 HAS_DONE UPDATE(+ COMMIT/ROLLBACK)</b>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>TODO_NO를 입력하면 해당 Todo의 완료 여부를 변경</li>
	 * </ol>
	 */
	private void updateCompleteYN() throws Exception {

		System.out.println("[완료여부 변경]");
		
		if(!isLogin()) {
			System.out.println("로그인 후 다시 시도해주세요.");
			return;
		}
		
		selectAll();
		
		System.out.print("Todo 번호 입력: ");
		int todoNo = sc.nextInt();
		sc.nextLine(); // 버퍼 제거
		
		// 해당 유저에 해당 todoNo이 있는지 확인
		int count = service.todoNoCheck(todoNo);
		
		if(count == 0) {
			System.out.println("해당 Todo가 없습니다. 다시 시도해주세요.");
			return;
		}
		
		Todo todo = new Todo(loginMemCode, todoNo);
		
		int result = service.updateCompleteYN(todo);
		
		if(result > 0) {
			System.out.println("완료여부 변경 완료!");
		} else {
			System.out.println("완료여부 변경 실패..");
		}
	}
	
	/** <h3>Todo 삭제</h3>
	 * <b>현재 로그인 된 회원코드를 가지고 Todo DELETE(+ COMMIT/ROLLBACK)</b>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>TODO_NO를 입력하면 해당 Todo 삭제</li>
	 * </ol>
	 */
	private void deleteTodo() throws Exception {

		System.out.println("[Todo 삭제]");
		
		if(!isLogin()) {
			System.out.println("로그인 후 다시 시도해주세요.");
			return;
		}

		selectAll();
		
		System.out.print("Todo 번호 입력: ");
		int todoNo = sc.nextInt();
		sc.nextLine(); // 버퍼 비워주기
		
		// 해당 유저에 해당 todoNo이 있는지 확인
		int count = service.todoNoCheck(todoNo);
		
		if(count == 0) {
			System.out.println("해당 Todo가 없습니다. 다시 시도해주세요.");
			return;
		}
		
		int result = service.deleteTodo(loginMemCode, todoNo);
		
		if(result > 0) {
			System.out.println("Todo 삭제 성공!");
		} else {
			System.out.println("Todo 삭제 실패..");
		}
	}
	
	/** <h3>로그아웃</h3>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>로그인 된 경우 로그아웃</li>
	 * </ol>
	 */
	private void logout() {
		
		System.out.println("[로그아웃]");
		if(isLogin()) {
			loginMemCode = 0;
			curNickname = null;
			System.out.println("로그아웃 성공!");
		} else {
			System.out.println("로그인 되어 있지 않습니다.");
		}
	}
}
