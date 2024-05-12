package com.github.alwaysseen.merchsite.payment.crypto.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.3.
 */
@SuppressWarnings("rawtypes")
public class PaymentContract extends Contract {
    public static final String BINARY = "0x608060405234801561000f575f80fd5b50335f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555061083c8061005c5f395ff3fe608060405260043610610033575f3560e01c8063278ecde1146100375780636ac1939e1461005f5780636cee6d771461007b575b5f80fd5b348015610042575f80fd5b5061005d6004803603810190610058919061062b565b610091565b005b6100796004803603810190610074919061062b565b6102b1565b005b348015610086575f80fd5b5061008f6103fc565b005b5f5b6001805490508110156102ad575f60028111156100b3576100b2610656565b5b600182815481106100c7576100c6610683565b5b905f5260205f2090600502016004015f9054906101000a900460ff1660028111156100f5576100f4610656565b5b1480156101235750816001828154811061011257610111610683565b5b905f5260205f2090600502015f0154145b801561015d57506078600182815481106101405761013f610683565b5b905f5260205f209060050201600301544261015b91906106dd565b105b1561029a575f6001828154811061017757610176610683565b5b905f5260205f2090600502016001015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690505f600183815481106101bd576101bc610683565b5b905f5260205f2090600502016002015490508173ffffffffffffffffffffffffffffffffffffffff166108fc8290811502906040515f60405180830381858888f19350505050158015610212573d5f803e3d5ffd5b50600180848154811061022857610227610683565b5b905f5260205f2090600502016004015f6101000a81548160ff0219169083600281111561025857610257610656565b5b02179055507f389216e3d34e350266ae2c46e6a99133e4cf066b6aa77017b506730eaa47beb184600160405161028f929190610765565b60405180910390a150505b80806102a59061078c565b915050610093565b5050565b5f6040518060a001604052808381526020013373ffffffffffffffffffffffffffffffffffffffff1681526020013481526020014281526020015f60028111156102fe576102fd610656565b5b81525090507f389216e3d34e350266ae2c46e6a99133e4cf066b6aa77017b506730eaa47beb1825f604051610334929190610765565b60405180910390a1600181908060018154018082558091505060019003905f5260205f2090600502015f909190919091505f820151815f01556020820151816001015f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060408201518160020155606082015181600301556080820151816004015f6101000a81548160ff021916908360028111156103f1576103f0610656565b5b021790555050505050565b5f805b60018054905081101561058d5760786001828154811061042257610421610683565b5b905f5260205f209060050201600301544261043d91906106dd565b11801561049c57505f600281111561045857610457610656565b5b6001828154811061046c5761046b610683565b5b905f5260205f2090600502016004015f9054906101000a900460ff16600281111561049a57610499610656565b5b145b1561057a57600181815481106104b5576104b4610683565b5b905f5260205f20906005020160020154826104d091906107d3565b91506002600182815481106104e8576104e7610683565b5b905f5260205f2090600502016004015f6101000a81548160ff0219169083600281111561051857610517610656565b5b02179055507f389216e3d34e350266ae2c46e6a99133e4cf066b6aa77017b506730eaa47beb16001828154811061055257610551610683565b5b905f5260205f2090600502015f01546002604051610571929190610765565b60405180910390a15b80806105859061078c565b9150506103ff565b505f8054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc8290811502906040515f60405180830381858888f193505050501580156105f0573d5f803e3d5ffd5b5050565b5f80fd5b5f819050919050565b61060a816105f8565b8114610614575f80fd5b50565b5f8135905061062581610601565b92915050565b5f602082840312156106405761063f6105f4565b5b5f61064d84828501610617565b91505092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52602160045260245ffd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52603260045260245ffd5b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f6106e7826105f8565b91506106f2836105f8565b925082820390508181111561070a576107096106b0565b5b92915050565b610719816105f8565b82525050565b600381106107305761072f610656565b5b50565b5f8190506107408261071f565b919050565b5f61074f82610733565b9050919050565b61075f81610745565b82525050565b5f6040820190506107785f830185610710565b6107856020830184610756565b9392505050565b5f610796826105f8565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82036107c8576107c76106b0565b5b600182019050919050565b5f6107dd826105f8565b91506107e8836105f8565b9250828201905080821115610800576107ff6106b0565b5b9291505056fea264697066735822122061d3ce2f97ea545fc0caa36ad2f37cfa01702e8c0a817476dbf491df4649261164736f6c63430008150033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CAPTUREPAYMENT = "capturePayment";

    public static final String FUNC_REFUND = "refund";

    public static final String FUNC_TRANSFERMONEY = "transferMoney";

    public static final Event PAYMENTHISTORY_EVENT = new Event("PaymentHistory", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    protected PaymentContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PaymentContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PaymentContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PaymentContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<PaymentHistoryEventResponse> getPaymentHistoryEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PAYMENTHISTORY_EVENT, transactionReceipt);
        ArrayList<PaymentHistoryEventResponse> responses = new ArrayList<PaymentHistoryEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PaymentHistoryEventResponse typedResponse = new PaymentHistoryEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.orderId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.status = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PaymentHistoryEventResponse getPaymentHistoryEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PAYMENTHISTORY_EVENT, log);
        PaymentHistoryEventResponse typedResponse = new PaymentHistoryEventResponse();
        typedResponse.log = log;
        typedResponse.orderId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.status = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<PaymentHistoryEventResponse> paymentHistoryEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getPaymentHistoryEventFromLog(log));
    }

    public Flowable<PaymentHistoryEventResponse> paymentHistoryEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PAYMENTHISTORY_EVENT));
        return paymentHistoryEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> send_capturePayment(BigInteger orderId, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_CAPTUREPAYMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(orderId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> send_refund(BigInteger orderId) {
        final Function function = new Function(
                FUNC_REFUND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(orderId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> send_transferMoney() {
        final Function function = new Function(
                FUNC_TRANSFERMONEY, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static PaymentContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PaymentContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PaymentContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PaymentContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PaymentContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new PaymentContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PaymentContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PaymentContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PaymentContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PaymentContract.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<PaymentContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PaymentContract.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<PaymentContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(PaymentContract.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<PaymentContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(PaymentContract.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    public static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class PaymentHistoryEventResponse extends BaseEventResponse {
        public BigInteger orderId;

        public BigInteger status;
    }
}
