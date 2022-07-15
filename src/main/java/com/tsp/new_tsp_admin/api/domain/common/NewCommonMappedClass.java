package com.tsp.new_tsp_admin.api.domain.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public abstract class NewCommonMappedClass {
    @CreatedBy
    @Column(name = "creator", updatable = false)
    @ApiModelProperty(required = true, value = "등록자")
    private String creator;

    @LastModifiedBy
    @Column(name = "updater", insertable = false)
    @ApiModelProperty(required = true, value = "수정자")
    private String updater;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    @Temporal(value = TIMESTAMP)
    @ApiModelProperty(required = true, value = "등록 일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @UpdateTimestamp
    @Column(name = "update_time", insertable = false)
    @Temporal(TIMESTAMP)
    @ApiModelProperty(required = true, value = "수정 일자")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;
}
