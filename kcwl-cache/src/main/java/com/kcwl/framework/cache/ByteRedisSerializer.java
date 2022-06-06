package com.kcwl.framework.cache;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author ckwl
 */
public class ByteRedisSerializer<T> implements RedisSerializer<Object> {
    private final Charset charset;

    public ByteRedisSerializer() {
        this(Charset.forName("UTF8"));
    }

    public ByteRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Nullable
    @Override
    public byte[] serialize(@Nullable Object obj) throws SerializationException {
        if(obj == null){
            return null;
        }
        byte[] bs = null;
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try{
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            bs = baos.toByteArray();
        }catch(Exception e){

        }
        finally{
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bs;
    }


    @Nullable
    @Override
    public Object deserialize(@Nullable byte[] bytes) throws SerializationException {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try{
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        }catch(Exception e){

        }
        finally{
            if(ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bais != null){
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
