package com.ail.narad.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CallDetails.
 */
@Entity
@Table(name = "call_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "calldetails")
public class CallDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "request_id")
    private String requestId;
    
    @Column(name = "caller_id")
    private String callerId;
    
    @Column(name = "dialed_number")
    private String dialedNumber;
    
    @Column(name = "start_time")
    private ZonedDateTime startTime;
    
    @Column(name = "end_time")
    private ZonedDateTime endTime;
    
    @Column(name = "time_to_answer")
    private String timeToAnswer;
    
    @Column(name = "duration")
    private String duration;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "agent_unique_id")
    private String agentUniqueID;
    
    @Column(name = "status")
    private String status;
    
    @Lob
    @Column(name = "response")
    private String response;
    
    @Column(name = "count")
    private Long count;
    
    @Column(name = "orderid")
    private String orderid;
    
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

    public String getCallerId() {
        return callerId;
    }
    
    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getDialedNumber() {
        return dialedNumber;
    }
    
    public void setDialedNumber(String dialedNumber) {
        this.dialedNumber = dialedNumber;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public String getTimeToAnswer() {
        return timeToAnswer;
    }
    
    public void setTimeToAnswer(String timeToAnswer) {
        this.timeToAnswer = timeToAnswer;
    }

    public String getDuration() {
        return duration;
    }
    
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }

    public String getAgentUniqueID() {
        return agentUniqueID;
    }
    
    public void setAgentUniqueID(String agentUniqueID) {
        this.agentUniqueID = agentUniqueID;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }

    public Long getCount() {
        return count;
    }
    
    public void setCount(Long count) {
        this.count = count;
    }

    public String getOrderid() {
        return orderid;
    }
    
    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CallDetails callDetails = (CallDetails) o;
        if(callDetails.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, callDetails.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CallDetails{" +
            "id=" + id +
            ", requestId='" + requestId + "'" +
            ", callerId='" + callerId + "'" +
            ", dialedNumber='" + dialedNumber + "'" +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", timeToAnswer='" + timeToAnswer + "'" +
            ", duration='" + duration + "'" +
            ", location='" + location + "'" +
            ", agentUniqueID='" + agentUniqueID + "'" +
            ", status='" + status + "'" +
            ", response='" + response + "'" +
            ", count='" + count + "'" +
            ", orderid='" + orderid + "'" +
            '}';
    }
}
