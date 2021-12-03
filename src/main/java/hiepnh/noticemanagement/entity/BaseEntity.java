package hiepnh.noticemanagement.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<U> implements Serializable {

    @CreatedDate
    @Temporal(TIMESTAMP)
    protected Date registrationDate;

    @LastModifiedBy
    protected U modifiedBy;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    protected Date modifiedDate;

    public Date getRegistrationDate() {
        return registrationDate;
    }
}
