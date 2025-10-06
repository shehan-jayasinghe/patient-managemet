package com.pm.patient_service.mapper;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.model.Patient;

import java.time.LocalDate;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient) {
       PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
       patientResponseDTO.setId(patient.getId() != null ? patient.getId().toString() : null);
       patientResponseDTO.setName(patient.getName());
       patientResponseDTO.setEmail(patient.getEmail());
       patientResponseDTO.setAddress(patient.getAddress());
       patientResponseDTO.setDateOfBirth(patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : null);

       return patientResponseDTO;
    }
    public static Patient toModel(PatientRequestDTO requestDTO){
        Patient patient = new Patient();
        patient.setName(requestDTO.getName());
        patient.setEmail(requestDTO.getEmail());
        patient.setAddress(requestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(requestDTO.getDateOfBirth()));
        // Registered date is required in request; parse and set it
        patient.setRegisteredDate(LocalDate.parse(requestDTO.getRegisteredDate()));
        return patient;
    }
}
