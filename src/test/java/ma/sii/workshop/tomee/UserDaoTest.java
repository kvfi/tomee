package ma.sii.workshop.tomee;

import org.apache.tomee.embedded.EmbeddedTomEEContainer;
import org.apache.ziplock.Archive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ma.sii.workshop.tomee.rest.dao.UserDAO;
import ma.sii.workshop.tomee.rest.model.User;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;

import static org.apache.openejb.loader.JarLocation.jarLocation;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class UserDaoTest {

    private static EJBContainer container;

    @BeforeClass
    public static void start() throws IOException {
        final File webApp = Archive.archive().copyTo("WEB-INF/classes", jarLocation(UserDAO.class)).asDir();
        final Properties p = new Properties();
        p.setProperty(EJBContainer.APP_NAME, "rest-example");
        p.setProperty(EJBContainer.PROVIDER, "tomee-embedded"); // need web feature
        p.setProperty(EJBContainer.MODULES, webApp.getAbsolutePath());
        p.setProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT, "-1"); // random port
        container = EJBContainer.createEJBContainer(p);
    }

    @AfterClass
    public static void stop() {
        if (container != null) {
            container.close();
        }
    }

    @Test
    public void create() throws NamingException {
    	final UserDAO dao = (UserDAO) container.getContext().lookup("java:global/rest-example/UserDAO");
        final User user = dao.create("foo", "dummy", "foo@bar.org");
        assertNotNull(dao.find(user.getId()));
    }
}
