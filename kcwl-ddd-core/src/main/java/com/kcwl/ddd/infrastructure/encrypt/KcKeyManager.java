package com.kcwl.ddd.infrastructure.encrypt;

import com.kcwl.ddd.application.constants.ProductEnum;

import java.util.HashMap;

public class KcKeyManager {

    private static KcKeyManager kcKeyManager = new KcKeyManager();

    private final String defaultKey = "123456kcwl654321";

    private HashMap<String, String> paramPriKeyMap;

    private KcKeyManager() {
        initKey();
    }

    public static KcKeyManager getInstance(){
        return kcKeyManager;
    }

    public String getParamPrivateKey(String productType) {
        String paramKey =paramPriKeyMap.get(productType);
        return (paramKey!=null) ? paramKey : defaultKey;
    }

    private void initKey() {
        paramPriKeyMap = new HashMap<>();
        paramPriKeyMap.put(ProductEnum.CARRIER_APP.getId().toString(), defaultKey);
        paramPriKeyMap.put(ProductEnum.SHIPPER_APP.getId().toString(), defaultKey);
        paramPriKeyMap.put(ProductEnum.SHIPPER_WEB.getId().toString(), "sw7109@kcwl#ygds");
        paramPriKeyMap.put(ProductEnum.SPMS_WEB.getId().toString(), "op1102$kcwl#sxty");
        paramPriKeyMap.put(ProductEnum.OPMS_WEB.getId().toString(), "ic8112$kcwl#sxty");
        paramPriKeyMap.put(ProductEnum.TMS_WEB.getId().toString(), "tm5113$kcwl@qycd");
        paramPriKeyMap.put(ProductEnum.BPMS_WEB.getId().toString(), "bp6114$kcwl#jymm");
        paramPriKeyMap.put(ProductEnum.FMGT_WEB.getId().toString(), "fg1102$kcwl#sxty");
        paramPriKeyMap.put(ProductEnum.DA_WEB.getId().toString(), "da1102$kcwl#sxty");

    }
}
