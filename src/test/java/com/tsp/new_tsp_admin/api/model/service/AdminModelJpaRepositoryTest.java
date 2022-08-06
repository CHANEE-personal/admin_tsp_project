package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.CareerJson;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.model.mapper.ModelImageMapper;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.*;

import static com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity.builder;
import static com.tsp.new_tsp_admin.api.model.mapper.ModelMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Repository Test")
class AdminModelJpaRepositoryTest {
    @Mock private AdminModelJpaRepository mockAdminModelJpaRepository;
    private final AdminModelJpaRepository adminModelJpaRepository;
    private final AdminUserJpaRepository adminUserJpaRepository;
    private final EntityManager em;

    private AdminModelEntity adminModelEntity;
    private AdminModelDTO adminModelDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;

    public void createModelAndImage() {
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin02")
                .password("pass1234")
                .name("test")
                .visible("Y")
                .build();

        adminUserJpaRepository.adminLogin(adminUserEntity);

        ArrayList<CareerJson> careerList = new ArrayList<>();
        careerList.add(new CareerJson("title","txt"));

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
                .status("draft")
                .careerList(careerList)
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        adminModelDTO = INSTANCE.toDto(adminModelEntity);

        commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();

        commonImageDTO = ModelImageMapper.INSTANCE.toDto(commonImageEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createModelAndImage();
    }

    @Test
    @DisplayName("모델 리스트 조회 테스트")
    void 모델리스트조회테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("jpaStartPage", 0);
        modelMap.put("size", 100);

        // then
        assertThat(adminModelJpaRepository.findModelsList(modelMap)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 Mockito 조회 테스트")
    void 모델Mockito조회테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> modelList = new ArrayList<>();
        modelList.add(AdminModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희").modelImage(commonImageDtoList).build());

        // when
        when(mockAdminModelJpaRepository.findModelsList(modelMap)).thenReturn(modelList);

        // then
        assertThat(mockAdminModelJpaRepository.findModelsList(modelMap).get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(mockAdminModelJpaRepository.findModelsList(modelMap).get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(mockAdminModelJpaRepository.findModelsList(modelMap).get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());

        // verify
        verify(mockAdminModelJpaRepository, times(3)).findModelsList(modelMap);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findModelsList(modelMap);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);
    }

    @Test
    @DisplayName("모델 리스트 조회 BDD 테스트")
    void 모델리스트조회BDD테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> modelList = new ArrayList<>();
        modelList.add(AdminModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희").modelImage(commonImageDtoList).build());

        // when
        given(mockAdminModelJpaRepository.findModelsList(modelMap)).willReturn(modelList);

        // then
        assertThat(mockAdminModelJpaRepository.findModelsList(modelMap).get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(mockAdminModelJpaRepository.findModelsList(modelMap).get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(mockAdminModelJpaRepository.findModelsList(modelMap).get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());

        // verify
        then(mockAdminModelJpaRepository).should(times(3)).findModelsList(modelMap);
        then(mockAdminModelJpaRepository).should(atLeastOnce()).findModelsList(modelMap);
        then(mockAdminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 상세 조회 테스트")
    void 모델상세조회테스트() {
        // given
        adminModelEntity = builder().idx(143).categoryCd(2).build();

        // when
        adminModelDTO = adminModelJpaRepository.findOneModel(adminModelEntity);

        // then
        assertAll(() -> {
                    assertThat(adminModelDTO.getIdx()).isEqualTo(143);
                },
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
    @DisplayName("모델 상세 조회 Mockito 테스트")
    void 모델상세조회Mockito테스트() {
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

        // when
        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);

        // then
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

        // verify
        verify(mockAdminModelJpaRepository, times(15)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);
    }

    @Test
    @DisplayName("모델 상세 조회 BDD 테스트")
    void 모델상세조회BDD테스트() {
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

        // when
        given(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).willReturn(adminModelDTO);

        // then
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

        // verify
        then(mockAdminModelJpaRepository).should(times(15)).findOneModel(adminModelEntity);
        then(mockAdminModelJpaRepository).should(atLeastOnce()).findOneModel(adminModelEntity);
        then(mockAdminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 등록 Mockito 테스트")
    void 모델등록Mockito테스트() {
        // given
        adminModelJpaRepository.insertModel(adminModelEntity);

        // when
        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);

        // then
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryCd()).isEqualTo(1);
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryAge()).isEqualTo("2");

        // verify
        verify(mockAdminModelJpaRepository, times(2)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);
    }

    @Test
    @DisplayName("모델 등록 BDD 테스트")
    void 모델등록BDD테스트() {
        // given
        adminModelJpaRepository.insertModel(adminModelEntity);

        // when
        given(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).willReturn(adminModelDTO);

        // then
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryCd()).isEqualTo(1);
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryAge()).isEqualTo("2");

        // verify
        then(mockAdminModelJpaRepository).should(times(2)).findOneModel(adminModelEntity);
        then(mockAdminModelJpaRepository).should(atLeastOnce()).findOneModel(adminModelEntity);
        then(mockAdminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 등록 CreatedBy 테스트")
    void 모델등록CreatedBy테스트() {
        // given
        adminModelJpaRepository.insertModel(adminModelEntity);

        // when
        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);

        // then
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCreator()).isNotEmpty();
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCreateTime()).isEqualTo(new Date());
    }

    @Test
    @DisplayName("모델 등록 예외 테스트")
    void 모델등록예외테스트() {
        adminModelEntity = builder()
                .categoryCd(-1)
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

        // then
        assertThatThrownBy(() -> adminModelJpaRepository.insertModel(adminModelEntity))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("모델 수정 Mockito 테스트")
    void 모델수정Mockito테스트() {
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
                .status("active")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        adminModelJpaRepository.updateModelByEm(adminModelEntity);

        adminModelDTO = INSTANCE.toDto(adminModelEntity);

        // when
        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);

        // then
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryCd()).isEqualTo(2);
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryAge()).isEqualTo("3");

        // verify
        verify(mockAdminModelJpaRepository, times(2)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);
    }

    @Test
    @DisplayName("모델 수정 BDD 테스트")
    void 모델수정BDD테스트() {
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
                .status("active")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        adminModelJpaRepository.updateModelByEm(adminModelEntity);

        adminModelDTO = INSTANCE.toDto(adminModelEntity);

        // when
        given(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).willReturn(adminModelDTO);

        // then
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryCd()).isEqualTo(2);
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryAge()).isEqualTo("3");

        // verify
        then(mockAdminModelJpaRepository).should(times(2)).findOneModel(adminModelEntity);
        then(mockAdminModelJpaRepository).should(atLeastOnce()).findOneModel(adminModelEntity);
        then(mockAdminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 수정 예외 테스트")
    void 모델수정예외테스트() {
        adminModelEntity = builder()
                .categoryCd(-1)
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

        // then
        assertThatThrownBy(() -> adminModelJpaRepository.updateModelByEm(adminModelEntity))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("모델 삭제 테스트")
    void 모델삭제테스트() {
        // given
        em.persist(adminModelEntity);

        // when
        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);

        Integer entityIdx = adminModelEntity.getIdx();
        Integer deleteIdx = adminModelJpaRepository.deleteModelByEm(adminModelEntity.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(entityIdx);
    }

    @Test
    @DisplayName("모델 이미지 등록 테스트")
    void 모델이미지등록테스트() {
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
    @DisplayName("모델 공통 코드 조회 테스트")
    void 모델공통코드조회테스트() {
        CommonCodeEntity commonCodeEntity = CommonCodeEntity.builder()
                .categoryCd(1).visible("Y").cmmType("model").build();

        // then
        assertThat(adminModelJpaRepository.modelCommonCode(commonCodeEntity)).isNotEmpty();
    }
}