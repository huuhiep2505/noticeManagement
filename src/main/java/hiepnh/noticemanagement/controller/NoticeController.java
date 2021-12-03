package hiepnh.noticemanagement.controller;

import hiepnh.noticemanagement.dto.NoticeDto;
import hiepnh.noticemanagement.dto.NoticeResponse;
import hiepnh.noticemanagement.entity.NoticeEntity;
import hiepnh.noticemanagement.exception.NoticeGenericException;
import hiepnh.noticemanagement.exception.ResourceNotFoundException;
import hiepnh.noticemanagement.service.NoticeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static hiepnh.noticemanagement.utils.Constants.*;

@RestController
@RequestMapping("/notice")
@Log4j2
public class NoticeController {
    public static final String REGISTRATION_DATE = "registrationDate";
    @Autowired
    private NoticeService noticeService;

    /**
     * create a notice
     * @Param NoticeDto : Notice need to be create
     * @return @NoticeEntity
     */
    @PostMapping
    public ResponseEntity<?> createNotice(@Valid @ModelAttribute NoticeDto noticeRequest) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(noticeService.createNotice(noticeRequest));
    }

    /**
     * update a notice
     * @Param NoticeDto : contains fields need to update
     * @return success message if update success
     * @throws error message
     */
    @PutMapping
    public ResponseEntity<?> updateNotice(@Valid @RequestBody NoticeDto noticeEntity) throws Exception {
        return ResponseEntity.ok(noticeService.updateNotice(noticeEntity));
    }

    /**
     * delete a notice by ID
     * @Param id : id of notice need to delete
     * @return success message if delete success
     * @throws error message
     */
    @DeleteMapping
    public ResponseEntity<?> deleteNotice(@RequestParam Long id) throws ResourceNotFoundException, NoticeGenericException {
        noticeService.deleteNotice(id);
        return ResponseEntity.status(HttpStatus.OK).body(DELETE_SUCCESS);
    }

    /**
     * get all active notices and can filter by Title
     *
     * @return @Page[@NoticeResponse]
     */
    @GetMapping
    public ResponseEntity<?> getAllNotices(@RequestParam(name = "offset", required = false,defaultValue = "0") Integer offset,
                                           @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
                                           @RequestParam(name = "title", required = false) String title) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(REGISTRATION_DATE).descending());
        Page<NoticeResponse> notices;
        if(title == null){
            notices = noticeService.getAll(pageable);
        }else{
            notices = noticeService.getAllByTitle(title, pageable);
        }
        return ResponseEntity.status(HttpStatus.OK).body(notices);
    }

    /**
     * get all active notices by current user
     *
     * @return @Page[@NoticeResponse]
     */
    @GetMapping("/user")
    public ResponseEntity<?> getAllNoticesByUser(@RequestParam(name = "offset", required = false,defaultValue = "0") Integer offset,
                                                 @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(REGISTRATION_DATE).descending());
        Page<NoticeResponse> notices = noticeService.getAllNoticeByUser(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(notices);
    }

    /**
     * get a notice by ID
     *
     * @return notice if it's available
     * @throws error message
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getNotice(@PathVariable("id") Long id) throws Exception {
        NoticeResponse notice = noticeService.getNotice(id);
        return new ResponseEntity<>(notice, HttpStatus.OK);
    }
}