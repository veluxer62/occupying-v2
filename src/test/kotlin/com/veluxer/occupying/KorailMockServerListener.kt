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
import com.veluxer.occupying.Fixture.TRAIN_NO
import com.veluxer.occupying.domain.PassengerType
import com.veluxer.occupying.domain.SeatType
import com.veluxer.occupying.domain.TrainType
import com.veluxer.occupying.domain.korail.KorailConstraint.DATE_FORMAT
import com.veluxer.occupying.domain.korail.KorailConstraint.LOGIN_PATH
import com.veluxer.occupying.domain.korail.KorailConstraint.RESERVATION_PATH
import com.veluxer.occupying.domain.korail.KorailConstraint.SEARCH_PATH
import com.veluxer.occupying.domain.korail.KorailConstraint.SESSION_COOKIE_NAME
import com.veluxer.occupying.domain.korail.KorailConstraint.TIME_FORMAT
import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import java.net.URLEncoder

internal class KorailMockServerListener : TestListener {
    override suspend fun beforeTest(testCase: TestCase) {
        loginSuccess()
        loginFailure()
        searchTrains()
        reserveSuccess()
        reserveFailure()
    }

    private fun reserveFailure() {
        generateMockServerClient()
            .`when`(
                HttpRequest.request()
                    .withMethod("POST")
                    .withPath(RESERVATION_PATH)
                    .withHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .withBody(
                        "Device=AD" +
                            "&txtSeatAttCd1=${SeatType.NORMAL.code}" +
                            "&txtSeatAttCd2=000" +
                            "&txtSeatAttCd3=000" +
                            "&txtSeatAttCd4=015" +
                            "&txtStndFlg=N" +
                            "&txtJrnyCnt=1" +
                            "&txtJrnySqno1=001" +
                            "&txtJrnyTpCd1=11" +
                            "&txtTotPsgCnt=1" +
                            "&txtDptRsStnCd1=${SEARCH_DEPARTURE_STATION.code}" +
                            "&txtDptDt1=${SEARCH_DEPARTURE_DATETIME.toLocalDate().format(DATE_FORMAT)}" +
                            "&txtDptTm1=${SEARCH_DEPARTURE_DATETIME.toLocalTime().format(TIME_FORMAT)}" +
                            "&txtArvRsStnCd1=${SEARCH_DESTINATION_STATION.code}" +
                            "&txtTrnNo1=$TRAIN_NO" +
                            "&txtTrnClsfCd1=${TrainType.KTX.code}" +
                            "&txtTrnGpCd1=100" +
                            "&txtPsgTpCd1=${PassengerType.ADULT_YOUTH.code}" +
                            "&txtDiscKndCd1=000" +
                            "&txtCompaCnt1=1"
                    )
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "text/plain;charset=utf-8")
                    .withBody(
                        """
                            {
                              "h_msg_txt": "???????????????????????????. ?????? ??????????????? ????????????.",
                              "h_msg_cd": "P058",
                              "strResult": "FAIL"
                            }
                        """.trimIndent()
                    )
            )
    }

    private fun reserveSuccess() {
        generateMockServerClient()
            .`when`(
                HttpRequest.request()
                    .withMethod("POST")
                    .withPath(RESERVATION_PATH)
                    .withHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .withCookie(SESSION_COOKIE_NAME, JSESSIONID)
                    .withBody(
                        "Device=AD" +
                            "&txtSeatAttCd1=${SeatType.NORMAL.code}" +
                            "&txtSeatAttCd2=000" +
                            "&txtSeatAttCd3=000" +
                            "&txtSeatAttCd4=015" +
                            "&txtStndFlg=N" +
                            "&txtJrnyCnt=1" +
                            "&txtJrnySqno1=001" +
                            "&txtJrnyTpCd1=11" +
                            "&txtTotPsgCnt=1" +
                            "&txtDptRsStnCd1=${SEARCH_DEPARTURE_STATION.code}" +
                            "&txtDptDt1=${SEARCH_DEPARTURE_DATETIME.toLocalDate().format(DATE_FORMAT)}" +
                            "&txtDptTm1=${SEARCH_DEPARTURE_DATETIME.toLocalTime().format(TIME_FORMAT)}" +
                            "&txtArvRsStnCd1=${SEARCH_DESTINATION_STATION.code}" +
                            "&txtTrnNo1=$TRAIN_NO" +
                            "&txtTrnClsfCd1=${TrainType.KTX.code}" +
                            "&txtTrnGpCd1=100" +
                            "&txtPsgTpCd1=${PassengerType.ADULT_YOUTH.code}" +
                            "&txtDiscKndCd1=000" +
                            "&txtCompaCnt1=1"
                    )
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withHeader("Content-Type", "text/plain;charset=utf-8")
                    .withBody(
                        """
                            {
                              "h_seat_att_disc_flg": "",
                              "h_fmly_info_cfm_flg": "",
                              "jrny_infos": {
                                "jrny_info": [
                                  {
                                    "h_arv_rs_stn_cd": "0020",
                                    "h_trn_clsf_nm": "KTX",
                                    "h_dpt_rs_stn_nm": "??????",
                                    "h_jrny_tp_cd": "11",
                                    "h_trn_no": "009",
                                    "seat_infos": {
                                      "seat_info": [
                                        {
                                          "h_dcnt_reld_no": "",
                                          "h_etc_seat_att_cd": "",
                                          "h_srcar_no": "0010",
                                          "h_smk_seat_att_cd": "",
                                          "h_dir_seat_att_cd": "009",
                                          "h_dcnt_knd_cd2": "000",
                                          "h_cert_no": "",
                                          "h_rcvd_amt": "00000059800",
                                          "h_psrm_cl_nm": "?????????",
                                          "h_seat_att_cd_2": "009",
                                          "h_dcnt_knd_cd1_nm": "",
                                          "h_cert_dv_cd": "",
                                          "h_psrm_cl_cd": "1",
                                          "h_seat_no": "11A",
                                          "h_dcnt_knd_cd2_nm": "",
                                          "h_rq_seat_att_cd": "015",
                                          "h_sgr_nm": "",
                                          "h_dcnt_knd_cd1": "000",
                                          "h_seat_fare": "00000000000000",
                                          "h_loc_seat_att_cd": "012",
                                          "h_movie_psrm_flg": "",
                                          "h_disc_card_knd": "",
                                          "h_seat_prc": "00000000059800",
                                          "h_dcnt_knd_cd_nm1": "",
                                          "h_cont_seat_cnt": "0001",
                                          "h_frbs_cd": "",
                                          "h_tot_disc_amt": "00000000000",
                                          "h_disc_card_re_cnt": "000000000",
                                          "h_dcnt_knd_cd_nm2": "",
                                          "h_psg_tp_cd": "1",
                                          "h_bkcls_cd": "",
                                          "h_disc_card_use_cnt": "000000000"
                                        }
                                      ]
                                    },
                                    "h_dpt_dt": "20210701",
                                    "h_tot_stnd_cnt": "00000",
                                    "h_arv_tm": "093800",
                                    "h_ob_flg": "",
                                    "h_dpt_rs_stn_cd": "0001",
                                    "h_dpt_tm": "070000",
                                    "h_trn_clsf_cd": "00",
                                    "h_tot_seat_cnt": "00001",
                                    "h_fres_cnt": "00000",
                                    "h_arv_rs_stn_nm": "??????",
                                    "h_trn_gp_cd": "100",
                                    "h_seat_cnt": "000001"
                                  }
                                ]
                              },
                              "h_pay_cnt": "000",
                              "h_jrny_cnt": "0001",
                              "h_msg_txt3": "",
                              "h_msg_cd": "IRR000000",
                              "h_lunchbox_chg_flg": "",
                              "h_pay_limit_msg": "20??? ?????? ??????????????? ????????? ????????? ???????????????.",
                              "h_msg_txt2": "",
                              "h_tmp_job_sqno1": "12345",
                              "strResult": "SUCC",
                              "psgDiscAdd_infos": {
                                "psgDiscAdd_info": []
                              },
                              "h_tot_rcvd_amt": "0000000000059800",
                              "h_dlay_apv_txt": "",
                              "h_msg_txt": "???????????? ????????? ????????? ???????????????.\n\n1.????????? ?????????????????? ????????? ????????????.\n\n- ???????????? ~1????????? : 400???\n- 1??????~??????????????? : 10%\n* ?????????????????? ???????????? ??????????????? ??????????????? ?????????.\n* ????????? ??????????????? ???????????? ????????????.\n\n2. ????????????????????? ????????? ???????????? ??? ???????????? ?????? ??? ????????? ????????? ??? ????????????.\n\n3. ?????? ???????????? ???????????? ?????? ???????????? ????????? ??? ????????????.\n\n4. ????????????, ????????????, ?????? ??????, ???????????? ?????? ?????? ?????????????????????.\n1. ???????????? ???????????? ???????????? ????????? ????????????????????? ????????? ??? ????????????.\n\n2. ?????? ?????? ??? ????????? ???????????? ???????????? ?????? ????????? ???????????? ??????, ??????????????? ???????????????.\n??? ????????????????????? ????????? ????????? ????????? ????????????.",
                              "h_guide": "??? ???????????????????????? ????????? ?????? ????????????????????? ????????? ????????????, ?????? ?????? ??????????????????????? ?????????.\n?????????????????? ??? ???????????? ?????? ??? ????????? ????????? ??? ????????????.\n\n????????????(WiFi) ???????????? KTX?????? ???????????? ??? ????????????.",
                              "h_pre_stl_tgt_flg": "Y",
                              "h_ntisu_lmt": "20??? ?????? ??????????????? ????????? ????????? ???????????????.",
                              "h_dlay_apv_flg": "",
                              "h_msg_txt4": "",
                              "h_disc_cnt": "0000",
                              "h_disc_crd_reisu_flg": "",
                              "h_ntisu_lmt_tm": "233020",
                              "h_table_flg": "",
                              "h_ntisu_lmt_dt": "20210627",
                              "h_tmp_job_sqno2": "000000",
                              "h_psg_cnt": "0001",
                              "h_add_srv_flg": "Y",
                              "psg_infos": {
                                "psg_info": [
                                  {
                                    "h_dcsp_no": "",
                                    "h_psg_tp_cd": "1",
                                    "h_dcnt_knd_cd2": "",
                                    "h_dcnt_knd_cd": "",
                                    "h_dcsp_no2": "",
                                    "h_psg_info_per_prnb": "0001"
                                  }
                                ]
                              },
                              "h_msg_mndry": "",
                              "h_wct_no": "11223",
                              "h_pnr_no": "12345",
                              "h_msg_txt5": "???????????? ????????? ????????? ???????????????.\n\n1.????????? ?????????????????? ????????? ????????????.\n\n- ???????????? ~1????????? : 400???\n- 1??????~??????????????? : 10%\n* ?????????????????? ???????????? ??????????????? ??????????????? ?????????.\n* ????????? ??????????????? ???????????? ????????????.\n\n2. ????????????????????? ????????? ???????????? ??? ???????????? ?????? ??? ????????? ????????? ??? ????????????.\n\n3. ?????? ???????????? ???????????? ?????? ???????????? ????????? ??? ????????????.\n\n4. ????????????, ????????????, ?????? ??????, ???????????? ?????? ?????? ?????????????????????.\n1. ???????????? ???????????? ???????????? ????????? ????????????????????? ????????? ??? ????????????.\n\n2. ?????? ?????? ??? ????????? ???????????? ???????????? ?????? ????????? ???????????? ??????, ??????????????? ???????????????.\n??? ????????????????????? ????????? ????????? ????????? ????????????.",
                              "h_acnt_apv_no": "00000000000"
                            }
                        """.trimIndent()
                    )
            )
    }

    private fun searchTrains() {
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
                            "&txtPsgFlg_1=1" +
                            "&txtMenuId=11" +
                            "&txtGoAbrdDt=${SEARCH_DEPARTURE_DATETIME.toLocalDate().format(DATE_FORMAT)}" +
                            "&txtGoHour=${SEARCH_DEPARTURE_DATETIME.toLocalTime().format(TIME_FORMAT)}" +
                            "&txtGoStart=${URLEncoder.encode(SEARCH_DEPARTURE_STATION.label, Charsets.UTF_8)}" +
                            "&txtGoEnd=${URLEncoder.encode(SEARCH_DESTINATION_STATION.label, Charsets.UTF_8)}"
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
                              "h_msg_txt": "???????????????????????????",
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
                                    "h_chg_trn_dv_nm": "??????",
                                    "h_chg_trn_seq": "1",
                                    "h_dpt_rs_stn_cd": "0001",
                                    "h_dpt_rs_stn_nm": "??????",
                                    "h_arv_rs_stn_cd": "0020",
                                    "h_arv_rs_stn_nm": "??????",
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
                                    "h_trn_cps_nm1": "??????????????????",
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
                                    "h_spe_rsv_cd": "11",
                                    "h_spe_rsv_cd2": null,
                                    "h_spe_rsv_nm": "????????????",
                                    "h_spe_disc_rt": "0",
                                    "h_spe_seat_map_flg": "",
                                    "h_spe_psrm_cl_nm": "??????",
                                    "h_gen_rsv_cd": "11",
                                    "h_gen_rsv_cd2": null,
                                    "h_gen_rsv_nm": "????????????",
                                    "h_gen_disc_rt": "0",
                                    "h_gen_seat_map_flg": "",
                                    "h_gen_psrm_cl_nm": "?????????",
                                    "h_stnd_rsv_cd": "00",
                                    "h_stnd_rsv_nm": "-",
                                    "h_free_rsv_cd": "01",
                                    "h_free_rsv_nm": "????????????\n(3???)",
                                    "h_free_sracar_cnt": "003",
                                    "h_train_disc_gen_rt": "-005.00",
                                    "h_run_tm": "0238",
                                    "h_rd_add_info": "00",
                                    "h_nonstop_msg": "",
                                    "h_nonstop_msg_txt": "",
                                    "h_rd_seat_map_flg": "NN",
                                    "h_dpt_stn_cons_ordr": "000007",
                                    "h_arv_stn_cons_ordr": "000030",
                                    "h_dpt_stn_run_ordr": "000001",
                                    "h_arv_stn_run_ordr": "000008",
                                    "h_seat_att_cd": "",
                                    "h_rcvd_amt": "00000000059800",
                                    "h_rcvd_fare": "00000000023900",
                                    "h_cnec_trfc_psb_flg": "",
                                    "h_cnec_trfc_nd_hm": "",
                                    "h_cnec_trfc_rcvd_prc": "00000000000000",
                                    "h_rsv_psb_flg": "Y",
                                    "h_rsv_psb_nm": "59,800???\n5%??????",
                                    "h_stn_sale_flg": "N",
                                    "h_stn_sale_txt": "????????? ?????? ?????????\n????????? ????????? ??? ????????????.",
                                    "h_info_txt": "???????????? ????????? ?????? ????????? ?????? ????????? ?????? ?????? ????????? ?????? 0.6% ????????????.\n\n?????? ?????????????????????????",
                                    "h_trn_clsf_nm": "KTX",
                                    "h_trn_gp_nm": "KTX"
                                  },
                                  {
                                    "h_trn_seq": "001",
                                    "h_chg_trn_dv_cd": "1",
                                    "h_chg_trn_dv_nm": "??????",
                                    "h_chg_trn_seq": "1",
                                    "h_dpt_rs_stn_cd": "0001",
                                    "h_dpt_rs_stn_nm": "??????",
                                    "h_arv_rs_stn_cd": "0020",
                                    "h_arv_rs_stn_nm": "??????",
                                    "h_trn_no": "011",
                                    "h_trn_no_qb": "011",
                                    "h_yms_apl_flg": "Y",
                                    "h_trn_gp_cd": "100",
                                    "h_trn_clsf_cd": "00",
                                    "h_run_dt": "20210701",
                                    "h_dpt_dt": "20210701",
                                    "h_dpt_tm": "073000",
                                    "h_dpt_tm_qb": "07:30",
                                    "h_arv_dt": "20210701",
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
                                    "h_trn_cps_nm1": "??????????????????",
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
                                    "h_spe_rsv_cd": "11",
                                    "h_spe_rsv_cd2": null,
                                    "h_spe_rsv_nm": "????????????",
                                    "h_spe_disc_rt": "0",
                                    "h_spe_seat_map_flg": "",
                                    "h_spe_psrm_cl_nm": "??????",
                                    "h_gen_rsv_cd": "11",
                                    "h_gen_rsv_cd2": null,
                                    "h_gen_rsv_nm": "????????????",
                                    "h_gen_disc_rt": "0",
                                    "h_gen_seat_map_flg": "",
                                    "h_gen_psrm_cl_nm": "?????????",
                                    "h_stnd_rsv_cd": "00",
                                    "h_stnd_rsv_nm": "-",
                                    "h_free_rsv_cd": "01",
                                    "h_free_rsv_nm": "????????????\n(2???)",
                                    "h_free_sracar_cnt": "002",
                                    "h_train_disc_gen_rt": "-005.00",
                                    "h_run_tm": "0233",
                                    "h_rd_add_info": "00",
                                    "h_nonstop_msg": "",
                                    "h_nonstop_msg_txt": "",
                                    "h_rd_seat_map_flg": "NN",
                                    "h_dpt_stn_cons_ordr": "000007",
                                    "h_arv_stn_cons_ordr": "000030",
                                    "h_dpt_stn_run_ordr": "000002",
                                    "h_arv_stn_run_ordr": "000008",
                                    "h_seat_att_cd": "",
                                    "h_rcvd_amt": "00000000053900",
                                    "h_rcvd_fare": "00000000021600",
                                    "h_cnec_trfc_psb_flg": "",
                                    "h_cnec_trfc_nd_hm": "",
                                    "h_cnec_trfc_rcvd_prc": "00000000000000",
                                    "h_rsv_psb_flg": "Y",
                                    "h_rsv_psb_nm": "53,900???\n5%??????",
                                    "h_stn_sale_flg": "N",
                                    "h_stn_sale_txt": "????????? ?????? ?????????\n????????? ????????? ??? ????????????.",
                                    "h_info_txt": "???????????? ????????? ?????? ????????? ?????? ????????? ?????? ?????? ????????? ?????? 0.6% ????????????.\n\n?????? ?????????????????????????",
                                    "h_trn_clsf_nm": "KTX",
                                    "h_trn_gp_nm": "KTX"
                                  },
                                  {
                                    "h_trn_seq": "002",
                                    "h_chg_trn_dv_cd": "1",
                                    "h_chg_trn_dv_nm": "??????",
                                    "h_chg_trn_seq": "1",
                                    "h_dpt_rs_stn_cd": "0001",
                                    "h_dpt_rs_stn_nm": "??????",
                                    "h_arv_rs_stn_cd": "0020",
                                    "h_arv_rs_stn_nm": "??????",
                                    "h_trn_no": "025",
                                    "h_trn_no_qb": "025",
                                    "h_yms_apl_flg": "Y",
                                    "h_trn_gp_cd": "100",
                                    "h_trn_clsf_cd": "00",
                                    "h_run_dt": "20210701",
                                    "h_dpt_dt": "20210701",
                                    "h_dpt_tm": "110000",
                                    "h_dpt_tm_qb": "11:00",
                                    "h_arv_dt": "20210701",
                                    "h_arv_tm": "134100",
                                    "h_arv_tm_qb": "13:41",
                                    "h_expct_dlay_hr": "000000",
                                    "h_rsv_wait_ps_cnt": "0278",
                                    "h_dtour_flg": "",
                                    "h_dtour_txt": "-",
                                    "h_std_rest_seat_cnt": "",
                                    "h_fst_rest_seat_cnt": "",
                                    "h_car_tp_cd": "",
                                    "h_car_tp_nm": "",
                                    "h_trn_cps_cd1": "X",
                                    "h_trn_cps_nm1": "??????????????????",
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
                                    "h_spe_rsv_cd": "13",
                                    "h_spe_rsv_cd2": null,
                                    "h_spe_rsv_nm": "??????",
                                    "h_spe_disc_rt": "0",
                                    "h_spe_seat_map_flg": "",
                                    "h_spe_psrm_cl_nm": "??????",
                                    "h_gen_rsv_cd": "13",
                                    "h_gen_rsv_cd2": null,
                                    "h_gen_rsv_nm": "??????",
                                    "h_gen_disc_rt": "0",
                                    "h_gen_seat_map_flg": "",
                                    "h_gen_psrm_cl_nm": "?????????",
                                    "h_stnd_rsv_cd": "00",
                                    "h_stnd_rsv_nm": "-",
                                    "h_free_rsv_cd": "00",
                                    "h_free_rsv_nm": "-",
                                    "h_free_sracar_cnt": "000",
                                    "h_train_disc_gen_rt": "-005.00",
                                    "h_run_tm": "0241",
                                    "h_rd_add_info": "00",
                                    "h_nonstop_msg": "",
                                    "h_nonstop_msg_txt": "",
                                    "h_rd_seat_map_flg": "NN",
                                    "h_dpt_stn_cons_ordr": "000007",
                                    "h_arv_stn_cons_ordr": "000030",
                                    "h_dpt_stn_run_ordr": "000001",
                                    "h_arv_stn_run_ordr": "000008",
                                    "h_seat_att_cd": "",
                                    "h_rcvd_amt": "00000000059800",
                                    "h_rcvd_fare": "00000000023900",
                                    "h_cnec_trfc_psb_flg": "",
                                    "h_cnec_trfc_nd_hm": "",
                                    "h_cnec_trfc_rcvd_prc": "00000000000000",
                                    "h_rsv_psb_flg": "N",
                                    "h_rsv_psb_nm": "??????",
                                    "h_stn_sale_flg": "N",
                                    "h_stn_sale_txt": "????????? ?????? ?????????\n????????? ????????? ??? ????????????.",
                                    "h_info_txt": "???????????? ????????? ?????? ????????? ?????? ????????? ?????? ?????? ????????? ?????? 0.6% ????????????.\n\n?????? ?????????????????????????",
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

    private fun loginFailure() {
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
                              "h_msg_txt": "????????? ????????? ?????? ???????????????.\n????????? ???????????? ???????????? ?????? ??????????????? ????????????(?????????/????????????) ?????? ??????????????? ?????? ?????????????????????.\n??????????????? ?????? 5??? ????????? ????????? ???????????? ?????? ????????????.",
                              "h_msg_cd": "WRC000391",
                              "strResult": "SUCC"
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
                              "strGoffStnNm": "??????",
                              "strHdcpFlg": "F",
                              "strBtdt": "19990101",
                              "h_msg_cd": "IRZ000001",
                              "strResult": "SUCC",
                              "strEvtTgtFlg": "S",
                              "strEmailAdr": "test@naver.com",
                              "strSexDvCd": "M",
                              "strLognTpCd3": "N",
                              "strCustLeadFlgNm": "",
                              "h_msg_txt": "??????????????? ?????? ???????????????.",
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
                              "strCustNm": "?????????",
                              "strAthnFlg5": "Y",
                              "strCustClCd": "F",
                              "strHdcpTpCd": "",
                              "strYouthAgrFlg": "6",
                              "strHdcpTpCdNm": "",
                              "strAbrdStnNm": "??????",
                              "strGoffStnCd": "0020"
                            }
                        """.trimIndent()
                    )
            )
    }

    private fun generateMockServerClient() = MockServerClient(MOCK_SERVER_HOST, MOCK_SERVER_PORT)
}
