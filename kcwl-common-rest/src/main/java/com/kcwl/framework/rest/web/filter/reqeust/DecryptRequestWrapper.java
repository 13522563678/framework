package com.kcwl.framework.rest.web.filter.reqeust;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author ckwl
 */
public class DecryptRequestWrapper extends HttpServletRequestWrapper {
    protected Map<String , String[]> params = new HashMap<String, String[]>();
    protected Enumeration<String> parameterNames = null;

    public DecryptRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 重载一个构造方法
     * @param request
     * @param extendParams
     */
    public DecryptRequestWrapper(HttpServletRequest request , Map<String ,  String[]> extendParams) {
        this(request);
        //这里将扩展参数写入参数表
        addAllParameters(extendParams);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return params;
    }

    /**
     * 重写getParameter，代表参数从当前类中的map获取
     * @param name
     * @return
     */
    @Override
    public String getParameter(String name) {
        String[]values = params.get(name);
        if(values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @SuppressWarnings("all")
    @Override
    public Enumeration<String> getParameterNames() {
        Enumeration<String> paraNames=super.getParameterNames();
        Vector dayNames = new Vector();
        dayNames.addAll(params.keySet());
        parameterNames = dayNames.elements();
        return parameterNames;
    }

    @Override
    public String[] getParameterValues(String name) {//同上
        return params.get(name);
    }

    public void addAllParameters(Map<String ,  String[]>otherParams) {//增加多个参数
        for(Map.Entry<String ,  String[]>entry : otherParams.entrySet()) {
            addParameter(entry.getKey() , entry.getValue());
        }
    }

    public void addParameter(String name , Object value) {//增加参数
        if(value != null) {
            if(value instanceof String[]) {
                params.put(name , (String[])value);
            }else if(value instanceof String) {
                params.put(name , new String[] {(String)value});
            }else {
                params.put(name , new String[] {value.toString().replaceAll("\"", "")});
            }
        }
    }
}
