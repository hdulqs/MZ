package com.mz.front.user.entrust.model;

import java.util.Comparator;

public class DescEntrustComparator  implements Comparator<Entrust>{

	@Override
	public int compare(Entrust o1, Entrust o2) {
		if(o1.getEntrust_price().compareTo(o2.getEntrust_price())==1){  
            return 1;  
        }else if(o1.getEntrust_price().compareTo(o2.getEntrust_price())==-1){  
            return -1;  
        }else{  
            return 0;  
        }  
	}

}
