package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity.builder;
import static com.tsp.new_tsp_admin.api.model.mapper.ModelMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private AdminModelEntity adminModelEntity;
    private AdminModelDTO adminModelDTO;

    public void createModel() {
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
                .height("170")
                .size3("34-24-34")
                .shoes("270")
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
    @DisplayName("모델 리스트 조회 테스트")
    void 모델리스트조회테스트() throws Exception {
        // given
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("categoryCd", 1);
        modelMap.put("jpaStartPage", 0);
        modelMap.put("size", 100);

        assertThat(adminModelJpaService.findModelsList(modelMap)).isNotEmpty();
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
    @DisplayName("모델 상세 조회 테스트")
    void 모델상세조회테스트() throws Exception {
        // given
        adminModelJpaService.findOneModel(builder().idx(143).categoryCd(2).build());
    }

    @Test
    @DisplayName("모델 상세 조회 예외 테스트")
    void 모델상세조회예외테스트() {
        // given
        adminModelEntity = builder().categoryCd(-1).build();

        // then
        assertThatThrownBy(() -> adminModelJpaService.findOneModel(adminModelEntity))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("모델 등록 테스트")
    void 모델등록테스트() throws Exception {
        // given
        adminModelJpaService.insertModel(adminModelEntity);

        // when
        when(mockAdminModelJpaService.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);

        // then
        assertThat(mockAdminModelJpaService.findOneModel(adminModelEntity).getCategoryCd()).isEqualTo(adminModelDTO.getCategoryCd());
        assertThat(mockAdminModelJpaService.findOneModel(adminModelEntity).getModelKorFirstName()).isEqualTo(adminModelDTO.getModelKorFirstName());
        assertThat(mockAdminModelJpaService.findOneModel(adminModelEntity).getModelKorSecondName()).isEqualTo(adminModelDTO.getModelKorSecondName());

        // verify
        verify(mockAdminModelJpaService, times(3)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);
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
        assertThatThrownBy(() -> adminModelJpaService.insertModel(adminModelEntity))
                .isInstanceOf(TspException.class);
    }

    @Test
    @DisplayName("모델 수정 테스트")
    void 모델수정테스트() throws Exception {
        Integer idx = adminModelJpaService.insertModel(adminModelEntity).getIdx();

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

        adminModelJpaService.updateModel(adminModelEntity);

        adminModelDTO = INSTANCE.toDto(adminModelEntity);

        // when
        when(mockAdminModelJpaService.findOneModel(adminModelEntity)).thenReturn(adminModelDTO);

        // then
        assertThat(mockAdminModelJpaService.findOneModel(adminModelEntity).getModelKorFirstName()).isEqualTo("조");
        assertThat(mockAdminModelJpaService.findOneModel(adminModelEntity).getModelKorSecondName()).isEqualTo("찬희");

        // verify
        verify(mockAdminModelJpaService, times(2)).findOneModel(adminModelEntity);
        verify(mockAdminModelJpaService, atLeastOnce()).findOneModel(adminModelEntity);
        verifyNoMoreInteractions(mockAdminModelJpaService);
    }

    @Test
    @DisplayName("모델 삭제 테스트")
    void 모델삭제테스트() throws Exception {
        Integer idx = adminModelJpaService.insertModel(adminModelEntity).getIdx();

        assertThat(adminModelJpaService.deleteModel(idx)).isNotNull();
    }
}