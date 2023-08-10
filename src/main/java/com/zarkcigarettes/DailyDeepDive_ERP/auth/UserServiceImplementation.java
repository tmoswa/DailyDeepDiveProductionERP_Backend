package com.zarkcigarettes.DailyDeepDive_ERP.auth;

import com.zarkcigarettes.DailyDeepDive_ERP.persistence.dao.*;
import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.*;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImplementation implements UserService, UserDetailsService {

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;



    @Autowired
    private Environment env;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SubsidiaryRepository subsidiaryRepository;
    private final DesignationRepository designationRepository;
    private final PasswordEncoder passwordEncoder;

    private final PrivilegeRepository privilegeRepository;

    @Override
    public User saveUser(User user) {
        log.info(String.format("saving userFirstName %s , Password %s to the database", user.getFirstName(),user.getPassword()));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public Boolean update(Long userID,User userDetails) {
        User details = userRepository.findById(userID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("user with id %d not found", userID)));
//&& !Objects.equals(details.getName(), roleDetails.getName())
        //ToDo: ensure the roname
        if (details.getUsername().length() > 0 ) {
            Collection<Role> roles = new ArrayList();
            for(Role rl:userDetails.getRoles()){
                assert roles != null;
                roles.add(rl);
            }
            details.setFirstName(userDetails.getFirstName());
            details.setLastName(userDetails.getLastName());
            details.setEmail(userDetails.getEmail());
            details.setUsing2FA(userDetails.isUsing2FA());
            details.setDesignations(userDetails.getDesignations());
            details.setSubsidiaries(userDetails.getSubsidiaries());
            details.setEnabled(userDetails.isEnabled());
            details.setRoles(roles);

            return  Boolean.TRUE;
        }
        return  Boolean.FALSE;
    }

    @Override
    public Role saveRole(Role roleDetails) {
        return roleRepository.save(roleDetails);
    }
    @Override
    public Role saveRolePreviledge(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }


    @Override
    public Collection<Role> roleList(int limit) {
        return  roleRepository.findAll(PageRequest .of(0,limit)).toList();
    }

    @Override
    public Boolean deleteRole(Long roleID) {
        boolean exists = roleRepository.existsById(roleID);
        if (!exists) {
            return  Boolean.FALSE;
        }
        roleRepository.deleteById(roleID);
        return  Boolean.TRUE;
    }

    @Override
    public Boolean updateRoleDetails(Long roleID, Role roleDetails) {
        Role details = roleRepository.findById(roleID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("roleDetails with id %d not found", roleID)));
//&& !Objects.equals(details.getName(), roleDetails.getName())
        //ToDo: ensure the roname
      if (details.getName().length() > 0 ) {
/*  
            Collection<Privilege> privileges = new ArrayList();
            for(Privilege pr:roleDetails.getPrivileges()){
                assert privileges != null;
                Privilege priv=privilegeRepository.findPrivilegeByName(pr.getName());
                privileges.add(priv);
            }*/
            details.setName(roleDetails.getName());
            details.setPrivileges(roleDetails.getPrivileges());
            return  Boolean.TRUE;
        }
        return  Boolean.FALSE;
    }

    @Override
    public Subsidiary saveSubsidiary(Subsidiary subsidiary) {
        log.info("saving subsidiary %s to the database", subsidiary.getName());
        return subsidiaryRepository.save(subsidiary);
    }

    @Override
    public Collection<Subsidiary> subsidiaryList(int limit) {
        return  subsidiaryRepository.findAll(PageRequest .of(0,limit)).toList();
    }

    @Override
    public Collection<Designation> designationList(int limit) {
        return  designationRepository.findAll(PageRequest .of(0,limit)).toList();
    }

    @Override
    public Designation saveDesignation(Designation designation) {
        log.info("saving Designation %s to the database", designation.getName());
        return designationRepository.save(designation);
    }

    @Override
    public Boolean deleteDesignation(Long designationID) {
        boolean exists = designationRepository.existsById(designationID);
        if (!exists) {
            return  Boolean.FALSE;
        }
        designationRepository.deleteById(designationID);
        return  Boolean.TRUE;
    }

    @Override
    public Boolean updateDesignationDetails(Long designationID, Designation designationDetails) {
        Designation details = designationRepository.findById(designationID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("designationDetails with id %d not found", designationID)));

        if (designationDetails.getName().length() > 0 && !Objects.equals(details.getName(), designationDetails.getName())) {

            Optional<Designation> designationDetailsByName =
                    designationRepository
                            .findApplicationDesignationDetailsByName(designationDetails.getName());
            if (designationDetailsByName.isPresent()) {
                return  Boolean.FALSE;
            }

            details.setName(designationDetails.getName());
            return  Boolean.TRUE;
        }
        return  Boolean.FALSE;
    }

    @Override
    public Boolean deleteSubsidiary(Long subsidiaryID) {
        boolean exists = subsidiaryRepository.existsById(subsidiaryID);
        if (!exists) {
            return  Boolean.FALSE;
        }
        subsidiaryRepository.deleteById(subsidiaryID);
        return  Boolean.TRUE;
    }

    @Override
    public Boolean updateSubsidiaryDetails(Long subsidiaryID, Subsidiary subsidiaryDetails) {
        Subsidiary details = subsidiaryRepository.findById(subsidiaryID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("subsidiaryDetails with id %d not found", subsidiaryID)));

        if (subsidiaryDetails.getName().length() > 0 && !Objects.equals(details.getName(), subsidiaryDetails.getName())) {

            Optional<Privilege> subsidiaryDetailsByName =
                    subsidiaryRepository
                            .findApplicationPreviledgeDetailsByName(subsidiaryDetails.getName());
            if (subsidiaryDetailsByName.isPresent()) {
                return  Boolean.FALSE;
            }

            details.setName(subsidiaryDetails.getName());
            return  Boolean.TRUE;
        }
        return  Boolean.FALSE;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("adding role %s to  user %s and save", roleName, username);
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public void addSubsidiaryToUser(String username, String subsidiaryName) {
        log.info("adding subsidiary %s to  user %s and save", subsidiaryName, username);
        User user = userRepository.findByUsername(username);
        Subsidiary sub = subsidiaryRepository.findByName(subsidiaryName);
        //user.getSubsidiaries().add(sub);
    }

    @Override
    public User getUser(String username) {
        log.info(String.format("getting application user %s from the database", username));
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("getting all application users from the database");
        return userRepository.findAll();
    }


    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public Boolean deleteUser(final User user) {
        final VerificationToken verificationToken = tokenRepository.findByUser(user);

        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }

        final PasswordResetToken passwordToken = passwordTokenRepository.findByUser(user);

        if (passwordToken != null) {
            passwordTokenRepository.delete(passwordToken);
        }

        //userRepository.deleteById(user.getId());
        userRepository.findById(user.getId());
        userRepository.delete(user);

        return true;
    }


    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
                .toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public VerificationToken generateNewVerificationTokenByUser(final User userDetails) {
        VerificationToken vToken = tokenRepository.findByUser(userDetails);
        if(vToken==null){

            vToken=new VerificationToken(UUID.randomUUID()
                    .toString(),userDetails);
        }else{
            vToken.updateToken(UUID.randomUUID()
                    .toString());
        }

        vToken = tokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }


    @Override
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
    }


    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user.equals("")) {
            log.info("User Not Found");
            throw new UsernameNotFoundException("User Not Found");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

    }

    @Override
    public Optional<Privilege> findPrivilegeByName(String name) {

        Optional<Privilege> user = getPrivileges().stream()
                .filter(applicationUser -> name.equals(applicationUser.getName()))
                .findFirst();
        log.info(String.format("Privilege with name: %s ", name));
        log.info(user.toString());
        return user;
    }


    @Override
    public Optional<User> selectUserByMyUsername(String username) {

        Optional<User> user = getAppUsers().stream()
                .filter(applicationUser -> username.equals(applicationUser.getEmail()))
                .findFirst();
        log.info(String.format("User with username: %s ", username));
        log.info(user.toString());
        return user;
    }

    /**
     * This is the method that fetches users by email
     */
    @Override
    public Optional<User> selectUserByEmail(String email) {
        Optional<User> user = getAppUsers().stream()
                .filter(applicationUser -> email.equals(applicationUser.getEmail()))
                .findFirst();
        log.info(String.format("User with username: %s ", email));
        log.info(user.toString());
        return user;
    }

    private List<User> getAppUsers() {
        List<User> applicationUsers = Lists.newArrayList();
        applicationUsers = userRepository.findAll();
        return applicationUsers;
    }
    private List<Privilege> getPrivileges() {
        List<Privilege> privileges = Lists.newArrayList();
        privileges = privilegeRepository.findAll();
        return privileges;
    }


    @Override
    public List<Privilege> getAllPrivilegeDetails(){
        return privilegeRepository.findAll();
    }

    @Override
    public Collection<Privilege> privilegeList(int limit){

        return  privilegeRepository.findAll(PageRequest .of(0,limit)).toList();
    }

    @Override
    public Collection<User> userList(int limit){
        return  userRepository.findAll(PageRequest .of(0,limit)).toList();
    }

    @Override
    public Privilege savePrivilege(Privilege permission) {
        log.info("saving permission %s to the database", permission.getName());
       return privilegeRepository.save(permission);
    }

    @Override
    public Boolean deletePreviledge(Long privilegeID) {
        boolean exists = privilegeRepository.existsById(privilegeID);
        if (!exists) {
            return  Boolean.FALSE;
        }
        privilegeRepository.deleteById(privilegeID);
        return  Boolean.TRUE;
    }

    @Override
    public Boolean updatePrivilegeDetails(Long privilegeID, Privilege privilegeDetails) {
        Privilege details = privilegeRepository.findById(privilegeID)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("permission with id %d not found", privilegeID)));

        if (privilegeDetails.getName().length() > 0 && !Objects.equals(details.getName(), privilegeDetails.getName())) {

            Optional<Privilege> privilegeDetailsByName =
                    privilegeRepository
                            .findApplicationPreviledgeDetailsByName(privilegeDetails.getName());
            if (privilegeDetailsByName.isPresent()) {
                return  Boolean.FALSE;
            }

            details.setName(privilegeDetails.getName());
            return  Boolean.TRUE;
        }
        return  Boolean.FALSE;

    }

    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);

        log.info(String.format("Token %s",token));
        log.info(String.format("Verification token %s",verificationToken.toString()));
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();

        log.info(String.format("Verification token %s",verificationToken.toString()));

        if ((verificationToken.getExpiryDate()
            .getTime() - cal.getTime()
            .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        // tokenRepository.delete(verificationToken);
        userRepository.save(user);
        return TOKEN_VALID;
    }

    @Override
    public void updateUserCredentials(User user,String password, String secret) {
        user.setPassword(password);
        user.setSecret(secret);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
          userRepository.save(user);
    }

    @Override
    public User getUserByToken(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }


}
