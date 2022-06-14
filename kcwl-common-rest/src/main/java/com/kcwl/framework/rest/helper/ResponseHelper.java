package com.kcwl.framework.rest.helper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.gson.Gson;
import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.api.ResponseMessage;
import com.kcwl.ddd.interfaces.dto.ListResultDTO;
import com.kcwl.ddd.interfaces.dto.PageInfoDTO;
import com.kcwl.ddd.interfaces.dto.PageResultDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ckwl
 */
public class ResponseHelper {
    private static  final Gson gson = new Gson();
    public static final Object EMPTY_OBJECT = new Object();

    /**
     * 响应请求
     * @param response
     * @param responseMessage
     * @throws Exception
     */
    public static void response(HttpServletResponse response, ResponseMessage responseMessage) throws Exception{
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(responseMessage));
    }

    /**
     * 构造ResponseBody
     * @param code
     * @param message
     * @param response
     * @throws Exception
     */
    public static void buildResponseBody(String code, String message, HttpServletResponse response)throws Exception{
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage(message);
        responseMessage.setCode(code);
        response(response,responseMessage);
    }

    /**
     * 构造ResponseBody
     * @param commonCode
     * @param response
     * @throws Exception
     */
    public static void buildResponseBody(CommonCode commonCode, HttpServletResponse response)throws Exception{
        buildResponseBody(commonCode.getCode(), commonCode.getDescription(), response);
    }

    /**
     * 返回正确结果
     * @param
     * @return
     */
    public static ResponseMessage success() {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(CommonCode.SUCCESS.getCode());
        responseMessage.setMessage(CommonCode.SUCCESS.getDescription());
        responseMessage.setResult("");
        return responseMessage;
    }

    /**
     * 返回正确结果
     * @param result
     * @return
     */
    public static ResponseMessage success(Object result) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(CommonCode.SUCCESS.getCode());
        responseMessage.setMessage(CommonCode.SUCCESS.getDescription());
        responseMessage.setResult(result);
        return responseMessage;
    }

    /**
     * 返回正确结果
     * @param result
     * @return
     */
    public static ResponseMessage success(String message, Object result) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(CommonCode.SUCCESS.getCode());
        responseMessage.setMessage(message);
        responseMessage.setResult(result);
        return responseMessage;
    }

    /**
     * 返回正确结果
     * @param message
     * @return
     */
    public static ResponseMessage successMessage(String message) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(CommonCode.SUCCESS.getCode());
        responseMessage.setMessage(message);
        responseMessage.setResult(EMPTY_OBJECT);
        return responseMessage;
    }


    /**
     * 返回分页结果
     * @param page
     * @return
     */
    public static ResponseMessage successPage(PageInfoDTO page) {
        PageResultDTO pageResultDTO = new PageResultDTO(page);
        return success(pageResultDTO);
    }
    /**
     * 返回分页结果
     * @param page
     * @return
     */
    public static ResponseMessage successPage(IPage page) {
        PageInfoDTO pageInfoDTO = new PageInfoDTO(page.getTotal(), (int)page.getCurrent(),  (int)page.getSize(), page.getRecords());
        PageResultDTO pageResultDTO = new PageResultDTO(pageInfoDTO);
        return success(pageResultDTO);
    }

    /**
     * 返回分页结果
     * @param list
     * @return
     */
    public static ResponseMessage successList(List list) {
        return success(new ListResultDTO(list));
    }

    /**
     * 根据指定错误码返回结果
     * @param code
     * @param message
     * @return
     */
    public static ResponseMessage fail(String code, String message) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(code);
        responseMessage.setMessage(message);
        responseMessage.setResult(EMPTY_OBJECT);
        return responseMessage;
    }
}
