import {getRegions, saveDistrict,deleteDistrict,getDistricts} from "@/pages/service";
import {toast} from "react-toastify";


export default ({
  namespace: 'districtModel',
  state: {
    districts: [],
    regions: [],
    showModal: false,
    showDeleteModal: false,
    currentDistrict: ''
  },
  subscriptions: {},
  effects: {
    * addDistrict({payload}, {call, put, select}) {
      const res = yield call(saveDistrict, payload);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            showModal: false
          }
        });
        yield put({
          type: 'getDistricts'
        });
        toast.success(payload.id?"Tahrirlandi":"Qo'shildi");
      } else {
        toast.error(res.message);
      }
    },
    * deleteDistrict({payload}, {call, put, select}) {
      const res = yield call(deleteDistrict, payload);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            showDeleteModal: false
          }
        });
        yield put({
          type: 'getDistricts',
        });
        toast.success("O'chirildi");
      } else {
        toast.error(res.message);
      }
    },
    * getDistricts({}, {call, put}) {
      const res = yield call(getDistricts);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            districts: res._embedded.list
          }
        })
      }
    },
    * getRegions({}, {call, put}) {
      const res = yield call(getRegions);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            regions: res._embedded.list
          }
        })
      }
    }
  },
  reducers: {
    updateState(state, {payload}) {
      return {
        ...state,
        ...payload
      }
    }
  }
});
