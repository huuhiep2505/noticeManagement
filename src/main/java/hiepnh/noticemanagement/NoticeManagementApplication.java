package hiepnh.noticemanagement;

import hiepnh.noticemanagement.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.Resource;

@SpringBootApplication
@EnableCaching
public class NoticeManagementApplication implements CommandLineRunner {

    @Resource
    NoticeService noticeService;

    public static void main(String[] args) {
        SpringApplication.run(NoticeManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        noticeService.init();
    }
}
