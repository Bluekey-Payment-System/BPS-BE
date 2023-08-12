package com.github.bluekey.entity.transaction;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
public class Transaction {
	@Id
	private Long id;
}
