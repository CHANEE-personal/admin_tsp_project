package com.tsp.new_tsp_admin.api.support.service;

import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity.builder;
import static com.tsp.new_tsp_admin.api.support.mapper.SupportMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("지원모델 Service Test")
class AdminSupportJpaServiceTest {
    @Mock private AdminSupportJpaService mockAdminSupportJpaService;
    private final AdminSupportJpaService adminSupportJpaService;

    private AdminSupportEntity adminSupportEntity;
    private AdminSupportDTO adminSupportDTO;

    void createSupport() {
        adminSupportEntity = builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .supportInstagram("https://instagram.com")
                .visible("Y")
                .build();

        adminSupportDTO = INSTANCE.toDto(adminSupportEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createSupport();
    }

    @Test
    @DisplayName("지원모델 리스트 조회 테스트")
    void 지원모델리스트조회테스트() throws Exception {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        // then
        assertThat(adminSupportJpaService.findSupportsList(supportMap)).isNotEmpty();
    }

    @Test
    @DisplayName("지원모델 리스트 조회 Mockito 테스트")
    void 지원모델리스트조회Mockito테스트() throws Exception {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        List<AdminSupportDTO> returnSupportList = new ArrayList<>();
        returnSupportList.add(AdminSupportDTO.builder()
                .idx(1).supportName("조찬희").supportHeight(170).supportMessage("조찬희")
                .supportPhone("010-1234-5678").supportSize3("31-24-31").supportInstagram("https://instagram.com").visible("Y").build());

        // when
        when(mockAdminSupportJpaService.findSupportsList(supportMap)).thenReturn(returnSupportList);
        List<AdminSupportDTO> supportsList = mockAdminSupportJpaService.findSupportsList(supportMap);

        // then
        assertAll(
                () -> assertThat(supportsList).isNotEmpty(),
                () -> assertThat(supportsList).hasSize(1)
        );

        assertThat(supportsList.get(0).getIdx()).isEqualTo(returnSupportList.get(0).getIdx());
        assertThat(supportsList.get(0).getSupportName()).isEqualTo(returnSupportList.get(0).getSupportName());
        assertThat(supportsList.get(0).getSupportHeight()).isEqualTo(returnSupportList.get(0).getSupportHeight());
        assertThat(supportsList.get(0).getSupportMessage()).isEqualTo(returnSupportList.get(0).getSupportMessage());
        assertThat(supportsList.get(0).getVisible()).isEqualTo(returnSupportList.get(0).getVisible());

        // verify
        verify(mockAdminSupportJpaService, times(1)).findSupportsList(supportMap);
        verify(mockAdminSupportJpaService, atLeastOnce()).findSupportsList(supportMap);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findSupportsList(supportMap);
    }

    @Test
    @DisplayName("지원모델 리스트 조회 BDD 테스트")
    void 지원모델리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        List<AdminSupportDTO> returnSupportList = new ArrayList<>();
        returnSupportList.add(AdminSupportDTO.builder()
                .idx(1).supportName("조찬희").supportHeight(170).supportMessage("조찬희")
                .supportPhone("010-1234-5678").supportSize3("31-24-31").supportInstagram("https://instagram.com").visible("Y").build());

        // when
        given(mockAdminSupportJpaService.findSupportsList(supportMap)).willReturn(returnSupportList);
        List<AdminSupportDTO> supportsList = mockAdminSupportJpaService.findSupportsList(supportMap);

        // then
        assertAll(
                () -> assertThat(supportsList).isNotEmpty(),
                () -> assertThat(supportsList).hasSize(1)
        );

        assertThat(supportsList.get(0).getIdx()).isEqualTo(returnSupportList.get(0).getIdx());
        assertThat(supportsList.get(0).getSupportName()).isEqualTo(returnSupportList.get(0).getSupportName());
        assertThat(supportsList.get(0).getSupportHeight()).isEqualTo(returnSupportList.get(0).getSupportHeight());
        assertThat(supportsList.get(0).getSupportMessage()).isEqualTo(returnSupportList.get(0).getSupportMessage());
        assertThat(supportsList.get(0).getVisible()).isEqualTo(returnSupportList.get(0).getVisible());

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findSupportsList(supportMap);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findSupportsList(supportMap);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @Disabled
    @DisplayName("지원모델 상세 조회 테스트")
    void 지원모델상세조회테스트() throws Exception {
        // given
        adminSupportEntity = builder().idx(1).build();

        // when
        adminSupportDTO = adminSupportJpaService.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(adminSupportDTO.getIdx()).isEqualTo(1);
    }

    @Test
    @DisplayName("지원모델 상세 조회 Mockito 테스트")
    void 지원모델상세조회Mockito테스트() throws Exception {
        // when
        when(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity)).thenReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportDTO.getIdx());
        assertThat(supportInfo.getSupportName()).isEqualTo(adminSupportDTO.getSupportName());
        assertThat(supportInfo.getSupportMessage()).isEqualTo(adminSupportDTO.getSupportMessage());
        assertThat(supportInfo.getVisible()).isEqualTo(adminSupportDTO.getVisible());

        // verify
        verify(mockAdminSupportJpaService, times(1)).findOneSupportModel(adminSupportEntity);
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneSupportModel(adminSupportEntity);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneSupportModel(adminSupportEntity);
    }

    @Test
    @DisplayName("지원모델 상세 조회 BDD 테스트")
    void 지원모델상세조회BDD테스트() throws Exception {
        // when
        given(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity)).willReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportDTO.getIdx());
        assertThat(supportInfo.getSupportName()).isEqualTo(adminSupportDTO.getSupportName());
        assertThat(supportInfo.getSupportMessage()).isEqualTo(adminSupportDTO.getSupportMessage());
        assertThat(supportInfo.getVisible()).isEqualTo(adminSupportDTO.getVisible());

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findOneSupportModel(adminSupportEntity);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneSupportModel(adminSupportEntity);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 수정 Mockito 테스트")
    void 지원모델수정Mockito테스트() throws Exception {
        // given
        adminSupportEntity = builder()
                .idx(adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx())
                .supportName("test")
                .supportPhone("010-9466-2702")
                .supportHeight(170)
                .supportSize3("31-24-31")
                .supportMessage("test")
                .supportInstagram("https://instagram.com")
                .build();

        adminSupportJpaService.updateSupportModel(adminSupportEntity);
        adminSupportDTO = INSTANCE.toDto(adminSupportEntity);

        // when
        when(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity)).thenReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(supportInfo.getSupportName()).isEqualTo("test");
        assertThat(supportInfo.getSupportPhone()).isEqualTo("010-9466-2702");

        // verify
        verify(mockAdminSupportJpaService, times(1)).findOneSupportModel(adminSupportEntity);
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneSupportModel(adminSupportEntity);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneSupportModel(adminSupportEntity);
    }

    @Test
    @DisplayName("지원모델 수정 BDD 테스트")
    void 지원모델수정BDD테스트() throws Exception {
        // given
        adminSupportEntity = builder()
                .idx(adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx())
                .supportName("test")
                .supportPhone("010-9466-2702")
                .supportHeight(170)
                .supportSize3("31-24-31")
                .supportMessage("test")
                .supportInstagram("https://instagram.com")
                .build();

        adminSupportJpaService.updateSupportModel(adminSupportEntity);
        adminSupportDTO = INSTANCE.toDto(adminSupportEntity);

        // when
        given(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity)).willReturn(adminSupportDTO);

        // then
        assertThat(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity).getSupportName()).isEqualTo("test");
        assertThat(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity).getSupportPhone()).isEqualTo("010-9466-2702");

        // verify
        then(mockAdminSupportJpaService).should(times(2)).findOneSupportModel(adminSupportEntity);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneSupportModel(adminSupportEntity);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 삭제 테스트")
    void 지원모델삭제테스트() throws Exception {
        // given
        Integer idx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        // then
        assertThat(adminSupportJpaService.deleteSupportModel(idx)).isNotNull();
    }
}