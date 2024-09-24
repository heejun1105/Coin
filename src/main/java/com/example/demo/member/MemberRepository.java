package com.example.demo.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
	
	Optional<Member> findByMemberNum(Long memberNum);

}
