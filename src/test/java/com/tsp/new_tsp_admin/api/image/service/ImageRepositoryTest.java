package com.tsp.new_tsp_admin.api.image.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
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

import static org.assertj.core.api.Assertions.assertThat;
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
@DisplayName("이미지 Repository Test")
class ImageRepositoryTest {
    @Mock
    private ImageRepository mockImageRepository;
    private final ImageRepository imageRepository;
    private final EntityManager em;
    protected JPAQueryFactory queryFactory;

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    @DisplayName("파일넘버최대값조회Mockito테스트")
    void 파일넘버최대값조회Mockito테스트() {
        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .fileNum(2)
                .typeName("model")
                .visible("Y")
                .build();

        imageRepository.insertImage(commonImageEntity);

        // when
        when(mockImageRepository.maxSubCnt(commonImageEntity) + 1).thenReturn(commonImageEntity.getFileNum() + 1);
        Integer maxSubCnt = mockImageRepository.maxSubCnt(commonImageEntity);

        // then
        assertThat(maxSubCnt).isEqualTo(commonImageEntity.getFileNum() + 1);

        // verify
        verify(mockImageRepository, times(1)).maxSubCnt(commonImageEntity);
        verify(mockImageRepository, atLeastOnce()).maxSubCnt(commonImageEntity);
        verifyNoMoreInteractions(mockImageRepository);

        InOrder inOrder = inOrder(mockImageRepository);
        inOrder.verify(mockImageRepository).maxSubCnt(commonImageEntity);
    }

    @Test
    @DisplayName("파일넘버최대값조회BDD테스트")
    void 파일넘버최대값조회BDD테스트() {
        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .fileNum(2)
                .typeName("model")
                .visible("Y")
                .build();

        imageRepository.insertImage(commonImageEntity);

        // when
        given(mockImageRepository.maxSubCnt(commonImageEntity) + 1).willReturn(commonImageEntity.getFileNum() + 1);
        Integer maxSubCnt = mockImageRepository.maxSubCnt(commonImageEntity);

        // then
        assertThat(maxSubCnt).isEqualTo(commonImageEntity.getFileNum() + 1);

        // verify
        then(mockImageRepository).should(times(1)).maxSubCnt(commonImageEntity);
        then(mockImageRepository).should(atLeastOnce()).maxSubCnt(commonImageEntity);
        then(mockImageRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이미지등록Mockito테스트")
    void 이미지등록Mockito테스트() {
        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .fileNum(2)
                .typeName("model")
                .visible("Y")
                .build();

        imageRepository.insertImage(commonImageEntity);

        // when
        when(mockImageRepository.findOneImage(commonImageEntity)).thenReturn(commonImageEntity);
        CommonImageEntity imageEntity = mockImageRepository.findOneImage(commonImageEntity);

        // then
        assertThat(imageEntity.getImageType()).isEqualTo("main");
        assertThat(imageEntity.getFileName()).isEqualTo("test.jpg");
        assertThat(imageEntity.getFileMask()).isEqualTo("test.jpg");

        // verify
        verify(mockImageRepository, times(1)).findOneImage(commonImageEntity);
        verify(mockImageRepository, atLeastOnce()).findOneImage(commonImageEntity);
        verifyNoMoreInteractions(mockImageRepository);

        InOrder inOrder = inOrder(mockImageRepository);
        inOrder.verify(mockImageRepository).findOneImage(commonImageEntity);
    }

    @Test
    @DisplayName("이미지등록BDD테스트")
    void 이미지등록BDD테스트() {
        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .fileNum(2)
                .typeName("model")
                .visible("Y")
                .build();

        imageRepository.insertImage(commonImageEntity);

        // when
        given(mockImageRepository.findOneImage(commonImageEntity)).willReturn(commonImageEntity);
        CommonImageEntity imageEntity = mockImageRepository.findOneImage(commonImageEntity);

        // then
        assertThat(imageEntity.getImageType()).isEqualTo("main");
        assertThat(imageEntity.getFileName()).isEqualTo("test.jpg");
        assertThat(imageEntity.getFileMask()).isEqualTo("test.jpg");

        // verify
        then(mockImageRepository).should(times(1)).findOneImage(commonImageEntity);
        then(mockImageRepository).should(atLeastOnce()).findOneImage(commonImageEntity);
        then(mockImageRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이미지삭제Mockito테스트")
    void 이미지삭제Mockito테스트() {
        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .fileNum(2)
                .typeName("model")
                .visible("Y")
                .build();

        em.persist(commonImageEntity);

        // when
        when(mockImageRepository.findOneImage(commonImageEntity)).thenReturn(commonImageEntity);
        CommonImageEntity commonImageEntity1 = imageRepository.deleteImage(commonImageEntity);

        // then
        assertThat(mockImageRepository.findOneImage(commonImageEntity).getImageType()).isEqualTo(commonImageEntity1.getImageType());

        // verify
        verify(mockImageRepository, times(1)).findOneImage(commonImageEntity);
        verify(mockImageRepository, atLeastOnce()).findOneImage(commonImageEntity);
        verifyNoMoreInteractions(mockImageRepository);

        InOrder inOrder = inOrder(mockImageRepository);
        inOrder.verify(mockImageRepository).findOneImage(commonImageEntity);
    }

    @Test
    @DisplayName("이미지삭제BDD테스트")
    void 이미지삭제BDD테스트() {
        // given
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1L)
                .fileNum(2)
                .typeName("model")
                .visible("Y")
                .build();

        em.persist(commonImageEntity);

        // when
        given(mockImageRepository.findOneImage(commonImageEntity)).willReturn(commonImageEntity);
        CommonImageEntity commonImageEntity1 = imageRepository.deleteImage(commonImageEntity);

        // then
        assertThat(mockImageRepository.findOneImage(commonImageEntity).getImageType()).isEqualTo(commonImageEntity1.getImageType());

        // verify
        then(mockImageRepository).should(times(1)).findOneImage(commonImageEntity);
        then(mockImageRepository).should(atLeastOnce()).findOneImage(commonImageEntity);
        then(mockImageRepository).shouldHaveNoMoreInteractions();
    }
}