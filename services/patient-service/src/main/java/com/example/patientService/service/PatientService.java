package com.example.patientService.service;

import java.util.List;
import com.example.patientService.entity.ResponsePatientDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.patientService.entity.Patient;
import com.example.patientService.entity.PatientDTO;
import com.example.patientService.repository.PatientRepository;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public ResponsePatientDto creatPatient(PatientDTO patient) {
        Patient p= patientRepository.save(patient.getEntityFromDto());
        log.info("Patient fetched. {} {}",p.getPatientId(),p.getName());
        return ResponsePatientDto.getDtoFromEntity(p);

    }

    public List<ResponsePatientDto> listPatients() {
        List<Patient> patients=patientRepository.findAll();
        log.info("Patient list fetched.");
        return patients.stream()
                .map(ResponsePatientDto::getDtoFromEntity).toList();
    }

    public ResponsePatientDto getPatient(int id){
        Patient patient= patientRepository.findById(id).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Patient Not Found.")
        );
        log.info("Patient fetched. {} {}",patient.getPatientId(),patient.getName());
        return ResponsePatientDto.getDtoFromEntity(patient);
    }

    public ResponsePatientDto updatePatient(int id, PatientDTO patient) {
        Patient p= patientRepository.findById(id).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Patient Not Found.")
        );
        log.info("Patient fetched. {} {}",p.getPatientId(),p.getName());
        p.setName(patient.name());
        p.setAge(patient.age());
        p.setGender(patient.gender());
        p.setNumber(patient.number());
        p.setAddress(patient.address());
        Patient savedPatient= patientRepository.save(p);
        return ResponsePatientDto.getDtoFromEntity(savedPatient);
    }

    public void deletePatient(int id) {
        patientRepository.deleteById(id);
    }

    public List<ResponsePatientDto> searchPatient(String name) {
        return patientRepository.findPatientByName(name)
                .stream().map(ResponsePatientDto::getDtoFromEntity).toList();
    }

}
