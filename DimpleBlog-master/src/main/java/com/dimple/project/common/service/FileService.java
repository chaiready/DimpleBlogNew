package com.dimple.project.common.service;

import java.io.IOException;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import com.dimple.common.vo.FileForm;
import com.dimple.project.common.domain.FileItemInfo;
import com.qiniu.common.QiniuException;

/**
 * @className: FileService
 * @description: 文件处理相关
 * @auther: Dimple
 * @date: 07/24/19
 * @version: 1.0
 */
public interface FileService extends IService<FileItemInfo> {

    /**
     * 刷新七牛云数据到数据库保存
     *
     * @return 成功保存到数据库的条数
     */
    int syncQiNiuYunImage() throws QiniuException;

    /**
     * 删除七牛云图片
     *
     * @param name
     * @return 受影响的行数
     */
    int deleteQiNiuYunImageFile(String name) throws QiniuException;

    /**
     * 上传文件到七牛云
     *
     * @param file 需要上传的文件
     * @return 文件路径
     */
    String insertQiNiuYunImageFile(MultipartFile file);

    List<FileItemInfo> selectFileItemImageList(FileItemInfo fileItemInfo);

    /**
     * 上传文件到本地
     *
     * @param file 需要上传的文件
     * @return 文件路径
     */
    String insertLocalImageFile(FileForm fileForm) throws IOException;

    /**
     * 刷新本地图片到数据库
     *
     * @return 受影响的行数
     */
    int syncLocalImage();

    /**
     * 删除本地图片
     *
     * @param name 图片名称
     * @return 受影响的行数
     */
    int deleteLocalImageFile(String name);

    FileItemInfo uploadFile(FileForm fileForm) throws IOException;


    List<FileItemInfo> listByEntityInfo(String entityType,Long entityId);
}
