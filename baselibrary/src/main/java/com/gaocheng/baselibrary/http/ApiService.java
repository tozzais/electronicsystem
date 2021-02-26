package com.gaocheng.baselibrary.http;


import com.gaocheng.baselibrary.bean.net.BaseListResult;
import com.gaocheng.baselibrary.bean.net.BaseResult;
import com.gaocheng.baselibrary.bean.net.BasicDataBind;
import com.gaocheng.baselibrary.bean.net.BasicInformDetail;
import com.gaocheng.baselibrary.bean.net.BasicInfromListBean;
import com.gaocheng.baselibrary.bean.net.BeautifulHomeBean;
import com.gaocheng.baselibrary.bean.net.ConstructSubjectBean;
import com.gaocheng.baselibrary.bean.net.ManageLevelBean;
import com.gaocheng.baselibrary.bean.net.MessageBeanForRain;
import com.gaocheng.baselibrary.bean.net.MessageBeanForRiver;
import com.gaocheng.baselibrary.bean.net.RainGisItemBean;
import com.gaocheng.baselibrary.bean.net.RainHistoryBean;
import com.gaocheng.baselibrary.bean.net.RainInfromUpdataLoadData;
import com.gaocheng.baselibrary.bean.net.RainOnsiteAssessmentLoadData;
import com.gaocheng.baselibrary.bean.net.RainTransDetail;
import com.gaocheng.baselibrary.bean.net.RainTransListBean;
import com.gaocheng.baselibrary.bean.net.RemediationSituationBean;
import com.gaocheng.baselibrary.bean.net.RiverBasicInfoListBean;
import com.gaocheng.baselibrary.bean.net.RiverGisBasicInfo;
import com.gaocheng.baselibrary.bean.net.RiverGisRegulationInfo;
import com.gaocheng.baselibrary.bean.net.RiverGisResult;
import com.gaocheng.baselibrary.bean.net.RiverGisUnitInfo;
import com.gaocheng.baselibrary.bean.net.RiverHistoryBean;
import com.gaocheng.baselibrary.bean.net.RiverRegulationInfoListBean;
import com.gaocheng.baselibrary.bean.net.RiverRegulationLoadData;
import com.gaocheng.baselibrary.bean.net.TownBean;
import com.gaocheng.baselibrary.bean.net.TransFormBean;
import com.gaocheng.baselibrary.bean.net.UnitsBean;
import com.gaocheng.baselibrary.bean.net.VersionBean;
import com.gaocheng.baselibrary.bean.request.RequestBasicInformBean;
import com.gaocheng.baselibrary.bean.request.RequestBasicInformSearchBean;
import com.gaocheng.baselibrary.bean.request.RequestEiId;
import com.gaocheng.baselibrary.bean.request.RequestId;
import com.gaocheng.baselibrary.bean.request.RequestMessageBean;
import com.gaocheng.baselibrary.bean.request.RequestRainGisList;
import com.gaocheng.baselibrary.bean.request.RequestRainTransBean;
import com.gaocheng.baselibrary.bean.request.RequestRiverBasicInfoBean;
import com.gaocheng.baselibrary.bean.request.RequestRiverBasicInfoSearchBean;
import com.gaocheng.baselibrary.bean.request.RequestRiverGisList;
import com.gaocheng.baselibrary.bean.request.RequestRiverID;
import com.gaocheng.baselibrary.bean.request.RequestRiverMessageBean;
import com.gaocheng.baselibrary.bean.request.RequestRiverRegulationInfoBean;
import com.gaocheng.baselibrary.bean.request.RequestRiverRegulationInfoSearchBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by jumpbox on 16/5/2.
 */
public interface ApiService {


    @POST(HttpUrl.basic_inform_list)
    Observable<BaseResult<BasicInfromListBean>>
    getBasicInformList(@Body RequestBasicInformBean body);

    @POST(HttpUrl.basic_inform_list_for_search)
    Observable<BaseResult<BasicInfromListBean>>
    getBasicInformList(@Body RequestBasicInformSearchBean body);

    @POST(HttpUrl.rain_trans_list)
    Observable<BaseResult<RainTransListBean>>
    getRainTransList(@Body RequestRainTransBean body);

    @POST(HttpUrl.gis_search)
    Observable<BaseResult<List<RainGisItemBean>>>
    getRainGisList(@Body RequestRainGisList body);

    @POST(HttpUrl.rain_trans_list_for_search)
    Observable<BaseResult<RainTransListBean>>
    getRainTransList(@Body RequestBasicInformSearchBean body);
//
    @POST(HttpUrl.town)
    Observable<BaseListResult<TownBean>>
    getTownList();

    @POST(HttpUrl.binddata)
    Observable<BaseListResult<BasicDataBind>>
    getBindDate();
//
    @POST(HttpUrl.gis_house_data)
    Observable<BaseResult<BasicInformDetail>>
    getBasicInformDetail(@Body RequestId body);

    @POST(HttpUrl.rain_trans_detail)
    Observable<BaseResult<RainTransDetail>>
    getRainTransDetail(@Body RequestId body);

    @POST(HttpUrl.unit_data)
    Observable<BaseListResult<UnitsBean>>
    getUnitList(@Body RequestId body);

    @POST(HttpUrl.count_build)
    Observable<BaseListResult<ConstructSubjectBean>>
    getConstructSubjectList();

    @POST(HttpUrl.count_house)
    Observable<BaseListResult<BeautifulHomeBean>>
    getBeautifulHomeList();

    @POST(HttpUrl.count_transform)
    Observable<BaseListResult<TransFormBean>>
    getTransFromList();

    @POST(HttpUrl.message_search)
    Observable<BaseResult<MessageBeanForRain>>
    getMessageListForRain(@Body RequestMessageBean body);

    @Multipart
    @POST(HttpUrl.inform_update)
    Observable<BaseResult>
    getInFormUpdataForRain(@Part("EI_ID") RequestBody EI_ID,
                           @Part("HouDes") RequestBody HouDes,
                           @Part("Des") RequestBody Des,
                           @Part("Res") RequestBody Res,
                           @Part("Rtime") RequestBody Rtime,
                           @Part("Note") RequestBody Note
            ,@Part() List<MultipartBody.Part> parts);
    @Multipart
    @POST(HttpUrl.onsite_access)
    Observable<BaseResult>
    getOnsiteAccessForRain(@Part("ER_ID") RequestBody EI_ID, @Part("Content")RequestBody Content,
                           @Part("Note")RequestBody Note, @Part("Advise")RequestBody Advise,
                           @Part() List<MultipartBody.Part> parts);

    //雨污 信息更新数据加载
    @POST(HttpUrl.inform_update_loaddata)
    Observable<BaseResult<RainInfromUpdataLoadData>>
    getInformUpdataData(@Body RequestEiId body);

    //雨污 现场评估 数据加载
    @POST(HttpUrl.residentialquartersloaddata)
    Observable<BaseResult<RainOnsiteAssessmentLoadData>>
    getOnsiteAssessmentData(@Body RequestEiId body);

    @POST(HttpUrl.count_history)
    Observable<BaseListResult<RainHistoryBean>>
    getRainHistory();

    /**
************************************请求河道信息***********************************************
     */

    /**
     * 请求河道基本信息列表
     * @param body
     * @return
     */
    @POST(HttpUrl.river_basic_data_list)
    Observable<BaseResult<RiverBasicInfoListBean>>
    getRiverBasicInfoList(@Body RequestRiverBasicInfoBean body);
    /**
     * 请求河道基本信息列表（条件查询）
     * @param body
     * @return
     */
    @POST(HttpUrl.river_basic_data_list_MH)
    Observable<BaseResult<RiverBasicInfoListBean>>
    getRiverBasicInfoList(@Body RequestRiverBasicInfoSearchBean body);
    /**
     * 请求河道整治信息列表
     * @param body
     * @return
     */
    @POST(HttpUrl.river_regulation_data_list)
    Observable<BaseResult<RiverRegulationInfoListBean>>
    getRiverRegulationInfoList(@Body RequestRiverRegulationInfoBean body);
    /**
     * 请求河道整治信息列表（条件查询）
     * @param body
     * @return
     */
    @POST(HttpUrl.river_regulation_data_list_MH)
    Observable<BaseResult<RiverRegulationInfoListBean>>
    getRiverRegulationInfoList(@Body RequestRiverRegulationInfoSearchBean body);

    @POST(HttpUrl.river_gis_data)
    Observable<BaseResult<RiverGisResult>>
    getRiverGisList(@Body RequestRiverGisList body);

    /**
     * 请求河道Gis基本信息
     * @param body
     * @return
     */
    @POST(HttpUrl.river_gis_basic_data)
    Observable<BaseResult<RiverGisBasicInfo>>
    getRiverGisBasicInfo(@Body RequestId body);
    /**
     * 请求河道Gis整治信息
     * @param body
     * @return
     */
    @POST(HttpUrl.river_gis_regulation_data)
    Observable<BaseResult<RiverGisRegulationInfo>>
    getRiverGisRegulationInfo(@Body RequestId body);
    /**
     * 请求河道Gis实施单位信息
     * @param body
     * @return
     */
    @POST(HttpUrl.river_gis_unit_data)
    Observable<BaseResult<RiverGisUnitInfo>>
    getRiverGisUnitInfo(@Body RequestId body);

    /**
     * 整治情况
     * @return
     */
    @POST(HttpUrl.river_remediation_situation)
    Observable<BaseListResult<RemediationSituationBean>>
    getRemediationSituationList();
    /**
     * 管理等级
     * @return
     */
    @POST(HttpUrl.river_manage_level)
    Observable<BaseListResult<ManageLevelBean>>
    getManageLevelList();
    /**
     * 历年整治
     * @return
     */
    @POST(HttpUrl.river_history_remediation)
    Observable<BaseListResult<RiverHistoryBean>>
    getHistoryRemediationList();

    /**
     * 整治计划
     * @return
     */
    @POST(HttpUrl.river_remediation_plan)
    Observable<BaseListResult<RiverHistoryBean>>
    getRemediationPlanList();

    @POST(HttpUrl.river_message)
    Observable<BaseResult<MessageBeanForRiver>>
    getMessageListForRiver(@Body RequestRiverMessageBean body);


    /**
     * 河道 信息更新数据加载
     * @param body
     * @return
     */
    @POST(HttpUrl.river_infrom_update_loaddata)
    Observable<BaseResult<RiverRegulationLoadData>>
    getRiverRegulationUpdataData(@Body RequestRiverID body);

    /**
     * 河道信息 更新上传
     * @param RiverID
     * @param RenovationSituation
     * @param RenovationEffect
     * @param FinishTime
     * @param Note
     * @param parts
     * @return
     */
    @Multipart
    @POST(HttpUrl.river_infrom_update)
    Observable<BaseResult>
    getInfoUpLoadForRiver(@Part("RiverID") RequestBody RiverID,
                           @Part("RenovationSituation") RequestBody RenovationSituation,
                           @Part("RenovationEffect") RequestBody RenovationEffect,
                           @Part("FinishTime") RequestBody FinishTime,
                           @Part("Note") RequestBody Note,
                          @Part() List<MultipartBody.Part> parts);

    @Multipart
    @POST(HttpUrl.river_onsite_evaluation)
    Observable<BaseResult>
    getOnsiteEvaluationForRiver(@Part("RiverID") RequestBody RiverID,
                          @Part("EvaluateDescribe") RequestBody RenovationEffect,
                          @Part("proposal") RequestBody FinishTime,
                          @Part() List<MultipartBody.Part> parts);


    @POST(HttpUrl.version)
    Observable<VersionBean>
    getVersion();

    /**
     * 河道 现场评估 数据加载
     * @param body
     * @return
     */
    /*@POST(HttpUrl.river_onsite_evaluation_loaddata)
    Observable<BaseResult<River>>
    getOnsiteAssessmentData(@Body RequestEiId body);*/
}
