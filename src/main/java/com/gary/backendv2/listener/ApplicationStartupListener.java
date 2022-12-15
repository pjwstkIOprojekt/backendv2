package com.gary.backendv2.listener;

import com.gary.backendv2.exception.AdminAccountExistsException;
import com.gary.backendv2.model.Location;
import com.gary.backendv2.model.Tutorial;
import com.gary.backendv2.model.dto.request.AddAmbulanceRequest;
import com.gary.backendv2.model.dto.request.users.RegisterEmployeeRequest;
import com.gary.backendv2.model.enums.*;
import com.gary.backendv2.model.users.User;
import com.gary.backendv2.model.dto.request.users.SignupRequest;
import com.gary.backendv2.model.security.Role;
import com.gary.backendv2.repository.*;
import com.gary.backendv2.security.service.AuthService;
import com.gary.backendv2.service.AmbulanceService;
import com.gary.backendv2.utils.Utils;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {
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

    @Value("${gary.app.admin.credentials.email}")
    private String adminEmail;
    @Value("${gary.app.admin.credentials.password}")
    private String adminPassword;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        seed();
    }

    @Transactional
    void seed() {
        var roles = roleRepository.findAll();
        var users = roleRepository.findAll();
        var ambulances = roleRepository.findAll();

        boolean doSeed = roles.isEmpty() && users.isEmpty() && ambulances.isEmpty();
        log.info("Seeding: {}", doSeed);
        if (doSeed) {
            try {
                createRoles();
                createAdminAccount();
                createSampleUsers();
                createSampleAmbulances();
                createSampleEmployees();
                createTutorials();
            } catch (Exception e) {
                log.error("There were some errors during initial database seed process. Shutting down");
                e.printStackTrace();
                SpringApplication.exit(applicationContext, () -> 1);
            }
        }
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

    private void createSampleUsers() {
        SignupRequest s1 = new SignupRequest();
        s1.setEmail("test@test.pl");
        s1.setPassword("test123");
        s1.setBirthDate(LocalDate.of(2000, 1, 1));
        s1.setFirstName("Test");
        s1.setLastName("Testowski");
        s1.setPhoneNumber("123456789");

        SignupRequest s2 = new SignupRequest();
        s2.setEmail("test2@test.pl");
        s2.setPassword("test123");
        s2.setBirthDate(LocalDate.of(1984, 12, 7));
        s2.setFirstName("Robert");
        s2.setLastName("Kubica");
        s2.setPhoneNumber("9876543231");

        SignupRequest s3 = new SignupRequest();
        s3.setEmail("test3@test.pl");
        s3.setPassword("test123");
        s3.setBirthDate(LocalDate.of(1977, 12, 3));
        s3.setFirstName("Adam");
        s3.setLastName("Małysz");
        s3.setPhoneNumber("111222333");

        List<SignupRequest> regular = List.of(s1, s2, s3);
        regular.forEach(x -> authService.registerUser(x));
    }

    private void createSampleEmployees() {
        Map<EmployeeType, List<RegisterEmployeeRequest>> map = new LinkedHashMap<>();

        for (var etype : EmployeeType.values()) {
            map.put(etype, new ArrayList<>());
        }

        RegisterEmployeeRequest s1 = new RegisterEmployeeRequest();
        s1.setEmail("dispatch1@test.pl");
        s1.setPassword("test123");
        s1.setBirthDate(LocalDate.of(2000, 1, 1));
        s1.setFirstName("Dyspozytor");
        s1.setLastName("Dyspozytorski");
        s1.setPhoneNumber("123456789");

        RegisterEmployeeRequest s2 = new RegisterEmployeeRequest();
        s2.setEmail("medic1@test.pl");
        s2.setPassword("test123");
        s2.setBirthDate(LocalDate.of(1984, 12, 7));
        s2.setFirstName("Marcin");
        s2.setLastName("Defibrylator");
        s2.setPhoneNumber("9876543231");

        map.get(EmployeeType.DISPATCHER).add(s1);
        map.get(EmployeeType.MEDIC).add(s2);

        for (var kv : map.entrySet()) {
            for (var emp : kv.getValue()) {
                authService.registerEmployee(kv.getKey(), emp);
            }
        }

    }

    private void createSampleAmbulances() {
        AddAmbulanceRequest a1 = new AddAmbulanceRequest();
        a1.setAmbulanceType(AmbulanceType.A);
        a1.setAmbulanceClass(AmbulanceClass.BASIC);
        a1.setLatitude(Location.defaultLocation().getLatitude());
        a1.setLongitude(Location.defaultLocation().getLongitude());
        a1.setSeats(5);
        a1.setLicensePlate("LBI55362");

        AddAmbulanceRequest a2 = new AddAmbulanceRequest();
        a2.setAmbulanceType(AmbulanceType.B);
        a2.setAmbulanceClass(AmbulanceClass.SPECIAL);
        a2.setLatitude(Location.defaultLocation().getLatitude());
        a2.setLongitude(Location.defaultLocation().getLongitude());
        a2.setSeats(9);
        a2.setLicensePlate("WPI33221");

        AddAmbulanceRequest a3 = new AddAmbulanceRequest();
        a3.setAmbulanceType(AmbulanceType.C);
        a3.setAmbulanceClass(AmbulanceClass.TRANSPORT);
        a3.setLatitude(Location.defaultLocation().getLatitude());
        a3.setLongitude(Location.defaultLocation().getLongitude());
        a3.setSeats(7);
        a3.setLicensePlate("WB32213");

        List<AddAmbulanceRequest> requests = List.of(a1, a2 ,a3);

        @Cleanup
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        requests.forEach(x -> {
            var validationErrors = validator.validate(x);
            if (validationErrors.isEmpty()) {
                ambulanceService.addAmbulance(x);
            } else {
                validationErrors.forEach(y -> log.error(y.getMessage()));
                throw new ValidationException();
            }
        });
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
        tutorial4.setTutorialType(TutorialType.COURSE);
        tutorial4.setTutorialHTML(Utils.loadClasspathResource("classpath:templates/tutorial_burns.html"));

        Tutorial tutorial5 = new Tutorial();
        tutorial5.setName("Udar");
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
