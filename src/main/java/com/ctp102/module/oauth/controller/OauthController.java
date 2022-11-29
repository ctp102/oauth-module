package com.ctp102.module.oauth.controller;

import com.ctp102.module.oauth.core.domain.KakaoOauthAuthorizeResponseDomain;
import com.ctp102.module.oauth.core.domain.KakaoOauthTokenDomain;
import com.ctp102.module.oauth.core.enums.PlatformTypes;
import com.ctp102.module.oauth.core.form.KakaoOauthTokenForm;
import com.ctp102.module.oauth.core.service.GoogleOauthService;
import com.ctp102.module.oauth.core.service.KakaoOauthService;
import com.ctp102.module.oauth.core.service.NaverOauthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OauthController {

    private final KakaoOauthService kakaoOauthService;
    private final GoogleOauthService googleOauthService;
    private final NaverOauthService naverOauthService;

    @GetMapping("/oauth/kakao/authorize")
    public String oAuthKakaoAuthorize(HttpServletRequest request, HttpServletResponse response) {
        String redirectUri = getCallbackUri(request, PlatformTypes.KAKAO.getName());
        String oAuthRedirectUri = kakaoOauthService.getOauthRedirectUri(redirectUri);

        log.debug("[Kakao Oauth] redirectUri = {}, oAuthRedirectUri = {}", redirectUri, oAuthRedirectUri);
        return "redirect:" + oAuthRedirectUri;
    }

    @GetMapping("/oauth/kakao/callback")
    public String oAuthKakaoCallback(HttpServletRequest request, HttpServletResponse response, Model model,
                           @ModelAttribute KakaoOauthAuthorizeResponseDomain kakaoOauthAuthorizeResponseDomain) {

        if (StringUtils.isNotBlank(kakaoOauthAuthorizeResponseDomain.getError())) {
            log.info("[로그인 취소] errorCode = {}, errorMsg = {}", kakaoOauthAuthorizeResponseDomain.getError(), kakaoOauthAuthorizeResponseDomain.getErrorDescription());
        }

        String code = kakaoOauthAuthorizeResponseDomain.getCode();
        if (StringUtils.isNotBlank(code)) {
            KakaoOauthTokenForm kakaoOauthTokenForm = new KakaoOauthTokenForm();
            kakaoOauthTokenForm.setRedirect_uri(getCallbackUri(request, PlatformTypes.KAKAO.getName()));
            kakaoOauthTokenForm.setCode(code);

            KakaoOauthTokenDomain kakaoOauthTokenDomain = kakaoOauthService.getOauthInfo(kakaoOauthTokenForm);

            model.addAttribute("kakaoOauthTokenDomain", kakaoOauthTokenDomain);
        }
        return "profile";
    }

    @GetMapping("/oauth/google/authorize")
    public String oAuthGoogleAuthorize(HttpServletRequest request, HttpServletResponse response) {
        String redirectUri = getCallbackUri(request, PlatformTypes.GOOGLE.name());
        String oAuthRedirectUri = googleOauthService.getOauthRedirectUri(redirectUri);

        log.debug("[Google Oauth] redirectUri = {}, oAuthRedirectUri = {}", redirectUri, oAuthRedirectUri);
        return "redirect:" + oAuthRedirectUri;
    }

    @GetMapping("/oauth/kakao/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    private String getCallbackUri(HttpServletRequest request, String platform) {
        return UriComponentsBuilder.fromUriString(getRequestServerProtocolDomain(request) + String.format("/oauth/%s/callback", platform)).toUriString();
    }

    private String getRequestServerProtocolDomain(HttpServletRequest request) {
        String xForwardedProto = request.getHeader("X-Forwarded-Proto");
        String xForwardedPort = request.getHeader("X-Forwarded-Port");

        // protocol
        if (xForwardedProto == null) {
            String protocol = request.getProtocol(); // ex) HTTP/1.1
            xForwardedProto = protocol.substring(0, protocol.indexOf("/")).toLowerCase();
        }

        // domain
        String commonUrlStr = xForwardedProto + "://" + request.getServerName();

        // port
        if (xForwardedPort == null) {
            xForwardedPort = String.valueOf(request.getServerPort());
        }

        if (!xForwardedPort.equals("80") && !xForwardedPort.equals("443")) {
            commonUrlStr = commonUrlStr + ":" + xForwardedPort;
        }
        return commonUrlStr;
    }

}
