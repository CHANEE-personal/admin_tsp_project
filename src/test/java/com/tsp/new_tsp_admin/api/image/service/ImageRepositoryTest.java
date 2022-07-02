package com.tsp.new_tsp_admin.api.image.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("이미지 Repository Test")
class ImageRepositoryTest {
    @Autowired
    private ImageRepository imageRepository;

    @Mock
    private ImageRepository mockImageRepository;

    @Autowired
    private EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    public void 파일넘버최대값조회() throws Exception {
        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .fileNum(2)
                .typeName("model")
                .visible("Y")
                .build();

        imageRepository.insertImage(commonImageEntity);

        // when
        when(mockImageRepository.maxSubCnt(commonImageEntity)+1).thenReturn(commonImageEntity.getFileNum()+1);

        // then
        assertThat(mockImageRepository.maxSubCnt(commonImageEntity)).isEqualTo(commonImageEntity.getFileNum()+1);
    }

    @Test
    public void 이미지등록테스트() throws Exception {
        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .fileNum(2)
                .typeName("model")
                .visible("Y")
                .build();

        imageRepository.insertImage(commonImageEntity);

        // when
        when(mockImageRepository.findOneImage(commonImageEntity)).thenReturn(commonImageEntity);

        // then
        assertThat(mockImageRepository.findOneImage(commonImageEntity).getImageType()).isEqualTo("main");
        assertThat(mockImageRepository.findOneImage(commonImageEntity).getFileName()).isEqualTo("test.jpg");
        assertThat(mockImageRepository.findOneImage(commonImageEntity).getFileMask()).isEqualTo("test.jpg");
    }

    @Test
    public void 이미지삭제테스트() throws Exception {
        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .fileNum(2)
                .typeName("model")
                .visible("Y")
                .build();

        em.persist(commonImageEntity);
        // when
        when(mockImageRepository.findOneImage(commonImageEntity)).thenReturn(commonImageEntity);

        CommonImageEntity commonImageEntity1 = imageRepository.deleteImage(commonImageEntity);

        assertThat(mockImageRepository.findOneImage(commonImageEntity).getImageType()).isEqualTo(commonImageEntity1.getImageType());
    }
}