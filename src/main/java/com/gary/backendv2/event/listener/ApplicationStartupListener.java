package com.gary.backendv2.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.gary.backendv2.event.publisher.PrimitiveEntitiesCreatedEventPublisher;
import com.gary.backendv2.model.Facility;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.Tutorial;
import com.gary.backendv2.model.ambulance.Ambulance;
import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.request.BaseRequest;
import com.gary.backendv2.model.dto.request.FacilityRequest;
import com.gary.backendv2.model.dto.request.IncidentReportRequest;
import com.gary.backendv2.model.dto.request.items.*;
import com.gary.backendv2.model.dto.request.users.RegisterEmployeeRequest;
import com.gary.backendv2.model.enums.*;
import com.gary.backendv2.model.incident.Incident;
import com.gary.backendv2.model.incident.IncidentReport;
import com.gary.backendv2.model.inventory.items.*;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.dto.request.users.SignupRequest;
import com.gary.backendv2.model.security.Role;
import com.gary.backendv2.model.users.employees.AmbulanceManager;
import com.gary.backendv2.model.users.employees.Dispatcher;
import com.gary.backendv2.model.users.employees.Medic;
import com.gary.backendv2.repository.*;
import com.gary.backendv2.security.service.AuthService;
import com.gary.backendv2.service.AmbulanceService;
import com.gary.backendv2.service.GeocodingService;
import com.gary.backendv2.service.IncidentReportService;
import com.gary.backendv2.service.ItemService;
import com.gary.backendv2.utils.DictionaryIndexer;
import com.gary.backendv2.utils.Utils;
import com.gary.backendv2.utils.demodata.impl.ObjectInitializationVisitor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.validation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    PrimitiveEntitiesCreatedEventPublisher eventPublisher;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TutorialRepository tutorialRepository;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    AuthService authService;
    @Autowired
    AmbulanceService ambulanceService;

    @Autowired
    GeocodingService geocodingService;

    @Autowired
    IncidentReportService incidentReportService;

    @Autowired
    ItemService itemService;

    @Autowired
    ObjectInitializationVisitor objectInitializationVisitor;

    @Value("${gary.app.admin.credentials.email}")
    private String adminEmail;
    @Value("${gary.app.admin.credentials.password}")
    private String adminPassword;
    @Value("${gary.feature_flags.seed}")
    private boolean seed;

    private final String DB_INIT_BASE = "classpath:dbinit/";

    private final Class<?>[] ENTITIES_TO_SEED = {
            Facility.class,
            User.class,
            Medic.class,
            Dispatcher.class,
            Ambulance.class,
            SingleUseItem.class,
            MultiUseItem.class,
            MedicineItem.class,
            AmbulanceEquipmentItem.class,
            AmbulanceManager.class
    };

    private final Map<Class<?>, String> requestsFile = new HashMap<>(){{
        put(Facility.class, "facility_requests.json");
        put(Ambulance.class, "ambulance_requests.json");
        put(User.class, "regular_users_requests.json");
        put(Medic.class, "employee_medic_requests.json");
        put(Dispatcher.class, "employee_dispatcher_requests.json");
        put(SingleUseItem.class, "item_single_use_requests.json");
        put(MultiUseItem.class, "item_multi_use_requests.json");
        put(AmbulanceEquipmentItem.class, "item_ambulance_equipment_requests.json");
        put(MedicineItem.class, "item_medicine_requests.json");
        put(AmbulanceManager.class, "employee_ambulance_manager_requests.json");
    }};

    private final Map<Class<?>, Class<?>> requestClassFor = new HashMap<>(){{
        put(Facility.class, FacilityRequest.class);
        put(Ambulance.class, AddAmbulanceRequest.class);
        put(User.class, SignupRequest.class);
        put(Medic.class, RegisterEmployeeRequest.class);
        put(Dispatcher.class, RegisterEmployeeRequest.class);
        put(SingleUseItem.class, CreateSingleUseItemRequest.class);
        put(MultiUseItem.class, CreateMultiUseItemRequest.class);
        put(AmbulanceEquipmentItem.class, CreateAmbulanceEquipmentItemRequest.class);
        put(MedicineItem.class, CreateMedicineItemRequest.class);
        put(AmbulanceManager.class, RegisterEmployeeRequest.class);
    }};

    @Override
    @SneakyThrows
    public void onApplicationEvent(ContextRefreshedEvent event) {
        DictionaryIndexer indexer = DictionaryIndexer.getInstance();
       try {
           indexer.index();
       } catch (IOException e) {
           System.err.println("ERROR INDEXING DICTIONARY");
       }

       log.info("Database seeding enabled? {}", seed);
       if (seed) {
           seed();

           eventPublisher.publish(objectInitializationVisitor);
       }
    }

    @Transactional
    public void seed() {
        // System constants
        createRoles();
        createAdminAccount();
        createTutorials();

        // Sample user generated data
        Map<Class<?>, List<BaseRequest>> databaseInitializationMap = prepareSampleDataRequests();
        new Facility().accept(objectInitializationVisitor, geocodingService, databaseInitializationMap.get(Facility.class));
        new Ambulance().accept(objectInitializationVisitor, ambulanceService, databaseInitializationMap.get(Ambulance.class));
        new User().accept(objectInitializationVisitor, authService, databaseInitializationMap.get(User.class));
        new Medic().accept(objectInitializationVisitor, authService, EmployeeType.MEDIC, databaseInitializationMap.get(Medic.class));
        new Dispatcher().accept(objectInitializationVisitor, authService, EmployeeType.DISPATCHER, databaseInitializationMap.get(Dispatcher.class));
        new AmbulanceManager().accept(objectInitializationVisitor, authService, EmployeeType.AMBULANCE_MANAGER, databaseInitializationMap.get(AmbulanceManager.class));
        new SingleUseItem().accept(objectInitializationVisitor, itemService, databaseInitializationMap.get(SingleUseItem.class));
        new MultiUseItem().accept(objectInitializationVisitor, itemService, databaseInitializationMap.get(MultiUseItem.class));
        new AmbulanceEquipmentItem().accept(objectInitializationVisitor, itemService, databaseInitializationMap.get(AmbulanceEquipmentItem.class));
        new MedicineItem().accept(objectInitializationVisitor, itemService, databaseInitializationMap.get(MedicineItem.class));
    }

    @SneakyThrows
    private Map<Class<?>, List<BaseRequest>> prepareSampleDataRequests() {
        Map<Class<?>, List<BaseRequest>> dbMap = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        for (Class<?> clazz : ENTITIES_TO_SEED) {
            String requestsPath = DB_INIT_BASE + requestsFile.get(clazz);

            dbMap.put(clazz, mapper.readValue(Utils.loadClasspathResource(requestsPath), mapper.getTypeFactory().constructCollectionType(List.class, requestClassFor.get(clazz))));
        }

        return dbMap;
    }

    private void createRoles() throws RuntimeException {
        for (RoleName role : RoleName.values()) {
            createRole(role);
        }
    }

    private void createRole(RoleName name) {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName(name.getPrefixedName());

        try {
            roleRepository.save(role);
        } catch (Exception e) {
            if (e instanceof DataIntegrityViolationException violationException) {
                if ((violationException.getMostSpecificCause().toString().contains("Detail: Key (name)=(ROLE_"))) {
                    Pattern pattern = Pattern.compile("ROLE_\\w+");
                    Matcher matcher = pattern.matcher(violationException.getMostSpecificCause().toString());
                    if (matcher.find()) {
                        String rolename = matcher.group(0);

                        log.info("Role {} exists, skipping", rolename);
                    }
                }
            }
        }
    }

    private void createAdminAccount() {
        Optional<User> adminUser = Optional.ofNullable(userRepository.getByEmail("admin@gary.com"));
        if (adminUser.isEmpty()) {
            Optional<Role> adminRole = Optional.ofNullable(roleRepository.findByName(RoleName.ADMIN.getPrefixedName()));
            if (adminRole.isEmpty()) {
                log.error("ADMIN ROLE DOESN'T EXISTS");
                SpringApplication.exit(applicationContext, () -> -1);
                return;
            }

            User user = new User();
            user.setPassword(passwordEncoder.encode(adminPassword));
            user.setEmail(adminEmail);
            user.setRoles(Set.of(adminRole.get()));

            userRepository.save(user);

            log.info("Admin account created: email: {}, password: {}", adminEmail, adminPassword);
        }
    }

    private void createTutorials() {

        Tutorial tutorial = new Tutorial();
        tutorial.setName("Pierwsza pomoc - RKO");
        tutorial.setThumbnail("https://projektaed.pl/wp-content/uploads/2020/05/pierwsza_pomoc_w_trakcie_pandemii.jpg");
        tutorial.setTutorialType(TutorialType.COURSE);
        tutorial.setTutorialHTML(Utils.loadClasspathResource("classpath:templates/tutorial_cpr.html"));

        Tutorial tutorial1 = new Tutorial();
        tutorial1.setName("Pierwsza pomoc - atak epilepsji");
        tutorial1.setThumbnail("https://kursypierwszejpomocy.com.pl/wp-content/uploads/2019/07/32.jpg");
        tutorial1.setTutorialType(TutorialType.COURSE);
        tutorial1.setTutorialHTML(Utils.loadClasspathResource("classpath:templates/tutorial_epilepsy.html"));

        Tutorial tutorial2 = new Tutorial();
        tutorial2.setName("Pierwsza pomoc - porażenie prądem");
        tutorial2.setThumbnail("https://bi.im-g.pl/im/af/97/18/z25785775AMP,Porazenie-pradem.jpg");
        tutorial2.setTutorialType(TutorialType.COURSE);
        tutorial2.setTutorialHTML(Utils.loadClasspathResource("classpath:templates/tutorial_electric_shock.html"));

        Tutorial tutorial3 = new Tutorial();
        tutorial3.setName("Omdlenie");
        tutorial3.setThumbnail("https://cdn.galleries.smcloud.net/t/galleries/gf-bS8B-giVD-WyAk_jak-rozpoznac-omdlenie-pierwsza-pomoc-664x442-nocrop.jpg");
        tutorial3.setTutorialType(TutorialType.GENERAL);
        tutorial3.setTutorialHTML(Utils.loadClasspathResource("classpath:templates/tutorial_fainting.html"));

        Tutorial tutorial4 = new Tutorial();
        tutorial4.setName("Oparzenie");
        tutorial4.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Flag_of_the_Red_Cross.svg/1280px-Flag_of_the_Red_Cross.svg.png");
        tutorial4.setTutorialType(TutorialType.COURSE);
        tutorial4.setTutorialHTML(Utils.loadClasspathResource("classpath:templates/tutorial_burns.html"));

        Tutorial tutorial5 = new Tutorial();
        tutorial5.setName("Udar");
        tutorial5.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Flag_of_the_Red_Cross.svg/1280px-Flag_of_the_Red_Cross.svg.png");
        tutorial5.setTutorialType(TutorialType.GENERAL);
        tutorial5.setTutorialHTML(Utils.loadClasspathResource("classpath:templates/tutorial_stroke.html"));

        Tutorial tutorial6 = new Tutorial();
        tutorial6.setName("Zasłabnięcia");
        tutorial6.setThumbnail("https://ocdn.eu/pulscms-transforms/1/kSIk9kpTURBXy8yYTgzMDJhZTliYzNlNzQ0Mjc4YTJhN2VlODUzMDc3Ny5qcGeSlQMAzKbNFNLNC7aTBc0DAs0BkN4AAaEwBQ");
        tutorial6.setTutorialType(TutorialType.GENERAL);
        tutorial6.setTutorialHTML(Utils.loadClasspathResource("classpath:templates/tutorial_faint.html"));

        List<Tutorial> tutorials = List.of(tutorial, tutorial1, tutorial2,tutorial3,tutorial4,tutorial5,tutorial6);
        tutorialRepository.saveAll(tutorials);
    }

}
