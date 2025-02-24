package com.chalenge.wallet.configurations;

import com.chalenge.wallet.dtos.ErrorDTO;
import com.chalenge.wallet.exceptions.NegativeAmountException;
import com.chalenge.wallet.exceptions.NotEnoughFundsException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({NegativeAmountException.class})
    public ResponseEntity<ErrorDTO> handleNegativeAmountExceptions(HttpServletRequest req, NegativeAmountException ex) {
        logger.error("Amount cannot be negative: {}", ex.getMessage());
        ErrorDTO error = new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                req.getServletPath()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotEnoughFundsException.class})
    public ResponseEntity<ErrorDTO> handleNotEnoughFundExceptions(HttpServletRequest req, NotEnoughFundsException ex) {
        logger.error("The wallet has not enough funds: {}", ex.getMessage());
        ErrorDTO error = new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                req.getServletPath()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDTO> handleUnknownExceptions(HttpServletRequest req, Exception ex) {
        logger.error("Error: {}", ex.getMessage());
        ErrorDTO error = new ErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                req.getServletPath()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}