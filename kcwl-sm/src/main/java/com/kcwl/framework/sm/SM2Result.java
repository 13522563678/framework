package com.kcwl.framework.sm;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * @author 姚华成
 * @date 2018-05-18
 */
class SM2Result
{
	 SM2Result() {
	}

	// -------------- 签名/验签---------
	 BigInteger r;
	 BigInteger s;
	 BigInteger R;

	// -------------- 密钥交换-----------
	 byte[] sa;
	 byte[] sb;
	 byte[] s1;
	 byte[] s2;

	 ECPoint keyra;
	 ECPoint keyrb;
}
