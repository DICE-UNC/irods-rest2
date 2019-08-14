package org.irods.rest.api;

import java.io.File;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileBytesApiControllerIntegrationTest {

    @Autowired
    private FileBytesApi api;

    @Test
    public void fileBytesTest() throws Exception {
        String path = "path_example";
        Integer offset = 56;
        Integer limit = 56;
        ResponseEntity<File> responseEntity = api.fileBytes(path, offset, limit);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

}
