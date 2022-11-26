package edu.shmonin.bookshelftest.service;

import edu.shmonin.bookshelftest.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User saveUser(User user);

    User getUser(String username);

    List<User> getUsers();
}