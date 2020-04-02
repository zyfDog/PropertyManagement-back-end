package com.management.core.service.proprietor.repairs.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.core.entity.Proprietor;
import com.management.core.entity.Repairs;
import com.management.core.mapper.ProprietorMapper;
import com.management.core.mapper.RepairsMapper;
import com.management.core.service.proprietor.repairs.comm.RepairsComm;
import com.management.core.service.proprietor.repairs.service.RepairsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

@Service
public class RepairsServiceImpl implements RepairsService {
    @Resource
    private RepairsMapper repairsMapper;

    @Resource
    private ProprietorMapper proprietorMapper;

    public List<RepairsComm> getAllRepairsRecord(Long proprietorId) {
        List<Repairs> repairsList = repairsMapper.selectList(new QueryWrapper<Repairs>()
                .eq("proprietor_id", proprietorId)
        );

        List<RepairsComm> repairsCommList = new LinkedList<>();
        for (Repairs repairs : repairsList) {
            RepairsComm repairsComm = new RepairsComm();
            repairsComm.setId(repairs.getId());
            repairsComm.setRepairDate(getLongTimeToStringTime(repairs.getRepairsTime()));
            repairsComm.setServiceman(repairs.getServiceman());
            repairsComm.setSummary(repairs.getSummary());

            if (repairs.getStatus()) {
                repairsComm.setStatus("已处理");
            } else {
                repairsComm.setStatus("未处理");
            }

            repairsCommList.add(repairsComm);
        }

        return repairsCommList;
    }

    @Override
    public boolean addRepairsRecord(Long proprietorId, String summary) {
        Repairs repairs = new Repairs();
        repairs.setProprietorId(proprietorId);
        repairs.setSummary(summary);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            repairs.setRepairsTime(sdf.parse(sdf.format(ts)).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //将住户地址加进保修信息表里面
        Proprietor proprietor = proprietorMapper.selectOne(new QueryWrapper<Proprietor>()
                .eq("proprietor_id", proprietorId)
        );
        if (proprietor == null) {
            return false;
        } else {
            repairs.setAddress(proprietor.getAddress());
        }

        return repairsMapper.insert(repairs) > 0;
    }

    @Override
    public boolean updateRepairsRecord(Long id, String summary) {
        Repairs repairs = repairsMapper.selectById(id);
        if (repairs == null) {
            return false;
        }

        repairs.setSummary(summary);
        repairs.setRepairsTime(System.currentTimeMillis());
        repairsMapper.updateById(repairs);
        return true;
    }

    @Override
    public List<RepairsComm> getRepairsRecordByKeyword(Long id, String keyword) {
        List<Repairs> repairsList = repairsMapper.selectList(new QueryWrapper<Repairs>()
                .like("summary", keyword)
                .eq("proprietor_id", id)
        );

        List<RepairsComm> repairsCommList = new LinkedList<>();
        for (Repairs repairs : repairsList) {
            RepairsComm repairsComm = new RepairsComm();
            repairsComm.setSummary(repairs.getSummary());
            repairsComm.setId(repairs.getId());
            repairsComm.setServiceman(repairs.getServiceman());
            repairsComm.setRepairDate(getLongTimeToStringTime(repairs.getRepairsTime()));

            if (repairs.getStatus()) {
                repairsComm.setStatus("已处理");
            } else {
                repairsComm.setStatus("未处理");
            }

            repairsCommList.add(repairsComm);
        }

        return repairsCommList;
    }

    private String getLongTimeToStringTime(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(time);
    }
}
