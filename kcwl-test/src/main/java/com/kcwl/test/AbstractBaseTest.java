package com.kcwl.test;

import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;

import javax.annotation.Resource;

/**
 * @author ckwl
 */
public class AbstractBaseTest extends AbstractTransactionalTestNGSpringContextTests {
    @Resource
    private WebApplicationContext context;

    private MockMvc mockMvc;

    public WebApplicationContext getContext() {
        return context;
    }

    @BeforeClass
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    /**
     * 返回MockMvc实例
     *
     * @return
     */
    public MockMvc getMockMvc() {
        return mockMvc;
    }
}
