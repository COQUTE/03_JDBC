package com.hw.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

	private int memCode;
	private int todoNo;
	private String todoTitle;
	private String todoContent;
	private char completeYN;
	private String createdDate;
	private String updatedDate;
	
	public Todo(int memCode, String todoTitle, String todoContent) {
		this.memCode = memCode;
		this.todoTitle = todoTitle;
		this.todoContent = todoContent;
	}
	
	public Todo(int memCode, int todoNo, String todoTitle, String todoContent) {
		this.memCode = memCode;
		this.todoNo = todoNo;
		this.todoTitle = todoTitle;
		this.todoContent = todoContent;
	}
	
	public Todo(int memCode, int todoNo, char completeYN) {
		this.memCode = memCode;
		this.todoNo = todoNo;
		this.completeYN = completeYN;
	}

	@Override
	public String toString() {
		return "Todo [todoNo=" + todoNo + ", todoTitle=" + todoTitle + ", todoContent=" + todoContent + ", completeYN="
				+ completeYN + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
	}

}
