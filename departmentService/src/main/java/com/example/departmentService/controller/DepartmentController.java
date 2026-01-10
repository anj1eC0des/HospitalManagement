package com.example.departmentService.controller;

import com.example.departmentService.entity.Department;
import com.example.departmentService.entity.DepartmentDTO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DepartmentController {

    @PostMapping("/departments")
    @ResponseBody
    public String createDepartment(@RequestBody DepartmentDTO departments){
        return "New Department created";
    }

    @GetMapping("/departments")
    @ResponseBody
    public List<Department> getDepartments(){
        return new ArrayList<Department>();
    }

    @GetMapping("/departments/{id}")
    @ResponseBody
    public Department getDepartments(@PathVariable int id){
        return new Department();
    }

    @PutMapping("/departments/{id}")
    @ResponseBody
    public Department updateDepartments(@RequestBody DepartmentDTO doctor, @PathVariable int id){
        return new Department();
    }

    @DeleteMapping("departments/{id}")
    @ResponseBody
    public String deleteDepartment(@PathVariable int id){
        return "Department deleted";
    }
}
