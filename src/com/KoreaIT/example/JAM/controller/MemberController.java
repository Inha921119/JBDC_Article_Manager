package com.KoreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.Scanner;

import com.KoreaIT.example.JAM.Member;
import com.KoreaIT.example.JAM.service.MemberService;

public class MemberController extends Controller {
	private MemberService memberService;
	private boolean isLogined;

	public MemberController(Connection conn, Scanner sc) {
		this.memberService = new MemberService(conn);
		this.sc = sc;
		this.isLogined = false;
	}

	public void doJoin() {
		String loginId = null;
		String loginPw = null;
		String loginPwChk = null;
		String name = null;
		String phoneNum = null;

		if (isLogined) {
			System.out.println("로그아웃 후 이용가능합니다.");
			return;
		}

		System.out.println("== 회원 가입 ==");

		while (true) {
			System.out.printf("아이디 : ");
			loginId = sc.nextLine().trim();

			if (loginId.length() == 0) {
				System.out.println("아이디를 입력해주세요");
				continue;
			}

			boolean isLoginIdDup = memberService.isLoginIdDup(loginId);

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

			boolean isPhoneNumDup = memberService.isPhoneNumDup(phoneNum);

			if (isPhoneNumDup) {
				System.out.println("사용중인 전화번호입니다");
				System.out.println("다른 전화번호를 입력해주세요");
				continue;
			}

			System.out.println("사용 가능한 전화번호입니다.");

			break;
		}

		memberService.doJoin(loginId, loginPw, name, phoneNum);

		System.out.printf("%s님 회원가입이 완료되었습니다.\n", loginId);
	}

	public void doLogin() {
		String loginId = null;
		String loginPw = null;

		if (isLogined) {
			System.out.println("로그아웃 후 이용가능합니다.");
			return;
		}

		System.out.println("== 회원 로그인 ==");

		while (true) {
			System.out.printf("아이디 : ");
			loginId = sc.nextLine().trim();

			if (loginId.length() == 0) {
				System.out.println("아이디를 입력해주세요");
				continue;
			}

			System.out.printf("비밀번호 : ");
			loginPw = sc.nextLine().trim();

			if (loginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요");
				continue;
			}
			
			boolean isLoginIdChk = memberService.isLoginIdDup(loginId);
			
			if (!isLoginIdChk) {
				System.out.printf("%s회원은 존재하지 않습니다.\n", loginId);
				continue;
			}

			Member member = memberService.getMemberByLoginId(loginId);
			
			if (!member.loginPw.equals(loginPw)) {
				System.out.println("비밀번호를 확인해주세요");
				continue;
			}
			
			System.out.println("로그인에 성공하였습니다.");
			isLogined = memberService.isLogined();
			memberService.updateLastLoginedDate(loginId);
			break;
		}
	}

	public void doLogout() {
		if (!isLogined) {
			System.out.println("로그인 후 이용가능합니다.");
			return;
		}

		isLogined = memberService.isLogined();
		System.out.println("로그아웃이 완료되었습니다");
		return;
	}

	public void showProfile() {
		if (!isLogined) {
			System.out.println("로그인 후 이용가능합니다.");
			return;
		}
		Member member = memberService.getMember();
		
		System.out.println("==== 내 정보 ====");

		System.out.printf("아이디		: %s\n", member.loginId);
		System.out.printf("가입날짜		: %s\n", member.regDate);
		if (!member.regDate.equals(member.lastLoginedDate)) {
			System.out.printf("마지막 접속날짜	: %s\n", member.lastLoginedDate);
		}
		System.out.printf("이름		: %s\n", member.name);
		System.out.printf("전화번호		: %s\n", member.phoneNum);
	}
}
