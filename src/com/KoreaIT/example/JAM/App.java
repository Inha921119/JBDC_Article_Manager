package com.KoreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
	public static void run() {
		Scanner sc = new Scanner(System.in);

		System.out.println("== 프로그램 시작 ==");

		while (true) {
			System.out.printf("명령어) ");
			String cmd = sc.nextLine().trim();

			if (cmd.equals("exit")) {
				break;
			}

			if (cmd.equals("article write")) {
				System.out.println("== 게시물 작성 ==");
				System.out.printf("제목 : ");
				String title = sc.nextLine().trim();
				System.out.printf("내용 : ");
				String body = sc.nextLine().trim();

				doWrite(title, body);

				System.out.println("글 작성이 완료되었습니다.");
				continue;
			} else if (cmd.equals("article list")) {
				System.out.println("== 게시물 목록 ==");

				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;

				List<Article> articles = new ArrayList<>();

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");

					String sql = "SELECT *";
					sql += " FROM article";
					sql += " ORDER BY id DESC;";

					pstmt = conn.prepareStatement(sql);
					rs = pstmt.executeQuery();

					while (rs.next()) {
						int id = rs.getInt("id");
						String regDate = rs.getString("regDate");
						String updateDate = rs.getString("updateDate");
						String title = rs.getString("title");
						String body = rs.getString("body");

						Article article = new Article(id, regDate, updateDate, title, body);
						articles.add(article);

					}

				} catch (ClassNotFoundException e) {
					System.out.println("드라이버 로딩 실패");
				} catch (SQLException e) {
					System.out.println("에러: " + e);
				} finally {
					try {
						if (rs != null && !rs.isClosed()) {
							rs.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						if (pstmt != null && !pstmt.isClosed()) {
							pstmt.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						if (conn != null && !conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}

				if (articles.size() == 0) {
					System.out.println("게시물이 없습니다.");
					continue;
				}

				System.out.println("번호	|제목");
				for (Article article : articles) {
					System.out.printf("%d	|%s\n", article.id, article.title);
				}
			} else if (cmd.startsWith("article modify")) {
				String[] cmdBits = cmd.split(" ");

				if (cmdBits.length == 2) {
					System.out.println("명령어를 확인해주세요");
					continue;
				}
				int id = Integer.valueOf(cmdBits[2]);

				System.out.println("== 게시물 수정 ==");
				System.out.printf("제목 : ");
				String title = sc.nextLine().trim();
				System.out.printf("내용 : ");
				String body = sc.nextLine().trim();

				doModify(id, title, body);
				continue;
			} else {
				System.out.println("명령어가 없습니다.");

			}

		}
		System.out.println("== 프로그램 종료 ==");

		sc.close();
	}

	public static void doWrite(String title, String body) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");

			String sql = "INSERT INTO article";
			sql += " SET regDate = NOW()";
			sql += ", updateDate = NOW()";
			sql += ", title = '" + title + "'";
			sql += ", `body` = '" + body + "';";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void doModify(int id, String title, String body) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");

			String sql = "UPDATE article";
			sql += " SET updatedate = NOW()";
			sql += ", title = '" + title + "'";
			sql += ", `body` = '" + body + "'";
			sql += "WHERE id = " + id + ";";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("%d번 글이 수정되었습니다.\n", id);
	}
}
