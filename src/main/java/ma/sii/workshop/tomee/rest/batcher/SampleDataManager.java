package ma.sii.workshop.tomee.rest.batcher;

import ma.sii.workshop.tomee.rest.dao.CommentDAO;
import ma.sii.workshop.tomee.rest.dao.PostDAO;
import ma.sii.workshop.tomee.rest.dao.UserDAO;
import ma.sii.workshop.tomee.rest.model.Post;
import ma.sii.workshop.tomee.rest.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Startup
@DependsOn({"CommentDAO", "PostDAO", "UserDAO"})
@Singleton
@Lock(LockType.READ)
public class SampleDataManager {

    private static final Logger LOGGER = Logger.getLogger(SampleDataManager.class.getName());

    @PersistenceContext(unitName = "blog")
    private EntityManager em;

    @Inject
    private CommentDAO comments;

    @Inject
    private PostDAO posts;

    @Inject
    private UserDAO users;

    @PostConstruct
    public void createSomeData() {
        final User tomee = users.create("tomee", "tomee", "tomee@apache.org");
        final User openejb = users.create("openejb", "openejb", "openejb@apache.org");
        final Post tomeePost = posts.create("TomEE", "TomEE is a cool JEE App Server", tomee.getId());
        posts.create("OpenEJB", "OpenEJB is a cool embedded container", openejb.getId());
        comments.create("visitor", "nice post!", tomeePost.getId());
    }

    // a bit ugly but at least we clean data
    @Schedule(second = "0", minute = "30", hour = "*", persistent = false)
    private void cleanData() {
        LOGGER.info("Cleaning data");
        deleteAll();
        createSomeData();
        LOGGER.info("Data reset");
    }

    private void deleteAll() {
        em.createQuery("delete From Comment").executeUpdate();
        em.createQuery("delete From Post").executeUpdate();
        em.createQuery("delete From User").executeUpdate();
    }
}
