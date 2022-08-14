package com.tsp.new_tsp_admin.api.model;

import com.tsp.new_tsp_admin.common.SearchCommon;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@Api(tags = "모델 소속사 관련 API")
@RequestMapping("/api/jpa-agency")
@RequiredArgsConstructor
public class AdminAgencyJpaController {

    private final SearchCommon searchCommon;

}
