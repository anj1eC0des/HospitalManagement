package com.example.doctorService;

import com.example.doctorService.entity.*;
import com.example.doctorService.repository.DoctorRepository;
import com.example.doctorService.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

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
public class ServiceClassTest {
    @Mock
    DoctorRepository doctorRepository;

    @InjectMocks
    DoctorService doctorService;

    DoctorDTO makeCreateDoctorPayload(){
        return new DoctorDTO(null,
                "Ada Wong",
                "Cardiology",
                69,
                12345678,
                new ArrayList<WorkingHoursDTO>());
    }

    Doctor makeDoctor(Long id){
        Doctor d=new Doctor(id,
                "dummy"+id,
                "dummy spec",
                100,
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
        DoctorDTO createDoctorPayload= makeCreateDoctorPayload();
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
        Doctor d1=makeDoctor(1L);
        Doctor d2=makeDoctor(2L);
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
        Doctor dummyDoctor= makeDoctor(3L);
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
        Doctor dummyDoctor = makeDoctor(1L);
        Doctor dummyDoctor2= makeDoctor(2L);
        dummyDoctor2.setDoctorId(1L);
        //dummyDoctor2 has different fields but same id as dummyDoctor1 now.
        ArgumentCaptor<Doctor> captor= ArgumentCaptor.forClass(Doctor.class);
        DoctorDTO dummyCreateDoctor= DoctorDTO.dtoFromEntity(dummyDoctor2);
        DoctorDTO expectedDoctorDto= DoctorDTO.dtoFromEntity(dummyDoctor2);
        when(doctorRepository.findById(1)).thenReturn(Optional.of(dummyDoctor));
        when(doctorRepository.save(any(Doctor.class))).
                thenAnswer(i->i.getArgument(0));

        DoctorDTO returnedDoctorDto= doctorService.updateDoctor(1,dummyCreateDoctor);

        verify(doctorRepository).findById(1);
        verify(doctorRepository).save(captor.capture());

        Doctor mutatedDoctor= captor.getValue();
        DoctorDTO mutatedDoctorDto = DoctorDTO.dtoFromEntity(mutatedDoctor);

        assertThat(mutatedDoctor).isSameAs(dummyDoctor);
        assertThat(mutatedDoctorDto).usingRecursiveComparison()
                .isEqualTo(expectedDoctorDto);
        assertThat(returnedDoctorDto).usingRecursiveComparison()
                .isEqualTo(expectedDoctorDto);
    }

    @Test
    void updateDoctorThrowsNotFoundWhenEntityAbsent(){
        DoctorDTO dummyCreateDoctor= DoctorDTO.dtoFromEntity(makeDoctor(1L));
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
        List<Doctor> docs= List.of(makeDoctor(1L),makeDoctor(2L));
        List<DoctorDTO> docDtos= docs.stream().map(DoctorDTO::dtoFromEntity).toList();
        when(doctorRepository.findBySpecialization("Cardiology")).thenReturn(docs);
        List<DoctorDTO> actualDtos=doctorService.searchDoctorsBySpecialization("Cardiology");
        verify(doctorRepository).findBySpecialization("Cardiology");
        assertThat(actualDtos).usingRecursiveComparison()
                .isEqualTo(docDtos);

    }

    @Test
    void findByDepartmentIdDelegatesToRepo(){
        List<Doctor> doctorList=new ArrayList<>(List.of(makeDoctor(1L),makeDoctor(2L)));
        when(doctorRepository.findByDepartment(100)).thenReturn(doctorList);
        List<DoctorDTO> doctorDTOList= doctorList.stream()
                .map(DoctorDTO::dtoFromEntity).toList();

        List<DoctorDTO> actualList= doctorService.searchDoctorByDepartment(100);

        verify(doctorRepository).findByDepartment(100);

        assertThat(actualList).usingRecursiveComparison().isEqualTo(doctorDTOList);

    }

    @Test
    void getWorkingHoursReturnsAppropriateDto(){
        Doctor doctor=makeDoctor(5L);
        List<WorkingHoursDTO> expectedWorkingHoursDto=
                doctor.getWorkingHours()
                        .stream()
                        .map(WorkingHoursDTO::dtoFromEntity)
                        .toList();

        when(doctorRepository.findById(5)).thenReturn(Optional.of(doctor));

        List<WorkingHoursDTO> actualWorkingHoursDto=
                doctorService.getWorkingHours(5);

        verify(doctorRepository).findById(5);
        assertThat(actualWorkingHoursDto)
                .containsExactlyElementsOf(expectedWorkingHoursDto);

    }

    @Test
    void getWorkingHoursThrowsExceptionWhenEntityNotFound(){
        when(doctorRepository.findById(5)).thenReturn(Optional.empty());
       assertThatThrownBy(()->doctorService.getWorkingHours(5))
               .isInstanceOf(ResponseStatusException.class);
       verify(doctorRepository).findById(5);
    }

    @Test
    void setWorkingHoursWipesAndSetsWorkingHoursProperly(){
        Doctor doctor=makeDoctor(5L);
        when(doctorRepository.findById(5)).thenReturn(Optional.of(doctor));
        List<WorkingHoursDTO> wipedWorkingHoursDto= doctor.getWorkingHours()
                .stream().map(WorkingHoursDTO::dtoFromEntity).toList();
        List<WorkingHoursDTO> expectedWorkingHoursDto =new ArrayList<>(List.of(
                new WorkingHoursDTO(DayOfWeek.TUESDAY,
                        LocalTime.of(6,0),
                        LocalTime.of(14,0)
                ),
                new WorkingHoursDTO(DayOfWeek.WEDNESDAY,
                        LocalTime.of(9,0),
                        LocalTime.of(17,0)
                )
        ));
        doctorService.setWorkingHours(5, expectedWorkingHoursDto);

        verify(doctorRepository).findById(5);
        verify(doctorRepository).save(doctor);

        List<WorkingHoursDTO> actualWorkingHoursDto=
                doctor.getWorkingHours().stream()
                        .map(WorkingHoursDTO::dtoFromEntity).toList();

        assertThat(actualWorkingHoursDto)
                .containsExactlyElementsOf(expectedWorkingHoursDto);
        assertThat(actualWorkingHoursDto)
                .doesNotContainAnyElementsOf(wipedWorkingHoursDto);
    }

    @Test
    void setWorkingHoursThrowsExceptionWhenEntityNotFound(){
        when(doctorRepository.findById(5)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> doctorService
                    .setWorkingHours(5,new ArrayList<WorkingHoursDTO>()))
                .isInstanceOf(ResponseStatusException.class);

        verify(doctorRepository).findById(5);
    }

}
