package com.myserver.api;

import com.myserver.api.config.DatabaseConfig;
import com.myserver.api.dao.ProductDAO;
import com.myserver.api.dao.impl.ProductDAOImpl;
import com.myserver.api.dto.products.ProductDTO;
import com.myserver.api.dto.products.SaveProductDTO;
import com.myserver.api.model.Product;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDAOImplTest {

    private static ProductDAO productDAO;
    private static int productId;
    private final int groupId = 1;

    @BeforeClass
    public static void beforeClass() {
        DatabaseConfig.connect();
        productDAO = new ProductDAOImpl();
    }

    @Test
    public void aSave() {
        final SaveProductDTO product = new SaveProductDTO();
        product.setName("Кус кус");
        product.setPrice(80.0);
        product.setCount(12);
        product.setDescription("Особливий");
        product.setProducer("Жменька");
        product.setGroupId(groupId);
        final ProductDTO productDTO = productDAO.save(product);
        productId = productDTO.getId();
        Assert.assertNotNull(productDTO);
    }

    @Test
    public void bFindAll() {
        final ProductDTO product = productDAO.findById(productId);
        productDAO.sell(productId, 2);
        final ProductDTO updatedProduct = productDAO.findById(productId);
        Assert.assertNotEquals(product.getCount(), updatedProduct.getCount());
    }

    @Test
    public void cFindAll() {
        final List<ProductDTO> products = productDAO.findAll();
        Assert.assertTrue(products.size() > 0);
    }

    @Test
    public void dUpdate() {
        final Product product = new Product(productId, "Авокадо", "Хаос", "Італія", 12, 19.80, groupId);
        productDAO.update(product);

        final ProductDTO productDTO = productDAO.findById(productId);
        Assert.assertEquals(productDTO.getName(), "Авокадо");
    }

    @Test
    public void eDelete() {
        productDAO.delete(productId);
        final ProductDTO product = productDAO.findById(productId);
        Assert.assertNull(product);
    }

    @AfterClass
    public static void afterClass() {
        DatabaseConfig.close();
    }
}
