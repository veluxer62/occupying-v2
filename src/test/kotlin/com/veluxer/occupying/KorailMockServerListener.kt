package com.veluxer.occupying

import com.veluxer.occupying.TestConstraint.FAILURE_LOGIN_PW
import com.veluxer.occupying.TestConstraint.JSESSIONID
import com.veluxer.occupying.TestConstraint.LOGIN_ID
import com.veluxer.occupying.TestConstraint.MOCK_SERVER_HOST
import com.veluxer.occupying.TestConstraint.MOCK_SERVER_PORT
import com.veluxer.occupying.TestConstraint.SEARCH_DEPARTURE_DATETIME
import com.veluxer.occupying.TestConstraint.SEARCH_DEPARTURE_STATION
import com.veluxer.occupying.TestConstraint.SEARCH_DESTINATION_STATION
import com.veluxer.occupying.TestConstraint.SUCCESS_LOGIN_PW
import com.veluxer.occupying.domain.KorailConstraint.LOGIN_PATH
import com.veluxer.occupying.domain.KorailConstraint.SEARCH_PATH
import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import java.time.format.DateTimeFormatter

internal class KorailMockServerListener : TestListener {
    override suspend fun beforeTest(testCase: TestCase) {
        generateMockServerClient()
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

        generateMockServerClient()
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

        generateMockServerClient()
            .`when`(
                HttpRequest.request()
                    .withMethod("POST")
                    .withPath(SEARCH_PATH)
                    .withHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .withBody(
                        "Device=AD" +
                            "&Version=190617001" +
                            "&radJobId=1" +
                            "&selGoTrain=00" +
                            "&txtGoAbrdDt=${
                            SEARCH_DEPARTURE_DATETIME.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                            }" +
                            "&txtGoHour=${
                            SEARCH_DEPARTURE_DATETIME.toLocalTime().format(DateTimeFormatter.ofPattern("HHmmss"))
                            }" +
                            "&txtGoStart=${SEARCH_DEPARTURE_STATION.label}" +
                            "&txtGoEnd=${SEARCH_DESTINATION_STATION.label}" +
                            "&txtPsgFlg_1=1"
                    )
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "text/plain;charset=utf-8")
                    .withBody(
                        """
                        {
                          "h_msg_cd": "IRG000000",
                          "h_msg_txt": "정상처리되었습니다",
                          "strJobId": "1",
                          "h_menu_id": "11",
                          "h_gd_no": "",
                          "h_seat_cnt_first": "0001",
                          "h_seat_cnt_second": "0000",
                          "h_next_pg_flg": "Y",
                          "txtGoHour_first": "",
                          "h_rslt_cnt": "10",
                          "h_agree_txt": "",
                          "trn_infos": {
                            "trn_info": [
                              {
                                "h_trn_seq": "000",
                                "h_chg_trn_dv_cd": "1",
                                "h_chg_trn_dv_nm": "직통",
                                "h_chg_trn_seq": "1",
                                "h_dpt_rs_stn_cd": "0001",
                                "h_dpt_rs_stn_nm": "서울",
                                "h_arv_rs_stn_cd": "0020",
                                "h_arv_rs_stn_nm": "부산",
                                "h_trn_no": "009",
                                "h_trn_no_qb": "009",
                                "h_yms_apl_flg": "Y",
                                "h_trn_gp_cd": "100",
                                "h_trn_clsf_cd": "00",
                                "h_run_dt": "20210701",
                                "h_dpt_dt": "20210701",
                                "h_dpt_tm": "070000",
                                "h_dpt_tm_qb": "07:00",
                                "h_arv_dt": "20210701",
                                "h_arv_tm": "093800",
                                "h_arv_tm_qb": "09:38",
                                "h_expct_dlay_hr": "000000",
                                "h_rsv_wait_ps_cnt": "0278",
                                "h_dtour_flg": "",
                                "h_dtour_txt": "-",
                                "h_std_rest_seat_cnt": "",
                                "h_fst_rest_seat_cnt": "",
                                "h_car_tp_cd": "",
                                "h_car_tp_nm": "",
                                "h_trn_cps_cd1": "X",
                                "h_trn_cps_nm1": "전동휠체어석",
                                "h_trn_cps_cd2": "27",
                                "h_trn_cps_nm2": "가족석",
                                "h_trn_cps_cd3": "",
                                "h_trn_cps_nm3": "",
                                "h_trn_cps_cd4": "",
                                "h_trn_cps_nm4": "",
                                "h_trn_cps_cd5": "",
                                "h_trn_cps_nm5": "",
                                "h_train_disc_rt": "",
                                "h_wait_rsv_flg": "-2",
                                "h_rd_cnd_disc_no": "",
                                "h_rd_cnd_disc_nm": "",
                                "h_spe_rsv_cd": "11",
                                "h_spe_rsv_cd2": "39",
                                "h_spe_rsv_nm": "좌석많음",
                                "h_spe_disc_rt": "0",
                                "h_spe_seat_map_flg": "Y",
                                "h_spe_psrm_cl_nm": "특실",
                                "h_gen_rsv_cd": "11",
                                "h_gen_rsv_cd2": "39",
                                "h_gen_rsv_nm": "좌석많음",
                                "h_gen_disc_rt": "0",
                                "h_gen_seat_map_flg": "Y",
                                "h_gen_psrm_cl_nm": "일반실",
                                "h_stnd_rsv_cd": "00",
                                "h_stnd_rsv_nm": "-",
                                "h_free_rsv_cd": "01",
                                "h_free_rsv_nm": "역발매중\n(3량)",
                                "h_free_sracar_cnt": "003",
                                "h_train_disc_gen_rt": "-005.00",
                                "h_run_tm": "0238",
                                "h_rd_add_info": "00",
                                "h_nonstop_msg": "",
                                "h_nonstop_msg_txt": "",
                                "h_rd_seat_map_flg": "YY",
                                "h_dpt_stn_cons_ordr": "000007",
                                "h_arv_stn_cons_ordr": "000030",
                                "h_dpt_stn_run_ordr": "000001",
                                "h_arv_stn_run_ordr": "000008",
                                "h_seat_att_cd": "015",
                                "h_rcvd_amt": "00000000059800",
                                "h_rcvd_fare": "00000000023900",
                                "h_cnec_trfc_psb_flg": "",
                                "h_cnec_trfc_nd_hm": "",
                                "h_cnec_trfc_rcvd_prc": "00000000000000",
                                "h_rsv_psb_flg": "Y",
                                "h_rsv_psb_nm": "59,800원\n5%적립",
                                "h_stn_sale_flg": "N",
                                "h_stn_sale_txt": "자유석 또는 입석은\n역에서 구입할 수 있습니다.",
                                "h_info_txt": "선택하신 열차는 다른 열차에 비해 정차역 수가 적어 가격이 최대 0.6% 높습니다.\n\n계속 진행하시겠습니까?",
                                "h_trn_clsf_nm": "KTX",
                                "h_trn_gp_nm": "KTX"
                              },
                              {
                                "h_trn_seq": "001",
                                "h_chg_trn_dv_cd": "1",
                                "h_chg_trn_dv_nm": "직통",
                                "h_chg_trn_seq": "1",
                                "h_dpt_rs_stn_cd": "0001",
                                "h_dpt_rs_stn_nm": "서울",
                                "h_arv_rs_stn_cd": "0020",
                                "h_arv_rs_stn_nm": "부산",
                                "h_trn_no": "011",
                                "h_trn_no_qb": "011",
                                "h_yms_apl_flg": "Y",
                                "h_trn_gp_cd": "100",
                                "h_trn_clsf_cd": "00",
                                "h_run_dt": "20210730",
                                "h_dpt_dt": "20210730",
                                "h_dpt_tm": "073000",
                                "h_dpt_tm_qb": "07:30",
                                "h_arv_dt": "20210730",
                                "h_arv_tm": "100300",
                                "h_arv_tm_qb": "10:03",
                                "h_expct_dlay_hr": "000000",
                                "h_rsv_wait_ps_cnt": "0278",
                                "h_dtour_flg": "",
                                "h_dtour_txt": "-",
                                "h_std_rest_seat_cnt": "",
                                "h_fst_rest_seat_cnt": "",
                                "h_car_tp_cd": "",
                                "h_car_tp_nm": "",
                                "h_trn_cps_cd1": "X",
                                "h_trn_cps_nm1": "전동휠체어석",
                                "h_trn_cps_cd2": "",
                                "h_trn_cps_nm2": "",
                                "h_trn_cps_cd3": "",
                                "h_trn_cps_nm3": "",
                                "h_trn_cps_cd4": "",
                                "h_trn_cps_nm4": "",
                                "h_trn_cps_cd5": "",
                                "h_trn_cps_nm5": "",
                                "h_train_disc_rt": "",
                                "h_wait_rsv_flg": "-1",
                                "h_rd_cnd_disc_no": "",
                                "h_rd_cnd_disc_nm": "",
                                "h_spe_rsv_cd": "00",
                                "h_spe_rsv_cd2": null,
                                "h_spe_rsv_nm": "-",
                                "h_spe_disc_rt": "0",
                                "h_spe_seat_map_flg": "",
                                "h_spe_psrm_cl_nm": "특실",
                                "h_gen_rsv_cd": "00",
                                "h_gen_rsv_cd2": null,
                                "h_gen_rsv_nm": "-",
                                "h_gen_disc_rt": "0",
                                "h_gen_seat_map_flg": "",
                                "h_gen_psrm_cl_nm": "일반실",
                                "h_stnd_rsv_cd": "00",
                                "h_stnd_rsv_nm": "-",
                                "h_free_rsv_cd": "01",
                                "h_free_rsv_nm": "역발매중\n(2량)",
                                "h_free_sracar_cnt": "002",
                                "h_train_disc_gen_rt": "-005.00",
                                "h_run_tm": "0233",
                                "h_rd_add_info": "00",
                                "h_nonstop_msg": "",
                                "h_nonstop_msg_txt": "",
                                "h_rd_seat_map_flg": "  ",
                                "h_dpt_stn_cons_ordr": "000007",
                                "h_arv_stn_cons_ordr": "000030",
                                "h_dpt_stn_run_ordr": "000002",
                                "h_arv_stn_run_ordr": "000008",
                                "h_seat_att_cd": "",
                                "h_rcvd_amt": "00000000059800",
                                "h_rcvd_fare": "00000000023900",
                                "h_cnec_trfc_psb_flg": "",
                                "h_cnec_trfc_nd_hm": "",
                                "h_cnec_trfc_rcvd_prc": "00000000000000",
                                "h_rsv_psb_flg": "N",
                                "h_rsv_psb_nm": "자유석\n역발매중",
                                "h_stn_sale_flg": "Y",
                                "h_stn_sale_txt": "자유석 또는 입석은\n역에서 구입할 수 있습니다.",
                                "h_info_txt": "선택하신 열차는 다른 열차에 비해 정차역 수가 적어 가격이 최대 0.6% 높습니다.\n\n계속 진행하시겠습니까?",
                                "h_trn_clsf_nm": "KTX",
                                "h_trn_gp_nm": "KTX"
                              }
                            ]
                          },
                          "h_notice_msg": "",
                          "strResult": "SUCC"
                        }

                        """.trimIndent()
                    )
            )
    }

    private fun generateMockServerClient() = MockServerClient(MOCK_SERVER_HOST, MOCK_SERVER_PORT)
}
