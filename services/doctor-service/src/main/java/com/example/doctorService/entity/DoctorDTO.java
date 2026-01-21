package com.example.doctorService.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.ArrayList;
import java.util.List;


public record DoctorDTO (
        @Null(groups = CreateInstance.class)
        @NotNull(groups = UpdateInstance.class)
        Long id,
        @NotNull String name,
        @NotNull String specialization,
        @NotNull Integer departmentId,
        @NotNull Integer contactInformation,
        @NotNull List<WorkingHoursDTO> workingHours){

    public interface CreateInstance{}
    public interface UpdateInstance{}
    public static DoctorDTO dtoFromEntity(Doctor doctor){
        List<WorkingHoursDTO> workingHoursDTOS= new ArrayList<>();
        for(WorkingHours wh: doctor.getWorkingHours())
            workingHoursDTOS.add(WorkingHoursDTO.dtoFromEntity(wh));
        return new DoctorDTO(doctor.getDoctorId(),
                doctor.getName(),
                doctor.getSpecialization(),
                doctor.getDepartmentId(),
                doctor.getContactInformation(),
                workingHoursDTOS);
    }
    public Doctor entityFromDto(){
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
