package hiepnh.noticemanagement.exception;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorResponseDto {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorResponseDto.class);

    private Date timestamp;
    private int totalError;
    private List<ErrorDto> errorDtos;

    public ErrorResponseDto() {
    }

    public ErrorResponseDto(List<ErrorDto> errorDtos) {
        this.errorDtos = errorDtos == null ? new ArrayList<>() : new ArrayList<>(errorDtos);
        this.totalError = this.errorDtos.size();
        this.timestamp = new Date();
    }

    /**
     * @param message message is json string list of error dto
     */
    public ErrorResponseDto(String message) {
        // parse message
        ObjectMapper mapper = new ObjectMapper();
        List<ErrorDto> errorDtos = null;
        try {
            errorDtos = mapper.readValue(message, new TypeReference<List<ErrorDto>>() {
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        this.errorDtos = errorDtos;
        this.totalError = this.errorDtos == null ? 0 : this.errorDtos.size();
        this.timestamp = new Date();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getTotalError() {
        return totalError;
    }

    public List<ErrorDto> getErrors() {
        return errorDtos;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setTotalError(int totalError) {
        this.totalError = totalError;
    }

    public void setListErrors(List<ErrorDto> errorDtos) {
        this.errorDtos = errorDtos;
    }
}
