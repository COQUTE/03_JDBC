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
			String query = prop.getProperty("selectCount");
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, memId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				count = rs.getInt(1);
				
		} finally {
			close(pstmt);
			close(rs);
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
				
				TodoList.add(new Todo(rs.getInt("TODO_NO"), rs.getString("TODO_TITLE"), rs.getString("HAS_DONE").charAt(0), rs.getString("CREATED_DATE")));
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return TodoList;
	}

	
}
