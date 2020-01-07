package com.dimple.project.king.bookmarks.service.impl;

import com.dimple.project.king.bookmarks.domain.BookmarksEntity;
import com.dimple.project.king.bookmarks.mapper.BookmarksMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dimple.project.king.bookmarks.service.BookmarksService;

/**
 * 建议
 * @author ls2008
 * @date 2020-01-06 16:03:26
 */
@Service
public class BookmarksServiceImpl extends ServiceImpl<BookmarksMapper, BookmarksEntity> implements BookmarksService{

	@Autowired
	private BookmarksMapper mapper;
}