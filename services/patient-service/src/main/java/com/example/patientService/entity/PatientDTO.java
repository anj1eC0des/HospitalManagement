package com.example.patientService.entity;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.ArrayList;
import java.util.List;

public record PatientDTO (
    @NotNull(groups = UpdateInstance.class)
    @Null(groups = CreateInstance.class)
    Long id,
    String name,
    int age,
    String gender,
    int number,
    String address,
    List<PatientHistoryDto> patientHistory){
    public interface CreateInstance{}
    public interface UpdateInstance{}
    public static PatientDTO getDtoFromEntity(Patient patient){
        List<PatientHistoryDto> patientHistoryDtos= patient.getPatientHistoryList()
                .stream().map(PatientHistoryDto::getDtoFromEntity).toList();
        return new PatientDTO(patient.getPatientId(),
                patient.getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getNumber(),
                patient.getAddress(),
                patientHistoryDtos);
    }
    public Patient getEntityFromDto(){
        Patient patient=new Patient();
        List<PatientHistory> patientHistoryList=new ArrayList<>();
        for(PatientHistoryDto phdto:this.patientHistory()){
            patientHistoryList.add(phdto.getEntityFromDto(patient));
        }
        patient.setName(this.name());
        patient.setAge(this.age());
        patient.setGender(this.gender());
        patient.setNumber(this.number());
        patient.setAddress(this.address());
        patient.setPatientHistoryList(patientHistoryList);
        return patient;
    }
}
