package com.kcwl.framework.mpp;

import com.kcwl.framework.securtiy.DbEncryptManager;
import com.kcwl.framework.securtiy.encrypt.AES;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ckwl
 */
@Slf4j
public class AesTypeHandler extends BaseTypeHandler<String> {

    private static String ENCRYPT_TYPE_AES = "aes";

    @SneakyThrows
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        String key = DbEncryptManager.getInstance().getEncryptKey(ENCRYPT_TYPE_AES);
        if ( !StringUtils.isEmpty(parameter) && !StringUtils.isEmpty(key)  ) {
            ps.setString(i, AES.encrypt(parameter, key));
        } else {
            ps.setString(i, parameter);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getRealResult(rs.getString(columnName));
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getRealResult(rs.getString(columnIndex));
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getRealResult(cs.getString(columnIndex));
    }

    private String getRealResult(String columnValue) {
        String key = DbEncryptManager.getInstance().getEncryptKey(ENCRYPT_TYPE_AES);
        if ( !StringUtils.isEmpty(columnValue) && !StringUtils.isEmpty(key)  ) {
            try {
                return AES.decrypt(columnValue, key);
            } catch (Exception e) {
                log.error("decrypt error: {}", e);
            }
        }
        return columnValue;
    }
}
