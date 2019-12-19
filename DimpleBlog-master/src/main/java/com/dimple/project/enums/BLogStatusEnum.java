package com.dimple.project.enums;


/**
 * 博文状态，1表示已经发表，2表示在草稿箱，3表示在垃圾箱
 * @author liusheng
 *
 */
public enum BLogStatusEnum {
  
  PUBLISH("1"),
  DRAFTS("2"),
  DUSTBIN("3");
  
  private String status;

  private BLogStatusEnum(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
  
  
  
}
