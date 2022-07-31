package com.tsp.new_tsp_admin.api.portfolio.service;

import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity.builder;
import static com.tsp.new_tsp_admin.api.portfolio.mapper.PortFolioMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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

    void createPortfolio() {
        adminPortFolioEntity = builder()
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
    @DisplayName("포트폴리오 리스트 조회 BDD 테스트")
    void 포트폴리오리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        List<AdminPortFolioDTO> returnPortfolioList = new ArrayList<>();
        returnPortfolioList.add(AdminPortFolioDTO.builder()
                .idx(1).title("portfolioTest").description("portfolioTest").hashTag("portfolio").videoUrl("test").visible("Y").build());

        // when
        when(mockAdminPortfolioJpaService.findPortfoliosList(portfolioMap)).thenReturn(returnPortfolioList);
        List<AdminPortFolioDTO> portfolioList = mockAdminPortfolioJpaService.findPortfoliosList(portfolioMap);

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
    }

    @Test
    @Disabled
    @DisplayName("포트폴리오 상세 조회 테스트")
    void 포트폴리오상세조회테스트() throws Exception {
        // given
        adminPortFolioEntity = builder().idx(1).build();

        // then
        assertThat(adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity).getTitle()).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오 상세 조회 BDD 테스트")
    void 포트폴리오상세조회BDD테스트() throws Exception {
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
    }

    @Test
    @DisplayName("포트폴리오 등록 테스트")
    void 포트폴리오등록테스트() throws Exception {
        // given
        adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity);

        // when
        when(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity)).thenReturn(adminPortFolioDTO);

        // then
        assertThat(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity).getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity).getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity).getHashTag()).isEqualTo("#test");
        assertThat(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity).getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        verify(mockAdminPortfolioJpaService, times(4)).findOnePortfolio(adminPortFolioEntity);
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findOnePortfolio(adminPortFolioEntity);
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);
    }

    @Test
    @DisplayName("포트폴리오 수정 테스트")
    void 포트폴리오수정테스트() throws Exception {
        // given
        Integer idx = adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity).getIdx();

        adminPortFolioEntity = builder()
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

        // then
        assertThat(mockAdminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity).getTitle()).isEqualTo("포트폴리오 테스트1");

        // verify
        verify(mockAdminPortfolioJpaService, times(1)).findOnePortfolio(adminPortFolioEntity);
        verify(mockAdminPortfolioJpaService, atLeastOnce()).findOnePortfolio(adminPortFolioEntity);
        verifyNoMoreInteractions(mockAdminPortfolioJpaService);
    }

    @Test
    @DisplayName("포트폴리오 삭제 테스트")
    void 포트폴리오삭제테스트() throws Exception {
        // given
        Integer idx = adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity).getIdx();

        // then
        assertThat(adminPortfolioJpaService.deletePortfolio(idx)).isNotNull();
    }
}