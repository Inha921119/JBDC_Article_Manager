package com.KoreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.example.JAM.util.DBUtil;
import com.KoreaIT.example.JAM.util.SecSql;

public class App {
	public void run() {
		Scanner sc = new Scanner(System.in);
		System.out.println("== 프로그램 시작 ==");

		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");

			while (true) {
				System.out.printf("명령어) ");
				String cmd = sc.nextLine().trim();

				if (cmd.equals("article write")) {
					System.out.println("== 게시물 작성 ==");

					System.out.printf("제목 : ");
					String title = sc.nextLine().trim();
					System.out.printf("내용 : ");
					String body = sc.nextLine().trim();

					SecSql sql = new SecSql();

					sql.append("INSERT INTO article");
					sql.append("SET regDate = NOW()");
					sql.append(", updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);

					int id = DBUtil.insert(conn, sql);

					System.out.printf("%d번 글이 생성되었습니다\n", id);
				} else if (cmd.equals("article list")) {
					System.out.println("== 게시물 목록 ==");

					List<Article> articles = new ArrayList<>();

					SecSql sql = new SecSql();

					sql.append("SELECT *");
					sql.append("FROM article");
					sql.append("ORDER BY id DESC");

					List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

					for (Map<String, Object> articleMap : articleListMap) {
						articles.add(new Article(articleMap));
					}

					if (articles.size() == 0) {
						System.out.println("게시물이 없습니다");
						continue;
					}

					System.out.println("번호	|	제목");

					for (Article article : articles) {
						System.out.printf("%d	|	%s\n", article.id, article.title);
					}
				} else if (cmd.startsWith("article modify ")) {
					int id = Integer.parseInt(cmd.split(" ")[2]);

					SecSql sql = new SecSql();

					sql.append("SELECT COUNT(*)");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);

					int articlesCount = DBUtil.selectRowIntValue(conn, sql);

					if (articlesCount == 0) {
						System.out.printf("%d번 글은 존재하지 않습니다.\n", id);
						continue;
					}

					System.out.println("== 게시물 수정 ==");

					System.out.printf("수정할 제목 : ");
					String title = sc.nextLine().trim();
					System.out.printf("수정할 내용 : ");
					String body = sc.nextLine().trim();

					sql = new SecSql();

					sql.append("UPDATE article");
					sql.append("SET updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);
					sql.append("WHERE id = ?", id);

					DBUtil.update(conn, sql);

					System.out.printf("%d번 글이 수정되었습니다\n", id);
				} else if (cmd.startsWith("article detail ")) {
					int id = Integer.parseInt(cmd.split(" ")[2]);

					SecSql sql = new SecSql();

					sql.append("SELECT *");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);

					Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

					if (articleMap.isEmpty()) {
						System.out.printf("%d번 게시글은 존재하지 않습니다.", id);
						continue;
					}

					System.out.printf("==== %d번 게시글 상세보기 ====\n", id);

					Article article = new Article(articleMap);

					System.out.printf("제목 :	%s\n", article.title);
					System.out.printf("작성날짜 : %s\n", article.regDate);
					System.out.printf("수정날짜 : %s\n", article.updateDate);
					System.out.printf("내용 : 	%s\n", article.body);
				} else if (cmd.startsWith("article delete ")) {
					int id = Integer.parseInt(cmd.split(" ")[2]);

					SecSql sql = new SecSql();

					sql.append("SELECT COUNT(*)");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);

					int articlesCount = DBUtil.selectRowIntValue(conn, sql);

					if (articlesCount == 0) {
						System.out.printf("%d번 글은 존재하지 않습니다.\n", id);
						continue;
					}

					sql = new SecSql();

					sql.append("DELETE FROM article");
					sql.append("WHERE id = ?", id);

					DBUtil.delete(conn, sql);

					System.out.printf("%d번 글이 삭제되었습니다\n", id);
				} else if (cmd.equals("member join")) {
					String loginId = null;
					String loginPw = null;
					String loginPwChk = null;
					String name = null;
					String phoneNum = null;

					SecSql sql = new SecSql();

					System.out.println("== 회원 가입 ==");

					while (true) {
						System.out.printf("아이디 : ");
						loginId = sc.nextLine().trim();

						if (loginId.length() == 0) {
							System.out.println("아이디를 입력해주세요");
							continue;
						}

						sql = new SecSql();

						sql.append("SELECT COUNT(loginId) > 0");
						sql.append("FROM `member`");
						sql.append("WHERE loginId = ?", loginId);

						boolean isLoginIdDup = DBUtil.selectRowBooleanValue(conn, sql);

						if (isLoginIdDup) {
							System.out.println("사용중인 아이디입니다");
							System.out.println("다른 아이디를 입력해주세요");
							continue;
						}
						System.out.println("사용 가능한 아이디입니다.");

						break;
					} 

					while (true) {
						System.out.printf("비밀번호 : ");
						loginPw = sc.nextLine().trim();

						if (loginPw.length() == 0) {
							System.out.println("비밀번호를 입력해주세요");
							continue;
						}

						boolean loginPwCheck = true;

						while (true) {
							System.out.printf("비밀번호 확인 : ");
							loginPwChk = sc.nextLine().trim();

							if (loginPwChk.length() == 0) {
								System.out.println("비밀번호 확인을 입력해주세요");
								continue;
							}

							if (loginPw.equals(loginPwChk) == false) {
								System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
								loginPwCheck = false;
							}
							break;
						}
						if (loginPwCheck) {
							break;
						}
					}
					while (true) {
						System.out.printf("이름 : ");
						name = sc.nextLine().trim();

						if (name.length() == 0) {
							System.out.println("이름을 입력해주세요");
							continue;
						}
						break;
					}
					while (true) {
						System.out.printf("전화번호 : ");
						phoneNum = sc.nextLine().trim();

						if (phoneNum.length() == 0) {
							System.out.println("전화번호를 입력해주세요");
							continue;
						}

						sql = new SecSql();

						sql.append("SELECT COUNT(phoneNum) > 0");
						sql.append("FROM `member`");
						sql.append("WHERE phoneNum = ?", phoneNum);

						boolean isPhoneNumDup = DBUtil.selectRowBooleanValue(conn, sql);

						if (isPhoneNumDup) {
							System.out.println("사용중인 전화번호입니다");
							System.out.println("다른 전화번호를 입력해주세요");
							continue;
						}

						System.out.println("사용 가능한 전화번호입니다.");

						break;
					}

					sql = new SecSql();

					sql.append("INSERT INTO member");
					sql.append("SET regDate = NOW()");
					sql.append(", updateDate = NOW()");
					sql.append(", loginId = ?", loginId);
					sql.append(", loginPw = ?", loginPw);
					sql.append(", `name` = ?", name);
					sql.append(", phoneNum = ?", phoneNum);

					DBUtil.insert(conn, sql);

					System.out.printf("%s님 회원가입이 완료되었습니다.\n", loginId);
				} else if (cmd.equals("member login")) {
					String loginId = null;
					String loginPw = null;

					SecSql sql = new SecSql();

					System.out.println("== 회원 로그인 ==");

					while (true) {
						System.out.printf("아이디 : ");
						loginId = sc.nextLine().trim();

						if (loginId.length() == 0) {
							System.out.println("아이디를 입력해주세요");
							continue;
						}

						sql = new SecSql();

						sql.append("SELECT COUNT(loginId) > 0");
						sql.append("FROM `member`");
						sql.append("WHERE loginId = ?", loginId);

						boolean isLoginIdChk = DBUtil.selectRowBooleanValue(conn, sql);

						if (isLoginIdChk) {
							break;
						}
						System.out.println("아이디를 확인해주세요");
					} 

					while (true) {
						System.out.printf("비밀번호 : ");
						loginPw = sc.nextLine().trim();

						if (loginPw.length() == 0) {
							System.out.println("비밀번호를 입력해주세요");
							continue;
						}

						sql = new SecSql();

						sql.append("SELECT *");
						sql.append("FROM `member`");
						sql.append("WHERE loginId = ?", loginId);

						Map<String, Object> memberMap = DBUtil.selectRow(conn, sql);

						Member member = new Member(memberMap);

						if (member.loginPw.equals(loginPw)) {
							System.out.println("로그인에 성공하였습니다.");
							break;
						}
						System.out.println("비밀번호를 확인해주세요");
					}

				}

				if (cmd.equals("exit")) {
					System.out.println("== 프로그램 종료 ==");
					break;
				}

			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		sc.close();
	}
}