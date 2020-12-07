import React, {Component} from 'react';
import {connect} from "dva";
import 'bootstrap/dist/css/bootstrap.min.css';

@connect(({globalModel}) => ({globalModel}))
class Cabinet extends Component {
  render() {
    const {currentUser, isAdmin, isModer} = this.props.globalModel;
    console.log(currentUser);
    console.log(isAdmin);
    console.log(isModer);
    return (
      <div>
        <h1>Home page</h1>
        {currentUser ? <h1>Mening navbatlarim</h1> : ''}
      </div>
    );
  }
}

Cabinet.propTypes = {};

export default Cabinet;
