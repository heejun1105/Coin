package com.example.demo.account;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	
	private final AccountRepository accountRepository;
	
	//고유번호로계좌찾기
	public Account getAccount(Long accountNum) {
		
		Optional<Account> account = accountRepository.findByAccountNum(accountNum);
		
		return account.get(); 
		}

}
