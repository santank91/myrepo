import { EventEmitter } from "events";
import Dispatcher from "../dispatcher";
import ActionType from "../constants/actionType";
import EventType from "../constants/eventType";

class ClusterStore extends EventEmitter {
  constructor() {
    super();
    Dispatcher.register(this.registerToActions);
    this.items = ["santhosh", "kumar"];
  }
  registerToActions = (action) => {
    switch (action.actionType) {
      case ActionType.CREATE_CLUSTER:
        this.emit(EventType.CREATE_CLUSTER_SUCCESS, action.value);
        break;
      case ActionType.CREATE_CLUSTER_FAILED:
        this.emit(EventType.CREATE_CLUSTER_FAILED);
        break;
      // case ActionType.GET_LOOKUP_OPTIONS_DATA:
      //   this.lookupOptionData = action.value;
      //   this.emit(
      //     EventType.GET_LOOKUP_OPTIONS_DATA_SUCCESS,
      //     this.lookupOptionData
      //   );
      //   break;
      // case ActionType.GET_LOOKUP_OPTIONS_DATA_FAILED:
      //   this.emit(EventType.CREATE_CLUSTER_FAILED);
      //   break;
      default:
        break;
    }
  };

  addEventListener = (eventName, callBack) => {
    this.on(eventName, callBack);
  };
  removeEventListener = (eventName, callBack) => {
    this.removeListener(eventName, callBack);
  };
}

export default new ClusterStore();
