package com.hw.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.hw.common.Template.*;
import com.hw.model.dto.Member;
import com.hw.model.dto.Todo;

public class ToDoDAO {

	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private Properties prop;
	
	public ToDoDAO() {
		prop = getQueryProperties();
	}
	
	public int idCheck(Connection conn, String memId) throws Exception {
		
		int count = 0;
		
		try {
			String query = prop.getProperty("CountMember");
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, memId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				count = rs.getInt(1);
				
		} finally {
			close(rs);
			close(pstmt);
		}
			
		
		return count;
	}

	public int insertMem(Connection conn, Member mem) throws Exception {

		int result = 0;
		
		try {
			String query = prop.getProperty("insertMember");
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, mem.getMemberId());
			pstmt.setString(2, mem.getMemberPw());
			pstmt.setString(3, mem.getNickname());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int getMemCode(Connection conn, String memId, String memPw) throws Exception {

		int memCode = 0;
		
		try {
			String query = prop.getProperty("selectMember");
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, memId);
			pstmt.setString(2, memPw);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				memCode = rs.getInt(1);
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return memCode;
	}

	public String getNickname(Connection conn, int loginMemCode) throws Exception {
		
		String nickName = null;
		
		try {
			String query = prop.getProperty("selectNickname");
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, loginMemCode);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				nickName = rs.getString(1);
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return nickName;
	}

	public List<Todo> selectTodo(Connection conn, int loginMemCode) throws Exception {

		List<Todo> TodoList = new ArrayList<Todo>();
		
		try {
			String query = prop.getProperty("selectTodo");
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, loginMemCode);
			
			rs = pstmt.executeQuery();
			
			if(!rs.isBeforeFirst()) {
				return TodoList;
			}
			
			while(rs.next()) {
				
				TodoList.add(new Todo(rs.getInt("MEMBER_CODE"), rs.getInt("TODO_NO"),
						rs.getString("TODO_TITLE"), rs.getString("TODO_CONTENT"), rs.getString("COMPLETE_YN").charAt(0),
						rs.getString("CREATED_DATE"), rs.getString("UPDATED_DATE")));
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return TodoList;
	}

	public int insertTodo(Connection conn, Todo todo) throws Exception {

		int result = 0;
		
		try {
			String query = prop.getProperty("insertTodo");
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, todo.getMemCode());
			pstmt.setString(2, todo.getTodoTitle());
			pstmt.setString(3, todo.getTodoContent());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int todoNoCheck(Connection conn, int todoNo) throws Exception {

		int result = 0;
		
		try {
			String query = prop.getProperty("CountTodo");
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, todoNo);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return result;
	}
	
	public int updateTodo(Connection conn, Todo todo) throws Exception {

		int result = 0;
		
		try {
			String query = prop.getProperty("updateTodo");
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, todo.getTodoTitle());
			pstmt.setString(2, todo.getTodoContent());
			pstmt.setInt(3, todo.getMemCode());
			pstmt.setInt(4, todo.getTodoNo());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int updateCompleteYN(Connection conn, Todo todo) throws Exception {

		int result = 0;
		
		try {
			String query = prop.getProperty("updateCompleteYN");
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, String.valueOf(todo.getCompleteYN()));
			pstmt.setInt(2, todo.getMemCode());
			pstmt.setInt(3, todo.getTodoNo());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int deleteTodo(Connection conn, int loginMemCode, int todoNo) throws Exception {
		
		int result = 0;
		
		try {
			String query = prop.getProperty("deleteTodo");
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, loginMemCode);
			pstmt.setInt(2, todoNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
}
