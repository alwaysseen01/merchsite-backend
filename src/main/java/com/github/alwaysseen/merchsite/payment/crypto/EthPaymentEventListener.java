package com.github.alwaysseen.merchsite.payment.crypto;

import com.github.alwaysseen.merchsite.entities.AppOrder;
import com.github.alwaysseen.merchsite.entities.EthPaymentStatus;
import com.github.alwaysseen.merchsite.payment.crypto.contracts.PaymentContract;
import com.github.alwaysseen.merchsite.repositories.OrderRepository;
import io.reactivex.disposables.Disposable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.math.BigInteger;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EthPaymentEventListener {
    @Autowired
    private final EthPaymentService service;
    @Autowired
    private final OrderRepository repository;

    private Disposable subscription;

    @PostConstruct
    public void init(){
        PaymentContract contract = service.loadContract();
        subscription = contract.paymentHistoryEventFlowable(DefaultBlockParameterName.EARLIEST,
                                            DefaultBlockParameterName.LATEST)
                .subscribe(paymentHistoryEventResponse -> {
                    log.info("GETTING CONTRACT EVENT !!!");
                    BigInteger orderId = paymentHistoryEventResponse.orderId;
                    BigInteger status = paymentHistoryEventResponse.status;
                    Optional<AppOrder> optional = repository.findById(orderId.intValue());
                    if(optional.isPresent()){
                        AppOrder order = optional.get();
                        switch(status.intValue()){
                            case 0:
                                order.getEthPayment().setStatus(EthPaymentStatus.PAYED);
                                break;
                            case 1:
                                order.getEthPayment().setStatus(EthPaymentStatus.REFUNDED);
                                break;
                            case 2:
                                order.getEthPayment().setStatus(EthPaymentStatus.CAPTURED);
                                break;
                        }
                        repository.save(order);
                    }
                });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }));
    }
}
