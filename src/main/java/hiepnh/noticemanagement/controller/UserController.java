package hiepnh.noticemanagement.controller;

import hiepnh.noticemanagement.entity.UserEntity;
import hiepnh.noticemanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * get a user by name
     *
     * @return user if it's available
     */
    @GetMapping(path = "/{name}")
    public ResponseEntity<UserEntity> getUser(@PathVariable("name") String name) {
        UserEntity user = userService.getUser(name);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * get all user
     *
     * @return a list user
     */
    @GetMapping
    public @ResponseBody List<UserEntity> getAllUsers() {
        List<UserEntity> list = userService.getAll();
        return list;
    }

    /**
     * create a user
     */
    @PostMapping("/registration")
    public ResponseEntity<?> createUser(@RequestBody UserEntity userEntity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userEntity));
    }

    /**
     * delete a user by ID
     */
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

}