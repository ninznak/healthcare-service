import org.junit.jupiter.api.Test;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

class MedicalServiceTest {

    @Test
    void testCheckBloodPressureAbnormal() {
        PatientInfoRepository patientInfoRepository = mock(PatientInfoRepository.class);
        SendAlertService alertService = mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        String patientId = "111";
        BloodPressure normalBloodPressure = new BloodPressure(120, 80);
        BloodPressure abnormalBloodPressure = new BloodPressure(140, 90);
        PatientInfo patientInfo = new PatientInfo(patientId, "John", "Doe", LocalDate.of(1990, 1, 1),
                new HealthInfo(new BigDecimal("36.6"), normalBloodPressure));

        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);

        medicalService.checkBloodPressure(patientId, abnormalBloodPressure);

        verify(alertService).send("Warning, patient with id: 111, need help");
    }

    @Test
    void testCheckTemperatureAbnormal() {
        PatientInfoRepository patientInfoRepository = mock(PatientInfoRepository.class);
        SendAlertService alertService = mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        String patientId = "222";
        BigDecimal normalTemperature = new BigDecimal("36.6");
        BigDecimal abnormalTemperature = new BigDecimal("34.0");
        PatientInfo patientInfo = new PatientInfo(patientId, "John", "Doe", LocalDate.of(1990, 1, 1),
                new HealthInfo(normalTemperature, new BloodPressure(120, 80)));

        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);

        medicalService.checkTemperature(patientId, abnormalTemperature);

        verify(alertService).send("Warning, patient with id: 222, need help");
    }

    @Test
    void testCheckBloodPressureNormal() {
        PatientInfoRepository patientInfoRepository = mock(PatientInfoRepository.class);
        SendAlertService alertService = mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        String patientId = "333";
        BloodPressure normalBloodPressure = new BloodPressure(120, 80);
        PatientInfo patientInfo = new PatientInfo(patientId, "John", "Doe", LocalDate.of(1990, 1, 1),
                new HealthInfo(new BigDecimal("36.6"), normalBloodPressure));

        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);

        medicalService.checkBloodPressure(patientId, normalBloodPressure);

        verify(alertService, never()).send(anyString());
    }

    @Test
    void testCheckTemperatureNormal() {
        PatientInfoRepository patientInfoRepository = mock(PatientInfoRepository.class);
        SendAlertService alertService = mock(SendAlertService.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);

        String patientId = "444";
        BigDecimal normalTemperature = new BigDecimal("36.6");
        PatientInfo patientInfo = new PatientInfo(patientId, "John", "Doe", LocalDate.of(1990, 1, 1),
                new HealthInfo(normalTemperature, new BloodPressure(120, 80)));

        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);

        medicalService.checkTemperature(patientId, normalTemperature);

        verify(alertService, never()).send(anyString());
    }
}