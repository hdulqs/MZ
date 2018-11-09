/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年5月24日 上午9:27:24
 */
package com.mz.sms.utils.hx;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年5月24日 上午9:27:24 
 */
public class Testa {
	
	
	public static void main(String[] args){
		
		for(int i = 0 ; i < 10 ; i++){
			System.out.println(i);
		}
		MyRunnable myRunnable = new MyRunnable();
		Thread thread = new Thread(myRunnable);
		thread.start();
		
		System.out.println("11");
		
	}
	
}
