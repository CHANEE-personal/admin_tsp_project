package com.tsp.new_tsp_admin.api.portfolio.service;

import com.tsp.new_tsp_admin.api.common.EntityType;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
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
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("포트폴리오 Repository Test")
class AdminPortfolioJpaRepositoryTest {
    @Mock private AdminPortfolioJpaRepository mockAdminPortfolioJpaRepository;
    private final AdminPortfolioJpaRepository adminPortfolioJpaRepository;
    private final EntityManager em;

    private AdminPortFolioEntity adminPortFolioEntity;
    private AdminPortFolioDTO adminPortFolioDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    void createPortfolioAndImage() {
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        adminPortFolioDTO = AdminPortFolioEntity.toDto(adminPortFolioEntity);

        commonImageEntity = CommonImageEntity.builder()
                .idx(1L)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .typeName(EntityType.PORTFOLIO)
                .build();

        commonImageDTO = CommonImageEntity.toDto(commonImageEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createPortfolioAndImage();
    }

    @Test
    @DisplayName("포트폴리오조회테스트")
    void 포트폴리오조회테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        // then
        assertThat(adminPortfolioJpaRepository.findPortfolioList(portfolioMap)).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오상세조회테스트")
    void 포트폴리오상세조회테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder().idx(1L).build();

        // when
        adminPortFolioDTO = adminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertAll(() -> {
                    assertThat(adminPortFolioDTO.getIdx()).isEqualTo(1);
                },
                () -> {
                    assertThat(adminPortFolioDTO.getTitle()).isEqualTo("포트폴리오 테스트");
                },
                () -> {
                    assertThat(adminPortFolioDTO.getDescription()).isEqualTo("포트폴리오 테스트");
                },
                () -> {
                    assertThat(adminPortFolioDTO.getHashTag()).isEqualTo("#test");
                },
                () -> {
                    assertThat(adminPortFolioDTO.getVideoUrl()).isEqualTo("https://youtube.com");
                });
    }

    @Test
    @DisplayName("포트폴리오Mockito조회테스트")
    void 포트폴리오Mockito조회테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminPortFolioDTO> portfolioList = new ArrayList<>();
        portfolioList.add(AdminPortFolioDTO.builder().idx(1L)
                .title("포트폴리오 테스트").description("포트폴리오 테스트")
                .portfolioImage(commonImageDtoList).build());

        // when
        when(mockAdminPortfolioJpaRepository.findPortfolioList(portfolioMap)).thenReturn(portfolioList);
        List<AdminPortFolioDTO> newPortfolioList = mockAdminPortfolioJpaRepository.findPortfolioList(portfolioMap);

        // then
        assertThat(newPortfolioList.get(0).getIdx()).isEqualTo(portfolioList.get(0).getIdx());
        assertThat(newPortfolioList.get(0).getTitle()).isEqualTo(portfolioList.get(0).getTitle());
        assertThat(newPortfolioList.get(0).getDescription()).isEqualTo(portfolioList.get(0).getDescription());

        // verify
        verify(mockAdminPortfolioJpaRepository, times(1)).findPortfolioList(portfolioMap);
        verify(mockAdminPortfolioJpaRepository, atLeastOnce()).findPortfolioList(portfolioMap);
        verifyNoMoreInteractions(mockAdminPortfolioJpaRepository);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaRepository);
        inOrder.verify(mockAdminPortfolioJpaRepository).findPortfolioList(portfolioMap);
    }

    @Test
    @DisplayName("포트폴리오BDD조회테스트")
    void 포트폴리오BDD조회테스트() {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminPortFolioDTO> portfolioList = new ArrayList<>();
        portfolioList.add(AdminPortFolioDTO.builder().idx(1L)
                .title("포트폴리오 테스트").description("포트폴리오 테스트")
                .portfolioImage(commonImageDtoList).build());

        // when
        given(mockAdminPortfolioJpaRepository.findPortfolioList(portfolioMap)).willReturn(portfolioList);
        List<AdminPortFolioDTO> newPortfolioList = mockAdminPortfolioJpaRepository.findPortfolioList(portfolioMap);

        // then
        assertThat(newPortfolioList.get(0).getIdx()).isEqualTo(portfolioList.get(0).getIdx());
        assertThat(newPortfolioList.get(0).getTitle()).isEqualTo(portfolioList.get(0).getTitle());
        assertThat(newPortfolioList.get(0).getDescription()).isEqualTo(portfolioList.get(0).getDescription());

        // verify
        then(mockAdminPortfolioJpaRepository).should(times(1)).findPortfolioList(portfolioMap);
        then(mockAdminPortfolioJpaRepository).should(atLeastOnce()).findPortfolioList(portfolioMap);
        then(mockAdminPortfolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오상세Mockito조회테스트")
    void 포트폴리오상세Mockito조회테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        adminPortFolioEntity = AdminPortFolioEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();

        adminPortFolioDTO = AdminPortFolioDTO.builder()
                .idx(1L)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .portfolioImage(CommonImageEntity.toDtoList(commonImageEntityList))
                .build();

        // when
        when(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getHashTag()).isEqualTo("#test");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        verify(mockAdminPortfolioJpaRepository, times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaRepository, atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaRepository);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaRepository);
        inOrder.verify(mockAdminPortfolioJpaRepository).findOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오상세BDD조회테스트")
    void 포트폴리오상세BDD조회테스트() {
        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        adminPortFolioEntity = AdminPortFolioEntity.builder().idx(1L).commonImageEntityList(commonImageEntityList).build();

        adminPortFolioDTO = AdminPortFolioDTO.builder()
                .idx(1L)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .portfolioImage(CommonImageEntity.toDtoList(commonImageEntityList))
                .build();

        // when
        given(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getHashTag()).isEqualTo("#test");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        then(mockAdminPortfolioJpaRepository).should(times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).should(atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 포트폴리오 상세 조회 테스트")
    void 이전or다음포트폴리오상세조회테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        // 이전 포트폴리오
        assertThat(adminPortfolioJpaRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 포트폴리오
        assertThat(adminPortfolioJpaRepository.findNextOnePortfolio(adminPortFolioEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 Mockito 테스트")
    void 이전포트폴리오상세조회Mockito테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        when(mockAdminPortfolioJpaRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminPortfolioJpaRepository, times(1)).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaRepository, atLeastOnce()).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaRepository);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaRepository);
        inOrder.verify(mockAdminPortfolioJpaRepository).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 BDD 테스트")
    void 이전포트폴리오상세조회BDD테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        given(mockAdminPortfolioJpaRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminPortfolioJpaRepository).should(times(1)).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).should(atLeastOnce()).findPrevOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 Mockito 테스트")
    void 다음포트폴리오상세조회Mockito테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        when(mockAdminPortfolioJpaRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminPortfolioJpaRepository, times(1)).findNextOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaRepository, atLeastOnce()).findNextOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaRepository);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaRepository);
        inOrder.verify(mockAdminPortfolioJpaRepository).findNextOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 BDD 테스트")
    void 다음포트폴리오상세조회BDD테스트() {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(2L)
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        // when
        adminPortFolioDTO = adminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        given(mockAdminPortfolioJpaRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaRepository.findPrevOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminPortfolioJpaRepository).should(times(1)).findNextOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).should(atLeastOnce()).findNextOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오등록Mockito테스트")
    void 포트폴리오등록Mockito테스트() {
        // given
        adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity);

        // when
        when(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getHashTag()).isEqualTo("#test");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        verify(mockAdminPortfolioJpaRepository, times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaRepository, atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaRepository);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaRepository);
        inOrder.verify(mockAdminPortfolioJpaRepository).findOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오등록BDD테스트")
    void 포트폴리오등록BDD테스트() {
        // given
        adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity);

        // when
        given(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getHashTag()).isEqualTo("#test");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        then(mockAdminPortfolioJpaRepository).should(times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).should(atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오이미지등록테스트")
    void 포트폴리오이미지등록테스트() {
        // given
        Long portfolioIdx = adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity).getIdx();

        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(portfolioIdx)
                .typeName(EntityType.PORTFOLIO)
                .visible("Y")
                .build();

        // then
        assertNotNull(adminPortfolioJpaRepository.insertPortfolioImage(commonImageEntity));
    }

    @Test
    @DisplayName("포트폴리오수정Mockito테스트")
    void 포트폴리오수정Mockito테스트() {
        // given
        Long idx = adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity).getIdx();

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(idx)
                .categoryCd(1)
                .title("포트폴리오 테스트1")
                .description("포트폴리오 테스트1")
                .hashTag("#test1")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        AdminPortFolioDTO adminPortFolioDTO = AdminPortFolioEntity.toDto(adminPortFolioEntity);

        adminPortfolioJpaRepository.updatePortfolio(adminPortFolioEntity);

        // when
        when(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트1");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트1");
        assertThat(portfolioInfo.getHashTag()).isEqualTo("#test1");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        verify(mockAdminPortfolioJpaRepository, times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaRepository, atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaRepository);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaRepository);
        inOrder.verify(mockAdminPortfolioJpaRepository).findOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오수정BDD테스트")
    void 포트폴리오수정BDD테스트() {
        // given
        Long idx = adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity).getIdx();

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(idx)
                .categoryCd(1)
                .title("포트폴리오 테스트1")
                .description("포트폴리오 테스트1")
                .hashTag("#test1")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        AdminPortFolioDTO adminPortFolioDTO = AdminPortFolioEntity.toDto(adminPortFolioEntity);

        adminPortfolioJpaRepository.updatePortfolio(adminPortFolioEntity);

        // when
        given(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트1");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트1");
        assertThat(portfolioInfo.getHashTag()).isEqualTo("#test1");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        then(mockAdminPortfolioJpaRepository).should(times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).should(atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오삭제테스트")
    void 포트폴리오삭제테스트() {
        // given
        em.persist(adminPortFolioEntity);

        Long entityIdx = adminPortFolioEntity.getIdx();
        Long deleteIdx = adminPortfolioJpaRepository.deletePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(entityIdx);
    }

    @Test
    @DisplayName("포트폴리오삭제Mockito테스트")
    void 포트폴리오삭제Mockito테스트() {
        // given
        em.persist(adminPortFolioEntity);
        adminPortFolioDTO = AdminPortFolioEntity.toDto(adminPortFolioEntity);

        // when
        when(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx())).thenReturn(adminPortFolioDTO);
        Long deleteIdx = adminPortfolioJpaRepository.deletePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminPortfolioJpaRepository, times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaRepository, atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaRepository);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaRepository);
        inOrder.verify(mockAdminPortfolioJpaRepository).findOnePortfolio(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오삭제BDD테스트")
    void 포트폴리오삭제BDD테스트() {
        // given
        em.persist(adminPortFolioEntity);
        adminPortFolioDTO = AdminPortFolioEntity.toDto(adminPortFolioEntity);

        // when
        given(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx())).willReturn(adminPortFolioDTO);
        Long deleteIdx = adminPortfolioJpaRepository.deletePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminPortfolioJpaRepository).should(times(1)).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).should(atLeastOnce()).findOnePortfolio(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오 어드민 코멘트 조회 Mockito 테스트")
    void 포트폴리오어드민코멘트조회Mockito테스트() {
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        Long portfolioIdx = adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("portfolio")
                .commentTypeIdx(portfolioIdx)
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("portfolio")
                .commentTypeIdx(portfolioIdx)
                .visible("Y")
                .build());

        when(mockAdminPortfolioJpaRepository.findPortfolioAdminComment(adminPortFolioEntity.getIdx())).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminPortfolioJpaRepository.findPortfolioAdminComment(adminPortFolioEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("portfolio");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminPortFolioEntity.getIdx());

        // verify
        verify(mockAdminPortfolioJpaRepository, times(1)).findPortfolioAdminComment(adminPortFolioEntity.getIdx());
        verify(mockAdminPortfolioJpaRepository, atLeastOnce()).findPortfolioAdminComment(adminPortFolioEntity.getIdx());
        verifyNoMoreInteractions(mockAdminPortfolioJpaRepository);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaRepository);
        inOrder.verify(mockAdminPortfolioJpaRepository).findPortfolioAdminComment(adminPortFolioEntity.getIdx());
    }

    @Test
    @DisplayName("포트폴리오 어드민 코멘트 조회 BDD 테스트")
    void 포트폴리오어드민코멘트조회BDD테스트() {
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        Long portfolioIdx = adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("portfolio")
                .commentTypeIdx(portfolioIdx)
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("portfolio")
                .commentTypeIdx(portfolioIdx)
                .visible("Y")
                .build());

        given(mockAdminPortfolioJpaRepository.findPortfolioAdminComment(adminPortFolioEntity.getIdx())).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminPortfolioJpaRepository.findPortfolioAdminComment(adminPortFolioEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("portfolio");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminPortFolioEntity.getIdx());

        // verify
        then(mockAdminPortfolioJpaRepository).should(times(1)).findPortfolioAdminComment(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).should(atLeastOnce()).findPortfolioAdminComment(adminPortFolioEntity.getIdx());
        then(mockAdminPortfolioJpaRepository).shouldHaveNoMoreInteractions();
    }
}