package com.tsp.new_tsp_admin.api.model.service.agency;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.api.model.mapper.agency.AgencyImageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.model.mapper.agency.AgencyMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@Slf4j
@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 소속사 Repository Test")
class AdminAgencyJpaRepositoryTest {
    @Mock private AdminAgencyJpaRepository mockAdminAgencyJpaRepository;
    private final AdminAgencyJpaRepository adminAgencyJpaRepository;
    private final EntityManager em;

    private AdminAgencyEntity adminAgencyEntity;
    private AdminAgencyDTO adminAgencyDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;

    void createAgency() {
        adminAgencyEntity = AdminAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        adminAgencyDTO = INSTANCE.toDto(adminAgencyEntity);

        commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .typeName("agency")
                .visible("Y")
                .build();

        commonImageDTO = AgencyImageMapper.INSTANCE.toDto(commonImageEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createAgency();
    }

    @Test
    @DisplayName("소속사리스트조회테스트")
    void 소속사리스트조회테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        agencyMap.put("jpaStartPage", 1);
        agencyMap.put("size", 3);

        // then
        assertThat(adminAgencyJpaRepository.findAgencyList(agencyMap)).isNotEmpty();
    }

    @Test
    @DisplayName("소속사상세조회테스트")
    void 소속사상세조회테스트() {
        // given
        adminAgencyEntity = AdminAgencyEntity.builder().idx(1L).build();

        // when
        adminAgencyDTO = adminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertAll(() -> assertThat(adminAgencyDTO.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(adminAgencyDTO.getAgencyName()).isEqualTo("agency");
                    assertNotNull(adminAgencyDTO.getAgencyName());
                },
                () -> {
                    assertThat(adminAgencyDTO.getAgencyDescription()).isEqualTo("agency");
                    assertNotNull(adminAgencyDTO.getAgencyDescription());
                },
                () -> {
                    assertThat(adminAgencyDTO.getVisible()).isEqualTo("Y");
                    assertNotNull(adminAgencyDTO.getVisible());
                });
    }

    @Test
    @DisplayName("소속사Mockito조회테스트")
    void 소속사Mockito조회테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        agencyMap.put("jpaStartPage", 1);
        agencyMap.put("size", 3);

        List<AdminAgencyDTO> agencyList = new ArrayList<>();
        agencyList.add(AdminAgencyDTO.builder().idx(1L)
                .agencyName("agency").agencyDescription("agency").build());

        // when
        when(mockAdminAgencyJpaRepository.findAgencyList(agencyMap)).thenReturn(agencyList);
        List<AdminAgencyDTO> newAgencyList = mockAdminAgencyJpaRepository.findAgencyList(agencyMap);

        // then
        assertThat(newAgencyList.get(0).getIdx()).isEqualTo(agencyList.get(0).getIdx());
        assertThat(newAgencyList.get(0).getAgencyName()).isEqualTo(agencyList.get(0).getAgencyName());
        assertThat(newAgencyList.get(0).getAgencyDescription()).isEqualTo(agencyList.get(0).getAgencyDescription());
        assertThat(newAgencyList.get(0).getVisible()).isEqualTo(agencyList.get(0).getVisible());

        // verify
        verify(mockAdminAgencyJpaRepository, times(1)).findAgencyList(agencyMap);
        verify(mockAdminAgencyJpaRepository, atLeastOnce()).findAgencyList(agencyMap);
        verifyNoMoreInteractions(mockAdminAgencyJpaRepository);

        InOrder inOrder = inOrder(mockAdminAgencyJpaRepository);
        inOrder.verify(mockAdminAgencyJpaRepository).findAgencyList(agencyMap);
    }

    @Test
    @DisplayName("소속사BDD조회테스트")
    void 소속사BDD조회테스트() {
        // given
        Map<String, Object> agencyMap = new HashMap<>();
        agencyMap.put("jpaStartPage", 1);
        agencyMap.put("size", 3);

        List<AdminAgencyDTO> agencyList = new ArrayList<>();
        agencyList.add(AdminAgencyDTO.builder().idx(1L)
                .agencyName("agency").agencyDescription("agency").build());

        // when
        given(mockAdminAgencyJpaRepository.findAgencyList(agencyMap)).willReturn(agencyList);
        List<AdminAgencyDTO> newAgencyList = mockAdminAgencyJpaRepository.findAgencyList(agencyMap);

        // then
        assertThat(newAgencyList.get(0).getIdx()).isEqualTo(agencyList.get(0).getIdx());
        assertThat(newAgencyList.get(0).getAgencyName()).isEqualTo(agencyList.get(0).getAgencyName());
        assertThat(newAgencyList.get(0).getAgencyDescription()).isEqualTo(agencyList.get(0).getAgencyDescription());
        assertThat(newAgencyList.get(0).getVisible()).isEqualTo(agencyList.get(0).getVisible());

        // verify
        then(mockAdminAgencyJpaRepository).should(times(1)).findAgencyList(agencyMap);
        then(mockAdminAgencyJpaRepository).should(atLeastOnce()).findAgencyList(agencyMap);
        then(mockAdminAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사상세Mockito조회테스트")
    void 소속사상세Mockito조회테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminAgencyEntity adminAgencyEntity = AdminAgencyEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();
        AdminAgencyDTO adminAgencyDTO = AdminAgencyDTO.builder()
                .idx(1L)
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .agencyImage(AgencyImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        // when
        when(mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx())).thenReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(1);
        assertThat(agencyInfo.getAgencyName()).isEqualTo("agency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("agency");
        assertThat(agencyInfo.getVisible()).isEqualTo("Y");
        assertThat(agencyInfo.getAgencyImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(agencyInfo.getAgencyImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(agencyInfo.getAgencyImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(agencyInfo.getAgencyImage().get(0).getImageType()).isEqualTo("main");
        assertThat(agencyInfo.getAgencyImage().get(0).getTypeName()).isEqualTo("agency");

        // verify
        verify(mockAdminAgencyJpaRepository, times(1)).findOneAgency(adminAgencyEntity.getIdx());
        verify(mockAdminAgencyJpaRepository, atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        verifyNoMoreInteractions(mockAdminAgencyJpaRepository);

        InOrder inOrder = inOrder(mockAdminAgencyJpaRepository);
        inOrder.verify(mockAdminAgencyJpaRepository).findOneAgency(adminAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("소속사상세BDD조회테스트")
    void 소속사상세BDD조회테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminAgencyEntity adminAgencyEntity = AdminAgencyEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();
        AdminAgencyDTO adminAgencyDTO = AdminAgencyDTO.builder()
                .idx(1L)
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .agencyImage(AgencyImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        // when
        given(mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx())).willReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getIdx()).isEqualTo(1);
        assertThat(agencyInfo.getAgencyName()).isEqualTo("agency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("agency");
        assertThat(agencyInfo.getVisible()).isEqualTo("Y");
        assertThat(agencyInfo.getAgencyImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(agencyInfo.getAgencyImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(agencyInfo.getAgencyImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(agencyInfo.getAgencyImage().get(0).getImageType()).isEqualTo("main");
        assertThat(agencyInfo.getAgencyImage().get(0).getTypeName()).isEqualTo("agency");

        // verify
        then(mockAdminAgencyJpaRepository).should(times(1)).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaRepository).should(atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사등록Mockito테스트")
    void 소속사등록Mockito테스트() {
        // given
        adminAgencyJpaRepository.insertAgency(adminAgencyEntity);

        // when
        when(mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx())).thenReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getAgencyName()).isEqualTo("agency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("agency");
        assertThat(agencyInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminAgencyJpaRepository, times(1)).findOneAgency(adminAgencyEntity.getIdx());
        verify(mockAdminAgencyJpaRepository, atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        verifyNoMoreInteractions(mockAdminAgencyJpaRepository);

        InOrder inOrder = inOrder(mockAdminAgencyJpaRepository);
        inOrder.verify(mockAdminAgencyJpaRepository).findOneAgency(adminAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("소속사등록BDD테스트")
    void 소속사등록BDD테스트() {
        // given
        adminAgencyJpaRepository.insertAgency(adminAgencyEntity);

        // when
        given(mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx())).willReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getAgencyName()).isEqualTo("agency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("agency");
        assertThat(agencyInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminAgencyJpaRepository).should(times(1)).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaRepository).should(atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사수정Mockito테스트")
    void 소속사수정Mockito테스트() {
        // given
        Long idx = adminAgencyJpaRepository.insertAgency(adminAgencyEntity).getIdx();

        adminAgencyEntity = AdminAgencyEntity.builder()
                .idx(idx)
                .agencyName("newAgency")
                .agencyDescription("newAgency")
                .visible("Y")
                .build();

        AdminAgencyDTO adminAgencyDTO = INSTANCE.toDto(adminAgencyEntity);

        adminAgencyJpaRepository.updateAgency(adminAgencyEntity);

        // when
        when(mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx())).thenReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getAgencyName()).isEqualTo("newAgency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("newAgency");

        // verify
        verify(mockAdminAgencyJpaRepository, times(1)).findOneAgency(adminAgencyEntity.getIdx());
        verify(mockAdminAgencyJpaRepository, atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        verifyNoMoreInteractions(mockAdminAgencyJpaRepository);

        InOrder inOrder = inOrder(mockAdminAgencyJpaRepository);
        inOrder.verify(mockAdminAgencyJpaRepository).findOneAgency(adminAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("소속사수정BDD테스트")
    void 소속사수정BDD테스트() {
        // given
        Long idx = adminAgencyJpaRepository.insertAgency(adminAgencyEntity).getIdx();

        adminAgencyEntity = AdminAgencyEntity.builder()
                .idx(idx)
                .agencyName("newAgency")
                .agencyDescription("newAgency")
                .visible("Y")
                .build();

        AdminAgencyDTO adminAgencyDTO = INSTANCE.toDto(adminAgencyEntity);

        adminAgencyJpaRepository.updateAgency(adminAgencyEntity);

        // when
        given(mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx())).willReturn(adminAgencyDTO);
        AdminAgencyDTO agencyInfo = mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(agencyInfo.getAgencyName()).isEqualTo("newAgency");
        assertThat(agencyInfo.getAgencyDescription()).isEqualTo("newAgency");

        // verify
        then(mockAdminAgencyJpaRepository).should(times(1)).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaRepository).should(atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("소속사삭제Mockito테스트")
    void 소속사삭제Mockito테스트() {
        // given
        em.persist(adminAgencyEntity);
        adminAgencyDTO = INSTANCE.toDto(adminAgencyEntity);

        // when
        when(mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx())).thenReturn(adminAgencyDTO);
        Long deleteIdx = adminAgencyJpaRepository.deleteAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminAgencyJpaRepository, times(1)).findOneAgency(adminAgencyEntity.getIdx());
        verify(mockAdminAgencyJpaRepository, atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        verifyNoMoreInteractions(mockAdminAgencyJpaRepository);

        InOrder inOrder = inOrder(mockAdminAgencyJpaRepository);
        inOrder.verify(mockAdminAgencyJpaRepository).findOneAgency(adminAgencyEntity.getIdx());
    }

    @Test
    @DisplayName("소속사삭제BDD테스트")
    void 소속사삭제BDD테스트() {
        // given
        em.persist(adminAgencyEntity);
        adminAgencyDTO = INSTANCE.toDto(adminAgencyEntity);

        // when
        given(mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx())).willReturn(adminAgencyDTO);
        Long deleteIdx = adminAgencyJpaRepository.deleteAgency(adminAgencyEntity.getIdx());

        // then
        assertThat(mockAdminAgencyJpaRepository.findOneAgency(adminAgencyEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminAgencyJpaRepository).should(times(1)).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaRepository).should(atLeastOnce()).findOneAgency(adminAgencyEntity.getIdx());
        then(mockAdminAgencyJpaRepository).shouldHaveNoMoreInteractions();
    }
}