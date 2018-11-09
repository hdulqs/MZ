package com.mz.manage.remote.ico;

import com.mz.manage.remote.ico.model.AppIcoCoinAccountDTO;
import com.mz.manage.remote.ico.model.AppIcoCoinTransactionDTO;
import com.mz.manage.remote.ico.model.AppIcoEvaluateDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectHomePageDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectRepayDTO;
import com.mz.manage.remote.ico.model.AppIcoProjectSuportDTO;
import com.mz.manage.remote.ico.model.AppPersonInfoDTO;
import com.mz.manage.remote.model.RemoteResult;
import com.mz.manage.remote.model.User;
import com.mz.manage.remote.model.base.FrontPage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * ico业务远程接口
 * <p> TODO</p>
 * @author:         Shangxl 
 * @Date :          2017年7月14日 下午3:33:35
 */
public interface RemoteIcoService {
	/**
	 * 根据查询条件查询list
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param map
	 * @param:    @return
	 * @return: List<AppIcoProjectDTO> 
	 * @Date :          2017年7月20日 上午9:53:05   
	 * @throws:
	 */
	public FrontPage listProject(Map<String,String> map);
	
	/**
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param map
	 * @param:    @return
	 * @return: List<AppPersonInfoDTO> 
	 * @Date :          2017年7月20日 下午2:58:43   
	 * @throws:
	 */
	public List<AppPersonInfoDTO> getPersonInfo(Map<String,String> map);
	
	/**
	 * 保存项目完善信息
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param projectDTO
	 * @param:    @return
	 * @return: AppIcoProjectDTO
	 * @Date :          2017年7月21日 上午10:46:42   
	 * @throws:
	 */
	public AppIcoProjectDTO saveProjectStep(AppIcoProjectDTO projectDTO);
	
	/**
	 * 根据查询条件查询单个项目
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param map
	 * @param:    @return
	 * @return: AppIcoProjectDTO 
	 * @Date :          2017年7月21日 下午3:50:24   
	 * @throws:
	 */
	public AppIcoProjectDTO getProject(Map<String,Object> map);
	
	/**
	 *　保存项目的主页/项目详情
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param homePageDTO
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2017年7月21日 下午5:17:29   
	 * @throws:
	 */
	public boolean saveProjectHomePage(AppIcoProjectHomePageDTO homePageDTO);
	
	/**
	 * 根据条件查询项目详情页对象
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param map
	 * @param:    @return
	 * @return: AppIcoProjectHomePageDTO 
	 * @Date :          2017年7月21日 下午5:43:15   
	 * @throws:
	 */
	public AppIcoProjectHomePageDTO getProjectHomePageDTO(Map<String,Object> map);
	
	/**
	 * 根据查询条件查询项目回报list
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param map
	 * @param:    @return
	 * @return: List<AppIcoProjectRepayDTO> 
	 * @Date :          2017年7月21日 下午7:13:50   
	 * @throws:
	 */
	public List<AppIcoProjectRepayDTO> listProjectRepayDTO(Map<String,String> map);
	
	/**
	 * 保存项目回报
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param projectRepayDTO
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2017年7月24日 上午10:47:51   
	 * @throws:
	 */
	public boolean saveProjectRepay(AppIcoProjectRepayDTO projectRepayDTO);
	/**
	 * 
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param map
	 * @param:    @return
	 * @return: boolean 
	 * @Date :          2017年7月25日 下午7:31:41   
	 * @throws:
	 */
	public boolean remoteProject(Map<String,Object> map);
	/**
	 * 浏览项目
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param map
	 * @param:    @return
	 * @return: List<AppIcoProjectDTO> 
	 * @Date :          2017年7月26日 下午5:05:02   
	 * @throws:
	 */
	public List<AppIcoProjectDTO> browseProject(Map<String,Object> map);
	
	/**
	 * 查询项目支持者 
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param map
	 * @param:    @return
	 * @return: List<AppIcoProjectSuportDTO> 
	 * @Date :          2017年7月26日 下午6:19:36   
	 * @throws:
	 */
	public List<AppIcoProjectSuportDTO> listProjectSuport(Map<String,Object> map);
	
	/**
	 * 查询支持的项目
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param map
	 * @param:    @return
	 * @return: FrontPage 
	 * @Date :          2017年7月27日 上午9:41:21   
	 * @throws:
	 */
	public FrontPage listIsuportProject(Map<String,String> map);
	
	/**
	 * 查询分享的项目
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param map
	 * @param:    @return
	 * @return: FrontPage 
	 * @Date :          2017年7月27日 上午10:09:08   
	 * @throws:
	 */
	public FrontPage listIshareProject(Map<String,String> map);
	
	/**
	 * 查询评论
	 * @param project
	 * @return
	 */
	public List<AppIcoEvaluateDTO> listEvaluate(Long project);
	
	
	/**
	 * 查询一个ICO虚拟账户
	 * @param id
	 * @return
	 */
	public AppIcoCoinAccountDTO appIcoCoinAccountDTO(Long id);
	
	/**
	 * 支付订单
	 * @param user
	 * @param projectId
	 * @param getMoney
	 * @param proportions
	 * @return
	 */
	public RemoteResult immediatePayment(User user,Long projectId,BigDecimal getMoney,String proportions);
	
	/**
	 * 根据用户、订单号查询交易流水
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param userName
	 * @param:    @param txid
	 * @param:    @return
	 * @return: AppIcoCoinTransaction 
	 * @Date :          2017年8月18日 下午4:28:33   
	 * @throws:
	 */
	public AppIcoCoinTransactionDTO getTransaction(String userName, String txid);
	/**
	 * 保存充值、提现订单
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param dto
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年8月18日 下午7:18:00   
	 * @throws:
	 */
	public String saveTransaction(AppIcoCoinTransactionDTO dto);
	
	/**
	 * 查询当前账户下的币账户
	 * @return
	 */
	public List<AppIcoCoinAccountDTO> listAccount(Long customerId);
	
	/**
	 * 查询充币记录
	 * @param params
	 * @return
	 */
	public FrontPage findIcotransaction(Map<String, String> params);
	
	
	/**
	 * 提币
	 * @return
	 */
	public RemoteResult getIco(User user,String coinCode,BigDecimal money,String pwd,String btcKey);
	
	/**
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param transaction
	 * @param:    @return
	 * @return: String 
	 * @Date :          2017年8月19日 上午11:49:51   
	 * @throws:
	 */
	public String rechargeCoin(AppIcoCoinTransactionDTO transaction);
	
	/**
	 * <p> TODO</p>
	 * @author:         Shangxl
	 * @param:    @param account
	 * @param:    @param address
	 * @param:    @return
	 * @return: AppIcoCoinAccountDTO 
	 * @Date :          2017年8月19日 下午12:01:47   
	 * @throws:
	 */
	public AppIcoCoinAccountDTO getcoinAccountByPublicKey(String publicKey);
}
