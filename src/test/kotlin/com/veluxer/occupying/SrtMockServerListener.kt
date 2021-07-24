package com.veluxer.occupying

import com.veluxer.occupying.Fixture.JSESSIONID
import com.veluxer.occupying.Fixture.LOGIN_ID
import com.veluxer.occupying.Fixture.MOCK_SERVER_HOST
import com.veluxer.occupying.Fixture.MOCK_SERVER_PORT
import com.veluxer.occupying.Fixture.SUCCESS_LOGIN_PW
import com.veluxer.occupying.domain.srt.SrtConstraint.LOGIN_PATH
import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse

internal class SrtMockServerListener : TestListener {
    override suspend fun beforeTest(testCase: TestCase) {
        loginSuccess()
    }

    private fun loginSuccess() {
        generateMockServerClient()
            .`when`(
                HttpRequest.request()
                    .withMethod("POST")
                    .withPath(LOGIN_PATH)
                    .withHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Linux; Android 5.1.1; LGM-V300K Build/N2G47H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36SRT-APP-Android V.1.0.6"
                    )
                    .withHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .withHeader("Accept", "application/json")
                    .withBody("auto=Y&check=Y&srchDvCd=1&srchDvNm=$LOGIN_ID&hmpgPwdCphd=$SUCCESS_LOGIN_PW")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "application/json;charset=UTF-8")
                    .withHeader(
                        "Set-Cookie",
                        "WMONID=gFr1KFN1AR-; Expires=Mon, 25-Jul-2022 00:55:34 GMT; Path=/",
                        "JSESSIONID_XEBEC=$JSESSIONID; Path=/; HttpOnly",
                        "gs_loginMemNo=2089154494; Expires=Sun, 24-Jul-2022 15:55:34 GMT; Path=/",
                        "srail_type8=N; Expires=Sun, 24-Jul-2022 15:55:34 GMT; Path=/",
                        "srail_type10=lXiqvq7diQZtYSf+9pZGTHm92ZBLUms8eSYc0HQmlrIfuYnWz9taF//KBehxqk+nn96EvneEVzxaYFDAV+0iSYgGOP1GoSLMrHjrhjWojMOAkOcSbQ5zJBJ8VMvmaVwMWoK90bzZWokxVcCJXbtylW9EgJgsI/wCs3VX9ilx9c8=; Expires=Sun, 24-Jul-2022 15:55:34 GMT; Path=/"
                    )
                    .withBody(
                        """
                            {
                              "userMap": {
                                "deviceKey": "-",
                                "BTDT": "1901-01-01",
                                "PBL_DISC_MG_NO": "",
                                "requestJSessionTime": 1623561559604,
                                "UUID": "",
                                "KR_JSESSIONID": "JSESSIONID=세션ID;Path=/",
                                "USER_KEY": "0000000000",
                                "USER_DV": null,
                                "wctNo": "00000",
                                "strDeviceInfo": "",
                                "CUST_SRT_CD": "P",
                                "CUST_NM": "홍길동",
                                "MB_CRD_NO": "0000000000",
                                "POSI_NM": "",
                                "PBL_DISC_CD": "",
                                "PBL_DISC_NM": "",
                                "DPT_NM": "",
                                "MBL_PHONE": "010-0000-0000",
                                "GOFF_RS_STN_CD": "",
                                "WCTNO": "00000",
                                "CUST_CL_CD": "N",
                                "GRD_NM": "",
                                "SEX_DV_CD": "M",
                                "SR_JSESSIONID": "JSESSIONID=세션ID;Path=/",
                                "USR_PWD_CPHD": "비밀번호",
                                "DSCP_YN": "N",
                                "TGT_DTRM_YN": "",
                                "CUST_MG_NO": "MP0000000000",
                                "ABRD_RS_STN_CD": "0000",
                                "uuid": ""
                              },
                              "commandMap": {
                                "auto": "Y",
                                "check": "Y",
                                "page": "menu",
                                "deviceKey": "-",
                                "customerYn": "",
                                "login_referer": "https://app.srail.or.kr/main/main.do",
                                "srchDvCd": "1",
                                "srchDvNm": "계정ID",
                                "hmpgPwdCphd": "비밀번호",
                                "login_idIdx": "1",
                                "login_idVal": "0000000000",
                                "login_check": "Y",
                                "login_auto": "Y"
                              }
                            }
                        """.trimIndent()
                    )
            )
    }

    private fun generateMockServerClient() = MockServerClient(MOCK_SERVER_HOST, MOCK_SERVER_PORT)
}
