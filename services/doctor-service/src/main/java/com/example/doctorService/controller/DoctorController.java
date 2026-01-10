package com.example.doctorService.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorService.entity.Doctor;
import com.example.doctorService.entity.DoctorDTO;
import com.example.doctorService.service.DoctorService;

@RestController
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/doctors")
    @ResponseBody
    public Doctor createDoctor(@RequestBody DoctorDTO doctor) {
        return doctorService.creatDoctor(doctor);
    }

    @GetMapping("/doctors")
    @ResponseBody
    public List<Doctor> getDoctors() {
        return doctorService.listDoctors();
    }

    @GetMapping("/doctors/{id}")
    @ResponseBody
    public Optional<Doctor> getDoctors(@PathVariable int id) {
        return doctorService.getDoctor(id);
    }

    @PutMapping("/doctors/{id}")
    @ResponseBody
    public Doctor updateDoctors(@RequestBody DoctorDTO doctor, @PathVariable int id) {
        return doctorService.updateDoctor(id, doctor);
    }

    @DeleteMapping("doctors/{id}")
    @ResponseBody
    public void deleteDoctor(@PathVariable int id) {
        doctorService.deleteDoctor(id);
    }

    @GetMapping("/doctors/specialization/{name}")
    @ResponseBody
    public List<Doctor> searchDoctorsBySpecialization(@PathVariable String name) {
        return doctorService.searchDoctorsBySpecialization(name);
    }

    @GetMapping("/doctors/department/{departmentId}")
    @ResponseBody
    public List<Doctor> searchDoctorsByDepartment(@PathVariable int departmentID) {
        return doctorService.searchDoctorByDepartment(departmentID);
    }

}
