package com.tsp.new_tsp_admin.api.production.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.api.model.mapper.ModelImageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("프로덕션 Repository Test")
class AdminProductionJpaRepositoryTest {
    private AdminProductionEntity adminProductionEntity;
    private AdminProductionDTO adminProductionDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;

    @Autowired
    private AdminProductionJpaRepository adminProductionJpaRepository;

    @Mock
    private AdminProductionJpaRepository mockAdminProductionJpaRepository;

    @Autowired
    private EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);

        adminProductionEntity = builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        adminProductionDTO = AdminProductionDTO.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        commonImageEntity = commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("production")
                .visible("Y")
                .build();

        commonImageDTO = CommonImageDTO.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("production")
                .build();
    }

    @Test
    public void 프로덕션리스트조회테스트() throws Exception {

        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        // when
        List<AdminProductionDTO> productionList = adminProductionJpaRepository.findProductionsList(productionMap);

        // then
        assertThat(productionList.size()).isGreaterThan(0);
    }

    @Test
    public void 프로덕션상세조회테스트() throws Exception {

        // given
        adminProductionEntity = builder().idx(1).build();

        // when
        adminProductionDTO = adminProductionJpaRepository.findOneProduction(adminProductionEntity);

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
    public void 프로덕션BDD조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> productionMap = new ConcurrentHashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminProductionDTO> productionList = new ArrayList<>();
        productionList.add(AdminProductionDTO.builder().idx(1).title("프로덕션 테스트")
                .description("프로덕션 테스트").productionImage(commonImageDtoList).build());

        given(mockAdminProductionJpaRepository.findProductionsList(productionMap)).willReturn(productionList);

        // when
        Integer idx = mockAdminProductionJpaRepository.findProductionsList(productionMap).get(0).getIdx();
        String title = mockAdminProductionJpaRepository.findProductionsList(productionMap).get(0).getTitle();
        String description = mockAdminProductionJpaRepository.findProductionsList(productionMap).get(0).getDescription();
        String visible = mockAdminProductionJpaRepository.findProductionsList(productionMap).get(0).getVisible();

        assertThat(idx).isEqualTo(productionList.get(0).getIdx());
        assertThat(title).isEqualTo(productionList.get(0).getTitle());
        assertThat(description).isEqualTo(productionList.get(0).getDescription());
        assertThat(visible).isEqualTo(productionList.get(0).getVisible());
    }

    @Test
    public void 프로덕션상세BDD조회테스트() throws Exception {

        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminProductionEntity adminProductionEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        AdminProductionDTO adminProductionDTO = AdminProductionDTO.builder()
                .idx(1)
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .productionImage(ModelImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        given(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity)).willReturn(adminProductionDTO);

        // when
        Integer idx = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getIdx();
        String title = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getTitle();
        String description = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getDescription();
        String visible = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getVisible();
        String fileName = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getProductionImage().get(0).getFileName();
        String fileMask = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getProductionImage().get(0).getFileMask();
        String filePath = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getProductionImage().get(0).getFilePath();
        String imageType = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getProductionImage().get(0).getImageType();
        String typeName = mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getProductionImage().get(0).getTypeName();

        assertThat(idx).isEqualTo(1);
        assertThat(title).isEqualTo("프로덕션 테스트");
        assertThat(description).isEqualTo("프로덕션 테스트");
        assertThat(visible).isEqualTo("Y");
        assertThat(fileName).isEqualTo("test.jpg");
        assertThat(fileMask).isEqualTo("test.jpg");
        assertThat(filePath).isEqualTo("/test/test.jpg");
        assertThat(imageType).isEqualTo("main");
        assertThat(typeName).isEqualTo("production");
    }

    @Test
    public void 프로덕션등록테스트() throws Exception {
        adminProductionJpaRepository.insertProduction(adminProductionEntity);

        when(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity)).thenReturn(adminProductionDTO);

        assertThat(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getDescription()).isEqualTo("프로덕션 테스트");
        assertThat(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getVisible()).isEqualTo("Y");
    }

    @Test
    public void 프로덕션이미지등록테스트() throws Exception {
        Integer productionIdx = adminProductionJpaRepository.insertProduction(adminProductionEntity).getIdx();

        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(productionIdx)
                .typeName("production")
                .visible("Y")
                .build();

        Integer imageIdx = adminProductionJpaRepository.insertProductionImage(commonImageEntity);

        assertNotNull(imageIdx);
    }

    @Test
    public void 프로덕션수정테스트() throws Exception {
        Integer idx = adminProductionJpaRepository.insertProduction(adminProductionEntity).getIdx();

        adminProductionEntity = builder()
                .idx(idx)
                .title("프로덕션 테스트1")
                .description("프로덕션 테스트1")
                .visible("Y")
                .build();

        AdminProductionDTO adminProductionDTO = AdminProductionDTO.builder()
                .title("프로덕션 테스트1")
                .description("프로덕션 테스트1")
                .visible("Y")
                .build();

        adminProductionJpaRepository.updateProductionByEm(adminProductionEntity);

        when(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity)).thenReturn(adminProductionDTO);

        assertThat(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getTitle()).isEqualTo("프로덕션 테스트1");
        assertThat(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getDescription()).isEqualTo("프로덕션 테스트1");
    }

    @Test
    public void 프로덕션삭제테스트() throws Exception {

        em.persist(adminProductionEntity);

        adminProductionDTO = AdminProductionDTO.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        // when
        when(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity)).thenReturn(adminProductionDTO);

        AdminProductionDTO adminProductionDTO1 = adminProductionJpaRepository.deleteProductionByEm(adminProductionEntity);

        assertThat(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getTitle()).isEqualTo(adminProductionDTO1.getTitle());
        assertThat(mockAdminProductionJpaRepository.findOneProduction(adminProductionEntity).getDescription()).isEqualTo(adminProductionDTO1.getDescription());
    }
}