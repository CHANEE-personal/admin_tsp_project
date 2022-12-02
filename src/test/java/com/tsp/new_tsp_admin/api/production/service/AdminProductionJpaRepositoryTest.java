package com.tsp.new_tsp_admin.api.production.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.api.production.mapper.ProductionImageMapper;
import com.tsp.new_tsp_admin.api.production.mapper.ProductionImageMapperImpl;
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

import static com.tsp.new_tsp_admin.api.production.mapper.ProductionMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@Slf4j
@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("프로덕션 Repository Test")
class AdminProductionJpaRepositoryTest {
    @Mock private AdminProductionJpaRepository mockAdminProductionJpaRepository;
    private final AdminProductionJpaRepository adminProductionJpaRepository;
    private final EntityManager em;

    private AdminProductionEntity adminProductionEntity;
    private AdminProductionDTO adminProductionDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    void createProductionAndImage() {
        adminProductionEntity = AdminProductionEntity.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        adminProductionDTO = INSTANCE.toDto(adminProductionEntity);

        commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .typeName("production")
                .visible("Y")
                .build();

        commonImageDTO = ProductionImageMapperImpl.INSTANCE.toDto(commonImageEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createProductionAndImage();
    }

    @Test
    @DisplayName("프로덕션리스트조회테스트")
    void 프로덕션리스트조회테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        // then
        assertThat(adminProductionJpaRepository.findProductionList(productionMap)).isNotEmpty();
    }

    @Test
    @DisplayName("프로덕션상세조회테스트")
    void 프로덕션상세조회테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(1L).build();

        // when
        adminProductionDTO = adminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertAll(() -> assertThat(adminProductionDTO.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(adminProductionDTO.getTitle()).isEqualTo("테스트1");
                    assertNotNull(adminProductionDTO.getTitle());
                },
                () -> {
                    assertThat(adminProductionDTO.getDescription()).isEqualTo("테스트1");
                    assertNotNull(adminProductionDTO.getDescription());
                },
                () -> {
                    assertThat(adminProductionDTO.getVisible()).isEqualTo("Y");
                    assertNotNull(adminProductionDTO.getVisible());
                });

        assertThat(adminProductionDTO.getProductionImage().get(0).getTypeName()).isEqualTo("production");
        assertThat(adminProductionDTO.getProductionImage().get(0).getImageType()).isEqualTo("main");
        assertThat(adminProductionDTO.getProductionImage().get(0).getFileName()).isEqualTo("52d4fdc8-f109-408e-b243-85cc1be207c5.jpg");
        assertThat(adminProductionDTO.getProductionImage().get(0).getFileMask()).isEqualTo("1223024921206.jpg");
    }

    @Test
    @DisplayName("프로덕션Mockito조회테스트")
    void 프로덕션Mockito조회테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminProductionDTO> productionList = new ArrayList<>();
        productionList.add(AdminProductionDTO.builder().idx(1L).title("프로덕션 테스트")
                .description("프로덕션 테스트").productionImage(commonImageDtoList).build());

        // when
        when(mockAdminProductionJpaRepository.findProductionList(productionMap)).thenReturn(productionList);
        List<AdminProductionDTO> newProductionList = mockAdminProductionJpaRepository.findProductionList(productionMap);

        // then
        assertThat(newProductionList.get(0).getIdx()).isEqualTo(productionList.get(0).getIdx());
        assertThat(newProductionList.get(0).getTitle()).isEqualTo(productionList.get(0).getTitle());
        assertThat(newProductionList.get(0).getDescription()).isEqualTo(productionList.get(0).getDescription());
        assertThat(newProductionList.get(0).getVisible()).isEqualTo(productionList.get(0).getVisible());

        // verify
        verify(mockAdminProductionJpaRepository, times(1)).findProductionList(productionMap);
        verify(mockAdminProductionJpaRepository, atLeastOnce()).findProductionList(productionMap);
        verifyNoMoreInteractions(mockAdminProductionJpaRepository);

        InOrder inOrder = inOrder(mockAdminProductionJpaRepository);
        inOrder.verify(mockAdminProductionJpaRepository).findProductionList(productionMap);
    }

    @Test
    @DisplayName("프로덕션BDD조회테스트")
    void 프로덕션BDD조회테스트() {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminProductionDTO> productionList = new ArrayList<>();
        productionList.add(AdminProductionDTO.builder().idx(1L).title("프로덕션 테스트")
                .description("프로덕션 테스트").productionImage(commonImageDtoList).build());

        // when
        given(mockAdminProductionJpaRepository.findProductionList(productionMap)).willReturn(productionList);
        List<AdminProductionDTO> newProductionList = mockAdminProductionJpaRepository.findProductionList(productionMap);

        // then
        assertThat(newProductionList.get(0).getIdx()).isEqualTo(productionList.get(0).getIdx());
        assertThat(newProductionList.get(0).getTitle()).isEqualTo(productionList.get(0).getTitle());
        assertThat(newProductionList.get(0).getDescription()).isEqualTo(productionList.get(0).getDescription());
        assertThat(newProductionList.get(0).getVisible()).isEqualTo(productionList.get(0).getVisible());

        // verify
        then(mockAdminProductionJpaRepository).should(times(1)).findProductionList(productionMap);
        then(mockAdminProductionJpaRepository).should(atLeastOnce()).findProductionList(productionMap);
        then(mockAdminProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션상세Mockito조회테스트")
    void 프로덕션상세Mockito조회테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminProductionEntity adminProductionEntity = AdminProductionEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();

        AdminProductionDTO adminProductionDTO = AdminProductionDTO.builder()
                .idx(1L)
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .productionImage(ProductionImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        // when
        when(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(1);
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getVisible()).isEqualTo("Y");
        assertThat(productionInfo.getProductionImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getImageType()).isEqualTo("main");
        assertThat(productionInfo.getProductionImage().get(0).getTypeName()).isEqualTo("production");

        // verify
        verify(mockAdminProductionJpaRepository, times(1)).findOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaRepository, atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaRepository);

        InOrder inOrder = inOrder(mockAdminProductionJpaRepository);
        inOrder.verify(mockAdminProductionJpaRepository).findOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션상세BDD조회테스트")
    void 프로덕션상세BDD조회테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminProductionEntity adminProductionEntity = AdminProductionEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();

        AdminProductionDTO adminProductionDTO = AdminProductionDTO.builder()
                .idx(1L)
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .productionImage(ProductionImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        // when
        given(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(1);
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getVisible()).isEqualTo("Y");
        assertThat(productionInfo.getProductionImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(productionInfo.getProductionImage().get(0).getImageType()).isEqualTo("main");
        assertThat(productionInfo.getProductionImage().get(0).getTypeName()).isEqualTo("production");

        // verify
        then(mockAdminProductionJpaRepository).should(times(1)).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).should(atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 프로덕션 상세 조회 테스트")
    void 이전or다음프로덕션상세조회테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx());

        // 이전 프로덕션
        assertThat(adminProductionJpaRepository.findPrevOneProduction(adminProductionEntity.getIdx()).getIdx()).isEqualTo(117);
        // 다음 프로덕션
        assertThat(adminProductionJpaRepository.findNextOneProduction(adminProductionEntity.getIdx()).getIdx()).isEqualTo(119);
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 Mockito 테스트")
    void 이전프로덕션상세조회Mockito테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx());

        when(mockAdminProductionJpaRepository.findPrevOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaRepository.findPrevOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        verify(mockAdminProductionJpaRepository, times(1)).findPrevOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaRepository, atLeastOnce()).findPrevOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaRepository);

        InOrder inOrder = inOrder(mockAdminProductionJpaRepository);
        inOrder.verify(mockAdminProductionJpaRepository).findPrevOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 BDD 테스트")
    void 이전프로덕션상세조회BDD테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx());

        given(mockAdminProductionJpaRepository.findPrevOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaRepository.findPrevOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        then(mockAdminProductionJpaRepository).should(times(1)).findPrevOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).should(atLeastOnce()).findPrevOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 Mockito 테스트")
    void 다음프로덕션상세조회Mockito테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx());

        when(mockAdminProductionJpaRepository.findNextOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaRepository.findNextOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        verify(mockAdminProductionJpaRepository, times(1)).findNextOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaRepository, atLeastOnce()).findNextOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaRepository);

        InOrder inOrder = inOrder(mockAdminProductionJpaRepository);
        inOrder.verify(mockAdminProductionJpaRepository).findNextOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 BDD 테스트")
    void 다음프로덕션상세조회BDD테스트() {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118L).build();

        // when
        adminProductionDTO = adminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx());

        given(mockAdminProductionJpaRepository.findNextOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaRepository.findNextOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        then(mockAdminProductionJpaRepository).should(times(1)).findNextOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).should(atLeastOnce()).findNextOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션등록Mockito테스트")
    void 프로덕션등록Mockito테스트() {
        // given
        adminProductionJpaRepository.insertProduction(adminProductionEntity);

        // when
        when(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminProductionJpaRepository, times(1)).findOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaRepository, atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaRepository);

        InOrder inOrder = inOrder(mockAdminProductionJpaRepository);
        inOrder.verify(mockAdminProductionJpaRepository).findOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션등록BDD테스트")
    void 프로덕션등록BDD테스트() {
        // given
        adminProductionJpaRepository.insertProduction(adminProductionEntity);

        // when
        given(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminProductionJpaRepository).should(times(1)).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).should(atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션이미지등록테스트")
    void 프로덕션이미지등록테스트() {
        Long productionIdx = adminProductionJpaRepository.insertProduction(adminProductionEntity).getIdx();

        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(productionIdx)
                .typeName("production")
                .visible("Y")
                .build();

        assertNotNull(adminProductionJpaRepository.insertProductionImage(commonImageEntity));
    }

    @Test
    @DisplayName("프로덕션수정Mockito테스트")
    void 프로덕션수정Mockito테스트() {
        // given
        Long idx = adminProductionJpaRepository.insertProduction(adminProductionEntity).getIdx();

        adminProductionEntity = AdminProductionEntity.builder()
                .idx(idx)
                .title("프로덕션 테스트1")
                .description("프로덕션 테스트1")
                .visible("Y")
                .build();

        AdminProductionDTO adminProductionDTO = INSTANCE.toDto(adminProductionEntity);

        adminProductionJpaRepository.updateProductionByEm(adminProductionEntity);

        // when
        when(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트1");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트1");

        // verify
        verify(mockAdminProductionJpaRepository, times(1)).findOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaRepository, atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaRepository);

        InOrder inOrder = inOrder(mockAdminProductionJpaRepository);
        inOrder.verify(mockAdminProductionJpaRepository).findOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션수정BDD테스트")
    void 프로덕션수정BDD테스트() {
        // given
        Long idx = adminProductionJpaRepository.insertProduction(adminProductionEntity).getIdx();

        adminProductionEntity = AdminProductionEntity.builder()
                .idx(idx)
                .title("프로덕션 테스트1")
                .description("프로덕션 테스트1")
                .visible("Y")
                .build();

        AdminProductionDTO adminProductionDTO = INSTANCE.toDto(adminProductionEntity);

        adminProductionJpaRepository.updateProductionByEm(adminProductionEntity);

        // when
        given(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx())).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx());

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트1");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트1");

        // verify
        then(mockAdminProductionJpaRepository).should(times(1)).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).should(atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션삭제테스트")
    void 프로덕션삭제테스트() {
        // given
        em.persist(adminProductionEntity);

        Long entityIdx = adminProductionEntity.getIdx();
        Long idx = adminProductionJpaRepository.deleteProductionByEm(adminProductionEntity.getIdx());

        // then
        assertThat(entityIdx).isEqualTo(idx);
    }

    @Test
    @DisplayName("프로덕션삭제Mockito테스트")
    void 프로덕션삭제Mockito테스트() {
        // given
        em.persist(adminProductionEntity);
        adminProductionDTO = INSTANCE.toDto(adminProductionEntity);

        // when
        when(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        Long deleteIdx = adminProductionJpaRepository.deleteProductionByEm(adminProductionEntity.getIdx());

        // then
        assertThat(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminProductionJpaRepository, times(1)).findOneProduction(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaRepository, atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaRepository);

        InOrder inOrder = inOrder(mockAdminProductionJpaRepository);
        inOrder.verify(mockAdminProductionJpaRepository).findOneProduction(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션삭제BDD테스트")
    void 프로덕션삭제BDD테스트() {
        // given
        em.persist(adminProductionEntity);
        adminProductionDTO = INSTANCE.toDto(adminProductionEntity);

        // when
        when(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx())).thenReturn(adminProductionDTO);
        Long deleteIdx = adminProductionJpaRepository.deleteProductionByEm(adminProductionEntity.getIdx());

        // then
        assertThat(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminProductionJpaRepository).should(times(1)).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).should(atLeastOnce()).findOneProduction(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 어드민 코멘트 조회 Mockito 테스트")
    void 프로덕션어드민코멘트조회Mockito테스트() {
        adminProductionEntity = AdminProductionEntity.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        Long productionIdx = adminProductionJpaRepository.insertProduction(adminProductionEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("production")
                .commentTypeIdx(productionIdx)
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("production")
                .commentTypeIdx(productionIdx)
                .visible("Y")
                .build());

        when(mockAdminProductionJpaRepository.findProductionAdminComment(adminProductionEntity.getIdx())).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminProductionJpaRepository.findProductionAdminComment(adminProductionEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("production");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminProductionEntity.getIdx());

        // verify
        verify(mockAdminProductionJpaRepository, times(1)).findProductionAdminComment(adminProductionEntity.getIdx());
        verify(mockAdminProductionJpaRepository, atLeastOnce()).findProductionAdminComment(adminProductionEntity.getIdx());
        verifyNoMoreInteractions(mockAdminProductionJpaRepository);

        InOrder inOrder = inOrder(mockAdminProductionJpaRepository);
        inOrder.verify(mockAdminProductionJpaRepository).findProductionAdminComment(adminProductionEntity.getIdx());
    }

    @Test
    @DisplayName("프로덕션 어드민 코멘트 조회 BDD 테스트")
    void 프로덕션어드민코멘트조회BDD테스트() {
        adminProductionEntity = AdminProductionEntity.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        Long productionIdx = adminProductionJpaRepository.insertProduction(adminProductionEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("production")
                .commentTypeIdx(productionIdx)
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("production")
                .commentTypeIdx(productionIdx)
                .visible("Y")
                .build());

        given(mockAdminProductionJpaRepository.findProductionAdminComment(adminProductionEntity.getIdx())).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminProductionJpaRepository.findProductionAdminComment(adminProductionEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("production");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminProductionEntity.getIdx());

        // verify
        then(mockAdminProductionJpaRepository).should(times(1)).findProductionAdminComment(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).should(atLeastOnce()).findProductionAdminComment(adminProductionEntity.getIdx());
        then(mockAdminProductionJpaRepository).shouldHaveNoMoreInteractions();
    }
}