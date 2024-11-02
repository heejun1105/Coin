package com.example.demo.account;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	
	private final AccountRepository accountRepository;
	
	public Account getAccount(Long accountNum) {
		
		Optional<Account> account = accountRepository.findByAccountNum(accountNum);
		
		return account.get();
		}

}
