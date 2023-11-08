package com.viethung.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFormDto {
    private UUID id ;

    @NotBlank(message = "Mã không được trống")
    private String code ;

    @NotBlank(message = "Tên không được trống")
    private String name ;

//    @Size(min = 3,max = 3,message = "Bạn phải có 3 ảnh")
    @Size(max = 3,message = "Bạn chỉ có thể tải tối đa 3 ảnh")
    private List<MultipartFile> images ;

    private List<String> imageUrls ;

    @NotBlank(message = "Vui lòng chọn danh mục")
    private String categoryId ;

    @DecimalMin(value = "0.1",message = "Giá bán phải lớn hơn 0")
    private BigDecimal price ;

    @DecimalMin(value = "0.1",message = "Giá nhập phải lớn hơn 0")
    private BigDecimal cost ;

    @NotNull(message = "Vui lòng chọn trạng thái")
    private Integer status;

    private Float weight ;

    private String description ;
}
