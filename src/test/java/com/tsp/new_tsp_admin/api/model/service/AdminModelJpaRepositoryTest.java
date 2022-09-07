package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.CareerJson;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.model.mapper.ModelImageMapper;
import com.tsp.new_tsp_admin.api.model.mapper.ModelMapper;
import com.tsp.new_tsp_admin.api.model.mapper.agency.AgencyMapper;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
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

import java.util.*;

import static java.time.LocalDateTime.*;
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
    private AdminAgencyEntity adminAgencyEntity;
    private AdminAgencyDTO adminAgencyDTO;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    public void createModelAndImage() {
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin02")
                .password("pass1234")
                .name("test")
                .visible("Y")
                .build();

        adminUserJpaRepository.adminLogin(adminUserEntity);

        adminAgencyEntity = AdminAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        adminAgencyDTO = AgencyMapper.INSTANCE.toDto(adminAgencyEntity);

        ArrayList<CareerJson> careerList = new ArrayList<>();
        careerList.add(new CareerJson("title","txt"));

        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .favoriteCount(1)
                .viewCount(1)
                .agencyIdx(adminAgencyEntity.getIdx())
                .adminAgencyEntity(adminAgencyEntity)
                .careerList(careerList)
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        adminModelDTO = ModelMapper.INSTANCE.toDto(adminModelEntity);

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
        modelList.add(AdminModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());

        // when
        when(mockAdminModelJpaRepository.findModelsList(modelMap)).thenReturn(modelList);
        List<AdminModelDTO> newModelList = mockAdminModelJpaRepository.findModelsList(modelMap);

        // then
        assertThat(newModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(newModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(newModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());

        // verify
        verify(mockAdminModelJpaRepository, times(1)).findModelsList(modelMap);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findModelsList(modelMap);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaRepository);
        inOrder.verify(mockAdminModelJpaRepository).findModelsList(modelMap);
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
        modelList.add(AdminModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희").modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());

        // when
        given(mockAdminModelJpaRepository.findModelsList(modelMap)).willReturn(modelList);
        List<AdminModelDTO> newModelList = mockAdminModelJpaRepository.findModelsList(modelMap);

        // then
        assertThat(newModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(newModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(newModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());

        // verify
        then(mockAdminModelJpaRepository).should(times(1)).findModelsList(modelMap);
        then(mockAdminModelJpaRepository).should(atLeastOnce()).findModelsList(modelMap);
        then(mockAdminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 상세 조회 테스트")
    void 모델상세조회테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(143).categoryCd(2).build();

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
                    assertThat(adminModelDTO.getCategoryAge()).isEqualTo(2);
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
                    assertThat(adminModelDTO.getHeight()).isEqualTo(173);
                    assertNotNull(adminModelDTO.getHeight());
                },
                () -> {
                    assertThat(adminModelDTO.getSize3()).isEqualTo("31-24-34");
                    assertNotNull(adminModelDTO.getSize3());
                },
                () -> {
                    assertThat(adminModelDTO.getShoes()).isEqualTo(240);
                    assertNotNull(adminModelDTO.getShoes());
                },
                () -> {
                    assertThat(adminModelDTO.getModelAgency().getAgencyName()).isEqualTo("agency");
                    assertNotNull(adminModelDTO.getModelAgency().getAgencyName());
                },
                () -> {
                    assertThat(adminModelDTO.getModelAgency().getAgencyDescription()).isEqualTo("agency");
                    assertNotNull(adminModelDTO.getModelAgency().getAgencyDescription());
                }
                );

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

        adminModelEntity = AdminModelEntity.builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        adminModelDTO = AdminModelDTO.builder()
                .idx(1)
                .categoryCd(1)
                .categoryAge(2)
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .modelAgency(AgencyMapper.INSTANCE.toDto(adminAgencyEntity))
                .modelImage(ModelImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        // when
        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaRepository.findOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(1);
        assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
        assertThat(modelInfo.getCategoryAge()).isEqualTo(2);
        assertThat(modelInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(modelInfo.getModelEngName()).isEqualTo("CHOCHANHEE");
        assertThat(modelInfo.getModelDescription()).isEqualTo("chaneeCho");
        assertThat(modelInfo.getHeight()).isEqualTo(170);
        assertThat(modelInfo.getSize3()).isEqualTo("34-24-34");
        assertThat(modelInfo.getShoes()).isEqualTo(270);
        assertThat(modelInfo.getVisible()).isEqualTo("Y");
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo("agency");
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo("agency");
        assertThat(modelInfo.getModelImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getImageType()).isEqualTo("main");
        assertThat(modelInfo.getModelImage().get(0).getTypeName()).isEqualTo("model");

        // verify
        verify(mockAdminModelJpaRepository, times(1)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaRepository);
        inOrder.verify(mockAdminModelJpaRepository).findOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("모델 상세 조회 BDD 테스트")
    void 모델상세조회BDD테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        adminModelEntity = AdminModelEntity.builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        adminModelDTO = AdminModelDTO.builder()
                .idx(1)
                .categoryCd(1)
                .categoryAge(2)
                .modelKorName("조찬희")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .modelAgency(AgencyMapper.INSTANCE.toDto(adminAgencyEntity))
                .modelImage(ModelImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        // when
        given(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaRepository.findOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(1);
        assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
        assertThat(modelInfo.getCategoryAge()).isEqualTo(2);
        assertThat(modelInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(modelInfo.getModelEngName()).isEqualTo("CHOCHANHEE");
        assertThat(modelInfo.getModelDescription()).isEqualTo("chaneeCho");
        assertThat(modelInfo.getHeight()).isEqualTo(170);
        assertThat(modelInfo.getSize3()).isEqualTo("34-24-34");
        assertThat(modelInfo.getShoes()).isEqualTo(270);
        assertThat(modelInfo.getVisible()).isEqualTo("Y");
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo("agency");
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo("agency");
        assertThat(modelInfo.getModelImage().get(0).getFileName()).isEqualTo("test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFileMask()).isEqualTo("test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getFilePath()).isEqualTo("/test/test.jpg");
        assertThat(modelInfo.getModelImage().get(0).getImageType()).isEqualTo("main");
        assertThat(modelInfo.getModelImage().get(0).getTypeName()).isEqualTo("model");

        // verify
        then(mockAdminModelJpaRepository).should(times(1)).findOneModel(adminModelEntity);
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
        AdminModelDTO modelInfo = mockAdminModelJpaRepository.findOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
        assertThat(modelInfo.getCategoryAge()).isEqualTo(2);

        // verify
        verify(mockAdminModelJpaRepository, times(1)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaRepository);
        inOrder.verify(mockAdminModelJpaRepository).findOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("모델 등록 BDD 테스트")
    void 모델등록BDD테스트() {
        // given
        adminModelJpaRepository.insertModel(adminModelEntity);

        // when
        given(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaRepository.findOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getCategoryCd()).isEqualTo(1);
        assertThat(modelInfo.getCategoryAge()).isEqualTo(2);

        // verify
        then(mockAdminModelJpaRepository).should(times(1)).findOneModel(adminModelEntity);
        then(mockAdminModelJpaRepository).should(atLeastOnce()).findOneModel(adminModelEntity);
        then(mockAdminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 등록 CreatedBy 테스트")
    void 모델등록CreatedBy테스트() {
        // given
        AdminModelDTO modelInfo = adminModelJpaRepository.insertModel(adminModelEntity);

        // when
        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(modelInfo);
        AdminModelDTO newModelInfo = mockAdminModelJpaRepository.findOneModel(adminModelEntity);

        // then
        assertThat(newModelInfo.getCreator()).isNotNull();
        assertThat(newModelInfo.getCreateTime()).isNotNull();

        InOrder inOrder = inOrder(mockAdminModelJpaRepository);
        inOrder.verify(mockAdminModelJpaRepository).findOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("모델 등록 예외 테스트")
    void 모델등록예외테스트() {
        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(-1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height(170)
                .size3("34-24-34")
                .status("active")
                .shoes(270)
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

        adminModelEntity = AdminModelEntity.builder()
                .idx(idx)
                .categoryCd(2)
                .categoryAge(3)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        adminModelJpaRepository.updateModelByEm(adminModelEntity);

        adminModelDTO = ModelMapper.INSTANCE.toDto(adminModelEntity);

        // when
        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaRepository.findOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getCategoryCd()).isEqualTo(2);
        assertThat(modelInfo.getCategoryAge()).isEqualTo(3);

        // verify
        verify(mockAdminModelJpaRepository, times(1)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaRepository);
        inOrder.verify(mockAdminModelJpaRepository).findOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("모델 수정 BDD 테스트")
    void 모델수정BDD테스트() {
        Integer idx = adminModelJpaRepository.insertModel(adminModelEntity).getIdx();

        adminModelEntity = AdminModelEntity.builder()
                .idx(idx)
                .categoryCd(2)
                .categoryAge(3)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        adminModelJpaRepository.updateModelByEm(adminModelEntity);

        adminModelDTO = ModelMapper.INSTANCE.toDto(adminModelEntity);

        // when
        given(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaRepository.findOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getCategoryCd()).isEqualTo(2);
        assertThat(modelInfo.getCategoryAge()).isEqualTo(3);

        // verify
        then(mockAdminModelJpaRepository).should(times(1)).findOneModel(adminModelEntity);
        then(mockAdminModelJpaRepository).should(atLeastOnce()).findOneModel(adminModelEntity);
        then(mockAdminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 수정 예외 테스트")
    void 모델수정예외테스트() {
        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(-1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .status("active")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
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

        Integer entityIdx = adminModelEntity.getIdx();
        Integer deleteIdx = adminModelJpaRepository.deleteModelByEm(adminModelEntity.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(entityIdx);
    }

    @Test
    @DisplayName("모델 삭제 Mockito 테스트")
    void 모델삭제Mockito테스트() {
        // given
        em.persist(adminModelEntity);
        adminModelDTO = ModelMapper.INSTANCE.toDto(adminModelEntity);

        // when
        when(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        Integer deleteIdx = adminModelJpaRepository.deleteModelByEm(adminModelEntity.getIdx());

        // then
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminModelJpaRepository, times(1)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaRepository);
        inOrder.verify(mockAdminModelJpaRepository).findOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("모델 삭제 BDD 테스트")
    void 모델삭제BDD테스트() {
        // given
        em.persist(adminModelEntity);
        adminModelDTO = ModelMapper.INSTANCE.toDto(adminModelEntity);

        // when
        given(mockAdminModelJpaRepository.findOneModel(adminModelEntity)).willReturn(adminModelDTO);
        Integer deleteIdx = adminModelJpaRepository.deleteModelByEm(adminModelEntity.getIdx());

        // then
        assertThat(mockAdminModelJpaRepository.findOneModel(adminModelEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminModelJpaRepository).should(times(1)).findOneModel(adminModelEntity);
        then(mockAdminModelJpaRepository).should(atLeastOnce()).findOneModel(adminModelEntity);
        then(mockAdminModelJpaRepository).shouldHaveNoMoreInteractions();
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

        CommonImageDTO commonImageDTO = adminModelJpaRepository.insertModelImage(commonImageEntity);

        assertNotNull(commonImageDTO);
    }

    @Test
    @DisplayName("모델 소속사 수정 Mockito 테스트")
    void 모델소속사수정Mockito테스트() {
        // 소속사 등록
        em.persist(adminAgencyEntity);
        Integer agencyIdx = adminAgencyEntity.getIdx();

        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .favoriteCount(1)
                .viewCount(1)
                .agencyIdx(adminAgencyEntity.getIdx())
                .adminAgencyEntity(adminAgencyEntity)
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        // 모델 소속사 수정
        Integer idx = adminModelJpaRepository.insertModel(adminModelEntity).getIdx();

        AdminModelEntity newAdminModelEntity = AdminModelEntity.builder()
                .idx(idx)
                .categoryCd(2)
                .categoryAge(3)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .agencyIdx(agencyIdx)
                .adminAgencyEntity(adminAgencyEntity)
                .status("active")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        adminModelJpaRepository.updateModelAgency(newAdminModelEntity);
        adminModelDTO = ModelMapper.INSTANCE.toDto(newAdminModelEntity);

        // when
        when(mockAdminModelJpaRepository.findOneModel(newAdminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaRepository.findOneModel(newAdminModelEntity);

        // then
        assertThat(modelInfo.getAgencyIdx()).isEqualTo(adminModelEntity.getAgencyIdx());
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo(adminModelEntity.getAdminAgencyEntity().getAgencyName());
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo(adminModelEntity.getAdminAgencyEntity().getAgencyDescription());

        // verify
        verify(mockAdminModelJpaRepository, times(1)).findOneModel(newAdminModelEntity);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findOneModel(newAdminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaRepository);
        inOrder.verify(mockAdminModelJpaRepository).findOneModel(newAdminModelEntity);
    }

    @Test
    @DisplayName("모델 소속사 수정 BDD 테스트")
    void 모델소속사수정BDD테스트() {
        // 소속사 등록
        em.persist(adminAgencyEntity);
        Integer agencyIdx = adminAgencyEntity.getIdx();

        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .favoriteCount(1)
                .viewCount(1)
                .agencyIdx(adminAgencyEntity.getIdx())
                .adminAgencyEntity(adminAgencyEntity)
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        // 모델 소속사 수정
        Integer idx = adminModelJpaRepository.insertModel(adminModelEntity).getIdx();

        AdminModelEntity newAdminModelEntity = AdminModelEntity.builder()
                .idx(idx)
                .categoryCd(2)
                .categoryAge(3)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .agencyIdx(agencyIdx)
                .adminAgencyEntity(adminAgencyEntity)
                .status("active")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        adminModelJpaRepository.updateModelAgency(newAdminModelEntity);
        adminModelDTO = ModelMapper.INSTANCE.toDto(newAdminModelEntity);

        // when
        given(mockAdminModelJpaRepository.findOneModel(newAdminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaRepository.findOneModel(newAdminModelEntity);

        // then
        assertThat(modelInfo.getAgencyIdx()).isEqualTo(adminModelEntity.getAgencyIdx());
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo(adminModelEntity.getAdminAgencyEntity().getAgencyName());
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo(adminModelEntity.getAdminAgencyEntity().getAgencyDescription());

        // verify
        then(mockAdminModelJpaRepository).should(times(1)).findOneModel(newAdminModelEntity);
        then(mockAdminModelJpaRepository).should(atLeastOnce()).findOneModel(newAdminModelEntity);
        then(mockAdminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 어드민 코멘트 조회 Mockito 테스트")
    void 모델어드민코멘트조회Mockito테스트() {
        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .favoriteCount(1)
                .viewCount(1)
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        Integer modelIdx = adminModelJpaRepository.insertModel(adminModelEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("model")
                .commentTypeIdx(modelIdx)
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("model")
                .commentTypeIdx(modelIdx)
                .visible("Y")
                .build());

        when(mockAdminModelJpaRepository.findModelAdminComment(adminModelEntity)).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminModelJpaRepository.findModelAdminComment(adminModelEntity);

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("model");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        verify(mockAdminModelJpaRepository, times(1)).findModelAdminComment(adminModelEntity);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findModelAdminComment(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaRepository);
        inOrder.verify(mockAdminModelJpaRepository).findModelAdminComment(adminModelEntity);
    }

    @Test
    @DisplayName("모델 어드민 코멘트 조회 BDD 테스트")
    void 모델어드민코멘트조회BDD테스트() {
        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .favoriteCount(1)
                .viewCount(1)
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        Integer modelIdx = adminModelJpaRepository.insertModel(adminModelEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("model")
                .commentTypeIdx(modelIdx)
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("model")
                .commentTypeIdx(modelIdx)
                .visible("Y")
                .build());

        given(mockAdminModelJpaRepository.findModelAdminComment(adminModelEntity)).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminModelJpaRepository.findModelAdminComment(adminModelEntity);

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("model");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        then(mockAdminModelJpaRepository).should(times(1)).findModelAdminComment(adminModelEntity);
        then(mockAdminModelJpaRepository).should(atLeastOnce()).findModelAdminComment(adminModelEntity);
        then(mockAdminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("새로운 모델 Mockito 조회 테스트")
    void 새로운모델Mockito조회테스트() {
        // given
        Map<String, Object> newModelMap = new HashMap<>();
        newModelMap.put("categoryCd", 1);
        newModelMap.put("jpaStartPage", 1);
        newModelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> modelList = new ArrayList<>();
        modelList.add(AdminModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희").newYn("Y")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());

        // when
        when(mockAdminModelJpaRepository.findNewModelsList(newModelMap)).thenReturn(modelList);
        List<AdminModelDTO> newModelList = mockAdminModelJpaRepository.findNewModelsList(newModelMap);

        // then
        assertThat(newModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(newModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(newModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(newModelList.get(0).getNewYn()).isEqualTo(modelList.get(0).getNewYn());
        assertThat(newModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());

        // verify
        verify(mockAdminModelJpaRepository, times(1)).findNewModelsList(newModelMap);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findNewModelsList(newModelMap);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaRepository);
        inOrder.verify(mockAdminModelJpaRepository).findNewModelsList(newModelMap);
    }

    @Test
    @DisplayName("새로운 모델 리스트 조회 BDD 테스트")
    void 새로운모델리스트조회BDD테스트() {
        // given
        Map<String, Object> newModelMap = new HashMap<>();
        newModelMap.put("categoryCd", 1);
        newModelMap.put("jpaStartPage", 1);
        newModelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> modelList = new ArrayList<>();
        modelList.add(AdminModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희").newYn("Y").modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());

        // when
        given(mockAdminModelJpaRepository.findNewModelsList(newModelMap)).willReturn(modelList);
        List<AdminModelDTO> newModelList = mockAdminModelJpaRepository.findNewModelsList(newModelMap);

        // then
        assertThat(newModelList.get(0).getIdx()).isEqualTo(modelList.get(0).getIdx());
        assertThat(newModelList.get(0).getCategoryCd()).isEqualTo(modelList.get(0).getCategoryCd());
        assertThat(newModelList.get(0).getModelKorName()).isEqualTo(modelList.get(0).getModelKorName());
        assertThat(newModelList.get(0).getNewYn()).isEqualTo(modelList.get(0).getNewYn());
        assertThat(newModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(modelList.get(0).getModelAgency().getAgencyName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(modelList.get(0).getModelAgency().getAgencyDescription());

        // verify
        then(mockAdminModelJpaRepository).should(times(1)).findNewModelsList(newModelMap);
        then(mockAdminModelJpaRepository).should(atLeastOnce()).findNewModelsList(newModelMap);
        then(mockAdminModelJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().idx(1).modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        // when
        when(mockAdminModelJpaRepository.findOneModelSchedule(adminModelEntity)).thenReturn(scheduleList);
        List<AdminScheduleDTO> newScheduleList = mockAdminModelJpaRepository.findOneModelSchedule(adminModelEntity);

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        verify(mockAdminModelJpaRepository, times(1)).findOneModelSchedule(adminModelEntity);
        verify(mockAdminModelJpaRepository, atLeastOnce()).findOneModelSchedule(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaRepository);

        InOrder inOrder = inOrder(mockAdminModelJpaRepository);
        inOrder.verify(mockAdminModelJpaRepository).findOneModelSchedule(adminModelEntity);
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().idx(1).modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        // when
        when(mockAdminModelJpaRepository.findOneModelSchedule(adminModelEntity)).thenReturn(scheduleList);
        List<AdminScheduleDTO> newScheduleList = mockAdminModelJpaRepository.findOneModelSchedule(adminModelEntity);

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        then(mockAdminModelJpaRepository).should(times(1)).findOneModelSchedule(adminModelEntity);
        then(mockAdminModelJpaRepository).should(atLeastOnce()).findOneModelSchedule(adminModelEntity);
        then(mockAdminModelJpaRepository).shouldHaveNoMoreInteractions();
    }
}