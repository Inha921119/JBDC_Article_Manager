package com.KoreaIT.example.JAM.dao;

import java.sql.Connection;
import java.util.Map;

import com.KoreaIT.example.JAM.util.DBUtil;
import com.KoreaIT.example.JAM.util.SecSql;

public class MemberDao extends Dao {
	private boolean isLoginedChk;
	
	public MemberDao (Connection conn) {
		this.conn = conn;
		this.isLoginedChk = false;
	}

	public boolean isLoginIdDup(String loginId) {
		SecSql sql = new SecSql();

		sql.append("SELECT COUNT(loginId) > 0");
		sql.append("FROM `member`");
		sql.append("WHERE loginId = ?", loginId);

		return DBUtil.selectRowBooleanValue(conn, sql);
	}
	
	public boolean isPhoneNumDup(String phoneNum) {
		SecSql sql = new SecSql();

		sql.append("SELECT COUNT(phoneNum) > 0");
		sql.append("FROM `member`");
		sql.append("WHERE phoneNum = ?", phoneNum);
		return DBUtil.selectRowBooleanValue(conn, sql);
	}
	
	public void doJoin(String loginId, String loginPw, String name, String phoneNum) {
		SecSql sql = new SecSql();

		sql.append("INSERT INTO member");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", loginId);
		sql.append(", loginPw = ?", loginPw);
		sql.append(", `name` = ?", name);
		sql.append(", phoneNum = ?", phoneNum);

		DBUtil.insert(conn, sql);
	}

	public Map<String, Object> getMemberByLoginId(String loginId) {
		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM `member`");
		sql.append("WHERE loginId = ?", loginId);
		
		return DBUtil.selectRow(conn, sql);
	}

	public boolean isLogined() {
		if (isLoginedChk) {
			isLoginedChk = false;
		} else {
			isLoginedChk = true;
		}
		return isLoginedChk;
	}
}
