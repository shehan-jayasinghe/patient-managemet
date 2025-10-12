package com.pm.patient_service.kafka;

import com.pm.patient_service.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    //KafkaTemplate is used to send messages to a Kafka topic strin byte[] is the format of the message
    // strin jason strin string etc.
    //spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
    //spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.ByteArrayDeserializer

    private final KafkaTemplate<String,byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String,byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
   public void sendEvent(Patient patient) {
       PatientEvent patientEvent = PatientEvent.newBuilder()
               .setPatientId(patient.getId().toString())
               .setName(patient.getName())
               .setEmail(patient.getEmail())
               .setEventType("PATIENT_CREATED")
               .build();
       try {
           kafkaTemplate.send("patient-events", patientEvent.toByteArray());
       } catch (Exception e) {
           log.error("Error while sending patient event to kafka", e);
       }
    }
}
