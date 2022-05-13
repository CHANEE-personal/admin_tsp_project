package com.tsp.new_tsp_admin.api.portfolio.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.QAdminPortFolioEntity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity.builder;
import static com.tsp.new_tsp_admin.api.domain.portfolio.QAdminPortFolioEntity.adminPortFolioEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

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
    private AdminPortfolioJpaRepository mockAdminPortfolioJpaRepository;

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

    @Test
    public void 포트폴리오BDD조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> portfolioMap = new ConcurrentHashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

        CommonImageDTO commonImageDTO = CommonImageDTO.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("portfolio")
                .build();

        List<CommonImageDTO> commonImageDtoList = new ArrayList<>();
        commonImageDtoList.add(commonImageDTO);

        List<AdminPortFolioDTO> portfolioList = new ArrayList<>();
        portfolioList.add(AdminPortFolioDTO.builder().idx(1)
                .title("포트폴리오 테스트").description("포트폴리오 테스트")
                .portfolioImage(commonImageDtoList).build());

        given(mockAdminPortfolioJpaRepository.findPortfoliosList(portfolioMap)).willReturn(portfolioList);

        // when
        Integer idx = mockAdminPortfolioJpaRepository.findPortfoliosList(portfolioMap).get(0).getIdx();
        String title = mockAdminPortfolioJpaRepository.findPortfoliosList(portfolioMap).get(0).getTitle();
        String description = mockAdminPortfolioJpaRepository.findPortfoliosList(portfolioMap).get(0).getDescription();

        // then
        assertThat(idx).isEqualTo(portfolioList.get(0).getIdx());
        assertThat(title).isEqualTo(portfolioList.get(0).getTitle());
        assertThat(description).isEqualTo(portfolioList.get(0).getDescription());
    }
}