package com.yule.dashboard.mypage;

import com.yule.dashboard.entities.History;
import com.yule.dashboard.entities.Site;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.HistoryType;
import com.yule.dashboard.entities.enums.SiteType;
import com.yule.dashboard.mypage.model.MailCheckInfo;
import com.yule.dashboard.mypage.model.req.SiteData;
import com.yule.dashboard.mypage.model.req.ChangeMailData;
import com.yule.dashboard.mypage.model.req.ChangeNickData;
import com.yule.dashboard.mypage.model.req.CheckMailData;
import com.yule.dashboard.mypage.model.resp.ChangeMailKeyData;
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
import com.yule.dashboard.user.model.data.res.CheckPwData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final HistoryRepository historyRepository;
    private final MyPageRepository myPageRepository;
    private final UserRepository userRepository;
    private final SiteRepository siteRepository;
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
            historyRepository.saveHistory(findUser, HistoryType.PIC, null);
        }
        if (saveResult.movedPath() != null) {
            // history 에 사진 변경 이력 저장
            // 유저의 pic 의 path 는 이미 savePath 로 저장되어 있다.
            History findPrevHistory = historyRepository.findFirstByUserAndType(findUser, HistoryType.PIC,
                    Sort.by(Sort.Direction.DESC, "id"));
            historyRepository.saveHistory(findUser, findPrevHistory, HistoryType.PIC, saveResult.movedPath());

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
        History prevHistory = historyRepository.findPrevHistory(findUser, HistoryType.NICK);
        historyRepository.saveHistory(findUser, prevHistory, HistoryType.NICK, prevNick);

        return new BaseResponse();
    }


    /**
     * 메일인증은 회원가입과 별개의 로직들이 존재한다.
     * ( 예컨데 회원가입은 메일이 회원정보라는 데이터 안에 포함되고, 인증완료시에는 로그인까지 곧바로 진행, 즉 at를 발급하게 된다. )
     * 따라서 메일 체크로직과는 별개의 요청으로 분리한다.
     * <p>
     * 메일은 redisTokenAndMailRepository 에 토큰과 함께 저장되며,
     * RedisDataType.TOKEN (value = tk:) , RedisDataType.MAIL (value = m:) 로 구분된 prefix 로 구별된다.
     */
    public ChangeMailKeyData changeMail(ChangeMailData data) {
        if (userRepository.existsByMail(data.mail())) {
            throw new ClientException(ExceptionCause.MAIL_IS_ALREADY_EXISTS);
        }
        return new ChangeMailKeyData(myPageRepository.saveMailCode(data.mail()));
    }

    @Transactional
    public BaseResponse checkMailCode(CheckMailData data) {
        MailCheckInfo findInfo = myPageRepository.getMailCheckInfo(data.key());
        if (!findInfo.getCode().equals(data.code())) {
            throw new ClientException(ExceptionCause.AUTH_CODE_IS_NOT_MATCHES);
        }
        // 유저 메일 변경
        Users findUser = userRepository.findById(facade.getId());
        String prevMail = findUser.getMail();
        findUser.setMail(findInfo.getMail());

        // 히스토리 추가
        historyRepository.saveHistory(findUser, historyRepository.findPrevHistory(findUser, HistoryType.MAIL), HistoryType.MAIL, prevMail);
        return new BaseResponse();
    }

    @Transactional
    public BaseResponse changePw(CheckPwData data) {
        Users findUser = userRepository.findById(facade.getId());
        String prevPw = findUser.getPw();
        findUser.setPw(data.pw());

        historyRepository.saveHistory(findUser, historyRepository.findPrevHistory(findUser, HistoryType.PW), HistoryType.PW, prevPw);

        return new BaseResponse();
    }

    @Transactional
    public BaseResponse withDraw() {
        Users findUser = userRepository.findById(facade.getId());
        userRepository.delete(findUser);

        historyRepository.saveHistory(findUser, HistoryType.WITHDRAW);
        return new BaseResponse();
    }


    public List<BaseResponse> getSiteList() {
        return siteRepository.findByUserIdAndState(facade.getId(), BaseState.ACTIVATED)
                .stream()
                .map(s -> new BaseResponse((long) s.getSite().getValue()))
                .toList();
    }


    @Transactional
    public BaseResponse addSite(SiteData data) {
        Users findUser = userRepository.findById(facade.getId());
        Site findSite =
                siteRepository.findByUserAndStateAndSite(findUser, BaseState.DEACTIVATED, SiteType.getByValue(data.site()));
        try {
            if (findSite == null) {
                Site saveSite = new Site(SiteType.getByValue(data.site()));
                return new BaseResponse((long) siteRepository.save(saveSite).getSite().getValue());
            }
            findSite.setState(BaseState.ACTIVATED);
            return new BaseResponse((long) findSite.getSite().getValue());
        } finally {
            historyRepository.saveHistory(findUser, historyRepository.findPrevHistory(findUser, HistoryType.SITE), HistoryType.SITE,
                    "add:" + SiteType.getByValue(data.site()).name());
        }
    }

    @Transactional
    public BaseResponse removeSite(SiteData data) {
        Users findUser = userRepository.findById(facade.getId());
        Site findSite = siteRepository.findByUserAndStateAndSite(findUser, BaseState.ACTIVATED, SiteType.getByValue(data.site()));
        if (findSite == null) throw new ClientException(ExceptionCause.PRIMARY_KEY_IS_NOT_VALID);
        findSite.setState(BaseState.DEACTIVATED);
        historyRepository.saveHistory(findUser, historyRepository.findPrevHistory(findUser, HistoryType.SITE), HistoryType.SITE,
                "remove:" + SiteType.getByValue(data.site()).name());
        return new BaseResponse((long) findSite.getSite().getValue());
    }
}
