package com.example.patientService.controller;

import java.util.List;

import com.example.patientService.entity.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.patientService.service.PatientService;

@RestController
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/patients")
    @ResponseBody
    public PatientDTO createPatient(@RequestBody PatientDTO patient) {
        return patientService.creatPatient(patient);
    }

    @GetMapping("/patients")
    @ResponseBody
    public List<PatientDTO> getPatients() {
        return patientService.listPatients();
    }

    @GetMapping("/patients/{id}")
    @ResponseBody
    public PatientDTO getPatients(@PathVariable int id) {
        return patientService.getPatient(id);
    }

    @PutMapping("/patients/{id}")
    @ResponseBody
    public PatientDTO updatePatients(@RequestBody PatientDTO patient, @PathVariable int id) {
        return patientService.updatePatient(id, patient);
    }

    @GetMapping("/patients/{id}/patientHistory")
    public List<PatientHistoryDto> getPatientHistory(@PathVariable int id){
        return patientService.getPatientHistory(id);
    }

    @PostMapping("/patients/{id}/patientHistory")
    public List<PatientHistoryDto> setPatientHistory(@PathVariable int id,
                @RequestBody List<PatientHistoryDto> patientHistoryDtos){
        return patientService.updatePatientHistory(id,patientHistoryDtos);
    }

    @DeleteMapping("patients/{id}")
    public void deletePatient(@PathVariable int id) {
        patientService.deletePatient(id);
    }

    @GetMapping("/search?name={name}")
    @ResponseBody
    public List<PatientDTO> searchPatients(@PathVariable String name) {
        return patientService.searchPatient(name);
    }
}
