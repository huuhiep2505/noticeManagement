package hiepnh.noticemanagement.service.impl;

import hiepnh.noticemanagement.dto.GenericDataResponse;
import hiepnh.noticemanagement.entity.UserEntity;
import hiepnh.noticemanagement.repository.UserRepository;
import hiepnh.noticemanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    /**
     * get a user by Name
     *
     * @param name input
     */
    @Override
    public UserEntity getUser(String name) {
        return userRepository.findByName(name);
    }
    /**
     * get all user
     *
     */
    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }
    /**
     * create a user
     *
     * @param @UserEntity input
     */
    @Override
    public UserEntity createUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    /**
     * delete a user by id
     *
     * @param id input
     */
    @Override
    public GenericDataResponse deleteUser(Long id) {
        GenericDataResponse genericDataResponse = new GenericDataResponse();
        Optional<UserEntity> userEntity = userRepository.findById(id);
        userRepository.delete(userEntity.get());
        return genericDataResponse;
    }
}
