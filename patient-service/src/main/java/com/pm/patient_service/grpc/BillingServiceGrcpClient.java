package com.pm.patient_service.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

@Service
public class BillingServiceGrcpClient {
    
      private final BillingServiceGrpc.BillingServiceBlockingStub billingServiceBlockingStub;
      private static final Logger log = LoggerFactory.getLogger(BillingServiceGrcpClient.class);

      public BillingServiceGrcpClient(@Value("$billing.server.address:localhost") String billingServerAddress,
                                      @Value("${billing.server.grpc.port:9001}") int grpcServerPort
      ) {

          log.info("Connecting to billing service at {}:{}", billingServerAddress, grpcServerPort);
          ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(billingServerAddress, grpcServerPort).usePlaintext().build();
          billingServiceBlockingStub = BillingServiceGrpc.newBlockingStub(managedChannel);
      }
      
      public BillingResponse createBillingAccount(String patientId, String name, String email){
          log.info("Creating billing account for patient: {}", patientId);
          BillingRequest billingRequest = BillingRequest.newBuilder()
                  .setPatientId(patientId)
                  .setName(name)
                  .setEmail(email)
                  .build();
          BillingResponse billingResponse= billingServiceBlockingStub.createBillingAccount(billingRequest);
          log.info("Billing account created for patient: {}", billingResponse);
          return billingResponse;
      }
}
