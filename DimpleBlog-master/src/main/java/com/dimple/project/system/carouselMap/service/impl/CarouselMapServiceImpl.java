package com.dimple.project.system.carouselMap.service.impl;

import com.dimple.common.constant.Constants;
import com.dimple.common.exception.BusinessException;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.project.chart.business.domain.Business;
import com.dimple.project.system.carouselMap.mapper.CarouselMapper;
import com.dimple.project.system.carouselMap.entity.CarouselMap;
import com.dimple.project.system.carouselMap.service.CarouselMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 轮播图设置(CarouselMap)表服务实现类
 */
@Service
public class CarouselMapServiceImpl implements CarouselMapService {
    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public CarouselMap selectCarouselMapById(Integer carouselId) {
        return carouselMapper.selectCarouselMap(carouselId);
    }


    @Override
    public List<CarouselMap> selectCarouselMapList(CarouselMap carouselMap) {
        return carouselMapper.selectCarouselMapList(carouselMap);
    }


    @Override
    public int insertCarouselMap(CarouselMap carouselMap) {
        return carouselMapper.insertCarouselMap(carouselMap);
    }


    @Override
    public int updateCarouselMap(CarouselMap carouselMap) {
        return carouselMapper.updateCarouselMap(carouselMap);
    }


    @Override
    public int deleteCarouselMapByIds(Integer[] carouselId) {
        return carouselMapper.deleteCarouselMapByIds(carouselId);
    }

    @Override
    public int changeCarouselDisplay(String carouselId, String display) {
        return carouselMapper.changeCarouselDisplay(carouselId,display);
    }

    @Override
    public List<CarouselMap> selectCarouselMapListFront() {
        return carouselMapper.selectCarouselMapListFront();
    }

    @Override
    public int incrementCarouselClickById(Integer carouselId) {
        return carouselMapper.incrementCarouselClickById(carouselId);
    }

    @Override
    public List<Business> selectCarouselMapData() {
        return carouselMapper.selectCarouselMapData();
    }


    @Override
    public List<CarouselMap> selectByCreateBy(String loginName) {
      return carouselMapper.selectByCreateBy(loginName);
    }


	@Override
	public int blogSave(Integer[] carouselIds, String[] imgUrl) {
		int count = 0;
		CarouselMap carouselMap = null;
		for(int i=0;i<carouselIds.length;i++){
			if(carouselIds[i]==0&&!imgUrl[i].equals(Constants.BLOG_INDEX_PIC_URL)){
				carouselMap = new CarouselMap();
				carouselMap.setDisplay("1");
				carouselMap.setImgUrl(imgUrl[i]);
				carouselMap.setCreateBy(ShiroUtils.getLoginName());
				carouselMap.setCreateTime(new Date());
				count+=carouselMapper.insertCarouselMap(carouselMap);
			}else if(carouselIds[i]!=0){
				carouselMap = carouselMapper.selectCarouselMap(carouselIds[i]);
				if(!carouselMap.getCreateBy().equals(ShiroUtils.getLoginName())){
					throw new BusinessException("不是当前博主不能修改图片");
				}
				carouselMap.setImgUrl(imgUrl[i]);
				count+=carouselMapper.updateCarouselMap(carouselMap);
			}
		}
		return count;
	}
}