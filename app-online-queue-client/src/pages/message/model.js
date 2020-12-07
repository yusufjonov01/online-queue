import {toast} from "react-toastify";
import {getMessages, viewChange} from "../service";

export default ({
  namespace: 'messageModel',
  state: {
    messages: [],
    isOpen: false,
    messageId: ''
  },
  subscriptions: {},
  effects: {
    * getMessages({}, {call, put}) {
      let res = yield call(getMessages);
      yield put({
        type: 'updateState',
        payload: {
          messages: res.object
        }
      });
    },

    * isViewChange({payload}, {call, put}) {
      let res = yield call(viewChange, payload);
      console.log(res);
      if (res.success === true) {
        toast.success("Tasdiqlandi");
        let a = yield put({
          type: 'getMessages',
          payload: {
            id: res.object
          }
        });
      } else {
        toast.error("Xatolik");
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
