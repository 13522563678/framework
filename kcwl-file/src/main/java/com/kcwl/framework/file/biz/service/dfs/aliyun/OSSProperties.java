/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.kcwl.framework.file.biz.service.dfs.aliyun;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * oss 配置
 */
@ConfigurationProperties(prefix = "kcwl.common.oss")
@Data
public class OSSProperties {

    //@Value("${ossEndpoint:http://oss-cn-beijing.aliyuncs.com}")
    private   String endpoint;
    //@Value("${imgEndpoint:http://img-cn-beijing.aliyuncs.com}")
    private   String imgEndpoint;
    //@Value("${ossAccessKeyid:5UeNTfCruCaKtYYV}")
    private   String accessKeyid; //new PropertiesUtil("sysConfig.properties").readProperty("oss.access.keyId");
    //@Value("${ossAccessKeysecret:f59cRvHwdtmel4xyhmud0Y7Mb3Cdvl}")
    private   String accessKeysecret;//new PropertiesUtil("sysConfig.properties").readProperty("oss.access.keySecret");
    //@Value("${ossBucketName:kcwl-test}")
    private   String bucketName;//new PropertiesUtil("sysConfig.properties").readProperty("oss.bucket.name");
    //@Value("${ossAccessUrl:http://kcwl-release-web.oss-cn-beijing.aliyuncs.com}")
    private   String accessUrl;
    //@Value("${ossKey:upload/}")
    private  String key;//key的前缀
    //@Value("${expiresTime:60/}")
    private String expiresTime;//过期时间


    /*
    public static String OSS_APK_BUCKET_NAME = "kcwl-app-test";
    
    public static String OSS_APK_KEY = "download/appv2/";//OSS上传APK的前缀
    
    public static  String OSS_APK_ACCESS_URL = "http://kcwl-app-test.oss-cn-beijing.aliyuncs.com";
    */
}
