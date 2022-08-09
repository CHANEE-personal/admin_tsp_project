package com.tsp.new_tsp_admin.api.common.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.common.mapper.CommonCodeMapper.INSTANCE;
import static com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("공통코드 Service Test")
class AdminCommonJpaServiceTest {
    @Mock private AdminCommonJpaService mockAdminCommonJpaService;
    private final AdminCommonJpaService adminCommonJpaService;
    private CommonCodeEntity commonCodeEntity;
    private CommonCodeDTO commonCodeDTO;

    public void createCommonCode() {

        // 공통코드 생성
        commonCodeEntity = CommonCodeEntity.builder()
                .categoryCd(1)
                .categoryNm("남성")
                .cmmType("model")
                .visible("Y")
                .build();

        commonCodeDTO = INSTANCE.toDto(commonCodeEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createCommonCode();
    }

    @Test
    @DisplayName("공통코드 리스트 조회 테스트")
    void 공통코드리스트조회테스트() throws Exception {
        // given
        Map<String, Object> commonMap = new HashMap<>();
        commonMap.put("jpaStartPage", 0);
        commonMap.put("size", 100);

        // then
        assertThat(adminCommonJpaService.findCommonCodeList(commonMap)).isNotEmpty();
    }

    @Test
    @DisplayName("공통코드 리스트 조회 Mockito 테스트")
    void 공통코드리스트조회Mockito테스트() throws Exception {
        Map<String, Object> commonMap = new HashMap<>();
        commonMap.put("jpaStartPage", 1);
        commonMap.put("size", 3);

        List<CommonCodeDTO> commonCodeList = new ArrayList<>();
        commonCodeList.add(CommonCodeDTO.builder().idx(1).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build());

        // when
        when(mockAdminCommonJpaService.findCommonCodeList(commonMap)).thenReturn(commonCodeList);
        List<CommonCodeDTO> newCommonCodeList = mockAdminCommonJpaService.findCommonCodeList(commonMap);

        // then
        assertThat(newCommonCodeList.get(0).getIdx()).isEqualTo(commonCodeList.get(0).getIdx());
        assertThat(newCommonCodeList.get(0).getCategoryCd()).isEqualTo(commonCodeList.get(0).getCategoryCd());
        assertThat(newCommonCodeList.get(0).getCategoryNm()).isEqualTo(commonCodeList.get(0).getCategoryNm());
        assertThat(newCommonCodeList.get(0).getCmmType()).isEqualTo(commonCodeList.get(0).getCmmType());

        // verify
        verify(mockAdminCommonJpaService, times(1)).findCommonCodeList(commonMap);
        verify(mockAdminCommonJpaService, atLeastOnce()).findCommonCodeList(commonMap);
        verifyNoMoreInteractions(mockAdminCommonJpaService);

        InOrder inOrder = inOrder(mockAdminCommonJpaService);
        inOrder.verify(mockAdminCommonJpaService).findCommonCodeList(commonMap);
    }

    @Test
    @DisplayName("공통코드 리스트 조회 BDD 테스트")
    void 공통코드리스트조회BDD테스트() throws Exception {
        Map<String, Object> commonMap = new HashMap<>();
        commonMap.put("jpaStartPage", 1);
        commonMap.put("size", 3);

        List<CommonCodeDTO> commonCodeList = new ArrayList<>();
        commonCodeList.add(CommonCodeDTO.builder().idx(1).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build());

        // when
        given(mockAdminCommonJpaService.findCommonCodeList(commonMap)).willReturn(commonCodeList);
        List<CommonCodeDTO> newCommonCodeList = mockAdminCommonJpaService.findCommonCodeList(commonMap);

        // then
        assertThat(newCommonCodeList.get(0).getIdx()).isEqualTo(commonCodeList.get(0).getIdx());
        assertThat(newCommonCodeList.get(0).getCategoryCd()).isEqualTo(commonCodeList.get(0).getCategoryCd());
        assertThat(newCommonCodeList.get(0).getCategoryNm()).isEqualTo(commonCodeList.get(0).getCategoryNm());
        assertThat(newCommonCodeList.get(0).getCmmType()).isEqualTo(commonCodeList.get(0).getCmmType());

        // verify
        then(mockAdminCommonJpaService).should(times(1)).findCommonCodeList(commonMap);
        then(mockAdminCommonJpaService).should(atLeastOnce()).findCommonCodeList(commonMap);
        then(mockAdminCommonJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 상세 조회 Mockito 테스트")
    void 공통코드상세조회Mockito테스트() throws Exception {
        // given
        commonCodeEntity = builder().idx(1).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build();

        commonCodeDTO = CommonCodeDTO.builder()
                .idx(1)
                .categoryCd(1)
                .categoryNm("men")
                .cmmType("model")
                .visible("Y")
                .build();

        // when
        when(mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity)).thenReturn(commonCodeDTO);
        CommonCodeDTO commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity);

        // then
        assertThat(commonCodeInfo.getIdx()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryCd()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        verify(mockAdminCommonJpaService, times(1)).findOneCommonCode(commonCodeEntity);
        verify(mockAdminCommonJpaService, atLeastOnce()).findOneCommonCode(commonCodeEntity);
        verifyNoMoreInteractions(mockAdminCommonJpaService);

        InOrder inOrder = inOrder(mockAdminCommonJpaService);
        inOrder.verify(mockAdminCommonJpaService).findOneCommonCode(commonCodeEntity);
    }

    @Test
    @DisplayName("공통코드 상세 조회 BDD 테스트")
    void 공통코드상세조회BDD테스트() throws Exception {
        // given
        commonCodeEntity = builder().idx(1).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build();

        commonCodeDTO = CommonCodeDTO.builder()
                .idx(1)
                .categoryCd(1)
                .categoryNm("men")
                .cmmType("model")
                .visible("Y")
                .build();

        // when
        when(mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity)).thenReturn(commonCodeDTO);
        CommonCodeDTO commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity);

        // then
        assertThat(commonCodeInfo.getIdx()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryCd()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        then(mockAdminCommonJpaService).should(times(1)).findOneCommonCode(commonCodeEntity);
        then(mockAdminCommonJpaService).should(atLeastOnce()).findOneCommonCode(commonCodeEntity);
        then(mockAdminCommonJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 등록 Mockito 테스트")
    void 공통코드등록Mockito테스트() throws Exception {
        // given
        adminCommonJpaService.insertCommonCode(commonCodeEntity);

        // when
        when(mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity)).thenReturn(commonCodeDTO);
        CommonCodeDTO commonInfo = mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity);

        // then
        assertThat(commonInfo.getCategoryCd()).isEqualTo(commonCodeDTO.getCategoryCd());
        assertThat(commonInfo.getCategoryNm()).isEqualTo(commonCodeDTO.getCategoryNm());
        assertThat(commonInfo.getCmmType()).isEqualTo(commonCodeDTO.getCmmType());

        // verify
        verify(mockAdminCommonJpaService, times(1)).findOneCommonCode(commonCodeEntity);
        verify(mockAdminCommonJpaService, atLeastOnce()).findOneCommonCode(commonCodeEntity);
        verifyNoMoreInteractions(mockAdminCommonJpaService);

        InOrder inOrder = inOrder(mockAdminCommonJpaService);
        inOrder.verify(mockAdminCommonJpaService).findOneCommonCode(commonCodeEntity);
    }

    @Test
    @DisplayName("공통코드 등록 BDD 테스트")
    void 공통코드등록BDD테스트() throws Exception {
        // given
        adminCommonJpaService.insertCommonCode(commonCodeEntity);

        // when
        given(mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity)).willReturn(commonCodeDTO);
        CommonCodeDTO commonInfo = mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity);

        // then
        assertThat(commonInfo.getCategoryCd()).isEqualTo(commonCodeDTO.getCategoryCd());
        assertThat(commonInfo.getCategoryNm()).isEqualTo(commonCodeDTO.getCategoryNm());
        assertThat(commonInfo.getCmmType()).isEqualTo(commonCodeDTO.getCmmType());

        // verify
        then(mockAdminCommonJpaService).should(times(1)).findOneCommonCode(commonCodeEntity);
        then(mockAdminCommonJpaService).should(atLeastOnce()).findOneCommonCode(commonCodeEntity);
        then(mockAdminCommonJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 수정 Mockito 테스트")
    void 공통코드수정Mockito테스트() throws Exception {
        // given
        Integer idx = adminCommonJpaService.insertCommonCode(commonCodeEntity).getIdx();

        commonCodeEntity = CommonCodeEntity.builder()
                .idx(idx)
                .categoryCd(2)
                .categoryNm("new men")
                .cmmType("model")
                .visible("Y")
                .build();

        adminCommonJpaService.updateCommonCode(commonCodeEntity);

        commonCodeDTO = INSTANCE.toDto(commonCodeEntity);

        // when
        when(mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity)).thenReturn(commonCodeDTO);
        CommonCodeDTO commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity);

        // then
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("new men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        verify(mockAdminCommonJpaService, times(1)).findOneCommonCode(commonCodeEntity);
        verify(mockAdminCommonJpaService, atLeastOnce()).findOneCommonCode(commonCodeEntity);
        verifyNoMoreInteractions(mockAdminCommonJpaService);

        InOrder inOrder = inOrder(mockAdminCommonJpaService);
        inOrder.verify(mockAdminCommonJpaService).findOneCommonCode(commonCodeEntity);
    }

    @Test
    @DisplayName("공통코드 수정 BDD 테스트")
    void 공통코드수정BDD테스트() throws Exception {
        // given
        Integer idx = adminCommonJpaService.insertCommonCode(commonCodeEntity).getIdx();

        commonCodeEntity = CommonCodeEntity.builder()
                .idx(idx)
                .categoryCd(2)
                .categoryNm("new men")
                .cmmType("model")
                .visible("Y")
                .build();

        adminCommonJpaService.updateCommonCode(commonCodeEntity);

        commonCodeDTO = INSTANCE.toDto(commonCodeEntity);

        // when
        given(mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity)).willReturn(commonCodeDTO);
        CommonCodeDTO commonCodeInfo = mockAdminCommonJpaService.findOneCommonCode(commonCodeEntity);

        // then
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("new men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        then(mockAdminCommonJpaService).should(times(1)).findOneCommonCode(commonCodeEntity);
        then(mockAdminCommonJpaService).should(atLeastOnce()).findOneCommonCode(commonCodeEntity);
        then(mockAdminCommonJpaService).shouldHaveNoMoreInteractions();
    }
}