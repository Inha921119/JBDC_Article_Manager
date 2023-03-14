package com.KoreaIT.example.JAM;

import java.time.LocalDateTime;
import java.util.Map;

public class Member {
	public int id;
	public LocalDateTime regDate;
	public LocalDateTime updateDate;
	public LocalDateTime lastLoginedDate;
	public String loginId;
	public String loginPw;
	public String name;
	public String phoneNum;

	public Member(Map<String, Object> memberMap) {
		this.id = (int) memberMap.get("id");
		this.regDate = (LocalDateTime) memberMap.get("regDate");
		this.updateDate = (LocalDateTime) memberMap.get("updateDate");
		this.lastLoginedDate = (LocalDateTime) memberMap.get("lastLoginedDate");
		this.loginId = (String) memberMap.get("loginId");
		this.loginPw = (String) memberMap.get("loginPw");
		this.name = (String) memberMap.get("name");
		this.phoneNum = (String) memberMap.get("phoneNum");
	}

	@Override
	public String toString() {
		return "Member [id=" + id + ", regDate=" + regDate + ", updateDate=" + updateDate + ", lastLoginedDate="
				+ lastLoginedDate + ", loginId=" + loginId + ", loginPw=" + loginPw + ", name=" + name + ", phoneNum="
				+ phoneNum + "]";
	}


}
