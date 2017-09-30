/**
 * 
 */
package com.anxpp.io.calculator.srt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;

/**
 * @Description:利用通道完成文件的复制
 * @author zhengjuntao@hjtechcn.cn
 * @Since:2017年9月29日
 * @Version:1.1.0
 */
public class TestChannel_pic {
	
	@Test
	public void test1() throws Exception{
		FileInputStream fis=new FileInputStream("git.jpg");
		FileOutputStream fos=new FileOutputStream("2.JPG");
		//获取通道
		FileChannel inChannel=fis.getChannel();
		FileChannel outChannel=fos.getChannel();
		//分配指定大小的缓存区
		ByteBuffer buf=ByteBuffer.allocate(1024);
		//将通道中的数据存入缓冲区中
		while(inChannel.read(buf)!=-1){
			buf.flip();//切换成写模式
			//讲缓冲区中的数据写入通道
			outChannel.write(buf);
			buf.clear();
		}
		
		
	}
}
