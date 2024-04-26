package com.example.microservicesclientes.dtos;

import com.mercadopago.resources.customer.Identification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payer {
    private Long id;
    private String email;
    private Identification identification;
    private String type;

}
