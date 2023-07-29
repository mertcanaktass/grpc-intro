package com.mca.server.rpctypes;

import com.mca.models.Account;
import com.mca.models.TransferRequest;
import com.mca.models.TransferResponse;
import com.mca.models.TransferStatus;
import io.grpc.stub.StreamObserver;

public class TransferStreamingRequest implements StreamObserver<com.mca.models.TransferRequest> {

    private StreamObserver<com.mca.models.TransferResponse> transferResponseStreamObserver;

    public TransferStreamingRequest(StreamObserver<TransferResponse> transferResponseStreamObserver) {
        this.transferResponseStreamObserver = transferResponseStreamObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {
        int fromAccount = transferRequest.getFromAccount();
        int toAccount = transferRequest.getToAccount();
        int amount = transferRequest.getAmount();
        int balance = AccountDatabase.getBalance(fromAccount);
        com.mca.models.TransferStatus status = TransferStatus.FAILED;

        if (balance >= amount && fromAccount != toAccount) {
        AccountDatabase.deductBalance(fromAccount, amount);
        AccountDatabase.addBalance(toAccount, amount);
        status = TransferStatus.SUCCESS;
        }
        Account fromAccountInfo = Account.newBuilder().setAccountNumber(fromAccount).setAmount(AccountDatabase.getBalance(fromAccount)).build();
        Account toAccountInfo = Account.newBuilder().setAccountNumber(toAccount).setAmount(AccountDatabase.getBalance(toAccount)).build();
        TransferResponse transferResponse = TransferResponse.newBuilder()
                .setStatus(status)
                .addAccounts(fromAccountInfo)
                .addAccounts(toAccountInfo)
                .build();
        this.transferResponseStreamObserver.onNext(transferResponse);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        AccountDatabase.printAccountDetails();
        this.transferResponseStreamObserver.onCompleted();
    }
}
