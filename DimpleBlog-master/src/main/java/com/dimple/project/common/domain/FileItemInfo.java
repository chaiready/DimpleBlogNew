package com.dimple.project.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dimple.framework.web.domain.SuperEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @className: FileItemInfo
 * @description: 文件类
 * @auther: Dimple
 * @date: 07/24/19
 * @version: 1.0
 */
@Data
@TableName("bg_file_item_info")
public class FileItemInfo extends SuperEntity {
	
	private static final long serialVersionUID = 1L;

    @Id
    @TableId(value = "id", type = IdType.AUTO)
	private Long id;
    /**
     * 资源名。
     */
    private String name;
    /**
     * 文件的HASH值，使用hash值算法计算。
     */
    private String hash;
    /**
     * 资源内容的大小，单位：字节。
     */
    private Long size;
    /**
     * 资源的 MIME 类型。
     */
    private String mimeType;
    /**
     * 上传时间
     */
    private Date putTime;
    /**
     * 图床保存的文件的类型（1表示在本地存储，2表示存储在七牛云）
     */
    private Integer serverType;
    /**
     * 访问路径
     */
    private String path;
    
    private String relativePath;//相对路径

    private Long entityId;

    private String entityType;

    private String originalName;

    private String status;//状态0删除 1:正常



    public FileItemInfo() {
        super();
    }

    public FileItemInfo(String name,String originalName, String hash, Long size, String mimeType, Date putTime, Integer serverType, String path,String relativePath) {
        this.name = name;
        this.originalName = originalName;
        this.hash = hash;
        this.size = size;
        this.mimeType = mimeType;
        this.putTime = putTime;
        this.serverType = serverType;
        this.path = path;
        this.relativePath = relativePath;
    }

    public enum ServerType {
        LOCAL(1), QI_NIU_YUN(2);

        private Integer serverType;

        ServerType(Integer serverType) {
            this.serverType = serverType;
        }

        public Integer getServerType() {
            return serverType;
        }
    }
}
