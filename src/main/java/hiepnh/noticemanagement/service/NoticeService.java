package hiepnh.noticemanagement.service;


import hiepnh.noticemanagement.dto.GenericDataResponse;
import hiepnh.noticemanagement.dto.NoticeDto;
import hiepnh.noticemanagement.dto.NoticeResponse;
import hiepnh.noticemanagement.entity.NoticeEntity;
import hiepnh.noticemanagement.exception.NoticeGenericException;
import hiepnh.noticemanagement.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface NoticeService {
    NoticeEntity createNotice(NoticeDto userEntity) throws Exception;

    GenericDataResponse updateNotice(NoticeDto userEntity) throws Exception;

    void deleteNotice(Long id) throws ResourceNotFoundException, NoticeGenericException;

    Page<NoticeResponse> getAll(Pageable pageable);

    Page<NoticeResponse> getAllByTitle(String title, Pageable pageable);

    Page<NoticeResponse> getAllNoticeByUser(Pageable pageable) throws ResourceNotFoundException;

    NoticeResponse getNotice(Long id) throws Exception;

    void init() throws IOException;
}
