package com.tju.bclab.vote_backend.controller;

import com.tju.bclab.vote_backend.common.R;
import com.tju.bclab.vote_backend.service.impl.BlockChainServiceImpl;
import com.tju.bclab.vote_backend.service.impl.OssServiceImpl;
import com.tju.bclab.vote_backend.service.impl.PdfServiceImpl;
import com.tju.bclab.vote_backend.vo.resp.VerifyVoteResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/vote_backend/main")
@Api(tags = {"主控制器"})
@CrossOrigin //解决跨域
public class MainController {
    @Autowired
    private OssServiceImpl ossService;

    @Autowired
    private PdfServiceImpl pdfService;

    @Autowired
    private BlockChainServiceImpl blockChainService;

    @ApiOperation(value = "上传图片文件")
    @PostMapping("/uploadImg")
    public R<String> addVoteTest(@RequestBody MultipartFile file) {
        return R.ok(ossService.uploadImg(file));
    }


    @ApiOperation(value = "导出pdf")
    @GetMapping("/creatPdf/{voteId}")
    public R creatPdf(@PathVariable("voteId") String voteId) throws Exception {
        String pdfUrl = pdfService.createPdf(voteId);
        return R.ok(pdfUrl);
    }

    @ApiOperation(value = "扫链验证投票数据正确")
    @GetMapping("/verifyVote")
    public R<VerifyVoteResp> QueryMultiNodes(String transactionId) throws Exception {
        VerifyVoteResp result = blockChainService.queryMultiNodes(transactionId);
        return R.ok(result);
    }
}
