import React, { Component } from "react";
import classNames from "classnames";

import DropDown from "../generic/Dropdown";
import Loader from "../loader/Loader";
import ClusterStore from "../../stores/ClusterStore";
import DashboardStore from "../../stores/DashBoardStore";
import ClusterActionCreator from "../../actionCreator/ClusterActionCreator";

import EventType from "../../constants/eventType";
import messages from "../../messges.json";
import Iframe from "../iframe/Iframe";

class K8DashBoard extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: true,
    };
  }
  onIframeLoad = () => {
    this.setState({
      loading: false,
    });
  };
  render() {
    const { url, onClick } = this.props;
    const { loading } = this.state;
    return (
      <div className="container-fluid">
        <div className="row page-titles">
          <div className="col-md-5 col-8 align-self-center">
            <h3 className="text-themecolor">Cluster Management</h3>
          </div>
        </div>
        <div className="row">
          <div className="col-lg-12">
            <div className="card">
              <div className="card-block">
                <h4 className="card-title">Cluster Management</h4>
                <button
                  id={"backToStatus"}
                  onClick={onClick}
                  className="btn pull-right btn-danger"
                >
                  Back to Status
                </button>
                <div className="table-responsive">
                  {loading ? <Loader /> : null}
                  <Iframe url={url} onLoad={this.onIframeLoad} />;
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default K8DashBoard;
