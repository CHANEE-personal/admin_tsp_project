package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.model.mapper.ModelImageMapper;
import com.tsp.new_tsp_admin.api.model.mapper.ModelMapper;
import com.tsp.new_tsp_admin.api.model.mapper.agency.AgencyMapper;
import com.tsp.new_tsp_admin.api.model.service.agency.AdminAgencyJpaService;
import com.tsp.new_tsp_admin.exception.TspException;
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

import java.util.*;

import static com.tsp.new_tsp_admin.api.model.mapper.ModelMapper.INSTANCE;
import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
@DisplayName("모델 Service Test")
class AdminModelJpaServiceTest {
    @Mock private AdminModelJpaService mockAdminModelJpaService;
    private final AdminModelJpaService adminModelJpaService;
    private final AdminAgencyJpaService adminAgencyJpaService;
    private AdminModelEntity adminModelEntity;
    private AdminModelDTO adminModelDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;
    private AdminAgencyEntity adminAgencyEntity;
    private AdminAgencyDTO adminAgencyDTO;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    public void createModel() {
        adminAgencyEntity = AdminAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        adminAgencyDTO = AgencyMapper.INSTANCE.toDto(adminAgencyEntity);

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
                .favoriteCount(1)
                .viewCount(1)
                .adminAgencyEntity(adminAgencyEntity)
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        adminModelDTO = INSTANCE.toDto(adminModelEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createModel();
    }

    @Test
    @DisplayName("모델 리스트 조회 예외 테스트")
    void 모델리스트조회예외테스트() {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", -1);

        // then
        assertThatThrownBy(() -> adminModelJpaService.findModelsList(modelMap))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("모델 리스트 조회 테스트")
    void 모델리스트조회테스트() throws Exception {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("jpaStartPage", 0);
        modelMap.put("size", 100);

        // then
        assertThat(adminModelJpaService.findModelsList(modelMap)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 리스트 조회 Mockito 테스트")
    void 모델리스트조회Mockito테스트() throws Exception {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> returnModelList = new ArrayList<>();
        // 남성
        returnModelList.add(AdminModelDTO.builder().idx(1).categoryCd(1).modelKorName("남성모델").modelEngName("menModel")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        // 여성
        returnModelList.add(AdminModelDTO.builder().idx(2).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        // 시니어
        returnModelList.add(AdminModelDTO.builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());

        // when
        when(mockAdminModelJpaService.findModelsList(modelMap)).thenReturn(returnModelList);
        List<AdminModelDTO> modelList = mockAdminModelJpaService.findModelsList(modelMap);

        // then
        assertAll(
                () -> assertThat(modelList).isNotEmpty(),
                () -> assertThat(modelList).hasSize(3)
        );

        assertThat(modelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(modelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(modelList.get(0).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyName());
        assertThat(modelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyDescription());

        assertThat(modelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(modelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(modelList.get(1).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyName());
        assertThat(modelList.get(1).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyDescription());

        assertThat(modelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(modelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(modelList.get(2).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyName());
        assertThat(modelList.get(2).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyDescription());

        // verify
        verify(mockAdminModelJpaService, times(1)).findModelsList(modelMap);
        verify(mockAdminModelJpaService, atLeastOnce()).findModelsList(modelMap);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findModelsList(modelMap);
    }

    @Test
    @DisplayName("모델 리스트 조회 BDD 테스트")
    void 모델리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(AdminModelDTO.builder().idx(1).categoryCd(1).modelKorName("남성모델").modelEngName("menModel")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        // 여성
        returnModelList.add(AdminModelDTO.builder().idx(2).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        // 시니어
        returnModelList.add(AdminModelDTO.builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());

        // when
        given(mockAdminModelJpaService.findModelsList(modelMap)).willReturn(returnModelList);
        List<AdminModelDTO> modelList = mockAdminModelJpaService.findModelsList(modelMap);

        // then
        assertAll(
                () -> assertThat(modelList).isNotEmpty(),
                () -> assertThat(modelList).hasSize(3)
        );

        assertThat(modelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(modelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(modelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(modelList.get(0).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyName());
        assertThat(modelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyDescription());

        assertThat(modelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(modelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(modelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(modelList.get(1).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyName());
        assertThat(modelList.get(1).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyDescription());

        assertThat(modelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(modelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(modelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(modelList.get(2).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyName());
        assertThat(modelList.get(2).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyDescription());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findModelsList(modelMap);
        then(mockAdminModelJpaService).should(atLeastOnce()).findModelsList(modelMap);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 상세 조회 예외 테스트")
    void 모델상세조회예외테스트() {
        // given
        adminModelEntity = AdminModelEntity.builder().categoryCd(-1).build();

        // then
        assertThatThrownBy(() -> adminModelJpaService.findOneModel(adminModelEntity))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("모델 상세 조회 테스트")
    void 모델상세조회테스트() throws Exception {
        AdminModelDTO modelInfo = adminModelJpaService.findOneModel(AdminModelEntity.builder().idx(143).categoryCd(2).build());
        // then
        assertThat(modelInfo).isNotNull();
        assertThat(modelInfo.getIdx()).isEqualTo(143);
        assertThat(modelInfo.getCategoryCd()).isEqualTo(2);
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo("김");
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo("예영");
    }

    @Test
    @DisplayName("모델 상세 조회 Mockito 테스트")
    void 모델상세조회Mockito테스트() throws Exception {
        // when
        when(mockAdminModelJpaService.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(adminModelDTO.getIdx());
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(adminModelDTO.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(adminModelDTO.getModelEngName());
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo(adminModelDTO.getModelAgency().getAgencyName());
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo(adminModelDTO.getModelAgency().getAgencyDescription());

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("모델 상세 조회 BDD 테스트")
    void 모델상세조회BDD테스트() throws Exception {
        // when
        given(mockAdminModelJpaService.findOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getIdx()).isEqualTo(adminModelDTO.getIdx());
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorName()).isEqualTo(adminModelDTO.getModelKorName());
        assertThat(modelInfo.getModelEngName()).isEqualTo(adminModelDTO.getModelEngName());
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo(adminModelDTO.getModelAgency().getAgencyName());
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo(adminModelDTO.getModelAgency().getAgencyDescription());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModel(adminModelEntity);
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModel(adminModelEntity);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 모델 상세 조회 테스트")
    void 이전or다음모델상세조회테스트() throws Exception {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145).categoryCd(2).build();

        // when
        adminModelDTO = adminModelJpaService.findOneModel(adminModelEntity);

        // 이전 모델
        assertThat(adminModelJpaService.findPrevOneModel(adminModelEntity).getIdx()).isEqualTo(144);
        // 다음 모델
        assertThat(adminModelJpaService.findNextOneModel(adminModelEntity).getIdx()).isEqualTo(147);
    }

    @Test
    @DisplayName("이전 모델 상세 조회 Mockito 테스트")
    void 이전모델상세조회Mockito테스트() throws Exception {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145).categoryCd(2).build();
        // when
        adminModelDTO = adminModelJpaService.findPrevOneModel(adminModelEntity);

        when(mockAdminModelJpaService.findPrevOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findPrevOneModel(adminModelEntity);

        assertThat(modelInfo.getIdx()).isEqualTo(144);
        // verify
        verify(mockAdminModelJpaService, times(1)).findPrevOneModel(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findPrevOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findPrevOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("이전 모델 상세 조회 BDD 테스트")
    void 이전모델상세조회BDD테스트() throws Exception {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145).categoryCd(2).build();
        // when
        adminModelDTO = adminModelJpaService.findPrevOneModel(adminModelEntity);

        given(mockAdminModelJpaService.findPrevOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findPrevOneModel(adminModelEntity);

        assertThat(modelInfo.getIdx()).isEqualTo(144);

        // verify
        then(mockAdminModelJpaService).should(times(1)).findPrevOneModel(adminModelEntity);
        then(mockAdminModelJpaService).should(atLeastOnce()).findPrevOneModel(adminModelEntity);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 모델 상세 조회 Mockito 테스트")
    void 다음모델상세조회Mockito테스트() throws Exception {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145).categoryCd(2).build();
        // when
        adminModelDTO = adminModelJpaService.findNextOneModel(adminModelEntity);

        when(mockAdminModelJpaService.findNextOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findNextOneModel(adminModelEntity);

        assertThat(modelInfo.getIdx()).isEqualTo(147);
        // verify
        verify(mockAdminModelJpaService, times(1)).findNextOneModel(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findNextOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findNextOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("다음 모델 상세 조회 BDD 테스트")
    void 다음모델상세조회BDD테스트() throws Exception {
        // given
        adminModelEntity = AdminModelEntity.builder().idx(145).categoryCd(2).build();
        // when
        adminModelDTO = adminModelJpaService.findNextOneModel(adminModelEntity);

        given(mockAdminModelJpaService.findNextOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findNextOneModel(adminModelEntity);

        assertThat(modelInfo.getIdx()).isEqualTo(147);

        // verify
        then(mockAdminModelJpaService).should(times(1)).findNextOneModel(adminModelEntity);
        then(mockAdminModelJpaService).should(atLeastOnce()).findNextOneModel(adminModelEntity);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 등록 Mockito 테스트")
    void 모델등록Mockito테스트() throws Exception {
        // given
        adminModelJpaService.insertModel(adminModelEntity);

        // when
        when(mockAdminModelJpaService.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo(adminModelDTO.getModelKorFirstName());
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo(adminModelDTO.getModelKorSecondName());
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo(adminModelDTO.getModelAgency().getAgencyName());
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo(adminModelDTO.getModelAgency().getAgencyDescription());

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("모델 등록 BDD 테스트")
    void 모델등록BDD테스트() throws Exception {
        // given
        adminModelJpaService.insertModel(adminModelEntity);

        // when
        given(mockAdminModelJpaService.findOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getCategoryCd()).isEqualTo(adminModelDTO.getCategoryCd());
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo(adminModelDTO.getModelKorFirstName());
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo(adminModelDTO.getModelKorSecondName());
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo(adminModelDTO.getModelAgency().getAgencyName());
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo(adminModelDTO.getModelAgency().getAgencyDescription());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModel(adminModelEntity);
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModel(adminModelEntity);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 등록 예외 테스트")
    void 모델등록예외테스트() {
        // given
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
                .shoes(270)
                .visible("Y")
                .build();

        // then
        assertThatThrownBy(() -> adminModelJpaService.insertModel(adminModelEntity))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("모델 수정 Mockito 테스트")
    void 모델수정Mockito테스트() throws Exception {
        // given
        Integer idx = adminModelJpaService.insertModel(adminModelEntity).getIdx();

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

        adminModelJpaService.updateModel(adminModelEntity);

        adminModelDTO = INSTANCE.toDto(adminModelEntity);

        // when
        when(mockAdminModelJpaService.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo("조");
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo("찬희");

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("모델 수정 BDD 테스트")
    void 모델수정BDD테스트() throws Exception {
        // given
        Integer idx = adminModelJpaService.insertModel(adminModelEntity).getIdx();

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

        adminModelJpaService.updateModel(adminModelEntity);

        adminModelDTO = INSTANCE.toDto(adminModelEntity);

        // when
        given(mockAdminModelJpaService.findOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelEntity);

        // then
        assertThat(modelInfo.getModelKorFirstName()).isEqualTo("조");
        assertThat(modelInfo.getModelKorSecondName()).isEqualTo("찬희");

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModel(adminModelEntity);
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModel(adminModelEntity);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 삭제 테스트")
    void 모델삭제테스트() throws Exception {
        // given
        Integer idx = adminModelJpaService.insertModel(adminModelEntity).getIdx();

        // then
        assertThat(adminModelJpaService.deleteModel(idx)).isNotNull();
    }

    @Test
    @DisplayName("모델 삭제 Mockito 테스트")
    void 모델삭제Mockito테스트() throws Exception {
        // given
        adminModelJpaService.insertModel(adminModelEntity);
        adminModelDTO = INSTANCE.toDto(adminModelEntity);

        // when
        when(mockAdminModelJpaService.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        Integer deleteIdx = adminModelJpaService.deleteModel(adminModelEntity.getIdx());

        // then
        assertThat(mockAdminModelJpaService.findOneModel(adminModelEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("모델 삭제 BDD 테스트")
    void 모델삭제BDD테스트() throws Exception {
        // given
        adminModelJpaService.insertModel(adminModelEntity);
        adminModelDTO = INSTANCE.toDto(adminModelEntity);

        // when
        given(mockAdminModelJpaService.findOneModel(adminModelEntity)).willReturn(adminModelDTO);
        Integer deleteIdx = adminModelJpaService.deleteModel(adminModelEntity.getIdx());

        // then
        assertThat(mockAdminModelJpaService.findOneModel(adminModelEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModel(adminModelEntity);
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModel(adminModelEntity);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 소속사 수정 Mockito 테스트")
    void 모델소속사수정Mockito테스트() throws Exception {
        // 소속사 등록
        AdminAgencyDTO newAgencyDTO = adminAgencyJpaService.insertAgency(adminAgencyEntity);
        Integer agencyIdx = newAgencyDTO.getIdx();

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
        Integer idx = adminModelJpaService.insertModel(adminModelEntity).getIdx();

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

        adminModelJpaService.updateModelAgency(newAdminModelEntity);
        adminModelDTO = INSTANCE.toDto(newAdminModelEntity);

        // when
        when(mockAdminModelJpaService.findOneModel(newAdminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(newAdminModelEntity);

        // then
        assertThat(modelInfo.getAgencyIdx()).isEqualTo(adminModelEntity.getAgencyIdx());
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo(adminModelEntity.getAdminAgencyEntity().getAgencyName());
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo(adminModelEntity.getAdminAgencyEntity().getAgencyDescription());

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModel(newAdminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(newAdminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModel(newAdminModelEntity);
    }

    @Test
    @DisplayName("모델 소속사 수정 BDD 테스트")
    void 모델소속사수정BDD테스트() throws Exception {
        // 소속사 등록
        AdminAgencyDTO newAgencyDTO = adminAgencyJpaService.insertAgency(adminAgencyEntity);
        Integer agencyIdx = newAgencyDTO.getIdx();

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
        Integer idx = adminModelJpaService.insertModel(adminModelEntity).getIdx();

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

        adminModelJpaService.updateModelAgency(newAdminModelEntity);
        adminModelDTO = INSTANCE.toDto(newAdminModelEntity);

        // when
        given(mockAdminModelJpaService.findOneModel(newAdminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(newAdminModelEntity);

        // then
        assertThat(modelInfo.getAgencyIdx()).isEqualTo(adminModelEntity.getAgencyIdx());
        assertThat(modelInfo.getModelAgency().getAgencyName()).isEqualTo(adminModelEntity.getAdminAgencyEntity().getAgencyName());
        assertThat(modelInfo.getModelAgency().getAgencyDescription()).isEqualTo(adminModelEntity.getAdminAgencyEntity().getAgencyDescription());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModel(newAdminModelEntity);
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModel(newAdminModelEntity);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 어드민 코멘트 조회 Mockito 테스트")
    void 모델어드민코멘트조회Mockito테스트() throws Exception {
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

        Integer modelIdx = adminModelJpaService.insertModel(adminModelEntity).getIdx();

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

        when(mockAdminModelJpaService.findModelAdminComment(adminModelEntity)).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminModelJpaService.findModelAdminComment(adminModelEntity);

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("model");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        verify(mockAdminModelJpaService, times(1)).findModelAdminComment(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findModelAdminComment(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findModelAdminComment(adminModelEntity);
    }

    @Test
    @DisplayName("모델 어드민 코멘트 조회 BDD 테스트")
    void 모델어드민코멘트조회BDD테스트() throws Exception {
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

        Integer modelIdx = adminModelJpaService.insertModel(adminModelEntity).getIdx();

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

        given(mockAdminModelJpaService.findModelAdminComment(adminModelEntity)).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminModelJpaService.findModelAdminComment(adminModelEntity);

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("model");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminModelEntity.getIdx());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findModelAdminComment(adminModelEntity);
        then(mockAdminModelJpaService).should(atLeastOnce()).findModelAdminComment(adminModelEntity);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("새로운 모델 리스트 조회 Mockito 테스트")
    void 새로운모델리스트조회Mockito테스트() throws Exception {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> returnModelList = new ArrayList<>();
        // 남성
        returnModelList.add(AdminModelDTO.builder().idx(1).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").newYn("Y")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        // 여성
        returnModelList.add(AdminModelDTO.builder().idx(2).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").newYn("Y")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        // 시니어
        returnModelList.add(AdminModelDTO.builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").newYn("Y")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());

        // when
        when(mockAdminModelJpaService.findNewModelsList(modelMap)).thenReturn(returnModelList);
        List<AdminModelDTO> newModelList = mockAdminModelJpaService.findNewModelsList(modelMap);

        // then
        assertAll(
                () -> assertThat(newModelList).isNotEmpty(),
                () -> assertThat(newModelList).hasSize(3)
        );

        assertThat(newModelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(newModelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(newModelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(newModelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(newModelList.get(0).getNewYn()).isEqualTo(returnModelList.get(0).getNewYn());
        assertThat(newModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyDescription());

        assertThat(newModelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(newModelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(newModelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(newModelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(newModelList.get(1).getNewYn()).isEqualTo(returnModelList.get(1).getNewYn());
        assertThat(newModelList.get(1).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyName());
        assertThat(newModelList.get(1).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyDescription());

        assertThat(newModelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(newModelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(newModelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(newModelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(newModelList.get(2).getNewYn()).isEqualTo(returnModelList.get(2).getNewYn());
        assertThat(newModelList.get(2).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyName());
        assertThat(newModelList.get(2).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyDescription());

        // verify
        verify(mockAdminModelJpaService, times(1)).findNewModelsList(modelMap);
        verify(mockAdminModelJpaService, atLeastOnce()).findNewModelsList(modelMap);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findNewModelsList(modelMap);
    }

    @Test
    @DisplayName("새로운 모델 리스트 조회 BDD 테스트")
    void 새로운모델리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", "1");
        modelMap.put("jpaStartPage", 1);
        modelMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminModelDTO> returnModelList = new ArrayList<>();

        // 남성
        returnModelList.add(AdminModelDTO.builder().idx(1).categoryCd(1).modelKorName("남성모델").modelEngName("menModel").newYn("Y")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        // 여성
        returnModelList.add(AdminModelDTO.builder().idx(2).categoryCd(2).modelKorName("여성모델").modelEngName("womenModel").newYn("Y")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());
        // 시니어
        returnModelList.add(AdminModelDTO.builder().idx(3).categoryCd(3).modelKorName("시니어모델").modelEngName("seniorModel").newYn("Y")
                .modelImage(commonImageDtoList).modelAgency(adminAgencyDTO).build());

        // when
        given(mockAdminModelJpaService.findNewModelsList(modelMap)).willReturn(returnModelList);
        List<AdminModelDTO> newModelList = mockAdminModelJpaService.findNewModelsList(modelMap);

        // then
        assertAll(
                () -> assertThat(newModelList).isNotEmpty(),
                () -> assertThat(newModelList).hasSize(3)
        );

        assertThat(newModelList.get(0).getIdx()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(newModelList.get(0).getCategoryCd()).isEqualTo(returnModelList.get(0).getIdx());
        assertThat(newModelList.get(0).getModelKorName()).isEqualTo(returnModelList.get(0).getModelKorName());
        assertThat(newModelList.get(0).getModelEngName()).isEqualTo(returnModelList.get(0).getModelEngName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyName());
        assertThat(newModelList.get(0).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(0).getModelAgency().getAgencyDescription());

        assertThat(newModelList.get(1).getIdx()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(newModelList.get(1).getCategoryCd()).isEqualTo(returnModelList.get(1).getIdx());
        assertThat(newModelList.get(1).getModelKorName()).isEqualTo(returnModelList.get(1).getModelKorName());
        assertThat(newModelList.get(1).getModelEngName()).isEqualTo(returnModelList.get(1).getModelEngName());
        assertThat(newModelList.get(1).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyName());
        assertThat(newModelList.get(1).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(1).getModelAgency().getAgencyDescription());

        assertThat(newModelList.get(2).getIdx()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(newModelList.get(2).getCategoryCd()).isEqualTo(returnModelList.get(2).getIdx());
        assertThat(newModelList.get(2).getModelKorName()).isEqualTo(returnModelList.get(2).getModelKorName());
        assertThat(newModelList.get(2).getModelEngName()).isEqualTo(returnModelList.get(2).getModelEngName());
        assertThat(newModelList.get(2).getModelAgency().getAgencyName()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyName());
        assertThat(newModelList.get(2).getModelAgency().getAgencyDescription()).isEqualTo(returnModelList.get(2).getModelAgency().getAgencyDescription());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findNewModelsList(modelMap);
        then(mockAdminModelJpaService).should(atLeastOnce()).findNewModelsList(modelMap);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("새로운 모델 설정 Mockito 테스트")
    void 새로운모델설정Mockito테스트() throws Exception {
        adminModelEntity = AdminModelEntity.builder()
                .idx(1)
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

        adminModelJpaService.toggleModelNewYn(adminModelEntity);
        adminModelDTO = ModelMapper.INSTANCE.toDto(adminModelEntity);

        // when
        when(mockAdminModelJpaService.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelEntity);

        assertThat(modelInfo.getNewYn()).isEqualTo("N");

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModel(adminModelEntity);
    }

    @Test
    @DisplayName("새로운 모델 설정 BDD 테스트")
    void 새로운모델설정BDD테스트() throws Exception {
        adminModelEntity = AdminModelEntity.builder()
                .idx(1)
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

        adminModelJpaService.toggleModelNewYn(adminModelEntity);
        adminModelDTO = ModelMapper.INSTANCE.toDto(adminModelEntity);

        // when
        given(mockAdminModelJpaService.findOneModel(adminModelEntity)).willReturn(adminModelDTO);
        AdminModelDTO modelInfo = mockAdminModelJpaService.findOneModel(adminModelEntity);

        assertThat(modelInfo.getNewYn()).isEqualTo("N");

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModel(adminModelEntity);
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModel(adminModelEntity);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트() throws Exception {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().idx(1).modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        // when
        when(mockAdminModelJpaService.findOneModelSchedule(adminModelEntity)).thenReturn(scheduleList);
        List<AdminScheduleDTO> newScheduleList = mockAdminModelJpaService.findOneModelSchedule(adminModelEntity);

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        verify(mockAdminModelJpaService, times(1)).findOneModelSchedule(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModelSchedule(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);

        InOrder inOrder = inOrder(mockAdminModelJpaService);
        inOrder.verify(mockAdminModelJpaService).findOneModelSchedule(adminModelEntity);
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트() throws Exception {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().idx(1).modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());

        // when
        given(mockAdminModelJpaService.findOneModelSchedule(adminModelEntity)).willReturn(scheduleList);
        List<AdminScheduleDTO> newScheduleList = mockAdminModelJpaService.findOneModelSchedule(adminModelEntity);

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        then(mockAdminModelJpaService).should(times(1)).findOneModelSchedule(adminModelEntity);
        then(mockAdminModelJpaService).should(atLeastOnce()).findOneModelSchedule(adminModelEntity);
        then(mockAdminModelJpaService).shouldHaveNoMoreInteractions();
    }
}