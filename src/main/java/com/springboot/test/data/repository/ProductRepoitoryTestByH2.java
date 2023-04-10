package com.springboot.test.data.repository;

import com.springboot.test.data.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/* @DataJpaTest
1. JPA와 관련된 설정만 로드해서 테스트 징행
2. 기본적으로 @Transactional 어노테이션 포함, 테스트 코드가 종료되면 자동으로 데이터베이스의 롤백 진행
3. 기본값으로 임베디드 데이터베이스 사용, 다른 데이터베이스 사용 시, 별도의 설정을 거쳐 사용 가능
 */
@DataJpaTest
public class ProductRepoitoryTestByH2 {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void selectTest(){
        // given
        Product product=new Product();
        product.setName("펜");
        product.setPrice(1000);
        product.setStock(1000);

        Product savedProduct=productRepository.saveAndFlush(product); // 먼저 데이터베이스에 테스트 데이터를 추가

        // when
        Product foundProduct=productRepository.findById(savedProduct.getNumber()).get();

        // then
        assertEquals(product.getName(), foundProduct.getName());
        assertEquals(product.getPrice(), foundProduct.getPrice());
        assertEquals(product.getStock(), foundProduct.getStock());
    }

    @Test
    void saveTest(){
        // given : 테스트에서 사용할 엔티티를 만듦
        Product product=new Product();
        product.setName("펜");
        product.setPrice(1000);
        product.setStock(1000);

        // when : saved 메서드를 호출해 테스트 진행
        Product savedProduct=productRepository.save(product);

        // then : 정상적인 테스트가 이뤄졌는지 체크하기 위해 save 메서드의 리턴 객체와 Given에서 생성한 엔티티 객체의 값이 일치하는지 검증
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getPrice(), savedProduct.getPrice());
        assertEquals(product.getStock(), savedProduct.getStock());
    }
}
