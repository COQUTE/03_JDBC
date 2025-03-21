package com.hw.view;

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
	
	public void display() {
	
		int menuNum = 0;
		
		do {
			try {
				showMenu();
				menuNum = sc.nextInt();
				sc.nextLine(); // 버퍼 비워주기
				
				System.out.println(); // 공백 추가
				
				switch(menuNum) {
				
				case 1: createMember(); break;
				case 2: login(); break;
				case 3: selectAll(); break;
				case 4: insertTodo(); break;
				case 5: insertTodo2(); break;
				case 6: updateTodo(); break;
				case 7: updateHasDone(); break;
				case 8: deleteTodo(); break;
				case 9: logout(); break;
				case 0: System.out.println("시스템 종료..."); break;
				default: System.out.println("메뉴 내에서 선택해주세요.");
				}
				
			} catch (InputMismatchException e) {
				System.out.println("잘못된 입력입니다.");
				sc.nextLine(); // 잘못 입력된 버퍼 비워주기
				menuNum = -1;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} while(menuNum != 0);
		
	}

	private void showMenu() {
		
		System.out.println();
		System.out.println("---------ToDoList---------");
		System.out.println("1. 회원가입");
		System.out.println("2. 로그인");
		System.out.println("3. 내 Todo 전체 조회");
		System.out.println("4. Todo 추가");
		System.out.println("5. 커스텀 Todo 추가");
		System.out.println("6. Todo 수정");
		System.out.println("7. 완료 여부 변경");
		System.out.println("8. Todo 삭제");
		System.out.println("9. 로그아웃");
		System.out.println("0. 종료");
		System.out.println("--------------------------");
		
		if(isLogin())
			System.out.println("<현재 사용자: " + curNickname + ">");
		
		System.out.print("메뉴 번호 입력: ");
	}
	
	/**
	 * 로그인 여부를 확인
	 * 
	 * @return 로그인 여부
	 */
	private boolean isLogin() {
		
		if(loginMemCode != 0)
			return true;
		else
			return false;
	}
	
	/**
	 * 1. 회원가입
	 * <h3>새로운 Member 정보를 입력받고 Member INSERT(+ COMMIT/ROLLBACK)</h3>
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
		
		while(true) {
		
			// 아이디 입력
			System.out.print("아이디: ");
			memId = sc.next();
			
			if(memId.length() <= 5) { // NOT NULL로 설정해놨기 때문, 최소 길이 지정
				System.out.println("다섯 글자 이상 입력하세요.");
				continue;
				
			} else if(memId.length() > 10) { // NVARCHAR2(10)으로 설정해놨기 때문
				System.out.println("아이디는 최대 10글자입니다.");
				continue;
			}
			
			// service 호출해서 입력한 아이디 중복 확인
			// 중복은 1, 중복이 아니면 0
			int count = service.idCheck(memId);
			
			if(count == 0)
				break;
			
			System.out.println("이미 존재하는 아이디입니다.");
		}
		
		// 중복이 안 될 경우, 비밀번호, 닉네임 입력
		System.out.print("비밀번호: ");
		String memPw = sc.next();
		
		System.out.print("닉네임: ");
		String nickname = sc.next();
		
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
	
	/**
	 * 2. 로그인
	 * <h3>아이디, 비밀번호 입력받고 기존 Member SELECT</h3>
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
		
		System.out.print("아이디: ");
		String memId = sc.next();
		
		System.out.print("비밀번호: ");
		String memPw = sc.next();
		
		loginMemCode = service.getMemCode(memId, memPw);
		
		if(isLogin()) {
			curNickname = service.getNickname(loginMemCode);
			
			System.out.println("로그인 성공!");
		}
		else
			System.out.println("로그인 실패..");
		
	}
	
	/**
	 * 3. 내 Todo 전체 조회
	 * <h3>현재 로그인 된 회원코드를 가지고 Todo 전체 SELECT</h3>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>현재 Member의 모든 Todo 출력</li>
	 * </ol>
	 */
	private void selectAll() throws Exception {
		
		System.out.println("------------------------------------------------------------------------");
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
		
		for(Todo todo : TodoList) {
			System.out.println(todo);
		}
		System.out.println("------------------------------------------------------------------------");
	}
	
	/**
	 * 4. Todo 추가
	 * <h3>현재 로그인 된 회원코드를 가지고 Todo INSERT(+ COMMIT/ROLLBACK)</h3>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>현재 회원의 Todo - default 값으로 추가</li>
	 * </ol>
	 */
	private void insertTodo() throws Exception {

		System.out.println("[Todo 추가]");
		
		if(!isLogin()) {
			System.out.println("로그인 후 다시 시도해주세요.");
			return;
		}
		
		int result = service.insertTodo(loginMemCode, "제목");
		
		if(result > 0) {
			System.out.println("Todo 추가 성공!");
		} else {
			System.out.println("Todo 추가 실패..");
		}
	}
	
	private String inputTodoTitle() {
		
		String todoTitle = null;
		
		while(true) {
			System.out.print("제목 입력: ");
			todoTitle = sc.nextLine();
			
			if(todoTitle.length() > 30) {
				System.out.println("제목은 최대 30자입니다.");
				
			} else if(todoTitle.length() == 0) {
				System.out.print("제목을 기본값으로 지정하시겠습니까?(Y/N): ");
				String yn = sc.next().toUpperCase();
				
				if(yn.equals("Y")) {
					todoTitle = "제목";
					break;
					
				} else if(yn.equals("N")) {
					System.out.println("제목을 다시 입력해주세요.");
					sc.nextLine(); // 버퍼 비워주기
					
				} else {
					throw new InputMismatchException();
				}
				
			} else {
				break;
			}
		}
		
		return todoTitle;
	}
	
	/**
	 * 5. Todo 커스텀 추가
	 * <h3>현재 로그인 된 회원코드를 가지고 Todo INSERT(+ COMMIT/ROLLBACK)</h3>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>현재 회원의 Todo - 입력한 값으로 추가</li>
	 * </ol>
	 */
	private void insertTodo2() throws Exception {

		System.out.println("[Todo 커스텀 추가]");
		
		if(!isLogin()) {
			System.out.println("로그인 후 다시 시도해주세요.");
			return;
		}
		
		String todoTitle = inputTodoTitle();
		
		int result = service.insertTodo(loginMemCode, todoTitle);
		
		if(result > 0) {
			System.out.println("Todo 추가 성공!");
		} else {
			System.out.println("Todo 추가 실패..");
		}
		
	}
	
	/**
	 * 6. Todo 수정
	 * <h3>현재 로그인 된 회원코드를 가지고 Todo UPDATE(+ COMMIT/ROLLBACK)</h3>
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
		
		String todoTitle = inputTodoTitle();
		
		Todo todo = new Todo(loginMemCode, todoNo, todoTitle);
		
		int result = service.updateTodo(todo);
		
		if(result > 0) {
			System.out.println("Todo 수정 성공!");
		} else {
			System.out.println("Todo 수정 실패..");
		}
	}
	
	/**
	 * 7. 완료여부 변경
	 * <h3>현재 로그인 된 회원코드를 가지고 HAS_DONE UPDATE(+ COMMIT/ROLLBACK)</h3>
	 * <ol>
	 * 	<li>현재 로그인 되어 있는지 확인</li>
	 * 	<li>TODO_NO를 입력하면 해당 Todo의 완료 여부를 변경</li>
	 * </ol>
	 */
	private void updateHasDone() throws Exception {

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
		
		System.out.print("완료여부 입력: ");
		char hasDone = sc.next().toUpperCase().charAt(0);
		
		if(hasDone != 'Y' && hasDone != 'N') {
			System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
			return;
		}
		
		Todo todo = new Todo(loginMemCode, todoNo, hasDone);
		
		int result = service.updateHasDone(todo);
		
		if(result > 0) {
			System.out.println("완료여부 변경 완료!");
		} else {
			System.out.println("완료여부 변경 실패..");
		}
	}
	
	/**
	 * 8. Todo 삭제
	 * <h3>현재 로그인 된 회원코드를 가지고 Todo DELETE(+ COMMIT/ROLLBACK)</h3>
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
	
	/**
	 * 9. 로그아웃
	 * <h3></h3>
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
