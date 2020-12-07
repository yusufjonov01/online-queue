import React, {Component} from 'react';
import PropTypes from 'prop-types';
import ProfilForm from "../../components/ProfilForm";
import {Button, Col, Container, ListGroup, ListGroupItem, Row} from "reactstrap";
import {AvField, AvForm} from "availity-reactstrap-validation";
import {connect} from "dva";
import DashboardSidebar from "../../components/DashboardSidebar";
import DashboardLayout from "../../components/DashboardLayout";
import {toast} from "react-toastify";

@connect(({globalModel}) => ({globalModel}))
class Sozlamalar extends Component {
  render() {
    const {dispatch, globalModel} = this.props;
    const {currentUser, isAdmin, isModer, isReception, isOperator, isDirector, message} = globalModel;
    const editInformation = (e, v) => {
      if (v.password === v.prePassword) {
        dispatch({
          type: 'globalModel/editPassword',
          payload:
          v
        })
      }else {
        toast.error("Takroriy parollarda xatolik qaytadan kiriting")
      }
    };
    return (

      <div>
        {isAdmin || isModer || isReception || isOperator ? <DashboardLayout pathname={this.props.pathname}>
            <Container>
              <Row className="profil-form">
                <h3 className="h">Malu'motlarni o'zgartirish</h3>
                <ListGroupItem className={isReception || isOperator || isDirector||isAdmin ? "d-block prof" : "d-none"}>
                  <AvForm onValidSubmit={editInformation}>
                    <Col>
                      <AvField className="av" type="password" label="Enter your old password" name="oldPassword" required
                               placeholder="Enter your old password"/>
                      <AvField className="av" type="password" label="Enter your new password" name="password" required
                               placeholder="Enter your new password"/>
                      <AvField className="av" type="password" label="Enter your new pre password" name="prePassword"
                               required
                               placeholder="Enter your new pre password"/>
                      <Button className="but">O'zgartirish</Button>
                    </Col>
                  </AvForm>
                </ListGroupItem>
              </Row>
            </Container>
          </DashboardLayout> :
          ""
        }
      </div>
    );
  }
}
Sozlamalar.propTypes = {};
export default Sozlamalar;
