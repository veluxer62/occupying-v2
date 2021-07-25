package com.veluxer.occupying

import com.veluxer.occupying.Fixture.FAILURE_LOGIN_PW
import com.veluxer.occupying.Fixture.JSESSIONID
import com.veluxer.occupying.Fixture.LOGIN_ID
import com.veluxer.occupying.Fixture.MOCK_SERVER_HOST
import com.veluxer.occupying.Fixture.MOCK_SERVER_PORT
import com.veluxer.occupying.Fixture.SEARCH_DEPARTURE_DATETIME
import com.veluxer.occupying.Fixture.SEARCH_DEPARTURE_STATION
import com.veluxer.occupying.Fixture.SEARCH_DESTINATION_STATION
import com.veluxer.occupying.Fixture.SUCCESS_LOGIN_PW
import com.veluxer.occupying.domain.korail.KorailConstraint
import com.veluxer.occupying.domain.srt.SrtConstraint.LOGIN_PATH
import com.veluxer.occupying.domain.srt.SrtConstraint.SEARCH_PATH
import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse

internal class SrtMockServerListener : TestListener {
    override suspend fun beforeTest(testCase: TestCase) {
        loginSuccess()
        loginFailure()
        searchTrains()
    }

    private fun searchTrains() {
        generateMockServerClient()
            .`when`(
                HttpRequest.request()
                    .withMethod("POST")
                    .withPath(SEARCH_PATH)
                    .withHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Linux; Android 5.1.1; LGM-V300K Build/N2G47H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36SRT-APP-Android V.1.0.6"
                    )
                    .withHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .withHeader("Accept", "application/json")
                    .withBody(
                        "chtnDvCd=1" +
                            "&seatAttCd=015" +
                            "&psgNum=1" +
                            "&trnGpCd=109" +
                            "&stlbTrnClsfCd=05" +
                            "&dptDt=${SEARCH_DEPARTURE_DATETIME.toLocalDate().format(KorailConstraint.DATE_FORMAT)}" +
                            "&dptTm=${SEARCH_DEPARTURE_DATETIME.toLocalTime().format(KorailConstraint.TIME_FORMAT)}" +
                            "&dptRsStnCd=${SEARCH_DEPARTURE_STATION.code}" +
                            "&arvRsStnCd=${SEARCH_DESTINATION_STATION.code}"
                    )
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "application/json;charset=UTF-8")
                    .withBody(
                        """
                            {
                              "ErrorMsg": "",
                              "ErrorCode": "0",
                              "resultMap": [
                                {
                                  "msgCd": "IRG000000",
                                  "seandYo": "N",
                                  "wctNo": "81301",
                                  "qryCnqeCnt": 10,
                                  "strResult": "SUCC",
                                  "msgTxt": "정상처리되었습니다",
                                  "fllwPgExt2": null,
                                  "fllwPgExt": "Y",
                                  "uuid": "APP061314385800070920000000000000-",
                                  "cgPsId": "korail"
                                }
                              ],
                              "outDataSets": {
                                "dsOutput1": [
                                  {
                                    "etcRsvPsbCdNm": "-",
                                    "rcvdAmt": "00000000052900",
                                    "sprmRsvPsbStr": "예약가능",
                                    "trnOrdrNo": 0,
                                    "gnrmRsvPsbStr": "예약가능",
                                    "gnrmRsvPsbColor": "#8e1a3cff",
                                    "runTm": "0235",
                                    "sprmRsvPsbColor": "#8e1a3cff",
                                    "arvTm": "080500",
                                    "fresRsvPsbCdNm": null,
                                    "runDt": "20210701",
                                    "stlbDturDvCd": "",
                                    "ocurDlayTnum": 0,
                                    "seatAttCd": "015",
                                    "rcvdFare": "00000000023800",
                                    "arvStnConsOrdr": "000020",
                                    "sprmRsvPsbImg": "IMAGE::grd_WF_Ok01.png",
                                    "rsvWaitPsbCdNm": "-",
                                    "chtnDvCd": "1",
                                    "rsvWaitPsbCd": "-2",
                                    "seatSelect": "",
                                    "dptStnRunOrdr": "000001",
                                    "stlbTrnClsfCd": "17",
                                    "trainDiscGenRt": "0000.00",
                                    "dptTm": "053000",
                                    "stmpRsvPsbFlgCd": "YY",
                                    "trnNstpLeadInfo": "",
                                    "arvDt": "20210701",
                                    "gnrmRsvPsbCdNm": "예약하기/좌석선택",
                                    "gnrmRsvPsbImg": "IMAGE::grd_WF_Ok01.png",
                                    "trnGpCd": "300",
                                    "sprmRsvPsbCdNm": "예약하기/좌석선택",
                                    "payTable": "",
                                    "dptRsStnCd": "0551",
                                    "ymsAplFlg": "Y",
                                    "fresOprCno": 0,
                                    "doReserv": "",
                                    "timeTable": "",
                                    "stndRsvPsbCdNm": "-",
                                    "dptStnConsOrdr": "000001",
                                    "arvRsStnCd": "0020",
                                    "dptDt": "20210701",
                                    "trnNo": "301",
                                    "arvStnRunOrdr": "000008",
                                    "trnCpsCd5": "",
                                    "trnCpsCd3": "",
                                    "trnCpsCd4": "",
                                    "trnCpsCd1": "X",
                                    "trnCpsCd2": ""
                                  },
                                  {
                                    "etcRsvPsbCdNm": "-",
                                    "rcvdAmt": "00000000052900",
                                    "sprmRsvPsbStr": "매진",
                                    "trnOrdrNo": 0,
                                    "gnrmRsvPsbStr": "예약가능",
                                    "gnrmRsvPsbColor": "#8e1a3cff",
                                    "runTm": "0225",
                                    "sprmRsvPsbColor": "#ffffffff",
                                    "arvTm": "184600",
                                    "fresRsvPsbCdNm": null,
                                    "runDt": "20210725",
                                    "stlbDturDvCd": "",
                                    "ocurDlayTnum": 0,
                                    "seatAttCd": "015",
                                    "rcvdFare": "00000000023800",
                                    "arvStnConsOrdr": "000020",
                                    "sprmRsvPsbImg": "IMAGE::grd_WF_Soldout.png",
                                    "rsvWaitPsbCdNm": "-",
                                    "chtnDvCd": "1",
                                    "rsvWaitPsbCd": "-1",
                                    "seatSelect": "",
                                    "dptStnRunOrdr": "000001",
                                    "stlbTrnClsfCd": "17",
                                    "trainDiscGenRt": "0000.00",
                                    "dptTm": "162100",
                                    "stmpRsvPsbFlgCd": "NN",
                                    "trnNstpLeadInfo": "",
                                    "arvDt": "20210701",
                                    "gnrmRsvPsbCdNm": "예약하기",
                                    "gnrmRsvPsbImg": "IMAGE::grd_WF_Ok01.png",
                                    "trnGpCd": "300",
                                    "sprmRsvPsbCdNm": "좌석매진",
                                    "payTable": "",
                                    "dptRsStnCd": "0551",
                                    "ymsAplFlg": "Y",
                                    "fresOprCno": 0,
                                    "doReserv": "",
                                    "timeTable": "",
                                    "stndRsvPsbCdNm": "-",
                                    "dptStnConsOrdr": "000001",
                                    "arvRsStnCd": "0020",
                                    "dptDt": "20210701",
                                    "trnNo": "349",
                                    "arvStnRunOrdr": "000006",
                                    "trnCpsCd5": "",
                                    "trnCpsCd3": "",
                                    "trnCpsCd4": "",
                                    "trnCpsCd1": "X",
                                    "trnCpsCd2": ""
                                  }
                                ],
                                "dsOutput0": [
                                  {
                                    "msgCd": "IRG000000",
                                    "seandYo": "N",
                                    "wctNo": "81301",
                                    "qryCnqeCnt": 10,
                                    "strResult": "SUCC",
                                    "msgTxt": "정상처리되었습니다",
                                    "fllwPgExt2": null,
                                    "fllwPgExt": "Y",
                                    "uuid": "APP061314385800070920000000000000-",
                                    "cgPsId": "korail"
                                  }
                                ]
                              },
                              "commandMap": {
                                "chtnDvCd": "1",
                                "arriveTime": "N",
                                "seatAttCd": "015",
                                "psgNum": "1",
                                "trnGpCd": "109",
                                "stlbTrnClsfCd": "05",
                                "dptDt": "20210701",
                                "dptTm": "000000",
                                "arvRsStnCd": "0020",
                                "dptRsStnCd": "0551"
                              },
                              "trainListMap": [
                                {
                                  "etcRsvPsbCdNm": "-",
                                  "rcvdAmt": "00000000052900",
                                  "sprmRsvPsbStr": "예약가능",
                                  "trnOrdrNo": 0,
                                  "gnrmRsvPsbStr": "예약가능",
                                  "gnrmRsvPsbColor": "#8e1a3cff",
                                  "runTm": "0235",
                                  "sprmRsvPsbColor": "#8e1a3cff",
                                  "arvTm": "080500",
                                  "fresRsvPsbCdNm": null,
                                  "runDt": "20210701",
                                  "stlbDturDvCd": "",
                                  "ocurDlayTnum": 0,
                                  "seatAttCd": "015",
                                  "rcvdFare": "00000000023800",
                                  "arvStnConsOrdr": "000020",
                                  "sprmRsvPsbImg": "IMAGE::grd_WF_Ok01.png",
                                  "rsvWaitPsbCdNm": "-",
                                  "chtnDvCd": "1",
                                  "rsvWaitPsbCd": "-2",
                                  "seatSelect": "",
                                  "dptStnRunOrdr": "000001",
                                  "stlbTrnClsfCd": "17",
                                  "trainDiscGenRt": "0000.00",
                                  "dptTm": "053000",
                                  "stmpRsvPsbFlgCd": "YY",
                                  "trnNstpLeadInfo": "",
                                  "arvDt": "20210701",
                                  "gnrmRsvPsbCdNm": "예약하기/좌석선택",
                                  "gnrmRsvPsbImg": "IMAGE::grd_WF_Ok01.png",
                                  "trnGpCd": "300",
                                  "sprmRsvPsbCdNm": "예약하기/좌석선택",
                                  "payTable": "",
                                  "dptRsStnCd": "0551",
                                  "ymsAplFlg": "Y",
                                  "fresOprCno": 0,
                                  "doReserv": "",
                                  "timeTable": "",
                                  "stndRsvPsbCdNm": "-",
                                  "dptStnConsOrdr": "000001",
                                  "arvRsStnCd": "0020",
                                  "dptDt": "20210701",
                                  "trnNo": "301",
                                  "arvStnRunOrdr": "000008",
                                  "trnCpsCd5": "",
                                  "trnCpsCd3": "",
                                  "trnCpsCd4": "",
                                  "trnCpsCd1": "X",
                                  "trnCpsCd2": ""
                                },
                                {
                                  "etcRsvPsbCdNm": "-",
                                  "rcvdAmt": "00000000052900",
                                  "sprmRsvPsbStr": "매진",
                                  "trnOrdrNo": 0,
                                  "gnrmRsvPsbStr": "예약가능",
                                  "gnrmRsvPsbColor": "#8e1a3cff",
                                  "runTm": "0225",
                                  "sprmRsvPsbColor": "#ffffffff",
                                  "arvTm": "184600",
                                  "fresRsvPsbCdNm": null,
                                  "runDt": "20210701",
                                  "stlbDturDvCd": "",
                                  "ocurDlayTnum": 0,
                                  "seatAttCd": "015",
                                  "rcvdFare": "00000000023800",
                                  "arvStnConsOrdr": "000020",
                                  "sprmRsvPsbImg": "IMAGE::grd_WF_Soldout.png",
                                  "rsvWaitPsbCdNm": "-",
                                  "chtnDvCd": "1",
                                  "rsvWaitPsbCd": "-1",
                                  "seatSelect": "",
                                  "dptStnRunOrdr": "000001",
                                  "stlbTrnClsfCd": "17",
                                  "trainDiscGenRt": "0000.00",
                                  "dptTm": "162100",
                                  "stmpRsvPsbFlgCd": "NN",
                                  "trnNstpLeadInfo": "",
                                  "arvDt": "20210701",
                                  "gnrmRsvPsbCdNm": "예약하기",
                                  "gnrmRsvPsbImg": "IMAGE::grd_WF_Ok01.png",
                                  "trnGpCd": "300",
                                  "sprmRsvPsbCdNm": "좌석매진",
                                  "payTable": "",
                                  "dptRsStnCd": "0551",
                                  "ymsAplFlg": "Y",
                                  "fresOprCno": 0,
                                  "doReserv": "",
                                  "timeTable": "",
                                  "stndRsvPsbCdNm": "-",
                                  "dptStnConsOrdr": "000001",
                                  "arvRsStnCd": "0020",
                                  "dptDt": "20210701",
                                  "trnNo": "349",
                                  "arvStnRunOrdr": "000006",
                                  "trnCpsCd5": "",
                                  "trnCpsCd3": "",
                                  "trnCpsCd4": "",
                                  "trnCpsCd1": "X",
                                  "trnCpsCd2": ""
                                }
                              ]
                            }
                        """.trimIndent()
                    )
            )
    }

    private fun loginFailure() {
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
                    .withBody("auto=Y&check=Y&srchDvCd=1&srchDvNm=$LOGIN_ID&hmpgPwdCphd=$FAILURE_LOGIN_PW")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "application/json;charset=UTF-8")
                    .withBody(
                        """
                            {
                              "MSG": "비밀번호 오류입니다.",
                              "page": "menu",
                              "userMap": {
                                "USER_KEY": "",
                                "USER_DV": null,
                                "wctNo": "",
                                "deviceKey": "-",
                                "uuid": "",
                                "strDeviceInfo": "",
                                "deviceId": "-"
                              },
                              "commandMap": {
                                "auto": "Y",
                                "check": "Y",
                                "page": "menu",
                                "deviceKey": "-",
                                "customerYn": "",
                                "login_referer": "https://app.srail.or.kr/main/main.do",
                                "srchDvCd": "1",
                                "srchDvNm": "1234567890",
                                "hmpgPwdCphd": "<비밀번호>",
                                "login_idIdx": "1",
                                "login_idVal": "1234567890",
                                "login_check": "Y",
                                "login_auto": "Y"
                              },
                              "strResult": "FAIL",
                              "msgTxt": "입력값 오류",
                              "RTNCD": "N"
                            }
                        """.trimIndent()
                    )
            )
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
