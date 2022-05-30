package com.tsp.new_tsp_admin.api.model.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private AdminModelJpaRepository adminModelJpaRepository;

    @Mock
    private AdminModelJpaRepository mockAdminModelJpaRepository;

    @Autowired
    private EntityManager em;
    JPAQueryFactory queryFactory;

    private AdminModelEntity adminModelEntity;
    private AdminModelDTO adminModelDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;

    public void createModelAndImage() {
        List<MultipartFile> imageFiles = List.of(
                new MockMultipartFile("test1", "test1.jpg", MediaType.IMAGE_JPEG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.jpg", MediaType.IMAGE_JPEG_VALUE, "test2".getBytes())
        );

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

        commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();

        commonImageDTO = CommonImageDTO.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();
    }

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);
        createModelAndImage();
    }

    @Test
    public void 모델리스트조회테스트() throws Exception {

        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        // then
        assertThat(adminModelJpaRepository.findModelsList(modelMap).size()).isGreaterThan(0);
    }

    @Test
    public void 모델상세조회테스트() throws Exception {

        // given
        adminModelEntity = builder().idx(143).categoryCd(2).build();

        // when
        adminModelDTO = adminModelJpaRepository.findOneModel(adminModelEntity);

        assertAll(() -> assertThat(adminModelDTO.getIdx()).isEqualTo(143),
                () -> {
                    assertThat(adminModelDTO.getCategoryCd()).isEqualTo(2);
                    assertNotNull(adminModelDTO.getCategoryCd());
                },
                () -> {
                    assertThat(adminModelDTO.getCategoryAge()).isEqualTo("2");
                    assertNotNull(adminModelDTO.getCategoryAge());
                },
                () -> {
                    assertThat(adminModelDTO.getModelKorName()).isEqualTo("김예영");
                    assertNotNull(adminModelDTO.getModelKorName());
                },
                () -> {
                    assertThat(adminModelDTO.getModelEngName()).isEqualTo("kimye yeong");
                    assertNotNull(adminModelDTO.getModelEngName());
                },
                () -> {
                    assertThat(adminModelDTO.getModelDescription()).isEqualTo("　");
                    assertNotNull(adminModelDTO.getModelDescription());
                },
                () -> {
                    assertThat(adminModelDTO.getHeight()).isEqualTo("173");
                    assertNotNull(adminModelDTO.getHeight());
                },
                () -> {
                    assertThat(adminModelDTO.getSize3()).isEqualTo("31-24-34");
                    assertNotNull(adminModelDTO.getSize3());
                },
                () -> {
                    assertThat(adminModelDTO.getShoes()).isEqualTo("240");
                    assertNotNull(adminModelDTO.getShoes());
                },
                () -> {
                    assertThat(adminModelDTO.getVisible()).isEqualTo("Y");
                    assertNotNull(adminModelDTO.getVisible());
                });

        assertThat(adminModelDTO.getModelImage().get(0).getTypeName()).isEqualTo("model");
        assertThat(adminModelDTO.getModelImage().get(0).getImageType()).isEqualTo("main");
        assertThat(adminModelDTO.getModelImage().get(0).getFileName()).isEqualTo("1.png");
        assertThat(adminModelDTO.getModelImage().get(0).getFilePath()).isEqualTo("/var/www/dist/upload/0522045010647.png");

        assertThat(adminModelDTO.getModelImage().get(1).getTypeName()).isEqualTo("model");
        assertThat(adminModelDTO.getModelImage().get(1).getImageType()).isEqualTo("sub1");
        assertThat(adminModelDTO.getModelImage().get(1).getFileName()).isEqualTo("2.png");
        assertThat(adminModelDTO.getModelImage().get(1).getFilePath()).isEqualTo("/var/www/dist/upload/0522045010772.png");
    }

    @Test
    public void 모델BDD조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> modelList = new ArrayList<>();
        modelList.add(AdminModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희").modelImage(commonImageDtoList).build());

        given(mockAdminModelJpaRepository.findModelsList(modelMap)).willReturn(modelList);

        assertThat(mockAdminModelJpaRepository.findModelsList(modelMap).get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(mockAdminModelJpaRepository.findModelsList(modelMap).get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(mockAdminModelJpaRepository.findModelsList(modelMap).get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
    }

    @Test
    public void 모델상세BDD조회테스트() throws Exception {

        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        adminModelEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        adminModelDTO = AdminModelDTO.builder()
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

        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getIdx()).isEqualTo(1);
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryCd()).isEqualTo(1);
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryAge()).isEqualTo("2");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelKorName()).isEqualTo("조찬희");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelEngName()).isEqualTo("CHOCHANHEE");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelDescription()).isEqualTo("chaneeCho");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getHeight()).isEqualTo("170");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getSize3()).isEqualTo("34-24-34");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getShoes()).isEqualTo("270");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getVisible()).isEqualTo("Y");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelImage().get(0).getImageType()).isEqualTo("main");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelImage().get(0).getTypeName()).isEqualTo("model");
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
        Integer idx = adminModelJpaRepository.insertModel(adminModelEntity).getIdx();

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

        AdminModelDTO adminModelDTO1 = adminModelJpaRepository.deleteModelByEm(adminModelEntity);

        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryCd()).isEqualTo(adminModelDTO1.getCategoryCd());
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryAge()).isEqualTo(adminModelDTO1.getCategoryAge());
    }

    @Test
    public void 모델이미지등록테스트() throws Exception {
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

        Integer modelIdx = adminModelJpaRepository.insertModel(adminModelEntity).getIdx();

        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(modelIdx)
                .typeName("model")
                .visible("Y")
                .build();

        CommonImageDTO commonImageDTO1 = adminModelJpaRepository.insertModelImage(commonImageEntity);

        assertNotNull(commonImageDTO1);
    }

    @Test
    public void 모델공통코드조회테스트() throws Exception {
        CommonCodeEntity commonCodeEntity = CommonCodeEntity.builder()
                .categoryCd(1).visible("Y").cmmType("model").build();

        // then
        assertThat(adminModelJpaRepository.modelCommonCode(commonCodeEntity).size()).isGreaterThan(0);
    }
}