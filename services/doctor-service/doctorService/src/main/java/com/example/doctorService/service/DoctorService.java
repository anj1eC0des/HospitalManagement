package com.example.doctorService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
