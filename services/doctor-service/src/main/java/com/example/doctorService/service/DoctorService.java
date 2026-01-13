package com.example.doctorService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.doctorService.entity.*;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.doctorService.repository.DoctorRepository;

@Service
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public void creatDoctor(CreateDoctor doctor) {
        doctorRepository.save(doctor.entityFromDoctor());
    }

    public List<DoctorDTO> listDoctors() {
        List<Doctor> doctorList= doctorRepository.findAll();
        List<DoctorDTO> doctorDTOList=new ArrayList<>();
        for(Doctor doc:doctorList){
            doctorDTOList.add(DoctorDTO.dtoFromEntity(doc));
        }
        return doctorDTOList;
    }

    public DoctorDTO getDoctor(int id) {
        Doctor doctor= doctorRepository.findById(id).orElseThrow(
                ()-> new NotFoundException("Doctor not found."));
        return DoctorDTO.dtoFromEntity(doctor);
    }

    public Doctor updateDoctor(int id, CreateDoctor doctor) {
        Doctor doc = doctorRepository.findById(id).orElseThrow(()->
                new NotFoundException("Doctor not found."));
        doc.setName(doctor.name());
        doc.setSpecialization(doctor.specialization());
        doc.setDepartmentId(doctor.departmentId());
        doc.setContactInformation(doctor.contactInformation());
        return doctorRepository.save(doc);
    }

    public void deleteDoctor(int id) {
        doctorRepository.deleteById(id);
    }

    public List<Doctor> searchDoctorsBySpecialization(String name) {
        return doctorRepository.findBySpecialization(name);
    }

    public List<Doctor> searchDoctorByDepartment(int departmentId) {
        return doctorRepository.findByDepartment(departmentId);
    }

    public List<WorkingHoursDTO> getWorkingHours(int id){
        Doctor doctor= doctorRepository.findById(id).orElseThrow(NotFoundException::new);
        List<WorkingHoursDTO> workingHours=new ArrayList<>();
        for(WorkingHours wh: doctor.getWorkingHours())
            workingHours.add(WorkingHoursDTO.dtoFromEntity(wh));
        return workingHours;
    }

    public void setWorkingHours(int id,List<WorkingHoursDTO> workingHoursDtos){
        Doctor doctor= doctorRepository.findById(id).orElseThrow(NotFoundException::new);
        List<WorkingHours> whs=doctor.getWorkingHours();
        whs.clear();
        for(WorkingHoursDTO whd:workingHoursDtos)
            whs.add(whd.entityFromDto(doctor));
        doctorRepository.save(doctor);
    }
}
