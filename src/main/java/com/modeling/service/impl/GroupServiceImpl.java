package com.modeling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modeling.mapper.GroupMapper;
import com.modeling.model.entity.Group;
import com.modeling.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * (Group)表服务实现类
 *
 * @author zrx
 * @since 2024-04-30 20:37:53
 */
@Service("groupService")
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

}
