package com.tsp.new_tsp_admin.api.portfolio.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("포트폴리오 Repository Test")
class AdminPortfolioJpaRepositoryTest {

    @Autowired
    private AdminPortfolioJpaRepository adminPortfolioJpaRepository;

    @Mock
    private AdminPortfolioJpaRepository mockPortfolioJpaRepository;

    @Autowired
    private EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() { queryFactory = new JPAQueryFactory(em); }

    @Test
    public void 포트폴리오조회테스트() throws Exception {

        // given
        Map<String, Object> portfolioMap = new HashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        // when
        List<AdminPortFolioDTO> portfolioList = adminPortfolioJpaRepository.findPortfoliosList(portfolioMap);

        // then
        assertThat(portfolioList.size()).isGreaterThan(0);
    }

    @Test
    public void 포트폴리오상세조회테스트() throws Exception {

        // given
        AdminPortFolioEntity adminPortFolioEntity = builder().idx(1).build();

        // when
        AdminPortFolioDTO portfolioInfo = adminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity);

        assertAll(() -> assertThat(portfolioInfo.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(portfolioInfo.getTitle()).isEqualTo("포트폴리오 테스트");
                },
                () -> {
                    assertThat(portfolioInfo.getDescription()).isEqualTo("포트폴리오 테스트");
                });
    }
}