import React, { Component } from "react";
import Header from "./Header";
import Footer from "./Footer";
import Sidebar from "./sideBar/Sidebar";
import PageWrapper from "./PageWrapper";
import Loader from "./loader/Loader";
import DashboardActionCreator from "../actionCreator/DashboardActionCreator";
import DashboardStore from "../stores/DashBoardStore";
import EventType from "../constants/eventType";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: true,
    };
  }
  lookupOptionDataLoaded = () => {
    this.setState({
      loading: false,
    });
  };
  componentDidMount() {
    console.log(this.state);
    DashboardActionCreator.loadOptionsData();
    DashboardStore.addEventListener(
      EventType.GET_LOOKUP_OPTIONS_DATA_SUCCESS,
      this.lookupOptionDataLoaded
    );
  }
  componentWillUnmount() {
    DashboardStore.removeEventListener(
      EventType.GET_LOOKUP_OPTIONS_DATA_SUCCESS,
      this.lookupOptionDataLoaded
    );
  }
  render() {
    const toRender = this.state.loading ? (
      <div id="main-wrapper">
        <Header />
        <Loader />
      </div>
    ) : (
      <div id="main-wrapper">
        <Header />
        <Footer />
        <PageWrapper />
        <Sidebar />
      </div>
    );
    return toRender;
  }
}

export default App;
