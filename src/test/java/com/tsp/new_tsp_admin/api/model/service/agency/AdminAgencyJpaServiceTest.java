package com.tsp.new_tsp_admin.api.model.service.agency;

import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
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

import static com.tsp.new_tsp_admin.api.model.mapper.agency.AgencyMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
@DisplayName("소속사 Service Test")
class AdminAgencyJpaServiceTest {
    @Mock private AdminAgencyJpaService mockAdminAgencyJpaService;
    private final AdminAgencyJpaService adminAgencyJpaService;

    private AdminAgencyEntity adminAgencyEntity;
    private AdminAgencyDTO adminAgencyDTO;

    void createAgency() {
        adminAgencyEntity = AdminAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        adminAgencyDTO = INSTANCE.toDto(adminAgencyEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createAgency();
    }

    @Test
    @DisplayName("소속사 리스트 조회 테스트")
    void 소속사리스트조회테스트() throws Exception {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        agencyMap.put("jpaStartPage", 1);
        agencyMap.put("size", 3);

        // then
        assertThat(adminAgencyJpaService.findAgencyList(agencyMap)).isNotEmpty();
    }

    @Test
    @DisplayName("소속사 리스트 조회 Mockito 테스트")
    void 소속사리스트조회Mockito테스트() throws Exception {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        agencyMap.put("jpaStartPage", 1);
        agencyMap.put("size", 3);

        List<AdminAgencyDTO> returnAgencyList = new ArrayList<>();

        returnAgencyList.add(AdminAgencyDTO.builder().idx(1).agencyName("agency1").agencyDescription("agency1").visible("Y").build());
        returnAgencyList.add(AdminAgencyDTO.builder().idx(2).agencyName("agency2").agencyDescription("agency2").visible("Y").build());

        // when
        when(mockAdminAgencyJpaService.findAgencyList(agencyMap)).thenReturn(returnAgencyList);
        List<AdminAgencyDTO> agencyList = mockAdminAgencyJpaService.findAgencyList(agencyMap);

        // then
        assertAll(
                () -> assertThat(agencyList).isNotEmpty(),
                () -> assertThat(agencyList).hasSize(2)
        );

        assertThat(agencyList.get(0).getIdx()).isEqualTo(returnAgencyList.get(0).getIdx());
        assertThat(agencyList.get(0).getAgencyName()).isEqualTo(returnAgencyList.get(0).getAgencyName());
        assertThat(agencyList.get(0).getAgencyDescription()).isEqualTo(returnAgencyList.get(0).getAgencyDescription());
        assertThat(agencyList.get(0).getVisible()).isEqualTo(returnAgencyList.get(0).getVisible());

        assertThat(agencyList.get(1).getIdx()).isEqualTo(returnAgencyList.get(1).getIdx());
        assertThat(agencyList.get(1).getAgencyName()).isEqualTo(returnAgencyList.get(1).getAgencyName());
        assertThat(agencyList.get(1).getAgencyDescription()).isEqualTo(returnAgencyList.get(1).getAgencyDescription());
        assertThat(agencyList.get(1).getVisible()).isEqualTo(returnAgencyList.get(1).getVisible());

        // verify
        verify(mockAdminAgencyJpaService, times(1)).findAgencyList(agencyMap);
        verify(mockAdminAgencyJpaService, atLeastOnce()).findAgencyList(agencyMap);
        verifyNoMoreInteractions(mockAdminAgencyJpaService);

        InOrder inOrder = inOrder(mockAdminAgencyJpaService);
        inOrder.verify(mockAdminAgencyJpaService).findAgencyList(agencyMap);
    }

    @Test
    @DisplayName("소속사 리스트 조회 BDD 테스트")
    void 소속사리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        agencyMap.put("jpaStartPage", 1);
        agencyMap.put("size", 3);

        List<AdminAgencyDTO> returnAgencyList = new ArrayList<>();

        returnAgencyList.add(AdminAgencyDTO.builder().idx(1).agencyName("agency1").agencyDescription("agency1").visible("Y").build());
        returnAgencyList.add(AdminAgencyDTO.builder().idx(2).agencyName("agency2").agencyDescription("agency2").visible("Y").build());

        // when
        given(mockAdminAgencyJpaService.findAgencyList(agencyMap)).willReturn(returnAgencyList);
        List<AdminAgencyDTO> agencyList = mockAdminAgencyJpaService.findAgencyList(agencyMap);

        // then
        assertAll(
                () -> assertThat(agencyList).isNotEmpty(),
                () -> assertThat(agencyList).hasSize(2)
        );

        assertThat(agencyList.get(0).getIdx()).isEqualTo(returnAgencyList.get(0).getIdx());
        assertThat(agencyList.get(0).getAgencyName()).isEqualTo(returnAgencyList.get(0).getAgencyName());
        assertThat(agencyList.get(0).getAgencyDescription()).isEqualTo(returnAgencyList.get(0).getAgencyDescription());
        assertThat(agencyList.get(0).getVisible()).isEqualTo(returnAgencyList.get(0).getVisible());

        assertThat(agencyList.get(1).getIdx()).isEqualTo(returnAgencyList.get(1).getIdx());
        assertThat(agencyList.get(1).getAgencyName()).isEqualTo(returnAgencyList.get(1).getAgencyName());
        assertThat(agencyList.get(1).getAgencyDescription()).isEqualTo(returnAgencyList.get(1).getAgencyDescription());
        assertThat(agencyList.get(1).getVisible()).isEqualTo(returnAgencyList.get(1).getVisible());

        // verify
        then(mockAdminAgencyJpaService).should(times(1)).findAgencyList(agencyMap);
        then(mockAdminAgencyJpaService).should(atLeastOnce()).findAgencyList(agencyMap);
        then(mockAdminAgencyJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사 상세 조회 테스트")
    void 소속사상세조회테스트() throws Exception {
        // given
        adminAgencyEntity = AdminAgencyEntity.builder().idx(1).build();

        // then
        assertThat(adminAgencyJpaService.findOneAgency(adminAgencyEntity).getAgencyName()).isEqualTo("agency");
    }

    @Test
    @DisplayName("소속사상세조회Mockito테스트")
    void 소속사상세조회Mockito테스트() throws Exception {
        // when
        when(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity)).thenReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity);

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(adminAgencyDTO.getIdx());
        assertThat(agencyInfo.getAgencyName()).isEqualTo(adminAgencyDTO.getAgencyName());
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo(adminAgencyDTO.getAgencyDescription());
        assertThat(agencyInfo.getVisible()).isEqualTo(adminAgencyDTO.getVisible());

        // verify
        verify(mockAdminAgencyJpaService, times(1)).findOneAgency(adminAgencyEntity);
        verify(mockAdminAgencyJpaService, atLeastOnce()).findOneAgency(adminAgencyEntity);
        verifyNoMoreInteractions(mockAdminAgencyJpaService);

        InOrder inOrder = inOrder(mockAdminAgencyJpaService);
        inOrder.verify(mockAdminAgencyJpaService).findOneAgency(adminAgencyEntity);
    }

    @Test
    @DisplayName("소속사상세조회BDD테스트")
    void 소속사상세조회BDD테스트() throws Exception {
        // when
        given(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity)).willReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity);

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(adminAgencyDTO.getIdx());
        assertThat(agencyInfo.getAgencyName()).isEqualTo(adminAgencyDTO.getAgencyName());
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo(adminAgencyDTO.getAgencyDescription());
        assertThat(agencyInfo.getVisible()).isEqualTo(adminAgencyDTO.getVisible());

        // verify
        then(mockAdminAgencyJpaService).should(times(1)).findOneAgency(adminAgencyEntity);
        then(mockAdminAgencyJpaService).should(atLeastOnce()).findOneAgency(adminAgencyEntity);
        then(mockAdminAgencyJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사 수정 Mockito 테스트")
    void 소속사수정Mockito테스트() throws Exception {
        // given
        Integer idx = adminAgencyJpaService.insertAgency(adminAgencyEntity).getIdx();

        adminAgencyEntity = AdminAgencyEntity.builder()
                .idx(idx)
                .agencyName("newAgency")
                .agencyDescription("newAgency")
                .visible("Y")
                .build();

        AdminAgencyDTO adminAgencyDTO = INSTANCE.toDto(adminAgencyEntity);

        adminAgencyJpaService.updateAgency(adminAgencyEntity);

        // when
        when(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity)).thenReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity);

        // then
        assertThat(agencyInfo.getAgencyName()).isEqualTo("newAgency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("newAgency");

        // verify
        verify(mockAdminAgencyJpaService, times(1)).findOneAgency(adminAgencyEntity);
        verify(mockAdminAgencyJpaService, atLeastOnce()).findOneAgency(adminAgencyEntity);
        verifyNoMoreInteractions(mockAdminAgencyJpaService);

        InOrder inOrder = inOrder(mockAdminAgencyJpaService);
        inOrder.verify(mockAdminAgencyJpaService).findOneAgency(adminAgencyEntity);
    }

    @Test
    @DisplayName("소속사 수정 BDD 테스트")
    void 소속사수정BDD테스트() throws Exception {
        // given
        Integer idx = adminAgencyJpaService.insertAgency(adminAgencyEntity).getIdx();

        adminAgencyEntity = AdminAgencyEntity.builder()
                .idx(idx)
                .agencyName("newAgency")
                .agencyDescription("newAgency")
                .visible("Y")
                .build();

        AdminAgencyDTO adminAgencyDTO = INSTANCE.toDto(adminAgencyEntity);

        adminAgencyJpaService.updateAgency(adminAgencyEntity);

        // when
        given(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity)).willReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity);

        // then
        assertThat(agencyInfo.getAgencyName()).isEqualTo("newAgency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("newAgency");

        // verify
        then(mockAdminAgencyJpaService).should(times(1)).findOneAgency(adminAgencyEntity);
        then(mockAdminAgencyJpaService).should(atLeastOnce()).findOneAgency(adminAgencyEntity);
        then(mockAdminAgencyJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사 삭제 Mockito 테스트")
    void 소속사삭제Mockito테스트() throws Exception {
        // given
        adminAgencyJpaService.insertAgency(adminAgencyEntity);
        adminAgencyDTO = INSTANCE.toDto(adminAgencyEntity);

        // when
        when(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity)).thenReturn(adminAgencyDTO);
        Integer deleteIdx = adminAgencyJpaService.deleteAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminAgencyJpaService, times(1)).findOneAgency(adminAgencyEntity);
        verify(mockAdminAgencyJpaService, atLeastOnce()).findOneAgency(adminAgencyEntity);
        verifyNoMoreInteractions(mockAdminAgencyJpaService);

        InOrder inOrder = inOrder(mockAdminAgencyJpaService);
        inOrder.verify(mockAdminAgencyJpaService).findOneAgency(adminAgencyEntity);
    }

    @Test
    @DisplayName("소속사 삭제 BDD 테스트")
    void 소속사삭제BDD테스트() throws Exception {
        // given
        adminAgencyJpaService.insertAgency(adminAgencyEntity);
        adminAgencyDTO = INSTANCE.toDto(adminAgencyEntity);

        // when
        given(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity)).willReturn(adminAgencyDTO);
        Integer deleteIdx = adminAgencyJpaService.deleteAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(mockAdminAgencyJpaService.findOneAgency(adminAgencyEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminAgencyJpaService).should(times(1)).findOneAgency(adminAgencyEntity);
        then(mockAdminAgencyJpaService).should(atLeastOnce()).findOneAgency(adminAgencyEntity);
        then(mockAdminAgencyJpaService).shouldHaveNoMoreInteractions();
    }
}