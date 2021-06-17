package ma.sii.workshop.tomee.rest.dao;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import ma.sii.workshop.tomee.rest.model.User;

import java.util.List;

@Singleton
@Lock(LockType.READ)
public class UserDAO {

    @Inject
    private DAO dao;

    public User create(String name, String pwd, String mail) {
        User user = new User();
        user.setFullname(name);
        user.setPassword(pwd);
        user.setEmail(mail);
        return dao.create(user);
    }

    public List<User> list(int first, int max) {
        return dao.namedFind(User.class, "user.list", first, max);
    }

    public User find(long id) {
        return dao.find(User.class, id);
    }

    public void delete(long id) {
        dao.delete(User.class, id);
    }

    public User update(long id, String name, String pwd, String mail) {
        User user = dao.find(User.class, id);
        if (user == null) {
            throw new IllegalArgumentException("setUser id " + id + " not found");
        }

        user.setFullname(name);
        user.setPassword(pwd);
        user.setEmail(mail);
        return dao.update(user);
    }
}
