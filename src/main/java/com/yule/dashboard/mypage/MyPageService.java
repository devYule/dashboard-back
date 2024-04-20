package com.yule.dashboard.mypage;

import com.yule.dashboard.entities.History;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.HistoryType;
import com.yule.dashboard.mypage.model.req.ChangeNickData;
import com.yule.dashboard.mypage.model.resp.ChangeProfPicData;
import com.yule.dashboard.pbl.BaseResponse;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.pbl.security.SecurityFacade;
import com.yule.dashboard.pbl.utils.FileUtils;
import com.yule.dashboard.pbl.utils.enums.FileCategory;
import com.yule.dashboard.pbl.utils.enums.FileKind;
import com.yule.dashboard.pbl.utils.enums.FileType;
import com.yule.dashboard.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final SecurityFacade facade;

    @Transactional
    public ChangeProfPicData changeProfPic(MultipartFile pic) {
        Long id = facade.getId();
        Users findUser = userRepository.findById(id);
        FileUtils.FileSaveResult saveResult = new FileUtils(FileCategory.USER, FileType.PIC, id).save(pic, FileKind.PROFILE_PIC);
        if (saveResult.movedPath() == null) {
            // 이 로직을 수행한다는 것은 프로필사진을 등록한적이 없다 == 회원가입 후 첫 프로필사진 변경이다.
            // 유저의 pic 에 path 를 savePath 로 변경해주어야 한다.
            findUser.setPic(saveResult.savedPath());
            saveHistory(findUser, HistoryType.PIC, null);
        }
        if (saveResult.movedPath() != null) {
            // history 에 사진 변경 이력 저장
            // 유저의 pic 의 path 는 이미 savePath 로 저장되어 있다.
            History findPrevHistory = historyRepository.findFirstByUserAndType(findUser, HistoryType.PIC,
                    Sort.by(Sort.Direction.DESC, "id"));
            saveHistory(findUser, findPrevHistory, HistoryType.PIC, saveResult.movedPath());

        }
        return new ChangeProfPicData(saveResult.savedPath());
    }


    @Transactional
    public BaseResponse changeNick(ChangeNickData data) {
        if (userRepository.existsByNick(data.nick())) {
            throw new ClientException(ExceptionCause.NICK_IS_ALREADY_EXISTS);
        }
        Users findUser = userRepository.findById(facade.getId());
        String prevNick = findUser.getNick();
        findUser.setNick(data.nick());
        // history 추가
        History prevHistory = historyRepository.findFirstByUserAndType(findUser, HistoryType.NICK, Sort.by(Sort.Direction.DESC, "id"));

        saveHistory(findUser, prevHistory, HistoryType.NICK, prevNick);

        return new BaseResponse();
    }



    /* --- extracted Methods --- */

    private History saveHistory(Users user, History prevHistory, HistoryType type, String value) {
        return historyRepository.save(History.builder()
                .user(user)
                .prev(prevHistory)
                .type(type)
                .value(value)
                .build());
    }

    private History saveHistory(Users user, HistoryType type, String value) {
        return saveHistory(user, null, type, value);
    }
}
