package com.greencross.gctemperlib.greencare.bluetooth.manager;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.greencross.gctemperlib.greencare.network.tr.data.Tr_bdsg_dose_medicine_input;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_bdsg_info_input_data;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_bdwgh_goal_input;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_bdwgh_info_input_data;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_brssr_info_input_data;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.common.CommonData;
import com.greencross.gctemperlib.greencare.base.BaseFragment;
import com.greencross.gctemperlib.greencare.bluetooth.model.BandModel;
import com.greencross.gctemperlib.greencare.bluetooth.model.BloodModel;
import com.greencross.gctemperlib.greencare.bluetooth.model.MessageModel;
import com.greencross.gctemperlib.greencare.bluetooth.model.PressureModel;
import com.greencross.gctemperlib.greencare.bluetooth.model.WaterModel;
import com.greencross.gctemperlib.greencare.bluetooth.model.WeightModel;
import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.database.DBHelper;
import com.greencross.gctemperlib.greencare.database.DBHelperMessage;
import com.greencross.gctemperlib.greencare.database.DBHelperPPG;
import com.greencross.gctemperlib.greencare.database.DBHelperPresure;
import com.greencross.gctemperlib.greencare.database.DBHelperStep;
import com.greencross.gctemperlib.greencare.database.DBHelperStepRealtime;
import com.greencross.gctemperlib.greencare.database.DBHelperSugar;
import com.greencross.gctemperlib.greencare.database.DBHelperWater;
import com.greencross.gctemperlib.greencare.database.DBHelperWeight;
import com.greencross.gctemperlib.greencare.network.tr.ApiData;
import com.greencross.gctemperlib.greencare.network.tr.BaseData;
import com.greencross.gctemperlib.greencare.network.tr.BaseUrl;
import com.greencross.gctemperlib.greencare.network.tr.CConnAsyncTask;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_get_hedctdata;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_infra_message_write;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_mvm_info_input_data;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_ppg_info_input_data;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_water_goalqy;
import com.greencross.gctemperlib.greencare.network.tr.data.Tr_water_info_input_data;
import com.greencross.gctemperlib.greencare.util.CDateUtil;
import com.greencross.gctemperlib.greencare.util.Logger;
import com.greencross.gctemperlib.greencare.util.NetworkUtil;
import com.greencross.gctemperlib.greencare.util.SharedPref;
import com.greencross.gctemperlib.greencare.util.StringUtil;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.greencross.gctemperlib.greencare.util.CDateUtil.getForamtyyMMddHHmmssSS;

/**
 * Created by MrsWin on 2017-04-09.
 */

public class DeviceDataUtil {

    private final String TAG = DeviceDataUtil.class.getSimpleName();

    public static final String DEVICE_BROCAST_RECEIVER = "com.appmd.hi.gngcare.greencare.broadcast";
    public static final String DEVICE_SUGAR_MESSAGE = "device_sugar_message";
    public static final String DEVICE_SUGAR_VAL = "device_sugar_val";

    Handler mHandler = new Handler();

    /**
     * 걸음 데이터 서버 및 sqlite에 저장
     *
     * @param dataModel
     */
    public void uploadStepByData(final Context baseFragment, final List<BandModel> dataModel) {
        uploadStepByData(baseFragment, dataModel, null);
    }

    /**
     * 걸음 데이터 서버 및 sqlite에 저장
     *
     * @param dataModel
     */
    public void uploadStepData(final Context baseFragment, final List<BandModel> dataModel) {
        uploadStepData(baseFragment, dataModel, null);
    }

    /**
     * 심박수 데이터 서버 및 sqlite에 저장
     *
     * @param dataModel
     */
    public void uploadPPGData(final BaseFragment baseFragment, final List<BandModel> dataModel) {
        uploadPPGData(baseFragment, dataModel, null);
    }

    /**
     * 서버에 데이터 등록
     *
     * @param baseFragment
     * @param dataModel
     * @param iBluetoothResult
     */
    public void uploadStepByData(final Context baseFragment, final List<BandModel> dataModel, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        Tr_mvm_info_input_data inputData = new Tr_mvm_info_input_data();
        CommonData login = CommonData.getInstance(baseFragment);
        Tr_mvm_info_input_data.RequestData requestData = new Tr_mvm_info_input_data.RequestData();
        requestData.mber_sn = login.getMberSn();
        requestData.ast_mass = inputData.getArray(dataModel, "D");

        getData(baseFragment, inputData.getClass(), requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_mvm_info_input_data) {
                    Tr_mvm_info_input_data data = (Tr_mvm_info_input_data) obj;
                    if ("Y".equals(data.reg_yn)) {
                        registStepByDB(baseFragment, dataModel, true);

                        if (iBluetoothResult != null)
                            iBluetoothResult.onResult(true);
                    } else {
                        CDialog.showDlg(baseFragment, baseFragment.getString(R.string.text_regist_fail));
                    }
                }
            }
        }, new ApiData.IFailStep() {
            @Override
            public void fail() {
//                registStepDB(baseFragment, dataModel, false);

                if (iBluetoothResult != null)
                    iBluetoothResult.onResult(false);
            }
        });
    }

    /**
     * 서버에 데이터 등록
     *
     * @param baseFragment
     * @param dataModel
     * @param iBluetoothResult
     */
    public void uploadStepData(final Context baseFragment, final List<BandModel> dataModel, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        Tr_mvm_info_input_data inputData = new Tr_mvm_info_input_data();
        CommonData login = CommonData.getInstance(baseFragment);

        Tr_mvm_info_input_data.RequestData requestData = new Tr_mvm_info_input_data.RequestData();
        requestData.mber_sn = login.getMberSn();
        requestData.ast_mass = inputData.getArray(dataModel, "D");

        getData(baseFragment, inputData.getClass(), requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_mvm_info_input_data) {
                    Tr_mvm_info_input_data data = (Tr_mvm_info_input_data) obj;
                    if ("Y".equals(data.reg_yn)) {
//                        registStepDB(baseFragment, dataModel, true);

                        if (iBluetoothResult != null)
                            iBluetoothResult.onResult(true);
                    } else {
                        CDialog.showDlg(baseFragment, baseFragment.getString(R.string.text_regist_fail));
                    }
                }
            }
        }, new ApiData.IFailStep() {
            @Override
            public void fail() {
//                registStepDB(baseFragment, dataModel, false);
                if (iBluetoothResult != null)
                    iBluetoothResult.onResult(false);
            }
        });
    }

    public void getData(final Context context, final Class<? extends BaseData> cls, final Object obj, boolean isShowProgress, final ApiData.IStep step, final ApiData.IFailStep failStep) {
        BaseData tr = createTrClass(cls, context);
        if (NetworkUtil.getConnectivityStatus(context) == false) {
            CDialog.showDlg(context, "네트워크 연결 상태를 확인해주세요.");
            return;
        }
//        String url = "http://wkd.walkie.co.kr/SK/WebService/SK_Mobile_Call.asmx/SK_mobile_Call";
        String url = BaseUrl.COMMON_URL;

        Logger.i(TAG, "LoadBalance.cls=" + cls + ", url=" + url);
//        if (TextUtils.isEmpty(url) && (cls != Tr_get_infomation.class)) {
//            getInformation(context, cls, obj, step);
//            return;
//        }
//        if(!cls.getName().equals(Tr_hra_check_result_input.class.getName())) {
//            if (isShowProgress)
//                showProgress();
//        }

        CConnAsyncTask.CConnectorListener queryListener = new CConnAsyncTask.CConnectorListener() {

            @Override
            public Object run() throws Exception {

                ApiData data = new ApiData();
                return data.getData(context, tr, obj);
            }

            @Override
            public void view(CConnAsyncTask.CQueryResult result) {
//                hideProgress();

                if (result.result == CConnAsyncTask.CQueryResult.SUCCESS && result.data != null) {
                    if (step != null) {
                        step.next(result.data);
                    }

                } else {
                    //mBaseActivity.hideProgressForce();
                    if (failStep != null) {
                        failStep.fail();
                    } else {

                        CDialog.showDlg(context, "데이터 수신에 실패 하였습니다.");
                        Log.e(TAG, "CConnAsyncTask error=" + result.errorStr);
//                        hideProgress();
                    }
                }
            }
        };

        CConnAsyncTask asyncTask = new CConnAsyncTask();
        asyncTask.execute(queryListener);
    }

    private BaseData createTrClass(Class<? extends BaseData> cls, Context context) {
        BaseData trClass = null;
        try {
            Constructor<? extends BaseData> co = cls.getConstructor();
            trClass = co.newInstance();
        } catch (Exception e) {
            try {
                Constructor<? extends BaseData> co = cls.getConstructor(Context.class);
                trClass = co.newInstance(context);
            } catch (Exception e2) {
                Log.e(TAG, "createTrClass", e2);
            }
        }

        return trClass;
    }


    /**
     * 서버에 데이터 등록
     *
     * @param baseFragment
     * @param dataModel
     * @param iBluetoothResult
     */
    public void uploadStepRealTimeData(final Context baseFragment, final BandModel dataModel, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        registStepDB(baseFragment, dataModel, true);
    }

    /**
     * 걸음 실시간 데이터 Sqlite에 저장하기
     *
     * @param baseFragment
     * @param model
     * @param isServerRegist
     */
    private void registStepDB(Context baseFragment, BandModel model, boolean isServerRegist) {

        DBHelper helper = new DBHelper(baseFragment);
        DBHelperStepRealtime db = helper.getmStepRtimeDb();
        db.insert(model, isServerRegist);
    }

    /**
     * 걸음 데이터 Sqlite에 저장하기
     *
     * @param baseFragment
     * @param dataModel
     * @param isServerRegist
     */
    private void registStepDB(Context baseFragment, List<BandModel> dataModel, boolean isServerRegist) {
        DBHelper helper = new DBHelper(baseFragment);
        DBHelperStep db = helper.getStepDb();
        db.insert(dataModel, isServerRegist);
    }

    /**
     * 걸음 데이터 Sqlite에 저장하기
     *
     * @param baseFragment
     * @param dataModel
     * @param isServerRegist
     */
    private void registStepByDB(Context baseFragment, List<BandModel> dataModel, boolean isServerRegist) {
        DBHelper helper = new DBHelper(baseFragment);
        DBHelperStep db = helper.getStepDb();
        db.insert2(dataModel, isServerRegist);
    }

    /**
     * 심박수 데이터 Sqlite에 저장하기
     *
     * @param baseFragment
     * @param dataModel
     * @param isServerRegist
     */
    private void registPPGDB(BaseFragment baseFragment, List<BandModel> dataModel, boolean isServerRegist) {
        DBHelper helper = new DBHelper(baseFragment.getContext());
        DBHelperPPG db = helper.getPPGDb();
        db.insert(dataModel, isServerRegist);
    }

    /**
     * 혈압데이터 서버 및 sqlite에 저장
     *
     * @param pressureModel
     */
    public void uploadPresure(final BaseFragment baseFragment, final PressureModel pressureModel, final BluetoothManager.IBluetoothResult iBluetoothResult) {
        Tr_brssr_info_input_data inputData = new Tr_brssr_info_input_data();
        CommonData login = CommonData.getInstance(baseFragment.getContext());

        Tr_brssr_info_input_data.RequestData requestData = new Tr_brssr_info_input_data.RequestData();
        requestData.mber_sn = login.getMberSn();
        requestData.ast_mass = inputData.getArray(pressureModel);

        baseFragment.getData(baseFragment.getContext(), inputData.getClass(), requestData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_brssr_info_input_data) {
                    Tr_brssr_info_input_data data = (Tr_brssr_info_input_data) obj;
                    if ("Y".equals(data.reg_yn)) {
                        if (pressureModel.getDiastolicPressure() > 0.0f && pressureModel.getSystolicPressure() > 0.0f) {
                            insertPressureMessage(baseFragment, pressureModel);
                        }
                        registPresureDB(baseFragment, pressureModel, true);
                        iBluetoothResult.onResult(true);
                    } else {
                        CDialog.showDlg(baseFragment.getContext(), baseFragment.getContext().getString(R.string.text_regist_fail));
                    }

                } else {
                    if (pressureModel.getDiastolicPressure() > 0.0f && pressureModel.getSystolicPressure() > 0.0f) {
                        insertPressureMessage(baseFragment, pressureModel);
                    }
                    registPresureDB(baseFragment, pressureModel, false);
                    iBluetoothResult.onResult(false);
                }
            }
        });
    }

    private void registPresureDB(BaseFragment baseFragment, PressureModel pressureModel, boolean isServerRegist) {
        DBHelper helper = new DBHelper(baseFragment.getContext());
        DBHelperPresure db = helper.getPresureDb();
        db.insert(helper, pressureModel, isServerRegist);
    }

    /**
     * 혈압에 대한 투약정보 넣기
     *
     * @param baseFragment
     * @param iBluetoothResult
     */
    public void uploadPresureDrug(final BaseFragment baseFragment, final Tr_bdsg_dose_medicine_input.RequestData requestData, final BluetoothManager.IBluetoothResult iBluetoothResult) {
        Tr_bdsg_dose_medicine_input inputData = new Tr_bdsg_dose_medicine_input();
        CommonData login = CommonData.getInstance(baseFragment.getContext());

        baseFragment.getData(baseFragment.getContext(), inputData.getClass(), requestData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_bdsg_dose_medicine_input) {
                    Tr_bdsg_dose_medicine_input data = (Tr_bdsg_dose_medicine_input) obj;
                    if ("Y".equals(data.reg_yn)) {

                        iBluetoothResult.onResult(true);
                    } else {
                        CDialog.showDlg(baseFragment.getContext(), baseFragment.getContext().getString(R.string.text_regist_fail));
                    }
                } else {
                    registPresureDB(baseFragment, requestData, false);
                    iBluetoothResult.onResult(false);
                }
            }
        });
    }

    private void registPresureDB(BaseFragment baseFragment, Tr_bdsg_dose_medicine_input.RequestData requestData, boolean isServerRegist) {
        DBHelper helper = new DBHelper(baseFragment.getContext());
        DBHelperPresure db = helper.getPresureDb();
        db.insert(helper, requestData, true);
    }

    /**
     * 혈당 데이터 서버 및 sqlite에 저장
     *
     * @param dataModel
     */
    public void uploadSugarData(final BaseFragment baseFragment, final SparseArray<BloodModel> dataModel, boolean isMedicen, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        Tr_bdsg_info_input_data inputData = new Tr_bdsg_info_input_data();
        CommonData login = CommonData.getInstance(baseFragment.getContext());

        Tr_bdsg_info_input_data.RequestData requestData = new Tr_bdsg_info_input_data.RequestData();
        requestData.mber_sn = login.getMberSn();
        requestData.ast_mass = inputData.getArray(dataModel);

        // 투약이 아닌경우 만 메시지 입력
        if (isMedicen == false)
            insertSugarMessage(baseFragment, dataModel);

        baseFragment.getData(baseFragment.getContext(), inputData.getClass(), requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_bdsg_info_input_data) {
                    Tr_bdsg_info_input_data data = (Tr_bdsg_info_input_data) obj;
                    boolean isServerReg = "Y".equals(data.reg_yn);
                    if (isServerReg) {
                        registSugarDB(baseFragment, dataModel, true);

                        if (dataModel.size() > 0) {
                            BloodModel model = dataModel.get(dataModel.keyAt(dataModel.size() - 1));
                            if (model.getSugar() > 0.0f) {
                                insertSugarMessage(baseFragment, dataModel);
                            }
                        }

                        if (iBluetoothResult != null)
                            iBluetoothResult.onResult(true);
                    } else {
                        CDialog.showDlg(baseFragment.getContext(), baseFragment.getContext().getString(R.string.text_regist_fail));
                    }
                }
            }
        }, new ApiData.IFailStep() {
            @Override
            public void fail() {
                registSugarDB(baseFragment, dataModel, false);

                if (dataModel.size() > 0) {
                    BloodModel model = dataModel.get(dataModel.keyAt(dataModel.size() - 1));
                    if (model.getSugar() > 0.0f) {
                        insertSugarMessage(baseFragment, dataModel);
                    }
                }
                if (iBluetoothResult != null)
                    iBluetoothResult.onResult(false);
            }
        });
    }
//
//    /**
//     * 혈당계로 받은 메시지 LocalBroadCast로 전달
//     * @param baseFragment
//     * @param dataModel
//     */
//    public void sendBroadCastSugar(BaseFragment baseFragment, SparseArray<BloodModel> dataModel) {
//        // 메시지 DB 등록하기
//        if (dataModel.size() > 0) {
//            BloodModel model        = dataModel.get(dataModel.keyAt(dataModel.size() - 1));
//
//            String message = getSugarMessage(model.getBefore(), model.getSugar());
//            if (TextUtils.isEmpty(message) == false) {
//                SharedPref.getInstance(baseFragment.getContext()).savePreferences(SharedPref.HEALTH_MESSAGE_SUGAR, true);
//
//                MessageModel messageModel = new MessageModel();
//                messageModel.setIdx(getForamtyyMMddHHmmssSS(new Date(System.currentTimeMillis())));
//                messageModel.setSugar("" + model.getSugar());
//                messageModel.setRegdate("" + model.getRegTime());
//                messageModel.setMessage(message);
//                messageModel.setInfraType(Tr_infra_message_write.INFRA_TY_SUGAR);
//
//                Intent intent = new Intent(DeviceDataUtil.DEVICE_BROCAST_RECEIVER);
//                intent.putExtra(DEVICE_SUGAR_MESSAGE, message);
//                intent.putExtra(DEVICE_SUGAR_VAL, model.getSugar());
//
//                LocalBroadcastManager.getInstance(baseFragment.getContext()).sendBroadcast(intent);
////                insertMesageDb(baseFragment, messageModel);
//            }
//        }
//    }

    /**
     * 건강메시지 sqlite 등록하기(혈당)
     *
     * @param baseFragment
     * @param dataModel
     */
    public void insertSugarMessage(BaseFragment baseFragment, SparseArray<BloodModel> dataModel) {
        // 메시지 DB 등록하기
        if (dataModel.size() > 0) {
            BloodModel model = dataModel.get(dataModel.keyAt(dataModel.size() - 1));

            String message = getSugarMessage(baseFragment.getContext(), model.getBefore(), model.getSugar());
            if (TextUtils.isEmpty(message) == false) {
                SharedPref.getInstance(baseFragment.getContext()).savePreferences(SharedPref.HEALTH_MESSAGE_SUGAR, true);

                MessageModel messageModel = new MessageModel();
                messageModel.setIdx(getForamtyyMMddHHmmssSS(new Date(System.currentTimeMillis())));
                messageModel.setSugar("" + model.getSugar());
                messageModel.setRegdate("" + model.getRegTime());
                messageModel.setMessage(message);
                messageModel.setInfraType(Tr_infra_message_write.INFRA_TY_SUGAR);

                insertMesageDb(baseFragment, messageModel);
            }
        }
    }

    /**
     * 건강메시지 sqlite 등록하기(혈압)
     *
     * @param baseFragment
     * @param model
     **/
    private void insertPressureMessage(BaseFragment baseFragment, PressureModel model) {
        // 메시지 DB 등록하기
        String message = getPressureMessage(baseFragment, (int) model.getSystolicPressure(), (int) model.getDiastolicPressure());
        if (TextUtils.isEmpty(message) == false) {
            SharedPref.getInstance(baseFragment.getContext()).savePreferences(SharedPref.HEALTH_MESSAGE_HEALTH, true);

            MessageModel messageModel = new MessageModel();
            messageModel.setIdx(getForamtyyMMddHHmmssSS(new Date(System.currentTimeMillis())));
            try {

                Thread.sleep(100);
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageModel.setHeightpressure("" + model.getDiastolicPressure());
            messageModel.setLowpressure("" + model.getSystolicPressure());
            messageModel.setRegdate(model.getRegdate());
            messageModel.setMessage(message);
            messageModel.setInfraType(Tr_infra_message_write.INFRA_TY_HEALTH);
            insertMesageDb(baseFragment, messageModel);
        }
    }

    /**
     * 건강메시지 sqlite 등록하기(체지방)
     *
     * @param baseFragment
     **/
    private void insertWeightMessage(BaseFragment baseFragment, String weight, String reg, String fat) {
        // 메시지 DB 등록하기

//        String message = getWeightMessage(baseFragment, weight, fat);
        String message = getWeightMessage(baseFragment.getContext(), weight, fat);
        if (TextUtils.isEmpty(message) == false) {
            SharedPref.getInstance(baseFragment.getContext()).savePreferences(SharedPref.HEALTH_MESSAGE_HEALTH, true);
            MessageModel messageModel = new MessageModel();
            messageModel.setIdx(getForamtyyMMddHHmmssSS(new Date(System.currentTimeMillis())));
            try {

                Thread.sleep(100);
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageModel.setWeight("" + weight);
            messageModel.setRegdate(reg);
            messageModel.setMessage(message);
            messageModel.setInfraType(Tr_infra_message_write.INFRA_TY_HEALTH);

            insertMesageDb(baseFragment, messageModel);
        }
    }

    /**
     * 건강메시지 서버 전송 및 sqlite 저장
     *
     * @param baseFragment
     * @param model
     */
    private void insertMesageDb(final BaseFragment baseFragment, final MessageModel model) {
        Tr_infra_message_write.RequestData reqData = new Tr_infra_message_write.RequestData();
        CommonData login = CommonData.getInstance(baseFragment.getContext());
        reqData.idx = model.getIdx();
        reqData.mber_sn = login.getMberSn();
        reqData.infra_message = model.getMessage();
        reqData.infra_ty = model.getInfraType();

        baseFragment.getData(baseFragment.getContext(), Tr_infra_message_write.class, reqData, new ApiData.IStep() {
            @Override
            public void next(Object obj) {

                DBHelper helper = new DBHelper(baseFragment.getContext());
                DBHelperMessage db = helper.getMessageDb();
                if (obj instanceof Tr_infra_message_write) {
                    Tr_infra_message_write data = (Tr_infra_message_write) obj;
                    db.insert(model, "Y".equals(data.reg_yn));
                } else {
                    db.insert(model, false);
                }
            }
        });
    }

    /**
     * 혈당 sqlite 저장하기
     *
     * @param baseFragment
     * @param dataModel
     * @param isServerRegist
     */
    public void registSugarDB(BaseFragment baseFragment, SparseArray<BloodModel> dataModel, boolean isServerRegist) {
        DBHelper helper = new DBHelper(baseFragment.getContext());
        DBHelperSugar db = helper.getSugarDb();
        db.insert(dataModel, isServerRegist);
    }


    /**
     * 물 데이터 업로드 및 Sqlite저장
     *
     * @param baseFragment
     * @param dataModel
     * @param iBluetoothResult
     */
    public void uploadWaterData(final BaseFragment baseFragment, final SparseArray<WaterModel> dataModel, final String TargetAmount, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        Tr_water_info_input_data inputData = new Tr_water_info_input_data();
        CommonData login = CommonData.getInstance(baseFragment.getContext());

        Tr_water_info_input_data.RequestData requestData = new Tr_water_info_input_data.RequestData();
        requestData.mber_sn = login.getMberSn();
        requestData.ast_mass = inputData.getArray(dataModel);

        //섭취량 등록
        baseFragment.getData(baseFragment.getContext(), inputData.getClass(), requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_water_info_input_data) {
                    Tr_water_info_input_data data = (Tr_water_info_input_data) obj;
                    if ("Y".equals(data.reg_yn)) {

                        // 등록이 성공후에 목표 등록
                        if (!TargetAmount.toString().isEmpty()) {
                            CommonData login = CommonData.getInstance(baseFragment.getContext());
                            Tr_water_goalqy goalinputData = new Tr_water_goalqy();
                            Tr_water_goalqy.RequestData goalRequestData = new Tr_water_goalqy.RequestData();
                            goalRequestData.mber_sn = login.getMberSn();
                            goalRequestData.goal_water_ntkqy = TargetAmount;
                            goalRequestData.goal_water_goalqy = "";

                            baseFragment.getData(baseFragment.getContext(), goalinputData.getClass(), goalRequestData, true, new ApiData.IStep() {
                                @Override
                                public void next(Object obj) {
                                    if (obj instanceof Tr_water_goalqy) {
                                        Tr_water_goalqy data = (Tr_water_goalqy) obj;
                                        if ("Y".equals(data.reg_yn)) {
                                            registWaterDB(baseFragment, dataModel, true);

                                            if (iBluetoothResult != null)
                                                iBluetoothResult.onResult(true);
                                        } else {
                                        }
                                    }
                                }
                            }, new ApiData.IFailStep() {
                                @Override
                                public void fail() {
                                }
                            });
                        } else {
                            registWaterDB(baseFragment, dataModel, true);

                            if (iBluetoothResult != null)
                                iBluetoothResult.onResult(true);
                        }
                    } else {
                        CDialog.showDlg(baseFragment.getContext(), baseFragment.getContext().getString(R.string.text_regist_fail));
                    }
                }
            }
        }, new ApiData.IFailStep() {
            @Override
            public void fail() {
                registWaterDB(baseFragment, dataModel, false);
                if (iBluetoothResult != null)
                    iBluetoothResult.onResult(false);
            }
        });
    }

    private void registWaterDB(BaseFragment baseFragment, SparseArray<WaterModel> dataModel, boolean isServerRegist) {
        DBHelper helper = new DBHelper(baseFragment.getContext());
        DBHelperWater db = helper.getWaterDb();
        db.insert(dataModel, isServerRegist);
    }

    /**
     * 체중 데이터 입력
     *
     * @param baseFragment
     * @param weightModel
     * @param iBluetoothResult
     */
    public void uploadWeight(final BaseFragment baseFragment, final WeightModel weightModel, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        Tr_get_hedctdata.DataList data = new Tr_get_hedctdata.DataList();
        data.bmr = "" + weightModel.getBmr();
        data.bodywater = "" + weightModel.getBodyWater();
        data.bone = "" + weightModel.getBone();
        data.fat = "" + weightModel.getFat();
        data.heartrate = "" + weightModel.getHeartRate();
        data.muscle = "" + weightModel.getMuscle();
        data.obesity = "" + weightModel.getObesity();
        data.weight = "" + weightModel.getWeight();
        data.bdwgh_goal = "" + weightModel.getBdwgh_goal();

        data.idx = weightModel.getIdx();
        data.regtype = weightModel.getRegType();
        data.reg_de = weightModel.getRegDate();

        List<Tr_get_hedctdata.DataList> datas = new ArrayList<>();
        datas.add(data);
        new DeviceDataUtil().uploadWeight(baseFragment, datas, iBluetoothResult);
    }

    /**
     * 목표체중 데이터 입력
     *
     * @param baseFragment
     * @param weightModel
     * @param iBluetoothResult
     */
    public void uploadTargetWeight(final BaseFragment baseFragment, final WeightModel weightModel, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        Tr_bdwgh_goal_input inputData = new Tr_bdwgh_goal_input();
        CommonData login = CommonData.getInstance(baseFragment.getContext());

        Tr_bdwgh_goal_input.RequestData requestData = new Tr_bdwgh_goal_input.RequestData();
        requestData.mber_sn = login.getMberSn();
        requestData.mber_bdwgh_goal = Float.toString(weightModel.getBdwgh_goal());

        baseFragment.getData(baseFragment.getContext(), inputData.getClass(), requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_bdwgh_goal_input) {
                    Tr_bdwgh_goal_input data = (Tr_bdwgh_goal_input) obj;
                    if ("Y".equals(data.reg_yn)) {

                        if (iBluetoothResult != null)
                            iBluetoothResult.onResult(true);

                    } else {
                        CDialog.showDlg(baseFragment.getContext(), baseFragment.getContext().getString(R.string.text_regist_fail));
                    }
                }
            }
        }, new ApiData.IFailStep() {
            @Override
            public void fail() {

                if (iBluetoothResult != null)
                    iBluetoothResult.onResult(false);
            }
        });
    }

    /**
     * 심박수 데이터 등록
     *
     * @param baseFragment
     * @param dataModel
     * @param iBluetoothResult
     */
    public void uploadPPGData(final BaseFragment baseFragment, final List<BandModel> dataModel, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        Tr_ppg_info_input_data inputData = new Tr_ppg_info_input_data();
        CommonData login = CommonData.getInstance(baseFragment.getContext());

        Tr_ppg_info_input_data.RequestData requestData = new Tr_ppg_info_input_data.RequestData();
        requestData.mber_sn = login.getMberSn();
        requestData.ast_mass = inputData.getArray(dataModel, "D");

        baseFragment.getData(baseFragment.getContext(), inputData.getClass(), requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_ppg_info_input_data) {
                    Tr_ppg_info_input_data data = (Tr_ppg_info_input_data) obj;
                    if ("Y".equals(data.reg_yn)) {
                        registPPGDB(baseFragment, dataModel, true);

                        if (iBluetoothResult != null)
                            iBluetoothResult.onResult(true);
                    } else {
                        CDialog.showDlg(baseFragment.getContext(), baseFragment.getContext().getString(R.string.text_regist_fail));
                    }
                }
            }
        }, new ApiData.IFailStep() {
            @Override
            public void fail() {
                registPPGDB(baseFragment, dataModel, false);
                if (iBluetoothResult != null)
                    iBluetoothResult.onResult(false);
            }
        });
    }

    /**
     * 체중데이터 업로드 및 Sqlite 저장
     *
     * @param baseFragment
     * @param datas
     * @param iBluetoothResult
     */
    public void uploadWeight(final BaseFragment baseFragment, final List<Tr_get_hedctdata.DataList> datas, final BluetoothManager.IBluetoothResult iBluetoothResult) {

        Tr_bdwgh_info_input_data inputData = new Tr_bdwgh_info_input_data();
        CommonData login = CommonData.getInstance(baseFragment.getContext());

        Tr_bdwgh_info_input_data.RequestData requestData = new Tr_bdwgh_info_input_data.RequestData();
        requestData.mber_sn = login.getMberSn();
        requestData.ast_mass = inputData.getArray(datas);

        String tempidx = SharedPref.getInstance(baseFragment.getContext()).getPreferences(SharedPref.MOTHER_WEIGHT_TEMP);
        Log.i(TAG, "tempidx : " + tempidx);

        if (!tempidx.equals("")) {
            Log.i(TAG, "datas.get(0).idx : " + datas.get(0).idx);
            long temptime = StringUtil.getLongVal(datas.get(0).idx) - StringUtil.getLongVal(tempidx);
            Log.i(TAG, "temptime : " + temptime);
            if (temptime < 150) {
                Log.i(TAG, "1.5초 미만이므로 무시 : " + temptime);
                return;
            }
        }
        SharedPref.getInstance(baseFragment.getContext()).savePreferences(SharedPref.MOTHER_WEIGHT_TEMP, datas.get(0).idx);

        Log.i(TAG, "SharedPref.MOTHER_WEIGHT_TEMP : " + SharedPref.getInstance(baseFragment.getContext()).getPreferences(SharedPref.MOTHER_WEIGHT_TEMP));


        baseFragment.getData(baseFragment.getContext(), inputData.getClass(), requestData, true, new ApiData.IStep() {
            @Override
            public void next(Object obj) {
                if (obj instanceof Tr_bdwgh_info_input_data) {
                    Tr_bdwgh_info_input_data data = (Tr_bdwgh_info_input_data) obj;
                    if ("Y".equals(data.reg_yn)) {
                        if (TextUtils.isEmpty(data.kg_kind) == false)
                            login.setKg_Kind(data.kg_kind);
//                        login.setMberKg(data.mber_kg);

                        for (Tr_get_hedctdata.DataList listdata : datas) {
                            if (StringUtil.getFloatVal(listdata.weight) > 0.0f || listdata.regtype.equals("D")) {
                                Log.d(TAG, "listdata.weight=" + listdata.weight + ", reg_de=" + listdata.reg_de + ", fat=" + listdata.fat + ", login.kg_kind=" + login.getKg_Kind());
//                                insertWeightMessage(baseFragment, listdata.weight, listdata.reg_de, listdata.fat);
                                registWeightDB(baseFragment, datas, true);

                            }
                        }

                        if (iBluetoothResult != null)
                            iBluetoothResult.onResult(true);
                    } else {
                        CDialog.showDlg(baseFragment.getContext(), baseFragment.getContext().getString(R.string.text_regist_fail));
                    }
                }
            }
        }, new ApiData.IFailStep() {
            @Override
            public void fail() {
                for (Tr_get_hedctdata.DataList data : datas) {
                    if (StringUtil.getFloatVal(data.weight) > 0.0f || data.regtype.equals("D")) {
//                        insertWeightMessage(baseFragment, data.weight, data.reg_de, data.fat);
                        registWeightDB(baseFragment, datas, true);
                    }
                }

                if (iBluetoothResult != null)
                    iBluetoothResult.onResult(false);
            }
        });
    }

    private void registWeightDB(BaseFragment baseFragment, List<Tr_get_hedctdata.DataList> datas, boolean isServerRegist) {
        DBHelper helper = new DBHelper(baseFragment.getContext());
        DBHelperWeight db = helper.getWeightDb();
        db.insert(datas, isServerRegist);
    }

    /**
     * 건강메시지 체지방메시지 만들기
     */

    private String getRatingMsg(Context context, String fat) {
        String ratingMsg = "";
        int rating = getRating(context, fat);
        //TODO 체중페이지 체지방 관련 삭제로 인한 주석처리
        /*if (rating == 1) {
            ratingMsg = "체지방률 " + fat + "%로서 평균보다 상당히 적은 상태입니다.";
        } else if (rating == 2) {
            ratingMsg = "체지방률 " + fat + "%로서 평균보다 적은 상태입니다.";
        } else if (rating == 3) {
            ratingMsg = "체지방률 " + fat + "%로서 평균적인 상태입니다.";
        } else if (rating == 4) {
            ratingMsg = "체지방률 " + fat + "%로서 평균보다 많은 상태입니다.";
        } else if (rating == 5) {
            ratingMsg = "체지방률 " + fat + "%로서 평균보다 상당히 많은 상태입니다.";
        }*/
        return ratingMsg;
    }

    /**
     * 건강메시지 체지방 등급 만들기
     */

    private int getRating(Context context, String fat) {
        int rating = 0;                                                                                 // 체지방 등급
        CommonData login = CommonData.getInstance(context);                                           // 회원 정보
        int sex = StringUtil.getIntVal(login.getGender());                                         // 회원 성별
        String nowYear = CDateUtil.getFormattedString_yyyy(System.currentTimeMillis());                // 현재 년도
        int rBirth = StringUtil.getIntVal(login.getBirthDay().substring(0, 4));                      // 회원 생년
        int rAge = (StringUtil.getIntVal(nowYear) - rBirth);                                     // 회원 나이
        float bdfat = StringUtil.getFloatVal(fat);                                                  // 회원 체지방률

        // 남자
        if (sex == 1) {
            if (((rAge >= 19 && rAge <= 24) && (bdfat <= 8.0))                            // 1등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat <= 9.4))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat <= 10.6))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat <= 12.9))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat <= 12.8))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat <= 13.2))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat <= 14.3))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat <= 14.4))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat <= 16.1))) {
                rating = 1;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 8.1 && bdfat <= 11.7))      // 2등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 9.5 && bdfat <= 13.7))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 10.7 && bdfat <= 14.5))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 13.0 && bdfat <= 16.7))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 12.9 && bdfat <= 15.6))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 13.3 && bdfat <= 16.5))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 14.4 && bdfat <= 17.7))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 14.5 && bdfat <= 18.0))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 16.2 && bdfat <= 17.8))) {
                rating = 2;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 11.8 && bdfat <= 16.6))     // 3등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 13.8 && bdfat <= 18.3))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 14.6 && bdfat <= 18.8))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 16.8 && bdfat <= 21.1))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 15.7 && bdfat <= 20.0))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 16.6 && bdfat <= 20.3))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 17.8 && bdfat <= 21.8))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 18.1 && bdfat <= 21.5))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 17.9 && bdfat <= 22.5))) {
                rating = 3;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 16.7 && bdfat <= 22.8))     // 4등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 18.4 && bdfat <= 24.4))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 18.9 && bdfat <= 23.0))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 21.2 && bdfat <= 25.1))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 20.1 && bdfat <= 24.0))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 20.4 && bdfat <= 24.8))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 21.9 && bdfat <= 25.9))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 21.6 && bdfat <= 25.1))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 22.6 && bdfat <= 27.5))) {
                rating = 4;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 22.9))                    // 5등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 24.5))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 23.1))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 25.2))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 24.1))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 24.9))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 26.0))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 25.2))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 27.6))) {
                rating = 5;
            }
            return rating;
        }
        // 여자
        if (sex == 2) {
            if (((rAge >= 19 && rAge <= 24) && (bdfat <= 19.0))                           // 1등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat <= 18.6))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat <= 18.9))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat <= 19.2))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat <= 19.8))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat <= 19.4))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat <= 19.7))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat <= 20.6))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat <= 21.2))) {
                rating = 1;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 19.1 && bdfat <= 22.3))     // 2등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 18.7 && bdfat <= 21.3))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 19.0 && bdfat <= 22.1))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 19.3 && bdfat <= 23.0))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 19.9 && bdfat <= 23.1))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 19.5 && bdfat <= 22.9))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 19.8 && bdfat <= 23.9))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 20.7 && bdfat <= 24.3))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 21.3 && bdfat <= 24.8))) {
                rating = 2;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 22.4 && bdfat <= 25.3))     // 3등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 21.4 && bdfat <= 24.9))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 22.2 && bdfat <= 24.8))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 23.1 && bdfat <= 27.0))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 23.2 && bdfat <= 28.0))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 23.0 && bdfat <= 27.7))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 24.4 && bdfat <= 27.8))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 24.4 && bdfat <= 28.9))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 24.9 && bdfat <= 29.2))) {
                rating = 3;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 25.4 && bdfat <= 29.6))     // 4등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 25.0 && bdfat <= 29.6))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 24.9 && bdfat <= 28.6))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 27.1 && bdfat <= 32.8))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 28.1 && bdfat <= 33.1))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 27.8 && bdfat <= 31.4))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 27.9 && bdfat <= 34.6))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 29.0 && bdfat <= 36.0))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 29.3 && bdfat <= 34.7))) {
                rating = 4;
            } else if (((rAge >= 19 && rAge <= 24) && (bdfat >= 29.7))                    // 5등급군에 해당
                    || ((rAge >= 25 && rAge <= 29) && (bdfat >= 29.7))
                    || ((rAge >= 30 && rAge <= 34) && (bdfat >= 28.7))
                    || ((rAge >= 35 && rAge <= 39) && (bdfat >= 32.9))
                    || ((rAge >= 40 && rAge <= 44) && (bdfat >= 33.2))
                    || ((rAge >= 45 && rAge <= 49) && (bdfat >= 31.5))
                    || ((rAge >= 50 && rAge <= 54) && (bdfat >= 34.7))
                    || ((rAge >= 55 && rAge <= 59) && (bdfat >= 36.1))
                    || ((rAge >= 60 && rAge <= 64) && (bdfat >= 34.8))) {
                rating = 5;
            }
            return rating;
        }
        return rating;
    }

    // 체중 메시지
    private String getWeightMessage(Context context, String weight, String fat) {
        CommonData login = CommonData.getInstance(context);                                          // 회원 정보
        String rWeight = String.format("%.1f", StringUtil.getFloatVal(weight));  // 회원 체중
        float rHeight = StringUtil.getFloat(login.getBefCm()) * 0.01f;                               // 회원 키
        float fWeight = StringUtil.getFloat(weight);
        Logger.i(TAG, "getWeightMessage.weight=" + weight);
//        float bmi       = StringUtil.getFloatVal(String.format("%.1f", StringUtil.getFloatVal(weight) / (rHeight * rHeight))); // 회원 BMI
        float bmi = (fWeight / (rHeight * rHeight)); // 회원 BMI
        String lavelstr = "";
        if (bmi < 18.5) {
            lavelstr = "저체중";
        } else if (bmi >= 18.5 && bmi <= 22.9) {
            lavelstr = "정상체중";
        } else if (bmi > 22.9 && bmi < 25.0) {
            lavelstr = "과체중";
        } else if (bmi >= 25.0) {
            lavelstr = "비만";
        }

        String message = "측정된 체중 " + rWeight + "kg으로 계산된 BMI(체질량지수)는 " + bmi + "으로 " + lavelstr + "군에 해당합니다.";

        float bdfat = StringUtil.getFloatVal(fat);

        if (bdfat > 0) {
            if (message != "")
                message += "\n\n";
            message += getRatingMsg(context, fat);
        }

        // 저체중군
        if (bmi < 18.5) {
            // 추가메시지는 ||로 구분하여 넣는다.
            if (message != "")
                message += "\n\n";
            message += "적절한 운동과 균형 잡힌 음식섭취를 통해 정상체중을 회복 할 수 있도록 노력이 필요합니다. \n" +
                    "체중증가가 지방만이 아닌 제지방의 증가까지 병행하여 목표 활동량 달성 노력과 함께 근력운동을 추가하여 근육의 양과 크기를 증가시키는 것이 중요합니다. \n" +
                    "점진적으로 목표를 수정하여 활동량과 식사량을 늘려주세요.";
            return message;
        }
        // 정상체중군
        if (bmi >= 18.5 && bmi <= 22.9) {
            // 추가메시지는 ||로 구분하여 넣는다.
            if (message != "")
                message += "\n\n";
            message += "적절한 운동과 식사조절을 통해 건강한 체중을 유지하는 것이 중요합니다. \n" +
                    "점진적으로 목표를 수정하여 활동량을 늘려주세요.";
            return message;
        }
        // 비만군
        if (bmi >= 25.0) {
            // 추가메시지는 ||로 구분하여 넣는다.
            if (message != "")
                message += "\n\n";
            message += "적절한 체중 감량에는 시간이 걸립니다. 가능한 매일 활동 목표 달성을 위해 노력해야 합니다. \n" +
                    "추천되는 목표활동량은 최소한입니다. 점진적으로 목표를 수정하여 활동량을 늘려가야 합니다. \n" +
                    "체중 감량을 위해 평소보다 하루 500~1,000kcal정도의 에너지 섭취량을 줄이세요.";
            return message;
        }
        return message;
    }

    //  혈당 메시지
    private String getSugarMessage(Context context, String eatType, float sugar) {
        String message = "";

        String tString = CDateUtil.HH_MM(new Date(System.currentTimeMillis()));

        int isPregnancy = iPregnancyValue(context);

        if (isPregnancy == 1 || isPregnancy == 2 || isPregnancy == 3) {
            // 식전
            if (eatType.equals("0") || eatType.equals("1") || eatType.equals("3")) {
                if (sugar <= 60) {
                    //정상
                    message = tString + " 식전 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 낮은 편입니다.\n\n저혈당은 태아에게 해는 끼치지 않지만 임신부에게 불편감을 줄 수 있습니다.\n만약 저혈당 증상이 나타날 경우 주스나 사탕, 설탕물 등의 당질을 섭취하세요.\n*저혈당 증상 : 식은땀, 불안감, 손떨림, 창백한 얼굴, 의식혼미, 어지럼증, 시력변화, 말하기 힘듦, 두통 등";

                } else if (sugar >= 61 && sugar <= 90) {
                    //당뇨 전단계
                    message = tString + " 식전 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 관리가 잘 되고 있습니다.\n\n규칙적인 생활을 통해 꾸준히 관리하세요.";

                } else if (sugar >= 91) {
                    //당뇨병
                    message = tString + " 식전 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 관리가 필요합니다.\n\n높은 혈당은 산모와 태아의 건강에도 영향을 미칠 수 있으므로 더욱 세심한 관리가 필요합니다.\n만약 이러한 수치가 지속된다면 전문의와 상담이 필요합니다.";
                }
            }
            // 식후
            else {
                if (sugar <= 84) {
                    //정상
                    message = tString + " 식후 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 낮은 편입니다.\n\n저혈당은 태아에게 해는 끼치지 않지만 임신부에게 불편감을 줄 수 있습니다.\n만약 저혈당 증상이 나타날 경우 주스나 사탕, 설탕물 등의 당질을 섭취하세요.\n*저혈당 증상 : 식은땀, 불안감, 손떨림, 창백한 얼굴, 의식혼미, 어지럼증, 시력변화, 말하기 힘듦, 두통 등";

                } else if (sugar >= 85 && sugar <= 120) {
                    //당뇨 전단계
                    message = tString + " 식후 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 관리가 잘 되고 있습니다.\n\n규칙적인 생활을 통해 꾸준히 관리하세요.\n";
                } else if (sugar >= 121) {
                    //당뇨병
                    message = tString + " 식후 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 관리가 필요합니다.\n\n높은 혈당은 산모와 태아의 건강에도 영향을 미칠 수 있으므로 더욱 세심한 관리가 필요합니다.\n만약 이러한 수치가 지속된다면 전문의와 상담이 필요합니다.";

                }
            }
        } else {
            // 식전
            if (eatType.equals("0") || eatType.equals("1") || eatType.equals("3")) {
                if (sugar <= 70) {
                    //정상
                    message = tString + " 식전 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 낮은 편입니다.\n\n저혈당 증상이 나타날 경우 주스나 사탕, 설탕물 등의 당질을 섭취하세요.\n*저혈당 증상 : 식은땀, 불안감, 손떨림, 창백한 얼굴, 의식혼미, 어지럼증, 시력변화, 말하기 힘듦, 두통 등";

                } else if (sugar >= 71 && sugar <= 99) {
                    //당뇨 전단계
                    message = tString + " 식전 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 관리가 잘 되고 있습니다.\n\n규칙적인 생활을 통해 꾸준히 관리하세요.";

                } else if (sugar >= 100) {
                    //당뇨병
                    message = tString + " 식전 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 관리가 필요합니다.\n\n혈당 조절을 잘하면 당뇨병 예방은 물론 당뇨 합병증 진행을 지연시킬 수 있으므로 전문의와 정기적인 상담과 혈당 체크, 생활습관 개선이 필요합니다.";
                }
            }
            // 식후
            else {
                if (sugar <= 89) {
                    //정상
                    message = tString + " 식후 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 낮은 편입니다.\n\n저혈당 증상이 나타날 경우 주스나 사탕, 설탕물 등의 당질을 섭취하세요.\n*저혈당 증상 : 식은땀, 불안감, 손떨림, 창백한 얼굴, 의식혼미, 어지럼증, 시력변화, 말하기 힘듦, 두통 등";

                } else if (sugar >= 90 && sugar <= 139) {
                    //당뇨 전단계
                    message = tString + " 식후 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 관리가 잘 되고 있습니다.\n\n규칙적인 생활을 통해 꾸준히 관리하세요.\n";
                } else if (sugar >= 140) {
                    //당뇨병
                    message = tString + " 식후 혈당은 " + String.format("%.0f", sugar) + "mg/dL로 관리가 필요합니다.\n\n혈당 조절을 잘하면 당뇨병 예방은 물론 당뇨 합병증 진행을 지연시킬 수 있으므로 전문의와 정기적인 상담과 혈당 체크, 생활습관 개선이 필요합니다.";

                }
            }
        }
        Logger.i(TAG, "getSugarMessage=" + message);


        return message;
    }


    private String getPresureGroup(BaseFragment baseFragment, float systolic, float diastolic) {
        String presureGroup = "";

        if (systolic < 90 && diastolic < 60) {
            presureGroup = "낮은 편";
        }

        if ((systolic >= 90 && systolic < 120) || (diastolic >= 60 && diastolic < 80)) {
            presureGroup = "정상혈압";
        }
        if ((systolic >= 120 && systolic <= 139) || (diastolic >= 80 && diastolic <= 89)) {
            presureGroup = "고혈압 전단계";
        }
        if ((systolic >= 140 && systolic <= 159) || (diastolic >= 90 && diastolic <= 99)) {
            presureGroup = "고혈압 1기";
        }
        if ((systolic >= 160) || (diastolic >= 100)) {
            presureGroup = "고혈압 2기";
        }
        return presureGroup;
    }

    /**
     * 혈압 메시지
     *
     * @param baseFragment
     * @param systolic
     * @param diastolic
     * @return
     */
    private String getPressureMessage(BaseFragment baseFragment, int systolic, int diastolic) {

        String presureGroup = getPresureGroup(baseFragment, systolic, diastolic);
        String currString = StringUtil.getFormattedDateTime();
        String message = "측정된 혈압 " + (int) systolic + "/" + (int) diastolic + "mmHg은 " + presureGroup + "에 해당합니다.";

        CommonData login = CommonData.getInstance(baseFragment.getContext());                             // 로그인 정보
        String nowYear = CDateUtil.getFormattedString_yyyy(System.currentTimeMillis());   // 현재년도
        int rAge = Integer.parseInt(login.getBirthDay().substring(0, 4));                    // 회원 생년


        String tString = CDateUtil.HH_MM(new Date(System.currentTimeMillis()));

        int isPregnancy = iPregnancyValue(baseFragment.getContext());

        if (isPregnancy == 1 || isPregnancy == 2 || isPregnancy == 3) {

            //임신중
            if (diastolic < 90 && systolic < 90) {
                message = tString + " 혈압은 " + systolic + "/" + diastolic + "mmHg로 낮은 편입니다.\n\n임신 중에는 일시적으로 혈압이 떨어질 수 있습니다. 임신 중 갑자기 일어서거나 오랫동안 서 있으면 어지럼증을 일으킬 수 있으므로 과격한 활동은 피하고 적절한 휴식을 취하는 것이 중요합니다.\n";

            } else if (diastolic < 120 && systolic < 120) {

                message = tString + " 혈압은 " + systolic + "/" + diastolic + "mmHg로 관리가 잘 되고 있습니다.\n\n규칙적인 생활을 통해 꾸준히 관리하세요.\n";

                //        }else if(bigV >= 120 || smallV >= 80){
            } else {
                message = tString + " 혈압은 " + systolic + "/" + diastolic + "mmHg로 관리가 필요합니다.\n\n임신 후 일시적으로 혈압이 올라갈 수 있지만, 높은 혈압은 태아와 임신부의 건강에 영향을 줄 수 있으므로 적극적인 관리가 필요합니다.\n3~5분 정도의 안정을 취한 후 재측정을 하였을 때도 이러한 수치가 지속된다면 전문의와 상담이 필요합니다.\n";
            }
        } else {

            //출산후
            if (diastolic < 90 && systolic < 90) {
                message = tString + " 혈압은 " + systolic + "/" + diastolic + "mmHg로 낮은 편입니다.\n\n낮은 혈압에 속하더라도 별다른 증상을 보이지 않을 수 있습니다.\n그러나 정도가 심한 경우에는 실신을 일으킬 수 있으므로 규칙적인 생활과 충분한 휴식을 실천하는 것이 좋습니다.\n";

            } else if (diastolic < 120 && systolic < 120) {
                message = tString + " 혈압은 " + systolic + "/" + diastolic + "mmHg로 관리가 잘 되고 있습니다.\n\n규칙적인 생활을 통해 꾸준히 관리하세요.\n";

                //        }else if(bigV >= 120 || smallV >= 80){
            } else {
                message = tString + " 혈압은 " + systolic + "/" + diastolic + "mmHg로 관리가 필요합니다.\n\n혈압은 변동이 많고 여러 원인에 의해 상승할 수 있지만 높은 혈압은 건강에 영향을 줄 수 있으므로 적극적인 관리가 필요합니다.\n3~5분 정도의 안정을 취한 후 재측정을 하였을 때도 이러한 수치가 지속된다면 전문의와 상담이 필요합니다.\n";
            }
        }

        Logger.i(TAG, "getPressureMessage=" + message);
        return message;
    }

    /**
     * 식사 메시지(알고리즘 메시지 )
     *
     * @param eatCal
     * @return
     */
    public String getCalroMessage(Context context, float eatCal) {
        String msg = "";

        //○ 에너지 섭취 비율(%) = 실제섭취량(kcal)/권장섭취량(kcal)*100
        float per = (eatCal / getPregnancyRecommendCal(context)) * 100;

        int iPregnancy = iPregnancyValue(context);

        // 출산예정일 -300일이 오늘보다 작아야 하고, 출산예정일이 오늘보다 크거나 같아야 한다.
        if (iPregnancy == 1) {
            //  ② 임신 초기
            if (per < 90) {

                msg = "영양 불균형이 생기지 않도록 양질의 음식으로 열량을 보충해주세요. \n단백질, 칼슘, 엽산, 철분이 부족하지 않도록 특히 신경써주세요.";
            } else if (per >= 90 && per <= 110) {

                msg = "현재 섭취 수준을 유지하세요. \n양질의 단백질과 엽산, 칼슘, 철분, 비타민, 섬유질이 함유된 각종 채소와 과일을 골고루 섭취하세요.";
            } else if (per > 110) {

                msg = "열량 과잉 섭취에 주의하세요. 임신 초기에는 추가 열량이 필요하지 않습니다. 단백질과 칼슘 섭취에 신경쓰시고, 탄수화물이나 지방을 필요 이상으로 먹지 않도록 주의하세요.";
            }
        } else if (iPregnancy == 2) {
            //  ③ 임신 중기
            if (per < 90) {

                msg = "양질의 음식으로 열량을 보충해주세요.\n영양 불균형은 태아의 성장, 산모의 건강에 큰 영향을 줍니다.";
            } else if (per >= 90 && per <= 110) {

                msg = "현재 섭취 수준을 유지하세요. \n하루 30mg의 철분을 섭취하고, 칼슘, 섬유질을 충분히 섭취하세요.";
            } else if (per > 110) {

                msg = "열량 과잉 섭취에 주의하세요. \n소화기능이 약해지므로 과식을 피하고, 철분(하루 30mg), 칼슘, 섬유질은 충분히 섭취하세요.";
            }
        } else if (iPregnancy == 3) {
            //  임신 후기
            if (per < 90) {

                msg = "양질의 음식으로 열량을 보충해주세요.\n임신후기에는 조금씩 나누어 자주 먹는 것이 좋습니다. 단백질과 비타민이 부족하지 않도록 주의하세요.";
            } else if (per >= 90 && per <= 110) {

                msg = "현재 섭취 수준을 유지하세요. 조금씩 나누어 먹는 것이 소화에 도움이 되고, 임신후기에 접어들면 자주 붓게 되므로 짜지 않게 먹는 것이 좋습니다.";
            } else if (per > 110) {

                msg = "열량 과잉 섭취에 주의하세요.\n체중이 지나치게 늘면 순산을 방해합니다.";
            }
        } else if (iPregnancy == 55) {
            //  수유
            if (per < 90) {

                msg = "양질의 음식으로 열량을 보충해주세요. \n단백질, 철분, 비타민, 무기질이 풍부한 음식을 충분히 섭취하세요.";
            } else if (per >= 90 && per <= 110) {

                msg = "현재 섭취 수준을 유지하세요. \n수유부에게 가장 좋은 음식인 다양한 음식을 골고루 균형있게 먹는 것입니다.";
            } else if (per > 110) {

                msg = "건강한 체중관리를 위해 열량 과잉섭취에 주의하세요.";
            }
        } else if (iPregnancy == 99) {
            //  비임신
            if (per < 90) {

                msg = "양질의 음식으로 열량을 보충해주세요. \n영양 불균형은 건강에 해롭습니다.";
            } else if (per >= 90 && per <= 110) {

                msg = "현재 섭취 수준을 유지하세요. \n양질의 음식을 골고루 섭취하세요.";
            } else if (per > 110) {

                msg = "열량 과잉 섭취에 주의하세요. \n열량 과잉 섭취는 체중 증가의 원인이 됩니다. ";
            }
        }

        return msg;
    }

    /**
     * 활동 메시지
     *
     * @return
     */
    public String getStepMessage(Context context) {

        String msg = "";
        Date nowDate = new Date();

        // 출산일이 없으면, 출산예정일
        String exeDateStr = CommonData.getInstance(context).getMbeChlBirthDe().length() >= 8 ?
                CommonData.getInstance(context).getMbeChlBirthDe() : CommonData.getInstance(context).getMberBirthDueDe(); //출산예정일, 출산일

        int active = Integer.parseInt(CommonData.getInstance(context).getActqy());

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        Date exeDate = new Date();
        try {
            exeDate = transFormat.parse(exeDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date dateSixAft = addDay(exeDate, 42); // 6주후
        Date datethreeMonthAft = addDay(exeDate, 90); // 3개월 후


        Log.i(TAG, "출산(출산예정일)후 6주 이후 날짜:" + dateSixAft);

        int isPregnancy = iPregnancyValue(context);

        if (isPregnancy == 1) {
            //초기
            msg = "산책 및 가벼운 스트레칭과 근력운동이 추천됩니다.";
        } else if (isPregnancy == 2) {
            //중기
            msg = "걷기 운동 중 대화가 가능한 수준으로 하세요.\n최소한 15분 이상으로 시작해서 점차 시간을 늘려가는 것이 좋습니다.";
        } else if (isPregnancy == 3) {
            //후기
            msg = "가벼운 걷기, 임신부 체조와 같은 안전하게 계획된 운동이 추천됩니다.";
        } else {

            //출산후 6주동안
            // 오늘이 출산일 이후이고 && 오늘이 출산후 6주후 보다 이전
            if (nowDate.after(exeDate) && nowDate.before(dateSixAft)) {
                msg = "출산 후 6주 동안(산후 건강진단까지)은 안전하게 계획된 운동이 필요합니다.\n걷기 운동은 산후 우울증의 발병 위험도 낮출 수 있으며, 감정적인 스트레스 요인을 감소시킬 수 있습니다.";
            } else if (nowDate.after(dateSixAft) && nowDate.before(datethreeMonthAft)) {
                if (active == 1) {
                    msg = "목표 달성을 위해 노력해주세요.\n완전히 활동적인 운동은 산후 3개월 이후에 시작하는 것이 좋습니다. ";
                } else if (active == 2) {
                    msg = "목표 달성을 위해 노력해주세요.\n완전히 활동적인 운동은 산후 3개월 이후에 시작하는 것이 좋습니다. ";
                } else if (active == 3) {
                    msg = "목표 달성을 위해 노력해주세요.\n완전히 활동적인 운동은 산후 3개월 이후에 시작하는 것이 좋습니다. ";
                }
            } else {
                if (active == 1) {
                    msg = "목표 달성을 위해 노력해주세요.";
                } else if (active == 2) {
                    msg = "목표 달성을 위해 노력해주세요.";
                } else if (active == 3) {
                    msg = "목표 달성을 위해 노력해주세요.";
                }
            }
        }
        return msg;
    }

    /**
     * 활동 목표 칼로리
     *
     * @return
     */
    public String getStepGoalCal(Context context) {

        String cal = "";
        Date nowDate = new Date();

        // 출산일이 없으면, 출산예정일
        String exeDateStr = CommonData.getInstance(context).getMbeChlBirthDe().length() >= 8 ?
                CommonData.getInstance(context).getMbeChlBirthDe() : CommonData.getInstance(context).getMberBirthDueDe(); //출산예정일, 출산일

        int active = Integer.parseInt(CommonData.getInstance(context).getActqy());

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        Date exeDate = new Date();
        try {
            exeDate = transFormat.parse(exeDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date dateSixAft = addDay(exeDate, 42); // 6주후
        Date datethreeMonthAft = addDay(exeDate, 90); // 3개월 후

        Log.i(TAG, "출산(출산예정일)후 6주 이후 날짜:" + dateSixAft);

        int isPregnancy = iPregnancyValue(context);

        if (isPregnancy == 1) {
            //초기
            cal = "150";
        } else if (isPregnancy == 2) {
            //중기
            cal = "250";
        } else if (isPregnancy == 3) {
            //후기
            cal = "200";
        } else {

            //출산후 6주동안
            if (nowDate.after(exeDate) && nowDate.before(dateSixAft)) {

                cal = "150";
            } else {
                //출산후 6주동안
                if (nowDate.after(dateSixAft) && nowDate.before(datethreeMonthAft)) {

                    if (active == 1) {
                        cal = "375";
                    } else if (active == 2) {
                        cal = "400";
                    } else if (active == 3) {
                        cal = "500";
                    }
                } else {
                    if (active == 1) {
                        cal = "375";
                    } else if (active == 2) {
                        cal = "400";
                    } else if (active == 3) {
                        cal = "500";
                    }
                }
            }
        }
        return cal;
    }

    public int birthMonth() {

//        CommonData.getInstance(baseFragment.getContext())
//        만나이(개월) = ((측정년도-출생년도)*12)+(측정월-출생월)+((측정일-출생일)/30.4)

        return 0;
    }

    /**
     * 식사 권장칼로리.
     *
     * @return
     */
    public int getPregnancyRecommendCal(Context context) {
        /*
         ① 비임신, 비수유=356-6.91*연령(세)+PA(활동계수)*[9.36*임신 전 체중(kg)+726*신장(m)]
         ② 임신 초기=356-6.91*연령(세)+PA(활동계수)*[9.36*임신 전 체중(kg)+726*신장(m)]
         ③ 임신 중기=[ 356-6.91*연령(세)+PA(활동계수)*(9.36*임신 전 체중(kg)+726*신장(m))]+340
         ④ 임신 후기=[ 356-6.91*연령(세)+PA(활동계수)*(9.36*임신 전 체중(kg)+726*신장(m))]+450
         ⑤ 수유기=[ 356-6.91*연령(세)+PA(활동계수)*(9.36*임신 전 체중(kg)+726*신장(m))]+340
         */

        int recommandCal = 0;    //● 비임신•임신 분기에 따른 권장섭취 칼로리

        String nowDate = CDateUtil.getToday_yyyyMMdd();
        String mber_brthdy = CommonData.getInstance(context).getBirthDay();   // 엄마 생일
        String mber_active = CommonData.getInstance(context).getActqy();      // 가벼운운동, 보통활동, 힘들활동
        String befCm = CommonData.getInstance(context).getBefCm();       // 출산전 신장(키)
        String mber_beWeight = CommonData.getInstance(context).getBefKg();       // 출산전 체중
        String motherWeight = CommonData.getInstance(context).getMotherWeight();
        float fMotherWeight = Float.parseFloat(mber_beWeight);
        float fBefCm = Float.parseFloat(befCm);

        // + ---------------------------------
        // 만나이
        // + ---------------------------------
        if (CommonData.getInstance(context).getBirthDay().length() != 8) {
            Logger.i(TAG, "getPressureMessage= SERVER value Error 엄마 생년월일 없음 - 나이계산할 수 없음");
        }

        String tempBirth = "19" + mber_brthdy;
        int fAge = (Integer.parseInt(nowDate.substring(0, 4)) - Integer.parseInt(tempBirth.substring(0, 4))) - 1;  //만나이

        // + ---------------------------------
        // 활동정도
        // + ---------------------------------
        float fActivie = 0.0f;
        if ("1".equals(mber_active)) {
            fActivie = 1.12f;    //가벼운운동
        } else if ("2".equals(mber_active)) {
            fActivie = 1.27f;    //보통활동
        } else if ("3".equals(mber_active)) {
            fActivie = 1.45f;    //힘들활동
        }

        int iPregnancy = iPregnancyValue(context);

        // 출산예정일 -300일이 오늘보다 작아야 하고, 출산예정일이 오늘보다 크거나 같아야 한다.
        if (iPregnancy == 1) {
            //  ② 임신 초기=356-6.91*연령(세)+PA(활동계수)*[9.36*임신 전 체중(kg)+726*신장(m)]
            recommandCal = (int) (356 - 6.91 * fAge + fActivie * (9.36 * fMotherWeight + 726 * (fBefCm / 100)));

        } else if (iPregnancy == 2) {
            //  ③ 임신 중기=[ 356-6.91*연령(세)+PA(활동계수)*(9.36*임신 전 체중(kg)+726*신장(m))]+340
            recommandCal = (int) (356 - 6.91 * fAge + fActivie * (9.36 * fMotherWeight + 726 * (fBefCm / 100))) + 340;

        } else if (iPregnancy == 3) {
            //  ④ 임신 후기=[ 356-6.91*연령(세)+PA(활동계수)*(9.36*임신 전 체중(kg)+726*신장(m))]+450
            recommandCal = (int) (356 - 6.91 * fAge + fActivie * (9.36 * fMotherWeight + 726 * (fBefCm / 100))) + 450;


        } else if (iPregnancy == 55) {
            //(출산일/예정일)후 5개월 이전
            // ⑤ 수유기=[ 356-6.91*연령(세)+PA(활동계수)*(9.36*임신 전 체중(kg)+726*신장(m))]+340
            recommandCal = (int) (356 - 6.91 * fAge + fActivie * (9.36 * fMotherWeight + 726 * (fBefCm / 100))) + 340;

        } else if (iPregnancy == 99) {
            // 비임신기
            // ① 비임신, 비수유=356-6.91*연령(세)+PA(활동계수)*[9.36*임신 전 체중(kg)+726*신장(m)]
            recommandCal = (int) (356 - 6.91 * fAge + fActivie * (9.36 * fMotherWeight + 726 * (fBefCm / 100)));

        }

        return recommandCal;
    }

    public static Date addDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }

    public int iPregnancyValue(Context context) {
        int value = -99;
        // 오늘날짜

        long now = System.currentTimeMillis();
        Date nDate = new Date(now);
        String nowDate = CDateUtil.getToday_yyyyMMdd();

        String mber_chl_birth_de = CommonData.getInstance(context).getMbeChlBirthDe();
        String mber_birth_due_de = CommonData.getInstance(context).getMberBirthDueDe();

        // 출산일이 없으면, 출산예정일
        String exeDateStr = mber_chl_birth_de.length() >= 8 ? mber_chl_birth_de : mber_birth_due_de; //출산예정일, 출산일

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date exeDday = nDate;
        try {
            exeDday = format.parse(exeDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "출산예정일(출산일) exeDateStr :" + exeDateStr);

        Date exeBegin = addDay(exeDday, -40 * 7);
        Date exeMiddleOne = addDay(exeDday, -26 * 7);
        Date exeMiddleTwo = addDay(exeDday, -14 * 7);
        Date shrimpDay = addDay(exeDday, 30 * 5);

        // 출산예정일 -300일이 오늘보다 작아야 하고, 출산예정일이 오늘보다 크거나 같아야 한다.
        // 임신중
        if (nDate.after(exeBegin) && nDate.before(exeMiddleOne)) {
            //  ② 임신 초기
            value = 1;
        } else if (nDate.after(exeMiddleOne) && nDate.before(exeMiddleTwo)) {
            //  ③ 임신 중기
            value = 2;
        } else if (nDate.after(exeMiddleTwo) && nDate.before(exeDday)) {
            //  ② 임신 후기
            value = 3;
        } else if (nDate.after(exeDday) && nDate.before(shrimpDay)) {
            // ⑤ 수유기
            value = 55;
        } else {
            // ① 비임신, 비수유
            value = 99;
        }

        return value;
    }

    private long getDateDiff(String bDate) {

        int year = StringUtil.getIntVal(bDate.substring(0, 4));
        int month = StringUtil.getIntVal(bDate.substring(4, 6));
        int day = StringUtil.getIntVal(bDate.substring(6, 8));
        Calendar thatDay = Calendar.getInstance();
        thatDay.set(Calendar.DAY_OF_MONTH, day);
        thatDay.set(Calendar.MONTH, month - 1);
        thatDay.set(Calendar.YEAR, year);

        Calendar today = Calendar.getInstance();
        long diff = today.getTimeInMillis() - thatDay.getTimeInMillis(); //result in millis
        long days = diff / (24 * 60 * 60 * 1000);
        return days;
    }

//    public static String getMomBmiText() {
//        CommonData commonData = CommonData.getInstance(baseFragment.getContext());
//        float weight = StringUtil.getFloat(commonData.getMotherWeight());
//        float height = StringUtil.getFloat(commonData.getBefCm()) * 0.01f;
//        float bmi = getBmi(weight, height);
//        String lavelstr = "";
//        if ((bmi <= 0) == false) {
//            if (bmi <= 18.5) {
//                lavelstr = "저체중군";
//            }
//            if (bmi > 18.5 && bmi < 25) {
//                lavelstr = "정상체중군";
//            }
//            if (bmi >= 25 && bmi < 30) {
//                lavelstr = "과체중군";
//            }
//            if (bmi >= 30 && bmi < 40) {
//                lavelstr = "비만군";
//            }
//            if (bmi >= 40) {
//                lavelstr = "고도비만군";
//            }
//        }
//        return lavelstr;
//    }

    public static String getBmiText(float weight, float height) {
        height = height * 0.01f;
        float bmi = getBmi(weight, height);
        String lavelstr = "";
        if ((bmi <= 0) == false) {
            if (bmi <= 18.5) {
                lavelstr = "저체중군";
            }
            if (bmi > 18.5 && bmi < 25) {
                lavelstr = "정상체중군";
            }
            if (bmi >= 25 && bmi < 30) {
                lavelstr = "과체중군";
            }
            if (bmi >= 30 && bmi < 40) {
                lavelstr = "비만군";
            }
            if (bmi >= 40) {
                lavelstr = "고도비만군";
            }
        }
        return lavelstr;
    }

    public static float getBmi(float weight, float height) {
        height = height * 0.01f;
        float bmi = (weight / (height * height)); // 회원 BMI
        return bmi;
    }
}