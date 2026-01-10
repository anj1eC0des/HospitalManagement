package com.example.patientService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.patientService.entity.Patient;
import com.example.patientService.entity.PatientDTO;
import com.example.patientService.repository.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient creatPatient(PatientDTO patient) {
        Patient p = new Patient();
        p.setName(patient.getName());
        p.setAge(patient.getAge());
        p.setGender(patient.getGender());
        p.setNumber(patient.getNumber());
        p.setAddress(patient.getAddress());
        return patientRepository.save(p);
    }

    public List<Patient> listPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatient(int id) {
        return patientRepository.findById(id);
    }

    public Patient updatePatient(int id, PatientDTO patient) {
        Patient p = new Patient();
        p.setPatientId(id);
        p.setName(patient.getName());
        p.setAge(patient.getAge());
        p.setGender(patient.getGender());
        p.setNumber(patient.getNumber());
        p.setAddress(patient.getAddress());
        return patientRepository.save(p);
    }

    public void deletePatient(int id) {
        patientRepository.deleteById(id);
    }

    public List<Patient> searchPatient(String name) {
        return patientRepository.findPatientByName(name);
    }

}
