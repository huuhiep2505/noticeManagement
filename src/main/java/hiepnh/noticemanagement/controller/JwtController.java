package hiepnh.noticemanagement.controller;

import hiepnh.noticemanagement.dto.AuthRequest;
import hiepnh.noticemanagement.utils.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static hiepnh.noticemanagement.utils.Constants.INVALID_USER;

@RestController
@Log4j2
public class JwtController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Qualifier("customAccountService")
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * @param authenicate request with username, password
     * @return token for each correct user
     * @throws Exception
     */
    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            userDetailsService.loadUserByUsername(authRequest.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            log.error("Exception when generate token with username = {}", authRequest.getUsername());
            throw new Exception(INVALID_USER);
        }
        return jwtUtil.generateToken(authRequest.getUsername());
    }
}
