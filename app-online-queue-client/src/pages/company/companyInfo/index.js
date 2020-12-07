import React, {Component} from 'react';
import {connect} from "dva";


@connect(({globalModel}) => ({globalModel}))
class companyInfo extends Component {
  render() {
    return (
      <div>

      </div>
    );
  }
}

companyInfo.propTypes = {};

export default companyInfo;
