package com.example.doctorService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.doctorService.entity.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @Query("SELECT d FROM Doctor d WHERE d.specialization LIKE :specialization")
    public List<Doctor> findBySpecialization(String specialization);

    @Query("SELECT d FROM Doctor d WHERE d.departmentId = :departmentId")
    public List<Doctor> findByDepartment(int departmentId);
}
