package com.gary.backendv2.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.gary.backendv2.event.PrimitiveEntitiesCreatedEvent;
import com.gary.backendv2.exception.HttpException;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.dto.request.*;
import com.gary.backendv2.model.enums.AmbulanceStateType;
import com.gary.backendv2.model.enums.BackupType;
import com.gary.backendv2.model.enums.IncidentStatusType;
import com.gary.backendv2.model.incident.IncidentReport;
import com.gary.backendv2.model.users.employees.Dispatcher;
import com.gary.backendv2.repository.*;
import com.gary.backendv2.service.*;
import com.gary.backendv2.utils.Utils;
import com.gary.backendv2.utils.demodata.impl.ObjectInitializationVisitor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class PrimitiveEntitiesCreatedEventListener implements ApplicationListener<PrimitiveEntitiesCreatedEvent> {
    private final DispatcherRepository dispatcherRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final EmployeeService employeeService;
    private final IncidentReportService incidentReportService;
    private final AmbulanceService ambulanceService;
    private final IncidentService incidentService;
    private final BackupService backupService;
    private final TutorialService tutorialService;
    private final MedicRepository medicRepository;
    private final AmbulanceManagerRepository ambulanceManagerRepository;
    private final AmbulanceHistoryRepository ambulanceHistoryRepository;

    @Override
    @Transactional
    public void onApplicationEvent(PrimitiveEntitiesCreatedEvent event) {
        prepareData(event.getVisitor());
    }

    private void prepareData(ObjectInitializationVisitor visitor) {
        try {
            setDispatchersStateToWorking();
            createSampleIncidents(visitor);
            setDispatchersStateToNotWorking();
        } catch (Exception ignored) {}

        String[] ambulances = new String[] { // 0 to 8 indices
                "LBI 55362", "WA 1337", "WB 1337", "WC 1337", "WD 1337", "WE 1337", "WF 1337", "WG 1337", "WJ 1337"
        };

        Set<String> predefinedAmbulances = new HashSet<>(List.of(ambulances));
        Set<Ambulance> allAmbulances = new HashSet<>(ambulanceRepository.findAll());
        allAmbulances.removeIf(x -> predefinedAmbulances.contains(x.getLicensePlate()));

        ambulanceService.changeAmbulanceState(ambulances[6], AmbulanceStateType.MAINTENANCE);
        ambulanceService.changeAmbulanceState(ambulances[4], AmbulanceStateType.FAILURE);

//        WJ 1337

        Location[] incident8Path = new Location[] {
                Location.of(52.21928142554568, 20.977858455917147),
                Location.of(52.218925773049946, 20.977724489927088),
                Location.of(52.21885346432566, 20.979239595313505),
                Location.of(52.21877331962926, 20.981076859761217),
                Location.of(52.21886067180057, 20.98198240864779),
                Location.of(52.21892065646493, 20.98248183257912),
                Location.of(52.21876030451264, 20.983122886020542),
                Location.of(52.217454400161024, 20.98198519923355),
                Location.of(52.216961717314604, 20.981476895672103),
                Location.of(52.21626063441941, 20.980930171843546),
                Location.of(52.2161459381765, 20.981245936472085),
                Location.of(52.21596767457716, 20.98192934074391),
                Location.of(52.21602155889862, 20.983625452278385),
                Location.of(52.216221505739455, 20.987355196148506),
                Location.of(52.216201994486134, 20.988098812336542),
                Location.of(52.214110289043916, 20.988394674869053),
                Location.of(52.212622805250284, 20.988462946249413),
                Location.of(52.210224138964435, 20.987689152068707),
                Location.of(52.21034965588472, 20.988136744685313),
                Location.of(52.21123754983119, 20.988462952411105),
                Location.of(52.211539707035776, 20.992195508044695),
                Location.of(52.21229741247768, 20.99668675178345),
                Location.of(52.21157688179377, 20.996079776356027),
                Location.of(52.21064716067642, 20.99545764271592)
        };

        Location[] incident10Path = new Location[] {
                Location.of(52.21919528978569, 20.98106802827873),
                Location.of(52.22050827225227, 20.98084421933226),
                Location.of(52.22173988533476, 20.980678544921506),
                Location.of(52.22113019573063, 20.984933607153664),
                Location.of(52.22108329043526, 20.985539650603695),
                Location.of(52.2237489888002, 20.987880141891463),
                Location.of(52.225227700357856, 20.991163118781127),
                Location.of(52.226785237817396, 20.997804240006452),
                Location.of(52.22748843517241, 21.001827485416037),
                Location.of(52.228638467105526, 21.00640872532784),
                Location.of(52.22955190196804, 21.011075730143432),
                Location.of(52.22964388284195, 21.012180806293056),
                Location.of(52.23014331576029, 21.013489727030997),
                Location.of(52.231299787663254, 21.019176051372995),
                Location.of(52.231847392434325, 21.020499651610063),
                Location.of(52.23282315004424, 21.019786525514466),
                Location.of(52.233998597874134, 21.019062311983475),
                Location.of(52.23525740094083, 21.01849828827275),
                Location.of(52.237034995599686, 21.01792733181952),
                Location.of(52.237146336512325, 21.01857165031779),
                Location.of(52.237247910652556, 21.01853018513205),
                Location.of(52.237320183115024, 21.018855534925205),
                Location.of(52.23748230912144, 21.019123473099345),
                Location.of(52.23769131314779, 21.01956366021356),
                Location.of(52.237871025739466, 21.018673723515686),
                Location.of(52.23793938796786, 21.019464774214565),
                Location.of(52.23748035892024, 21.020134607158617),
                Location.of(52.23657986098689, 21.020456764733996),
                Location.of(52.23594696999613, 21.020387901294114),
                Location.of(52.23385423218657, 21.021385684385763),
                Location.of(52.234044779714765, 21.02226007824656),
                Location.of(52.234931823100396, 21.0222761876645),
                Location.of(52.23609481445723, 21.021525151953213),
                Location.of(52.23597324771865, 21.02038789094952),
                Location.of(52.23872619586852, 21.01922918860165),
                Location.of(52.23944340041172, 21.020721446976964),
                Location.of(52.23986411793637, 21.02322066913011),
                Location.of(52.23864543144924, 21.02508212710643),
                Location.of(52.23659898132788, 21.026719728712585),
                Location.of(52.235100322263136, 21.02939280616639),
                Location.of(52.23140650545127, 21.03035629854463),
                Location.of(52.23021234980099, 21.030561107693394),
                Location.of(52.229812520843076, 21.022283126207686),
                Location.of(52.227926853783366, 21.0224283765081),
                Location.of(52.223633037036805, 21.020470860662837),
                Location.of(52.22305004986808, 21.015230956192223),
                Location.of(52.223096573723936, 21.003198901674548),
                Location.of(52.223835228988904, 20.98999214136857),
                Location.of(52.22418947776417, 20.989832200877416),
                Location.of(52.2251424508773, 20.98920991536538),
                Location.of(52.22189572020209, 20.977805158157246),
                Location.of(52.21976618705908, 20.978073423310605),
                Location.of(52.21971357515517, 20.98097015741107),
                Location.of(52.21920747077389, 20.98101305227995)
        };

        ambulanceService.changeAmbulanceState(ambulances[8], AmbulanceStateType.FAILURE);
        ambulanceService.changeAmbulanceState(ambulances[8], AmbulanceStateType.MAINTENANCE);
        ambulanceService.changeAmbulanceState(ambulances[8], AmbulanceStateType.AVAILABLE);
        ambulanceService.assignMedics(ambulances[8], List.of(9));

        incidentService.addAmbulances(8, List.of(ambulances[8]));
        for (Location location : incident8Path) {
            PostAmbulanceLocationRequest r = new PostAmbulanceLocationRequest();
            r.setLatitude(location.getLongitude());
            r.setLongitude(location.getLatitude());
            ambulanceService.addGeoLocation(ambulances[8], r);
        }

        ambulanceService.addItems(ambulances[8], 4, 20);
        ambulanceService.addItems(ambulances[8], 1, 15);
        ambulanceService.addItems(ambulances[8], 7, 2);

        IncidentRequest incident8Request = new IncidentRequest();
        incident8Request.setDangerScale(3);
        incident8Request.setIncidentStatusType(IncidentStatusType.CLOSED);
        incident8Request.setReactionJustification("Karetka dojechała, poszkodowany dostał podstawową opiekę medyczną");
        incidentService.update(8, incident8Request);

        incidentService.addAmbulances(10, List.of(ambulances[8]));
        for (Location location : incident10Path) {
            PostAmbulanceLocationRequest r = new PostAmbulanceLocationRequest();
            r.setLatitude(location.getLongitude());
            r.setLongitude(location.getLatitude());
            ambulanceService.addGeoLocation(ambulances[8], r);
        }

        IncidentRequest incident10Request = new IncidentRequest();
        incident10Request.setDangerScale(9);
        incident10Request.setIncidentStatusType(IncidentStatusType.CLOSED);
        incident10Request.setReactionJustification("Karetka dojechała, poszkodowany został przetransportowany do najbliżeszego szpitala, karetka wróciła na bazę");
        incidentService.update(10, incident10Request);
        ambulanceService.changeAmbulanceState(ambulances[8], AmbulanceStateType.CREW_BRAKE);

        Ambulance a = ambulanceRepository.findByLicensePlate(ambulances[8]).orElseThrow(RuntimeException::new);
        a.setLocation(Location.of(20.98101305227995, 52.21920747077389));
        ambulanceRepository.save(a);

//         LBI 55362
        ambulanceService.assignMedics(ambulances[0], List.of(11));
        ambulanceService.addItems(ambulances[0], 1, 20);
        ambulanceService.addItems(ambulances[0], 2, 23);
        ambulanceService.addItems(ambulances[0], 6, 15);
        ambulanceService.addItems(ambulances[0], 19, 10);
        ambulanceService.addItems(ambulances[0], 22, 1);
        ambulanceService.addItems(ambulances[0], 30, 1);


//         WA 1337
        ambulanceService.assignMedics(ambulances[1], List.of(5, 6, 7));
        ambulanceService.addItems(ambulances[1], 1, 20);
        ambulanceService.addItems(ambulances[1], 2, 23);
        ambulanceService.addItems(ambulances[1], 6, 15);
        ambulanceService.addItems(ambulances[1], 19, 10);
        ambulanceService.addItems(ambulances[1], 22, 1);
        ambulanceService.addItems(ambulances[1], 30, 1);

//         WB 1337
        ambulanceService.assignMedics(ambulances[2], List.of(8, 10));
        ambulanceService.addItems(ambulances[2], 1, 20);
        ambulanceService.addItems(ambulances[2], 2, 23);
        ambulanceService.addItems(ambulances[2], 6, 15);
        ambulanceService.addItems(ambulances[2], 19, 10);
        ambulanceService.addItems(ambulances[2], 22, 1);
        ambulanceService.addItems(ambulances[2], 30, 1);


//        WC 1337
        ambulanceService.addItems(ambulances[3], 1, 5);
        ambulanceService.addItems(ambulances[3], 3, 5);
        ambulanceService.addItems(ambulances[3], 5, 5);
        ambulanceService.addItems(ambulances[3], 13, 5);
        ambulanceService.addItems(ambulances[3], 12, 5);
        ambulanceService.addItems(ambulances[3], 23, 5);

//        WD 1337
        ambulanceService.addItems(ambulances[4], 2, 1);
        ambulanceService.addItems(ambulances[4], 6, 3);
        ambulanceService.addItems(ambulances[4], 4, 5);
        ambulanceService.addItems(ambulances[4], 11, 5);
        ambulanceService.addItems(ambulances[4], 20, 2);
        ambulanceService.addItems(ambulances[4], 23, 4);

//        WD 1337
        ambulanceService.addItems(ambulances[5], 2, 1);
        ambulanceService.addItems(ambulances[5], 6, 3);
        ambulanceService.addItems(ambulances[5], 4, 5);
        ambulanceService.addItems(ambulances[5], 11, 5);
        ambulanceService.addItems(ambulances[5], 20, 2);
        ambulanceService.addItems(ambulances[5], 23, 4);

//        WE 1337
        ambulanceService.addItems(ambulances[6], 4, 2);
        ambulanceService.addItems(ambulances[6], 6, 3);
        ambulanceService.addItems(ambulances[6], 2, 3);
        ambulanceService.addItems(ambulances[6], 7, 5);
        ambulanceService.addItems(ambulances[6], 26, 2);
        ambulanceService.addItems(ambulances[6], 22, 6);

//        WF 1337
        ambulanceService.addItems(ambulances[7], 4, 2);
        ambulanceService.addItems(ambulances[7], 6, 3);
        ambulanceService.addItems(ambulances[7], 2, 3);
        ambulanceService.addItems(ambulances[7], 7, 5);
        ambulanceService.addItems(ambulances[7], 26, 2);
        ambulanceService.addItems(ambulances[7], 22, 6);

        incidentService.addAmbulances(2, List.of(ambulances[0], ambulances[1], ambulances[2]));
        incidentService.addAmbulances(4, List.of(ambulances[0]));
        incidentService.addAmbulances(6, List.of(ambulances[1]));
        incidentService.addAmbulances(10, List.of(ambulances[2]));
        incidentService.addAmbulances(1, List.of(ambulances[7]));

        IncidentRequest incident7Request = new IncidentRequest();
        incident7Request.setDangerScale(2);
        incident7Request.setIncidentStatusType(IncidentStatusType.REJECTED);
        incident7Request.setReactionJustification("Fałszywe zgłoszenie, patrol nic nie zauważył");
        incidentService.update(7, incident7Request);

        // Backups
        BackupAddRequest[] backups = new BackupAddRequest[] {
                new BackupAddRequest("dispatch@test.pl", 2, true, "Wypadek, policja wymagana", BackupType.POLICE),
                new BackupAddRequest("dispatch2@test.pl", 4, true, "Pożar, wezwano straż pożarną", BackupType.FIRE_FIGHTERS),
                new BackupAddRequest("dispatch@test.pl", 6, true, "Pożar, wezwano straż pożarną", BackupType.FIRE_FIGHTERS),
                new BackupAddRequest("dispatch2@test.pl", 10, true, "Wezwoano policję w celu zabezpieczenia miejsca zdarzenia", BackupType.POLICE),
                new BackupAddRequest("dispatch@test.pl", 1, true, "Wypadek samochodowy, policja wezwana w celu wyjaśnienia przyczyn", BackupType.POLICE)
        };

        for (BackupAddRequest backup : backups) {
            backupService.add(backup);
        }


        // Tutorial reviews
        List<ReviewRequest[]> allReviews = new ArrayList<>();
        ReviewRequest[] reviews1 = new ReviewRequest[] {
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(4.0d, "none"),
                new ReviewRequest(3.0d, "none"),
                new ReviewRequest(2.0d, "none"),
                new ReviewRequest(1.0d, "none"),
        };
        ReviewRequest[] reviews2 = new ReviewRequest[] {
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(4.0d, "none"),
                new ReviewRequest(4.0d, "none"),
                new ReviewRequest(1.0d, "none"),
                new ReviewRequest(1.0d, "none"),
        };
        ReviewRequest[] reviews3 = new ReviewRequest[] {
                new ReviewRequest(4.0d, "none"),
                new ReviewRequest(4.0d, "none"),
                new ReviewRequest(3.0d, "none"),
                new ReviewRequest(3.0d, "none"),
                new ReviewRequest(2.0d, "none"),
        };
        ReviewRequest[] reviews4 = new ReviewRequest[] {
                new ReviewRequest(2.0d, "none"),
                new ReviewRequest(4.0d, "none"),
                new ReviewRequest(3.0d, "none"),
                new ReviewRequest(2.0d, "none"),
                new ReviewRequest(4.0d, "none"),
        };
        ReviewRequest[] reviews5 = new ReviewRequest[] {
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(4.0d, "none"),
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(4.0d, "none"),
        };
        ReviewRequest[] reviews6 = new ReviewRequest[] {
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(5.0d, "none"),
        };
        ReviewRequest[] reviews7 = new ReviewRequest[] {
                new ReviewRequest(2.0d, "none"),
                new ReviewRequest(4.0d, "none"),
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(3.0d, "none"),
                new ReviewRequest(2.0d, "none"),
        };
        ReviewRequest[] reviews8 = new ReviewRequest[] {
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(5.0d, "none"),
                new ReviewRequest(3.0d, "none"),
                new ReviewRequest(4.0d, "none"),
        };
        allReviews.addAll(List.of(reviews1, reviews2, reviews3, reviews4, reviews5, reviews6, reviews7, reviews8));


        int i = 1;
        String[] userEmails = new String[] {
                "test@test.pl", "test2@test.pl", "test3@test.pl"
        };
        for (ReviewRequest[] array : allReviews) {
            for (ReviewRequest review : array) {
                tutorialService.addReviewToTutorial(userEmails[ThreadLocalRandom.current().nextInt(0, userEmails.length)], i, review);
            }
            i++;
        }

    }

    private void setDispatchersStateToWorking() {
        List<Dispatcher> dispatchers = dispatcherRepository.findAll();
        try {
            dispatchers.forEach(employeeService::startShift);
        } catch (HttpException ignored) {}
    }

    private void setDispatchersStateToNotWorking() {
        List<Dispatcher> dispatchers = dispatcherRepository.findAll();
        dispatchers.forEach(employeeService::endShift);
    }

    @SneakyThrows
    private void createSampleIncidents(ObjectInitializationVisitor visitor) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());

        List<BaseRequest> incidentRequests = mapper.readValue(Utils.loadClasspathResource("classpath:dbinit/incident_report_requests.json"), mapper.getTypeFactory().constructCollectionType(List.class, IncidentReportRequest.class));
        new IncidentReport().accept(visitor, incidentReportService, incidentRequests);
    }

}
