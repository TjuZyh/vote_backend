package com.tju.bclab.vote_backend.service.impl;

import com.tju.bclab.vote_backend.service.OssService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/** 
* OssServiceImpl Tester. 
* 
* @author 
* @since 11/30/2021
*/ 
@RunWith(SpringRunner.class)
@SpringBootTest
public class OssServiceImplTest {
    @Autowired
    private OssService ossService;

    /**
     * Method: uploadImg(MultipartFile file)
     */
    @Test
    public void testUploadImg() {
        //test位于src/main/java/com/tju/bclab/vote_backend/controller/MainController.java
        //中的addVoteTest部分
    }
}


