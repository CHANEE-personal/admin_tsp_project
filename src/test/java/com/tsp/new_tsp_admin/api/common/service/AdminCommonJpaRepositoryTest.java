package com.tsp.new_tsp_admin.api.common.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("공통코드 Repository Test")
class AdminCommonJpaRepositoryTest {
    @Mock
    private AdminCommonJpaRepository mockAdminCommonJpaRepository;
    private final AdminCommonJpaRepository adminCommonJpaRepository;
    private final AdminUserJpaRepository adminUserJpaRepository;
    private final EntityManager em;

    private CommonCodeEntity commonCodeEntity;
    private CommonCodeDTO commonCodeDTO;

    public void createModelAndImage() {
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin02")
                .password("pass1234")
                .name("test")
                .visible("Y")
                .build();

        adminUserJpaRepository.adminLogin(adminUserEntity);

        commonCodeEntity = CommonCodeEntity.builder()
                .categoryCd(1)
                .categoryNm("공통코드")
                .cmmType("common")
                .visible("Y")
                .build();

        commonCodeDTO = CommonCodeEntity.toDto(commonCodeEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createModelAndImage();
    }

    @Test
    @DisplayName("공통코드 리스트 조회 테스트")
    void 공통코드리스트조회테스트() {
        // given
        Map<String, Object> commonMap = new HashMap<>();
        commonMap.put("jpaStartPage", 0);
        commonMap.put("size", 100);

        // then
        assertThat(adminCommonJpaRepository.findCommonCodeList(commonMap)).isNotEmpty();
    }

    @Test
    @DisplayName("공통코드 리스트 조회 Mockito 테스트")
    void 공통코드리스트조회Mockito테스트() {
        Map<String, Object> commonMap = new HashMap<>();
        commonMap.put("jpaStartPage", 1);
        commonMap.put("size", 3);

        List<CommonCodeDTO> commonCodeList = new ArrayList<>();
        commonCodeList.add(CommonCodeDTO.builder().idx(1L).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build());

        // when
        when(mockAdminCommonJpaRepository.findCommonCodeList(commonMap)).thenReturn(commonCodeList);
        List<CommonCodeDTO> newCommonCodeList = mockAdminCommonJpaRepository.findCommonCodeList(commonMap);

        // then
        assertThat(newCommonCodeList.get(0).getIdx()).isEqualTo(commonCodeList.get(0).getIdx());
        assertThat(newCommonCodeList.get(0).getCategoryCd()).isEqualTo(commonCodeList.get(0).getCategoryCd());
        assertThat(newCommonCodeList.get(0).getCategoryNm()).isEqualTo(commonCodeList.get(0).getCategoryNm());
        assertThat(newCommonCodeList.get(0).getCmmType()).isEqualTo(commonCodeList.get(0).getCmmType());

        // verify
        verify(mockAdminCommonJpaRepository, times(1)).findCommonCodeList(commonMap);
        verify(mockAdminCommonJpaRepository, atLeastOnce()).findCommonCodeList(commonMap);
        verifyNoMoreInteractions(mockAdminCommonJpaRepository);

        InOrder inOrder = inOrder(mockAdminCommonJpaRepository);
        inOrder.verify(mockAdminCommonJpaRepository).findCommonCodeList(commonMap);
    }

    @Test
    @DisplayName("공통코드 리스트 조회 BDD 테스트")
    void 공통코드리스트조회BDD테스트() {
        Map<String, Object> commonMap = new HashMap<>();
        commonMap.put("jpaStartPage", 1);
        commonMap.put("size", 3);

        List<CommonCodeDTO> commonCodeList = new ArrayList<>();
        commonCodeList.add(CommonCodeDTO.builder().idx(1L).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build());

        // when
        given(mockAdminCommonJpaRepository.findCommonCodeList(commonMap)).willReturn(commonCodeList);
        List<CommonCodeDTO> newCommonCodeList = mockAdminCommonJpaRepository.findCommonCodeList(commonMap);

        // then
        assertThat(newCommonCodeList.get(0).getIdx()).isEqualTo(commonCodeList.get(0).getIdx());
        assertThat(newCommonCodeList.get(0).getCategoryCd()).isEqualTo(commonCodeList.get(0).getCategoryCd());
        assertThat(newCommonCodeList.get(0).getCategoryNm()).isEqualTo(commonCodeList.get(0).getCategoryNm());
        assertThat(newCommonCodeList.get(0).getCmmType()).isEqualTo(commonCodeList.get(0).getCmmType());

        // verify
        then(mockAdminCommonJpaRepository).should(times(1)).findCommonCodeList(commonMap);
        then(mockAdminCommonJpaRepository).should(atLeastOnce()).findCommonCodeList(commonMap);
        then(mockAdminCommonJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 상세 조회 테스트")
    void 공통코드상세조회테스트() {
        // given
        commonCodeEntity = CommonCodeEntity.builder().idx(1L).categoryCd(1).build();

        commonCodeDTO = adminCommonJpaRepository.findOneCommonCode(commonCodeEntity);

        assertAll(() -> {
                    assertThat(commonCodeDTO.getIdx()).isEqualTo(1);
                },
                () -> {
                    assertThat(commonCodeDTO.getCategoryCd()).isEqualTo(1);
                    assertNotNull(commonCodeDTO.getCategoryCd());
                },
                () -> {
                    assertThat(commonCodeDTO.getCategoryNm()).isEqualTo("men");
                    assertNotNull(commonCodeDTO.getCategoryNm());
                },
                () -> {
                    assertThat(commonCodeDTO.getCmmType()).isEqualTo("model");
                    assertNotNull(commonCodeDTO.getCmmType());
                });
    }

    @Test
    @DisplayName("공통코드 상세 조회 Mockito 테스트")
    void 공통코드상세조회Mockito테스트() {
        // given
        commonCodeEntity = CommonCodeEntity.builder().idx(1L).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build();

        commonCodeDTO = CommonCodeDTO.builder()
                .idx(1L)
                .categoryCd(1)
                .categoryNm("men")
                .cmmType("model")
                .visible("Y")
                .build();

        // when
        when(mockAdminCommonJpaRepository.findOneCommonCode(commonCodeEntity)).thenReturn(commonCodeDTO);
        CommonCodeDTO commonCodeInfo = mockAdminCommonJpaRepository.findOneCommonCode(commonCodeEntity);

        // then
        assertThat(commonCodeInfo.getIdx()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryCd()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        verify(mockAdminCommonJpaRepository, times(1)).findOneCommonCode(commonCodeEntity);
        verify(mockAdminCommonJpaRepository, atLeastOnce()).findOneCommonCode(commonCodeEntity);
        verifyNoMoreInteractions(mockAdminCommonJpaRepository);

        InOrder inOrder = inOrder(mockAdminCommonJpaRepository);
        inOrder.verify(mockAdminCommonJpaRepository).findOneCommonCode(commonCodeEntity);
    }

    @Test
    @DisplayName("공통코드 상세 조회 BDD 테스트")
    void 공통코드상세조회BDD테스트() {
        // given
        commonCodeEntity = CommonCodeEntity.builder().idx(1L).categoryCd(1).categoryNm("men").cmmType("model").visible("Y").build();

        commonCodeDTO = CommonCodeDTO.builder()
                .idx(1L)
                .categoryCd(1)
                .categoryNm("men")
                .cmmType("model")
                .visible("Y")
                .build();

        // when
        given(mockAdminCommonJpaRepository.findOneCommonCode(commonCodeEntity)).willReturn(commonCodeDTO);
        CommonCodeDTO commonCodeInfo = mockAdminCommonJpaRepository.findOneCommonCode(commonCodeEntity);

        // then
        assertThat(commonCodeInfo.getIdx()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryCd()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        then(mockAdminCommonJpaRepository).should(times(1)).findOneCommonCode(commonCodeEntity);
        then(mockAdminCommonJpaRepository).should(atLeastOnce()).findOneCommonCode(commonCodeEntity);
        then(mockAdminCommonJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 등록 Mockito 테스트")
    void 공통코드등록Mockito테스트() {
        // given
        adminCommonJpaRepository.insertCommonCode(commonCodeEntity);

        // when
        when(mockAdminCommonJpaRepository.insertCommonCode(commonCodeEntity)).thenReturn(commonCodeDTO);
        CommonCodeDTO commonInfo = mockAdminCommonJpaRepository.insertCommonCode(commonCodeEntity);

        // then
        assertThat(commonInfo.getCategoryCd()).isEqualTo(1);
        assertThat(commonInfo.getCategoryNm()).isEqualTo("공통코드");
        assertThat(commonInfo.getCmmType()).isEqualTo("common");

        // verify
        verify(mockAdminCommonJpaRepository, times(1)).insertCommonCode(commonCodeEntity);
        verify(mockAdminCommonJpaRepository, atLeastOnce()).insertCommonCode(commonCodeEntity);
        verifyNoMoreInteractions(mockAdminCommonJpaRepository);

        InOrder inOrder = inOrder(mockAdminCommonJpaRepository);
        inOrder.verify(mockAdminCommonJpaRepository).insertCommonCode(commonCodeEntity);
    }

    @Test
    @DisplayName("공통코드 등록 BDD 테스트")
    void 공통코드등록BDD테스트() {
        // given
        adminCommonJpaRepository.insertCommonCode(commonCodeEntity);

        // when
        given(mockAdminCommonJpaRepository.insertCommonCode(commonCodeEntity)).willReturn(commonCodeDTO);
        CommonCodeDTO commonInfo = mockAdminCommonJpaRepository.insertCommonCode(commonCodeEntity);

        // then
        assertThat(commonInfo.getCategoryCd()).isEqualTo(1);
        assertThat(commonInfo.getCategoryNm()).isEqualTo("공통코드");
        assertThat(commonInfo.getCmmType()).isEqualTo("common");

        // verify
        then(mockAdminCommonJpaRepository).should(times(1)).insertCommonCode(commonCodeEntity);
        then(mockAdminCommonJpaRepository).should(atLeastOnce()).insertCommonCode(commonCodeEntity);
        then(mockAdminCommonJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 수정 Mockito 테스트")
    void 공통코드수정Mockito테스트() {
        Long idx = adminCommonJpaRepository.insertCommonCode(commonCodeEntity).getIdx();

        commonCodeEntity = CommonCodeEntity.builder()
                .idx(idx)
                .categoryCd(1)
                .categoryNm("new men")
                .cmmType("model")
                .visible("Y").build();

        adminCommonJpaRepository.updateCommonCode(commonCodeEntity);

        commonCodeDTO = CommonCodeEntity.toDto(commonCodeEntity);

        // when
        when(mockAdminCommonJpaRepository.findOneCommonCode(commonCodeEntity)).thenReturn(commonCodeDTO);
        CommonCodeDTO commonCodeInfo = mockAdminCommonJpaRepository.findOneCommonCode(commonCodeEntity);

        // then
        assertThat(commonCodeInfo.getCategoryCd()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("new men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        verify(mockAdminCommonJpaRepository, times(1)).findOneCommonCode(commonCodeEntity);
        verify(mockAdminCommonJpaRepository, atLeastOnce()).findOneCommonCode(commonCodeEntity);
        verifyNoMoreInteractions(mockAdminCommonJpaRepository);

        InOrder inOrder = inOrder(mockAdminCommonJpaRepository);
        inOrder.verify(mockAdminCommonJpaRepository).findOneCommonCode(commonCodeEntity);
    }

    @Test
    @DisplayName("공통코드 수정 BDD 테스트")
    void 공통코드수정BDD테스트() {
        Long idx = adminCommonJpaRepository.insertCommonCode(commonCodeEntity).getIdx();

        commonCodeEntity = CommonCodeEntity.builder()
                .idx(idx)
                .categoryCd(1)
                .categoryNm("new men")
                .cmmType("model")
                .visible("Y").build();

        adminCommonJpaRepository.updateCommonCode(commonCodeEntity);

        commonCodeDTO = CommonCodeEntity.toDto(commonCodeEntity);

        // when
        given(mockAdminCommonJpaRepository.findOneCommonCode(commonCodeEntity)).willReturn(commonCodeDTO);
        CommonCodeDTO commonCodeInfo = mockAdminCommonJpaRepository.findOneCommonCode(commonCodeEntity);

        // then
        assertThat(commonCodeInfo.getCategoryCd()).isEqualTo(1);
        assertThat(commonCodeInfo.getCategoryNm()).isEqualTo("new men");
        assertThat(commonCodeInfo.getCmmType()).isEqualTo("model");

        // verify
        then(mockAdminCommonJpaRepository).should(times(1)).findOneCommonCode(commonCodeEntity);
        then(mockAdminCommonJpaRepository).should(atLeastOnce()).findOneCommonCode(commonCodeEntity);
        then(mockAdminCommonJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공통코드 삭제 테스트")
    void 공통코드삭제테스트() {
        // given
        em.persist(commonCodeEntity);

        Long entityIdx = commonCodeEntity.getIdx();
        Long deleteIdx = adminCommonJpaRepository.deleteCommonCode(commonCodeEntity.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(entityIdx);
    }
}