package com.sagari.service.feign;

import com.sagari.dto.input.NoticeFollowDTO;
import com.sagari.dto.input.NoticeGoodDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author biubiupiu~
 */
@FeignClient("sagari-notice-service")
public interface NoticeServiceFeign {

    @PutMapping("/noticeFollow")
    public Boolean noticeFollow(@RequestBody NoticeFollowDTO noticeFollowDTO);

    @PutMapping("/noticeGood")
    public Boolean noticeGood(@RequestBody NoticeGoodDTO noticeGoodDTO);
}
