package com.tsp.new_tsp_admin.api.portfolio.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.api.portfolio.mapper.PortfolioImageMapper;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("포트폴리오 Repository Test")
class AdminPortfolioJpaRepositoryTest {
    private AdminPortFolioEntity adminPortFolioEntity;
    private AdminPortFolioDTO adminPortFolioDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;

    @Autowired
    private AdminPortfolioJpaRepository adminPortfolioJpaRepository;

    @Mock
    private AdminPortfolioJpaRepository mockAdminPortfolioJpaRepository;

    @Autowired
    private EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);

        adminPortFolioEntity = builder()
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        adminPortFolioDTO = AdminPortFolioDTO.builder()
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();

        commonImageDTO = CommonImageDTO.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("portfolio")
                .build();
    }

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
        adminPortFolioEntity = builder().idx(1).build();

        // when
        adminPortFolioDTO = adminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity);

        assertAll(() -> assertThat(adminPortFolioDTO.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(adminPortFolioDTO.getTitle()).isEqualTo("포트폴리오 테스트");
                },
                () -> {
                    assertThat(adminPortFolioDTO.getDescription()).isEqualTo("포트폴리오 테스트");
                });
    }

    @Test
    public void 포트폴리오BDD조회테스트() throws Exception {

        // given
        ConcurrentHashMap<String, Object> portfolioMap = new ConcurrentHashMap<>();
        portfolioMap.put("jpaStartPage", 1);
        portfolioMap.put("size", 3);

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

    @Test
    public void 포트폴리오상세BDD조회테스트() throws Exception {

        // given
        List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
        commonImageEntityList.add(commonImageEntity);

        adminPortFolioEntity = builder().idx(1).commonImageEntityList(commonImageEntityList).build();

        adminPortFolioDTO = AdminPortFolioDTO.builder()
                .idx(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .portfolioImage(PortfolioImageMapper.INSTANCE.toDtoList(commonImageEntityList))
                .build();

        given(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity)).willReturn(adminPortFolioDTO);

        // when
        Integer idx = mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getIdx();
        String title = mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getTitle();
        String description = mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getDescription();

        assertThat(idx).isEqualTo(1);
        assertThat(title).isEqualTo("포트폴리오 테스트");
        assertThat(description).isEqualTo("포트폴리오 테스트");
    }

    @Test
    public void 포트폴리오등록테스트() throws Exception {
        adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity);

        when(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity)).thenReturn(adminPortFolioDTO);

        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getDescription()).isEqualTo("포트폴리오 테스트");
    }

    @Test
    public void 포트폴리오이미지등록테스트() throws Exception {
        Integer portfolioIdx = adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity);

        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(portfolioIdx)
                .typeName("portfolio")
                .visible("Y")
                .build();

        assertNotNull(adminPortfolioJpaRepository.insertPortfolioImage(commonImageEntity));
    }

    @Test
    public void 포트폴리오수정테스트() throws Exception {
        Integer idx = adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity);

        adminPortFolioEntity = builder()
                .idx(idx)
                .categoryCd(1)
                .title("포트폴리오 테스트1")
                .description("포트폴리오 테스트1")
                .hashTag("#test1")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        AdminPortFolioDTO adminPortFolioDTO = AdminPortFolioDTO.builder()
                .idx(idx)
                .categoryCd(1)
                .title("포트폴리오 테스트1")
                .description("포트폴리오 테스트1")
                .hashTag("#test1")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        adminPortfolioJpaRepository.updatePortfolio(adminPortFolioEntity);

        when(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity)).thenReturn(adminPortFolioDTO);

        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getTitle()).isEqualTo("포트폴리오 테스트1");
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getDescription()).isEqualTo("포트폴리오 테스트1");
    }

    @Test
    public void 포트폴리오삭제테스트() throws Exception {
        em.persist(adminPortFolioEntity);

        // when
        when(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity)).thenReturn(adminPortFolioDTO);

        AdminPortFolioEntity adminPortFolioEntity1 = adminPortfolioJpaRepository.deletePortfolio(adminPortFolioEntity);

        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getTitle()).isEqualTo(adminPortFolioEntity1.getTitle());
    }
}