package com.KoreaIT.example.JAM.dao;

import java.sql.Connection;
import java.util.Map;

import com.KoreaIT.example.JAM.util.DBUtil;
import com.KoreaIT.example.JAM.util.SecSql;

public class MemberDao extends Dao {
	private String nowLoginedId;
	
	public MemberDao (Connection conn) {
		this.conn = conn;
		this.nowLoginedId = null;
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
		
		nowLoginedId = loginId;
		
		return DBUtil.selectRow(conn, sql);
	}

	public void updateLastLoginedDate(String loginId) {
		SecSql sql = new SecSql();

		sql.append("UPDATE `member`");
		sql.append("SET lastLoginedDate = NOW()");
		sql.append("WHERE loginId = ?", loginId);

		DBUtil.update(conn, sql);
	}
	
	public String getLoginedId() {
		return nowLoginedId;
	}

	public Map<String, Object> getMemberById(int id) {
		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append("FROM `member`");
		sql.append("WHERE id = ?", id);
			
		return DBUtil.selectRow(conn, sql);
	}
}
