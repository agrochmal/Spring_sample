package pl.demo.core.service.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.model.entity.RoleName;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.RoleRepository;
import pl.demo.core.model.repo.UserRepository;
import pl.demo.core.service.basic_service.CRUDServiceImpl;
import pl.demo.core.service.mail.MailDTOSupplier;
import pl.demo.core.service.mail.SendMailEvent;
import pl.demo.core.service.mail.Template;

/**
 * Created by robertsikora on 05.11.2015.
 */


public class RegistrationServiceImpl extends CRUDServiceImpl<Long, User> implements RegistrationService {

    private final static String EMAIL_TITLE = "Zarejstrowano nowego usera";
    private final static String EMAIL_CONTENT = "registration";

    private UserRepository      userRepository;
    private RoleRepository      roleRepository;
    private PasswordEncoder     passwordEncoder;

    @Transactional
    @Override
    public User register(final User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.addRole(this.roleRepository.findByRoleName(RoleName.USER_ROLE));
        user.setAccountStatus(AccountStatus.ACTIVE);
        User registered = userRepository.save(user);
        publishBusinessEvent(new SendMailEvent(MailDTOSupplier.get(EMAIL_TITLE, EMAIL_CONTENT).get(),
                Template.REGISTRATION_TEMPLATE));
        return registered;
    }

    @Autowired
    public void setRoleRepository(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
