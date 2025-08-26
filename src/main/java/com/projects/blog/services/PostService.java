package com.projects.blog.services;

import com.projects.blog.domain.CreatePostRequest;
import com.projects.blog.domain.UpdatePostReq;
import com.projects.blog.domain.entities.Post;
import com.projects.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<Post> getAllPosts(UUID categoryId,UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User loggedInUser, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostReq updatePostReq);
    Post getPost(UUID id);
    void deletePost(UUID id);
}
