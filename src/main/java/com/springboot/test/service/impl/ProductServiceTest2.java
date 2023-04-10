package com.springboot.test.service.impl;

import com.springboot.test.data.dto.ProductDto;
import com.springboot.test.data.dto.ProductResponseDto;
import com.springboot.test.data.entity.Product;
import com.springboot.test.data.repository.ProductRepository;
import com.springboot.test.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class) // 스프링에서 객체를 주입받기 위해 해당 어노테이션을 사용해 Junit5의 테스트에서 스프링 테스트 컨텍스트를 사용하도록 설정
@Import(ProductServiceImpl.class) // Autowired 어노테이션으로 주입받는 ProductService를 주입받기 위해 사용
public class ProductServiceTest2 { // Service 단 -> Repository 단으로 DTO 전송

    @MockBean
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @BeforeEach
    public void setUpTest() {
        productService=new ProductServiceImpl(productRepository); // 각 테스트 전에 ProductService 객체를 초기화해서 사용
    }

    @Test
    void getProductTest(){ // Given-When-Then 패턴을 기반으로 작성
        Product givenProduct=new Product(); // 테스트에 사용될 Product 엔티티 객체 생성
        givenProduct.setNumber(123L);
        givenProduct.setName("펜");
        givenProduct.setPrice(1000);
        givenProduct.setStock(1234);

        Mockito.when(productRepository.findById(123L)) // ProductRepository의 동작에 대한 결괏값 리턴 설정
                .thenReturn(Optional.of(givenProduct)); // Optional.of : value값이 null이 아닌 경우 사용

        ProductResponseDto productResponseDto=productService.getProduct(123L);

        // Assertion을 통해 값을 검증함으로써 테스트의 목적을 달성하는지 확인
        Assertions.assertEquals(productResponseDto.getNumber(), givenProduct.getNumber());
        Assertions.assertEquals(productResponseDto.getName(), givenProduct.getName());
        Assertions.assertEquals(productResponseDto.getPrice(), givenProduct.getPrice());
        Assertions.assertEquals(productResponseDto.getStock(), givenProduct.getStock());

        // 검증 보완을 위해 verify() 메서드로 부가 검증 시도
        verify(productRepository).findById(123L);
    }

    @Test
    void saveProductTest(){
        Mockito.when(productRepository.save(any(Product.class))) // 레퍼런스 변수(객체의 위치를 가리킴)의 비교는 주솟값으로 이뤄지기 때문에 any()를 사용해 클래스만 정의하는 경우도 있다.
                .then(returnsFirstArg());

        ProductResponseDto productResponseDto=productService.saveProduct(
                new ProductDto("펜", 1000, 1234));


        Assertions.assertEquals(productResponseDto.getName(),"펜");
        Assertions.assertEquals(productResponseDto.getPrice(),1000);
        Assertions.assertEquals(productResponseDto.getStock(),"1234");

        verify(productRepository).save(any());
    }
}
