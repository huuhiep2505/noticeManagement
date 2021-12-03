package hiepnh.noticemanagement.service.impl;

import hiepnh.noticemanagement.dto.GenericDataResponse;
import hiepnh.noticemanagement.dto.NoticeDto;
import hiepnh.noticemanagement.dto.NoticeResponse;
import hiepnh.noticemanagement.entity.AttachFileEntity;
import hiepnh.noticemanagement.entity.NoticeEntity;
import hiepnh.noticemanagement.entity.UserEntity;
import hiepnh.noticemanagement.exception.NoticeGenericException;
import hiepnh.noticemanagement.exception.ResourceNotFoundException;
import hiepnh.noticemanagement.repository.NoticeRepository;
import hiepnh.noticemanagement.repository.UserRepository;
import hiepnh.noticemanagement.service.NoticeService;
import hiepnh.noticemanagement.utils.UserAuthenticationUtils;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

@Service
@Log4j2
public class NoticeServiceImpl implements NoticeService {

    private final Path root = Paths.get("uploads");

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthenticationUtils userAuthenticationUtils;

    @Autowired
    private ModelMapper mapper;

    /**
     * create a notice
     *
     * @param @NoticeDto input
     * @throws Exception If user not exist or startDate and endDate not valid
     */
    @Override
    @Cacheable(value = "notice")
    public NoticeEntity createNotice(NoticeDto input) throws Exception {
        //get user to set author of notice
        String username = userAuthenticationUtils.getUsername();
        UserEntity user = userRepository.findByUsername(username);
        Date currentDate = new Date();
        NoticeEntity noticeEntity;
        // check current Date, start Date and end Date ard valid to create
        // start date < end Date
        if (input.getStartDate().before(input.getEndDate())) {
            noticeEntity = mapper.map(input, NoticeEntity.class);
            noticeEntity.setUserEntity(user);
            noticeEntity.setNumberOfView(0);
            noticeEntity.setIsActive(true);
            //set attach files
            List<AttachFileEntity> attachFileEntities = saveFile(input.getAttachFiles(), currentDate.getTime());
            noticeEntity.setAttachFiles(attachFileEntities);
            noticeRepository.save(noticeEntity);
        } else {
            log.error("Exception when create notice with input = {}", input);
            throw new NoticeGenericException("Can't create this notice");
        }
        return noticeEntity;
    }

    /**
     * Update a notice
     *
     * @param @NoticeEntity input
     * @throws Exception If notice not exist
     */
    @Override
    @CachePut(value = "notice")
    public GenericDataResponse updateNotice(NoticeDto noticeRequest) throws Exception {
        GenericDataResponse genericDataResponse = new GenericDataResponse();
        // get notice by ID
        NoticeEntity notice = noticeRepository.findById(noticeRequest.getId()).orElseThrow(() -> new ResourceNotFoundException("Can't find this notice to update"));
        notice.setEndDate(noticeRequest.getEndDate());
        notice.setStartDate(noticeRequest.getStartDate());
        notice.setTitle(noticeRequest.getTitle());
        notice.setContent(noticeRequest.getContent());
        // update new value to db
        noticeRepository.save(notice);
        return genericDataResponse;
    }

    /**
     * Delete a notice
     *
     * @param id input
     * @throws Exception If notice not exist
     */
    @Override
    @CachePut(value = "notice")
    public void deleteNotice(Long id) throws ResourceNotFoundException, NoticeGenericException {
        // get notice by ID
        NoticeEntity notice = noticeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Can't find this notice to delete"));
        // if notice already delete => throw Exception
        if (!notice.getIsActive()) {
            log.error("Exception when delete notice with id = {}", id);
            throw new NoticeGenericException("This notice already deleted");
        } else {
            // set isActive is false that's mean it not exist
            notice.setIsActive(false);
            noticeRepository.save(notice);
        }
    }

    /**
     * Get all notice
     */
    @Override
    @Cacheable(value = "allNotice")
    public Page<NoticeResponse> getAll(Pageable pageable) {
        Date date = new Date();
        //get all notice based on current date and end date and start date
        Page<NoticeResponse> noticeResponses = noticeRepository.
                findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsActiveIsTrue(date, date, pageable).map(new Function<NoticeEntity, NoticeResponse>() {
                    @Override // map data to NoticeResponse
                    public NoticeResponse apply(NoticeEntity noticeEntity) {
                        NoticeResponse noticeResponse = mapper.map(noticeEntity,NoticeResponse.class);
                        noticeResponse.setAuthor(noticeEntity.getUserEntity().getUsername());
                        return noticeResponse;
                    }
                });
        return noticeResponses;
    }
    /**
     * Get all notice by Title
     */
    @Override
    public Page<NoticeResponse> getAllByTitle(String title, Pageable pageable) {
        Date date = new Date();
        //get all notice based on current date and end date and start date and title
        Page<NoticeResponse> noticeResponses = noticeRepository.
                findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndTitleContainingAndIsActiveIsTrue(date, date, title, pageable)
                .map(new Function<NoticeEntity, NoticeResponse>() {
                    @Override // map data to NoticeResponse
                    public NoticeResponse apply(NoticeEntity noticeEntity) {
                        NoticeResponse noticeResponse = mapper.map(noticeEntity,NoticeResponse.class);
                        noticeResponse.setAuthor(noticeEntity.getUserEntity().getUsername());
                        return noticeResponse;
                    }
                });
        return noticeResponses;
    }

    /**
     * Get all notice by current user
     */
    @Override
    public Page<NoticeResponse> getAllNoticeByUser(Pageable pageable) throws ResourceNotFoundException {
        Date date = new Date();
        // get username of current user
        String username = userAuthenticationUtils.getUsername();
        UserEntity user = userRepository.findByUsername(username);
        //get all notice based on current date and end date and start date by current user
        Page<NoticeResponse> noticeResponses = noticeRepository.
                findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsActiveIsTrueAndUserEntity(date, date, user, pageable).map(new Function<NoticeEntity, NoticeResponse>() {
                    @Override //map data to NoticeResponse
                    public NoticeResponse apply(NoticeEntity noticeEntity) {
                        NoticeResponse noticeResponse = mapper.map(noticeEntity, NoticeResponse.class);
                        return noticeResponse;
                    }
                });

        return noticeResponses;
    }

    /**
     * get a notice by id
     *
     * @param id input
     * @throws Exception If notice not exist or endDate after currentDate
     */
    @Override
    @CachePut(value = "notice")
    public NoticeResponse getNotice(Long id) throws Exception {
        // get notice by ID and isActive = true
        NoticeEntity notice = noticeRepository.findByIdAndIsActiveIsTrue(id);
        if (notice == null) {
            log.error("Notice with id = {} not exist", id);
            throw new ResourceNotFoundException("This notice not exist");
        }
        Date date = new Date();
        if (notice.getEndDate().before(date)) {
            log.error("Exception when getNotice with id = {}", id);
            throw new NoticeGenericException("this notice has expire");
        }
        String currentUser = userAuthenticationUtils.getUsername();
        // when this function is called. Number of view will be +1 if current user isn't the author
        if (!currentUser.equals(notice.getUserEntity().getUsername())) {
            notice.setNumberOfView(notice.getNumberOfView() + 1);
        }
        // update db
        noticeRepository.saveAndFlush(notice);
        //use NoticeResponse to get the required fields
        NoticeResponse noticeDto = mapper.map(notice, NoticeResponse.class);
        noticeDto.setAuthor(notice.getUserEntity().getUsername());
        return noticeDto;
    }

    public List<AttachFileEntity> saveFile(MultipartFile[] files, Long time) {
        try {
            List<AttachFileEntity> attachFileEntities = new ArrayList<>();
            //run loop to save each file to folder
            Arrays.asList(files).stream().forEach(file -> {
                save(file, time);
                try {
                    // add each file path to array
                    AttachFileEntity attachFileEntity = new AttachFileEntity();
                    Path pathFile = root.resolve(time + file.getOriginalFilename());
                    Resource resource = new UrlResource(pathFile.toUri());
                    attachFileEntity.setName(resource.getURL().getFile());
                    attachFileEntities.add(attachFileEntity);
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Error: " + e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            return attachFileEntities;
        } catch (Exception e) {
            throw e;
        }

    }

    public void save(MultipartFile file, Long time) {
        try {
            //to copy all bytes from a file to an output stream.
            Files.copy(file.getInputStream(), this.root.resolve(time + file.getOriginalFilename()));
        } catch (Exception e) {
            log.error("Exception when save file to server with file = {}", file.getOriginalFilename());
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    /**
     * init storage path
     */
    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
        }
    }
}
