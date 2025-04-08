package com.example.be_ClothingStore.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.be_ClothingStore.domain.Payments;
import com.example.be_ClothingStore.repository.PaymentRepository;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    public PaymentService (PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    public Payments createAPayment(Payments payments){
        return this.paymentRepository.save(payments);
    }

    public Payments findPaymentByOrderId (String orderId){
        Optional<Payments> payment = this.paymentRepository.findByOrderID(orderId);
        if (payment.isPresent()){
            return payment.get();
        }
        return null;
    }
}
