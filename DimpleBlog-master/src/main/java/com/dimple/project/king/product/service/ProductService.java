package com.dimple.project.king.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dimple.project.king.product.domain.ProductEntity;

/**
 * 产品
 * @author ls2008
 * @date 2020-01-18 14:43:23
 */
public interface ProductService extends IService<ProductEntity>{

	int insertWithFiles(ProductEntity entity, List<MultipartFile> files);

	int updateWithFiles(ProductEntity entity, List<MultipartFile> files);

}