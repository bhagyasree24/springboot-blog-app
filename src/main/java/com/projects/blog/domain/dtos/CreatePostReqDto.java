package com.projects.blog.domain.dtos;

import com.projects.blog.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePostReqDto {

    @NotBlank(message = "Title is required")
    @Size(min = 3,max = 200,message = "Title must be between {min} and {max} characters")
    private String title;

    @NotBlank
    @Size(min = 10,max = 50000,message = "Content must be between {min} and {max} characters")
    private String content;

    @NotNull(message = "Category Id is required")
    private UUID categoryId;

    @Builder.Default
    @Size(max = 10,message = "Maximum of 10 tags are allowed")
    private Set<UUID> tagIds = new HashSet<>();

    @NotNull(message = "Status is required")
    private PostStatus status;


}
