package com.yule.dashboard.mypage;

import com.yule.dashboard.mypage.model.req.SiteData;
import com.yule.dashboard.mypage.model.req.ChangeMailData;
import com.yule.dashboard.mypage.model.req.ChangeNickData;
import com.yule.dashboard.mypage.model.req.CheckMailData;
import com.yule.dashboard.mypage.model.resp.ChangeMailKeyData;
import com.yule.dashboard.mypage.model.resp.ChangeProfPicData;
import com.yule.dashboard.pbl.BaseResponse;
import com.yule.dashboard.user.model.data.res.CheckPwData;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
@Slf4j
public class MyPageController {
    private final MyPageService myPageService;

    @PutMapping("/prof")
    public ChangeProfPicData changeProfPic(@NotNull @RequestPart MultipartFile pic) {
        return myPageService.changeProfPic(pic);
    }

    @PutMapping("/nick")
    public BaseResponse changeNick(@RequestBody ChangeNickData data) {
        return myPageService.changeNick(data);
    }

    @PutMapping("/mail")
    public ChangeMailKeyData changeMail(@RequestBody ChangeMailData data) {
        return myPageService.changeMail(data);
    }

    @PostMapping("/mail")
    public BaseResponse checkMail(@RequestBody CheckMailData data) {
        return myPageService.checkMailCode(data);
    }

    @PutMapping("/pw")
    public BaseResponse changePw(@RequestBody CheckPwData data) {
        return myPageService.changePw(data);
    }

    @DeleteMapping
    public BaseResponse withDraw() {
        return myPageService.withDraw();
    }

    @GetMapping("site")
    public List<BaseResponse> getSiteList() {
        return myPageService.getSiteList();
    }

    @PostMapping("site")
    public BaseResponse addSite(@RequestBody SiteData data) {
        return myPageService.addSite(data);
    }

    @DeleteMapping("site")
    public BaseResponse removeSite(@RequestParam SiteData data) {
        return myPageService.removeSite(data);
    }
}
