package hiepnh.noticemanagement.impl;

import hiepnh.noticemanagement.entity.UserEntity;
import hiepnh.noticemanagement.repository.UserRepository;
import hiepnh.noticemanagement.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void getUser_success(){
        UserEntity user = new UserEntity();
        user.setName("abc");
        Mockito.when(userRepository.findByName("abc")).thenReturn(user);
        UserEntity user1 = userService.getUser("abc");
        Assert.assertEquals(user1.getName(), user.getName());
    }
    @Test
    public void createUser_success(){
        UserEntity user = new UserEntity();
        user.setPassword("123");
        UserEntity user1 = new UserEntity();
        Mockito.when(userRepository.save(any())).thenReturn(user);
        Mockito.when(passwordEncoder.encode(any())).thenReturn("user");
        user1 = userService.createUser(user);
        Assert.assertEquals(user, user1);
    }

    @Test
    public void getAllUser_success(){
        List<UserEntity> user = new ArrayList<>();
        Mockito.when(userRepository.findAll()).thenReturn(user);
        List<UserEntity> user1 = new ArrayList<>();
        user1 = userService.getAll();
        Assert.assertEquals(user1, user);
    }

    @Test
    public void deleteUser_success(){
        UserEntity user = new UserEntity();
        user.setId(1L);
        Mockito.when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        userService.deleteUser(1L);
        Mockito.verify(userRepository, Mockito.timeout(1)).findById(any());
    }
}
