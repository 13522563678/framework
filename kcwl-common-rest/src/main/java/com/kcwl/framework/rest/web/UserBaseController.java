package com.kcwl.framework.rest.web;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.api.ResponseMessage;
import com.kcwl.ddd.interfaces.dto.ListResultDTO;
import com.kcwl.ddd.interfaces.dto.PageInfoDTO;
import com.kcwl.ddd.interfaces.dto.PageResultDTO;


import java.util.List;

/**
 * @author ckwl
 */
public class UserBaseController {

    /**
     * 返回正确结果
     * @param result
     * @return
     */
    public ResponseMessage success(Object result) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(CommonCode.SUCCESS.getCode());
        responseMessage.setMessage(CommonCode.SUCCESS.getDescription());
        responseMessage.setResult(result);
        return responseMessage;
    }

    /**
     * 返回分页结果
     * @param pageInfo
     * @return
     */
    public ResponseMessage successPage(PageInfoDTO pageInfo) {
        PageResultDTO pageQueryInfo = new PageResultDTO(pageInfo);
        return success(pageQueryInfo);
    }

    /**
     * 返回分页结果
     * @param list
     * @return
     */
    public ResponseMessage successList(List list) {
        return success(new ListResultDTO(list));
    }

    /**
     * 根据指定错误码返回结果
     * @param code
     * @param message
     * @return
     */
    public ResponseMessage fail(String code, String message) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(code);
        responseMessage.setMessage(message);
        responseMessage.setResult("");
        return responseMessage;
    }
}
