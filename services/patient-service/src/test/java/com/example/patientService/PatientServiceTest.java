package com.example.patientService;

import com.example.patientService.entity.*;
import com.example.patientService.repository.PatientRepository;
import com.example.patientService.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
    @Mock
    PatientRepository patientRepository;

    @InjectMocks
    PatientService patientService;

    //helper methods ---------------------------------
    PatientDTO makePatientDto(int i){
        List<PatientHistoryDto> patientHistoryDtos=new ArrayList<>(List.of(
                new PatientHistoryDto("Lethargy",
                        LocalDate.of(2025,1,1),
                        true),
                new PatientHistoryDto("Braindead-ness",
                        LocalDate.of(2000,1,1),
                        true),
                new PatientHistoryDto("Stupidity",
                        LocalDate.of(2005,3,13),
                        true)
        ));
        return new PatientDTO(i%2==0?"Bhagirathi":"Alokik",
                25,
                "Male",
                12345,
                "Nowhere at nowhere street",
                i%2==0?patientHistoryDtos:patientHistoryDtos.subList(0,1));
    }
    Patient makePatient(int id){
        Patient p=makePatientDto(id).getEntityFromDto();
        p.setPatientId(id);
        int i=0;
        for(PatientHistory ph:p.getPatientHistoryList()){
            ph.setId(id+i++);
            ph.setPatient(p);
        }
        return p;
    }
    //------------------------------------------------
    @Test
    void createPatientMapsDtoToEntity(){
        ArgumentCaptor<Patient> captor=ArgumentCaptor.forClass(Patient.class);
        PatientDTO expectedPatientDto = makePatientDto(2);
        when(patientRepository.save(any(Patient.class))).thenAnswer(
                i->i.getArgument(0));
        patientService.creatPatient(expectedPatientDto);
        verify(patientRepository).save(captor.capture());
        PatientDTO actualPatientDto=PatientDTO.getDtoFromEntity(captor.getValue());
        assertThat(actualPatientDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedPatientDto);
    }

    @Test
    void listPatientMapsToDto(){
        List<Patient> patients= List.of(makePatient(1),makePatient(2));
        List<ResponsePatientDto> expectedPatientDtos = patients.stream()
                        .map(ResponsePatientDto::getDtoFromEntity).toList();
        when(patientRepository.findAll()).thenReturn(patients);
        List<ResponsePatientDto> actualPatientDtos = patientService.listPatients();
        verify(patientRepository).findAll();
        assertThat(actualPatientDtos)
                .usingRecursiveComparison()
                .isEqualTo(expectedPatientDtos);
    }

    @Test
    void getPatientReturnsDtoWhenFound(){
        Patient patient= makePatient(2);
        ResponsePatientDto expectedDto= ResponsePatientDto.getDtoFromEntity(patient);
        when(patientRepository.findById(2)).thenReturn(Optional.of(patient));
        ResponsePatientDto actualDto= patientService.getPatient(2);
        verify(patientRepository).findById(2);
        assertThat(actualDto).usingRecursiveComparison().isEqualTo(expectedDto);
    }

    @Test
    void getPatientThrowsNotFoundExceptionWhenEntityAbsent(){
        when(patientRepository.findById(15)).thenReturn(Optional.empty());
        assertThatThrownBy(()->patientService.getPatient(15))
                .isInstanceOf(ResponseStatusException.class);
        verify(patientRepository).findById(15);
    }

    @Test
    void checkIfUpdateMethodMutatesEntityCorrectly(){
        Patient dummyPatient = makePatient(1);
        Patient dummyPatient2= makePatient(2);
        dummyPatient2.setPatientId(1);

        ArgumentCaptor<Patient> captor=ArgumentCaptor.forClass(Patient.class);

        ResponsePatientDto expectedResponsePatientDto= ResponsePatientDto
                .getDtoFromEntity(dummyPatient2);
        PatientDTO dtoToBePassedToMutate= PatientDTO.getDtoFromEntity(dummyPatient2);
        when(patientRepository.findById(1)).thenReturn(Optional.of(dummyPatient));
        when(patientRepository.save(any(Patient.class))).
                thenAnswer(i->i.getArgument(0));

        ResponsePatientDto mutatedPatientResponseDto= patientService
                .updatePatient(1,dtoToBePassedToMutate);

        verify(patientRepository).findById(1);
        verify(patientRepository).save(captor.capture());

        Patient mutatedPatient= captor.getValue();
        PatientDTO mutatedPatientDto = PatientDTO.getDtoFromEntity(mutatedPatient);

        assertThat(mutatedPatient).isSameAs(dummyPatient);
        assertThat(mutatedPatientResponseDto).usingRecursiveComparison()
                .isEqualTo(expectedResponsePatientDto);
        assertThat(mutatedPatientDto).usingRecursiveComparison()
                .isEqualTo(dtoToBePassedToMutate);
    }

    @Test
    void updatePatientThrowsExceptionWhenEntityNotFound(){
        when(patientRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(()->patientService.updatePatient(1,makePatientDto(1)))
                .isInstanceOf(ResponseStatusException.class);
        verify(patientRepository).findById(1);
    }

    @Test
    void deletePatientByIdDeletesPatient(){
        patientService.deletePatient(1);
        verify(patientRepository).deleteById(1);
    }

    @Test
    void getPatientHistoryMapsEntityToDto(){
        Patient patient= makePatient(2);
        List<PatientHistoryDto> expectedPatientHistoryDtos=patient
                .getPatientHistoryList().stream()
                .map(PatientHistoryDto::getDtoFromEntity).toList();
        when(patientRepository.findById(2))
                .thenReturn(Optional.of(patient));
        List<PatientHistoryDto> patientHistoryDtoList=
                patientService.getPatientHistory(2);
        assertThat(patientHistoryDtoList).usingRecursiveComparison()
                .isEqualTo(expectedPatientHistoryDtos);
    }

    @Test
    void updatePatientHistoryMutatesEntityProperly(){
        Patient patient= makePatient(1);
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class)))
                .thenAnswer(i->i.getArgument(0));
        List<PatientHistoryDto> patientHistoryDtosToBeSet=
                makePatient(2).getPatientHistoryList().stream()
                        .map(PatientHistoryDto::getDtoFromEntity).toList();
        ArgumentCaptor<Patient> captor= ArgumentCaptor.forClass(Patient.class);

        List<PatientHistoryDto> returnedPatientDto= patientService
                .updatePatientHistory(1,patientHistoryDtosToBeSet);

        verify(patientRepository).findById(1);
        verify(patientRepository).save(captor.capture());

        Patient capturedPatient= captor.getValue();
        List<PatientHistoryDto> savedPatientDto= capturedPatient.getPatientHistoryList()
                .stream().map(PatientHistoryDto::getDtoFromEntity).toList();

        assertThat(capturedPatient).isSameAs(patient);
        assertThat(capturedPatient.getPatientHistoryList())
                .isSameAs(patient.getPatientHistoryList());
        assertThat(savedPatientDto).usingRecursiveComparison()
                .isEqualTo(patientHistoryDtosToBeSet);
        assertThat(returnedPatientDto).usingRecursiveComparison()
                .isEqualTo(patientHistoryDtosToBeSet);
    }
    
    @Test
    void getPatientHistoryThrowsExceptionWhenEntityNotFound(){
        when(patientRepository.findById(2)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> patientService.getPatientHistory(2))
                .isInstanceOf(ResponseStatusException.class);
        verify(patientRepository).findById(2);
    }

    @Test
    void updatePatientHistoryThrowsExceptionWhenEntityNotFound(){
        when(patientRepository.findById(2)).thenReturn(Optional.empty());
        List<PatientHistoryDto> patientHistoryDtosToBeSet=
                makePatient(2).getPatientHistoryList().stream()
                        .map(PatientHistoryDto::getDtoFromEntity).toList();

        assertThatThrownBy(()-> patientService
                .updatePatientHistory(2,patientHistoryDtosToBeSet))
                .isInstanceOf(ResponseStatusException.class);

        verify(patientRepository).findById(2);

    }
}
