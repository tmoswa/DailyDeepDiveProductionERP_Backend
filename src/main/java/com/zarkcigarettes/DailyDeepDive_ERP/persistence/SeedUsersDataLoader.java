package com.zarkcigarettes.DailyDeepDive_ERP.persistence;

import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.*;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.*;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SeedUsersDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SubsidiaryRepository subsidiaryRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityTypeRepository entityTypeRepository;

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        // == create initial privileges
        //Global Priviledges
        final Privilege readToAllPrivilege = createPrivilegeIfNotFound("PRIVILEGE-READ-TO-ALL");
        final Privilege writeToAllPrivilege = createPrivilegeIfNotFound("PRIVILEGE-WRITE-TO-ALL");
        final Privilege deleteToAllPrivilege = createPrivilegeIfNotFound("PRIVILEGE-WRITE-TO-ALL");

        //Roles Priviledges
        final Privilege rolesReadPrivilege = createPrivilegeIfNotFound("PRIVILEGE-ROLES-READ");
        final Privilege rolesAlterPrivilege = createPrivilegeIfNotFound("PRIVILEGE-ROLES-ALTER");
        final Privilege rolesDeletePrivilege = createPrivilegeIfNotFound("PRIVILEGE-ROLES-DELETE");

        //Permissions Priviledges
        final Privilege permissionsReadPrivilege = createPrivilegeIfNotFound("PRIVILEGE-PERMISSIONS-READ");
        final Privilege permissionsAlterPrivilege = createPrivilegeIfNotFound("PRIVILEGE-PERMISSIONS-ALTER");
        final Privilege permissionsDeletePrivilege = createPrivilegeIfNotFound("PRIVILEGE-PERMISSIONS-DELETE");


        //DESGINATIONS Privileges
        final Privilege desginationsReadPrivilege = createPrivilegeIfNotFound("PRIVILEGE-DESGINATIONS-READ");
        final Privilege desginationsAlterPrivilege = createPrivilegeIfNotFound("PRIVILEGE-DESGINATIONS-ALTER");
        final Privilege desginationsDeletePrivilege = createPrivilegeIfNotFound("PRIVILEGE-DESGINATIONS-DELETE");

        //SUBSIDIARIES Privileges
        final Privilege subsidiariesReadPrivilege = createPrivilegeIfNotFound("PRIVILEGE-SUBSIDIARIES-READ");
        final Privilege subsidiariesAlterPrivilege = createPrivilegeIfNotFound("PRIVILEGE-SUBSIDIARIES-ALTER");
        final Privilege subsidiariesDeletePrivilege = createPrivilegeIfNotFound("PRIVILEGE-SUBSIDIARIES-DELETE");

        //USERS Privileges
        final Privilege usersReadPrivilege = createPrivilegeIfNotFound("PRIVILEGE-USERS-READ");
        final Privilege usersAlterPrivilege = createPrivilegeIfNotFound("PRIVILEGE-USERS-ALTER");
        final Privilege usersDeletePrivilege = createPrivilegeIfNotFound("PRIVILEGE-USERS-DELETE");

        final Privilege passwordPrivilege = createPrivilegeIfNotFound("PRIVILEGE-CHANGE-PASSWORD");

        String[] priviledges={
                "PRIVILEGE-ENTITY-READ","PRIVILEGE-ENTITY-ALTER","PRIVILEGE-ENTITY-DELETE",
                "PRIVILEGE-PRODUCTION_PLAN-READ","PRIVILEGE-PRODUCTION_PLAN-ALTER","PRIVILEGE-PRODUCTION_PLAN-DELETE",
                "PRIVILEGE-PURCHASE_ORDER-READ","PRIVILEGE-PURCHASE_ORDER-ALTER","PRIVILEGE-PURCHASE_ORDER-DELETE","PRIVILEGE-PURCHASE_ORDER-DELIVER",
                "PRIVILEGE-INVOICE-READ","PRIVILEGE-INVOICE-ALTER","PRIVILEGE-INVOICE-DELETE",
                "PRIVILEGE-MATERIAL-READ","PRIVILEGE-MATERIAL-ALTER","PRIVILEGE-MATERIAL-DELETE",
                "PRIVILEGE-MATERIAL_USAGE-READ","PRIVILEGE-MATERIAL_USAGE-ALTER","PRIVILEGE-MATERIAL_USAGE-DELETE","PRIVILEGE-MATERIAL_REQUIRED-READ",
                "PRIVILEGE-MATERIAL_REQUIRED_EXPECTED-READ",
                "PRIVILEGE-PRODUCTION_PLAN-READ","PRIVILEGE-PRODUCTION_PLAN-ALTER","PRIVILEGE-PRODUCTION_PLAN-DELETE",
                "PRIVILEGE-DAILY_PRODUCTION_RUN-READ","PRIVILEGE-DAILY_PRODUCTION_RUN-ALTER","PRIVILEGE-DAILY_PRODUCTION_RUN-DELETE",
                "PRIVILEGE-FINISHED_PRODUCT-READ","PRIVILEGE-FINISHED_PRODUCT-ALTER","PRIVILEGE-FINISHED_PRODUCT-DELETE",
                "PRIVILEGE-FINISHED_PRODUCT_MOVEMENT-READ","PRIVILEGE-FINISHED_PRODUCT_MOVEMENT-ALTER","PRIVILEGE-FINISHED_PRODUCT_MOVEMENT-DELETE",
                "PRIVILEGE-DELAYED_PURCHASE_ORDERS-READ","PRIVILEGE-NTMS_DELIVERED-READ","PRIVILEGE-NTMS_USED-READ","PRIVILEGE-PRODUCED_PRODUCTS-READ","CONFIGURATIONS"
        };



        //password Privileges

        // == create initial roles
         List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(
                readToAllPrivilege,writeToAllPrivilege,deleteToAllPrivilege,
                rolesReadPrivilege,rolesAlterPrivilege,rolesDeletePrivilege,
                permissionsReadPrivilege,permissionsAlterPrivilege,permissionsDeletePrivilege,
                desginationsReadPrivilege,desginationsAlterPrivilege,desginationsDeletePrivilege,
                subsidiariesReadPrivilege,subsidiariesAlterPrivilege,subsidiariesDeletePrivilege,
                usersReadPrivilege,usersAlterPrivilege,usersDeletePrivilege, passwordPrivilege));

        for(String priviledge:priviledges){
            Privilege pp = createPrivilegeIfNotFound(priviledge);
            adminPrivileges.add(pp);
        }

        final List<Privilege> userGlobalPrivileges = new ArrayList<>(Arrays.asList(
                readToAllPrivilege,usersReadPrivilege, passwordPrivilege,
                rolesReadPrivilege,subsidiariesReadPrivilege,desginationsReadPrivilege,
                permissionsReadPrivilege
                ));

        final Subsidiary sub0 = createSubsidiaryIfNotFound("Subsidiary-Cavendish-Lloyd-Global");
        final Subsidiary sub2 = createSubsidiaryIfNotFound("Subsidiary-Haltrade-Distribution-Zimbabwe");
        final Subsidiary sub3 = createSubsidiaryIfNotFound("Subsidiary-Haltrade-Distribution-Zambia");

        final Designation des1 = createDesignationIfNotFound("Systems Developer");
        final Designation des2 = createDesignationIfNotFound("Global NTMs Director");
        final Designation des3 = createDesignationIfNotFound("Global Operations Director");
        final Designation des4 = createDesignationIfNotFound("Haltrade Accountant");
        final Designation des5 = createDesignationIfNotFound("Haltrade Assistant Accountant");

        final List<Subsidiary> subs = new ArrayList<>(Arrays.asList(sub0,sub2,sub3));


        final Role developerRole = createRoleIfNotFound("ROLE-GLOBAL-DEVELOPER", adminPrivileges);
        final Role adminGlobalRole = createRoleIfNotFound("ROLE-GLOBAL-ADMIN", adminPrivileges);
        final Role userGlobalRole =createRoleIfNotFound("ROLE-USER", userGlobalPrivileges);


        // == create initial user
        createUserIfNotFound("tmoswa@haltradedistribution.com", "Timothy", "Moswa", "@m*dam-202","tmoswa", new ArrayList<>(Arrays.asList(developerRole)),sub0,des1);


        createClientTypeIfNotFound("Customer");
        createClientTypeIfNotFound("Supplier");
        alreadySetup = true;
    }



    @Transactional
    Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findPrivilegeByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }
    @Transactional
    Subsidiary createSubsidiaryIfNotFound(final String name) {
        Subsidiary subsidiary = subsidiaryRepository.findByName(name);
        if (subsidiary == null) {
            subsidiary = new Subsidiary(name);
        }
        subsidiary = subsidiaryRepository.save(subsidiary);
        return subsidiary;
    }

    @Transactional
    Designation createDesignationIfNotFound(final String name) {
        Designation designation = designationRepository.findByName(name);
        if (designation == null) {
            designation = new Designation(name);
        }
        designation = designationRepository.save(designation);
        return designation;
    }

    @Transactional
    User createUserIfNotFound(final String email, final String firstName, final String lastName, final String password,final String username, final Collection<Role> roles,final Subsidiary subsidiaries,final Designation designation_s) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);

            user.setUsername(username);

            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setEnabled(true);
        }
        user.setRoles(roles);
        user.setSubsidiaries(subsidiaries);
        user.setDesignations(designation_s);
        user = userRepository.save(user);
        return user;
    }


    @Transactional
    EntityType createClientTypeIfNotFound(final String name) {
        EntityType entityType = entityTypeRepository.findClientTypeByName(name);
        if (entityType == null) {
            entityType = new EntityType(name);
            entityType = entityTypeRepository.save(entityType);
        }
        return entityType;
    }

}