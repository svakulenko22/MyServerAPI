package com.myserver.api;

import com.myserver.api.config.DatabaseConfig;
import com.myserver.api.dao.GroupDAO;
import com.myserver.api.dao.ProductDAO;
import com.myserver.api.dao.impl.GroupDAOImpl;
import com.myserver.api.dao.impl.ProductDAOImpl;
import com.myserver.api.dto.products.ProductDTO;
import com.myserver.api.dto.products.SaveProductDTO;
import com.myserver.api.model.Group;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupDAOImplTest {

    private static GroupDAO groupDao;
    private static ProductDAO productDAO;
    private static int groupId;

    @BeforeClass
    public static void beforeClass() {
        DatabaseConfig.connect();
        groupDao = new GroupDAOImpl();
        productDAO = new ProductDAOImpl();
    }

    @Test
    public void aSave() {
        final Group group = groupDao.save("Еко test");
        groupId = group.getId();

        final SaveProductDTO product = new SaveProductDTO();
        product.setName("Кус кус test");
        product.setPrice(80.0);
        product.setCount(12);
        product.setDescription("Особливий");
        product.setProducer("Жменька");
        product.setGroupId(groupId);
        final ProductDTO productDTO = productDAO.save(product);

        Assert.assertNotNull(productDTO);
        Assert.assertNotNull(group);
    }

    @Test
    public void bUpdate() {
        final Group group = new Group();
        group.setId(groupId);
        group.setName("Еко еко");
        groupDao.update(group);
    }

    @Test
    public void cFindAll() {
        final List<Group> groups = groupDao.findAll();
        final List<ProductDTO> productsByGroup = productDAO.findByNameAndGroup("", groupId);

        Assert.assertTrue(groups.size() > 0);
        Assert.assertTrue(productsByGroup.size() > 0);
    }

    @Test
    public void dExists() {
        final boolean exists = groupDao.exists("Еко еко", 0);
        Assert.assertTrue(exists);
    }

    @Test
    public void fDelete() {
        groupDao.delete(groupId);
        final boolean exists = groupDao.exists("Еко еко", 0);
        final List<ProductDTO> productsByGroup = productDAO.findByNameAndGroup("", groupId);

        Assert.assertFalse(exists);
        Assert.assertEquals(0, productsByGroup.size());
    }

    @AfterClass
    public static void afterClass() {
        DatabaseConfig.close();
    }
}
