package com.kcwl.framework.rest.helper;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.EnableSpringUtil;
import com.kcwl.ddd.domain.entity.UserAgent;
import com.kcwl.ddd.infrastructure.api.IErrorPromptDecorator;
import com.kcwl.ddd.infrastructure.session.SessionContext;
import com.kcwl.framework.rest.web.CommonWebProperties;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 * 错误码提示语 热更新 单测
 * </p>
 *
 * @author renyp
 * @since 2023/3/2 16:04
 */

@RunWith(SpringRunner.class)
@EnableSpringUtil
@SpringBootTest(classes = {ResponseDecorator.class, CommonWebProperties.class}, properties = {"kcwl.common.web.error.prompt.enable=true"})
class ResponseDecoratorTest {

    private final String expectedMessage = "这是 被替换的提示信息！";
    private final String defaultMessage = "这是 默认的提示信息！";

    @MockBean
    private IErrorPromptDecorator errorPromptDecorator;

    @Autowired
    private ResponseDecorator responseDecorator;

    @BeforeEach
    public void setUp() {
        Mockito.reset(errorPromptDecorator);

        UserAgent userAgent = new UserAgent();
        userAgent.setProduct("1");
        SessionContext.setRequestUserAgent(userAgent);
    }

    /**
     * 合法输入 单测
     */
    @Test
    void testGetErrorPromptMessageCase1() {
        Mockito.when(errorPromptDecorator.getErrorPrompt("404", "1")).thenReturn(expectedMessage);
        Mockito.when(errorPromptDecorator.getErrorPrompt("403", "1")).thenReturn(defaultMessage);

        String failMessage = responseDecorator.getErrorPromptMessage("404", defaultMessage);
        Assert.assertEquals(expectedMessage, failMessage);

        String failMessage1 = responseDecorator.getErrorPromptMessage("403", defaultMessage);
        Assert.assertEquals(defaultMessage, failMessage1);
    }

    /**
     * 部分状态码提示语未定义 单测
     */
    @Test
    void testGetErrorPromptMessageCase2() {

        Mockito.when(errorPromptDecorator.getErrorPrompt("405", "1")).thenReturn(StrUtil.EMPTY);

        String failMessage = responseDecorator.getErrorPromptMessage("405", defaultMessage);
        Assert.assertEquals(defaultMessage, failMessage);

    }

    /**
     * errorPromptDecorator.getErrorPrompt 模拟异常场景，不影响原来提示返回
     */
    @Test
    void testGetErrorPromptMessageCase3() {
        Mockito.when(errorPromptDecorator.getErrorPrompt("406", "1")).thenThrow(new RuntimeException("错误码提示语 异常"));

        String failMessage = responseDecorator.getErrorPromptMessage("406", defaultMessage);
        Assert.assertEquals(defaultMessage, failMessage);

    }

    /**
     * {@Deprecated 货主app 货主web 不区分，统一使用货主app的product获取错误提示语}
     * 2023.08.03 产品设计调整：货主app 和 货主web 改为区分，错误码提示语单独配置 {@link ResponseDecorator#getErrorPromptMessage(String, String)}
     */
    @Test
    void testGetErrorPromptMessageCase4() {
        Mockito.when(errorPromptDecorator.getErrorPrompt("407", "9")).thenReturn(expectedMessage);

        UserAgent userAgent = new UserAgent();
        userAgent.setProduct("7");
        SessionContext.setRequestUserAgent(userAgent);

        String failMessage = responseDecorator.getErrorPromptMessage("407", defaultMessage);
        Assert.assertEquals(defaultMessage, failMessage);

    }
}