package com.kcwl.framework.securtiy.codec;

import com.kcwl.ddd.infrastructure.api.CommonCode;
import com.kcwl.ddd.infrastructure.exception.BizException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ckwl
 */
public class PassDict {
    private static final int MAX_KEY_LENGTH = 62;
    private static final int MAX_GROUP_COUNT = 128;
    private static final int DEFAULT_GROUP_ID = 0;
    private static final String BASE_KEY ="7140862935aFYNQptcfxlRKTwOBnSqbjdEUrPzyVgDHLZIvoesGWMJhXAmCuik";
    private static final String ALPHABET="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DEFAULT_RSA_KEY="KRSA#0001";
    public static final String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCX0jK9ZGQkhgCZWqipx4A+R0l4qIX3ux8Q9YenevBXCf46JiaPuMIAi6JrXiuBZcimdGQxWyv8Enea7lwefSKD7QOjTE3hHnlKgT0D7WeHhFATsUh9pjGfrLmJOyHojz1XYxbmB/QFbcMztyLorMjE2LjMoV6vsfOy6i6KvpxSMwIDAQAB";
    public static final String RSA_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJfSMr1kZCSGAJlaqKnHgD5HSXiohfe7HxD1h6d68FcJ/jomJo+4wgCLomteK4FlyKZ0ZDFbK/wSd5ruXB59IoPtA6NMTeEeeUqBPQPtZ4eEUBOxSH2mMZ+suYk7IeiPPVdjFuYH9AVtwzO3IuisyMTYuMyhXq+x87LqLoq+nFIzAgMBAAECgYBha8qPaYoxPaPIxDoSqCgMzqXAvTMqInLu6P8hY2wyDgGKwsxf0Uj8HJ4ek5ELOYsl9O4OFdkFqbwCOQ33qoioaP83VTCs6uTu8EwTWtYvmZBwndMDPEhw5gmj3yWAtzbef2oXanWT8OhgRdz3fleVbj615gOjDkZ0qLZdtlC0MQJBAPGSCNOCKbbR6Pn9wZ3lmNYeVgs2p74tdq01czDURwld3ADY5jpqbVM0zEfFaYRaLl4vngffoM9zUK8+qorHzN0CQQCg48PEMcPf4ZDCVmfNsLF0bK2TiraDAPNzSHdvHVWScJ6qJ8AywL5g4EjjDhWR5VrGPPFCKC8sRY+7i6ymbeJPAkEAwp8k/UKg2OnFCaOnmc6gk+Fc8SvXeSCnvGSTqYNct+8mkIgu9GFKMv8Wlbw2hqshMECs0SsaJAiDuKyEOcs/AQJAIfMNyyeJIoPRbQCYlNG1EFEia2C41Hnt5SSkdyDkfqse/961csAzK/QHzUmaiZexCOWxLvmVwqzHAJcfjlrfhwJANmXVk8ohYkBpDBCD0axBg9bv3C9l4EOxcyDHzGRQI2Do2VqMzz3GQ+Cjm6LEoSNgUTrEQPL852umvzu2kzqqlA==";

    private Map<Integer, PassTable> passTableMap;

    public PassDict(String key){
        passTableMap = createPassTableMap(key, DEFAULT_GROUP_ID);
    }

    public PassDict(String key, int count){
        passTableMap = createPassTableMap(key, count);
    }

    public Character encode(Character ch) {
        return encode(ch, DEFAULT_GROUP_ID);
    }

    public Character encode(Character ch, int groupId) {
        PassTable passTable = passTableMap.get(groupId);
        return (passTable != null) ? passTable.encode(ch) : ch;
    }

    public Character decode(Character ch) {
        return decode(ch, 0);
    }

    public Character decode(Character ch, int groupId) {
        PassTable passTable = passTableMap.get(groupId);
        return (passTable != null) ? passTable.decode(ch) : ch;
    }

    private Map<Integer, PassTable> createPassTableMap(String key, int count) {
        if ( (key == null) || (key.length() == 0) || (key.length()>MAX_KEY_LENGTH) ) {
            throw new BizException(CommonCode.ERROR_SECRET_KEY.getCode(), CommonCode.ERROR_SECRET_KEY.getDescription());
        }
        if ( count < 0 || count > MAX_GROUP_COUNT ){
            throw new BizException(CommonCode.ERROR_SECRET_GROUP.getCode(), CommonCode.ERROR_SECRET_GROUP.getDescription());
        }
        Map<Integer, PassTable> map = new HashMap<Integer, PassTable>();
        for ( int i=0; i<count; i++ ) {
            map.put(i, new PassTable(getPassword(key, i)));
        }
        return map;
    }

    private String getPassword(String key, int seed) {
        StringBuilder sb = new StringBuilder(key.length());
        String unRepeatKey = getUnRepeatKey(key);
        int startPos = seed%key.length();
        for ( int i=startPos; i<unRepeatKey.length(); i++) {
            sb.append(unRepeatKey.charAt(i));
        }
        for ( int i=0; i<startPos; i++) {
            sb.append(unRepeatKey.charAt(i));
        }
        return sb.toString();
    }

    private String getUnRepeatKey(String key) {
        int keySize = key.length();
        Set<Character> usedKey = new HashSet<Character>();
        StringBuilder sb = new StringBuilder();
        for ( int i=0; i<keySize; i++ ) {
            Character ch = key.charAt(i);
            if ( !usedKey.contains(ch) ) {
                sb.append(ch);
                usedKey.add(ch);
            }
        }
        int leftKeySize = MAX_KEY_LENGTH - usedKey.size();
        for (int i = 0; i< BASE_KEY.length() && leftKeySize >0; i++) {
            Character ch = BASE_KEY.charAt(i);
            if ( !usedKey.contains(ch) ) {
                sb.append(ch);
                usedKey.add(ch);
                leftKeySize--;
            }
        }
        return sb.toString();
    }

    private static class PassTable{
        private Map<Character, Character> dictPass  = new HashMap<>();
        private Map<Character, Character> dictPlain = new HashMap<>();

        public PassTable(String password) {
            initPassMapping(password);
        }
        public Character encode(Character ch) {
            Character encodeChar = dictPass.get(ch);
            return (encodeChar!=null) ? encodeChar : ch;
        }
        public Character decode(Character ch) {
            Character decodeChar = dictPlain.get(ch);
            return (decodeChar!=null) ? decodeChar : ch;
        }
        private void initPassMapping(String key) {
            for ( int i=0; i<key.length(); i++) {
                Character ch = ALPHABET.charAt(i);
                Character da = key.charAt(i);
                dictPass.put(ch, da);
                dictPlain.put(da, ch);
            }
        }
    }
}
