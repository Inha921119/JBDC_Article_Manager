package com.KoreaIT.example.JAM.service;

import java.sql.Connection;
import java.util.Map;

import com.KoreaIT.example.JAM.Member;
import com.KoreaIT.example.JAM.dao.MemberDao;

public class MemberService {
	private MemberDao memberDao;
	
	public MemberService(Connection conn) {
		this.memberDao = new MemberDao(conn);
	}

	public boolean isLoginIdDup(String loginId) {
		return memberDao.isLoginIdDup(loginId);
	}
	
	public boolean isPhoneNumDup(String phoneNum) {
		return memberDao.isPhoneNumDup(phoneNum);
	}
	
	public void doJoin(String loginId, String loginPw, String name, String phoneNum) {
		memberDao.doJoin(loginId, loginPw, name, phoneNum);
	}

	public Member getMemberByLoginId(String loginId) {
		Map<String, Object> memberMap = memberDao.getMemberByLoginId(loginId);
		
		if (memberMap.isEmpty()) {
			System.out.printf("%s회원은 존재하지 않습니다.\n", loginId);
			return null;
		}
		return new Member(memberMap);
	}
	
	public void updateLastLoginedDate(String loginId) {
		memberDao.updateLastLoginedDate(loginId);
	}

	public Member getMember() {
		String LoginedId = memberDao.getLoginedId();
		Map<String, Object> memberMap = memberDao.getMemberByLoginId(LoginedId);
		
		return new Member(memberMap);
	}

}
