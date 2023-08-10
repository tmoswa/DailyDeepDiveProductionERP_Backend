package com.zarkcigarettes.DailyDeepDive_ERP.auth;

import com.zarkcigarettes.DailyDeepDive_ERP.persistence.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    Boolean update(Long id,User user);

    void addRoleToUser(String username,String roleName);

    User getUser(String username);
    User getUserByToken(String verificationToken);
    List<User> getUsers();
    void createVerificationTokenForUser(User user, String token);
    Boolean deleteUser(User user);
    VerificationToken getVerificationToken(String VerificationToken);
    VerificationToken generateNewVerificationToken(String token);
    VerificationToken generateNewVerificationTokenByUser(User user);
    void createPasswordResetTokenForUser(User user, String token);
    User findUserByEmail(String email);
    PasswordResetToken getPasswordResetToken(String token);
    Optional<User> getUserByPasswordResetToken(String token);
    Optional<Privilege> findPrivilegeByName(String name);
    Optional<User> selectUserByMyUsername(String username);
    Optional<User> selectUserByEmail(String email);
    Boolean deletePreviledge(Long privilegeID);
    Boolean updatePrivilegeDetails(Long privilegeID, Privilege privilegeDetails);
    Collection<Privilege> privilegeList(int limit);
    Collection<User> userList(int limit);
    void addSubsidiaryToUser(String username,String subsidiary);
    List<Privilege> getAllPrivilegeDetails();
    Privilege savePrivilege(Privilege permission);
    Subsidiary saveSubsidiary(Subsidiary subsidiary);
    Collection<Subsidiary> subsidiaryList(int limit);
    Boolean deleteSubsidiary(Long subsidiaryID);
    Boolean updateSubsidiaryDetails(Long subsidiaryID, Subsidiary subsidiaryDetails);
    Role saveRole(Role role);
    Role saveRolePreviledge(String name,Collection<Privilege> privileges);
    Collection<Role> roleList(int limit);
    Boolean deleteRole(Long roleID);
    Boolean updateRoleDetails(Long roleID, Role roleDetails);
    Collection<Designation> designationList(int limit);
    Designation saveDesignation(Designation designation);
    Boolean deleteDesignation(Long designationID);
    Boolean updateDesignationDetails(Long designationID, Designation designationDetails);
    String validateVerificationToken(String token);

    void updateUserCredentials(User user,String password, String secret);
}
