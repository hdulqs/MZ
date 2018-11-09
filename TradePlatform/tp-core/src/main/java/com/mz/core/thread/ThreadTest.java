/**
 * Copyright:   北京互融时代软件有限公司
 * @author:      Liu Shilei
 * @version:      V1.0 
 * @Date:        2016年6月8日 下午5:36:36
 */
package com.mz.core.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p> TODO</p>
 * @author:         Liu Shilei 
 * @Date :          2016年6月8日 下午5:36:36 
 */
public class ThreadTest {
	 	private int coreCpuNum;  
	    private ExecutorService  executor;  
	    private CompletionService<Long> completionService;  
	      
	    public ThreadTest(){  
	    	coreCpuNum = Runtime.getRuntime().availableProcessors();  
	        executor = Executors.newFixedThreadPool(8);  
	    }  
	    class SumCalculator implements Callable<Long>{  
	    	 int nums[];  
	         int start;  
	         int end;  
	         public SumCalculator(final int nums[],int start,int end){  
	             this.nums = nums;  
	             this.start = start;  
	             this.end = end;  
	         }  
	         @Override  
	         public Long call() throws Exception {  
	             long sum =0;  
	             for(int i=start;i<end;i++){  
	                 sum += nums[i];  
	             }  
	             return sum;  
	         }   
	    }  
	    public long sum(int[] nums){  
	        int start,end,increment;  
	        // 根据CPU核心个数拆分任务，创建FutureTask并提交到Executor   
	        for(int i=0;i<coreCpuNum;i++){  
	            increment = nums.length / coreCpuNum+1;  
	            start = i*increment;  
	            end = start+increment;  
	            if(end > nums.length){  
	                end = nums.length;   
	            }  
	            SumCalculator task = new SumCalculator(nums, start, end);  
	            if(!executor.isShutdown()){  
	                completionService.submit(task);  
	            }  
	        }  
	        return getPartSum();  
	    }  
	    public long getPartSum(){  
	        long sum = 0;  
	        for(int i=0;i<coreCpuNum;i++){  
	            try {  
	                sum += completionService.take().get();  
	            } catch (InterruptedException e) {  
	                e.printStackTrace();  
	            } catch (ExecutionException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        return sum;  
	    }  
	    public void close(){  
	        executor.shutdown();  
	    } 
	    
	    public static void main(String[] args) {  
	        int arr[] = new int[]{1, 22, 33, 4, 52, 61, 7, 48, 10, 11 };  
	        long sum = new ThreadTest().sum(arr);  
	        System.out.println("sum: " + sum);  
	    }
}