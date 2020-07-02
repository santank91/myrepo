import React, { Component } from "react";
import classNames from "classnames";

class TextBox extends Component {
  constructor(props) {
    super(props);
  }
  render() {
    const { labelName, onChange, value, required, isPassword, id, showEyeIcon, eyeIcon } = this.props;
    return (
      <div className="form-group">
        <label className={classNames("col-md-12", !required ? "" : "required")}>
          {labelName}
        </label>
        <div className="col-md-12">
          <input
            type={isPassword ? "password" : "text"}
            id={id}
            value={value}
            onChange={onChange}
            className={classNames(
              "form-control form-control-line",
              required && !value ? "mandatory" : ""
            )}
          />
          {showEyeIcon ? eyeIcon : null}
        </div>
      </div>
    );
  }
}

export default TextBox;
