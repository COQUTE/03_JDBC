package com.hw.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

	private int memCode;
	private int todoNo;
	private String todoTitle;
	private char hasDone;
	private String createdDate;
	
	public Todo(int memCode, int todoNo, String todoTitle) {
		this.memCode = memCode;
		this.todoNo = todoNo;
		this.todoTitle = todoTitle;
	}
	
	public Todo(int memCode, int todoNo, char hasDone) {
		this.memCode = memCode;
		this.todoNo = todoNo;
		this.hasDone = hasDone;
	}

	@Override
	public String toString() {
		return "Todo [todoNo=" + todoNo + ", todoTitle=" + todoTitle + ", hasDone=" + hasDone + ", createdDate="
				+ createdDate + "]";
	}
}
