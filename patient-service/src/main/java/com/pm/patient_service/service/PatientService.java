package com.pm.patient_service.service;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.exception.EmailAlreadyExistsException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.grpc.BillingServiceGrcpClient;
import com.pm.patient_service.kafka.KafkaProducer;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceGrcpClient billingServiceGrcpClient;
    private final KafkaProducer kafkaProducer;

    public PatientService(
             PatientRepository patientRepository,
             BillingServiceGrcpClient billingServiceGrcpClient,
             KafkaProducer kafkaProducer
    ) {
        this.patientRepository = patientRepository;
        this.billingServiceGrcpClient = billingServiceGrcpClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
/*      List<PatientResponseDTO> patientResponseDTOS = patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();*/
        return patients.stream().map(PatientMapper::toDTO).toList();
    }
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
          throw new EmailAlreadyExistsException("A patient width this email "+ "already exists"+ patientRequestDTO.getEmail());
        }
        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        billingServiceGrcpClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());
        kafkaProducer.sendEvent(newPatient);
        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(PatientRequestDTO patientRequestDTO, UUID id){
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)){
            throw new EmailAlreadyExistsException("A patient width this email "+ "already exists"+ patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        return PatientMapper.toDTO(patientRepository.save(patient));
    }

    public void deletePatient(UUID id){
        patientRepository.deleteById(id);
    }
}
