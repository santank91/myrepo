import React, { Component } from "react";
import classNames from "classnames";

import DropDown from "../generic/Dropdown";
import Loader from "../loader/Loader";
import ClusterStore from "../../stores/ClusterStore";
import DashboardStore from "../../stores/DashBoardStore";
import ClusterActionCreator from "../../actionCreator/ClusterActionCreator";

import EventType from "../../constants/eventType";
import messages from "../../messges.json";

class CreateCluster extends Component {
  constructor(props) {
    super(props);
    this.state = this.getInitialState();
    this.state.lookupData = {
      provider: [],
      masterInstTypes: [],
      workerInstTypes: [],
      imageName: [],
      dashboard: [],
      credentials: [],
    };
    this.state.loading = true;
  }

  getInitialState = () => {
    return {
      nodeCount: "",
      owner:'',
      lead:'',
      clusterName: ""
      /* message: "",
      cloudSrvc: "AzureNative",
      masterCount: "1",
      masterSize: "Standard_B2s",
      nodeSize: "Standard_B1ms",
      credentials: "",
      imageName: "Ubuntu",
      kubeDashboard: "KubernetesDashboard",
      loggingEnabled: "Y",
      monitoringEnabled: "N",
      cloudSrvcMissing: false,
      masterCountMissing: false,
      clusterNameMissing: false,
      credentialsMissing: false, */
    };
  };

  clusterAdded = (clusterID) => {
    this.setState({
      nodeCount: "",
      clusterName: "",
      message: messages.CLUSTER.CLUSTER_CREATED + " : " + clusterID,
    });
  };

  loadLookupOptionsData = () => {
    const provider = DashboardStore.getDropdownData("Provider", "cloudSrvc");
    const masterInstTypes = DashboardStore.getDropdownData(
      "Master Instance Type",
      "masterSize"
    );
    const workerInstTypes = DashboardStore.getDropdownData(
      "Worker Instance Type",
      "nodeSize"
    );
    const imageName = DashboardStore.getDropdownData("Image Name", "imageName");
    const dashboard = DashboardStore.getDropdownData("Dashboard", "dashboard");
    const credentials = DashboardStore.getDropdownData(
      "Credentials",
      "credentials"
    );

    const lookupData = {
      provider,
      masterInstTypes,
      workerInstTypes,
      imageName,
      dashboard,
      credentials,
    };

    this.setState({
      loading: false,
      lookupData,
    });
  };

  clusterAddingFailed = () => {
    this.setState({
      message: messages.CLUSTER.SOMETHING_WRONG,
    });
  };

  componentDidMount() {
    this.loadLookupOptionsData();

    ClusterStore.addEventListener(
      EventType.CREATE_CLUSTER_SUCCESS,
      this.clusterAdded
    );
    ClusterStore.addEventListener(
      EventType.CREATE_CLUSTER_FAILED,
      this.clusterAddingFailed
    );
  }
  componentWillUnmount() {
    ClusterStore.removeEventListener(
      EventType.CREATE_CLUSTER_SUCCESS,
      this.clusterAdded
    );
    ClusterStore.removeEventListener(
      EventType.CREATE_CLUSTER_FAILED,
      this.clusterAddingFailed
    );
  }

  handleReset = (event) => {
    event.preventDefault();
    this.setState(this.getInitialState());
  };
  handleOnChange = (event) => {
    this.setState({
      [event.target.name]: event.target.value,
      [event.target.name + "Missing"]: false,
      message: "",
    });
  };
  handleOnProviderChange = (event) => {
    let nodeSize = "";
    switch (event.target.value) {
      case "AzureNative":
        nodeSize = "Standard_B1ms";
        break;
      case "AKS":
        nodeSize = "Standard_B2s";
        break;
      default:
        break;
    }
    this.setState({
      [event.target.name]: event.target.value,
      // [event.target.name + "Missing"]: !nodeSize,
      nodeSize,
    });
  };
  
  handleSubmit = (event) => {
    event.preventDefault();
    console.log('submit', this.state);
    const {clusterName, appID } = this.state;

    if (!clusterName) {
      this.setState({
        message: messages.CLUSTER.FIELD_MISSING,
        clusterNameMissing: !clusterName,
      });
      return false;
    }

    const requestParams = {/* 
      cloudSrvc: this.state.cloudSrvc,
      masterCount:
        this.state.cloudSrvc === "AzureNative"
          ? this.state.masterCount
          : undefined, 
      masterSize:
        this.state.cloudSrvc === "AzureNative"
          ? this.state.masterSize
          : undefined,
      nodeSize: this.state.nodeSize,
      imageName: this.state.imageName ? this.state.imageName : "Ubuntu",
      kubeDashboard: this.state.kubeDashboard,
      loggingEnabled: this.state.loggingEnabled,
      monitoringEnabled: this.state.monitoringEnabled,
      credentialName: this.state.credentials,*/
      kubeDashboard: this.state.kubeDashboard,
      nodeCount: this.state.nodeCount ? this.state.nodeCount : Date.now(),
      clusterName: this.state.clusterName,
      owner: this.state.owner,
      lead:this.state.lead,
    };
    console.log('asd',requestParams)

    Object.keys(requestParams).map(
      (key) => requestParams[key] === undefined && delete requestParams[key]
    );

    ClusterActionCreator.createCluster(requestParams);
  };

  render() {
    if (this.state.loading || !this.state.lookupData.provider) {
      return <Loader />;
    }
    return (
      <div className="container-fluid">
        <div className="row page-titles">
          <div className="col-md-5 col-8 align-self-center">
            <h3 className="text-themecolor">Application Creation</h3>
          </div>
        </div>
        <div className="row">
          <div className="col-lg-12">
            <div className="card">
              <div className="card-block">
                <h3 className="card-title">Create Application</h3>
                <button
                  id={"backToStatus"}
                  onClick={this.props.onClick}
                  className="btn pull-right btn-danger"
                >
                  Back To Details
                </button>
                <div className="table-responsive">
                  <form className="form-horizontal form-material">
                    
                    <div className="form-group">
                      <label className="col-md-12 required">Application Name</label>
                      <div className="col-md-12">
                        <input
                          type="text"
                          name="clusterName"
                          required
                          value={this.state.clusterName}
                          onChange={this.handleOnChange}
                          className={classNames(
                            "form-control form-control-line",
                            this.state.clusterNameMissing ? "mandatory" : ""
                          )}
                        />
                      </div>
                    </div>
                    
                    <div className="form-group">
                      <label className="col-md-12 required">
                        Application ID
                      </label>
                      <div className="col-md-12">
                        <input
                          name="nodeCount"
                          type="text"
                          required
                          value={this.state.nodeCount}
                          onChange={this.handleOnChange}
                          className={classNames(
                            "form-control form-control-line",
                            this.state.nodeCountMissing ? "mandatory" : ""
                          )}
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <label className="col-md-12 required">Owner</label>
                      <div className="col-md-12">
                        <input
                          type="text"
                          name="owner"
                          required
                          value={this.state.owner}
                          onChange={this.handleOnChange}
                          className={classNames(
                            "form-control form-control-line",
                            this.state.clusterNameMissing ? "mandatory" : ""
                          )}
                        />
                      </div>
                    </div>

                    <div className="form-group">
                      <label className="col-md-12 required">App Lead</label>
                      <div className="col-md-12">
                        <input
                          type="text"
                          name="lead"
                          required
                          value={this.state.lead}
                          onChange={this.handleOnChange}
                          className={classNames(
                            "form-control form-control-line",
                            this.state.clusterNameMissing ? "mandatory" : ""
                          )}
                        />
                      </div>
                    </div>
                    
{/*                     <DropDown
                      data={this.state.lookupData.credentials}
                      value={this.state.credentials}
                      onChange={this.handleOnChange}
                      mandatory={this.state.credentialsMissing}
                      required={true}
                    /> */}

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

export default CreateCluster;
