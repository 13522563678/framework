package com.kcwl.framework.sm;

/**
 * @author 姚华成
 * @date 2018-05-18
 */
 class SM4_Context
{
	 int mode;
	
	 long[] sk;
	
	 boolean isPadding;

	 SM4_Context()
	{
		this.mode = 1;
		this.isPadding = true;
		this.sk = new long[32];
	}
    SM4_Context(int mode)
    {
        this();
        this.mode = mode;
    }
}
