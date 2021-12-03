package hiepnh.noticemanagement.impl;

import hiepnh.noticemanagement.dto.NoticeDto;
import hiepnh.noticemanagement.dto.NoticeResponse;
import hiepnh.noticemanagement.entity.NoticeEntity;
import hiepnh.noticemanagement.entity.UserEntity;
import hiepnh.noticemanagement.exception.NoticeGenericException;
import hiepnh.noticemanagement.exception.ResourceNotFoundException;
import hiepnh.noticemanagement.repository.NoticeRepository;
import hiepnh.noticemanagement.repository.UserRepository;
import hiepnh.noticemanagement.service.impl.NoticeServiceImpl;
import hiepnh.noticemanagement.utils.UserAuthenticationUtils;
import hiepnh.noticemanagement.utils.AuditorAwareImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NoticeServiceImplTest {

    @InjectMocks
    private NoticeServiceImpl noticeService;
    @Mock
    private NoticeRepository noticeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserAuthenticationUtils userAuthenticationUtils;
    @Mock
    private ModelMapper mapper;

    @Test
    public void createNotice_sucessfull() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        MockMultipartFile file
                = new MockMultipartFile(
                "test",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test".getBytes()
        );
        String username = "abc";
        UserEntity user = new UserEntity();
        user.setUsername("test");
        when(userAuthenticationUtils.getUsername()).thenReturn(username);
        when(userRepository.findByUsername(any())).thenReturn(user);
        MultipartFile[] multipartFile = {file};
        NoticeEntity noticeEntity = new NoticeEntity();
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setContent("abc");
        noticeDto.setTitle("abc123");
        noticeDto.setAttachFiles(multipartFile);
        noticeDto.setEndDate(new Date(2022,1,21));
        noticeDto.setStartDate(new Date(2021,12,12));
        noticeDto.setAttachFiles(multipartFile);
        when(mapper.map(noticeDto,NoticeEntity.class)).thenReturn(noticeEntity);
        noticeService.createNotice(noticeDto);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void createNotice_ThrowError() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(userAuthenticationUtils.getUsername()).thenThrow(ResourceNotFoundException.class);
        MultipartFile[] multipartFile = new MultipartFile[0];
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setContent("abc");
        noticeDto.setTitle("abc123");
        noticeDto.setAttachFiles(multipartFile);
        noticeDto.setEndDate(new Date(2022,1,21));
        noticeDto.setStartDate(new Date(2021,12,12));
        noticeService.createNotice(noticeDto);
    }

    @Test(expected = Exception.class)
    public void createNotice_ThrowException() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        MockMultipartFile file
                = new MockMultipartFile(
                "test",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test".getBytes()
        );
        String username = "abc";
        UserEntity user = new UserEntity();
        user.setUsername("test");
        when(userAuthenticationUtils.getUsername()).thenReturn(username);
        when(userRepository.findByUsername(any())).thenReturn(user);
        MultipartFile[] multipartFile = new MultipartFile[0];
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setContent("abc");
        noticeDto.setTitle("abc123");
        noticeDto.setAttachFiles(multipartFile);
        noticeDto.setEndDate(new Date(2021,12,21));
        noticeDto.setStartDate(new Date(2021,12,22));
        noticeService.createNotice(noticeDto);
    }

    @Test
    public void updateNotice_successfull() throws Exception {
        NoticeEntity noticeEntity = new NoticeEntity();
        NoticeDto noticeDto = new NoticeDto();
        noticeEntity.setContent("123");
        noticeEntity.setTitle("abc");
        Mockito.when(noticeRepository.findById(any())).thenReturn(Optional.of(noticeEntity));
        noticeService.updateNotice(noticeDto);
        Mockito.verify(noticeRepository, Mockito.times(1)).findById(any());
    }

    @Test(expected = NullPointerException.class)
    public void updateNotice_throwError() throws Exception {
        NoticeEntity noticeEntity = new NoticeEntity();
        NoticeDto noticeDto = new NoticeDto();
        noticeEntity.setContent("123");
        noticeEntity.setTitle("abc");
        Mockito.when(noticeRepository.findById(any())).thenReturn(null);
        noticeService.updateNotice(noticeDto);
        Mockito.verify(noticeRepository, Mockito.times(1)).findById(any());
    }

    @Test
    public void deleteNotice_successfull() throws Exception {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setIsActive(true);
        Optional<NoticeEntity> notice = Optional.of(noticeEntity);
        Mockito.when(noticeRepository.findById(any())).thenReturn(notice);
        noticeService.deleteNotice(any());
        Mockito.verify(noticeRepository, Mockito.times(1)).findById(any());
    }

    @Test(expected = NoticeGenericException.class)
    public void deleteNotice_throwException() throws Exception {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setIsActive(false);
        Mockito.when(noticeRepository.findById(any())).thenReturn(Optional.of(noticeEntity));
        noticeService.deleteNotice(any());
        Mockito.verify(noticeRepository, Mockito.times(1)).findById(any());
    }

    @Test
    public void getAll_success(){
        Pageable pageable = PageRequest.of(0,10);
        List<NoticeEntity> notices = new ArrayList<>();
        Page<NoticeEntity> noticeEntity = new PageImpl(notices);
        Mockito.when(noticeRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsActiveIsTrue(any(),any(),any())).thenReturn(noticeEntity);
        Page<NoticeResponse> noticeEntityExpected = noticeService.getAll(pageable);
        Mockito.verify(noticeRepository, Mockito.times(1)).findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsActiveIsTrue(any(),any(),any());
        Assert.assertEquals(noticeEntityExpected, noticeEntity);
    }

    @Test
    public void getAllByFilter_success(){
        String title = "title";
        Pageable pageable = PageRequest.of(0,10);
        List<NoticeEntity> notices = new ArrayList<>();
        Page<NoticeEntity> noticeEntity = new PageImpl(notices);
        Mockito.when(noticeRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndTitleContainingAndIsActiveIsTrue(any(),any(), any(),any())).thenReturn(noticeEntity);
        Page<NoticeResponse> noticeEntityExpected = noticeService.getAllByTitle(title, pageable);
        Mockito.verify(noticeRepository, Mockito.times(1)).
                findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndTitleContainingAndIsActiveIsTrue(any(),any(),any(),any());
        Assert.assertEquals(noticeEntityExpected, noticeEntity);
    }

    @Test
    public void getById_success() throws Exception {
        UserEntity user = new UserEntity();
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setTitle("abc");
        noticeEntity.setContent("123");
        noticeEntity.setUserEntity(user);
        noticeEntity.setNumberOfView(1);
        noticeEntity.setEndDate(new Date(2021,12,22));
        Mockito.when(noticeRepository.findByIdAndIsActiveIsTrue(any())).thenReturn(noticeEntity);
        String username = "test";
        when(userAuthenticationUtils.getUsername()).thenReturn(username);
        NoticeResponse noticeDto = new NoticeResponse();
        when(mapper.map(noticeEntity,NoticeResponse.class)).thenReturn(noticeDto);
        noticeService.getNotice(any());
        Mockito.verify(noticeRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test(expected = NoticeGenericException.class)
    public void getById_ThrowError() throws Exception {
        UserEntity user = new UserEntity();
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setTitle("abc");
        noticeEntity.setContent("123");
        noticeEntity.setUserEntity(user);
        noticeEntity.setNumberOfView(1);
        noticeEntity.setEndDate(new Date());
        Mockito.when(noticeRepository.findByIdAndIsActiveIsTrue(any())).thenReturn(noticeEntity);
        noticeService.getNotice(any());
        Mockito.verify(noticeRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test(expected = Exception.class)
    public void getById_throwError() throws Exception {
        UserEntity user = new UserEntity();
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setTitle("abc");
        noticeEntity.setContent("123");
        noticeEntity.setUserEntity(user);
        noticeEntity.setEndDate(new Date());
        Mockito.when(noticeRepository.findByIdAndIsActiveIsTrue(any())).thenReturn(noticeEntity);
        noticeService.getNotice(any());
        Mockito.verify(noticeRepository, Mockito.times(1)).saveAndFlush(any());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getById_throwResourceNotFound() throws Exception {
        UserEntity user = new UserEntity();
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setTitle("abc");
        noticeEntity.setContent("123");
        noticeEntity.setUserEntity(user);
        noticeEntity.setEndDate(new Date());
        Mockito.when(noticeRepository.findByIdAndIsActiveIsTrue(any())).thenReturn(null);
        noticeService.getNotice(any());
        Mockito.verify(noticeRepository, Mockito.times(1)).findByIdAndIsActiveIsTrue(any());
    }

    @Test
    public void getAllByUser_success() throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(0,10);
        List<NoticeResponse> notices = new ArrayList<>();
        Page<NoticeEntity> noticeEntity = new PageImpl(notices);
        Mockito.when(noticeRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsActiveIsTrueAndUserEntity(any(),any(),any(),any())).thenReturn(noticeEntity);
        Page<NoticeResponse> noticeEntityExpected = noticeService.getAllNoticeByUser(pageable);
        Mockito.verify(noticeRepository, Mockito.times(1)).findAllByStartDateLessThanEqualAndEndDateGreaterThanEqualAndIsActiveIsTrueAndUserEntity(any(),any(),any(),any());
        Assert.assertEquals(noticeEntityExpected, noticeEntity);
    }
}
