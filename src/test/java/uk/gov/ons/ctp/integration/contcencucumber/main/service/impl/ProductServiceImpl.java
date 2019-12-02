package uk.gov.ons.ctp.integration.contcencucumber.main.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.ProductReference;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.contcencucumber.main.service.ProductService;

@Service
@Validated()
public class ProductServiceImpl implements ProductService {
  @Autowired ProductReference productReference;

  @Override
  public List<Product> getProducts() throws CTPException {
    Product example = new Product();
    return productReference.searchProducts(example);
  }
}
