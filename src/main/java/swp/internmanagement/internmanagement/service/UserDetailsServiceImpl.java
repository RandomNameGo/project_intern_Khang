package swp.internmanagement.internmanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import swp.internmanagement.internmanagement.models.UserAccount;
import swp.internmanagement.internmanagement.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
@Autowired
    UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            logger.info("Attempting to load user by username: {}", userName);
            UserAccount user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found with username: " + userName));
            logger.info("User found: {}", user);
            return UserDetailsImpl.build(user);
        } catch (UsernameNotFoundException ex) {
            logger.error("UsernameNotFoundException: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Exception occurred while loading user by username: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error loading user by username", ex);
        }
    }
}
