package com.sagari.service.feign;

import com.sagari.service.VCodeService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author biubiupiu~
 */
@FeignClient("sagari-vcode-service")
public interface VCodeServiceFeign extends VCodeService {
}
