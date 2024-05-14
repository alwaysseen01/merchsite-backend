package com.github.alwaysseen.merchsite.payment.crypto;

import com.github.alwaysseen.merchsite.payment.crypto.contracts.PaymentContract;
import com.github.alwaysseen.merchsite.payment.crypto.response.CurrencyCourseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.response.NoOpProcessor;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

@Service
@Slf4j
public class EthPaymentService {
    @Value("${MAIN_WALLET}")
    private String MAIN_WALLET;
    @Value("${CONTRACT}")
    private String CONTRACT;
    @Value("${COINMARKET_API}")
    private String COINMARKET_API;

    private final String NET_URL = "HTTP://127.0.0.1:7545";
    private final BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);
    private final BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
    private final ContractGasProvider gasProvider = new StaticGasProvider(GAS_PRICE,
            GAS_LIMIT);
    private final Web3j web3j = Web3j.build(new HttpService(NET_URL));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private LocalDateTime convertTimeStamp = null;
    private double ethPrice;

    public PaymentContract deployContract() throws Exception {
        Credentials SHOP_ADDRESS = Credentials.create(ECKeyPair.create(new BigInteger(MAIN_WALLET.substring(2), 16)));
        return PaymentContract.deploy(web3j, SHOP_ADDRESS, gasProvider).send();
    }

    public PaymentContract loadContract(){
        Credentials SHOP_ADDRESS = Credentials.create(ECKeyPair.create(new BigInteger(MAIN_WALLET.substring(2), 16)));
        return PaymentContract.load(CONTRACT, web3j, SHOP_ADDRESS, gasProvider);
    }

    public TransactionReceipt capturePayment(int orderId, double amount, String address) throws IOException {
        PaymentContract contract = loadContract();
        long weiValue = (long)(amount / 2927 * Math.pow(10, 18));
        Credentials sender = Credentials.create(ECKeyPair.create(new BigInteger(address.substring(2), 16)));
        TransactionManager txSendr = new FastRawTransactionManager(web3j, sender, new NoOpProcessor(web3j));
        Function function = new Function(
                "capturePayment",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(orderId)),
                Collections.<TypeReference<?>>emptyList());
        String txFunction = FunctionEncoder.encode(function);

        EthSendTransaction transaction = txSendr.sendTransaction(
                GAS_PRICE,
                GAS_LIMIT,
                contract.getContractAddress(),
                txFunction,
                BigInteger.valueOf(weiValue));
        return web3j.ethGetTransactionReceipt(transaction.getTransactionHash())
                .send()
                .getTransactionReceipt()
                .get();
    }

    public TransactionReceipt refund(int orderId) throws IOException {
        Credentials SHOP_ADDRESS = Credentials.create(ECKeyPair.create(new BigInteger(MAIN_WALLET.substring(2), 16)));
        TransactionManager txSender = new FastRawTransactionManager(web3j, SHOP_ADDRESS);

        Function function = new Function(
                "refund",
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(orderId)),
                Collections.<TypeReference<?>>emptyList());
        String txFunction = FunctionEncoder.encode(function);

        PaymentContract contract = loadContract();

        EthSendTransaction transaction = txSender.sendTransaction(
                GAS_PRICE,
                GAS_LIMIT,
                contract.getContractAddress(),
                txFunction,
                BigInteger.ZERO
        );
        return web3j.ethGetTransactionReceipt(transaction.getTransactionHash())
                .send()
                .getTransactionReceipt()
                .get();
    }

    public double getEthCourse() {
        boolean makeRequest = true;
        if (convertTimeStamp != null) {
            long duration = Math.abs(Duration.between(LocalDateTime.now(), convertTimeStamp).toSeconds());
            log.info("DURATION = " + duration);
            if (duration < 7200) {
                log.info("REQUEST ISN'T NEEDED");
                makeRequest = false;
            }
        }
        if (makeRequest) {
            log.info("PERFORMING REQUEST TO *COIN MARKET CAP*");
            String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?symbol=ETH&convert=USD";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-CMC_PRO_API_KEY", COINMARKET_API);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<CurrencyCourseResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, CurrencyCourseResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("RESPONSE = " + response.getBody().toString());
                convertTimeStamp = LocalDateTime.parse(response.getBody().getStatus().getTimestamp(), formatter);
                ethPrice = response.getBody().getData().getEth().getQuote().getUsd().getPrice();
            }
        }
        return ethPrice;
    }
}
