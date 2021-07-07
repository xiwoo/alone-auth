package com.inminhouse.alone.auth.model.audit;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class FullAudit {

    @CreatedDate
    @Column(updatable = false)
	private Timestamp createTimestmap;
    @LastModifiedDate
    @Column(updatable = true)
	private Timestamp updateTimestmap;
    @CreatedBy
	private long createId;
    @LastModifiedBy
	private long updateId;
}
