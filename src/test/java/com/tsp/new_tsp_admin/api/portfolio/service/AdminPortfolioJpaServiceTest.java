package com.tsp.new_tsp_admin.api.portfolio.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.portfolio.mapper.PortFolioMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
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
@DisplayName("포트폴리오 Service Test")
class AdminPortfolioJpaServiceTest {
    @Mock private AdminPortfolioJpaService mockAdminPortfolioJpaService;
    private final AdminPortfolioJpaService adminPortfolioJpaService;

    private AdminPortFolioEntity adminPortFolioEntity;
    private AdminPortFolioDTO adminPortFolioDTO;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    void createPortfolio() {
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        adminPortFolioDTO = INSTANCE.toDto(adminPortFolioEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createPortfolio();
    }

    @Test
    @DisplayName("포트폴리오 리스트 조회 테스트")
    void 포트폴리오리스트조회테스트() throws Exception {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        // then
        assertThat(adminPortfolioJpaService.findPortfoliosList(portfolioMap)).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오 리스트 조회 Mockito 테스트")
    void 포트폴리오리스트조회Mockito테스트() throws Exception {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        List<AdminPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(AdminPortFolioDTO.builder()
                .idx(1L).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());

        // when
        when(mockAdminPortfolioJpaService.findPortfoliosList(portfolioMap)).thenReturn(returnPortfolioList);
        List<AdminPortFolioDTO> portfolioList = mockAdminPortfolioJpaService.findPortfoliosList(portfolioMap);

        // then
        assertAll(
                () -> assertThat(portfolioList).isNotEmpty(),
                () -> assertThat(portfolioList).hasSize(1)
        );

        assertThat(portfolioList.get(0).getIdx()).isEqualTo(returnPortfolioList.get(0).getIdx());
        assertThat(portfolioList.get(0).getTitle()).isEqualTo(returnPortfolioList.get(0).getTitle());
        assertThat(portfolioList.get(0).getDescription()).isEqualTo(returnPortfolioList.get(0).getDescription());
        assertThat(portfolioList.get(0).getHashTag()).isEqualTo(returnPortfolioList.get(0).getHashTag());
        assertThat(portfolioList.get(0).getVideoUrl()).isEqualTo(returnPortfolioList.get(0).getVideoUrl());
        assertThat(portfolioList.get(0).getVisible()).isEqualTo(returnPortfolioList.get(0).getVisible());

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findPortfoliosList(portfolioMap);
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findPortfoliosList(portfolioMap);
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findPortfoliosList(portfolioMap);
    }

    @Test
    @DisplayName("포트폴리오 리스트 조회 BDD 테스트")
    void 포트폴리오리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        List<AdminPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(AdminPortFolioDTO.builder()
                .idx(1L).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());

        // when
        given(mockAdminPortfolioJpaService.findPortfoliosList(portfolioMap)).willReturn(returnPortfolioList);
        List<AdminPortFolioDTO> portfolioList = mockAdminPortfolioJpaService.findPortfoliosList(portfolioMap);

        // then
        assertAll(
                () -> assertThat(portfolioList).isNotEmpty(),
                () -> assertThat(portfolioList).hasSize(1)
        );

        assertThat(portfolioList.get(0).getIdx()).isEqualTo(returnPortfolioList.get(0).getIdx());
        assertThat(portfolioList.get(0).getTitle()).isEqualTo(returnPortfolioList.get(0).getTitle());
        assertThat(portfolioList.get(0).getDescription()).isEqualTo(returnPortfolioList.get(0).getDescription());
        assertThat(portfolioList.get(0).getHashTag()).isEqualTo(returnPortfolioList.get(0).getHashTag());
        assertThat(portfolioList.get(0).getVideoUrl()).isEqualTo(returnPortfolioList.get(0).getVideoUrl());
        assertThat(portfolioList.get(0).getVisible()).isEqualTo(returnPortfolioList.get(0).getVisible());

        // verify
        then(mockAdminPortfolioJpaService).should(times(1)).findPortfoliosList(portfolioMap);
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findPortfoliosList(portfolioMap);
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @Disabled
    @DisplayName("포트폴리오 상세 조회 테스트")
    void 포트폴리오상세조회테스트() throws Exception {
        // given
        adminPortFolioEntity = AdminPortFolioEntity.builder().idx(1L).build();

        // then
        assertThat(adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity).getTitle()).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 Mockito 테스트")
    void 포트폴리오상세조회Mockito테스트() throws Exception {
        // when
        when(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity)).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(adminPortFolioDTO.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(adminPortFolioDTO.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(adminPortFolioDTO.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(adminPortFolioDTO.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(adminPortFolioDTO.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(adminPortFolioDTO.getVisible());

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findOnePortfolio(adminPortFolioEntity);
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findOnePortfolio(adminPortFolioEntity);
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findOnePortfolio(adminPortFolioEntity);
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 BDD 테스트")
    void 포트폴리오상세조회BDD테스트() throws Exception {
        // when
        given(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity)).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(adminPortFolioDTO.getIdx());
        assertThat(portfolioInfo.getTitle()).isEqualTo(adminPortFolioDTO.getTitle());
        assertThat(portfolioInfo.getDescription()).isEqualTo(adminPortFolioDTO.getDescription());
        assertThat(portfolioInfo.getHashTag()).isEqualTo(adminPortFolioDTO.getHashTag());
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo(adminPortFolioDTO.getVideoUrl());
        assertThat(portfolioInfo.getVisible()).isEqualTo(adminPortFolioDTO.getVisible());

        // verify
        then(mockAdminPortfolioJpaService).should(times(1)).findOnePortfolio(adminPortFolioEntity);
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findOnePortfolio(adminPortFolioEntity);
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 Mockito 테스트")
    void 이전포트폴리오상세조회Mockito테스트() throws Exception {
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
        adminPortFolioDTO = adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity);

        when(mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity)).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findPrevOnePortfolio(adminPortFolioEntity);
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findPrevOnePortfolio(adminPortFolioEntity);
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findPrevOnePortfolio(adminPortFolioEntity);
    }

    @Test
    @DisplayName("이전 포트폴리오 상세 조회 BDD 테스트")
    void 이전포트폴리오상세조회BDD테스트() throws Exception {
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
        adminPortFolioDTO = adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity);

        given(mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity)).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminPortfolioJpaService).should(times(1)).findPrevOnePortfolio(adminPortFolioEntity);
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findPrevOnePortfolio(adminPortFolioEntity);
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 Mockito 테스트")
    void 다음포트폴리오상세조회Mockito테스트() throws Exception {
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
        adminPortFolioDTO = adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity);

        when(mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity)).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findNextOnePortfolio(adminPortFolioEntity);
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findNextOnePortfolio(adminPortFolioEntity);
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findNextOnePortfolio(adminPortFolioEntity);
    }

    @Test
    @DisplayName("다음 포트폴리오 상세 조회 BDD 테스트")
    void 다음포트폴리오상세조회BDD테스트() throws Exception {
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
        adminPortFolioDTO = adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity);

        given(mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity)).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findPrevOnePortfolio(adminPortFolioEntity);

        // then
        assertThat(portfolioInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminPortfolioJpaService).should(times(1)).findNextOnePortfolio(adminPortFolioEntity);
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findNextOnePortfolio(adminPortFolioEntity);
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오 등록 Mockito 테스트")
    void 포트폴리오등록Mockito테스트() throws Exception {
        // given
        adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity);

        // when
        when(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity)).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity);

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getHashTag()).isEqualTo("#test");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findOnePortfolio(adminPortFolioEntity);
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findOnePortfolio(adminPortFolioEntity);
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findOnePortfolio(adminPortFolioEntity);
    }

    @Test
    @DisplayName("포트폴리오 등록 BDD 테스트")
    void 포트폴리오등록BDD테스트() throws Exception {
        // given
        adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity);

        // when
        given(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity)).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity);

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(portfolioInfo.getHashTag()).isEqualTo("#test");
        assertThat(portfolioInfo.getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        then(mockAdminPortfolioJpaService).should(times(1)).findOnePortfolio(adminPortFolioEntity);
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findOnePortfolio(adminPortFolioEntity);
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오 수정 Mockito 테스트")
    void 포트폴리오수정Mockito테스트() throws Exception {
        // given
        Long idx = adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity).getIdx();

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(idx)
                .categoryCd(1)
                .title("포트폴리오 테스트1")
                .description("포트폴리오 테스트1")
                .hashTag("#test1")
                .videoUrl("https://test.com")
                .visible("Y")
                .build();

        adminPortFolioDTO = INSTANCE.toDto(adminPortFolioEntity);

        adminPortfolioJpaService.updatePortfolio(adminPortFolioEntity);

        // when
        when(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity)).thenReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity);

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트1");

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findOnePortfolio(adminPortFolioEntity);
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findOnePortfolio(adminPortFolioEntity);
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findOnePortfolio(adminPortFolioEntity);
    }

    @Test
    @DisplayName("포트폴리오 수정 BDD 테스트")
    void 포트폴리오수정BDD테스트() throws Exception {
        // given
        Long idx = adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity).getIdx();

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(idx)
                .categoryCd(1)
                .title("포트폴리오 테스트1")
                .description("포트폴리오 테스트1")
                .hashTag("#test1")
                .videoUrl("https://test.com")
                .visible("Y")
                .build();

        adminPortFolioDTO = INSTANCE.toDto(adminPortFolioEntity);

        adminPortfolioJpaService.updatePortfolio(adminPortFolioEntity);

        // when
        given(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity)).willReturn(adminPortFolioDTO);
        AdminPortFolioDTO portfolioInfo = mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity);

        // then
        assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트1");

        // verify
        then(mockAdminPortfolioJpaService).should(times(1)).findOnePortfolio(adminPortFolioEntity);
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findOnePortfolio(adminPortFolioEntity);
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("포트폴리오 삭제 테스트")
    void 포트폴리오삭제테스트() throws Exception {
        // given
        Long idx = adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity).getIdx();

        // then
        assertThat(adminPortfolioJpaService.deletePortfolio(idx)).isNotNull();
    }

    @Test
    @DisplayName("포트폴리오 어드민 코멘트 조회 Mockito 테스트")
    void 포트폴리오어드민코멘트조회Mockito테스트() throws Exception {
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        Long portfolioIdx = adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity).getIdx();

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

        when(mockAdminPortfolioJpaService.findPortfolioAdminComment(adminPortFolioEntity)).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminPortfolioJpaService.findPortfolioAdminComment(adminPortFolioEntity);

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("portfolio");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminPortFolioEntity.getIdx());

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findPortfolioAdminComment(adminPortFolioEntity);
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findPortfolioAdminComment(adminPortFolioEntity);
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);

        InOrder inOrder = inOrder(mockAdminPortfolioJpaService);
        inOrder.verify(mockAdminPortfolioJpaService).findPortfolioAdminComment(adminPortFolioEntity);
    }

    @Test
    @DisplayName("포트폴리오 어드민 코멘트 조회 BDD 테스트")
    void 포트폴리오어드민코멘트조회BDD테스트() throws Exception {
        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        Long portfolioIdx = adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity).getIdx();

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

        given(mockAdminPortfolioJpaService.findPortfolioAdminComment(adminPortFolioEntity)).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminPortfolioJpaService.findPortfolioAdminComment(adminPortFolioEntity);

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("portfolio");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminPortFolioEntity.getIdx());

        // verify
        then(mockAdminPortfolioJpaService).should(times(1)).findPortfolioAdminComment(adminPortFolioEntity);
        then(mockAdminPortfolioJpaService).should(atLeastOnce()).findPortfolioAdminComment(adminPortFolioEntity);
        then(mockAdminPortfolioJpaService).shouldHaveNoMoreInteractions();
    }
}