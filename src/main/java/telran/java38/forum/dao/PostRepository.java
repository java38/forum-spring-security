package telran.java38.forum.dao;

import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.java38.forum.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {

	Stream<Post> findByAuthor(String author);

}
