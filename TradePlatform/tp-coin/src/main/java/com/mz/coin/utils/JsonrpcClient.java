package com.mz.coin.utils;

import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;
import com.mz.Constant;
import com.mz.coin.CoinService;
import com.mz.coin.coin.service.CoinTransactionService;
import com.mz.core.mvc.model.page.JsonResult;
import com.mz.dto.model.Token;
import com.mz.coin.Wallet;
import com.mz.util.StringUtil;
import com.mz.util.log.LogFactory;
import com.mz.util.sys.ContextUtil;
import com.mz.utils.Properties;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

/**
 * 基于比特币rpc接口开发新的功能、主要服务omni钱包rpc接口
 * <p>
 * TODO
 * </p>
 *
 * @author: shangxl
 * @Date : 2017年11月16日 下午6:10:02
 */
public class JsonrpcClient extends BitcoinJSONRPCClient {
    public static final double BTCFEE = 0.001;

    /**
     * <p>
     * TODO
     * </p>
     *
     * @author: shangxl
     * @param: @param
     * rpcUrl
     * @param: @throws
     * MalformedURLException
     */
    public JsonrpcClient(String rpcUrl) throws MalformedURLException {
        super(rpcUrl);
    }

    @Override
    public String getNewAddress(String accountName) {
        try {
            if (StringUtils.isNotEmpty(accountName)) {
                return super.getNewAddress(accountName);
            } else {
                return super.getNewAddress();
            }
        } catch (BitcoinException e) {
            LogFactory.info("创建新币账户出错：  accountName=" + accountName);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getBlockCount(){
        int blockCount = 0;
        try {
            blockCount = super.getBlockCount();
        } catch (BitcoinException e) {
            e.printStackTrace();
        }
        return blockCount;
    }

    /**
     * 根据地址获取usdt的账户余额
     * <p>
     * TODO
     * </p>
     *
     * @author: shangxl
     * @param: @param
     * address
     * @param: @return
     * @return: String
     * @Date : 2017年12月18日 下午5:39:04
     * @throws:
     */
    public BigDecimal omniGetBalance(String address, Integer propertyid) {
        BigDecimal balance = BigDecimal.ZERO;
        try {
            Map<String, Object> map = (Map<String, Object>) query("omni_getbalance", new Object[]{address, propertyid});
            if (map != null) {
                Object balanceObj = map.get("balance");
                if (balanceObj != null) {
                    String balanceStr = map.get("balance").toString();
                    if (StringUtils.isNotEmpty(balanceStr)) {
                        balance = new BigDecimal(balanceStr);
                    }
                }
            }
        } catch (BitcoinException e) {
            e.printStackTrace();
            LogFactory.info("接口错误");
        }
        return balance;
    }

    /**
     * <p> TODO</p>
     * usdt 查询充币记录
     *
     * @author: shangxl
     * @param: @param address
     * @param: @param count
     * @param: @param skip
     * @param: @param startblock
     * @param: @param endblock
     * @param: @return
     * @return: List<Map   <   String   ,   Object>>
     * @Date :          2017年12月19日 上午11:26:51
     * @throws:
     */
    public List<Map<String, Object>> omniListTransactions(String address, int count, int skip, int startblock, int endblock) {
        try {
            return (List<Map<String, Object>>) query("omni_listtransactions", new Object[]{address, count, skip, startblock, endblock});
        } catch (BitcoinException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * usdt 转币
     * <p> TODO</p>
     *
     * @author: shangxl
     * @param: @return
     * @return: String
     * @Date :          2017年12月19日 下午3:38:31
     * @throws:
     */
    public String omniSend(String fromaddress, String toaddress, String amount, Integer propertyId) {
        try {
            return query("omni_send", new Object[]{fromaddress, toaddress, propertyId, amount}).toString();
        } catch (BitcoinException e) {
            LogFactory.info("fromaddress=" + fromaddress + " toaddress=" + toaddress + " propertyid=" + propertyId + " amount=" + amount);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * <p>查询钱包总币、提币地址余额</p>
     *
     * @author: shangxl
     * @param: @return
     * @return: Wallet
     * @Date :          2018年1月16日 下午3:43:16
     * @throws:
     */
    public Wallet getUsdtWalletInfo() {
        Wallet wallet = new Wallet();
        wallet.setCoinCode(Constant.USDT);
        String coldwalletAddres = Properties.appcoinMap().get(Constant.USDT.toLowerCase() + Properties.COLDADDERSS);
        wallet.setColdwalletAddress(coldwalletAddres);
        Integer propertyid = Integer.parseInt(Constant.PROPERTYID_USDT);
        try {
            //查看提币地址
            String withdrawalsAddress = this.getAddressesByAccount("").get(0);

            CoinServiceImpl coinServiceImpl = new CoinServiceImpl(Constant.USDT);
            //查找USDT钱包
            Map<String, Number> map = coinServiceImpl.listaccounts();
            Double totalMoneyDouble = Double.valueOf(0d);
            for (Entry<String, Number> entry : map.entrySet()) {
                List<String> addrs = this.getAddressesByAccount(entry.getKey());
                for (String l : addrs) {
                    double m = this.omniGetBalance(l, propertyid).doubleValue();
                    totalMoneyDouble += m;
                    System.out.println(l+" money: " +m);
                }
            }
            BigDecimal totalMoney = BigDecimal.valueOf(totalMoneyDouble);
            wallet.setWithdrawalsAddress(withdrawalsAddress);
            wallet.setTotalMoney(totalMoney.toString());
            wallet.setWithdrawalsAddressMoney(this.omniGetBalance(withdrawalsAddress, propertyid).toString());
        } catch (BitcoinException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return wallet;
    }

    /**
     * 提币
     * <p> TODO</p>
     *
     * @author: shangxl
     * @param: @param type
     * @param: @param amount
     * @param: @param toAddress
     * @param: @param propertyId
     * @param: @return
     * @return: JsonResult
     * @Date :          2018年4月12日 下午4:31:50
     * @throws:
     */
    public JsonResult omniSendFrom(String amount, String toAddress, Integer propertyId) {
        JsonResult result = new JsonResult();

        CoinService coinService = new CoinServiceImpl("USDT");
        String validate = coinService.validateAddress(toAddress);
        validate = validate.replace(" ", "");
        Map<String, Object> map = StringUtil.str2map(validate);
        boolean success = Boolean.parseBoolean(map.get("isvalid").toString());
        if(success==false){
            result.setMsg("提币地址有误");
            return result;
        }

        List<String> list = new ArrayList<>();
        try {
            list = this.getAddressesByAccount("");
        } catch (BitcoinException e) {
            e.printStackTrace();
        }
        if (list != null && list.size() > 0) {
            String fromaddress = list.get(0);
            LogFactory.info("USDT提币地址为= " + fromaddress);
            double btcmoney = Double.valueOf(0d);
            try {
                //查USDT提币地址的BTC余额
                btcmoney = this.getReceivedByAddress(fromaddress);
            } catch (BitcoinException e) {
                e.printStackTrace();
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
            double usdtmoney = this.omniGetBalance(fromaddress, propertyId).doubleValue();
            double needmoney = Double.valueOf(amount);
            if (btcmoney >= BTCFEE) {
                if (usdtmoney >= needmoney) {
                    String txid = this.omniSend(fromaddress, toAddress, amount, propertyId);
                    if (StringUtils.isNotEmpty(txid)) {
                        LogFactory.info(toAddress+"USDT提币hash为= " + txid);
                        result.setSuccess(true);
                        result.setMsg(txid);
                    } else {
                        result.setMsg("usdt提币失败，返回为空");
                    }
                } else {
                    result.setMsg("usdt提币地址币量不足");
                }
            } else {
                result.setMsg("钱包btc不足");
            }
        } else {
            result.setMsg("未查询到提币地址");
        }
        return result;
    }

    /**
     * USDT提币地址转账到冷钱包地址
     *
     * @param amount
     * @param toAddress
     * @param propertyId
     * @return
     */
    public JsonResult omnisendColdWallet(String amount, String toAddress, Integer propertyId) {
        JsonResult result = new JsonResult();
        List<String> list = new ArrayList<>();
        try {
            list = this.getAddressesByAccount("");
        } catch (BitcoinException e) {
            e.printStackTrace();
        }
        if (list != null && list.size() > 0) {
            //获取提币地址（第一个）
            String fromaddress = list.get(0);
            LogFactory.info("USDT提币地址为= " + fromaddress);
            double btcmoney = Double.valueOf(0d);
            try {
                //获取提币地址的btc（矿工费）
                btcmoney = this.getReceivedByAddress(fromaddress);
            } catch (BitcoinException e) {
                e.printStackTrace();
            }
            //获取提币地址的USDT（提币余额）
            double usdtmoney = this.omniGetBalance(fromaddress, propertyId).doubleValue();
            //所需提币数量
            double needmoney = Double.valueOf(amount);
            if (btcmoney >= BTCFEE) {
                if (usdtmoney >= needmoney) {
                    String txid = this.omniSend(fromaddress, toAddress, amount, propertyId);
                    if (StringUtils.isNotEmpty(txid)) {
                        result.setSuccess(true);
                        result.setMsg(txid);
                    } else {
                        result.setMsg("usdt提币失败，返回为空");
                    }
                } else {
                    result.setMsg("usdt提币地址币量不足");
                }
            } else {
                result.setMsg("钱包btc不足");
            }
        } else {
            result.setMsg("未查询到提币地址");
        }
        return result;
    }

    /**
     * 根据propertyid 查询omni的资产余额
     * <p> TODO</p>
     *
     * @author: shangxl
     * @param: @param propertyid
     * @param: @return
     * @return: JsonResult
     * @Date :          2018年4月12日 上午10:37:26
     * @throws:
     */
    public JsonResult listOmniAssetByPropertyid(Integer propertyid) {
        JsonResult result = new JsonResult();
        String withdrawalsAddress ="";
        try {
            withdrawalsAddress = this.getAddressesByAccount("").get(0);
        } catch (BitcoinException e) {
            e.printStackTrace();
        }
        //USDT的assetId=31
        CoinTransactionService coinTransaction = (CoinTransactionService) ContextUtil.getBean("coinTransactionService");
        if (propertyid.toString().equals(Constant.PROPERTYID_USDT)) {
            String coinType = Constant.USDT;

            //首先获取充值过资产的地址
            List<String> listAddress = coinTransaction.listAddressGroupByCoinType(coinType);
            if (listAddress != null && listAddress.size() > 0) {
                List<Token> listToken = new ArrayList<Token>();
                for (String address : listAddress) {
                    if(!withdrawalsAddress.equals(address)) {
                        BigDecimal tokenAssets = omniGetBalance(address, propertyid);
                        //omini资产必须大于零
                        if (tokenAssets.compareTo(BigDecimal.ZERO) > 0) {
                            BigDecimal feeAssets = BigDecimal.ZERO;
                            try {
                                //查询BTC余额
                                String account = this.getAccount(address);
                                feeAssets = BigDecimal.valueOf(this.getBalance(account));
                            } catch (BitcoinException e) {
                                e.printStackTrace();
                            }
                            Boolean abledCollect = feeAssets.compareTo(BigDecimal.valueOf(BTCFEE)) >= 0;
                            listToken.add(new Token(0, address, tokenAssets, feeAssets, abledCollect));
                        }
                    }
                }
                //根据资产排序
                Collections.sort(listToken);
                result.setSuccess(true);
                result.setObj(listToken);
            }
        }
        return result;
    }

    /**
     * omni资产归集
     * <p> TODO</p>
     *
     * @author: shangxl
     * @param: @param from
     * @param: @param amount
     * @param: @param propertyId
     * @param: @return
     * @return: JsonResult
     * @Date :          2018年4月12日 下午4:40:43
     * @throws:
     */
    public JsonResult omniAssetCollect(String fromAddress, String amount, Integer propertyId) {
        JsonResult result = new JsonResult();
        //获取所有地址，然后去第一个地址作为提币地址
        List<String> list = new ArrayList<>();
        try {
            list = this.getAddressesByAccount("");
        } catch (BitcoinException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (list != null && list.size() > 0) {
            String toAddress = list.get(0);
            double btcmoney = Double.valueOf(0d);
            try {
                btcmoney = this.getReceivedByAddress(fromAddress);
            } catch (BitcoinException e) {
                e.printStackTrace();
            }
            //usdt提币地址币量
            double usdtmoney = this.omniGetBalance(fromAddress, propertyId).doubleValue();
            double needmoney = Double.valueOf(amount);
            if (btcmoney >= BTCFEE) {
                if (usdtmoney >= needmoney) {
                    String txid = this.omniSend(fromAddress, toAddress, amount, propertyId);
                    if (StringUtils.isNotEmpty(txid)) {
                        result.setSuccess(true);
                        result.setMsg(txid);
                    } else {
                        result.setMsg("提币失败，返回为空");
                    }
                } else {
                    result.setMsg("提币地址币量不足");
                }
            } else {
                result.setMsg("钱包btc不足");
            }
        } else {
            result.setMsg("未查询到提币地址");
        }
        return result;
    }

    /**
     * 获取提币地址
     * <p> TODO</p>
     *
     * @author: shangxl
     * @param: @return
     * @return: String
     * @Date :          2018年4月12日 下午5:56:34
     * @throws:
     */
    public String getOmniWithdrawAddress() {
        String fromAddress = null;
        List<String> list = new ArrayList<>();
        try {
            list = this.getAddressesByAccount("");
        } catch (BitcoinException e) {
            e.printStackTrace();
        }
        if (list != null && list.size() > 0) {
            fromAddress = list.get(0).toString();
            if (StringUtils.isNotEmpty(fromAddress)) {
                return fromAddress;
            }
        }
        return fromAddress;
    }


    /**
     * <p> 充值BTC矿工费给散户</p>
     *
     * @author: shangxl
     * @param: @param address
     * @param: @param amount
     * @param: @return
     * @return: JsonResult
     * @Date :          2018年4月12日 下午5:55:24
     * @throws:
     */
    public JsonResult omniRechargeTxFee(String address, String amount, Integer propertyId) {

        JsonResult result = new JsonResult();
        CoinService coinService = new CoinServiceImpl("BTC");
        result=coinService.sendFrom(amount, address);

        return result;
    }

}
