import { EventEmitter } from "events";
import Dispatcher from "../dispatcher";
import ActionType from "../constants/actionType";
import EventType from "../constants/eventType";

class NameSpaceStore extends EventEmitter {
  const;
  constructor() {
    super();
    Dispatcher.register(this.registerToActions);
  }

  registerToActions = (action) => {
    switch (action.actionType) {
      case ActionType.CREATE_NAMESPACE_SUCCESS:
        this.emit(EventType.CREATE_NAMESPACE_SUCCESS, action.value);
        break;
      case ActionType.CREATE_SECURITY_FAILED:
        this.emit(EventType.CREATE_NAMESPACE_FAILED);
        break;
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

export default new NameSpaceStore();
