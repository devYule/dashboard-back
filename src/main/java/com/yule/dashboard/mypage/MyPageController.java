package com.yule.dashboard.mypage;

import com.yule.dashboard.mypage.model.req.*;
import com.yule.dashboard.mypage.model.resp.ChangeMailKeyData;
import com.yule.dashboard.mypage.model.resp.ChangeProfPicData;
import com.yule.dashboard.pbl.BaseResponse;
import com.yule.dashboard.pbl.exception.ExceptionMessages;
import com.yule.dashboard.user.model.data.res.CheckPwData;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @GetMapping
    public GetAllUserInfoData getAllUserInfoData() {
        return myPageService.getAllUserInfoData();
    }

    @PutMapping("/prof")
    public ChangeProfPicData changeProfPic(@NotNull @RequestPart MultipartFile pic) {
        return myPageService.changeProfPic(pic);
    }

    @DeleteMapping("/prof")
    public BaseResponse removeProfPic() {
        return myPageService.removeProfPic();
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
    public BaseResponse checkMailCode(@RequestBody CheckMailData data) {
        return myPageService.checkMailCode(data);
    }

    @PutMapping("/pw")
    public BaseResponse changePw(@RequestBody ChangePwData data) {
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
    public BaseResponse addSite(
            @RequestBody SiteData data) {
        return myPageService.addSite(data);
    }

    @DeleteMapping("site")
    public BaseResponse removeSite(
            @Min(value = 0, message = ExceptionMessages.REQUEST_VALUE_RANGE_ERROR + "from 0 to 8")
            @Max(value = 8, message = ExceptionMessages.REQUEST_VALUE_RANGE_ERROR + "from 0 to 8")
            @RequestParam int id) {
        return myPageService.removeSite(id);
    }

}
