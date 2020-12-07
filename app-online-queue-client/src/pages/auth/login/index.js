import React, {Component} from 'react';
import {AvField, AvForm} from 'availity-reactstrap-validation'
import {connect} from "dva";
import {Button, Col, Container, CustomInput, NavLink, Row} from "reactstrap";

@connect(({globalModel}) => ({globalModel}))
class Login extends Component {
  render() {
    const login = (e, v) => {
      console.log(v);

      this.props.dispatch({
        type: 'globalModel/login',
        payload: {
          ...v
        }
      })
    };


    return (
      <Container className="login-page" fluid>
        <Row className="login-row">
          <div className="col-md-4 col-10 offset-1 offset-md-4">
            <h1 className="card-body text-center text-primary">Log in</h1>
            <p className="text-center text-white-75">Programming is thinking, not typing</p>
            <AvForm onValidSubmit={login}>
              <AvField className="phone-number" name="phoneNumber" placeholder="    Phone number"/>
              <AvField className="login-password" type="password" name="password" placeholder="    Password"/>
              <Row className="login-row2">
                <Col className="col-6"><CustomInput type="checkbox" label="Remember Me" id="aa"/></Col>
                <Col className="col-6 float-right"><NavLink href="#" className="text-primary pt-0">Forgot
                  password?</NavLink></Col>
              </Row>
              <button className="btn btn-primary btn-block">Login</button>
              <div className="justify-content-center d-flex align-content-center">
                <h6 className="text-black-50 text-center">Don't have an account? </h6>
                <NavLink href="#" className="text-primary pt-0">Sign up</NavLink>
              </div>
            </AvForm>
          </div>
        </Row>
      </Container>
    );
  }
}

Login.propTypes = {};

export default Login;
