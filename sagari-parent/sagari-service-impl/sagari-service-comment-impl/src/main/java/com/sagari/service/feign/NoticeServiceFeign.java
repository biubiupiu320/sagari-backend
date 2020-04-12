package com.sagari.service.feign;

import com.sagari.dto.input.NoticeCommentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author biubiupiu~
 */
@FeignClient("sagari-article-service")
public interface NoticeServiceFeign {

    @PutMapping("/noticeComment")
    public Boolean noticeComment(@RequestBody NoticeCommentDTO noticeCommentDTO);
}
