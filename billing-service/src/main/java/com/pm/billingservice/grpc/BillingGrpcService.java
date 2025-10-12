package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest billingRequest, StreamObserver<BillingResponse> responseObserver) {
      log.info("Received billing request: {}", billingRequest);
      //Business logic to create billing account
      BillingResponse billingResponse = BillingResponse.newBuilder()
              .setAccountId("1234567890")
              .setStatus("ACTIVE")
              .build();
      responseObserver.onNext(billingResponse);
      responseObserver.onCompleted();

    }
}
