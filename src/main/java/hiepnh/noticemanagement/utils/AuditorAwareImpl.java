package hiepnh.noticemanagement.utils;

import hiepnh.noticemanagement.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    @Autowired
    UserAuthenticationUtils userAuthenticationUtils;
    //get current user
    @Override
    public Optional<String> getCurrentAuditor() {
        String username;
        try {
            username = userAuthenticationUtils.getUsername();
        } catch (ResourceNotFoundException e) {
            username = null;
        }
        return Optional.of(username);
    }
}
