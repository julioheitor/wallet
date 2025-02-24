package com.challenge.wallet.dtos;

import lombok.Data;

@Data
public class BalanceDTO {

    private Long id;
    private Double funds;
    private String date;
}
