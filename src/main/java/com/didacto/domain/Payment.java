package com.didacto.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long price;
    private String status;
    private String paymentUid;

    @Builder
    public Payment(Long price, String status){
        this.price = price;
        this.status = status;
    }
    public void changePaymentBySuccess(String status, String paymentUid) {
        this.status = status;
        this.paymentUid = paymentUid;
    }
}
