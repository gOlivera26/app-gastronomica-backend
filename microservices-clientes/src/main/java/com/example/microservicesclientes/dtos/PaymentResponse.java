package com.example.microservicesclientes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long id;
    private LocalDateTime dateCreated;
    private LocalDateTime dateApproved;
    private LocalDateTime dateLastUpdated;
    private LocalDateTime moneyReleaseDate;
    private String paymentMethodId;
    private String paymentTypeId;
    private String status;
    private String statusDetail;
    private String currencyId;
    private String description;
    private Long collectorId;
    private Payer payer;
    private Object metadata;
    private Object additionalInfo;
    private String externalReference;
    private Double transactionAmount;
    private Double transactionAmountRefunded;
    private Double couponAmount;
    private TransactionDetails transactionDetails;
    private Integer installments;
    private Object card;
}
