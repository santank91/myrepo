import React, { Component } from "react";
import MenuItem from "./MenuItem";

class SideBar extends Component {
  render() {
    return (
      <aside className="left-sidebar">
        <div className="slimScrollDiv">
          <div className="scroll-sidebar">
            <nav className="sidebar-nav active">
              <ul id="sidebarnav" className="in">
                <MenuItem item="Menuitem" />
              </ul>
            </nav>
          </div>
          <div className="slimScrollBar"></div>
          <div className="slimScrollRail"></div>
        </div>

        <div className="sidebar-footer">
          <a
            className="link"
            data-toggle="tooltip"
            title=""
            data-original-title="Settings"
          >
            <i className="mdi mdi-settings"></i>
          </a>
          <a
            className="link"
            data-toggle="tooltip"
            title=""
            data-original-title="Profile Edit"
          >
            <i className="fa fa-edit"></i>
          </a>
          <a
            className="link"
            data-toggle="tooltip"
            title=""
            data-original-title="Logout"
          >
            <i className="mdi mdi-power"></i>
          </a>
        </div>
      </aside>
    );
  }
}

export default SideBar;
