/**
 * 
 */
package com.anxpp.io.calculator.srt;

import java.nio.ByteBuffer;

/**
 * @Description:
 * @author zhengjuntao@hjtechcn.cn
 * @Since:2017年9月29日
 * @Version:1.1.0
 */
public class BuffByteDemo {

	/** 
	 * @Title: main 
	 * @Description:
	 * @param @param args    
	 * @return void    返回类型 
	 * @throws 
	 */
	public static void main(String[] args) {
		String str="abcde";
		// TODO Auto-generated method stub
		ByteBuffer buf=ByteBuffer.allocate(1024);
		print(buf);
		//2.利用put()存入数据到缓存区
		buf.put(str.getBytes());
		print(buf);
		//切换成读取数据的模式
		buf.flip();
		print(buf);
		byte[] dst=new byte[buf.limit()];
		buf.get(dst);
		System.out.println(new String(dst));
		print(buf);
		//--------切换成重复读模式 flip的加强版
		System.out.println("rewind");
		buf.rewind();
		print(buf);
		byte[] dst1=new byte[buf.limit()];
		buf.get(dst);
		System.out.println(new String(dst1));
		print(buf);
		//-清空缓存区
		buf.clear();
	}
	
	public static void print(ByteBuffer buf){
		System.out.println("//--------------------");
		System.out.println(buf.position());
		System.out.println(buf.limit());
		System.out.println(buf.capacity());
	}
}
