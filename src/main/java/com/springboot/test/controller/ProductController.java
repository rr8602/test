package com.springboot.test.controller;

import com.springboot.test.data.dto.ChangeProductNameDto;
import com.springboot.test.data.dto.ProductDto;
import com.springboot.test.data.dto.ProductResponseDto;
import com.springboot.test.service.ProductService;
import com.springboot.test.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @Autowired // 생성자를 통한 빈 주입 받음
    public ProductController(ProductService productService){
        this.productService=productService;
    } // ProductService의 객체를 의존성 주입 받음

    @ApiOperation(value = "GET", notes = "서비스 단에서 받은 데이터를 클라이언트에게 응답")
    @GetMapping()
    public ResponseEntity<ProductResponseDto> getProduct(Long number){
        ProductResponseDto productResponseDto=productService.getProduct(number); // getProduct의 return 값 = productResponseDto

        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @ApiOperation(value = "POST", notes = "서비스 단에서 받은 데이터를 클라이언트에게 응답")
    @PostMapping()
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductDto productDto){ // @RequestBody 해당 HTTP Body의 내용을 어노테이션 받는 객체에 대입
        ProductResponseDto productResponseDto=productService.saveProduct(productDto);

        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @ApiOperation(value = "PUT", notes = "서비스 단에서 받은 데이터를 클라이언트에게 응답")
    @PutMapping()
    public ResponseEntity<ProductResponseDto> changeProductName(
            @RequestBody ChangeProductNameDto changeProductNameDto) throws Exception{
        ProductResponseDto productResponseDto=productService.changeProductName(
                changeProductNameDto.getNumber(),
                changeProductNameDto.getName());

        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @ApiOperation(value = "DELETE", notes = "서비스 단에서 받은 데이터를 클라이언트에게 응답")
    @DeleteMapping(produces="application/json;charset=UTF-8")
    public ResponseEntity<String> deleteProduct(Long number) throws Exception{
        productService.deleteProduct(number);

        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
    }
}
