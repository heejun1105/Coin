package com.example.demo.member;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	
	//멤버넘으로 회원찾기
	public Member findMemberByMemberNum(Long MemberNum) {
		
		Member member = memberRepository.findByMemberNum(MemberNum).get();
		
		return member;
	}
	
	//멤버ID로 회원찾기
	public Member findMemberByMemberId(String MemberId) {
		
		Member member = memberRepository.findByMemberId(MemberId).get();
		
		return member;
	}
	
	public void createmember(String memberId, String passWord, String email) {
		
		Member member = new Member();
		
		member.setMemberId(memberId);
		member.setPassword(passWord);
		member.setEmail(email);
		member.setCreateDate(LocalDateTime.now());
	}
	
}
