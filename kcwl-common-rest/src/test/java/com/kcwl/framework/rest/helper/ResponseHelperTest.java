package com.kcwl.framework.rest.helper;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.api.IErrorPromptDecorator;
import com.kcwl.ddd.infrastructure.api.ResponseMessage;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 * 错误码 热更新 单测
 * </p>
 *
 * @author renyp
 * @since 2023/2/28 16:46
 */

@RunWith(SpringRunner.class)
@EnableSpringUtil
@SpringBootTest(classes = ResponseHelperTest.class)
class ResponseHelperTest {

    private final String expectedMessage = "这是 被替换的提示信息！";
    private final String defaultMessage = "这是 默认的提示信息！";

    @MockBean
    private IErrorPromptDecorator errorPromptDecorator;

    @BeforeEach
    public void setUp() {
        Mockito.reset(errorPromptDecorator);
        Mockito.when(errorPromptDecorator.getErrorPrompt("404", "1")).thenReturn(expectedMessage);
        Mockito.when(errorPromptDecorator.getErrorPrompt("403", "1")).thenReturn(defaultMessage);
        Mockito.when(errorPromptDecorator.getErrorPrompt("405", "1")).thenReturn("");

        UserAgent userAgent = new UserAgent();
        userAgent.setProduct("1");
        SessionContext.setRequestUserAgent(userAgent);
    }

    /**
     * 合法输入 单测
     */
    @Test
    void createFailMessageCase1() {

        ResponseMessage<?> failMessage = ResponseHelper.createFailMessage("404", defaultMessage);
        Assert.assertEquals(expectedMessage, failMessage.getMessage());

        ResponseMessage<?> failMessage1 = ResponseHelper.createFailMessage("403", defaultMessage);
        Assert.assertEquals(defaultMessage, failMessage1.getMessage());

    }

    /**
     * 部分状态码提示语未定义 单测
     */
    @Test
    void createFailMessageCase2() {

        ResponseMessage<?> failMessage = ResponseHelper.createFailMessage("405", defaultMessage);
        Assert.assertEquals(defaultMessage, failMessage.getMessage());

    }
}