package telran.java38.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.java38.forum.dao.PostRepository;
import telran.java38.forum.model.Post;

@Service("customSecurity")
public class CustomWebSecurityService {
	
	PostRepository postRepository;

	@Autowired
	public CustomWebSecurityService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}
	
	public boolean checkPostAuthority(String postId, String userName) {
		Post post = postRepository.findById(postId).orElse(null);
		return post == null || userName.equals(post.getAuthor());
	}

}
