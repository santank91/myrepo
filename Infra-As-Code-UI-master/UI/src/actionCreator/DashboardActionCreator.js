import Dispatcher from "../dispatcher";
import ActionType from "../constants/actionType";
import WebApi from "../webapiutils/WebApi";

import config from "../../config.json";
import url from "../../url.json";

class DashboardActionCreator {
  loadOptionsData = () => {
    WebApi.apiGet(config.API_URL + url.LOOKUP_OPTIONS_DATA)
      .then((response) => {
        console.log(response);
        const action = {
          actionType: ActionType.GET_LOOKUP_OPTIONS_DATA,
          value: response.data,
        };
        Dispatcher.dispatch(action);
      })
      .catch((reject) => {
        const action = {
          actionType: ActionType.GET_LOOKUP_OPTIONS_DATA_FAILED,
        };
        Dispatcher.dispatch(action);
      });
  };
}
export default new DashboardActionCreator();
