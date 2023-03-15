package com.KoreaIT.example.JAM.service;

import java.sql.Connection;
import java.util.Map;

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

	public Map<String, Object> getMemberByLoginId(String loginId) {
		return memberDao.getMemberByLoginId(loginId);
	}

	public boolean isLogined() {
		return memberDao.isLogined();
	}
}
