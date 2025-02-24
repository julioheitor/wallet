package com.chalenge.wallet.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "wallet")
public class Wallet extends DomainEntity implements Serializable {

    private String name;

    private Double funds;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
