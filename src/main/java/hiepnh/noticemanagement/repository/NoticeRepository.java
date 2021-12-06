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
    //Get all notice with condition : start date < current date and end date > current date
    Page<NoticeEntity> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsActiveIsTrue(Date startDate, Date endDate, Pageable pageable);
    //Get all notice by title with condition : start date < current date and end date > current date
    Page<NoticeEntity> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndTitleContainingAndIsActiveIsTrue(Date startDate, Date endDate, String title, Pageable pageable);
    //Get all notice by current user with condition : start date < current date and end date > current date
    Page<NoticeEntity> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsActiveIsTrueAndUserEntity(Date startDate, Date endDate, UserEntity user, Pageable pageable);

    NoticeEntity findByIdAndIsActiveIsTrue(Long id);
}
