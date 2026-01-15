package com.example.doctorService;

import com.example.doctorService.entity.CreateDoctor;
import com.example.doctorService.entity.Doctor;
import com.example.doctorService.entity.WorkingHoursDTO;
import com.example.doctorService.repository.DoctorRepository;
import com.example.doctorService.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ServiceClassTests {
    @Mock
    DoctorRepository doctorRepository;

    @InjectMocks
    DoctorService doctorService;

    CreateDoctor makeCreateDoctorPayload(){
        return new CreateDoctor("Ada Wong",
                "Cardiology",
                69,
                12345678,
                new ArrayList<WorkingHoursDTO>());
    }

    @Test
    void createDoctorSavesEntityMappedFromPayload(){
        CreateDoctor createDoctorPayload= makeCreateDoctorPayload();
        ArgumentCaptor<Doctor> captor=ArgumentCaptor.forClass(Doctor.class);
        when(doctorRepository.save(any(Doctor.class)))
                .thenAnswer(inv-> inv.getArgument(0));
        doctorService.createDoctor(createDoctorPayload);
        verify(doctorRepository).save(captor.capture());
        Doctor doc=captor.getValue();
        assertThat(doc.getName())
                .isEqualTo(createDoctorPayload.name());
        assertThat(doc.getSpecialization())
                .isEqualTo(createDoctorPayload.specialization());
        assertThat(doc.getDepartmentId())
                .isEqualTo(createDoctorPayload.departmentId());
        assertThat(doc.getContactInformation()).
                isEqualTo(createDoctorPayload.contactInformation());
        List<WorkingHoursDTO> savedWorkingHoursDtos =
                doc.getWorkingHours().stream()
                .map(WorkingHoursDTO::dtoFromEntity)
                .toList();
        assertThat(savedWorkingHoursDtos)
                .containsExactlyElementsOf(createDoctorPayload.workingHours());
    }
}
