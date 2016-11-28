package com.ail.narad.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A RequestLog.
 */
@Entity
@Table(name = "request_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "requestlog")
public class RequestLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "request_id", nullable = false)
    private String requestId;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "request", nullable = false)
    private String request;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private ZonedDateTime update_time;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public ZonedDateTime getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(ZonedDateTime update_time) {
        this.update_time = update_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RequestLog requestLog = (RequestLog) o;

        if ( ! Objects.equals(id, requestLog.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RequestLog{" +
            "id=" + id +
            ", requestId='" + requestId + "'" +
            ", type='" + type + "'" +
            ", request='" + request + "'" +
            ", update_time='" + update_time + "'" +
            '}';
    }
}
