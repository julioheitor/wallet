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
@Table(name = "balance")
public class Balance extends DomainEntity implements Serializable {

    private Double funds;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
}
