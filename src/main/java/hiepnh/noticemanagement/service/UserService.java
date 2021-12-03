package hiepnh.noticemanagement.service;

import hiepnh.noticemanagement.dto.GenericDataResponse;
import hiepnh.noticemanagement.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity getUser(String name);

    List<UserEntity> getAll();

    UserEntity createUser(UserEntity userEntity);

    GenericDataResponse deleteUser(Long id);
}
