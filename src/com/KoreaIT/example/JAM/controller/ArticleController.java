package com.KoreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import com.KoreaIT.example.JAM.Article;
import com.KoreaIT.example.JAM.service.ArticleService;
import com.KoreaIT.example.JAM.session.Session;
import com.KoreaIT.example.JAM.util.Util;

public class ArticleController extends Controller {
	private ArticleService articleService;

	public ArticleController(Connection conn, Scanner sc) {
		this.articleService = new ArticleService(conn);
		this.sc = sc;
	}

	public void doWrite() {
		if (!Session.isLogined()) {
			System.out.println("로그인 후 이용가능합니다.");
			return;
		}
		System.out.println("== 게시물 작성 ==");

		System.out.printf("제목 : ");
		String title = sc.nextLine().trim();
		System.out.printf("내용 : ");
		String body = sc.nextLine().trim();

		int id = articleService.doWrite(title, body, Session.loginedMemberId);

		System.out.printf("%d번 글이 생성되었습니다\n", id);
	}

	public void showList() {
		System.out.println("== 게시물 목록 ==");

		List<Article> articles = articleService.getArticles();

		if (articles.size() == 0) {
			System.out.println("게시물이 없습니다");
			return;
		}

		System.out.println("번호	| 제목		| 작성자	| 작성날짜");

		for (Article article : articles) {
			System.out.printf("%d	| %s		| %s	| %s\n", article.id, article.title, article.writerName,
					Util.changeDateToString(article.updateDate).substring(0, 10));
		}
	}

	public void doModify(String cmd) {
		if (!Session.isLogined()) {
			System.out.println("로그인 후 이용가능합니다.");
			return;
		}
		int id = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(id);

		if (article == null) {
			return;
		}

		if (article.writerId == Session.loginedMemberId) {
			System.out.println("== 게시물 수정 ==");
			
			System.out.printf("수정할 제목 : ");
			String title = sc.nextLine().trim();
			System.out.printf("수정할 내용 : ");
			String body = sc.nextLine().trim();
			
			articleService.doModify(id, title, body);
			
			System.out.printf("%d번 글이 수정되었습니다\n", id);
		} else {
			System.out.println("권한이 없습니다");
		}
	}

	public void showDetail(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(id);

		if (article == null) {
			return;
		}

		System.out.printf("==== %d번 게시글 상세보기 ====\n", id);

		System.out.printf("제목 :	%s\n", article.title);
		System.out.printf("작성자 : 	%s\n", article.writerName);
		System.out.printf("작성날짜 : %s\n", Util.changeDateToString(article.regDate));
		if (!article.regDate.equals(article.updateDate)) {
			System.out.printf("수정날짜 : %s\n", Util.changeDateToString(article.updateDate));
		}
		System.out.printf("내용 : 	%s\n", article.body);
	}

	public void doDelete(String cmd) {
		if (!Session.isLogined()) {
			System.out.println("로그인 후 이용가능합니다.");
			return;
		}
		int id = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(id);

		if (article == null) {
			return;
		}

		if (article.writerId == Session.loginedMemberId) {
			articleService.doDelete(id);
			System.out.printf("%d번 글이 삭제되었습니다\n", id);
		} else {
			System.out.println("권한이 없습니다");
		}
	}

}