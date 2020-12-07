import {
  login, userMe,
  saveRegion, getRegions, deleteRegion,
  saveDistrict, getDistricts, deleteDistrict,
  saveAware, getAwares, deleteAware,
  saveCategory, getCategories, deleteCategory,
  editPassword
} from "@/pages/service";
import {OPEN_PAGES, OPEN_PAGES2, TOKEN_NAME} from "../constants/constants";
import router from "umi/router";
import {toast} from "react-toastify";

export default ({
  namespace: 'globalModel',
  state: {
    categories: [],
    regions: [],
    regionId: '',
    districts: [],
    awares: [],
    showModal: false,
    showDeleteModal: false,
    currentItem: '',
    currentUser: '',
    photoUrl: '',
    isAdmin: false,
    isModer: false,
    isDirector: false,
    isOperator: false,
    isReception: false,
    message:0,



  },
  subscriptions: {
    setupHistory({dispatch, history}) {
      history.listen((location) => {
        if (!OPEN_PAGES.includes(location.pathname)) {
          dispatch({
            type: 'userMe',
            payload: {
              pathname: location.pathname
            }
          })
        }
      })
    }
  },
  effects: {
    * userMe({payload}, {call, put}) {
      console.log(888);
      console.log(payload);
      const res = yield call(userMe);
      console.log(res);
      console.log(payload);
      if (!res.success) {
        console.log(payload.pathname);
        console.log(payload.pathname.split('/'));
        console.log(OPEN_PAGES2.includes(`/${payload.pathname.split('/')[1]}`));
        if (!OPEN_PAGES2.includes(payload.pathname)
          && !OPEN_PAGES2.includes('/' + payload.pathname.split('/')[1])) {
          localStorage.removeItem(TOKEN_NAME);
          router.push('/auth/login')
        }
      } else {
        yield put({
          type: 'updateState',
          payload: {
            currentUser: res,
            isAdmin: res.roles.filter(item => item.roleName === 'ADMIN').length > 0 ? "ADMIN" : '',
            isModer: !!res.roles.filter(item => item.roleName === 'MODERATOR').length ? "MODERATOR" : '',
            isDirector: !!res.roles.filter(item => item.roleName === 'DIRECTOR').length ? "DIRECTOR" : '',
            isOperator: !!res.roles.filter(item => item.roleName === 'OPERATOR').length ? "OPERATOR" : '',
            isReception: !!res.roles.filter(item => item.roleName === 'RECEPTION').length ? "RECEPTION" : '',
          }
        })
      }
    },
    * login({payload}, {call, put}) {
      const res = yield call(login, payload);
      console.log(res);
      if (res.success) {
        localStorage.setItem(TOKEN_NAME, res.tokenType + res.token);
        router.push('/cabinet');
      }
    },

    * editPassword({payload},{call,put}){
      const res=yield call(editPassword,payload);
      if (res.success) {
        yield put({
          type: 'updateState'
        });
        toast("Tahrirlandi")
        router.push("/cabinet")
      } else {
        toast.error("xatolik")
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
