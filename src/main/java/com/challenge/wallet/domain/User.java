package com.chalenge.wallet.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "wallet_user")
public class User extends DomainEntity implements Serializable{
	
	private String name;

}
