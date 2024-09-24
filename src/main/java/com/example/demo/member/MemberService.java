package com.example.demo.member;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	
	public Member findMemberByMemberNum(Long MemberNum) {
		
		Member member = memberRepository.findByMemberNum(MemberNum).get();
		
		return member;
	}
}
