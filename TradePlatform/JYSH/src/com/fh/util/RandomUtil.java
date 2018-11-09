package com.fh.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomUtil
{
   private ArrayList<Integer> al = null;

  public List<Integer> createRandom(int max, int type, int num)
  {
     this.al = new ArrayList(num);

     while (this.al.size() < num) {
       int temp = (int)(Math.random() * max);
       if ((type == 1) && 
         (temp % 2 != 0))
      {
         this.al.add(Integer.valueOf(temp));
      }

       if ((type != 2) || 
         (temp % 2 != 0))
        continue;
       this.al.add(Integer.valueOf(temp));
    }

     return this.al;
  }

  public List<Integer> createRandom(int min, int max, int type, int num)
  {
     this.al = new ArrayList(num);

     while (this.al.size() < num) {
       int temp = (int)(Math.random() * max);
       if ((type == 1) && 
         (temp % 2 != 0) && (temp > min))
      {
         this.al.add(Integer.valueOf(temp));
      }

       if ((type != 2) || (temp <= min) || 
         (temp % 2 != 0))
        continue;
       this.al.add(Integer.valueOf(temp));
    }

     return this.al;
  }

  public static String GetRandomNumber()
  {
     Set set = new HashSet();

     Random random = new Random();
     while (set.size() < 4)
    {
       set.add(Integer.valueOf(random.nextInt(10)));
    }
     Iterator iterator = set.iterator();
     StringBuffer temp = new StringBuffer();
     while (iterator.hasNext()) {
       temp.append(iterator.next());
    }

     return temp.toString();
  }

  public static String GetRandomNumber6()
  {
     Set set = new HashSet();

     Random random = new Random();
     while (set.size() < 6)
    {
       set.add(Integer.valueOf(random.nextInt(10)));
    }
     Iterator iterator = set.iterator();
     StringBuffer temp = new StringBuffer();
     while (iterator.hasNext()) {
       temp.append(iterator.next());
    }

     return temp.toString();
  }

  public static void main(String[] args) {
     RandomUtil ru = new RandomUtil();
     System.out.println(GetRandomNumber6());
  }

  public static String getRandomNumberString(int strLen)
  {
     Random random = new Random();
     String ss = "0123456789";
     StringBuffer s = new StringBuffer();
     for (int i = 0; i < strLen; i++) {
       int n = random.nextInt(ss.length());
       char r = ss.charAt(n);
       s.append(r);
    }
     return s.toString();
  }
  
  //字母大小写+数字
  public static String getRandomString(int length){  
      String str="1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  
      Random random=new Random();  
        
      StringBuffer sb=new StringBuffer();  
        
      for(int i=0;i<length;i++){  
            
          int number =random.nextInt(62);  
            
          sb.append(str.charAt(number));  
      }  
      return sb.toString();  
  }
  
  
  
  
  
  
	/**
	 * 
	 * @param len
	 * @param flag 0-包括数字和大小写字母,1-// 只有数字,2-// 只有字母
	 * @return
	 */
	public static String Random(int len, int flag) {
		StringBuffer sb = new StringBuffer();
		int templen = 0;
		while (templen < len) {
			// SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			// int k = (int) (126 * random.nextDouble() + 1);
			int k = (int) (126 * Math.random() + 1);
			if (flag == 0) {// 包括数字和大小写字母
				if ((k >= 48 && k <= 57) || (k >= 65 && k <= 90)
						|| (k >= 97 && k <= 122)) {
					sb.append((char) k);
					templen++;
				}
			} else if (flag == 1) {// 只有数字
				if ((k >= 48 && k <= 57)) {
					sb.append((char) k);
					templen++;
				}
			} else if (flag == 2) {// 只有字母
				if ((k >= 65 && k <= 90) || (k >= 97 && k <= 122)) {
					sb.append((char) k);
					templen++;
				}
			}
		}
		return sb.toString();
	}
  
  
  
}

