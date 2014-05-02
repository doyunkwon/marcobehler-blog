package marcobehler.blog.jta;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
public class ApplicationTests {

    @Autowired
    private UserRegistryXA service;

    @Test
    public void myXATest() {
        String username = "hans";
        int atmWidthDrawalLimit = 100;

        // this method will commit to two databases...could of course also a db&jms queue..etc..etc.
        service.registerUser(username, atmWidthDrawalLimit);

        List<Map<String, Object>> users = service.findAllUsers();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).get("name")).isEqualTo(username);
    }

}
