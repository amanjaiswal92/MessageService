package com.ail.narad.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.ail.narad.domain.enumeration.TemplateStatus;

import com.ail.narad.domain.enumeration.TemplateType;

/**
 * A Templates.
 */
@Entity
@Table(name = "templates")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "templates")
public class Templates implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "template_id", nullable = false)
    private String template_id;

    @NotNull
    @Size(min = 3)
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TemplateStatus status;

    @NotNull
    @Column(name = "creation_time", nullable = false)
    private ZonedDateTime creation_time;

    @Column(name = "approval_time", nullable = false)
    private ZonedDateTime approval_time;

    @Column(name = "disabled_time", nullable = false)
    private ZonedDateTime disabled_time;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TemplateType type;

    @NotNull
    @Size(min = 2)
    @Column(name = "module", nullable = false)
    private String module;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TemplateStatus getStatus() {
        return status;
    }

    public void setStatus(TemplateStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(ZonedDateTime creation_time) {
        this.creation_time = creation_time;
    }

    public ZonedDateTime getApproval_time() {
        return approval_time;
    }

    public void setApproval_time(ZonedDateTime approval_time) {
        this.approval_time = approval_time;
    }

    public ZonedDateTime getDisabled_time() {
        return disabled_time;
    }

    public void setDisabled_time(ZonedDateTime disabled_time) {
        this.disabled_time = disabled_time;
    }

    public TemplateType getType() {
        return type;
    }

    public void setType(TemplateType type) {
        this.type = type;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Templates templates = (Templates) o;

        if ( ! Objects.equals(id, templates.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Templates{" +
            "id=" + id +
            ", template_id='" + template_id + "'" +
            ", message='" + message + "'" +
            ", status='" + status + "'" +
            ", creation_time='" + creation_time + "'" +
            ", approval_time='" + approval_time + "'" +
            ", disabled_time='" + disabled_time + "'" +
            ", type='" + type + "'" +
            ", module='" + module + "'" +
            '}';
    }
}
