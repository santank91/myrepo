import React, { Component } from "react";
import classNames from "classnames";

import DropDown from "../generic/Dropdown";
import Loader from "../loader/Loader";
import ClusterStore from "../../stores/ClusterStore";
import DashboardStore from "../../stores/DashBoardStore";
import ClusterActionCreator from "../../actionCreator/ClusterActionCreator";

import EventType from "../../constants/eventType";
import messages from "../../messges.json";

class ClusterStatus extends Component {
  constructor(props) {
    super(props);
  }
  getClusterList = () => {
    const { data, loading, navigate } = this.props;
    if (!data || data.length === 0) {
      return loading ? <Loader position={true} /> : null;
    }
    const toRender = data.map((value, index) => {
      const link = value.k8sDashboardUrl ? (
        <a
          data-url={value.k8sDashboardUrl}
          className="underline"
          onClick={this.props.navigate}
          href="#"
        >
          {value.clusterName}
        </a>
      ) : (
        value.clusterName
      );
      let status = (
        <img
          height="25px"
          title={value.createStatus}
          src="../src/styles/images/inProgress.png"
          alt="inProgress"
        ></img>
      );
      switch (value.createStatus) {
        case "Ready":
          status = (
            <img
              height="25px"
              title={value.createStatus}
              src="../src/styles/images/ready.png"
              alt="inProgress"
            ></img>
          );
          break;
        case "InProgress":
          status = (
            <img
              height="25px"
              title={value.createStatus}
              src="../src/styles/images/inProgress_1.png"
              alt="inProgress"
            ></img>
          );
          break;
        case "Failure":
          status = (
            <img
              height="25px"
              title={value.createStatus}
              src="../src/styles/images/failed.png"
              alt="inProgress"
            ></img>
          );
          break;
        default:
          break;
      }
      return (
        <tr key={index}>
          <td>{link}</td>
          <td>{status}</td>
          <td>{value.deployments}</td>
          <td>{value.utilization}</td>
          <td>{value.alerts}</td>
        </tr>
      );
    });
    return toRender;
  };

  render() {
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
                <h4 className="card-title">Application Management</h4>
                <div className="col-md-6 pull-left">
                  <input
                    type="text"
                    placeholder="Search for Name or Status"
                    className="form-control form-control-line"
                    onChange={this.props.onChange}
                    value={this.props.searchValue}
                  />
                </div>
                <button
                  id={"createCluster"}
                  onClick={this.props.onClick}
                  className="btn pull-right btn-danger"
                >
                  Create Application
                </button>
                <div className="table-responsive">
                  <table className="table">
                    <thead>
                      <tr>
                        <th>Application Name</th>
                        <th>Status</th>
                        <th>Deployments</th>
                        <th>Utilization</th>
                        <th>Alerts</th>
                      </tr>
                    </thead>
                    <tbody>{this.getClusterList()}</tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default ClusterStatus;
