package com.example.demo.account;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ACCOUNT")
public class Account {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountNum")
    @SequenceGenerator(name = "accountNum", sequenceName = "accountNum", allocationSize = 1)
	private Long accountNum;
	
	//보유KRW
	private Long possesstionKRW;
	
	private Long totalAssets;
	
	

}
