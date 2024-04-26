package com.example.microservicesclientes.configs;

import com.example.microservicesclientes.dtos.PaymentResponse;
import com.mercadopago.client.payment.PaymentClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.mercadopago.MercadoPagoConfig;

@Configuration
public class CheckoutProConfig {


    private String accessToken= "APP_USR-2915382496521148-042613-50299701241a317034f600a38a77294a-1784943659";
    @Autowired
    private ModelMapper modelMapper;

    public CheckoutProConfig() {
        MercadoPagoConfig.setAccessToken(accessToken);
        System.out.println("Mercado Pago access token set"+ accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }
    public PaymentResponse getPagos(Long paymentId) {
        try {
            PaymentClient client = new PaymentClient();
            return modelMapper.map(client.get(paymentId), PaymentResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
