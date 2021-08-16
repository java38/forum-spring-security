package telran.java38.forum.service;

import telran.java38.forum.dto.NewCommentDto;
import telran.java38.forum.dto.NewPostDto;
import telran.java38.forum.dto.PostDto;

public interface ForumService {
	PostDto addNewPost(NewPostDto newPost, String author);

	PostDto getPost(String id);

	PostDto removePost(String id);

	PostDto updatePost(NewPostDto postUpdateDto, String id);

	boolean addLike(String id);

	PostDto addComment(String id, String author, NewCommentDto newCommentDto);

	Iterable<PostDto> findPostsByAuthor(String author);
}
