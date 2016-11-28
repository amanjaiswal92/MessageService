package com.ail.narad.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.ail.narad.domain.enumeration.TemplateType;

import com.ail.narad.domain.enumeration.Message_sender_status;

/**
 * A Messagesenders.
 */
@Entity
@Table(name = "messagesenders")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "messagesenders")
public class Messagesenders implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "sender_id", nullable = false)
    private String sender_id;

    @NotNull
    @Size(min = 1)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TemplateType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Message_sender_status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TemplateType getType() {
        return type;
    }

    public void setType(TemplateType type) {
        this.type = type;
    }

    public Message_sender_status getStatus() {
        return status;
    }

    public void setStatus(Message_sender_status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Messagesenders messagesenders = (Messagesenders) o;

        if ( ! Objects.equals(id, messagesenders.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Messagesenders{" +
            "id=" + id +
            ", sender_id='" + sender_id + "'" +
            ", name='" + name + "'" +
            ", type='" + type + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
