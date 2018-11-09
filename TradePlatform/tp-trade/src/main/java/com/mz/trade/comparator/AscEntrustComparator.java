package com.mz.trade.comparator;

import java.util.Comparator;

import com.mz.trade.redis.model.EntrustTrade;


public class AscEntrustComparator  implements Comparator<EntrustTrade>{

	@Override
	public int compare(EntrustTrade o1, EntrustTrade o2) {
		if(o1.getEntrustPrice().compareTo(o2.getEntrustPrice())==1){  
            return 1;  
        }else if(o1.getEntrustPrice().compareTo(o2.getEntrustPrice())==-1){  
            return -1;  
        }else{  
            return 0;  
        }  
	}

}
