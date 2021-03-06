package com.tsp.new_tsp_admin.api.model.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.model.mapper.ModelImageMapper;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.new_tsp_admin.exception.TspException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
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
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("?????? Repository Test")
class AdminModelJpaRepositoryTest {
    @Autowired private AdminModelJpaRepository adminModelJpaRepository;
    @Autowired private AdminUserJpaRepository adminUserJpaRepository;
    @Mock private AdminModelJpaRepository mockAdminModelJpaRepository;
    @Autowired private EntityManager em;
    JPAQueryFactory queryFactory;
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

        adminModelEntity = builder()
                .categoryCd(1)
                .categoryAge("2")
                .modelKorFirstName("???")
                .modelKorSecondName("??????")
                .modelKorName("?????????")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .status("draft")
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
        queryFactory = new JPAQueryFactory(em);
        createModelAndImage();
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????????")
    void ??????????????????????????????() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("jpaStartPage", 0);
        modelMap.put("size", 100);

        // then
        assertThat(adminModelJpaRepository.findModelsList(modelMap)).isNotEmpty();
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? ?????????")
    void ????????????????????????????????????() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", -1);

        // then
        assertThatThrownBy(() -> adminModelJpaRepository.findModelsList(modelMap))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void ???????????????????????????() {
        // given
        adminModelEntity = builder().idx(143).categoryCd(2).build();

        // when
        adminModelDTO = adminModelJpaRepository.findOneModel(adminModelEntity);

        // then
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
                    assertThat(adminModelDTO.getModelKorName()).isEqualTo("?????????");
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
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    void ?????????????????????????????????() {
        // given
        adminModelEntity = builder().categoryCd(-1).build();

        // then
        assertThatThrownBy(() -> adminModelJpaRepository.findOneModel(adminModelEntity))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("?????? BDD ?????? ?????????")
    void ??????BDD???????????????() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> modelList = new ArrayList<>();
        modelList.add(AdminModelDTO.builder().idx(3).categoryCd(1).modelKorName("?????????").modelImage(commonImageDtoList).build());

        // when
//        given(mockAdminModelJpaRepository.findModelsList(modelMap)).willReturn(modelList);
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
    @DisplayName("?????? ?????? BDD ?????? ?????????")
    void ????????????BDD???????????????() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        adminModelEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        adminModelDTO = AdminModelDTO.builder()
                .idx(1)
                .categoryCd(1)
                .categoryAge("2")
                .modelKorName("?????????")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .modelImage(ModelImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        // when
//        given(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).willReturn(adminModelDTO);
        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);

        // then
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getIdx()).isEqualTo(1);
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryCd()).isEqualTo(1);
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCategoryAge()).isEqualTo("2");
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getModelKorName()).isEqualTo("?????????");
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
    @DisplayName("?????? ?????? ?????????")
    void ?????????????????????() {
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
    @DisplayName("?????? ?????? CreatedBy ?????????")
    void ????????????CreatedBy?????????() {
        // given
        adminModelJpaRepository.insertModel(adminModelEntity);

        // when
        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);

        // then
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCreator()).isNotEmpty();
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getCreateTime()).isEqualTo(new Date());
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void ???????????????????????????() {
        adminModelEntity = builder()
                .categoryCd(-1)
                .categoryAge("2")
                .modelKorFirstName("???")
                .modelKorSecondName("??????")
                .modelKorName("?????????")
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
    @DisplayName("?????? ?????? ?????????")
    void ?????????????????????() {
        Integer idx = adminModelJpaRepository.insertModel(adminModelEntity).getIdx();

        adminModelEntity = builder()
                .idx(idx)
                .categoryCd(2)
                .categoryAge("3")
                .modelKorFirstName("???")
                .modelKorSecondName("??????")
                .modelKorName("?????????")
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
    @DisplayName("?????? ?????? ?????? ?????????")
    void ???????????????????????????() {
        adminModelEntity = builder()
                .categoryCd(-1)
                .categoryAge("2")
                .modelKorFirstName("???")
                .modelKorSecondName("??????")
                .modelKorName("?????????")
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
    @DisplayName("?????? ?????? ?????????")
    void ?????????????????????() {
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
    @DisplayName("?????? ????????? ?????? ?????????")
    void ??????????????????????????????() {
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
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    void ?????????????????????????????????() {
        CommonCodeEntity commonCodeEntity = CommonCodeEntity.builder()
                .categoryCd(1).visible("Y").cmmType("model").build();

        // then
        assertThat(adminModelJpaRepository.modelCommonCode(commonCodeEntity)).isNotEmpty();
    }
}