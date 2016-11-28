package com.ail.narad.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CallRequest.
 */
@Entity
@Table(name = "call_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "callrequest")
public class CallRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "request_id")
    private String requestId;
    
    @Column(name = "phoneno")
    private String phoneno;
    
    @Column(name = "consignmentid")
    private String consignmentid;
    
    @Lob
    @Column(name = "response")
    private String response;
    
    @Column(name = "body")
    private String body;
    
    @Column(name = "cancelled")
    private Boolean cancelled;
    
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

    public String getPhoneno() {
        return phoneno;
    }
    
    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getConsignmentid() {
        return consignmentid;
    }
    
    public void setConsignmentid(String consignmentid) {
        this.consignmentid = consignmentid;
    }

    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }

    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getCancelled() {
        return cancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CallRequest callRequest = (CallRequest) o;
        if(callRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, callRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CallRequest{" +
            "id=" + id +
            ", requestId='" + requestId + "'" +
            ", phoneno='" + phoneno + "'" +
            ", consignmentid='" + consignmentid + "'" +
            ", response='" + response + "'" +
            ", body='" + body + "'" +
            ", cancelled='" + cancelled + "'" +
            '}';
    }
}
