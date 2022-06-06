package com.kcwl.tenant.helper;

import com.kcwl.tenant.constants.TenantConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 *  @author ckwl
 */
public class TenantProperties {

    private Properties  properties = null;

    public TenantProperties(String propertiesFile) {
        this.properties = getTenantProperties(propertiesFile);
    }

    public String getProperty(String key) {
        Object valObj = null;
        if ( properties != null ) {
            valObj = properties.get(key);
        }
        return (valObj!=null) ? valObj.toString() : null;
    }

    public boolean getPropertyBoolean(String key) {
        return str2boolean(getProperty(key));
    }

    public List<String> getPropertyList(String key) {
        Object valObj = null;
        List<String> list = new ArrayList<String>();
        for ( int i=0; ; i++) {
            valObj = properties.get(key + "[" + i + "]");
            if ( valObj == null ) {
                break;
            }
            list.add(valObj.toString());
        }
        return list;
    }

    public Set<String> getPropertySet(String key) {
        return new HashSet(getPropertyList(key));
    }

    protected Properties getTenantProperties(String propertiesFile) {
        Properties prop = new Properties();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFile);
            prop.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    private boolean str2boolean(String val) {
        if ( val != null ) {
            return TenantConstant.BOOL_FLAG_TRUE.equals(val.toLowerCase());
        }
        return false;
    }
}
