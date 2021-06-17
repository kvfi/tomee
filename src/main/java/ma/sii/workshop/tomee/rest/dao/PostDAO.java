package ma.sii.workshop.tomee.rest.dao;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import ma.sii.workshop.tomee.rest.model.Post;
import ma.sii.workshop.tomee.rest.model.User;

import java.util.List;

@Singleton
@Lock(LockType.READ)
public class PostDAO {

    @Inject
    private DAO dao;

    public Post create(String title, String content, long userId) {
        User user = dao.find(User.class, userId);
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        return dao.create(post);
    }

    public Post find(long id) {
        return dao.find(Post.class, id);
    }

    public List<Post> list(int first, int max) {
        return dao.namedFind(Post.class, "post.list", first, max);
    }

    public void delete(long id) {
        dao.delete(Post.class, id);
    }

    public Post update(long id, long userId, String title, String content) {
        User user = dao.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("user id " + id + " not found");
        }

        Post post = dao.find(Post.class, id);
        if (post == null) {
            throw new IllegalArgumentException("post id " + id + " not found");
        }

        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        return dao.update(post);
    }
}
