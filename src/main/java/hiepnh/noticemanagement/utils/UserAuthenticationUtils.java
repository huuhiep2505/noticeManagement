package hiepnh.noticemanagement.utils;

import hiepnh.noticemanagement.exception.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationUtils {
    public String getUsername() throws ResourceNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        if(username == null){
            throw new ResourceNotFoundException("Authenticated user not found");
        }
        return username;
    }


}
