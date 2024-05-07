package com.yule.dashboard.mypage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yule.dashboard.entities.History;
import com.yule.dashboard.entities.Site;
import com.yule.dashboard.entities.UserSiteRank;
import com.yule.dashboard.entities.Users;
import com.yule.dashboard.entities.enums.BaseState;
import com.yule.dashboard.entities.enums.HistoryType;
import com.yule.dashboard.entities.enums.SiteType;
import com.yule.dashboard.mypage.model.DeleteUserVo;
import com.yule.dashboard.mypage.model.MailCheckInfo;
import com.yule.dashboard.mypage.model.req.*;
import com.yule.dashboard.mypage.model.resp.ChangeMailKeyData;
import com.yule.dashboard.mypage.model.resp.ChangeProfPicData;
import com.yule.dashboard.pbl.BaseResponse;
import com.yule.dashboard.pbl.exception.ClientException;
import com.yule.dashboard.pbl.exception.ExceptionCause;
import com.yule.dashboard.pbl.exception.ServerException;
import com.yule.dashboard.pbl.security.SecurityFacade;
import com.yule.dashboard.pbl.utils.FileUtils;
import com.yule.dashboard.pbl.utils.enums.FileCategory;
import com.yule.dashboard.pbl.utils.enums.FileKind;
import com.yule.dashboard.pbl.utils.enums.FileType;
import com.yule.dashboard.search.UserSiteRankRepository;
import com.yule.dashboard.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
    private final HistoryRepository historyRepository;
    private final MyPageRepository myPageRepository;
    private final UserRepository userRepository;
    private final SiteRepository siteRepository;
    private final SecurityFacade facade;
    private final FileUtils fileUtils;
    private final PasswordEncoder encoder;
    private final ObjectMapper om;


    @Transactional
    public ChangeProfPicData changeProfPic(MultipartFile pic) {
        Long id = facade.getId();
        Users findUser = userRepository.findById(id);
        FileUtils.FileSaveResult saveResult = fileUtils.save(pic, FileKind.PROFILE_PIC, FileCategory.USER, FileType.PIC, id);
        if (saveResult.movedPath() == null) {
            // 이 로직을 수행한다는 것은 프로필사진을 등록한적이 없다 == 회원가입 후 첫 프로필사진 변경이다.
            // 유저의 pic 에 path 를 savePath 로 변경해주어야 한다.
            historyRepository.saveHistory(findUser, HistoryType.PIC, null);
        }
        if (saveResult.movedPath() != null) {
            // history 에 사진 변경 이력 저장
            // 유저의 pic 의 path 는 이미 savePath 로 저장되어 있다.
            savePicHistory(findUser, saveResult);
        }
        findUser.setPic(saveResult.savedPath());
        return new ChangeProfPicData(saveResult.savedPath());
    }


    @Transactional
    public BaseResponse removeProfPic() {
        Users findUser = userRepository.findById(facade.getId());
        savePicHistory(findUser, fileUtils.remove(findUser.getPic()));
        findUser.setPic(null);
        return new BaseResponse();
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
        // 캐시 삭제
        myPageRepository.delete(data.key());

        // 유저 메일 변경
        Users findUser = userRepository.findById(facade.getId());
        String prevMail = findUser.getMail();
        findUser.setMail(findInfo.getMail());

        // 히스토리 추가
        historyRepository.saveHistory(findUser, historyRepository.findPrevHistory(findUser, HistoryType.MAIL), HistoryType.MAIL, prevMail);
        return new BaseResponse();
    }

    @Transactional
    public BaseResponse changePw(ChangePwData data) {
        Users findUser = userRepository.findById(facade.getId());
        if (!encoder.matches(data.prevPw(), findUser.getPw())) throw new ClientException(ExceptionCause.PW_NOT_MATCHES);
        findUser.setPw(encoder.encode(data.pw()));

        historyRepository.saveHistory(findUser, historyRepository.findPrevHistory(findUser, HistoryType.PW), HistoryType.PW,
                null);

        return new BaseResponse();
    }

    @Transactional
    public BaseResponse withDraw() {
        Users findUser = userRepository.findById(facade.getId());


        String stringify;
        try {
            stringify = om.writeValueAsString(new DeleteUserVo(findUser.getId(), findUser.getLoginId(), findUser.getNick(),
                    findUser.getMail()
                    , findUser.getPic(), findUser.getSearchbar()));
        } catch (JsonProcessingException e) {
            throw new ServerException(e);
        }

        historyRepository.saveHistory(findUser,
                historyRepository.findPrevHistory(findUser, HistoryType.WITHDRAW), HistoryType.WITHDRAW,
                stringify);

        findUser.setState(BaseState.DEACTIVATED);
        findUser.setLoginId(null);
        findUser.setNick(null);
        findUser.setMail(null);

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
                siteRepository.findByUserAndSite(findUser, SiteType.getByValue(data.site()));
        try {
            if (findSite == null) {
                UserSiteRank rank = UserSiteRank.builder()
                        .user(findUser)
                        .build();
                Site saveSite = new Site(SiteType.getByValue(data.site()), rank);
                saveSite.setState(BaseState.ACTIVATED);
                saveSite.setUser(findUser);
                return new BaseResponse((long) siteRepository.save(saveSite).getSite().getValue());
            }
            if (findSite.getState().equals(BaseState.DEACTIVATED)) {
                findSite.setState(BaseState.ACTIVATED);
                return new BaseResponse((long) findSite.getSite().getValue());
            }
            throw new ClientException(ExceptionCause.ID_IS_ALREADY_EXISTS);
        } finally {
            historyRepository.saveHistory(findUser, historyRepository.findPrevHistory(findUser, HistoryType.SITE), HistoryType.SITE,
                    "add:" + SiteType.getByValue(data.site()).name());
        }
    }

    @Transactional
    public BaseResponse removeSite(int siteId) {
        Users findUser = userRepository.findById(facade.getId());
        Site findSite = siteRepository.findByUserAndStateAndSite(findUser, BaseState.ACTIVATED, SiteType.getByValue(siteId));
        if (findSite == null) throw new ClientException(ExceptionCause.PRIMARY_KEY_IS_NOT_VALID);
        findSite.setState(BaseState.DEACTIVATED);
        historyRepository.saveHistory(findUser, historyRepository.findPrevHistory(findUser, HistoryType.SITE), HistoryType.SITE,
                "remove:" + SiteType.getByValue(siteId).name());
        return new BaseResponse((long) findSite.getSite().getValue());
    }

    @Transactional
    public GetAllUserInfoData getAllUserInfoData() {
        Users findUser = userRepository.findById(facade.getId());
        List<Site> findSties = siteRepository.findByUserIdAndState(facade.getId(), BaseState.ACTIVATED);

        return new GetAllUserInfoData(
                findUser.getPic(), findUser.getNick(), findUser.getMail(),
                findSties.stream().map(s -> s.getSite().getValue()).toList()
        );

//        log.info("sites: {}", findUser.getSites().toString());
//        return new GetAllUserInfoData(
//                findUser.getPic(), findUser.getNick(), findUser.getMail(),
//                findUser.getSites().stream().map(s -> s.getSite().getValue()).toList()
//        );
    }

    /* --- extracted methods --- */

    /**
     * 사진 history 저장용 호출 위임 메소드
     */
    private void savePicHistory(Users findUser, FileUtils.FileSaveResult saveResult) {
        History findPrevHistory = historyRepository.findFirstByUserAndType(findUser, HistoryType.PIC,
                Sort.by(Sort.Direction.DESC, "id"));
        // request saveHistory
        historyRepository.saveHistory(findUser, findPrevHistory, HistoryType.PIC, saveResult.movedPath());
    }


}
