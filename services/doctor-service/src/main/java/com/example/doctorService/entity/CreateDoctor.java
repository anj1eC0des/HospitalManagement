package com.example.doctorService.entity;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public record CreateDoctor(
        @NotNull String name,
        @NotNull String specialization,
        @NotNull int departmentId,
        @NotNull int contactInformation,
        @NotNull List<WorkingHoursDTO> workingHours) {
    public static CreateDoctor dtoFromEntity(Doctor doctor){
        List<WorkingHoursDTO> whDtos=new ArrayList<>();
        for(WorkingHours whs: doctor.getWorkingHours())
            whDtos.add(WorkingHoursDTO.dtoFromEntity(whs));
        return new CreateDoctor(doctor.getName(),
                doctor.getSpecialization(),
                doctor.getDepartmentId(),
                doctor.getContactInformation(),
                whDtos);
    }
    public Doctor entityFromDoctor(){
        Doctor doctor= new Doctor();
        List<WorkingHours> whs=new ArrayList<>();
        for(WorkingHoursDTO whDto: this.workingHours()){
            whs.add(whDto.entityFromDto(doctor));
        }
        doctor.setWorkingHours(whs);
        doctor.setName(this.name());
        doctor.setSpecialization(this.specialization());
        doctor.setContactInformation(this.contactInformation());
        doctor.setDepartmentId(this.departmentId());
        return doctor;
    }
}
