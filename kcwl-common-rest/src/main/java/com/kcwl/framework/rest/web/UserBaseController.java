package com.kcwl.framework.rest.web;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.api.ResponseMessage;
import com.kcwl.ddd.interfaces.dto.ListResultDTO;
import com.kcwl.ddd.interfaces.dto.PageInfoDTO;
import com.kcwl.ddd.interfaces.dto.PageResultDTO;
import com.kcwl.framework.rest.helper.ResponseHelper;


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
        return ResponseHelper.success(result);
    }

    /**
     * 返回分页结果
     * @param pageInfoDTO
     * @return
     */
    public ResponseMessage successPage(PageInfoDTO pageInfoDTO) {
        return ResponseHelper.successPage(pageInfoDTO);
    }

    /**
     * 返回分页结果
     * @param list
     * @return
     */
    public ResponseMessage successList(List list) {
        return ResponseHelper.successList(list);
    }

    /**
     * 根据指定错误码返回结果
     * @param code
     * @param message
     * @return
     */
    public ResponseMessage fail(String code, String message) {
        return ResponseHelper.fail(code, message);
    }
}
