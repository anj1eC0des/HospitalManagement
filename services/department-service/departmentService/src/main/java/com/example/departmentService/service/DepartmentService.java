package com.example.departmentService.service;

import com.example.departmentService.entity.Department;
import com.example.departmentService.entity.DepartmentDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    public Department creatDepartment(DepartmentDTO patient){
        //jpa save - repo.save(patient)
        return new Department();
    }

    public List<Department> listDepartments(){
        // repo.listDepartments;
        return new ArrayList<>();
    }

    public Department getDepartment(int id){
        //patient find by id
        return new Department();
    }

    public Department updateDepartment(int id, DepartmentDTO patient){
        return new Department();
    }

    public int deleteDepartment(int id){
        return 0;
    }
}
