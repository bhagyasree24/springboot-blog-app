package com.projects.blog.services.impl;

import com.projects.blog.domain.CreatePostRequest;
import com.projects.blog.domain.PostStatus;
import com.projects.blog.domain.UpdatePostReq;
import com.projects.blog.domain.entities.Category;
import com.projects.blog.domain.entities.Post;
import com.projects.blog.domain.entities.Tag;
import com.projects.blog.domain.entities.User;
import com.projects.blog.repositories.PostRepository;
import com.projects.blog.services.CategoryService;
import com.projects.blog.services.PostService;
import com.projects.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MIN = 200;

    @Override
    @Transactional
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {

        if(categoryId!=null && tagId!=null){
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED,category,tag);
        }
        if(categoryId!=null){
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED,category);
        }

        if(tagId!=null){
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED,tag);
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user,PostStatus.DRAFT);
    }

    @Transactional
    @Override
    public Post createPost(User loggedInUser, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(loggedInUser);
        newPost.setReadingTime(calcReadingTime(createPostRequest.getContent()));

        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);

    }

    private Integer calcReadingTime(String content){
        if(content==null || content.isEmpty()){
            return 0;
        }

        int wordCount =  content.trim().split("\\s+").length;
        return (int) Math.ceil((double)wordCount/WORDS_PER_MIN);
    }

    @Transactional
    @Override
    public Post updatePost(UUID id, UpdatePostReq updatePostReq) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Post does not exist with id "+id));
        existingPost.setTitle(updatePostReq.getTitle());
        existingPost.setContent(updatePostReq.getContent());
        existingPost.setStatus(updatePostReq.getStatus());
        existingPost.setReadingTime(calcReadingTime(updatePostReq.getContent()));

        UUID updatePostReqCategoryId = updatePostReq.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostReqCategoryId)){
             Category category = categoryService.getCategoryById(updatePostReqCategoryId);
             existingPost.setCategory(category);
        }
        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> updatePostReqTagIds= updatePostReq.getTagIds();

        if (!existingTagIds.equals(updatePostReqTagIds)){
             List<Tag> newTags = tagService.getTagByIds(updatePostReqTagIds);
             existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepository.save(existingPost);
    }

    @Override
    public Post getPost(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Post does not exist with Id "+id));
    }

    @Override
    public void deletePost(UUID id) {
        Post post = getPost(id);
        postRepository.delete(post);

        }

}
