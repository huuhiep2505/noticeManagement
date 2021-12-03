package hiepnh.noticemanagement.repository;

import hiepnh.noticemanagement.entity.NoticeEntity;
import hiepnh.noticemanagement.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    Page<NoticeEntity> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsActiveIsTrue(Date startDate, Date endDate, Pageable pageable);

    Page<NoticeEntity> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndTitleContainingAndIsActiveIsTrue(Date startDate, Date endDate, String title, Pageable pageable);

    Page<NoticeEntity> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsActiveIsTrueAndUserEntity(Date startDate, Date endDate, UserEntity user, Pageable pageable);

    NoticeEntity findByIdAndIsActiveIsTrue(Long id);
}
