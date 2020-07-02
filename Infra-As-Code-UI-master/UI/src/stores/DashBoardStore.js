import { EventEmitter } from "events";
import Dispatcher from "../dispatcher";
import ActionType from "../constants/actionType";
import EventType from "../constants/eventType";

class DashBoardStore extends EventEmitter {
  constructor() {
    super();
    Dispatcher.register(this.registerToActions);
    this.lookupOptionData = {};
  }
  registerToActions = (action) => {
    switch (action.actionType) {
      case ActionType.GET_LOOKUP_OPTIONS_DATA:
        this.lookupOptionData = action.value;
        this.emit(
          EventType.GET_LOOKUP_OPTIONS_DATA_SUCCESS,
          this.lookupOptionData
        );
        break;
      case ActionType.GET_LOOKUP_OPTIONS_DATA_FAILED:
        this.emit(EventType.CREATE_CLUSTER_FAILED);
        break;
      default:
        break;
    }
  };

  getOptions = () => {
    const options = this.lookupOptionData ? this.lookupOptionData.options : {};
    const data = Object.keys(options).map((_key) => {
      const item = options[_key];
      return {
        [_key]: Object.keys(item).map((key) => {
          return {
            description: item[key],
            value: key,
          };
        }),
      };
    });
    return data;
  };

  getDropdownData = (header, name, value) => {
    let options = [];
    const optionsData = this.getOptions();
    if (optionsData.length === 0) {
      return;
    }
    switch (header) {
      case "Provider":
        const _filter = optionsData.find((option) => {
          return Object.keys(option)[0] === "provider";
        });
        options = _filter["provider"];
        break;
      case "Master Instance Type":
        const _filter1 = optionsData.find((option) => {
          return Object.keys(option)[0] === "masterInstTypes";
        });
        options = _filter1["masterInstTypes"];
        break;
      case "Worker Instance Type":
        const _filter2 = optionsData.find((option) => {
          return Object.keys(option)[0] === "workerInstTypes";
        });
        options = _filter2["workerInstTypes"];
        break;
      case "Image Name":
        const _filter3 = optionsData.find((option) => {
          return Object.keys(option)[0] === "imageName";
        });
        options = _filter3["imageName"];
        break;
      case "Dashboard":
        const _filter4 = optionsData.find((option) => {
          return Object.keys(option)[0] === "dashboard";
        });
        options = _filter4["dashboard"];
        break;
      case "Credentials":
        const _filter5 = optionsData.find((option) => {
          return Object.keys(option)[0] === "credentials";
        });
        options = _filter5 && _filter5["credentials"];
        break;
      case "Credential Type":
        const _filter6 = optionsData.find((option) => {
          return Object.keys(option)[0] === "credentialType";
        });
        options = _filter6["credentialType"];
        break;
    }

    return {
      header,
      name,
      value,
      options,
    };
  };
  addEventListener = (eventName, callBack) => {
    this.on(eventName, callBack);
  };
  removeEventListener = (eventName, callBack) => {
    this.removeListener(eventName, callBack);
  };
}

export default new DashBoardStore();
