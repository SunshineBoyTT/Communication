/**
 * 
 */
package com.anxpp.io.calculator.srt;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

/**
 * @Description:
 * @author zhengjuntao@hjtechcn.cn
 * @Since:2017年9月29日
 * @Version:1.1.0
 */
public class TestChannel {

	/** 
	 * @Title: main 
	 * @Description:通道：用于源节点和目标节点的连接。在NIO中负责缓冲区中数据的传输.channel本身不存储数据，因此需要配合缓存区进行传输
	 * 					二，通道的主要实现类；
	 * 						channel
	 * 							|----filechannel
	 * 							|----socketchannel
	 *  						|----serversocketchannel
	 *  						|----datagramchannel
	 *  				三。获取通道
	 *  					1.JAVA针对支持通道的类提供getChannel方法
	 *  					本地IO
	 *  					fileInputstream/fileoutstream
	 *  					randomAccessFile
	 *  					网络IO
	 *  					socket:   serverSocket   datagramSocket
	 *  					在JDK 1.7中NIO.2针对各个通道提供静态方法()
	 *  					在JKD1.7中files工具类的newByteChannel()
	 *  				四：文件读写
	 *  				五:分散和聚集
	 *  				*分散读取:将通道中的数据分散到多个缓冲区中
	 *  				*聚集写入：将多个缓冲区中的数据聚集到通道中
	 *  				六:编码与解码
	 *  				
	 *  				
	 * @param @param args    
	 * @return void    返回类型 
	 * @throws 
	 */
	
	@Test
	public void send() throws IOException{
		DatagramChannel dc=DatagramChannel.open();
		dc.configureBlocking(false);//非阻塞模式
		ByteBuffer buf=ByteBuffer.allocate(1024);
		Scanner scanner=new Scanner(System.in);
		while(scanner.hasNext()){
			String string=scanner.next();
			buf.put((new Date().toString()+":\n"+string).getBytes());
			buf.flip();
			dc.send(buf, new InetSocketAddress("127.0.0.1", 9898));
			buf.clear();
		}
		dc.close();
	}
	
	@Test
	public void receive() throws IOException{
		DatagramChannel dc=DatagramChannel.open();
		dc.configureBlocking(false);
		dc.bind(new InetSocketAddress(9898));
		Selector selector=Selector.open();
		dc.register(selector, SelectionKey.OP_READ);
		while(selector.select()>0){
			Iterator<SelectionKey> it=selector.selectedKeys().iterator();
			while(it.hasNext()){
				SelectionKey sk=it.next();
				if(sk.isReadable()){
					ByteBuffer buffer=ByteBuffer.allocate(1024);
					dc.receive(buffer);
					buffer.flip();
					System.out.println(new String(buffer.array(),0,buffer.limit()));
					buffer.clear();
				}
			}
			it.remove();
		}
	}
	
	@Test
	public void test4() throws Exception{
		RandomAccessFile file=new RandomAccessFile("1.txt", "rw");
		//1.获取通道
		FileChannel channel1=file.getChannel();
		//分配指定大小的缓冲区
		ByteBuffer buf1=ByteBuffer.allocate(100);
		ByteBuffer buf2=ByteBuffer.allocate(1024);
		//3.分散读取
		ByteBuffer[] buffers={buf1,buf2};
		channel1.read(buffers);
		for(ByteBuffer buf:buffers)
			buf.flip();
		System.out.println(new String(buffers[0].array(),0,buffers[0].limit()));
		System.out.println("//------------");
		System.out.println(new String(buffers[1].array(),0,buffers[1].limit()));
		//	聚集写入
		RandomAccessFile file2=new RandomAccessFile("2.txt", "rw");
		FileChannel channel2=file2.getChannel();
		channel2.write(buffers);
	}
	
	@Test
	public void test5() throws Exception{
		Charset cs1=Charset.forName("GBK");
		//编码器
		CharsetEncoder ce=cs1.newEncoder();
		//解码器
		CharsetDecoder cd=cs1.newDecoder();
		
		CharBuffer charBuffer=CharBuffer.allocate(1024);
		charBuffer.put("祖国万岁！");
		charBuffer.flip();
		ByteBuffer byteBuffer=ce.encode(charBuffer);
		for (int i = 0; i < 10; i++) {
			System.out.println(byteBuffer.get());
		}
		byteBuffer.flip();
		CharBuffer charBuffer1=cd.decode(byteBuffer);
		System.out.println(charBuffer1.toString());
	}
}
