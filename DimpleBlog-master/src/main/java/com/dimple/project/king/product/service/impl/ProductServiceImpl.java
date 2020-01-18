package com.dimple.project.king.product.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.common.vo.FileForm;
import com.dimple.framework.config.SystemConfig;
import com.dimple.project.common.service.FileService;
import com.dimple.project.king.product.domain.ProductEntity;
import com.dimple.project.king.product.mapper.ProductMapper;
import com.dimple.project.king.product.service.ProductService;


/**
 * 产品
 * @author ls2008
 * @date 2020-01-18 14:43:23
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductEntity> implements ProductService{

	@Autowired
	private ProductMapper mapper;
    @Autowired
    FileService fileService;

	@Override
	public int insertWithFiles(ProductEntity entity, List<MultipartFile> files) {
		entity.setCreateBy(ShiroUtils.getLoginName());
		entity.setCreateTime(new Date());
        String headerUrl = "";
        if(CollectionUtils.isNotEmpty(files)){
            try {
                headerUrl = fileService.insertLocalImageFile(new FileForm(SystemConfig.getProductPath(), files.get(0)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        entity.setHeaderImg(headerUrl);

        return mapper.insert(entity);
	}

	@Override
	public int updateWithFiles(ProductEntity entity, List<MultipartFile> files) {
		entity.setUpdateBy(ShiroUtils.getLoginName());
		entity.setUpdateTime(new Date());
        String headerUrl = "";
        if(CollectionUtils.isNotEmpty(files)){
            try {
                headerUrl = fileService.insertLocalImageFile(new FileForm(SystemConfig.getProductPath(), files.get(0)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        entity.setHeaderImg(headerUrl);
        return mapper.updateById(entity);
	}
}