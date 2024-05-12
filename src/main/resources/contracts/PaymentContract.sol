// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract PaymentContract{
    enum Status{
        PAYED,
        REFUNDED,
        CAPTURED
    }

    struct Transaction{
        uint orderId;
        address payer;
        uint value;
        uint timestamp;
        Status status;
    }

    event PaymentHistory(uint orderId, Status status);

    address payable owner;
    Transaction[] internal transactions;


    constructor(){
        owner = payable(msg.sender);
    }

    function capturePayment(uint orderId) external payable {
        Transaction memory transaction = Transaction(orderId, msg.sender, msg.value, block.timestamp, Status.PAYED);
        emit PaymentHistory(orderId, Status.PAYED);
        transactions.push(transaction);
    }

    function refund(uint orderId) external {
        for(uint i=0;i<transactions.length;i++){
            if(transactions[i].status == Status.PAYED
            && transactions[i].orderId == orderId
                && block.timestamp - transactions[i].timestamp < 120){
                address payable _to = payable(transactions[i].payer);
                uint value = transactions[i].value;
                _to.transfer(value);
                transactions[i].status = Status.REFUNDED;
                emit PaymentHistory(orderId, Status.REFUNDED);
            }
        }
    }

    function transferMoney() external {
        uint amount = 0;
        for(uint i=0;i<transactions.length;i++){
            if(block.timestamp - transactions[i].timestamp > 120
                && transactions[i].status == Status.PAYED){
                amount += transactions[i].value;
                transactions[i].status = Status.CAPTURED;
                emit PaymentHistory(transactions[i].orderId, Status.CAPTURED);
            }
        }
        owner.transfer(amount);
    }
}