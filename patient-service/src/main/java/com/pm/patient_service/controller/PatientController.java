package com.pm.patient_service.controller;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.dto.validators.CreatePatientValidationGroup;
import com.pm.patient_service.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.zip.Deflater;

@RestController
@RequestMapping("/patient")
@Tag(name = "Patient", description = "API for managing patient records")
public class PatientController
{
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieve a list of all patient records")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        List<PatientResponseDTO> patients = patientService.getAllPatients();
        return ResponseEntity.ok().body(patients);
    }
    @PostMapping
    @Operation(summary = "Create a new patient", description = "Add a new patient record to the system")
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Deflater.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO){
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing patient", description = "Modify an existing patient record")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id, @Validated({Deflater.class}) @RequestBody PatientRequestDTO patientRequestDTO){
        PatientResponseDTO patientResponseDTO = patientService.updatePatient(patientRequestDTO, id);
        return ResponseEntity.ok().body(patientResponseDTO);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a patient", description = "Remove a patient record from the system")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id){
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }
}
