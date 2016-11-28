package com.ail.narad.service;

import com.ail.narad.domain.Audit_logs;
import com.ail.narad.domain.RequestLog;
import com.ail.narad.repository.Audit_logsRepository;
import com.ail.narad.repository.RequestLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@EnableAsync
@Service("addToLogService")
public class AddToLogServiceImpl implements AddToLogService{

	@Autowired
	private RequestLogRepository requestLogRepository;

    @Autowired
    private Audit_logsRepository audit_logsRepository;

	private final Logger log = LoggerFactory.getLogger(QueueManagerServiceImpl.class);

	@Override
	@Async
	public void addToRequestTable(String requestId, String type, String requestBody) {
		RequestLog data = new RequestLog();
		data.setRequestId(requestId);
		data.setType(type);
		data.setRequest(requestBody);
        data.setUpdate_time(ZonedDateTime.now())    ;
		requestLogRepository.save(data);
		log.info("message added in request Log");
	}

    @Override
    @Async
    public void addToAuditTable(Audit_logs auditLogsBean) {
        Audit_logs data = new Audit_logs();
        data.setRequestId(auditLogsBean.getRequestId());
        data.setEvent(auditLogsBean.getEvent());
        data.setMessage(auditLogsBean.getMessage());
        data.setUpdated_time(auditLogsBean.getUpdated_time());
        audit_logsRepository.save(data);
        log.info("message added in Audit log with event = " + auditLogsBean.getEvent());
    }

}
