package com.KoreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.example.JAM.Article;
import com.KoreaIT.example.JAM.service.ArticleService;

public class ArticleController extends Controller {
	private ArticleService articleService;

	public ArticleController (Connection conn, Scanner sc) {
		this.articleService = new ArticleService(conn);
		this.sc = sc;
	}
	
	public void doWrite() {
		System.out.println("== 게시물 작성 ==");

		System.out.printf("제목 : ");
		String title = sc.nextLine().trim();
		System.out.printf("내용 : ");
		String body = sc.nextLine().trim();

		int id = articleService.doWrite(title, body);

		System.out.printf("%d번 글이 생성되었습니다\n", id);
	}

	public void showList() {
		System.out.println("== 게시물 목록 ==");
		
		List<Article> articles = articleService.getArticles();

		if (articles.size() == 0) {
			System.out.println("게시물이 없습니다");
			return;
		}

		System.out.println("번호	|	제목");

		for (Article article : articles) {
			System.out.printf("%d	|	%s\n", article.id, article.title);
		}
	}

	public void doModify(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		int articlesCount = articleService.isExistArticle(id);

		if (articlesCount == 0) {
			System.out.printf("%d번 글은 존재하지 않습니다.\n", id);
			return;
		}

		System.out.println("== 게시물 수정 ==");

		System.out.printf("수정할 제목 : ");
		String title = sc.nextLine().trim();
		System.out.printf("수정할 내용 : ");
		String body = sc.nextLine().trim();

		articleService.doModify(id, title, body);
		
		System.out.printf("%d번 글이 수정되었습니다\n", id);
	}

	public void showDetail(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		Map<String, Object> articleMap = articleService.getArticle(id);
				
		if (articleMap.isEmpty()) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.", id);
			return;
		}

		System.out.printf("==== %d번 게시글 상세보기 ====\n", id);

		Article article = new Article(articleMap);

		System.out.printf("제목 :	%s\n", article.title);
		System.out.printf("작성날짜 : %s\n", article.regDate);
		if (article.regDate.equals(article.updateDate) == false) {
			System.out.printf("수정날짜 : %s\n", article.updateDate);
		}
		System.out.printf("내용 : 	%s\n", article.body);
	}

	public void dodelete(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		int articlesCount = articleService.isExistArticle(id);
	
		if (articlesCount == 0) {
			System.out.printf("%d번 글은 존재하지 않습니다.\n", id);
			return;
		}

		articleService.doDelete(id);
		
		System.out.printf("%d번 글이 삭제되었습니다\n", id);
	}

}