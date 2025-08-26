package com.projects.blog.mappers;

import com.projects.blog.domain.CreatePostRequest;
import com.projects.blog.domain.UpdatePostReq;
import com.projects.blog.domain.dtos.CreatePostReqDto;
import com.projects.blog.domain.dtos.PostDto;
import com.projects.blog.domain.dtos.UpdatePostDto;
import com.projects.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author" ,source = "author" )
    @Mapping(target = "category" ,source = "category" )
    @Mapping(target = "tags" ,source = "tags" )
    PostDto toDto(Post post);

    @Mapping(source = "status", target = "status")
    CreatePostRequest toCreatePostRequest(CreatePostReqDto createPostReqDto);

    @Mapping(source = "status", target = "status")
    UpdatePostReq toUpdatePostReq(UpdatePostDto updatePostDto);
}
