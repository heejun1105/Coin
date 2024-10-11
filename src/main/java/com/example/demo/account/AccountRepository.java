package com.example.demo.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
	
	//AccountNum으로 Account찾기
	Optional<Account> findByAccountNum(Long accountNum);

}
