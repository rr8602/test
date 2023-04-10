package com.springboot.test.controller;

import com.google.gson.Gson;
import com.springboot.test.data.dto.ProductDto;
import com.springboot.test.data.dto.ProductResponseDto;
import com.springboot.test.data.entity.Product;
import com.springboot.test.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.awt.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest // 웹에서 사용하는 요청과 응답에 대한 테스트를 수행
// 대상 클래스만 로드해 테스트 수행 (만약 대상 클래스를 추가하지 않으면 @Controller, @RestController, @ControllerAdvice 등의 컨트롤러 관련 빈 객체가 모두 로드
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // 실제 빈 객체가 아닌 Mock(가짜) 객체를 생성해서 주입하는 역할, 실제 행위 수행 X, 개발자가 Mockito의 given() 메서드를 통해 동작 정의해야 함
    ProductServiceImpl productService; // ProductController가 가지고 있떤 ProductService 객체에 Mock 객체를 주입했다.

    @Test
    @DisplayName("MockMvc를 통한 Product 데이터 가져오기 테스트")
    void getProductTest() throws Exception{

        given(productService.getProduct(123L)).willReturn(new ProductResponseDto(123L, "pen", 5000, 2000));
        // given 메서드를 통해 이 객체에서 어떤 메서드가 호출되고, 어떤 파라미터를 주입받는지 가정한 후 willReturn 메서드를 통해 어떤 결과를 리턴할 것인지 정의하는 구조
        String productId = "123";

        mockMvc.perform( // 서버로 URL 요청을 보내는 것처럼 통신 테스트 코드를 작성해서 컨트롤러 테스트 가능
                // perform 메서드는 MockMvcRequestBuilders 에서 제공하는 HTTP 메서드로 URL을 정의해 사용
                // MockMvcRequestBuilders는 GET, POST, PUT, DELETE에 매핑되는 메서드 제공
                // 이 메서드는 MockHttpServletRequestBuilder 객체 리턴, HTTP 요청 정보 설정 가능
                // perform 메서드의 결과값으로 ResultActions 객체가 리턴됨 (andExpect() 메서드를 사용해 결괏값 검증 가능)
                get("/product?number=" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andDo(print()); // 요청과 응답의 전체 내용을 확인

        verify(productService).getProduct(123L);
    }

    // 일반적으로 @WebMvcTest 어노테이션을 사용한 테스는 슬라이스 테스트라고 부름
    // 슬라이스 테스트는 단위 테스트와 통합 테스트의 중간 개념으로 이해하면 되는데, 레이어드 아키텍처를 기준으로 각 레이어별로 나누어 테스트 진행한다는 의미
    // 단위 테스트를 진행하기 위해서는 모든 외부 요인을 차단하고 테스트를 진행해야 하지만, 컨트롤러는 개념상 웹과 가장 맞닿은 레이어로서 외부 요인을 차단하고
    // 테스트하면 의미가 없기 때문에 슬라이스 테스트를 진행하는 경우가 많습니다.


    @Test
    @DisplayName("Product 데이터 생성 테스트")
    void createProductTest() throws Exception{
        // Mock 객체에서 특정 메서드가 실행되는 경우 실제 Return을 줄 수 없기 때문에 아래와 같이 가정 사항을 만들어줌
        given(productService.saveProduct(new ProductDto("pen",5000,2000)))
                .willReturn(new ProductResponseDto(12315L,"pen",5000,2000));

        ProductDto productDto=ProductDto.builder()
                .name("pen")
                .price(5000)
                .stock(2000)
                .build(); // build() : 객체를 생성하는 동작 메서드

        Gson gson = new Gson();
        String content = gson.toJson(productDto);

        mockMvc.perform(
                post("/product")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andDo(print());

        verify(productService).saveProduct(new ProductDto("pen", 5000, 2000));

    }

}
