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

package com.kcwl.framework.file.biz.service;




/**
 * oss 配置
 * IMG中的压缩比例
 */
public class IMGConstant {
	//原图
    public static final short IMG_COMPRESS_TYPE_YT = 0;
	//按等比压缩
    public static final short IMG_COMPRESS_TYPE_DB = 1;
    
    //按宽高比例压缩
    public static final short IMG_COMPRESS_TYPE_W_H = 2;
    
    //按宽高且强制压缩
    public static final short IMG_COMPRESS_TYPE_W_H_QZ = 3;
  
    
  
}
