package com.tju.bclab.vote_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.tju.bclab.vote_backend.entity.User;
import com.tju.bclab.vote_backend.entity.Vote;
import com.tju.bclab.vote_backend.entity.VoteOption;
import com.tju.bclab.vote_backend.entity.Voteboxuser;
import com.tju.bclab.vote_backend.io.aelf.utils.QRCodeUtil;
import com.tju.bclab.vote_backend.mapper.UserMapper;
import com.tju.bclab.vote_backend.mapper.VoteMapper;
import com.tju.bclab.vote_backend.mapper.VoteOptionMapper;
import com.tju.bclab.vote_backend.mapper.VoteboxuserMapper;
import com.tju.bclab.vote_backend.service.OssService;
import com.tju.bclab.vote_backend.service.PdfService;
import com.tju.bclab.vote_backend.vo.common.BoxVoteQRcode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private VoteboxuserMapper voteboxuserMapper;

    @Autowired
    private VoteOptionMapper voteOptionMapper;

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OssService ossService;

    @Value("${pdf.path}")
    private String path;

    @Override
    public String createPdf(String voteId) throws Exception {
        File dir = new File(path);
        if (!dir.exists()) {
            createDir(path.substring(0, path.length() - 1));
        }

        QueryWrapper<VoteOption> voteOptionQueryWrapper = new QueryWrapper<>();
        voteOptionQueryWrapper.eq("vote_id", voteId);
        List<VoteOption> voteOptionList = voteOptionMapper.selectList(voteOptionQueryWrapper);
        QueryWrapper<Voteboxuser> voteQueryWrapper = new QueryWrapper<>();
        voteQueryWrapper.eq("vote_id", voteId);
        List<Voteboxuser> voteUserList = voteboxuserMapper.selectList(voteQueryWrapper);
        createQRcode(voteId, voteUserList, voteOptionList);

        int userSum = voteUserList.size();
        int optionSum = voteOptionList.size();

        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(baseFont, 12, Font.NORMAL);
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path + voteId + ".pdf"));
        document.open();

        Vote vote = voteMapper.selectById(voteId);

        for (int i = 0; i < userSum; i++) {
            document.newPage();
            Paragraph title = new Paragraph(vote.getTitle(), font);
            title.setSpacingAfter(5);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            User user = userMapper.selectById(voteUserList.get(i).getUserId());
            String name = user.getNickName() == null ? "无名氏" : user.getNickName();
            Paragraph userName = new Paragraph(name, font);
            userName.setSpacingAfter(5);
            userName.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(userName);

            for (int j = 0; j < optionSum; j++) {
                VoteOption voteOption = voteOptionList.get(j);
                String desc = voteOption.getOptionStr() == null ? "" : voteOption.getOptionStr();
                Paragraph optionDesc = new Paragraph(j + 1 + "、" + desc, font);
                optionDesc.setSpacingAfter(5);
                document.add(optionDesc);

                Image jpeg = Image.getInstance(path + voteId + "/" + user.getUserId() + "/" + j + ".jpg");
                jpeg.setAlignment(Image.MIDDLE);
                if (j > 0 && j % 3 == 0) {
                    document.newPage();
                    document.add(title);
                    document.add(userName);
                }
                document.add(jpeg);
            }
        }

        document.close();

        FileItem fileItem = createFileItem(path + voteId + ".pdf");
        MultipartFile mfile = new CommonsMultipartFile(fileItem);
        String url = ossService.uploadPdf(mfile);
        FileUtils.deleteDirectory(new File(path + voteId));
        return url;
    }

    public void createQRcode(String voteId, List<Voteboxuser> voteUserList, List<VoteOption> voteOptionList) throws Exception {
        QRCodeUtil qrCodeUtil = new QRCodeUtil();
        createDir(path + voteId);
        List<BoxVoteQRcode> boxVoteQRcodes = makeOptionStr(voteOptionList);
        for (int i = 0; i < voteUserList.size(); i++) {
            String userId = voteUserList.get(i).getUserId();
            createDir(path + voteId + "/" + userId);
            for (int j = 0; j < boxVoteQRcodes.size(); j++) {
                BoxVoteQRcode boxVoteQRcode = boxVoteQRcodes.get(j);
                boxVoteQRcode.setUserId(userId);
                BufferedImage image = qrCodeUtil.createImage(boxVoteQRcode.toString(), false);
                File outputfile = new File(path + voteId + "/" + userId + "/" + j + ".jpg");
                ImageIO.write(image, "jpg", outputfile);
            }
        }

    }

    public void createDir(String dirPath) {
        File dir = new File(dirPath);
        dir.mkdir();
    }

    public List<BoxVoteQRcode> makeOptionStr(List<VoteOption> voteOptions) {
        List<BoxVoteQRcode> boxVoteQRcodes = new ArrayList<>();
        for (int i = 0; i < voteOptions.size(); i++) {
            BoxVoteQRcode boxVoteQRcode = new BoxVoteQRcode();
            boxVoteQRcode.setVoteId(voteOptions.get(i).getVoteId());
            boxVoteQRcode.setOptionStr(voteOptions.get(i).getOptionStr());
            boxVoteQRcode.setOptionId(voteOptions.get(i).getOptionId());
            boxVoteQRcode.setOptionIndex(voteOptions.get(i).getOptionIndex());
            boxVoteQRcode.setGmtCreate(voteOptions.get(i).getGmtCreate());
            boxVoteQRcodes.add(boxVoteQRcode);
        }
        return boxVoteQRcodes;
    }

    public FileItem createFileItem(String filePath) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "textField";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "text/plain", true, "MyFileName");
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(newfile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

}
