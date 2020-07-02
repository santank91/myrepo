import React, { Component } from "react";

class Header extends Component {
  render() {
    return (
      <header className="topbar is_stuck">
        <nav className="navbar top-navbar navbar-toggleable-sm navbar-light">
          <div className="navbar-header">
            <a className="navbar-brand">
              <b>
                <img
                  height="25px"
                  width="25px"
                  
                  src="../src/styles/images/kubernetes_1.png"
                  alt="homepage"
                  className="light-logo"
                  
                />
              </b>
            </a>
          </div>

          <div className="col-md-5 col-8 align-self-center">
            <h3 style={{color: 'white'}}>Infosys IaaC Platform</h3>
          </div>
        
          <div className="navbar-collapse">
            <ul className="navbar-nav mr-auto mt-md-0">
              
            </ul>

            <ul className="navbar-nav my-lg-0">
              <li className="nav-item dropdown">
                <a
                  className="nav-link dropdown-toggle text-muted waves-effect waves-dark"
                  data-toggle="dropdown"
                  aria-haspopup="true"
                  aria-expanded="false"
                >
                  <img
                    src="../src/styles/images/users/profile.png"
                    alt="user"
                    className="profile-pic m-r-10"
                  />
                  User
                </a>
              </li>
            </ul>
          </div>
        </nav>
      </header>
    );
  }
}

export default Header;
