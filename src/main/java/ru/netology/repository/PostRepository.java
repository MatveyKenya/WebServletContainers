package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private static final AtomicLong countPosts = new AtomicLong(0);
    private static final Map<Long, Post> posts = new ConcurrentHashMap<>();

    public List<Post> all() {
        return posts.values().stream().toList();
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id)); // null если не нашли
    }

    public synchronized Post save(Post post) {
        long id = post.getId();
        if (id == 0){ // новая запись
            long newId = countPosts.incrementAndGet();
            post.setId(newId);
            posts.put(newId, post);
        } else { // обновление существующей записи
            if (posts.containsKey(id)) {
                posts.put(id, post);
            } else {
                return null; // если не нашли старой записи
            }
        }
        return post;
    }

    public boolean removeById(long id) {
        return posts.remove(id) != null; // true если запись была такая
    }
}
