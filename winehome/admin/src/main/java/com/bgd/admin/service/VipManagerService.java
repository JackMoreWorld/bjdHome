package com.bgd.admin.service;

import com.bgd.admin.dao.VipManagerDao;
import com.bgd.admin.entity.VipManagerDto;
import com.bgd.admin.entity.param.VipFindParam;
import com.bgd.support.base.PageBean;
import com.bgd.support.base.PageDto;
import com.bgd.support.exception.BasicException;
import com.bgd.support.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class VipManagerService {

    @Autowired
    VipManagerDao vipManagerDao;

    /**
     * 分页查询会员信息
     *
     * @param vipFindParam 会员信息查询条件
     * @throws BusinessException
     */
    public PageDto<VipManagerDto> findVip(VipFindParam vipFindParam) throws BusinessException {
        log.info("查询vip信息");
        try {
            PageDto<VipManagerDto> pageDto = new PageDto<>();
            PageBean<VipManagerDto, VipFindParam> pageBean = new PageBean<VipManagerDto, VipFindParam>(vipFindParam) {
                @Override
                protected Long generateRowCount() throws BasicException {
                    return vipManagerDao.countVip(vipFindParam);
                }

                @Override
                protected List<VipManagerDto> generateBeanList() throws BasicException {
                    return vipManagerDao.findVip(vipFindParam);
                }
            }.execute();
            long total = pageBean.getTotalCount();
            List<VipManagerDto> beanList = pageBean.getPageData();
            pageDto.setList(beanList);
            pageDto.setTotal(total);
            return pageDto;
        } catch (Exception e) {
            log.error("查询vip信息异常");
            throw new BusinessException("查询vip信息异常");
        }

    }
}
