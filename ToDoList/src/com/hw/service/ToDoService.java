package com.hw.service;

import java.sql.Connection;
import java.util.List;

import static com.hw.common.Template.*;
import com.hw.model.dao.ToDoDAO;
import com.hw.model.dto.Member;
import com.hw.model.dto.Todo;

public class ToDoService {

	ToDoDAO dao = new ToDoDAO();
	
	public int idCheck(String memId) throws Exception {
		
		Connection conn = getConnection();
		
		int result = dao.idCheck(conn, memId);
	
		close(conn);
		
		return result;
	}

	public int insertMem(Member mem) throws Exception {
		
		Connection conn = getConnection();
		
		int result = dao.insertMem(conn, mem);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		
		close(conn);
		
		return result;
	}

	public int getMemCode(String memId, String memPw) throws Exception {

		Connection conn = getConnection();
		
		int memCode = dao.getMemCode(conn, memId, memPw);
		
		close(conn);
		
		return memCode;
	}

	public String getNickname(int loginMemCode) throws Exception {
		
		Connection conn = getConnection();
		
		String nickName = dao.getNickname(conn, loginMemCode);
			
		close(conn);
		
		return nickName;
	}

	public List<Todo> selectTodo(int loginMemCode) throws Exception {
		
		Connection conn = getConnection();
		
		List<Todo> TodoList = dao.selectTodo(conn, loginMemCode);
		
		close(conn);
		
		return TodoList;
	}

	
}
