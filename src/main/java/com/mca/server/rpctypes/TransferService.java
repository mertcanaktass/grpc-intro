package com.mca.server.rpctypes;

import com.mca.models.TransferRequest;
import com.mca.models.TransferResponse;
import io.grpc.stub.StreamObserver;

public class TransferService extends com.mca.models.TransferServiceGrpc.TransferServiceImplBase {

    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferStreamingRequest(responseObserver);
    }
}
