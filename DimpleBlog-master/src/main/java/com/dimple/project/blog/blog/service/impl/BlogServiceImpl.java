package com.dimple.project.blog.blog.service.impl;

import com.dimple.common.constant.CacheConstant;
import com.dimple.common.constant.CommonConstant;
import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.common.utils.text.Convert;
import com.dimple.common.vo.FileForm;
import com.dimple.framework.config.SystemConfig;
import com.dimple.project.blog.blog.domain.Blog;
import com.dimple.project.blog.blog.mapper.BlogMapper;
import com.dimple.project.blog.blog.mapper.BlogTagMapper;
import com.dimple.project.blog.blog.service.BlogService;
import com.dimple.project.blog.category.service.CategoryService;
import com.dimple.project.blog.tag.service.TagService;
import com.dimple.project.common.service.FileService;
import com.dimple.project.dashboard.domain.BusinessCommonData;
import com.dimple.project.enums.BLogStatusEnum;
import com.dimple.project.king.func.domain.FuncBlogEntity;
import com.dimple.project.king.func.service.FuncBlogService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @className: BlogServiceImpl
 * @description:
 * @auther: Dimple
 * @Date: 2019/3/16
 * @Version: 1.0
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    TagService tagService;
    @Autowired
    BlogTagMapper blogTagMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    private FuncBlogService funcBlogService;
    @Autowired
    FileService fileService;

    @Override
    public List<Blog> selectBlogList(Blog blog) {
        return blogMapper.selectBlogList(blog);
    }

    @Override
    public int insertBlog(Blog blog,List<MultipartFile> files) {
        blog.setCreateBy(ShiroUtils.getLoginName());
        blog.setCreateTime(new Date());
        int i = blogMapper.insertBlog(blog);
        handlerBlogTag(blog.getBlogId(), blog.getTags());
        FuncBlogEntity funcBlog = new FuncBlogEntity();
        if(blog.getFuncId()!=null&&blog.getFuncId()!=0){
            funcBlog.setBlogId(Long.valueOf(blog.getBlogId()));
            funcBlog.setFuncId(blog.getFuncId());
            funcBlog.setCreateBy(ShiroUtils.getLoginName());
            funcBlog.setCreateTime(new Date());
            funcBlogService.save(funcBlog);
        }
        //上传附件
        if(CollectionUtils.isNotEmpty(files)){
            for (MultipartFile file:files){
                try {
                    fileService.uploadFile(new FileForm(SystemConfig.getAttachmentPath(), file,Long.valueOf(blog.getBlogId()),"blog"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return i;
    }

    void handlerBlogTag(int blogId, String[] tagTitles) {
        if (tagTitles == null || tagTitles.length == 0) {
            return;
        }
        //将tag数据插入到数据库
        tagService.insertTags(tagTitles);
        //根据tag的title获取id的集合
        List<Integer> tagIds = tagService.selectTagIdsByTagTitles(tagTitles);
        for (Integer id : tagIds) {
            blogTagMapper.insertBlogTag(id, blogId);
        }
    }

    @Override
    public Blog selectBlogById(Integer blogId) {
        return blogMapper.selectBlogById(blogId);
    }

    @CacheEvict(value = CacheConstant.BUSINESS_CACHE_BLOG_ITEM, key = "#blog.blogId")
    @Override
    public int updateBlog(Blog blog ,List<MultipartFile> files) {
        blog.setUpdateBy(ShiroUtils.getLoginName());
        //添加新的tag
        tagService.insertTags(blog.getTags());
        updateBlogTag(blog.getBlogId(), blog.getTags());
        //上传附件
        if(CollectionUtils.isNotEmpty(files)){
            for (MultipartFile file:files){
                try {
                    fileService.uploadFile(new FileForm(SystemConfig.getAttachmentPath(), file,Long.valueOf(blog.getBlogId()),"blog"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return blogMapper.updateBlog(blog);
    }

    private void updateBlogTag(Integer blogId, String[] tags) {
        if (tags == null || tags.length == 0) {
            return;
        }
        //根据tag名称获取所有的tag的id
        List<Integer> ids = tagService.selectTagIdsByTagTitles(tags);
        //删除原有的tag的和blog的关联关系
        blogTagMapper.deleteBlogTagByBlogId(blogId);
        //重新建立两者的关系
        for (Integer id : ids) {
            blogTagMapper.insertBlogTag(id, blogId);
        }
    }

    @CacheEvict(value = {CacheConstant.BUSINESS_CACHE_BLOG_NEWEST_UPDATE, CacheConstant.BUSINESS_CACHE_BLOG_RANKING, CacheConstant.BUSINESS_CACHE_BLOG_SUPPORT})
    @Override
    public int updateBlogSupportById(Integer blogId, String support) {
        return blogMapper.updateBlogSupportById(blogId, support);
    }

    //清除缓存
    @CacheEvict(value = {CacheConstant.BUSINESS_CACHE_BLOG_NEWEST_UPDATE,
            CacheConstant.BUSINESS_CACHE_BLOG_RANKING,
            CacheConstant.BUSINESS_CACHE_BLOG_SUPPORT,
            CacheConstant.BUSINESS_CACHE_BLOG_ITEM
    })
    @Override
    public int updateBlogStatusById(String blogIds, String status) {
        return blogMapper.updateBlogStatusByIds(Convert.toIntArray(blogIds), status);
    }

    @CacheEvict(value = {
            CacheConstant.BUSINESS_CACHE_BLOG_NEWEST_UPDATE,
            CacheConstant.BUSINESS_CACHE_BLOG_RANKING,
            CacheConstant.BUSINESS_CACHE_BLOG_SUPPORT,
            CacheConstant.BUSINESS_CACHE_BLOG_ITEM
    })
    @Override
    public int deleteBlogById(Integer[] ids) {
        return blogMapper.deleteBlogByIds(BLogStatusEnum.DUSTBIN.getStatus(),ids);
    }

    @Override
    public int selectBlogCountByStatus(int status) {
        return blogMapper.selectBlogCountByStatus(status);
    }

    @Override
    @Cacheable(value = CacheConstant.BUSINESS_CACHE_BLOG_ITEM, key = "#blogId")
    public Blog selectBlogWithTextAndTagsAndCategoryByBlogId(Integer blogId) {
        Blog blog = blogMapper.selectBlogWithTextById(blogId);
        if (blog == null) {
            return null;
        }
        //先去tag和blog的关联表中选择所有的tagIds
        List<Integer> tagIds = blogTagMapper.selectTagIdsByBlogId(blogId);
        //根据查询出的tag的id去tag表中查询
        String[] tags = new String[tagIds.size()];
        for (int i = 0; i < tagIds.size(); i++) {
            tags[i] = (tagService.selectTagById(tagIds.get(i)).getTagTitle());
        }
        blog.setTags(tags);
        blog.setCategory(categoryService.selectCategoryById(blog.getCategoryId()));
        return blog;
    }

    @Cacheable(value = CacheConstant.BUSINESS_CACHE_BLOG_NEWEST_UPDATE)
    @Override
    public List<Blog> selectNewestUpdateBlog() {
        return blogMapper.selectNewestUpdateBlog(4);
    }

    @Cacheable(value = CacheConstant.BUSINESS_CACHE_BLOG_RANKING)
    @Override
    public List<Blog> selectBlogRanking() {
        return blogMapper.selectBlogRankingList(6);
    }

    @Cacheable(value = CacheConstant.BUSINESS_CACHE_BLOG_SUPPORT)
    @Override
    public List<Blog> selectSupportBlog() {
        return blogMapper.selectSupportBlogList(5);
    }

    @Override
    public Blog selectPreviousBlogById(Integer blogId) {
        return blogMapper.selectPreviousBlogById(blogId);
    }

    @Override
    public Blog selectNextBlogById(Integer blogId) {
        return blogMapper.selectNextBlogById(blogId);
    }

    @Override
    public Blog selectPreviousBlogByFuncIdAndId(Long funcId, Integer blogId) {
        return blogMapper.selectPreviousBlogByFuncIdAndId(funcId, blogId);
    }

    @Override
    public Blog selectNextBlogByFuncIdAndId(Long funcId, Integer blogId) {
        return blogMapper.selectNextBlogByFuncIdAndId(funcId, blogId);
    }

    @Override
    public List<Blog> selectRandBlogList() {
        return blogMapper.selectRandBlogListLimit(4);
    }

    @Override
    public List<Blog> selectBlogListByTagId(Integer tagId) {
        return blogMapper.selectBlogsByTagId(tagId);
    }

    @Override
    public List<BusinessCommonData> selectBlogClickData(String startTime, String endTime) {
        if (CommonConstant.undefined.equals(startTime)) {
            startTime = null;
        }
        if (CommonConstant.undefined.equals(endTime)) {
            endTime = null;
        }
        return blogMapper.selectBlogClickData(startTime, endTime);
    }

    @Override
    public int updateBlogAllowCommentByBlogId(Boolean allowComment, Integer blogId) {
        return blogMapper.updateBlogAllowCommentByBlogId(allowComment, blogId);
    }

    @Override
    public int incrementBlogClick(Integer blogId) {
        //新增blog访问量
        return blogMapper.incrementBlogClick(blogId);
    }

}
