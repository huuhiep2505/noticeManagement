package hiepnh.noticemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hiepnh.noticemanagement.dto.NoticeDto;
import hiepnh.noticemanagement.dto.NoticeResponse;
import hiepnh.noticemanagement.entity.NoticeEntity;
import hiepnh.noticemanagement.exception.ResourceNotFoundException;
import hiepnh.noticemanagement.service.CustomAccountService;
import hiepnh.noticemanagement.service.impl.NoticeServiceImpl;
import hiepnh.noticemanagement.utils.JwtUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = NoticeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class NoticeControllerTest {
    public static final String BASE_URL ="/notice";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NoticeServiceImpl noticeService;
    @MockBean
    private CustomAccountService customAccountService;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void testCreateNotice_success() throws Exception{
        NoticeEntity noticeEntity = new NoticeEntity();
        NoticeDto noticeDto = new NoticeDto();
        Map<String, String> map = new HashMap<>();
        map.put("title","test");
        map.put("content","test");
        String request = convertMapToString(map);
        Mockito.when(noticeService.createNotice(noticeDto)).thenReturn(noticeEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(request);
        mockMvc.perform(requestBuilder).andExpect(status().is2xxSuccessful());
    }


    @Test(expected = Exception.class)
    public void testCreateNotice_throwException() throws Exception{
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setContent("content");
        noticeDto.setTitle("abc");
        noticeDto.setEndDate(new Date("2021-06-12 10:20:30"));
        noticeDto.setStartDate(new Date("2021-06-12 10:20:30"));
        doThrow(Exception.class).when(noticeService).createNotice(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(noticeDto));
        mockMvc.perform(requestBuilder).andExpect(status().is5xxServerError());
    }

    @Test
    public void testGetAllNotice_success() throws Exception{
        List<NoticeResponse> notices = new ArrayList<>();
        Page<NoticeResponse> noticeEntity = new PageImpl(notices);
        Mockito.when(noticeService.getAll(any())).thenReturn(noticeEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testUpdateNotice_success() throws Exception{
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setContent("content");
        noticeEntity.setTitle("abc");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(noticeEntity));
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testUpdateNotice_throwException() throws Exception{
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setContent("content");
        noticeEntity.setTitle("abc");
        doThrow(Exception.class).when(noticeService).updateNotice(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(noticeEntity));
        mockMvc.perform(requestBuilder).andExpect(status().is4xxClientError());
    }

    @Test
    public void testUpdateNotice_throwResourceNotFoundException() throws Exception{
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setContent("content");
        noticeEntity.setTitle("abc");
        doThrow(ResourceNotFoundException.class).when(noticeService).updateNotice(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(noticeEntity));
        mockMvc.perform(requestBuilder).andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeleteNotice_success() throws Exception{
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(BASE_URL+"?id=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testDeleteNotice_throwError() throws Exception{
        doThrow(ResourceNotFoundException.class).when(noticeService).deleteNotice(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(BASE_URL+"?id=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().is4xxClientError());
    }

    @Test
    public void testDeleteNotice_throwResourceNotFoundException() throws Exception{
        doThrow(ResourceNotFoundException.class).when(noticeService).deleteNotice(any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(BASE_URL+"?id=1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetNotice_success() throws Exception{
        NoticeResponse noticeInquiryDto = new NoticeResponse();
        when(noticeService.getNotice(any())).thenReturn(noticeInquiryDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL+"/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testGetNotice_throwError() throws Exception {
        when(noticeService.getNotice(any())).thenThrow(Exception.class);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL+"/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetNotice_throwResourceNotFoundException() throws Exception {
        when(noticeService.getNotice(any())).thenThrow(ResourceNotFoundException.class);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL+"/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetAllNoticeByUser_success() throws Exception{
        List<NoticeResponse> notices = new ArrayList<>();
        Page<NoticeResponse> noticeEntity = new PageImpl(notices);
        Mockito.when(noticeService.getAll(any())).thenReturn(noticeEntity);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URL+"/user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().is2xxSuccessful());
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String convertMapToString(Map<String,String> map){
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value = map.get(key);
            try {
                stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
                stringBuilder.append("=");
                stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }
        return stringBuilder.toString();
    }
}