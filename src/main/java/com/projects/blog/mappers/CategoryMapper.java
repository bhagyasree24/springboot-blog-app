package com.projects.blog.mappers;

import com.projects.blog.domain.PostStatus;
import com.projects.blog.domain.dtos.CategoryDto;
import com.projects.blog.domain.dtos.CreateCategoryRequest;
import com.projects.blog.domain.entities.Category;
import com.projects.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("calculatePostCount")
    default Integer calculatePostCount(List<Post> posts) {
        if (null == posts) {
            return 0;
        }
        return (int) posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus())).count();

    }

}
