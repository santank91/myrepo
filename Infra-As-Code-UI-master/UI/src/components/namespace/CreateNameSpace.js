import React, { Component } from "react";
import classNames from "classnames";
import Loader from "../loader/Loader";
import TextBox from "../generic/TextBox";
import ClusterStore from "../../stores/ClusterStore";
import DashboardStore from "../../stores/DashBoardStore";
import ClusterActionCreator from "../../actionCreator/ClusterActionCreator";

import EventType from "../../constants/eventType";
import messages from "../../messges.json";

class CreateNameSpace extends Component {
  constructor(props) {
    super(props);
    this.state = this.getInitialState();
    this.state.loading = false;
  }

  getInitialState = () => {
    return {
      message: "",
      namespaceName: "",
      clusterName: "",
      applicationLabelName: "",
      applicationPort: "",
      minReqMem: "",
      maxReqMem: "",
      minReqCPU: "",
      maxReqCPU: "",
      maxPod: "",
      maxClaims: "",
      maxStorage: "",
      minPodStorage: "",
      maxPodStorage: "",
      minPodCPU: "",
      maxPodCPU: "",
      minPodMem: "",
      maxPodMem: "",
    };
  };

  componentDidMount() {}
  componentWillUnmount() {}

  handleReset = (event) => {
    event.preventDefault();
    this.setState(this.getInitialState());
  };
  handleOnChange = (event) => {
    this.setState({
      [event.target.id]: event.target.value,
      message: "",
    });
  };
  handleSubmit = (event) => {
    event.preventDefault();
    // const { nodeCount, clusterName, cloudSrvc, credentialName } = this.state;

    // if (!nodeCount || !clusterName || !cloudSrvc) {
    //   // || !credentialName) { TODO - correct when lookupoption api return the value
    //   this.setState({
    //     message: messages.CLUSTER.FIELD_MISSING,
    //     nodeCountMissing: !nodeCount,
    //     cloudSrvcMissing: !cloudSrvc,
    //     clusterNameMissing: !clusterName,
    //     // credentialsMissing: !credentialName -- TODO: waiting for api correction
    //   });
    //   return false;
    // }

    // const requestParams = {
    //   cloudSrvc: this.state.cloudSrvc,
    //   masterCount:
    //     this.state.cloudSrvc === "Azure" ? this.state.masterCount : undefined,
    //   nodeCount: this.state.nodeCount,
    //   masterSize:
    //     this.state.cloudSrvc === "Azure" ? this.state.masterSize : undefined,
    //   nodeSize: this.state.nodeSize,
    //   clusterName: this.state.clusterName,
    //   imageName: this.state.imageName,
    //   kubeDashboard: this.state.kubeDashboard,
    //   loggingEnabled: this.state.loggingEnabled,
    //   monitoringEnabled: this.state.monitoringEnabled,
    //   credentialName: this.state.credentials,
    // };
    // Object.keys(requestParams).map(
    //   (key) => requestParams[key] === undefined && delete requestParams[key]
    // );
    // ClusterActionCreator.createCluster(requestParams);
  };

  render() {
    if (this.state.loading) {
      return <Loader />;
    }
    return (
      <div className="container-fluid">
        <div className="row page-titles">
          <div className="col-md-5 col-8 align-self-center">
            <h3 className="text-themecolor">Resource Creation</h3>
          </div>
        </div>
        <div className="row">
          <div className="col-lg-12">
            <div className="card">
              <div className="card-block">
                <h4 className="card-title">Resource Creation</h4>
                <div className="table-responsive">
                  <form className="form-horizontal form-material">
                    <TextBox
                      id={"namespaceName"}
                      labelName={"Namespace Name"}
                      onChange={this.handleOnChange}
                      value={this.state.namespaceName}
                      required={true}
                    />
                    <TextBox
                      id={"clusterName"}
                      labelName={"Cluster Name"}
                      onChange={this.handleOnChange}
                      value={this.state.clusterName}
                      required={true}
                    />
                    <TextBox
                      id={"applicationLabelName"}
                      labelName={"Application Label Name"}
                      onChange={this.handleOnChange}
                      value={this.state.applicationLabelName}
                      required={true}
                    />
                    <TextBox
                      id={"applicationPort"}
                      labelName={"Application Port"}
                      onChange={this.handleOnChange}
                      value={this.state.applicationPort}
                      required={true}
                    />
                    <TextBox
                      id={"minReqMem"}
                      labelName={"minReqMem"}
                      onChange={this.handleOnChange}
                      value={this.state.minReqMem}
                      required={true}
                    />
                    <TextBox
                      id={"maxReqMem"}
                      labelName={"maxReqMem"}
                      onChange={this.handleOnChange}
                      value={this.state.maxReqMem}
                      required={true}
                    />
                    <TextBox
                      id={"minReqCPU"}
                      labelName={"minReqCPU"}
                      onChange={this.handleOnChange}
                      value={this.state.minReqCPU}
                      required={true}
                    />
                    <TextBox
                      id={"maxReqCPU"}
                      labelName={"maxReqCPU"}
                      onChange={this.handleOnChange}
                      value={this.state.maxReqCPU}
                      required={true}
                    />
                    <TextBox
                      id={"maxPod"}
                      labelName={"maxPod"}
                      onChange={this.handleOnChange}
                      value={this.state.maxPod}
                      required={true}
                    />
                    <TextBox
                      id={"maxClaims"}
                      labelName={"maxClaims"}
                      onChange={this.handleOnChange}
                      value={this.state.maxClaims}
                      required={true}
                    />
                    <TextBox
                      id={"maxStorage"}
                      labelName={"maxStorage"}
                      onChange={this.handleOnChange}
                      value={this.state.maxStorage}
                      required={true}
                    />
                    <TextBox
                      id={"minPodStorage"}
                      labelName={"minPodStorage"}
                      onChange={this.handleOnChange}
                      value={this.state.minPodStorage}
                      required={true}
                    />
                    <TextBox
                      id={"maxPodStorage"}
                      labelName={"maxPodStorage"}
                      onChange={this.handleOnChange}
                      value={this.state.maxPodStorage}
                      required={true}
                    />
                    <TextBox
                      id={"minPodCPU"}
                      labelName={"minPodCPU"}
                      onChange={this.handleOnChange}
                      value={this.state.minPodCPU}
                      required={true}
                    />
                    <TextBox
                      id={"maxPodCPU"}
                      labelName={"maxPodCPU"}
                      onChange={this.handleOnChange}
                      value={this.state.maxPodCPU}
                      required={true}
                    />
                    <TextBox
                      id={"minPodMem"}
                      labelName={"minPodMem"}
                      onChange={this.handleOnChange}
                      value={this.state.minPodMem}
                      required={true}
                    />
                    <TextBox
                      id={"maxPodMem"}
                      labelName={"maxPodMem"}
                      onChange={this.handleOnChange}
                      value={this.state.maxPodMem}
                      required={true}
                    />

                    <div className="form-group float-left">
                      <div className="col-sm-10">
                        <button
                          onClick={this.handleSubmit}
                          className="btn btn-success"
                        >
                          Submit
                        </button>
                      </div>
                    </div>
                    <div className="form-group">
                      <div className="col-sm-10">
                        <button
                          onClick={this.handleReset}
                          className="btn btn-danger"
                        >
                          Reset
                        </button>
                      </div>
                    </div>
                    <div className="form-group">
                      <h5 className="text-themecolor">{this.state.message}</h5>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default CreateNameSpace;
