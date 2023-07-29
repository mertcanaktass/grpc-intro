package com.mca.client.rpctypes;

import com.mca.models.Money;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class MoneyStreamingResponse implements StreamObserver<com.mca.models.Money> {

    private CountDownLatch latch;

    public MoneyStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(Money money) {
        System.out.println(
                "Received async : " + money.getValue()
        );
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(
                throwable.getMessage()
        );
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println(
                "Serves is done!"
        );
        latch.countDown();
    }
}
