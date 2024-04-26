package com.example.microservicesclientes.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetails {
    private Double netReceivedAmount;
    private Double totalPaidAmount;
    private Double overpaidAmount;
    private Double installmentAmount;
}
