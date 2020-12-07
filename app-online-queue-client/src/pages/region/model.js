import {getRegions, saveRegion,deleteRegion} from "@/pages/service";
import {toast} from "react-toastify";
export default ({
  namespace: 'regionModel',
  state: {
    regions: [],
    showModal: false,
    showDeleteModal: false,
    currentItem: ''
  },
  subscriptions: {},
  effects: {

    * deleteRegion({payload}, {call, put, select}) {
      const res = yield call(deleteRegion, payload);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            showDeleteModal: false
          }
        });
        yield put({
          type: 'getRegions',
        });
        toast.success("O'chirildi");
      } else {
        toast.error(res.message);
      }
    },
    * getRegions( {}, {call, put}) {
      const res = yield call(getRegions);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            regions: res._embedded.list
          }
        })
      }
    },
    * addReg( {payload}, {call, put}) {
      const res = yield call(saveRegion, payload);
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            regions: res._embedded.list
          }
        })
      }
    }
    // * addRegion({payload}, {call, put, select}) {
    //   console.log(54);
    //   const res = yield call(saveRegion, payload);
    //   if (res.success) {
    //     yield put({
    //       type: 'updateState',
    //       payload: {
    //         showModal: false
    //       }
    //     });
    //     yield put({
    //       type: 'getRegions'
    //     });
    //     toast.success(payload.id?"Tahrirlandi":"Qo'shildi");
    //   } else {
    //     toast.error(res.message);
    //   }
    // }
  },
  reducers: {
    updateState(state, {payload}) {
      return {
        ...state,
        ...payload
      }
    }
  }
})
