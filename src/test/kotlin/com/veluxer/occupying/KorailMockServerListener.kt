package com.veluxer.occupying

import com.veluxer.occupying.TestConstraint.FAILURE_LOGIN_PW
import com.veluxer.occupying.TestConstraint.JSESSIONID
import com.veluxer.occupying.TestConstraint.LOGIN_ID
import com.veluxer.occupying.TestConstraint.MOCK_SERVER_HOST
import com.veluxer.occupying.TestConstraint.MOCK_SERVER_PORT
import com.veluxer.occupying.TestConstraint.SUCCESS_LOGIN_PW
import com.veluxer.occupying.domain.KorailConstraint.LOGIN_PATH
import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse

internal class KorailMockServerListener : TestListener {
    override suspend fun beforeTest(testCase: TestCase) {
        MockServerClient(MOCK_SERVER_HOST, MOCK_SERVER_PORT)
            .`when`(
                HttpRequest.request()
                    .withMethod("POST")
                    .withPath(LOGIN_PATH)
                    .withHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .withBody("Device=AD&txtInputFlg=2&txtMemberNo=$LOGIN_ID&txtPwd=$SUCCESS_LOGIN_PW")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "text/plain;charset=utf-8")
                    .withHeader("Set-Cookie", "JSESSIONID=$JSESSIONID;Path=/")
                    .withCookie("JSESSIONID", JSESSIONID)
                    .withBody(
                        """
                        {
                          "Key": "korail1234566778",
                          "strDiscCouponFlg": "N",
                          "encryptMbCrdNo": "ABCDE",
                          "encryptHMbCrdNo": "ABCDE",
                          "encryptCustNo": "ABCDE",
                          "strLognTpCd4": "N",
                          "strPrsCnqeMsgCd": "",
                          "strCustMgSrtCd": "P",
                          "strDiscCrdReisuFlg": "N",
                          "strCustSrtCd": "210",
                          "strGoffStnNm": "부산",
                          "strHdcpFlg": "F",
                          "strBtdt": "19990101",
                          "h_msg_cd": "IRZ000001",
                          "strResult": "SUCC",
                          "strEvtTgtFlg": "S",
                          "strEmailAdr": "test@naver.com",
                          "strSexDvCd": "M",
                          "strLognTpCd3": "N",
                          "strCustLeadFlgNm": "",
                          "h_msg_txt": "정상적으로 조회 되었습니다.",
                          "strAthnFlg": "N",
                          "strMbCrdNo": "123456789",
                          "notiTpCd": "",
                          "dlayDscpInfo": "N",
                          "strLognTpCd1": "N",
                          "strCustLeadFlg": "",
                          "strAthnFlg2": "F",
                          "strSubtDcsClCd": "",
                          "strAbrdStnCd": "0001",
                          "strCpNo": "01012345678",
                          "strCustNo": "RA12345678",
                          "strCustDvCd": "B",
                          "strLognTpCd2": "N",
                          "strCustNm": "홍길동",
                          "strAthnFlg5": "Y",
                          "strCustClCd": "F",
                          "strHdcpTpCd": "",
                          "strYouthAgrFlg": "6",
                          "strHdcpTpCdNm": "",
                          "strAbrdStnNm": "서울",
                          "strGoffStnCd": "0020"
                        }
                        """.trimIndent()
                    )
            )

        MockServerClient(MOCK_SERVER_HOST, MOCK_SERVER_PORT)
            .`when`(
                HttpRequest.request()
                    .withMethod("POST")
                    .withPath(LOGIN_PATH)
                    .withHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .withBody("Device=AD&txtInputFlg=2&txtMemberNo=$LOGIN_ID&txtPwd=$FAILURE_LOGIN_PW")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "text/plain;charset=utf-8")
                    .withBody(
                        """
                        {
                          "h_msg_txt": "로그인 정보를 다시 확인하세요.\n코레일 멤버십에 등록되지 않은 정보이거나 회원번호(이메일/휴대전화) 또는 비밀번호를 잘못 입력하셨습니다.\n비밀번호는 연속 5회 틀리면 이용이 불가하니 주의 바랍니다.",
                          "h_msg_cd": "WRC000391",
                          "strResult": "SUCC"
                        }
                        """.trimIndent()
                    )
            )
    }
}
