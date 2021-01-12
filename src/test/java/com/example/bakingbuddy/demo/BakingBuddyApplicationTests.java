package com.example.bakingbuddy.demo;

import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BakingBuddyApplication.class)
@AutoConfigureMockMvc
public class BakingBuddyApplicationTests {
    private User testUser;
    private HttpSession httpSession;

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserRepository usersDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    OrderRepository orderDao;



    @Before
    public void setup() throws Exception {
        testUser = usersDao.findByUsername("testUser");
        if(testUser == null) {
            User newUser = new User();
            newUser.setUsername("testUser");
            newUser.setPassword(passwordEncoder.encode("Password"));
            newUser.setEmail("testUser@codeup.com");
            newUser.setCity("testCity");
            newUser.setState("testState");
            newUser.setFirstName("John");
            newUser.setLastName("Smith");

            testUser = usersDao.save(newUser);
        }

        httpSession = this.mvc.perform(post("/login").with(csrf())
                .param("username", "testUser")
                .param("password", "Password"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();
    }
    
    @Test
    public void contextLoads() {
        assertNotNull(mvc);
    }

    @Test
    public void testIfUserSessionIsActive() throws Exception {
        assertNotNull(httpSession);
    }

    //test create
    @Test
    public void testCreateOrder() throws Exception {
        this.mvc.perform(
                post("/orders/create/9").with(csrf())
                .session((MockHttpSession) httpSession)
                .param("title", "test")
                .param("description", "All the cookies")
                .param("price", "20.00")
                .param("date", "2021-02-24")
                .param("uploadedImage", "https://images.app.goo.gl/nWUWT1kYacCaXSgM7")

        )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testCreateTool() throws Exception {
        this.mvc.perform(
                post("/inventory/tools/add").with(csrf())
                .session((MockHttpSession) httpSession)
                .param("name", "cookie cutter")
                .param("description", "Easter bunny")
        );
    }

    @Test
    public void testCreateConsumables() throws Exception {
        this.mvc.perform(
                post("/inventory/consumables/add")
                .param("type", "flour")
                .param("brand", "Bob's Red Mill")
                .param("amount", "10")
                .param("weightUnit", "lbs")
        );
    }

    //test edit
    



}
