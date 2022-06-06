package com.kcwl.framework.file.biz.service.dfs.aliyun;

public class OSSPath {
	
	public OSSPath(String fileUrl, String key) {
		this.key=key;
		this.fileUrl=fileUrl;
	}

	private String key;//文件在oss的key  唯一的访问值
	
	private String fileUrl;//文件路径

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	
	
}
