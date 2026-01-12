package com.example.doctorService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.doctorService.entity.WorkingHours;
import com.example.doctorService.entity.WorkingHoursDto;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.doctorService.entity.Doctor;
import com.example.doctorService.entity.DoctorDTO;
import com.example.doctorService.repository.DoctorRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor creatDoctor(DoctorDTO doctor) {
        Doctor doc = new Doctor();
        doc.setName(doctor.getName());
        doc.setSpecialization(doctor.getSpecialization());
        doc.setDepartmentId(doctor.getDepartmentId());
        doc.setContactInformation(doctor.getContactInformation());
        return doctorRepository.save(doc);
    }

    public List<Doctor> listDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctor(int id) {

        return doctorRepository.findById(id);
    }

    public Doctor updateDoctor(int id, DoctorDTO doctor) {
        Doctor doc = new Doctor();
        doc.setDoctorId(id);
        doc.setName(doctor.getName());
        doc.setSpecialization(doctor.getSpecialization());
        doc.setDepartmentId(doctor.getDepartmentId());
        doc.setContactInformation(doctor.getContactInformation());
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

    public List<WorkingHoursDto> getWorkingHours(int id){
        Doctor doctor= doctorRepository.findById(id).orElseThrow(NotFoundException::new);
        List<WorkingHoursDto> workingHours=new ArrayList<>();
        for(WorkingHours wh: doctor.getWorkingHours()){
            workingHours.add(new WorkingHoursDto(wh.getDayOfTheWeek(),
                    wh.getStartTime(),
                    wh.getEndTime()));
        }
        return workingHours;
    }

    public void setWorkingHours(int id,List<WorkingHoursDto> workingHoursDtos){
        Doctor doctor= doctorRepository.findById(id).orElseThrow(NotFoundException::new);
        List<WorkingHours> workingHours=new ArrayList<>();
        for(WorkingHoursDto whd:workingHoursDtos){
            WorkingHours wh=new WorkingHours();
            wh.setDoctor(doctor);
            wh.setDayOfTheWeek(whd.getDayOfTheWeek());
            wh.setStartTime(whd.getStartTime());
            wh.setEndTime(whd.getEndTime());
            workingHours.add(wh);
        }
        doctor.setWorkingHours(workingHours);
        doctorRepository.save(doctor);
    }
}
