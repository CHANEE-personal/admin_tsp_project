package com.tsp.new_tsp_admin.api.portfolio.service;

import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity.builder;
import static com.tsp.new_tsp_admin.api.portfolio.mapper.PortFolioMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("포트폴리오 Service Test")
class AdminPortfolioJpaServiceTest {
    AdminPortFolioEntity adminPortFolioEntity;
    AdminPortFolioDTO adminPortFolioDTO;
    @Autowired private AdminPortfolioJpaService adminPortfolioJpaService;
    @Mock private AdminPortfolioJpaService mockAdminPortfolioJpaService;

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

        assertThat(adminPortfolioJpaService.findPortfoliosList(portfolioMap)).isNotEmpty();
    }

    @Test
    @Disabled
    @DisplayName("포트폴리오 상세 조회 테스트")
    void 포트폴리오상세조회테스트() throws Exception {
        // given
        adminPortFolioEntity = builder().idx(1).build();

        assertThat(adminPortfolioJpaService.findOnePortfolio(adminPortFolioEntity).getTitle()).isNotEmpty();
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
    }

    @Test
    @DisplayName("포트폴리오 삭제 테스트")
    void 포트폴리오삭제테스트() throws Exception {
        Integer idx = adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity).getIdx();

        assertThat(adminPortfolioJpaService.deletePortfolio(idx)).isNotNull();
    }
}