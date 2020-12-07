export default ({
  namespace: 'adminModel',
  state: {

  },
  subscriptions: {},
  effects: {

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
