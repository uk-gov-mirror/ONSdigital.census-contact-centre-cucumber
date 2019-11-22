package uk.gov.ons.ctp.integration.contcencucumber.main.service;

import java.util.List;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.model.Product;

public interface ProductService {
  List<Product> getProducts() throws CTPException;
}
