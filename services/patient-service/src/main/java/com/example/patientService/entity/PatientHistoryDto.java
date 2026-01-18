package com.example.patientService.entity;

import java.time.LocalDate;

public record PatientHistoryDto (
        String conditionName,
        LocalDate diagnosedDate,
        boolean isActive){
    public static PatientHistoryDto getDtoFromEntity(PatientHistory patientHistory){
        return new PatientHistoryDto(
                patientHistory.getConditionName(),
                patientHistory.getDiagnosedDate(),
                patientHistory.isActive()
        );
    }
    public PatientHistory getEntityFromDto(){
        PatientHistory patientHistory=new PatientHistory();
        patientHistory.setConditionName(this.conditionName());
        patientHistory.setDiagnosedDate(this.diagnosedDate());
        patientHistory.setActive(this.isActive());
        return patientHistory;
    }
}
