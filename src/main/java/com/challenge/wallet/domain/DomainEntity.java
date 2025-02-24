package com.challenge.wallet.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class DomainEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", allocationSize=1)
    private Long id;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "updated_on")
	private LocalDateTime updatedOn;

	@PrePersist
	public void setDates(){
		if(createdOn == null){
			createdOn = LocalDateTime.now();
		}

		updatedOn = LocalDateTime.now();
	}

}
