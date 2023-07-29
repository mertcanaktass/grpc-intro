package com.mca.server.rpctypes;

import com.mca.models.Balance;
import com.mca.models.BalanceCheckRequest;
import com.mca.models.Money;
import com.mca.models.WithdrawRequest;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class BankService extends com.mca.models.BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {

        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder()
                .setAmount(AccountDatabase.getBalance(accountNumber))
                .build();
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();
        int balance = AccountDatabase.getBalance(accountNumber);

        if (balance < amount) {
            Status status = Status.FAILED_PRECONDITION.withDescription("No enough money. You have only " + balance);
            responseObserver.onError(status.asRuntimeException());
            return;
        }

        // all the validations passed
        for (int i = 0; i < (amount/10); i++) {
            Money money = Money.newBuilder().setValue(10).build();
            responseObserver.onNext(money);
            AccountDatabase.deductBalance(accountNumber, 10);
        }

        responseObserver.onCompleted();

    }

    @Override
    public StreamObserver<com.mca.models.DepositRequest> cashDeposit(StreamObserver<Balance> responseObserver) {
        return new CashStreamingRequest(responseObserver);
    }



}
