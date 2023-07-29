package com.mca.server.rpctypes;

import com.mca.models.Balance;
import com.mca.models.DepositRequest;
import io.grpc.stub.StreamObserver;

public class CashStreamingRequest implements StreamObserver<com.mca.models.DepositRequest> {

    private StreamObserver<com.mca.models.Balance> balanceStreamObserver;
    private int accountBalance;

    public CashStreamingRequest(StreamObserver<Balance> balanceStreamObserver) {
        this.balanceStreamObserver = balanceStreamObserver;
    }

    @Override
    public void onNext(DepositRequest depositRequest) {
        int accountNumber = depositRequest.getAccountNumber();
        int amount = depositRequest.getAmount();
        this.accountBalance = AccountDatabase.addBalance(accountNumber, amount);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        Balance balance = Balance.newBuilder().setAmount(this.accountBalance).build();
        this.balanceStreamObserver.onNext(balance);
        this.balanceStreamObserver.onCompleted();
    }
}
