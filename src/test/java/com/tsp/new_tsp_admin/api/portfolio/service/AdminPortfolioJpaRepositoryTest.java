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
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity.builder;
import static com.tsp.new_tsp_admin.api.portfolio.mapper.PortFolioMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
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
    private AdminPortFolioEntity adminPortFolioEntity;
    private AdminPortFolioDTO adminPortFolioDTO;
    private CommonImageEntity commonImageEntity;
    private CommonImageDTO commonImageDTO;

    void createPortfolioAndImage() {
        adminPortFolioEntity = builder()
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        adminPortFolioDTO = INSTANCE.toDto(adminPortFolioEntity);

        commonImageEntity = CommonImageEntity.builder()
                .idx(1)
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("portfolio")
                .build();

        commonImageDTO = PortfolioImageMapper.INSTANCE.toDto(commonImageEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        queryFactory = new JPAQueryFactory(em);
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
        assertThat(adminPortfolioJpaRepository.findPortfoliosList(portfolioMap)).isNotEmpty();
    }

    @Test
    @DisplayName("포트폴리오상세조회테스트")
    void 포트폴리오상세조회테스트() {
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
                },
                () -> {
                    assertThat(adminPortFolioDTO.getHashTag()).isEqualTo("#test");
                },
                () -> {
                    assertThat(adminPortFolioDTO.getVideoUrl()).isEqualTo("https://youtube.com");
                });
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
        portfolioList.add(AdminPortFolioDTO.builder().idx(1)
                .title("포트폴리오 테스트").description("포트폴리오 테스트")
                .portfolioImage(commonImageDtoList).build());

//        given(mockAdminPortfolioJpaRepository.findPortfoliosList(portfolioMap)).willReturn(portfolioList);
        when(mockAdminPortfolioJpaRepository.findPortfoliosList(portfolioMap)).thenReturn(portfolioList);

        // then
        assertThat(mockAdminPortfolioJpaRepository.findPortfoliosList(portfolioMap).get(0).getIdx()).isEqualTo(portfolioList.get(0).getIdx());
        assertThat(mockAdminPortfolioJpaRepository.findPortfoliosList(portfolioMap).get(0).getTitle()).isEqualTo(portfolioList.get(0).getTitle());
        assertThat(mockAdminPortfolioJpaRepository.findPortfoliosList(portfolioMap).get(0).getDescription()).isEqualTo(portfolioList.get(0).getDescription());

        // verify
        verify(mockAdminPortfolioJpaRepository, times(3)).findPortfoliosList(portfolioMap);
        verify(mockAdminPortfolioJpaRepository, atLeastOnce()).findPortfoliosList(portfolioMap);
        verifyNoMoreInteractions(mockAdminPortfolioJpaRepository);
    }

    @Test
    @DisplayName("포트폴리오상세BDD조회테스트")
    void 포트폴리오상세BDD조회테스트() {

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

//        given(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity)).willReturn(adminPortFolioDTO);
        when(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity)).thenReturn(adminPortFolioDTO);

        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getIdx()).isEqualTo(1);
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getHashTag()).isEqualTo("#test");
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        verify(mockAdminPortfolioJpaRepository, times(5)).findOnePortfolio(adminPortFolioEntity);
        verify(mockAdminPortfolioJpaRepository, atLeastOnce()).findOnePortfolio(adminPortFolioEntity);
        verifyNoMoreInteractions(mockAdminPortfolioJpaRepository);
    }

    @Test
    @DisplayName("포트폴리오등록테스트")
    void 포트폴리오등록테스트() {
        // given
        adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity);

        // when
        when(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity)).thenReturn(adminPortFolioDTO);

        // then
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getTitle()).isEqualTo("포트폴리오 테스트");
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getDescription()).isEqualTo("포트폴리오 테스트");
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getHashTag()).isEqualTo("#test");
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        verify(mockAdminPortfolioJpaRepository, times(4)).findOnePortfolio(adminPortFolioEntity);
        verify(mockAdminPortfolioJpaRepository, atLeastOnce()).findOnePortfolio(adminPortFolioEntity);
        verifyNoMoreInteractions(mockAdminPortfolioJpaRepository);
    }

    @Test
    @DisplayName("포트폴리오이미지등록테스트")
    void 포트폴리오이미지등록테스트() {
        // given
        Integer portfolioIdx = adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity).getIdx();

        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(portfolioIdx)
                .typeName("portfolio")
                .visible("Y")
                .build();

        // then
        assertNotNull(adminPortfolioJpaRepository.insertPortfolioImage(commonImageEntity));
    }

    @Test
    @DisplayName("포트폴리오수정테스트")
    void 포트폴리오수정테스트() {
        // given
        Integer idx = adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity).getIdx();

        adminPortFolioEntity = builder()
                .idx(idx)
                .categoryCd(1)
                .title("포트폴리오 테스트1")
                .description("포트폴리오 테스트1")
                .hashTag("#test1")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        AdminPortFolioDTO adminPortFolioDTO = INSTANCE.toDto(adminPortFolioEntity);

        adminPortfolioJpaRepository.updatePortfolio(adminPortFolioEntity);

        // when
        when(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity)).thenReturn(adminPortFolioDTO);

        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getTitle()).isEqualTo("포트폴리오 테스트1");
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getDescription()).isEqualTo("포트폴리오 테스트1");
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getHashTag()).isEqualTo("#test1");
        assertThat(mockAdminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity).getVideoUrl()).isEqualTo("https://youtube.com");

        // verify
        verify(mockAdminPortfolioJpaRepository, times(4)).findOnePortfolio(adminPortFolioEntity);
        verify(mockAdminPortfolioJpaRepository, atLeastOnce()).findOnePortfolio(adminPortFolioEntity);
        verifyNoMoreInteractions(mockAdminPortfolioJpaRepository);
    }

    @Test
    @DisplayName("포트폴리오삭제테스트")
    void 포트폴리오삭제테스트() {
        // given
        em.persist(adminPortFolioEntity);

        Integer entityIdx = adminPortFolioEntity.getIdx();
        Integer deleteIdx = adminPortfolioJpaRepository.deletePortfolio(adminPortFolioEntity.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(entityIdx);
    }
}