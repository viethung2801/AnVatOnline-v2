package com.viethung.service;

import com.viethung.dto.ProductFormDto;
import com.viethung.dto.ProductListDto;
import com.viethung.entity.Category;
import com.viethung.entity.ENUM.EProductStatus;
import com.viethung.entity.Product;
import com.viethung.entity.ProductImage;
import com.viethung.repository.CategoryRepository;
import com.viethung.repository.ProductImageRepository;
import com.viethung.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UploadFileServiceImpl uploadFileService;

    @Autowired
    private ProductImageRepository productImageRepository;

    public Page<ProductListDto> findProducts(Pageable pageable) {

        Page<Product> products = productRepository.findAll(pageable);
        List<ProductListDto> productListDtos = new ArrayList<>();
        long total = productRepository.count();
        products.forEach(product -> {
            productListDtos.add(mapProductToProductListDto(product));
        });
        return new PageImpl<ProductListDto>(productListDtos, pageable, total);
    }

    public Page<ProductListDto> searchProducts(String keys, Pageable pageable) {
        keys = "%" + keys + "%";
        Page<Product> products = productRepository.searchAllByCodeLikeOrNameLike(keys, keys, pageable);
        List<ProductListDto> productListDtos = new ArrayList<>();
        long total = products.getSize();
        products.forEach(product -> {
            productListDtos.add(mapProductToProductListDto(product));
        });
        return new PageImpl<ProductListDto>(productListDtos, pageable, total);
    }

    public ProductFormDto findProductFormById(UUID id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ProductFormDto.builder().build();
        }
        //mapper
        return mapProductToProductFormDto(product);
    }

    public boolean deleteById(UUID id) {
        try {
            productRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean handleAdd(ProductFormDto productFormDto) {
        try {
            Product product = mapProductFormDtoToProduct(productFormDto);
            //craeted date
            product.setCreatedDate(LocalDateTime.now());

            //category
            product.setCategory(Category.builder().id(UUID.fromString(productFormDto.getCategoryId())).build());
            // save products
            product = productRepository.save(product);

            //save image
            Product finalProduct = product;
            productFormDto.getImages().forEach(multipartFile -> {
                ProductImage productImage = ProductImage.builder().build();
                productImage.setUrl(uploadFileService.handleUpload(multipartFile));
                productImage.setProduct(finalProduct);
                productImageRepository.save(productImage);
            });

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean handleUpdate(ProductFormDto productFormDto) {
        try {
            Product product = productRepository.findById(productFormDto.getId()).orElse(null);
            if (product == null) {
                return false;
            }
            //update field
            product.setCode(productFormDto.getCode());
            product.setName(productFormDto.getName());
            product.setDescription(productFormDto.getDescription());
            product.setStatus(productFormDto.getStatus() == 1 ? EProductStatus.ACTIVE : EProductStatus.IN_ACTIVE);
            product.setPrice(productFormDto.getPrice());
            product.setCost(productFormDto.getCost());
            product.setWeight(productFormDto.getWeight());

            //update category
            product.setCategory(Category.builder().id(UUID.fromString(productFormDto.getCategoryId())).build());


            //update image
            if (productFormDto.getImages().size() == 3) {
                for (int i = 0; i < product.getProductImages().size(); i++) {

                    MultipartFile multipartFile = productFormDto.getImages().get(i);

                    if (multipartFile != null) {
                        ProductImage productImage = product.getProductImages().get(i);
                        productImage.setUrl(uploadFileService.handleUpload(multipartFile));
                        productImageRepository.save(productImage);
                    }
                }
            }

            // update products
            productRepository.save(product);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByCode(String code) {
        return productRepository.existsByCode(code);
    }

    public long countByCodeAndIdNot(String code, UUID id) {
        return productRepository.countByCodeAndIdNot(code, id);
    }

    public String generateCode() {
        long num = productRepository.count();
        while (true) {
            if (productRepository.existsByCode("SP0" + num)) {
                num++;
            }
            break;
        }
        return "SP0" + num;
    }

    public Product mapProductFormDtoToProduct(ProductFormDto productFormDto) {
        Product product = Product.builder().build();

        product.setId(productFormDto.getId());
        product.setCode(productFormDto.getCode());
        product.setName(productFormDto.getName());
        product.setDescription(productFormDto.getDescription());
        product.setStatus(productFormDto.getStatus() == 1 ? EProductStatus.ACTIVE : EProductStatus.IN_ACTIVE);
        product.setPrice(productFormDto.getPrice());
        product.setCost(productFormDto.getCost());
        product.setWeight(productFormDto.getWeight());

        return product;
    }

    public ProductListDto mapProductToProductListDto(Product product) {
        ProductListDto productListDto = ProductListDto.builder().build();

        //mapper
        productListDto.setId(product.getId().toString());
        productListDto.setImageUrl(product.getProductImages().get(0).getUrl());
        productListDto.setCode(product.getCode());
        productListDto.setName(product.getName());
        productListDto.setCategoryName(product.getCategory() == null ? "" : product.getCategory().getName());
        productListDto.setStatus(product.getStatus() == EProductStatus.ACTIVE ? 1 : 0);
        productListDto.setPrice(product.getPrice().toString());
        productListDto.setCreatedDate(product.getCreatedDate());

        return productListDto;
    }

    public ProductFormDto mapProductToProductFormDto(Product product) {
        ProductFormDto productFormDto = ProductFormDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .images(null)
                .imageUrls(List.of(
                        product.getProductImages().get(0).getUrl(),
                        product.getProductImages().get(1).getUrl(),
                        product.getProductImages().get(2).getUrl()
                ))
                .categoryId(product.getCategory() == null ? "" : product.getCategory().getId().toString())
                .price(product.getPrice())
                .cost(product.getCost())
                .status(product.getStatus() == EProductStatus.ACTIVE ? 1 : 0)
                .weight(product.getWeight())
                .description(product.getDescription())
                .build();


        return productFormDto;
    }
}
