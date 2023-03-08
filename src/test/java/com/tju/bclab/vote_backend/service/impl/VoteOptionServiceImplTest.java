package com.tju.bclab.vote_backend.service.impl;

import com.tju.bclab.vote_backend.entity.VoteOption;
import com.tju.bclab.vote_backend.mapper.VoteOptionMapper;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * VoteOptionServiceImpl Tester.
 *
 * @author 张辰宇
 * @since 11/30/2021
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VoteOptionServiceImplTest {

    @Autowired
    VoteOptionMapper voteOptionMapper;

    @Autowired
    VoteOptionServiceImpl voteOptionService;

    public static VoteOption[] expected;
    @BeforeClass
    public static void BeforeClass() throws ParseException {
        SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        expected = new VoteOption[2];

        expected[0] = new VoteOption();
        expected[0].setOptionId("1465546403460218882");
        expected[0].setVoteId("1465546402604580866");
        expected[0].setOptionStr("12312");
        expected[0].setOptionIndex(0);
        expected[0].setGmtCreate(dateParse.parse("2021-11-30 13:01:06"));
        expected[0].setGmtModified(dateParse.parse("2021-11-30 13:01:06"));
        expected[0].setImgUrl("../../imgs/添加图片.png");
        expected[0].setCount(0);

        expected[1] = new VoteOption();
        expected[1].setOptionId("1465546409827172353");
        expected[1].setVoteId("1465546402604580866");
        expected[1].setOptionStr("11111");
        expected[1].setOptionIndex(1);
        expected[1].setGmtCreate(dateParse.parse("2021-11-30 13:01:07"));
        expected[1].setGmtModified(dateParse.parse("2021-11-30 13:01:15"));
        expected[1].setImgUrl("../../imgs/添加图片.png");
        expected[1].setCount(1);
    }

    /**
     * Method: qryVoteOptionsByVoteId(String voteId)
     */
    @Test
    public void testQryVoteOptionsByVoteId() {
        String voteId = "1465546402604580866";
        List<VoteOption> actual = voteOptionService.qryVoteOptionsByVoteId(voteId);
        assertArrayEquals(expected, actual.toArray());
    }

    /**
     * Method: qryVoteOptionsByOptionId(String optionId)
     */
    @Test
    public void testQryVoteOptionsByOptionId() {
        String optionId = "1465546403460218882";
        List<VoteOption> actual = voteOptionService.qryVoteOptionsByOptionId(optionId);
        assertEquals(expected[0], actual.get(0));
    }


}
