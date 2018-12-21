package com.mz.shiro;

import com.mz.customer.user.model.AppCustomer;
import com.mz.oauth.user.model.AppUser;
import com.mz.tenant.user.model.SaasUser;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 密码加密
 * <p> TODO</p>
 *
 * @author: Liu Shilei
 * @Date :          2015年9月25日 上午10:36:45
 */
public class PasswordHelper {

  private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

  private String algorithmName = "md5";
  private int hashIterations = 2;

  public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
    this.randomNumberGenerator = randomNumberGenerator;
  }

  public void setAlgorithmName(String algorithmName) {
    this.algorithmName = algorithmName;
  }

  public void setHashIterations(int hashIterations) {
    this.hashIterations = hashIterations;
  }

  /**
   * AppUser密码加密 后台管理账号加密
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param appUser
   * @return: void
   * @Date :          2016年1月12日 上午11:05:16
   * @throws:
   */
  public void encryptPassword(AppUser appUser) {

    appUser.setSalt(randomNumberGenerator.nextBytes().toHex());
    String newPassword = new SimpleHash(
        algorithmName,
        appUser.getPassword(),
        ByteSource.Util.bytes(appUser.getSalt() + appUser.getAppuserprefix()),
        hashIterations).toHex();

    appUser.setPassword(newPassword);
  }

  /**
   * SaasUser密码加密 saas平台管理账号加密
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param saasUser
   * @return: void
   * @Date :          2016年3月28日 下午2:08:43
   * @throws:
   */
  public void encryptPassword(SaasUser saasUser) {

    saasUser.setSalt(randomNumberGenerator.nextBytes().toHex());
    String newPassword = new SimpleHash(
        algorithmName,
        saasUser.getPassword(),
        ByteSource.Util.bytes(saasUser.getSalt()),
        hashIterations).toHex();

    saasUser.setPassword(newPassword);
  }


  /**
   * AppCustomer密码加密 前台账号加密
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param appCustomer
   * @return: void
   * @Date :          2016年3月28日 下午2:09:58
   * @throws:
   */
  public void encryptPassword(AppCustomer appCustomer) {

    appCustomer.setSalt(randomNumberGenerator.nextBytes().toHex());
    String newPassword = new SimpleHash(
        algorithmName,
        appCustomer.getPassWord(),
        ByteSource.Util.bytes(appCustomer.getSalt()),
        hashIterations).toHex();

    appCustomer.setPassWord(newPassword);
  }


  /**
   * 验证密码
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param appUser
   * @param: @param oldPassword  旧密码
   * @param: @return  旧密码正确 则返回true   否则返回false
   * @return: boolean
   * @Date :          2016年1月12日 上午11:04:03
   * @throws:
   */
  public boolean validatePassword(AppUser appUser, String oldPassword) {

    String newPassword = new SimpleHash(
        algorithmName,
        oldPassword,
        ByteSource.Util.bytes(appUser.getSalt() + appUser.getAppuserprefix()),
        hashIterations).toHex();
    if (newPassword.equals(appUser.getPassword())) {
      return true;
    }
    return false;

  }

  /**
   * 加密任何字符串
   * <p> TODO</p>
   *
   * @author: Liu Shilei
   * @param: @param pwd   字符串
   * @param: @param salt  盐值
   * @param: @return
   * @return: String
   * @Date :          2016年3月30日 下午4:54:02
   * @throws:
   */
  public String encryString(String pwd, String salt) {
    String newPassword = new SimpleHash(
        algorithmName,
        pwd,
        ByteSource.Util.bytes(salt),
        hashIterations).toHex();
    return newPassword;
  }

  public static void main(String[] args) {
    PasswordHelper passwordHelper = new PasswordHelper();
    System.out.println(passwordHelper.encryString("admin", "a96fdca5ff384cb0185bb787f2fd0a2f"));
  }

}
