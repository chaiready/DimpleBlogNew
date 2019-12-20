package com.dimple.common.vo;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传form
 * @author liusheng
 *
 */
public class FileForm {

  private String relativePath;
  
  private MultipartFile file;
  
  private Integer width; 
  
  private Integer height;

  
  public FileForm() {
  }
  
  public FileForm(String relativePath, MultipartFile file) {
    this.relativePath = relativePath;
    this.file = file;
  }
  
  public FileForm(String relativePath, MultipartFile file, Integer width, Integer height) {
    this.relativePath = relativePath;
    this.file = file;
    this.width = width;
    this.height = height;
  }

  public String getRelativePath() {
    return relativePath;
  }

  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

  public MultipartFile getFile() {
    return file;
  }

  public void setFile(MultipartFile file) {
    this.file = file;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }
  
  
}
