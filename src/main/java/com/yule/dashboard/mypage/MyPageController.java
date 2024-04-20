package com.yule.dashboard.mypage;

import com.yule.dashboard.mypage.model.req.ChangeNickData;
import com.yule.dashboard.mypage.model.resp.ChangeProfPicData;
import com.yule.dashboard.pbl.BaseResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public BaseResponse changeNick(@RequestBody ChangeNickData data){
        return myPageService.changeNick(data);
    }
}
