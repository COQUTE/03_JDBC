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
@ToString
public class Todo {

	private int todoNo;
	private String todoTitle;
	private char hasDone;
	private String createdDate;
	
	public Todo(String todoTitle, char hasDone, String createdDate) {
		this.todoTitle = todoTitle;
		this.hasDone = hasDone;
		this.createdDate = createdDate;
	}
}
