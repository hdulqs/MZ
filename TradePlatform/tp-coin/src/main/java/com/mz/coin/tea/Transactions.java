package com.mz.coin.tea;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Frank on 2018/9/4.
 */
public class Transactions {

  /**
   * Changes in address balance
   */
  private Balance balance;
  /**
   * Address passed as parameter
   */
  private List<String> myaddresses;
  /**
   * Array of counterparty addresses  involved in transaction
   */
  private List<String> addresses;
  /**
   * Changes in permissions
   */
  private List<String> permissions;
  /**
   * Issue details
   */
  private List<String> issue;
  /**
   * Hexadecimal representation of metadata appended to the transaction
   */
  private List<String> data;
  /**
   * The number of confirmations for the transaction. Available for 'send' and 'receive' category of
   * transactions.
   */
  private BigDecimal confirmations;
  /**
   * The block hash containing the transaction. Available for 'send' and 'receive' category of
   * transactions.
   */
  private String blockhash;
  /**
   * The block index containing the transaction. Available for 'send' and 'receive' category of
   * transactions
   */
  private BigDecimal blockindex;
  /**
   * The transaction id. Available for 'send' and 'receive' category of transactions
   */
  private String txid;
  /**
   * The transaction time in seconds since epoch (midnight Jan 1 1970 GMT)
   */
  private Long time;
  /**
   * The time received in seconds since epoch (midnight Jan 1 1970 GMT). Available for 'send' and
   * 'receive' category of transactions
   */
  private Long timereceived;
  /**
   * If a comment is associated with the transaction
   */
  private String comment;
  /**
   * If verbose=true. Array of input details
   */
  private List<String> vin;
  /**
   * If verbose=true. Array of output details
   */
  private List<String> vout;
  /**
   * If verbose=true. Raw data for transaction
   */
  private String hex;

  public Balance getBalance() {
    return balance;
  }

  public void setBalance(Balance balance) {
    this.balance = balance;
  }

  public List<String> getMyaddresses() {
    return myaddresses;
  }

  public void setMyaddresses(List<String> myaddresses) {
    this.myaddresses = myaddresses;
  }

  public List<String> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<String> addresses) {
    this.addresses = addresses;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public List<String> getIssue() {
    return issue;
  }

  public void setIssue(List<String> issue) {
    this.issue = issue;
  }

  public List<String> getData() {
    return data;
  }

  public void setData(List<String> data) {
    this.data = data;
  }

  public BigDecimal getConfirmations() {
    return confirmations;
  }

  public void setConfirmations(BigDecimal confirmations) {
    this.confirmations = confirmations;
  }

  public String getBlockhash() {
    return blockhash;
  }

  public void setBlockhash(String blockhash) {
    this.blockhash = blockhash;
  }

  public BigDecimal getBlockindex() {
    return blockindex;
  }

  public void setBlockindex(BigDecimal blockindex) {
    this.blockindex = blockindex;
  }

  public String getTxid() {
    return txid;
  }

  public void setTxid(String txid) {
    this.txid = txid;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public Long getTimereceived() {
    return timereceived;
  }

  public void setTimereceived(Long timereceived) {
    this.timereceived = timereceived;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public List<String> getVin() {
    return vin;
  }

  public void setVin(List<String> vin) {
    this.vin = vin;
  }

  public List<String> getVout() {
    return vout;
  }

  public void setVout(List<String> vout) {
    this.vout = vout;
  }

  public String getHex() {
    return hex;
  }

  public void setHex(String hex) {
    this.hex = hex;
  }
}
