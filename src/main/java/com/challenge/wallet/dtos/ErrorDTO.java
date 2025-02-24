package com.challenge.wallet.dtos;

import java.time.LocalDateTime;

public record ErrorDTO (Integer status, String message, LocalDateTime timeStamp, String path){
}
