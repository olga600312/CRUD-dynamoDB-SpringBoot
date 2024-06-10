package com.crudDynamo.CrudDynamo.service.Impl;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import com.crudDynamo.CrudDynamo.model.Product;
import com.crudDynamo.CrudDynamo.model.ProductDTO;
import com.crudDynamo.CrudDynamo.service.BL.ProductServiceBL;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductServiceBL {

    private final DynamoDBMapper dynamoDBMapper;

    public ProductServiceImpl(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Get whole list of products
     * @return product list
     */
    @Override
    public List<ProductDTO> getAllProducts() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Product> products = dynamoDBMapper.scan(Product.class, scanExpression);
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Fetch aproduct by its ID
     * @param id Product id
     * @return product dto
     */
    @Override
    public ProductDTO getProductById(String id) {
        Product product = dynamoDBMapper.load(Product.class, id);
        return convertToDTO(product);
    }

    @Override
    public ProductDTO createNewProduct(ProductDTO dto) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product);

        dynamoDBMapper.save(product);

        return convertToDTO(product);
    }

    @Override
    public ProductDTO updateProduct(String id, ProductDTO dto) {
        Product product = dynamoDBMapper.load(Product.class, id);
        BeanUtils.copyProperties(dto, product);

        dynamoDBMapper.save(product);

        return convertToDTO(product);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = dynamoDBMapper.load(Product.class, id);
        if (product != null) {
            dynamoDBMapper.delete(product);
        }
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(product, dto);
        return dto;
    }
}
