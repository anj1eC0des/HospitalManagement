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

    public void createDoctor(CreateDoctor doctor) {
        Doctor doc=doctorRepository.save(doctor.entityFromDoctor());
        log.info("Doctor created. {},{},{},{}",
                doc.getDoctorId(),
                doc.getName(),
                doc.getDepartmentId(),
                doc.getSpecialization());
    }

    public List<DoctorDTO> listDoctors() {
        List<Doctor> doctorList= doctorRepository.findAll();
        log.info("Doctor list fetched.");
        List<DoctorDTO> doctorDTOList=new ArrayList<>();
        for(Doctor doc:doctorList){
            doctorDTOList.add(DoctorDTO.dtoFromEntity(doc));
        }
        return doctorDTOList;
    }

    public DoctorDTO getDoctor(int id) {
        Doctor doc= doctorRepository.findById(id).orElseThrow(
                ()-> new NotFoundException("Doctor not found."));
        log.info("Doctor fetched. {},{},{},{}",
                doc.getDoctorId(),
                doc.getName(),
                doc.getDepartmentId(),
                doc.getSpecialization());
        return DoctorDTO.dtoFromEntity(doc);
    }

    public DoctorDTO updateDoctor(int id, CreateDoctor doctor) {
        Doctor doc = doctorRepository.findById(id).orElseThrow(()->
                new NotFoundException("Doctor not found."));
        doc.setName(doctor.name());
        doc.setSpecialization(doctor.specialization());
        doc.setDepartmentId(doctor.departmentId());
        doc.setContactInformation(doctor.contactInformation());
        Doctor d=doctorRepository.save(doc);
        log.info("Doctor updated. {},{},{},{}",
                doc.getDoctorId(),
                doc.getName(),
                doc.getDepartmentId(),
                doc.getSpecialization());
        return DoctorDTO.dtoFromEntity(d);
    }

    public void deleteDoctor(int id) {
        doctorRepository.deleteById(id);
        log.info("Doctor with id= {} deleted.",id);
    }

    public List<DoctorDTO> searchDoctorsBySpecialization(String name) {
        List<Doctor> doctorList= doctorRepository.findBySpecialization(name);
        log.info("Doctor list by specialisation {} fetched.",name);
        List<DoctorDTO> doctorDTOList=new ArrayList<>();
        for(Doctor doc:doctorList){
            doctorDTOList.add(DoctorDTO.dtoFromEntity(doc));
        }
        return doctorDTOList;
    }

    public List<DoctorDTO> searchDoctorByDepartment(int departmentId) {
        List<Doctor> doctorList= doctorRepository.findByDepartment(departmentId);
        log.info("Doctor list by departmentId {} fetched.",departmentId);
        List<DoctorDTO> doctorDTOList=new ArrayList<>();
        for(Doctor doc:doctorList){
            doctorDTOList.add(DoctorDTO.dtoFromEntity(doc));
        }
        return doctorDTOList;
    }

    public List<WorkingHoursDTO> getWorkingHours(int id){
        Doctor doctor= doctorRepository.findById(id).orElseThrow(NotFoundException::new);
        log.info("Schedule for doctorId= {} fetched.",id);
        List<WorkingHoursDTO> workingHours=new ArrayList<>();
        for(WorkingHours wh: doctor.getWorkingHours())
            workingHours.add(WorkingHoursDTO.dtoFromEntity(wh));
        return workingHours;
    }

    public void setWorkingHours(int id,List<WorkingHoursDTO> workingHoursDtos){
        Doctor doctor= doctorRepository.findById(id).orElseThrow(NotFoundException::new);
        log.info("Schedule for doctorId= {} updated.",id);
        List<WorkingHours> whs=doctor.getWorkingHours();
        whs.clear();
        for(WorkingHoursDTO whd:workingHoursDtos)
            whs.add(whd.entityFromDto(doctor));
        doctorRepository.save(doctor);
    }
}
