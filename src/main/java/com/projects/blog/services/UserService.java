package com.projects.blog.services;

import com.projects.blog.domain.entities.Post;
import com.projects.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User getUserById(UUID id);

}
