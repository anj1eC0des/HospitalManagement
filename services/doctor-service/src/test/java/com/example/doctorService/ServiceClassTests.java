package com.example.doctorService;

import com.example.doctorService.entity.*;
import com.example.doctorService.repository.DoctorRepository;
import com.example.doctorService.service.DoctorService;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import javax.print.Doc;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    Doctor makeDoctor(int id){
        Doctor d=new Doctor(id,
                "dummy"+id,
                "dummy spec",
                100+id,
                12345,
                new ArrayList<>());
        List<WorkingHours> workingHours= new ArrayList<>(List.of(
                new WorkingHours(7+id,DayOfWeek.MONDAY,
                        LocalTime.of(9,0),
                        LocalTime.of(17,0),
                        d
                ),
                new WorkingHours(10+id,DayOfWeek.THURSDAY,
                        LocalTime.of(9,0),
                        LocalTime.of(17,0),
                        d
                )
        ));
        if(id%2==0) d.setWorkingHours(workingHours.subList(0,1));
        else d.setWorkingHours(workingHours);
        return d;
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

    @Test
    void listDoctorsMapsEntitiesToDtos(){
        Doctor d1=makeDoctor(1);
        Doctor d2=makeDoctor(2);
        List<Doctor> dummyRepoReturn= List.of(d1,d2);
        when(doctorRepository.findAll()).thenReturn(dummyRepoReturn);
        List<DoctorDTO> expectedDtos= dummyRepoReturn.stream()
                        .map(DoctorDTO::dtoFromEntity)
                        .toList();
        List<DoctorDTO> actualDtos= doctorService.listDoctors();
        verify(doctorRepository).findAll();
        assertThat(actualDtos).containsExactlyElementsOf(expectedDtos);
    }
    
    @Test
    void getDoctorReturnsDtoWhenFound(){
        Doctor dummyDoctor= makeDoctor(3);
        DoctorDTO expectedDto= DoctorDTO.dtoFromEntity(dummyDoctor);
        when(doctorRepository.findById(3)).thenReturn(Optional.of(dummyDoctor));
        DoctorDTO actualDto= doctorService.getDoctor(3);
        verify(doctorRepository).findById(3);
        assertThat(actualDto).usingRecursiveComparison().isEqualTo(expectedDto);
    }

    @Test
    void getDoctorThrowsNotFoundExceptionWhenEntityAbsent(){
        when(doctorRepository.findById(15)).thenReturn(Optional.empty());
        assertThatThrownBy(()->doctorService.getDoctor(15))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Doctor not found.");
        verify(doctorRepository).findById(15);
    }

    @Test
    void checkIfUpdateMethodMutatesEntityCorrectly(){
        Doctor dummyDoctor = makeDoctor(1);
        Doctor dummyDoctor2= makeDoctor(1);
        //extract new working hours
        List<WorkingHours> newWorkingHours= makeDoctor(2).getWorkingHours();
        dummyDoctor2.setWorkingHours(newWorkingHours);
        CreateDoctor dummyCreateDoctor= CreateDoctor.dtoFromEntity(dummyDoctor2);
        DoctorDTO expectedDoctorDto= DoctorDTO.dtoFromEntity(dummyDoctor2);
        when(doctorRepository.findById(1)).thenReturn(Optional.of(dummyDoctor));
        when(doctorRepository.save(any(Doctor.class))).
                thenAnswer(i->i.getArgument(0));

        DoctorDTO mutatedDoctor= doctorService.updateDoctor(1,dummyCreateDoctor);

        verify(doctorRepository).findById(1);
        verify(doctorRepository).save(dummyDoctor);

        assertThat(mutatedDoctor).usingRecursiveComparison()
                .isEqualTo(expectedDoctorDto);

    }

    @Test
    void updateDoctorThrowsNotFoundWhenEntityAbsent(){
        CreateDoctor dummyCreateDoctor= CreateDoctor.dtoFromEntity(makeDoctor(1));
        when(doctorRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(()->doctorService.updateDoctor(1,dummyCreateDoctor))
                .isInstanceOf(ResponseStatusException.class);
        verify(doctorRepository).findById(1);
    }

    @Test
    void deleteDoctor_calls_repo_deleteById() {
        doctorService.deleteDoctor(5);
        verify(doctorRepository).deleteById(5);
    }

    @Test
    void findBySpecialisationDelegatesToRepo(){
        List<Doctor> docs= List.of(makeDoctor(1),makeDoctor(2));
        List<DoctorDTO> docDtos= docs.stream().map(DoctorDTO::dtoFromEntity).toList();
        when(doctorRepository.findBySpecialization("Cardiology")).thenReturn(docs);
        List<DoctorDTO> actualDtos=doctorService.searchDoctorsBySpecialization("Cardiology");
        verify(doctorRepository).findBySpecialization("Cardiology");
        assertThat(actualDtos).usingRecursiveComparison()
                .isEqualTo(docDtos);

    }
}
