package com.dimple.common.utils.file;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;
import com.dimple.common.exception.file.FileNameLengthLimitExceededException;
import com.dimple.common.exception.file.FileSizeLimitExceededException;
import com.dimple.common.utils.ImageUtil;
import com.dimple.common.vo.FileForm;
import com.dimple.framework.config.SystemConfig;
import com.dimple.project.common.domain.FileItemInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @className: FileUploadUtils
 * @description: 文件上传工具类
 * @auther: Dimple
 * @Date: 2019/3/13
 * @Version: 1.1
 */
@Slf4j
public class FileUploadUtils {
    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;


    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件
     * @throws Exception
     */
    public static final FileItemInfo upload(MultipartFile file) throws IOException {
        try {
          return upload(SystemConfig.getUploadPath(), file);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }


    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 返回上传成功的文件
     * @throws FileSizeLimitExceededException       如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException                          比如读写文件出错时
     */
    public static final FileItemInfo upload(String relativePath,MultipartFile file) throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException {
        int fileNameLength = file.getOriginalFilename().length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file);

        String fileName = FileUtils.generateFileName(file);
        relativePath = relativePath + fileName;
        String absolutePath = SystemConfig.getProfile()+ relativePath;
        File desc = getAbsoluteFile(absolutePath);
        file.transferTo(desc);
        
        return new FileItemInfo(fileName, String.valueOf(file.hashCode()), file.getSize(), file.getContentType(), new Date(), 
              FileItemInfo.ServerType.LOCAL.getServerType(), absolutePath,relativePath);
    }
    
    
    public static final FileItemInfo uploadImg(FileForm form)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException {
       MultipartFile file = form.getFile();
    	// 上传文件路径
        int fileNameLength = file.getOriginalFilename().length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file);

        String fileName = FileUtils.generateFileName(file);
        String relativePath = form.getRelativePath() + fileName;
        String absolutePath = SystemConfig.getProfile()+ relativePath;

        File desc = getAbsoluteFile(absolutePath);
        file.transferTo(desc);
        
//      fileName = ImageUtil.appendSuffix(fileName, ImageUtil.SUFFIX);
        FileItemInfo fileItemInfo = null;
        if(form.getWidth()!=null&&form.getHeight()!=null&&form.getWidth()!=0&&form.getHeight()!=0){//压缩图
          fileItemInfo = ImageUtil.generateSize(form.getWidth(), form.getHeight(), absolutePath);
        }else{
          fileItemInfo = ImageUtil.generateSmall(absolutePath);
         
        }
        fileItemInfo.setName(fileName);
        return fileItemInfo;
    }


    private static final File getAbsoluteFile(String filename) throws IOException {
        File desc = new File(File.separator + filename);

        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return
     * @throws FileSizeLimitExceededException 如果超出最大大小
     */
    public static final void assertAllowed(MultipartFile file) throws FileSizeLimitExceededException {
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE) {
            throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
        }
    }

}
