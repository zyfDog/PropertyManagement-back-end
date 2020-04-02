package com.management.core.service.proprietor.complaint.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.core.entity.Complaint;
import com.management.core.mapper.ComplaintMapper;
import com.management.core.service.proprietor.complaint.comm.ComplaintComm;
import com.management.core.service.proprietor.complaint.service.ComplaintService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService {
    @Resource
    private ComplaintMapper complaintMapper;

    @Override
    public List<ComplaintComm> getAllComplaintByProprietorId(Long id) {
        List<Complaint> complaints = complaintMapper.selectList(new QueryWrapper<Complaint>()
                .eq("proprietor_id", id)
        );

        List<ComplaintComm> complaintComms = new LinkedList<>();
        for (Complaint complaint : complaints) {
            ComplaintComm complaintComm = reloadComplaintCommByComplaint(complaint);
            complaintComms.add(complaintComm);
        }
        return complaintComms;
    }

    @Override
    public boolean updateComplaintByComplaintId(Long id, String summary) {
        Complaint complaint = complaintMapper.selectById(id);
        if (complaint == null) {
            return false;
        }

        complaint.setSummary(summary);
        complaintMapper.update(complaint, new QueryWrapper<Complaint>().eq("id", id));
        return true;
    }

    @Override
    public List<ComplaintComm> getComplaintRecordBydate(Long id, String date) {
        try {
            List<Complaint> complaints = complaintMapper.selectList(new QueryWrapper<Complaint>()
                    .eq("proprietor_id", id)
            );

            List<ComplaintComm> complaintComms = new LinkedList<>();
            for (Complaint complaint : complaints) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                if (complaint.getComplainDate() != null) {
                    String complaintDate = format.format(complaint.getComplainDate());
                    if (date.trim().equals(complaintDate)) {
                        ComplaintComm complaintComm = reloadComplaintCommByComplaint(complaint);
                        complaintComms.add(complaintComm);
                    }
                }
            }

            return complaintComms;
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    @Override
    public boolean addComplaintRecordByProprietorId(Long id, String summary) {
        Complaint complaint = new Complaint();
        complaint.setProprietorId(id);
        complaint.setSummary(summary);
        complaint.setComplainDate(new Date(System.currentTimeMillis()));

        int num = complaintMapper.insert(complaint);

        return num > 0;
    }

    private String getLongTimeToStringTime(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(time);
    }

    private ComplaintComm reloadComplaintCommByComplaint(Complaint complaint) {
        ComplaintComm complaintComm = new ComplaintComm();
        complaintComm.setId(complaint.getId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (complaint.getComplainDate() != null) {
            String timeFormat = sdf.format(complaint.getComplainDate());
            complaintComm.setComplaintDate(timeFormat);
        }

        complaintComm.setSummary(complaint.getSummary());

        if (complaint.getConductDate() != null) {
            String timeDealDate = sdf.format(complaint.getConductDate());
            complaintComm.setDealDate(timeDealDate);
        }

        if (complaint.getStatus()) {
            complaintComm.setStatus("已处理");
        } else {
            complaintComm.setStatus("未处理");
        }
        return complaintComm;
    }
}
