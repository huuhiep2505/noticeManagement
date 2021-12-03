package hiepnh.noticemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoticeDto {
    private Long id;
    @NotBlank(message = "title can't blank")
    private String title;
    @NotBlank(message = "content can't blank")
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Start date should be today or in future")
    private Date startDate;
    @Future(message = "End date should be in future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private MultipartFile[] attachFiles;
}
