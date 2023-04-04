package com.kcwl.framework.mpp;

import com.kcwl.framework.securtiy.encrypt.EncryptType;
import com.kcwl.framework.securtiy.encrypt.IKcCrypt;
import com.kcwl.framework.securtiy.encrypt.KcEncryptFactory;
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
public class KcEncryptTypeHandler extends BaseTypeHandler<String> {
    @SneakyThrows
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        IKcCrypt kcCrypt = KcEncryptFactory.getKcCrypt(getEncryptName());
        if ( !StringUtils.isEmpty(parameter) && (kcCrypt != null) ) {
            ps.setString(i, kcCrypt.encrypt(parameter));
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
        IKcCrypt kcCrypt = KcEncryptFactory.getKcCrypt(getEncryptName());
        if ( !StringUtils.isEmpty(columnValue) && (kcCrypt != null) ) {
            try {
                return kcCrypt.decrypt(columnValue);
            } catch (Exception e) {
                log.error("decrypt error: {}", e);
            }
        }
        return columnValue;
    }

    String getEncryptName() {
        return EncryptType.ENCRYPT_SIMPLE.getName();
    }
}
