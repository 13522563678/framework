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
