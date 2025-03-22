package com.hw.model.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Member {
	
	private int memberCode;
	private String memberId;
	private String memberPw;
	private String nickname;
	
	public Member(String memberId, String memberPw, String nickname) {
		this.memberId = memberId;
		this.memberPw = memberPw;
		this.nickname = nickname;
	}
}
