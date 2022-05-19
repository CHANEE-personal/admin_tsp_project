package com.tsp.new_tsp_admin.api.model.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
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

import static com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Repository Test")
class AdminModelJpaRepositoryTest {
    private AdminModelEntity adminModelEntity;
    private AdminModelDTO adminModelDTO;
    @Autowired
    private AdminModelJpaRepository adminModelJpaRepository;

    @Mock
    private AdminModelJpaRepository mockAdminModelJpaRepository;

    @Autowired
    private EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);

        adminModelEntity = builder()
                .categoryCd(1)
                .categoryAge("2")
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        adminModelDTO = AdminModelDTO.builder()
                .categoryCd(1)
                .categoryAge("2")
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();
    }

    @Test
    public void 모델리스트조회테스트() throws Exception {

        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        // when
        List<AdminModelDTO> modelList = adminModelJpaRepository.findModelsList(modelMap);

        // then
        assertThat(modelList.size()).isGreaterThan(0);
    }

    @Test
    public void 모델상세조회테스트() throws Exception {

        // given
        AdminModelEntity adminModelEntity = builder().idx(3).categoryCd(1).build();

        // when
        AdminModelDTO modelInfo = adminModelJpaRepository.findOneModel(adminModelEntity);

        assertAll(() -> assertThat(modelInfo.getIdx()).isEqualTo(3),
                () -> {
                    assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
                    assertNotNull(modelInfo.getCategoryCd());
                },
                () -> {
                    assertThat(modelInfo.getCategoryAge()).isEqualTo("2");
                    assertNotNull(modelInfo.getCategoryAge());
                },
                () -> {
                    assertThat(modelInfo.getModelKorName()).isEqualTo("조찬희");
                    assertNotNull(modelInfo.getModelKorName());
                },
                () -> {
                    assertThat(modelInfo.getModelEngName()).isEqualTo("CHOCHANHEE");
                    assertNotNull(modelInfo.getModelEngName());
                },
                () -> {
                    assertThat(modelInfo.getModelDescription()).isEqualTo("chaneeCho");
                    assertNotNull(modelInfo.getModelDescription());
                },
                () -> {
                    assertThat(modelInfo.getHeight()).isEqualTo("170");
                    assertNotNull(modelInfo.getHeight());
                },
                () -> {
                    assertThat(modelInfo.getSize3()).isEqualTo("34-24-34");
                    assertNotNull(modelInfo.getSize3());
                },
                () -> {
                    assertThat(modelInfo.getShoes()).isEqualTo("270");
                    assertNotNull(modelInfo.getShoes());
                },
                () -> {
                    assertThat(modelInfo.getVisible()).isEqualTo("Y");
                    assertNotNull(modelInfo.getVisible());
                });

        assertThat(modelInfo.getModelImage().get(0).getTypeName()).isEqualTo("model");
        assertThat(modelInfo.getModelImage().get(0).getImageType()).isEqualTo("main");
        assertThat(modelInfo.getModelImage().get(0).getFileName()).isEqualTo("52d4fdc8-f109-408e-b243-85cc1be207c5.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFilePath()).isEqualTo("/var/www/dist/upload/1223023959779.jpg");

        assertThat(modelInfo.getModelImage().get(1).getTypeName()).isEqualTo("model");
        assertThat(modelInfo.getModelImage().get(1).getImageType()).isEqualTo("sub1");
        assertThat(modelInfo.getModelImage().get(1).getFileName()).isEqualTo("e13f6930-17a5-407c-96ed-fd625b720d21.jpg");
        assertThat(modelInfo.getModelImage().get(1).getFilePath()).isEqualTo("/var/www/dist/upload/1223023959823.jpg");
    }

    @Test
    public void 모델BDD조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        CommonImageDTO commonImageDTO = CommonImageDTO.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> modelList = new ArrayList<>();
        modelList.add(AdminModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희").modelImage(commonImageDtoList).build());

        given(mockAdminModelJpaRepository.findModelsList(modelMap)).willReturn(modelList);

        // when
        Integer idx = mockAdminModelJpaRepository.findModelsList(modelMap).get(0).getIdx();
        Integer categoryCd = mockAdminModelJpaRepository.findModelsList(modelMap).get(0).getCategoryCd();
        String modelKorName = mockAdminModelJpaRepository.findModelsList(modelMap).get(0).getModelKorName();

        assertThat(idx).isEqualTo(modelList.get(0).getIdx());
        assertThat(categoryCd).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(modelKorName).isEqualTo(modelList.get(0).getModelKorName());
    }

    @Test
    public void 모델상세BDD조회테스트() throws Exception {

        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();

        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        AdminModelEntity adminModelEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        AdminModelDTO adminModelDTO = AdminModelDTO.builder()
                .idx(1)
                .categoryCd(1)
                .categoryAge("2")
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .modelImage(ModelImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        given(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).willReturn(adminModelDTO);

        // when
        Integer idx = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getIdx();
        Integer categoryCd = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryCd();
        String categoryAge = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryAge();
        String modelKorName = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelKorName();
        String modelEngName = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelEngName();
        String modelDescription = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelDescription();
        String height = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getHeight();
        String size3 = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getSize3();
        String shoes = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getShoes();
        String visible = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getVisible();
        String fileName = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelImage().get(0).getFileName();
        String fileMask = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelImage().get(0).getFileMask();
        String filePath = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelImage().get(0).getFilePath();
        String imageType = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelImage().get(0).getImageType();
        String typeName = mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelImage().get(0).getTypeName();

        assertThat(idx).isEqualTo(1);
        assertThat(categoryCd).isEqualTo(1);
        assertThat(categoryAge).isEqualTo("2");
        assertThat(modelKorName).isEqualTo("조찬희");
        assertThat(modelEngName).isEqualTo("CHOCHANHEE");
        assertThat(modelDescription).isEqualTo("chaneeCho");
        assertThat(height).isEqualTo("170");
        assertThat(size3).isEqualTo("34-24-34");
        assertThat(shoes).isEqualTo("270");
        assertThat(visible).isEqualTo("Y");
        assertThat(fileName).isEqualTo("test.jpg");
        assertThat(fileMask).isEqualTo("test.jpg");
        assertThat(filePath).isEqualTo("/test/test.jpg");
        assertThat(imageType).isEqualTo("main");
        assertThat(typeName).isEqualTo("model");
    }

    @Test
    public void 모델등록테스트() throws Exception {
        adminModelJpaRepository.insertModel(adminModelEntity);

        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);

        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryCd()).isEqualTo(1);
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryAge()).isEqualTo("2");
    }

    @Test
    public void 모델수정테스트() throws Exception {
        Integer idx = adminModelJpaRepository.insertModel(adminModelEntity);

        adminModelEntity = builder()
                .idx(idx)
                .categoryCd(2)
                .categoryAge("3")
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        adminModelJpaRepository.updateModelByEm(adminModelEntity);

        adminModelDTO = AdminModelDTO.builder()
                .categoryCd(2)
                .categoryAge("3")
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);

        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryCd()).isEqualTo(2);
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryAge()).isEqualTo("3");
    }

    @Test
    public void 모델삭제테스트() throws Exception {
        adminModelEntity = builder()
                .categoryCd(2)
                .categoryAge("3")
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        em.persist(adminModelEntity);

        adminModelDTO = AdminModelDTO.builder()
                .categoryCd(2)
                .categoryAge("3")
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        // when
        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);

        AdminModelEntity adminModelEntity1 = adminModelJpaRepository.deleteModelByEm(adminModelEntity);

        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryCd()).isEqualTo(adminModelEntity1.getCategoryCd());
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryAge()).isEqualTo(adminModelEntity1.getCategoryAge());
    }

    @Test
    public void 모델이미지등록테스트() throws Exception {
        AdminModelEntity adminModelEntity = builder()
                .categoryCd(1)
                .categoryAge("2")
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        Integer modelIdx = adminModelJpaRepository.insertModel(adminModelEntity);

        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(modelIdx)
                .typeName("model")
                .visible("Y")
                .build();

        Integer imageIdx = adminModelJpaRepository.insertModelImage(commonImageEntity);

        assertNotNull(imageIdx);
    }
}