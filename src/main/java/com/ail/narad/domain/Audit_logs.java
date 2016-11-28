package com.ail.narad.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.elasticsearch.annotations.Document;

import com.ail.narad.domain.enumeration.AuditEvent;
import com.ail.narad.factory.messages.IMessageBean;
import com.ail.narad.service.util.LogUtils;

/**
 * A Audit_logs.
 */
@Entity
@Table(name = "audit_logs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "audit_logs")
public class Audit_logs implements Serializable, IMessageBean {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "request_id", nullable = false)
    private String requestId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "event", nullable = false)
    private AuditEvent event;

    @Column(name = "message")
    private String message;

    @NotNull
    @Column(name = "updated_time", nullable = false)
    private ZonedDateTime updated_time;

    public Audit_logs(){}
    
    public Audit_logs(AuditEvent event, String message) {
    	this.requestId = LogUtils.getCurrentRequestId();
    	this.event = event;
    	this.message = message;
    	this.updated_time = ZonedDateTime.now();
	}
    
    public Audit_logs(String requestId, AuditEvent event, String message) {
    	this.requestId = requestId;
    	this.event = event;
    	this.message = message;
    	this.updated_time = ZonedDateTime.now();
	}
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public AuditEvent getEvent() {
        return event;
    }

    public void setEvent(AuditEvent event) {
        this.event = event;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(ZonedDateTime updated_time) {
        this.updated_time = updated_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Audit_logs audit_logs = (Audit_logs) o;

        if ( ! Objects.equals(id, audit_logs.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Audit_logs{" +
            "id=" + id +
            ", requestId='" + requestId + "'" +
            ", event='" + event + "'" +
            ", message='" + message + "'" +
            ", updated_time='" + updated_time + "'" +
            '}';
    }

    @Override
	public String serialize() {
    	JSONObject jsonObject = new JSONObject();
    	try {
			jsonObject.put("id", id);
			jsonObject.put("requestId", requestId);
			jsonObject.put("event", event);
			jsonObject.put("message", message);
			jsonObject.put("updated_time", updated_time);
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return jsonObject.toString();
	}
	
	public static IMessageBean deSerialize(String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			Audit_logs auditLogs = new Audit_logs();
			if(jsonObject.get("event") == null) {
				auditLogs.setEvent(AuditEvent.UNKNOWN);
			} else {
				auditLogs.setEvent(AuditEvent.valueOf(StringUtils.upperCase(jsonObject.getString("event"))));
			}
			auditLogs.setMessage(jsonObject.getString("message"));
			auditLogs.setRequestId(jsonObject.getString("requestId"));
			auditLogs.setUpdated_time(ZonedDateTime.parse(jsonObject.getString("updated_time")));
			return auditLogs;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new Audit_logs();
	}
}
